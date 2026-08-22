package fr.unicaen.info.othello.ui;

import fr.unicaen.info.othello.actions.Move;
import fr.unicaen.info.othello.actions.RemoveLine;
import fr.unicaen.info.othello.factory.FactoryDoubled;
import fr.unicaen.info.othello.factory.IFactory;
import fr.unicaen.info.othello.state.State;
import fr.unicaen.info.othello.state.Team;
import fr.unicaen.info.othello.tokens.Ring;
import org.Coordinate.Coordinate;
import org.Coordinate.CoordinateDoubled;

import java.util.*;

/**
 * Classe principale pour l'interface utilisateur en ligne de commande (CUI) du jeu Othello.
 */
public class CUIMain {

    private static final String ANSI_RESET  = "\u001B[0m";
    private static final String ANSI_RED    = "\u001B[31;1m";   // sélectionné
    private static final String ANSI_GREEN  = "\u001B[32;1m";   // cases/pions traversés
    private static final String ANSI_YELLOW = "\u001B[33;1m";   // anneau qui a traversé

    /**
     * Affiche l'état actuel du plateau de jeu dans la console.
     *
     * @param state          L'état actuel du jeu.
     * @param vsAI           true si on joue contre l'IA.
     * @param selectedRing   Coordonnée de l'anneau sélectionné (rouge), ou null.
     * @param lastMoverRing  Coordonnée de l'anneau qui a bougé au tour précédent (jaune), ou null.
     * @param lastTraversed  Ensemble des cases traversées au tour précédent (vert), ou null.
     */
    public void displayBoard(State state, boolean vsAI,
                             Coordinate selectedRing,
                             Coordinate lastMoverRing,
                             Set<Coordinate> lastTraversed) {

        System.out.println("         " + ANSI_RED    + "█ sélectionné"         + ANSI_RESET
                + "  "        + ANSI_YELLOW + "█ a bougé (tour préc.)" + ANSI_RESET
                + "  "        + ANSI_GREEN  + "█ traversé (tour préc.)"+ ANSI_RESET);

        System.out.println("rappel : N : anneau noir  | n : pion noir\n         B : anneau blanc | b : pion blanc\n         . : vide");

        if (vsAI) {
            System.out.println("         (Vous êtes les BLANCS et l'IA est les NOIRS)");
        } else {
            System.out.println("         (Joueur 1 : BLANCS | Joueur 2 : NOIRS)");
        }


        char[][] canvas = new char[11][20];
        for (int i = 0; i < 11; i++) Arrays.fill(canvas[i], ' ');

        for (Coordinate c : state.getCoordinates()) {
            if (c instanceof CoordinateDoubled doubled) {
                int x = doubled.to2DCoordinate().x();
                int y = doubled.to2DCoordinate().y();

                if (state.isEmpty(c)) {
                    canvas[y][x] = '.';
                } else {
                    Team tokenTeam = state.getToken(c).getTeam();
                    boolean isRing = state.getToken(c) instanceof Ring;
                    if (tokenTeam == Team.BLACK) {
                        canvas[y][x] = isRing ? 'N' : 'n';
                    } else {
                        canvas[y][x] = isRing ? 'B' : 'b';
                    }
                }
            }
        }

        System.out.println("\n    0123456789 ..... 19");
        System.out.println("   ----------------------");
        for (int y = 0; y < 11; y++) {
            System.out.printf("%2d |", y);
            for (int x = 0; x < 20; x++) {
                char ch = canvas[y][x];
                if (ch == ' ') {
                    System.out.print(ch);
                    continue;
                }

                CoordinateDoubled coord = new CoordinateDoubled(y, x);

                boolean isSel       = selectedRing   != null && selectedRing.equals(coord);
                boolean isMover     = lastMoverRing   != null && lastMoverRing.equals(coord);
                boolean isTraversed = lastTraversed   != null && lastTraversed.contains(coord);

                if (isSel) {
                    System.out.print(ANSI_RED + ch + ANSI_RESET);
                } else if (isMover) {
                    System.out.print(ANSI_YELLOW + ch + ANSI_RESET);
                } else if (isTraversed) {
                    System.out.print(ANSI_GREEN + ch + ANSI_RESET);
                } else {
                    System.out.print(ch);
                }
            }
            System.out.println("|");
        }
        System.out.println("   ----------------------\n");
    }

    /** Surcharge sans highlight (phase de placement, etc.). */
    public void displayBoard(State state, boolean vsAI) {
        displayBoard(state, vsAI, null, null, null);
    }

