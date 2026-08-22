package fr.unicaen.info.othello.state;

import fr.unicaen.info.othello.actions.Move;
import fr.unicaen.info.othello.actions.RemoveLine;
import fr.unicaen.info.othello.tokens.Pawn;
import fr.unicaen.info.othello.tokens.Ring;
import fr.unicaen.info.othello.tokens.Token;
import org.Coordinate.Coordinate;
import org.Enums.Mode;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Représente une situation de jeu de manière immuable (Record Java).
 * La {@link java.util.HashMap} du plateau associe chaque coordonnée à son token,
 * ou {@code null} si la case est vide.
 * Toute modification renvoie un nouvel état sans altérer l'état courant.
 */
public record State(
        Map<Coordinate, Token> board,
        Team turn,
        List<Set<Coordinate>> lines
) implements IState {

    /** Mode par défaut utilisé pour les calculs de directions et de lignes. */
    private static final Mode DEFAULT_MODE = Mode.POINTY;

    /**
     * Constructeur compact : effectue une copie défensive du plateau et des lignes
     * pour garantir l'immuabilité du record.
     */
    public State {
        board = new HashMap<>(board);
        lines = new ArrayList<>(lines);
    }

    // -------------------------------------------------------------------------
    // Getters utilitaires
    // -------------------------------------------------------------------------

    /**
     * Renvoie le token présent à la coordonnée donnée.
     *
     * @param coordinate la coordonnée à interroger.
     * @return le token présent, ou {@code null} si la case est vide.
     */
    public Token getToken(Coordinate coordinate) {
        return board.get(coordinate);
    }

    /**
     * Renvoie {@code true} si aucun token n'est présent à la coordonnée donnée.
     *
     * @param coordinate la coordonnée à vérifier.
     * @return {@code true} si la case est vide.
     */
    public boolean isEmpty(Coordinate coordinate) {
        return board.get(coordinate) == null;
    }

    /**
     * Renvoie l'ensemble de toutes les coordonnées du terrain.
     *
     * @return l'ensemble des coordonnées valides du plateau.
     */
    public Set<Coordinate> getCoordinates() {
        return board.keySet();
    }

    /**
     * Renvoie {@code true} si la coordonnée fait partie des cases valides du terrain.
     *
     * @param c la coordonnée à vérifier.
     * @return {@code true} si la coordonnée est dans le terrain.
     */
    public boolean isInField(Coordinate c) {
        return board.containsKey(c);
    }

    // -------------------------------------------------------------------------
    // rings
    // -------------------------------------------------------------------------

    /**
     * Renvoie les coordonnées des anneaux de chaque équipe encore présents sur le plateau.
     *
     * @return une map associant chaque équipe à la liste des coordonnées de ses anneaux.
     */
    @Override
    public Map<Team, List<Coordinate>> rings() {
        Map<Team, List<Coordinate>> map = new HashMap<>();
        map.put(Team.WHITE, new ArrayList<>());
        map.put(Team.BLACK, new ArrayList<>());
        this.board.forEach((coordinate, token) -> {
            if (token instanceof Ring) {
                map.get(token.getTeam()).add(coordinate);
            }
        });
        return map;
    }

    // -------------------------------------------------------------------------
    // removeToken
    // -------------------------------------------------------------------------

    /**
     * Renvoie un nouvel état avec le token supprimé (mis à {@code null}) à la coordonnée donnée.
     *
     * @param c la coordonnée du token à supprimer.
     * @return le nouvel état sans le token.
     * @throws IndexOutOfBoundsException si la coordonnée est hors du terrain.
     */
    @Override
    public IState removeToken(Coordinate c) {
        if (!isInField(c)) {
            throw new IndexOutOfBoundsException("Coordonnée hors du terrain");
        }
        Map<Coordinate, Token> newBoard = new HashMap<>(board);
        newBoard.put(c, null);
        return new State(newBoard, turn, lines);
    }

    // -------------------------------------------------------------------------
    // toggleToken
    // -------------------------------------------------------------------------

    /**
     * Place ou retire dynamiquement un token via réflexion Java.
     * Si la case contient déjà un token de la même classe et de la même équipe,
     * on le supprime. Sinon, on place le nouveau token.
     *
     * @param position   la coordonnée ciblée.
     * @param team       l'équipe du token.
     * @param tokenClass la classe du token à instancier dynamiquement.
     * @return le nouvel état mis à jour.
     * @throws RuntimeException si l'instanciation du token échoue.
     */
    @Override
    public IState toggleToken(Coordinate position, Team team, Class<?> tokenClass) throws RuntimeException {
        try {
            Constructor<?> constructor = tokenClass.getConstructors()[0];
            Token newToken = (Token) constructor.newInstance(team);

            Map<Coordinate, Token> newBoard = new HashMap<>(board);
            Token existing = newBoard.get(position);

            if (existing != null
                    && existing.getClass().equals(tokenClass)
                    && existing.getTeam().equals(team)) {
                newBoard.put(position, null);
            } else {
                newBoard.put(position, newToken);
            }

            return new State(newBoard, turn, lines);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erreur lors de la manipulation du token : " + tokenClass.getSimpleName(), e
            );
        }
    }

    // -------------------------------------------------------------------------
    // getPawnLines
    // -------------------------------------------------------------------------

    /**
     * Renvoie la coordonnée voisine dans la direction donnée.
     *
     * @param c    la coordonnée de départ.
     * @param dir  la direction sous forme de chaîne (ex : {@code "NE"}).
     * @param mode le mode d'orientation du plateau.
     * @return la coordonnée voisine, ou {@code null} si la direction est inconnue.
     */
    private static Coordinate getNextCoord(Coordinate c, String dir, Mode mode) {
        switch (dir) {
            case "NO": return c.NO(mode);
            case "N":  return c.N(mode);
            case "NE": return c.NE(mode);
            case "E":  return c.E(mode);
            case "SE": return c.SE(mode);
            case "S":  return c.S(mode);
            case "SO": return c.SO(mode);
            case "O":  return c.O(mode);
            default:   return null;
        }
    }

    /**
     * Renvoie les directions à parcourir pour détecter les lignes selon le mode.
     *
     * @param mode le mode d'orientation du plateau.
     * @return un tableau de directions à explorer.
     */
    private static String[] directionsForMode(Mode mode) {
        if (mode == Mode.POINTY) return new String[]{"E", "NE", "SE"};
        return new String[]{"N", "NE", "SE"};
    }

    /**
     * Cherche toutes les lignes de 5 pions consécutifs de même couleur dans le plateau donné.
     * Si un alignement comporte plus de 5 pions, renvoie plusieurs ensembles de 5.
     *
     * @param board le plateau à analyser.
     * @param mode  le mode d'orientation du plateau.
     * @return la liste des lignes de 5 pions trouvées.
     */
    public static List<Set<Coordinate>> getPawnLines(HashMap<Coordinate, Token> board, Mode mode) {
        Set<Set<Coordinate>> completedLines = new HashSet<>();
        String[] directions = directionsForMode(mode);

        for (Coordinate startC : board.keySet()) {
            Token startToken = board.get(startC);
            if (!(startToken instanceof Pawn)) continue;

            Team team = startToken.getTeam();

            for (String dir : directions) {
                List<Coordinate> currentLine = new ArrayList<>();
                currentLine.add(startC);
                Coordinate next = getNextCoord(startC, dir, mode);

                while (next != null && board.containsKey(next)) {
                    Token nextToken = board.get(next);
                    if (nextToken instanceof Pawn && nextToken.getTeam() == team) {
                        currentLine.add(next);
                        next = getNextCoord(next, dir, mode);
                    } else {
                        break;
                    }
                }

                if (currentLine.size() >= 5) {
                    for (int i = 0; i <= currentLine.size() - 5; i++) {
                        Set<Coordinate> line = new HashSet<>();
                        for (int j = i; j < i + 5; j++) {
                            line.add(currentLine.get(j));
                        }
                        completedLines.add(line);
                    }
                }
            }
        }

        return new ArrayList<>(completedLines);
    }

    /**
     * Cherche les lignes de 5 pions sur le plateau courant avec le mode par défaut.
     *
     * @return la liste des lignes de 5 pions.
     */
    @Override
    public List<Set<Coordinate>> getPawnLines() {
        return getPawnLines(new HashMap<>(board), DEFAULT_MODE);
    }

    // -------------------------------------------------------------------------
    // winner
    // -------------------------------------------------------------------------

    /**
     * Renvoie l'équipe gagnante si elle a retiré 3 anneaux (il lui en reste 2 ou moins),
     * ou {@code null} si la partie n'est pas terminée.
     *
     * @return l'équipe gagnante, ou {@code null}.
     */
    @Override
    public Team winner() {
        int cptBlancs = 0;
        int cptNoirs = 0;

        for (Token token : board.values()) {
            if (token instanceof Ring) {
                if (token.getTeam() == Team.WHITE) cptBlancs++;
                else if (token.getTeam() == Team.BLACK) cptNoirs++;
            }
        }

        if (cptBlancs + cptNoirs < 7) return null;

        if (cptBlancs <= 2 && cptBlancs > 0) return Team.WHITE;
        if (cptNoirs <= 2 && cptNoirs > 0) return Team.BLACK;

        return null;
    }

    // -------------------------------------------------------------------------
    // move
    // -------------------------------------------------------------------------

    /**
     * Déplace l'anneau de {@code from} vers {@code to}, place un pion sur {@code from},
     * inverse les pions survolés et renvoie le nouvel état.
     *
     * @param move l'action de déplacement.
     * @return le nouvel état après déplacement.
     * @throws IndexOutOfBoundsException si une coordonnée est hors du terrain.
     * @throws IllegalArgumentException  si la case de départ ne contient pas un anneau du joueur courant,
     *                                   ou si la destination n'est pas accessible.
     * @throws RuntimeException          si une ligne est en attente de suppression.
     */
    @Override
    public IState move(Move move) {
        Coordinate from = move.getFrom();
        Coordinate to   = move.getTo();

        if (!isInField(from) || !isInField(to)) {
            throw new IndexOutOfBoundsException("Coordonnée hors du terrain.");
        }

        Token fromToken = board.get(from);
        if (!(fromToken instanceof Ring) || fromToken.getTeam() != turn) {
            throw new IllegalArgumentException("La case de départ ne contient pas un anneau du joueur courant.");
        }

        if (!lines.isEmpty()) {
            throw new RuntimeException("Impossible de déplacer un anneau : une ligne est en attente de suppression.");
        }

        if (!availableMoves(from).contains(to)) {
            throw new IllegalArgumentException("La case d'arrivée n'est pas accessible depuis la case de départ.");
        }

        HashMap<Coordinate, Token> newBoard = new HashMap<>(board);

        List<Coordinate> survolees;
        try {
            survolees = from.between(DEFAULT_MODE, to);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du calcul des cases survolées.", e);
        }

        for (Coordinate c : survolees) {
            Token t = newBoard.get(c);
            if (t instanceof Pawn pawn) {
                newBoard.put(c, new Pawn(pawn.getTeam().opposite()));
            }
        }

        newBoard.put(from, new Pawn(turn));
        newBoard.put(to, fromToken);

        List<Set<Coordinate>> newLines = getPawnLines(newBoard, DEFAULT_MODE);

        boolean currentPlayerHasLine = false;
        for (Set<Coordinate> line : newLines) {
            Coordinate first = line.iterator().next();
            Token token = newBoard.get(first);
            if (token != null && token.getTeam() == turn) {
                currentPlayerHasLine = true;
                break;
            }
        }

        Team nextTeam;
        if (newLines.isEmpty()) {
            nextTeam = turn.opposite();
        } else if (currentPlayerHasLine) {
            nextTeam = turn;
        } else {
            nextTeam = turn.opposite();
        }

        return new State(newBoard, nextTeam, newLines);
    }

    // -------------------------------------------------------------------------
    // removeLine
    // -------------------------------------------------------------------------

    /**
     * Supprime une ligne de 5 pions et un anneau du plateau, puis renvoie le nouvel état.
     * Le joueur garde la main s'il lui reste d'autres lignes à supprimer.
     *
     * @param action l'action de suppression (doit être une instance de {@link RemoveLine}).
     * @return le nouvel état après suppression.
     * @throws IllegalArgumentException si l'action n'est pas une {@link RemoveLine}.
     * @throws RuntimeException         si la ligne est invalide (taille != 5, pions mélangés,
     *                                  ligne inexistante, ou anneau invalide).
     */
    @Override
    public IState removeLine(Object action) {
        if (!(action instanceof RemoveLine remove)) {
            throw new IllegalArgumentException("L'action fournie n'est pas réalisable");
        }

        Set<Coordinate> ligneASupp = remove.getLine();
        Coordinate ringToRemove = remove.getRing();

        if (ligneASupp == null || ligneASupp.size() != 5) {
            throw new RuntimeException("Une ligne doit comporter exactement 5 cases");
        }

        Team equipeLigne = null;
        for (Coordinate c : ligneASupp) {
            Token token = board.get(c);
            if (!(token instanceof Pawn)) {
                throw new RuntimeException("Pas que des pions sur la ligne");
            }
            if (equipeLigne == null) {
                equipeLigne = token.getTeam();
            } else if (equipeLigne != token.getTeam()) {
                throw new RuntimeException("Les pions ne sont pas tous de la même équipe");
            }
        }

        List<Set<Coordinate>> existingLines = getPawnLines(new HashMap<>(board), DEFAULT_MODE);
        if (!existingLines.contains(ligneASupp)) {
            throw new RuntimeException("La ligne fournie n'existe pas sur le plateau.");
        }

        Token ringToken = board.get(ringToRemove);
        if (!(ringToken instanceof Ring) || ringToken.getTeam() != equipeLigne) {
            throw new RuntimeException("L'anneau est soit invalide soit pas de la bonne équipe");
        }

        HashMap<Coordinate, Token> newBoard = new HashMap<>(board);
        for (Coordinate coord : ligneASupp) {
            newBoard.put(coord, null);
        }
        newBoard.put(ringToRemove, null);

        List<Set<Coordinate>> lignesRestantes = getPawnLines(newBoard, DEFAULT_MODE);
        boolean encoreLignes = false;
        for (Set<Coordinate> ligne : lignesRestantes) {
            Token t = newBoard.get(ligne.iterator().next());
            if (t != null && t.getTeam() == turn()) {
                encoreLignes = true;
                break;
            }
        }

        Team nextTeam = encoreLignes ? turn() : turn.opposite();

        return new State(newBoard, nextTeam, lignesRestantes);
    }

    // -------------------------------------------------------------------------
    // availableMoves
    // -------------------------------------------------------------------------

    /**
     * Renvoie les directions valides pour le calcul des mouvements selon le mode.
     *
     * @param mode le mode d'orientation du plateau.
     * @return un tableau de directions valides.
     */
    private static String[] diretionsForAvailableMove(Mode mode) {
        if (mode == Mode.POINTY) return new String[]{"NO", "NE", "E", "SE", "SO", "O"};
        return new String[]{"NO", "N", "NE", "SE", "S", "SO"};
    }

    /**
     * Renvoie les cases accessibles depuis {@code from} avec le mode par défaut.
     *
     * @param from la coordonnée de l'anneau à déplacer.
     * @return l'ensemble des cases accessibles.
     */
    @Override
    public Set<Coordinate> availableMoves(Coordinate from) {
        return availableMoves(from, DEFAULT_MODE);
    }

    /**
     * Renvoie les cases accessibles depuis {@code from} selon le mode spécifié.
     * Règles appliquées :
     * <ul>
     *   <li>Case vide → valide, on continue.</li>
     *   <li>Anneau → stop immédiat.</li>
     *   <li>Pions → on peut sauter par-dessus, mais on s'arrête sur la première case vide derrière.</li>
     * </ul>
     *
     * @param from la coordonnée de l'anneau à déplacer.
     * @param mode le mode d'orientation du plateau.
     * @return l'ensemble des cases accessibles.
     */
    @Override
    public Set<Coordinate> availableMoves(Coordinate from, Mode mode) {
        Set<Coordinate> moves = new HashSet<>();

        if (!isInField(from)) return moves;

        Token startToken = board.get(from);
        if (!(startToken instanceof Ring) || startToken.getTeam() != turn) return moves;

        String[] directions = diretionsForAvailableMove(mode);

        for (String dir : directions) {
            Coordinate current = getNextCoord(from, dir, mode);
            boolean jumpedPawns = false;

            while (current != null && isInField(current)) {
                Token t = board.get(current);

                if (t == null) {
                    moves.add(current);
                    if (jumpedPawns) break;
                } else if (t instanceof Ring) {
                    break;
                } else if (t instanceof Pawn) {
                    jumpedPawns = true;
                }

                current = getNextCoord(current, dir, mode);
            }
        }

        return moves;
    }

    // -------------------------------------------------------------------------
    // isOver / getBoard
    // -------------------------------------------------------------------------

    /**
     * Renvoie {@code true} si la partie est terminée.
     * La partie se termine si un joueur a gagné ou si aucun anneau du joueur courant
     * ne peut se déplacer (match nul).
     *
     * @return {@code true} si la partie est terminée.
     */
    @Override
    public boolean isOver() {
        if (winner() != null) return true;

        for (Map.Entry<Coordinate, Token> entry : board.entrySet()) {
            Coordinate coordinate = entry.getKey();
            Token token = entry.getValue();
            if (token instanceof Ring && token.getTeam() == turn) {
                if (!availableMoves(coordinate).isEmpty()) return false;
            }
        }
        return true;
    }

    /**
     * Renvoie une représentation 2D du plateau sous forme de tableau d'équipes.
     * Les cases vides sont {@code null}.
     *
     * @return un tableau {@code Team[11][19]} représentant le plateau.
     */
    @Override
    public Team[][] getBoard() {
        Team[][] result = new Team[11][19];
        for (Map.Entry<Coordinate, Token> entry : board.entrySet()) {
            Coordinate coordinate = entry.getKey();
            Token token = entry.getValue();
            if (token == null) continue;
            int x = coordinate.to2DCoordinate().x();
            int y = coordinate.to2DCoordinate().y();
            result[y][x] = token.getTeam();
        }
        return result;
    }

    // -------------------------------------------------------------------------
    // equals, hashCode, toString
    // -------------------------------------------------------------------------

    /**
     * Deux états sont égaux s'ils ont le même plateau, le même tour et les mêmes lignes.
     *
     * @param obj l'objet à comparer.
     * @return {@code true} si les deux états sont identiques.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof State other)) return false;
        return board.equals(other.board)
                && turn == other.turn
                && lines.equals(other.lines);
    }

    /**
     * Calcule le hashCode à partir du plateau, du tour et des lignes.
     *
     * @return le hashCode de cet état.
     */
    @Override
    public int hashCode() {
        return 31 * (31 * board.hashCode() + turn.hashCode()) + lines.hashCode();
    }

    /**
     * Renvoie un résumé de l'état : équipe courante, nombre de tokens et nombre de lignes.
     *
     * @return une chaîne décrivant l'état courant.
     */
    @Override
    public String toString() {
        long tokenCount = 0;
        for (Token t : board.values()) {
            if (t != null) tokenCount++;
        }
        return "State{turn=" + turn + ", tokens=" + tokenCount + ", lines=" + lines.size() + "}";
    }
}