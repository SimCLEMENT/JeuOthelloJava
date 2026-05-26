package org.Coordinate;

/**
 * Représente un point 2D équivalent à une position dans le système {@link CoordinateDoubled}.
 *
 * @param x la colonne.
 * @param y la ligne.
 */
public record Point(int x, int y) {

    /**
     * Renvoie une représentation textuelle au format {@code [x,y]}.
     *
     * @return la chaîne représentant le point.
     */
    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}