    /**
     * Retourne les coordonnées situées strictement entre {@code from} et {@code to}
     * (destination incluse, départ exclu).
     */
    private Set<Coordinate> computeTraversed(Coordinate from, Coordinate to) {
        Set<Coordinate> traversed = new LinkedHashSet<>();
        if (!(from instanceof CoordinateDoubled f) || !(to instanceof CoordinateDoubled t)) {
            traversed.add(to);
            return traversed;
        }
        int fy = f.to2DCoordinate().y(), fx = f.to2DCoordinate().x();
        int ty = t.to2DCoordinate().y(), tx = t.to2DCoordinate().x();
        int dy = Integer.signum(ty - fy);
        int dx = Integer.signum(tx - fx);

        int cy = fy + dy, cx = fx + dx;
        while (cy != ty || cx != tx) {
            traversed.add(new CoordinateDoubled(cy, cx));
            cy += dy;
            cx += dx;
        }
        traversed.add(to);
        return traversed;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  PHASE DE PLACEMENT
    // ═══════════════════════════════════════════════════════════════════════════

    private State placementPhase(State state, Scanner scanner, boolean randomStart,
                                 boolean vsAI, boolean aiIsBlack) {
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {

            // ---- BLANC ----
            if (randomStart || (vsAI && !aiIsBlack)) {
                boolean ok = false;
                while (!ok) {
                    int y = rand.nextInt(11), x = rand.nextInt(20);
                    Coordinate c = new CoordinateDoubled(y, x);
                    if (state.isInField(c) && state.isEmpty(c)) {
                        state = (State) state.toggleToken(c, Team.WHITE, Ring.class);
                        ok = true;
                    }
                }
            } else {
                displayBoard(state, vsAI);
                boolean ok = false;
                while (!ok) {
                    System.out.println("Joueur BLANC (B), entrez y et x :");
                    int y = scanner.nextInt(), x = scanner.nextInt();
                    Coordinate c = new CoordinateDoubled(y, x);
                    try {
                        if (!state.isInField(c))       { System.out.println("Erreur >:( Hors du terrain."); }
                        else if (!state.isEmpty(c))    { System.out.println("Erreur !!!! Case déjà occupée."); }
                        else { state = (State) state.toggleToken(c, Team.WHITE, Ring.class); ok = true; }
                    } catch (IllegalArgumentException e) { System.out.println("Erreur: Coordonnée invalide \uD83E\uDD13"); }
                }
            }

            // ---- NOIR ----
            if (randomStart || (vsAI && aiIsBlack)) {
                boolean ok = false;
                while (!ok) {
                    int y = rand.nextInt(11), x = rand.nextInt(20);
                    Coordinate c = new CoordinateDoubled(y, x);
                    if (state.isInField(c) && state.isEmpty(c)) {
                        state = (State) state.toggleToken(c, Team.BLACK, Ring.class);
                        ok = true;
                    }
                }
            } else {
                displayBoard(state, vsAI);
                boolean ok = false;
                while (!ok) {
                    System.out.println("Joueur NOIR (N), entrez y et x :");
                    int y = scanner.nextInt(), x = scanner.nextInt();
                    Coordinate c = new CoordinateDoubled(y, x);
                    try {
                        if (!state.isInField(c))       { System.out.println("Erreur: Hors du terrain."); }
                        else if (!state.isEmpty(c))    { System.out.println("Erreur: Case déjà occupée."); }
                        else { state = (State) state.toggleToken(c, Team.BLACK, Ring.class); ok = true; }
                    } catch (IllegalArgumentException e) { System.out.println("Erreur: Coordonnée invalide."); }
                }
            }
        }
        return state;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  TOUR HUMAIN
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Joue un tour humain. Met à jour lastMoverOut[0] et lastTraversedOut[0].
     * Retourne null si le tour doit être rejoué (choix invalide / aucun coup).
     */
    private State playHumanTurn(State state, Scanner scanner, boolean vsAI,
                                Coordinate[] lastMoverOut, Set<Coordinate>[] lastTraversedOut,
                                Coordinate lastMoverRing, Set<Coordinate> lastTraversed) {

        if (!state.lines().isEmpty()) {
            System.out.println("Bravo joueur " + state.turn() + " !\nUne ligne a été formée !");
            Set<Coordinate> line = chooseLine(state, scanner);
            if (line == null) return state;

            System.out.println("Ligne formée : " + line + "\n");
            System.out.println("Il est maintenant l'heure du sacrifice...\nA quel anneau donnez-vous le privilège de se sacrifier ? :\n");
            for (int i = 0; i < state.rings().get(state.turn()).size(); i++) {
                System.out.println("\t> " + state.rings().get(state.turn()).get(i) + "\n");
            }
            System.out.print("Votre choix (1-" + state.rings().get(state.turn()).size() + ") > ");
            int choix = scanner.nextInt();
            Coordinate anneauASacrifier = state.rings().get(state.turn()).get(choix - 1);

            state = (State) state.removeLine(new RemoveLine(line, anneauASacrifier));
            System.out.println("Sacrifice accepté ! Ligne et anneau retirés.\n");

            lastMoverOut[0]     = null;
            lastTraversedOut[0] = null;
            return state;
        }

        System.out.println("Choisissez un anneau à bouger : ");
        List<Coordinate> availableRings = state.rings().get(state.turn());
        String[] coordRingsString = new String[availableRings.size()];

        for (int idx = 0; idx < availableRings.size(); idx++) {
            Coordinate ringCoord = availableRings.get(idx);
            if (ringCoord instanceof CoordinateDoubled doubledCoord) {
                coordRingsString[idx] = doubledCoord.toString();
                System.out.print((idx + 1) + ". " + coordRingsString[idx] + "\n");
            }
        }

        int choixAnneau = -1;
        while (choixAnneau < 1 || choixAnneau > availableRings.size()) {
            try {
                System.out.print("Votre choix (1-" + availableRings.size() + ") > ");;
                choixAnneau = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("choix invalide, doit être entre (1-" + availableRings.size() + "\n");
            }
        }
        Coordinate anneauChoisi = availableRings.get(choixAnneau - 1);

        displayBoard(state, vsAI, anneauChoisi, lastMoverRing, lastTraversed);

        List<Coordinate> possibleDestinations = new ArrayList<>(state.availableMoves(anneauChoisi));

        System.out.println("Où voulez-vous déplacer votre anneau situé en " + coordRingsString[choixAnneau - 1] + " ?");
        if (possibleDestinations.isEmpty()) {
            System.out.println("Aucun coup possible pour cet anneau !");
            displayBoard(state, vsAI, null, lastMoverRing, lastTraversed);
            return state;
        }

        for (int idx = 0; idx < possibleDestinations.size(); idx++) {
            System.out.print((idx + 1) + ". " + possibleDestinations.get(idx).toString() + "\n");
        }

        int choixDest = -1;
        while (choixDest < 1 || choixDest > possibleDestinations.size()) {
            try {
                System.out.print("Votre choix de destination (1-" + possibleDestinations.size() + ") > ");
                choixDest = scanner.nextInt();
            } catch (Exception e) {
                System.out.print("choix invalide, doit être entre (1-" + possibleDestinations.size() + "\n");
            }
        }

        Coordinate destination = possibleDestinations.get(choixDest - 1);

        Set<Coordinate> traversed = computeTraversed(anneauChoisi, destination);

        state = (State) state.move(new Move(anneauChoisi, destination));

        lastMoverOut[0]     = destination;
        lastTraversedOut[0] = new LinkedHashSet<>(traversed);
        lastTraversedOut[0].remove(destination);

        System.out.println("Mouvement effectué ! \n---------------------");
        return state;
    }

    private Set<Coordinate> chooseLine(State state, Scanner scanner) {
        if (state.lines().size() == 1) return state.lines().getFirst();

        System.out.println("Choisis en une parmi :\n");
        for (int i = 0; i < state.lines().size(); i++) {
            System.out.println("\t> " + state.lines().get(i) + "\n");
        }
        System.out.println("\nVotre choix (1-" + state.lines().size() + ")");
        try {
            int choix = scanner.nextInt();
            if (choix >= 1 && choix <= state.lines().size()) {
                return state.lines().get(choix - 1);
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrée invalide.");
            scanner.next();
        }
        System.out.println("Choix invalide.");
        return null;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  JOUER (PvP)
    // ═══════════════════════════════════════════════════════════════════════════

    public Team playHuman(Scanner scanner, boolean randomStart) {
        IFactory factory = new FactoryDoubled();
        State state = factory.emptyState();

        System.out.println("\n=== DÉBUT DE LA PARTIE (PvP) ===");
        System.out.println("Joueur 1 : Vous jouez les BLANCS (B).");
        System.out.println("Joueur 2 : Vous jouez les NOIRS (N).\n");

        state = placementPhase(state, scanner, randomStart, false, false);

        Coordinate      lastMoverRing = null;
        Set<Coordinate> lastTraversed = null;

        @SuppressWarnings("unchecked")
        Coordinate[]      lastMoverOut     = new Coordinate[1];
        @SuppressWarnings("unchecked")
        Set<Coordinate>[] lastTraversedOut = new Set[1];

        while (!state.isOver()) {
            displayBoard(state, false, null, lastMoverRing, lastTraversed);
            System.out.print("C'est au tour des " + state.turn() + ".\n");

            state = playHumanTurn(state, scanner, false, lastMoverOut, lastTraversedOut,
                    lastMoverRing, lastTraversed);

            lastMoverRing = lastMoverOut[0];
            lastTraversed = lastTraversedOut[0];
        }
        return state.winner();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  JOUER (vs IA)
    // ═══════════════════════════════════════════════════════════════════════════

    public Team playAI(Scanner scanner, boolean randomStart) {
        IFactory factory = new FactoryDoubled();
        State state = factory.emptyState();
        fr.unicaen.info.othello.ai.MinmaxAI ai = new fr.unicaen.info.othello.ai.MinmaxAI(Team.BLACK);

        System.out.println("\n=== DÉBUT DE LA PARTIE (vs IA) ===");
        System.out.println("Vous jouez les BLANCS (B).");
        System.out.println("L'IA joue les NOIRS (N).\n");

        state = placementPhase(state, scanner, randomStart, true, true);

        Coordinate      lastMoverRing = null;
        Set<Coordinate> lastTraversed = null;

        @SuppressWarnings("unchecked")
        Coordinate[]      lastMoverOut     = new Coordinate[1];
        @SuppressWarnings("unchecked")
        Set<Coordinate>[] lastTraversedOut = new Set[1];

        while (!state.isOver()) {
            displayBoard(state, true, null, lastMoverRing, lastTraversed);
            System.out.print("C'est au tour des " + state.turn() + ".\n");

            if (state.turn() == Team.BLACK) {
                System.out.println("L'IA (NOIR) réfléchit...");
                fr.unicaen.info.othello.actions.Action action = ai.chooseMove(state);
                if (action == null) break;

                Coordinate aiFrom = null, aiTo = null;
                if (action instanceof Move moveAct) {
                    aiFrom = moveAct.getFrom();
                    aiTo   = moveAct.getTo();
                }

                state = (State) ai.applyAction(state, action);

                if (action instanceof RemoveLine) {
                    System.out.println("L'IA a sacrifié un anneau et retiré une ligne !");
                    lastMoverRing = null;
                    lastTraversed = null;
                } else {
                    System.out.println("L'IA s'est déplacée.");
                    if (aiFrom != null && aiTo != null) {
                        Set<Coordinate> trav = computeTraversed(aiFrom, aiTo);
                        lastMoverRing = aiTo;
                        lastTraversed = new LinkedHashSet<>(trav);
                        lastTraversed.remove(aiTo);
                    }
                }

            } else {
                state = playHumanTurn(state, scanner, true, lastMoverOut, lastTraversedOut,
                        lastMoverRing, lastTraversed);
                lastMoverRing = lastMoverOut[0];
                lastTraversed = lastTraversedOut[0];
            }
        }
        return state.winner();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  CONSTRUCTEUR / MAIN
    // ═══════════════════════════════════════════════════════════════════════════

    public CUIMain() {
        Scanner scanner = new Scanner(System.in);
        int choixEnnemi = 0;
        System.out.println("Choisis ton ennemi :\n\t> 1. IA\n\t> 2. Humain (pvp)");
        while (choixEnnemi != 1 && choixEnnemi != 2) {
            System.out.print("Votre choix > ");
            try {
                choixEnnemi = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrée invalide.");
                scanner.next();
            }
        }

        int choixStart = 0;
        System.out.println("Début aléatoire ?\n\t> 1. Oui\n\t> 2. Non");
        while (choixStart != 1 && choixStart != 2) {
            System.out.print("Votre choix > ");
            try {
                choixStart = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrée invalide.");
                scanner.next();
            }
        }
        boolean randomStart = (choixStart == 1);

        Team winner;
        if (choixEnnemi == 1) {
            winner = playAI(scanner, randomStart);
        } else {
            winner = playHuman(scanner, randomStart);
        }

        System.out.println("Partie terminée !");
        if (winner != null) {
            System.out.println("\nBravo aux " + winner + " :D !\n");
        } else {
            System.out.println("\nAw... dommage, c'est un match nul...\n");
        }
        scanner.close();
    }

    public static void main(String[] args) {
        new CUIMain();
    }
}