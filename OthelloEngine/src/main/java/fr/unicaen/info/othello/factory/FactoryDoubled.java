package fr.unicaen.info.othello.factory;

import fr.unicaen.info.othello.state.State;
import fr.unicaen.info.othello.state.Team;
import fr.unicaen.info.othello.tokens.Pawn;
import fr.unicaen.info.othello.tokens.Ring;
import fr.unicaen.info.othello.tokens.Token;
import org.Coordinate.Coordinate;
import org.Coordinate.CoordinateDoubled;
import org.Enums.Mode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory générant des états de jeu en utilisant le système de coordonnées Doubled ({@link CoordinateDoubled}).
 * La case centrale du terrain est en {@code [5, 9]}.
 */
public class FactoryDoubled implements IFactory {

    /**
     * Place un token sur le plateau via {@link State#toggleToken}.
     *
     * @param state      l'état courant.
     * @param y          la ligne de la coordonnée.
     * @param x          la colonne de la coordonnée.
     * @param team       l'équipe du token.
     * @param tokenClass la classe du token à placer.
     * @return le nouvel état avec le token placé.
     */
    private State place(State state, int y, int x, Team team, Class<? extends Token> tokenClass) {
        return (State) state.toggleToken(new CoordinateDoubled(y, x), team, tokenClass);
    }

    /**
     * Crée un nouvel état en recalculant les lignes de pions.
     *
     * @param state l'état dont on recalcule les lignes.
     * @return un nouvel état avec les lignes mises à jour.
     */
    private State withComputedLines(State state) {
        return new State(state.board(), state.turn(),
                State.getPawnLines(new HashMap<>(state.board()), Mode.POINTY));
    }

    /**
     * {@inheritDoc}
     * Génère un terrain hexagonal de 91 cases centré en {@code [5, 9]}.
     */
    @Override
    public State emptyState() {
        Map<Coordinate, Token> board = new HashMap<>();
        int[] Xstart = {6, 3, 2, 1, 0, 1, 0, 1, 2, 3, 6};
        int[] nbCases = {4, 7, 8, 9, 10, 9, 10, 9, 8, 7, 4};
        for (int y = 0; y < nbCases.length; y++) {
            for (int i = 0; i < nbCases[y]; i++) {
                int x = Xstart[y] + i * 2;
                board.put(new CoordinateDoubled(y, x), null);
            }
        }
        return new State(board, Team.WHITE, new ArrayList<>());
    }

    /** {@inheritDoc} */
    @Override
    public State stateForWhiteLineTest() {
        State state = emptyState();
        state = place(state, 5, 3, Team.WHITE, Pawn.class);
        state = place(state, 5, 5, Team.WHITE, Pawn.class);
        state = place(state, 5, 7, Team.WHITE, Pawn.class);
        state = place(state, 5, 9, Team.WHITE, Pawn.class);
        state = place(state, 5, 11, Team.WHITE, Pawn.class);
        state = place(state, 4, 8, Team.WHITE, Ring.class);
        state = place(state, 6, 8, Team.BLACK, Ring.class);
        return withComputedLines(state);
    }

    /** {@inheritDoc} */
    @Override
    public State stateForBlackLineTest() {
        State state = emptyState();
        state = place(state, 4, 4, Team.BLACK, Pawn.class);
        state = place(state, 4, 6, Team.BLACK, Pawn.class);
        state = place(state, 4, 8, Team.BLACK, Pawn.class);
        state = place(state, 4, 10, Team.BLACK, Pawn.class);
        state = place(state, 4, 12, Team.BLACK, Pawn.class);
        state = place(state, 5, 9, Team.WHITE, Ring.class);
        state = place(state, 6, 10, Team.BLACK, Ring.class);
        return withComputedLines(state);
    }

    /** {@inheritDoc} */
    @Override
    public State testState() {
        State state = emptyState();
        state = place(state, 5, 9, Team.WHITE, Ring.class);
        state = place(state, 5, 11, Team.WHITE, Ring.class);
        state = place(state, 6, 8, Team.BLACK, Ring.class);
        state = place(state, 6, 10, Team.BLACK, Ring.class);
        state = place(state, 4, 8, Team.WHITE, Pawn.class);
        state = place(state, 4, 10, Team.BLACK, Pawn.class);
        state = place(state, 5, 7, Team.WHITE, Pawn.class);
        state = place(state, 6, 12, Team.BLACK, Pawn.class);
        return state;
    }

    /** {@inheritDoc} */
    @Override
    public State doubleLineStateTest() {
        State state = emptyState();
        state = place(state, 5, 3, Team.WHITE, Pawn.class);
        state = place(state, 5, 5, Team.WHITE, Pawn.class);
        state = place(state, 5, 7, Team.WHITE, Pawn.class);
        state = place(state, 5, 9, Team.WHITE, Pawn.class);
        state = place(state, 5, 11, Team.WHITE, Pawn.class);
        state = place(state, 3, 5, Team.WHITE, Pawn.class);
        state = place(state, 4, 6, Team.WHITE, Pawn.class);
        state = place(state, 6, 8, Team.WHITE, Pawn.class);
        state = place(state, 7, 9, Team.WHITE, Pawn.class);
        state = place(state, 4, 10, Team.WHITE, Ring.class);
        state = place(state, 6, 10, Team.BLACK, Ring.class);
        return withComputedLines(state);
    }
}