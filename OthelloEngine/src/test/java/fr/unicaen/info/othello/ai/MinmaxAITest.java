package fr.unicaen.info.othello.ai;

import fr.unicaen.info.othello.actions.Action;
import fr.unicaen.info.othello.actions.Move;
import fr.unicaen.info.othello.factory.FactoryCube;
import fr.unicaen.info.othello.state.IState;
import fr.unicaen.info.othello.state.State;
import fr.unicaen.info.othello.state.Team;
import fr.unicaen.info.othello.tokens.Pawn;
import fr.unicaen.info.othello.tokens.Ring;
import org.Coordinate.Coordinate;
import org.Coordinate.CoordinateCube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MinmaxAITest {

    private MinmaxAI ai;
    private FactoryCube factory;

    @BeforeEach
    void setUp() {
        ai = new MinmaxAI();
        factory = new FactoryCube();
    }

    // Crée un état avec 5 anneaux blancs et 5 anneaux noirs sur un vrai terrain
    private State createStateWith5Rings() {
        return (State) factory.emptyState()
                .toggleToken(new CoordinateCube( 0,  0,  0), Team.WHITE, Ring.class)
                .toggleToken(new CoordinateCube( 1, -1,  0), Team.WHITE, Ring.class)
                .toggleToken(new CoordinateCube(-1,  1,  0), Team.WHITE, Ring.class)
                .toggleToken(new CoordinateCube( 0,  1, -1), Team.WHITE, Ring.class)
                .toggleToken(new CoordinateCube( 0, -1,  1), Team.WHITE, Ring.class)
                .toggleToken(new CoordinateCube( 2, -2,  0), Team.BLACK, Ring.class)
                .toggleToken(new CoordinateCube(-2,  2,  0), Team.BLACK, Ring.class)
                .toggleToken(new CoordinateCube( 0,  2, -2), Team.BLACK, Ring.class)
                .toggleToken(new CoordinateCube( 0, -2,  2), Team.BLACK, Ring.class)
                .toggleToken(new CoordinateCube( 2,  0, -2), Team.BLACK, Ring.class);
    }

    // -------------------------------------------------------------------------
    // Tests chooseMove
    // -------------------------------------------------------------------------

    @Test
    void chooseMove_renvoieUneActionNonNull() {
        // L'IA doit toujours renvoyer une action quand des coups sont disponibles
        State state = createStateWith5Rings();
        Action action = ai.chooseMove(state);

        assertNotNull(action,
                "chooseMove ne devrait pas renvoyer null si des actions sont disponibles");
    }

    @Test
    void chooseMove_renvoieUnMove() {
        // Sans ligne sur le plateau → l'IA doit renvoyer un Move
        State state = createStateWith5Rings();
        Action action = ai.chooseMove(state);

        assertInstanceOf(Move.class, action,
                "Sans ligne sur le plateau, l'IA devrait renvoyer un Move");
    }

    // -------------------------------------------------------------------------
    // Tests applyAction
    // -------------------------------------------------------------------------

    @Test
    void applyAction_moveDeplaceBienLAnneau() {
        // Après un move : la case de départ contient un pion et l'arrivée contient l'anneau
        State state = new State(factory.emptyState().board(), Team.WHITE, new ArrayList<>());
        CoordinateCube from = new CoordinateCube(0, 0, 0);
        state = (State) state.toggleToken(from, Team.WHITE, Ring.class);

        Coordinate to = state.availableMoves(from).iterator().next();
        Move move = new Move(from, to);

        IState newState = ai.applyAction(state, move);

        // La règle du jeu : après le move, la case de départ contient un pion
        assertInstanceOf(Pawn.class, newState.board().get(from),
                "La case de départ devrait contenir un pion après le move");
        assertInstanceOf(Ring.class, newState.board().get(to),
                "La case d'arrivée devrait contenir l'anneau");
    }

    @Test
    void applyAction_leveExceptionSiActionInconnue() {
        // Une action de type inconnu doit lever une exception
        State state = new State(factory.emptyState().board(), Team.WHITE, new ArrayList<>());

        Action actionInvalide = new Action() {};

        assertThrows(IllegalArgumentException.class,
                () -> ai.applyAction(state, actionInvalide),
                "applyAction devrait lever une exception pour un type d'action inconnu");
    }

    // -------------------------------------------------------------------------
    // Tests cohérence
    // -------------------------------------------------------------------------

    @Test
    void chooseMove_actionEstDansLesBornes() {
        // L'action choisie doit avoir une destination accessible
        State state = createStateWith5Rings();
        Action action = ai.chooseMove(state);

        assertTrue(action instanceof Move,
                "L'action devrait être un Move");

        Move move = (Move) action;
        assertTrue(state.isInField(move.getFrom()),
                "La case de départ doit être dans le terrain");
        assertTrue(state.isInField(move.getTo()),
                "La case d'arrivée doit être dans le terrain");
        assertTrue(state.availableMoves(move.getFrom()).contains(move.getTo()),
                "La destination doit être accessible depuis la case de départ");
    }
}