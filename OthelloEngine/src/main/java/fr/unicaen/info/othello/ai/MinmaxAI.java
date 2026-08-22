package fr.unicaen.info.othello.ai;

import fr.unicaen.info.othello.actions.Action;
import fr.unicaen.info.othello.actions.Move;
import fr.unicaen.info.othello.actions.RemoveLine;
import fr.unicaen.info.othello.state.IState;
import fr.unicaen.info.othello.state.Team;
import fr.unicaen.info.othello.tokens.Pawn;
import fr.unicaen.info.othello.tokens.Ring;
import fr.unicaen.info.othello.tokens.Token;
import org.Coordinate.Coordinate;
import org.Enums.Direction;
import org.Enums.Mode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implémentation de l'algorithme Minimax avec élagage Alpha-Beta.
 * L'IA explore l'arbre des états jusqu'à une profondeur {@code DEPTH}
 * et choisit l'action maximisant ses chances de victoire.
 */
public class MinmaxAI implements AI {

    /** Profondeur maximale de l'exploration Minimax. */
    private static final int DEPTH = 3;

    /** L'équipe que joue cette IA. Mise à jour à chaque appel de {@link #chooseMove}. */
    private Team aiTeam = Team.BLACK;

    /** Construit une IA sans équipe prédéfinie (déterminée au premier appel). */
    public MinmaxAI() {}

    /**
     * Construit une IA pour l'équipe donnée.
     *
     * @param aiTeam l'équipe que joue cette IA.
     */
    public MinmaxAI(Team aiTeam) {
        this.aiTeam = aiTeam;
    }

    /**
     * Choisit la meilleure action pour l'état donné via l'algorithme Minimax.
     *
     * @param state l'état courant du jeu.
     * @return la meilleure action trouvée, ou {@code null} si aucun coup n'est disponible.
     */
    @Override
    public Action chooseMove(IState state) {
        aiTeam = state.turn();
        Node root = new Node(state, null, null);
        List<Action> actions = getAllActions(state);

        Action bestAction = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

        for (Action action : actions) {
            IState next = applyAction(state, action);
            Node child = new Node(next, root, action);
            boolean nextIsMax = determineNextIsMax(next, true);
            double score = minimax(child, DEPTH - 1, alpha, beta, nextIsMax);

            if (score > bestScore) {
                bestScore = score;
                bestAction = action;
            }
            alpha = Math.max(alpha, bestScore);
        }

        return bestAction;
    }

    /**
     * Algorithme Minimax récursif avec élagage Alpha-Beta.
     *
     * @param node   le nœud courant.
     * @param depth  la profondeur restante.
     * @param alpha  la meilleure valeur garantie pour MAX.
     * @param beta   la meilleure valeur garantie pour MIN.
     * @param isMax  {@code true} si c'est un nœud MAX, {@code false} pour MIN.
     * @return le score évalué pour ce nœud.
     */
    private double minimax(Node node, int depth, double alpha, double beta, boolean isMax) {
        IState state = node.getState();

        if (depth == 0 || state.isOver()) {
            return evaluate(state);
        }

        List<Action> actions = getAllActions(state);
        if (actions.isEmpty()) {
            return evaluate(state);
        }

        if (isMax) {
            double best = Double.NEGATIVE_INFINITY;
            for (Action action : actions) {
                IState next = applyAction(state, action);
                Node child = new Node(next, node, action);
                boolean nextIsMax = determineNextIsMax(next, isMax);
                double score = minimax(child, depth - 1, alpha, beta, nextIsMax);
                best = Math.max(best, score);
                alpha = Math.max(alpha, best);
                if (alpha >= beta) break;
            }
            return best;
        }

        double best = Double.POSITIVE_INFINITY;
        for (Action action : actions) {
            IState next = applyAction(state, action);
            Node child = new Node(next, node, action);
            boolean nextIsMax = determineNextIsMax(next, isMax);
            double score = minimax(child, depth - 1, alpha, beta, nextIsMax);
            best = Math.min(best, score);
            beta = Math.min(beta, best);
            if (alpha >= beta) break;
        }
        return best;
    }

    /**
     * Évalue le score d'un état terminal ou d'une feuille de l'arbre.
     * Combine plusieurs critères pondérés : victoire, anneaux retirés,
     * alignements de pions et mobilité.
     *
     * @param state l'état à évaluer.
     * @return le score de l'état (positif = favorable à l'IA, négatif = défavorable).
     */
    private double evaluate(IState state) {
        Team winner = state.winner();
        if (winner == aiTeam) return 100000;
        if (winner != null) return -100000;

        Team opponent = aiTeam.opposite();
        double score = 0;

        score += getRemovedRings(state, aiTeam)       * 1000;
        score += countAlignedPawns(state, aiTeam, 4)  * 50;
        score += countAlignedPawns(state, aiTeam, 3)  * 10;
        score += countAvailableMoves(state, aiTeam)   * 0.5;

        score -= getRemovedRings(state, opponent)      * 1000;
        score -= countAlignedPawns(state, opponent, 4) * 50;
        score -= countAlignedPawns(state, opponent, 3) * 10;
        score -= countAvailableMoves(state, opponent)  * 0.5;

        return score;
    }

