package org.Enums;

/**
 * Représente les deux orientations possibles d'un plateau hexagonal.
 * <ul>
 *   <li>{@link #POINTY} : les hexagones ont la pointe vers le haut,
 *       ils possèdent donc des voisins à gauche et à droite.</li>
 *   <li>{@link #FLAT} : les hexagones ont la pointe vers la droite,
 *       ils possèdent donc des voisins en haut et en bas.</li>
 * </ul>
 */
public enum Mode {

    /** Hexagones avec la pointe vers le haut. */
    POINTY,

    /** Hexagones avec la pointe vers la droite. */
    FLAT
}