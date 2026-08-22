package fr.unicaen.info.othello.state;

import fr.unicaen.info.othello.factory.FactoryCube;
import fr.unicaen.info.othello.tokens.Pawn;
import fr.unicaen.info.othello.tokens.Token;
import fr.unicaen.info.othello.tokens.Ring;
import org.Coordinate.Coordinate;
import org.Coordinate.CoordinateCube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour le Record State.
 */
public class StateTest {

    private State emptyState;
    private CoordinateCube centerCoord;
    private CoordinateCube otherCoord;

    @BeforeEach
    void setUp() {
        Map<Coordinate, Token> board = new HashMap<>();

        centerCoord = new CoordinateCube(0, 0, 0);
        otherCoord  = new CoordinateCube(1, -1, 0);

        board.put(centerCoord, null);
        board.put(otherCoord, null);

        emptyState = new State(board, Team.WHITE, new ArrayList<>());
    }

    // -------------------------------------------------------------------------
    // Tests toggleToken
    // -------------------------------------------------------------------------

    @Test
    void toggleToken_placeUnAnneau() {
        IState newState = emptyState.toggleToken(centerCoord, Team.WHITE, Ring.class);

        Token token = newState.board().get(centerCoord);
        assertNotNull(token, "Un token devrait être présent après toggleToken");
        assertInstanceOf(Ring.class, token, "Le token devrait être un Ring");
        assertEquals(Team.WHITE, token.getTeam(), "L'anneau devrait être blanc");
    }

    @Test
    void toggleToken_supprimeSiMemeClasseEtMemeEquipe() {
        IState stateAvec = emptyState.toggleToken(centerCoord, Team.WHITE, Ring.class);
        IState stateSans = stateAvec.toggleToken(centerCoord, Team.WHITE, Ring.class);

        assertNull(stateSans.board().get(centerCoord),
                "Le token devrait être supprimé après un double toggleToken");
    }

    @Test
    void toggleToken_remplaceSiEquipeDifferente() {
        IState stateWhite = emptyState.toggleToken(centerCoord, Team.WHITE, Ring.class);
        IState stateBlack = stateWhite.toggleToken(centerCoord, Team.BLACK, Ring.class);

        Token token = stateBlack.board().get(centerCoord);
        assertNotNull(token, "Un token devrait être présent");
        assertEquals(Team.BLACK, token.getTeam(), "L'anneau devrait maintenant être noir");
    }

    // -------------------------------------------------------------------------
    // Tests removeToken
    // -------------------------------------------------------------------------

    @Test
    void removeToken_supprimeLeBonToken() {
        IState stateAvec = emptyState.toggleToken(centerCoord, Team.BLACK, Pawn.class);
        IState stateSans = stateAvec.removeToken(centerCoord);

        assertNull(stateSans.board().get(centerCoord),
                "La case devrait être vide après removeToken");
    }

    @Test
    void removeToken_naffectePasLesAutresCases() {
        IState state = emptyState
                .toggleToken(centerCoord, Team.WHITE, Pawn.class)
                .toggleToken(otherCoord,  Team.BLACK, Ring.class);

        IState newState = state.removeToken(centerCoord);

        assertNull(newState.board().get(centerCoord), "centerCoord devrait être vide");
        assertNotNull(newState.board().get(otherCoord), "otherCoord ne devrait pas être affectée");
    }

    // -------------------------------------------------------------------------
    // Tests isInField
    // -------------------------------------------------------------------------

    @Test
    void isInField_renvoieTruePourCaseValide() {
        assertTrue(emptyState.isInField(centerCoord),
                "La case centrale devrait être dans le terrain");
    }

    @Test
    void isInField_renvoieFalsePourCaseHorsTerrain() {
        CoordinateCube horsChamp = new CoordinateCube(10, -5, -5);
        assertFalse(emptyState.isInField(horsChamp),
                "Une case hors terrain devrait renvoyer false");
    }

    // -------------------------------------------------------------------------
    // Tests immuabilité
    // -------------------------------------------------------------------------

    @Test
    void immutability_toggleTokenDoesNotMutateOldState() {
        IState newState = emptyState.toggleToken(centerCoord, Team.WHITE, Ring.class);

        assertNull(emptyState.board().get(centerCoord),
                "L'état original ne devrait pas être modifié");
        assertNotNull(newState.board().get(centerCoord),
                "Le nouvel état devrait contenir le token");
    }

    // -------------------------------------------------------------------------
    // Tests Team.opposite()
    // -------------------------------------------------------------------------

