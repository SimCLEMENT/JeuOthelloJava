package org.Coordinate;

import org.Enums.Mode;
import org.Exceptions.DifferentAxisException;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CoordinateCubeTest {

    // -------------------------------------------------------------------------
    // Tests constructeur
    // -------------------------------------------------------------------------

    @Test
    void constructeur_leveExceptionSiContrainteViolee() {
        assertThrows(IllegalArgumentException.class,
                () -> new CoordinateCube(1, 1, 1),
                "Le constructeur devrait lever une exception si q+r+s != 0"
        );
    }

    @Test
    void constructeur_accepteCoordonneeValide() {
        assertDoesNotThrow(() -> new CoordinateCube(1, -1, 0));
    }

    // -------------------------------------------------------------------------
    // Tests to2DCoordinate
    // -------------------------------------------------------------------------

    @Test
    void to2DCoordinate_centreDonneLePointCentral() {
        CoordinateCube centre = new CoordinateCube(0, 0, 0);
        Point p = centre.to2DCoordinate();
        assertEquals(9, p.x(), "La colonne du centre devrait être 9");
        assertEquals(5, p.y(), "La ligne du centre devrait être 5");
    }

    // -------------------------------------------------------------------------
    // Tests directions FLAT
    // -------------------------------------------------------------------------

    @Test
    void NE_renvoieLeVoisinCorrect() {
        CoordinateCube c = new CoordinateCube(0, 0, 0);
        assertEquals(new CoordinateCube(1, -1, 0), (CoordinateCube) c.NE(Mode.FLAT));
    }

    @Test
    void SO_renvoieLeVoisinCorrect() {
        CoordinateCube c = new CoordinateCube(0, 0, 0);
        assertEquals(new CoordinateCube(-1, 1, 0), (CoordinateCube) c.SO(Mode.FLAT));
    }

    @Test
    void SE_renvoieLeVoisinCorrect() {
        // Selon l'implémentation de Léopold : SE(FLAT) = (q+1, r, s-1)
        CoordinateCube c = new CoordinateCube(0, 0, 0);
        assertEquals(new CoordinateCube(1, 0, -1), (CoordinateCube) c.SE(Mode.FLAT));
    }

    @Test
    void NO_renvoieLeVoisinCorrect() {
        // Selon l'implémentation de Léopold : NO(FLAT) = (q-1, r, s+1)
        CoordinateCube c = new CoordinateCube(0, 0, 0);
        assertEquals(new CoordinateCube(-1, 0, 1), (CoordinateCube) c.NO(Mode.FLAT));
    }

    @Test
    void E_leveExceptionEnModeFLAT() {
        CoordinateCube c = new CoordinateCube(0, 0, 0);
        assertThrows(InvalidParameterException.class,
                () -> c.E(Mode.FLAT),
                "E ne devrait pas être valide en mode FLAT"
        );
    }

    @Test
    void O_leveExceptionEnModeFLAT() {
        CoordinateCube c = new CoordinateCube(0, 0, 0);
        assertThrows(InvalidParameterException.class,
                () -> c.O(Mode.FLAT),
                "O ne devrait pas être valide en mode FLAT"
        );
    }

    // -------------------------------------------------------------------------
    // Tests directions POINTY
    // -------------------------------------------------------------------------

    @Test
    void N_leveExceptionEnModePOINTY() {
        CoordinateCube c = new CoordinateCube(0, 0, 0);
        assertThrows(InvalidParameterException.class,
                () -> c.N(Mode.POINTY),
                "N ne devrait pas être valide en mode POINTY"
        );
    }

    @Test
    void S_leveExceptionEnModePOINTY() {
        CoordinateCube c = new CoordinateCube(0, 0, 0);
        assertThrows(InvalidParameterException.class,
                () -> c.S(Mode.POINTY),
                "S ne devrait pas être valide en mode POINTY"
        );
    }

    @Test
    void E_renvoieLeVoisinCorrectEnModePOINTY() {
        // Selon l'implémentation de Léopold : E(POINTY) = (q+1, r, s-1)
        CoordinateCube c = new CoordinateCube(0, 0, 0);
        assertEquals(new CoordinateCube(1, 0, -1), (CoordinateCube) c.E(Mode.POINTY));
    }

    // -------------------------------------------------------------------------
    // Tests getNeighbors
    // -------------------------------------------------------------------------

    @Test
    void getNeighbors_renvoieToujoursSixVoisins() {
        CoordinateCube c = new CoordinateCube(0, 0, 0);
        assertEquals(6, c.getNeighbors(Mode.FLAT).size(),
                "getNeighbors devrait toujours renvoyer 6 voisins"
        );
    }

    // -------------------------------------------------------------------------
    // Tests between
    // -------------------------------------------------------------------------

    @Test
    void between_renvoieLesCasesIntermediaires() throws DifferentAxisException {
        CoordinateCube from = new CoordinateCube(-2, 0, 2);
        CoordinateCube to   = new CoordinateCube(2, 0, -2);

        List<Coordinate> result = from.between(Mode.FLAT, to);

        assertEquals(3, result.size(),
                "Il devrait y avoir 3 cases entre [-2,0,2] et [2,0,-2]"
        );
    }

    @Test
    void between_leveExceptionSiPasMemeAxe() {
        CoordinateCube from = new CoordinateCube(0, 0, 0);
        CoordinateCube to   = new CoordinateCube(1, 1, -2);

        assertThrows(DifferentAxisException.class,
                () -> from.between(Mode.FLAT, to),
                "between devrait lever DifferentAxisException si pas sur le même axe"
        );
    }

    @Test
    void between_renvoieListeVideSiCasesAdjacentes() throws DifferentAxisException {
        CoordinateCube from = new CoordinateCube(0, 0, 0);
        CoordinateCube to   = new CoordinateCube(1, -1, 0);

        List<Coordinate> result = from.between(Mode.FLAT, to);
        assertTrue(result.isEmpty(),
                "between devrait renvoyer une liste vide pour des cases adjacentes"
        );
    }

    // -------------------------------------------------------------------------
    // Tests equals et hashCode
    // -------------------------------------------------------------------------

    @Test
    void equals_deuxCoordonneesIdentiques() {
        CoordinateCube a = new CoordinateCube(1, -1, 0);
        CoordinateCube b = new CoordinateCube(1, -1, 0);
        assertEquals(a, b, "Deux coordonnées identiques devraient être égales");
    }

    @Test
    void equals_deuxCoordonneesDifferentes() {
        CoordinateCube a = new CoordinateCube(1, -1, 0);
        CoordinateCube b = new CoordinateCube(0, 0, 0);
        assertNotEquals(a, b, "Deux coordonnées différentes ne devraient pas être égales");
    }

    @Test
    void hashCode_memeValeurPourCoordonneesEgales() {
        CoordinateCube a = new CoordinateCube(1, -1, 0);
        CoordinateCube b = new CoordinateCube(1, -1, 0);
        assertEquals(a.hashCode(), b.hashCode(),
                "Des coordonnées égales devraient avoir le même hashCode"
        );
    }
}