    /**
     * Renvoie toutes les actions légales disponibles pour l'état donné.
     * Si des lignes sont présentes, renvoie uniquement des actions {@link RemoveLine}.
     * Sinon, renvoie les actions {@link Move} possibles.
     *
     * @param state l'état courant.
     * @return la liste des actions légales.
     */
    private List<Action> getAllActions(IState state) {
        List<Action> actions = new ArrayList<>();

        if (!state.lines().isEmpty()) {
            for (Set<Coordinate> line : state.lines()) {
                Team lineTeam = getLineTeam(state, line);
                if (lineTeam == null) continue;
                for (Coordinate ring : getRingsOfTeam(state, lineTeam)) {
                    actions.add(new RemoveLine(line, ring));
                }
            }
            return actions;
        }

        for (Coordinate ring : getRingsOfTeam(state, state.turn())) {
            for (Coordinate destination : state.availableMoves(ring)) {
                actions.add(new Move(ring, destination));
            }
        }

        return actions;
    }

    /**
     * Applique une action à l'état donné et renvoie le nouvel état.
     *
     * @param state  l'état courant.
     * @param action l'action à appliquer.
     * @return le nouvel état après l'action.
     * @throws IllegalArgumentException si le type d'action est inconnu.
     */
    public IState applyAction(IState state, Action action) {
        if (action instanceof Move move) return state.move(move);
        if (action instanceof RemoveLine remove) return state.removeLine(remove);
        throw new IllegalArgumentException("Type d'action inconnu : " + action.getClass().getName());
    }

    /**
     * Détermine si le prochain nœud est un nœud MAX ou MIN.
     * Si des lignes sont présentes, le même joueur rejoue.
     *
     * @param after        l'état après l'action.
     * @param currentIsMax {@code true} si le nœud courant est MAX.
     * @return {@code true} si le prochain nœud est MAX.
     */
    private boolean determineNextIsMax(IState after, boolean currentIsMax) {
        if (!after.lines().isEmpty()) return currentIsMax;
        return !currentIsMax;
    }

    /**
     * Renvoie le nombre d'anneaux retirés par l'équipe donnée.
     *
     * @param state l'état courant.
     * @param team  l'équipe concernée.
     * @return le nombre d'anneaux retirés (5 - anneaux restants).
     */
    private int getRemovedRings(IState state, Team team) {
        return 5 - getRingsOfTeam(state, team).size();
    }

    /**
     * Compte le nombre d'alignements d'au moins {@code n} pions de l'équipe donnée.
     *
     * @param state l'état courant.
     * @param team  l'équipe concernée.
     * @param n     le nombre minimum de pions alignés.
     * @return le nombre d'alignements de longueur >= n.
     */
    private int countAlignedPawns(IState state, Team team, int n) {
        Map<Coordinate, Token> board = state.board();
        List<Direction> halfDirections = List.of(Direction.E, Direction.NE, Direction.SE);
        Set<Coordinate> pawnSet = new HashSet<>();

        for (Map.Entry<Coordinate, Token> entry : board.entrySet()) {
            if (entry.getValue() instanceof Pawn pawn && pawn.getTeam() == team) {
                pawnSet.add(entry.getKey());
            }
        }

        int count = 0;
        for (Coordinate start : pawnSet) {
            for (Direction dir : halfDirections) {
                int aligned = 1;
                Coordinate current = start;
                while (true) {
                    try {
                        Coordinate next = current.toDir(Mode.POINTY, dir);
                        if (pawnSet.contains(next)) { aligned++; current = next; }
                        else break;
                    } catch (Exception e) { break; }
                }
                if (aligned >= n) count++;
            }
        }
        return count;
    }

    /**
     * Calcule le nombre total de mouvements disponibles pour l'équipe donnée.
     *
     * @param state l'état courant.
     * @param team  l'équipe concernée.
     * @return la somme des mouvements possibles pour tous les anneaux de l'équipe.
     */
    private int countAvailableMoves(IState state, Team team) {
        return getRingsOfTeam(state, team).stream()
                .mapToInt(ring -> state.availableMoves(ring).size())
                .sum();
    }

    /**
     * Renvoie la liste des coordonnées des anneaux de l'équipe donnée.
     *
     * @param state l'état courant.
     * @param team  l'équipe concernée.
     * @return la liste des coordonnées des anneaux.
     */
    private List<Coordinate> getRingsOfTeam(IState state, Team team) {
        return state.board().entrySet().stream()
                .filter(entry -> entry.getValue() instanceof Ring ring && ring.getTeam() == team)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Renvoie l'équipe des pions d'une ligne donnée.
     *
     * @param state l'état courant.
     * @param line  l'ensemble des coordonnées de la ligne.
     * @return l'équipe des pions, ou {@code null} si la ligne est vide.
     */
    private Team getLineTeam(IState state, Set<Coordinate> line) {
        for (Coordinate coordinate : line) {
            Token token = state.board().get(coordinate);
            if (token instanceof Pawn pawn) return pawn.getTeam();
        }
        return null;
    }
}