    @Test
    void oppositeTeamReturnsTheOppositeTeam() {
        assertEquals(Team.BLACK, Team.WHITE.opposite());
        assertEquals(Team.WHITE, Team.BLACK.opposite());
    }

    // -------------------------------------------------------------------------
    // Tests charRepr
    // -------------------------------------------------------------------------

    @Test
    void pawn_charReprNoir() {
        assertEquals("x", new Pawn(Team.BLACK).charRepr());
    }

    @Test
    void pawn_charReprBlanc() {
        assertEquals(".", new Pawn(Team.WHITE).charRepr());
    }

    @Test
    void ring_charReprNoir() {
        assertEquals("O", new Ring(Team.BLACK).charRepr());
    }

    @Test
    void ring_charReprBlanc() {
        assertEquals("o", new Ring(Team.WHITE).charRepr());
    }

    // -------------------------------------------------------------------------
    // Tests isOver
    // -------------------------------------------------------------------------

    @Test
    void isOver_renvoieFalsiSiAnneauxPeuventBouger() {
        // 5 anneaux blancs sur un vrai terrain → peuvent bouger
        FactoryCube factory = new FactoryCube();
        State state = new State(factory.emptyState().board(), Team.WHITE, new ArrayList<>());

        state = (State) state
                .toggleToken(new CoordinateCube( 0,  0,  0), Team.WHITE, Ring.class)
                .toggleToken(new CoordinateCube( 1, -1,  0), Team.WHITE, Ring.class)
                .toggleToken(new CoordinateCube(-1,  1,  0), Team.WHITE, Ring.class)
                .toggleToken(new CoordinateCube( 0,  1, -1), Team.WHITE, Ring.class)
                .toggleToken(new CoordinateCube( 0, -1,  1), Team.WHITE, Ring.class);

        assertFalse(state.isOver(),
                "isOver devrait renvoyer false si un anneau peut encore bouger");
    }

    @Test
    void isOver_renvoieTrueSiAucunAnneauNePeutBouger() {
        // Plateau minimal : un seul anneau blanc entouré de pions (pas de case vide accessible)
        // On crée un plateau custom très petit avec juste l'anneau et ses voisins remplis de pions
        Map<Coordinate, Token> board = new HashMap<>();

        CoordinateCube centre = new CoordinateCube(0, 0, 0);
        board.put(centre, new Ring(Team.WHITE));

        // On remplit les 6 voisins avec des pions (pas des anneaux, car on peut sauter par dessus)
        // mais on ne met PAS de case libre derrière → l'anneau ne peut pas s'arrêter
        // Le plus simple : on met juste des anneaux noirs qui bloquent
        board.put(new CoordinateCube( 1, -1,  0), new Ring(Team.BLACK));
        board.put(new CoordinateCube(-1,  1,  0), new Ring(Team.BLACK));
        board.put(new CoordinateCube( 0,  1, -1), new Ring(Team.BLACK));
        board.put(new CoordinateCube( 0, -1,  1), new Ring(Team.BLACK));
        board.put(new CoordinateCube( 1,  0, -1), new Ring(Team.BLACK));
        board.put(new CoordinateCube(-1,  0,  1), new Ring(Team.BLACK));

        // 1 anneau blanc + 6 anneaux noirs → winner() = null (1 > 0 mais <= 2... attention!)
        // Pour éviter que winner() se déclenche on ajoute 4 anneaux blancs ailleurs
        board.put(new CoordinateCube( 2, -2,  0), new Ring(Team.WHITE));
        board.put(new CoordinateCube(-2,  2,  0), new Ring(Team.WHITE));
        board.put(new CoordinateCube( 0,  2, -2), new Ring(Team.WHITE));
        board.put(new CoordinateCube( 0, -2,  2), new Ring(Team.WHITE));
        // Ces 4 anneaux sont sur le bord du plateau minimal, pas de voisins → bloqués aussi

        State state = new State(board, Team.WHITE, new ArrayList<>());
        assertTrue(state.isOver(),
                "isOver devrait renvoyer true si aucun anneau ne peut bouger");
    }

    @Test
    void isOver_renvoieTrueSiGagnant() {
        // Seulement 2 anneaux blancs → blanc a gagné
        FactoryCube factory = new FactoryCube();
        State state = new State(factory.emptyState().board(), Team.WHITE, new ArrayList<>());

        state = (State) state
                .toggleToken(new CoordinateCube( 0,  0,  0), Team.WHITE, Ring.class)
                .toggleToken(new CoordinateCube( 1, -1,  0), Team.WHITE, Ring.class);

        assertTrue(state.isOver(),
                "isOver devrait renvoyer true si un joueur a gagné");
    }
}