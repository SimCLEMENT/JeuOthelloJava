package fr.unicaen.info.othello.factory;

import fr.unicaen.info.othello.state.State;
import fr.unicaen.info.othello.state.Team;
import fr.unicaen.info.othello.tokens.Pawn;
import fr.unicaen.info.othello.tokens.Ring;
import fr.unicaen.info.othello.tokens.Token;
import org.Coordinate.Coordinate;
import org.Coordinate.CoordinateCube;
import org.Enums.Mode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory générant des états de jeu en utilisant le système de coordonnées cubiques ({@link CoordinateCube}).
 * La case centrale du terrain est en {@code [0, 0, 0]}.
 */
public class FactoryCube implements IFactory {

    /**
     * Place un token sur le plateau via {@link State#toggleToken}.
     *
     * @param state      l'état courant.
     * @param q          composante q de la coordonnée.
     * @param r          composante r de la coordonnée.
     * @param s          composante s de la coordonnée.
     * @param team       l'équipe du token.
     * @param tokenClass la classe du token à placer.
     * @return le nouvel état avec le token placé.
     */
    private State place(State state, int q, int r, int s, Team team, Class<? extends Token> tokenClass) {
        return (State) state.toggleToken(new CoordinateCube(q, r, s), team, tokenClass);
    }

    /**
     * {@inheritDoc}
     * Génère un terrain hexagonal de rayon 5 centré en {@code [0, 0, 0]}.
     */
    @Override
    public State emptyState() {
        Map<Coordinate, Token> board = new HashMap<>();
        for (int q = -5; q <= 5; q++) {
            for (int r = -5; r <= 5; r++) {
                int s = -q - r;
                if (s >= -5 && s <= 5 && !isRemovedCorner(q, r, s)) {
                    board.put(new CoordinateCube(q, r, s), null);
                }
            }
        }
        return new State(board, Team.WHITE, new ArrayList<>());
    }

    /**
     * Vérifie si la coordonnée correspond à un coin à retirer du terrain hexagonal.
     *
     * @param q composante q.
     * @param r composante r.
     * @param s composante s.
     * @return {@code true} si la case doit être exclue du terrain.
     */
    private boolean isRemovedCorner(int q, int r, int s) {
        return (q == -5 && r == 0 && s == 5)
                || (q == -5 && r == 5 && s == 0)
                || (q == 0 && r == -5 && s == 5)
                || (q == 0 && r == 5 && s == -5)
                || (q == 5 && r == -5 && s == 0)
                || (q == 5 && r == 0 && s == -5);
    }

    /** {@inheritDoc} */
    @Override
    public State stateForWhiteLineTest() {
        State state = emptyState();
        state = place(state, -2, 0, 2, Team.WHITE, Pawn.class);
        state = place(state, -1, 0, 1, Team.WHITE, Pawn.class);
        state = place(state, 0, 0, 0, Team.WHITE, Pawn.class);
        state = place(state, 1, 0, -1, Team.WHITE, Pawn.class);
        state = place(state, 2, 0, -2, Team.WHITE, Pawn.class);
        state = place(state, 0, -1, 1, Team.WHITE, Ring.class);
        state = place(state, 0, 1, -1, Team.BLACK, Ring.class);
        return new State(state.board(), state.turn(), State.getPawnLines(new HashMap<>(state.board()), Mode.POINTY));
    }

    /** {@inheritDoc} */
    @Override
    public State stateForBlackLineTest() {
        State state = emptyState();
        state = place(state, 0, -2, 2, Team.BLACK, Pawn.class);
        state = place(state, 0, -1, 1, Team.BLACK, Pawn.class);
        state = place(state, 0, 0, 0, Team.BLACK, Pawn.class);
        state = place(state, 0, 1, -1, Team.BLACK, Pawn.class);
        state = place(state, 0, 2, -2, Team.BLACK, Pawn.class);
        state = place(state, 1, -1, 0, Team.WHITE, Ring.class);
        state = place(state, -1, 1, 0, Team.BLACK, Ring.class);
        return new State(state.board(), state.turn(), State.getPawnLines(new HashMap<>(state.board()), Mode.POINTY));
    }

    /** {@inheritDoc} */
    @Override
    public State testState() {
        State state = emptyState();
        state = place(state, 0, 0, 0, Team.WHITE, Ring.class);
        state = place(state, 1, 0, -1, Team.WHITE, Ring.class);
        state = place(state, -1, 0, 1, Team.BLACK, Ring.class);
        state = place(state, 0, 1, -1, Team.BLACK, Ring.class);
        state = place(state, 1, -1, 0, Team.WHITE, Pawn.class);
        state = place(state, 2, -1, -1, Team.BLACK, Pawn.class);
        state = place(state, -1, 1, 0, Team.WHITE, Pawn.class);
        state = place(state, -2, 1, 1, Team.BLACK, Pawn.class);
        return state;
    }

    /** {@inheritDoc} */
    @Override
    public State doubleLineStateTest() {
        State state = emptyState();
        state = place(state, -2, 0, 2, Team.WHITE, Pawn.class);
        state = place(state, -1, 0, 1, Team.WHITE, Pawn.class);
        state = place(state, 0, 0, 0, Team.WHITE, Pawn.class);
        state = place(state, 1, 0, -1, Team.WHITE, Pawn.class);
        state = place(state, 2, 0, -2, Team.WHITE, Pawn.class);
        state = place(state, 0, -2, 2, Team.WHITE, Pawn.class);
        state = place(state, 0, -1, 1, Team.WHITE, Pawn.class);
        state = place(state, 0, 1, -1, Team.WHITE, Pawn.class);
        state = place(state, 0, 2, -2, Team.WHITE, Pawn.class);
        state = place(state, 1, -1, 0, Team.WHITE, Ring.class);
        state = place(state, -1, 1, 0, Team.BLACK, Ring.class);
        return new State(state.board(), state.turn(), State.getPawnLines(new HashMap<>(state.board()), Mode.POINTY));
    }
}