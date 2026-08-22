package org.Coordinate;

import org.Enums.Mode;
import org.Exceptions.DifferentAxisException;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.security.InvalidParameterException;

/**
 * Implémentation du système de coordonnées "Doubled" pour un plateau hexagonal.
 * Les coordonnées sont représentées par (y, x) où les indices de colonne
 * progressent par pas de deux sur une même ligne.
 * La case centrale du terrain est en {@code [5, 9]}.
 */
public class CoordinateDoubled extends Coordinate {

    private final int x;
    private final int y;

    /**
     * Construit une coordonnée Doubled.
     *
     * @param y la ligne.
     * @param x la colonne.
     */
    public CoordinateDoubled(int y, int x) {
        this.x = x;
        this.y = y;
    }

    /**
     * Renvoie la colonne x.
     *
     * @return la valeur de x.
     */
    public int getX() { return x; }

    /**
     * Renvoie la ligne y.
     *
     * @return la valeur de y.
     */
    public int getY() { return y; }

    /**
     * {@inheritDoc}
     * En mode Doubled, renvoie directement {@code new Point(x, y)}.
     */
    @Override
    public Point to2DCoordinate() {
        return new Point(x, y);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException si {@code to} n'est pas une {@link CoordinateDoubled}.
     * @throws DifferentAxisException   si les deux coordonnées ne sont pas sur le même axe.
     */
    @Override
    public List<Coordinate> between(Mode mode, Coordinate to) throws DifferentAxisException {
        if (!(to instanceof CoordinateDoubled target)) {
            throw new IllegalArgumentException("Mauvais type de coordonnée de destination : doit être CoordinateDoubled");
        }
        int deltaX = target.x - this.x;
        int deltaY = target.y - this.y;
        int Xstep;
        int Ystep;
        int distance;
        if (deltaY == 0 && deltaX % 2 == 0) {
            if (deltaX > 0) { Xstep = 2; } else Xstep = -2;
            Ystep = 0;
            distance = Math.abs(deltaX) / 2;
        } else if (Math.abs(deltaX) == Math.abs(deltaY)) {
            Xstep = Integer.compare(deltaX, 0);
            Ystep = Integer.compare(deltaY, 0);
            distance = Math.abs(deltaX);
        } else throw new DifferentAxisException("Les deux coordonnées ne sont pas sur le même axe.");
        List<Coordinate> res = new ArrayList<>();
        for (int i = 1; i < distance; i++) {
            res.add(new CoordinateDoubled(this.y + i * Ystep, this.x + i * Xstep));
        }
        return res;
    }

    /** {@inheritDoc} */
    @Override
    public Coordinate NO(Mode mode) {
        if (mode == Mode.POINTY) return new CoordinateDoubled(y - 1, x - 1);
        return new CoordinateDoubled(y, x - 2);
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidParameterException si le mode est {@link Mode#POINTY}.
     */
    @Override
    public Coordinate N(Mode mode) {
        if (mode == Mode.POINTY) throw new InvalidParameterException("La direction N n'est pas acceptée en POINTY");
        return new CoordinateDoubled(y - 1, x - 1);
    }

    /** {@inheritDoc} */
    @Override
    public Coordinate NE(Mode mode) {
        return new CoordinateDoubled(y - 1, x + 1);
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidParameterException si le mode est {@link Mode#FLAT}.
     */
    @Override
    public Coordinate E(Mode mode) {
        if (mode == Mode.FLAT) throw new InvalidParameterException("La direction E n'est pas acceptée en FLAT");
        return new CoordinateDoubled(y, x + 2);
    }

    /** {@inheritDoc} */
    @Override
    public Coordinate SE(Mode mode) {
        if (mode == Mode.POINTY) return new CoordinateDoubled(y + 1, x + 1);
        return new CoordinateDoubled(y, x + 2);
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidParameterException si le mode est {@link Mode#POINTY}.
     */
    @Override
    public Coordinate S(Mode mode) {
        if (mode == Mode.POINTY) throw new InvalidParameterException("La direction S n'est pas acceptée en POINTY");
        return new CoordinateDoubled(y + 1, x + 1);
    }

    /** {@inheritDoc} */
    @Override
    public Coordinate SO(Mode mode) {
        return new CoordinateDoubled(y + 1, x - 1);
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidParameterException si le mode est {@link Mode#FLAT}.
     */
    @Override
    public Coordinate O(Mode mode) {
        if (mode == Mode.FLAT) throw new InvalidParameterException("La direction O n'est pas acceptée en FLAT");
        return new CoordinateDoubled(y, x - 2);
    }

    /**
     * Vérifie l'égalité entre deux coordonnées Doubled.
     *
     * @param obj l'objet à comparer.
     * @return {@code true} si les deux coordonnées ont les mêmes valeurs x et y.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CoordinateDoubled autre)) return false;
        return this.x == autre.x && this.y == autre.y;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Renvoie une représentation textuelle au format {@code [y, x]}.
     *
     * @return la chaîne représentant la coordonnée.
     */
    @Override
    public String toString() {
        return "[" + y + ", " + x + "]";
    }
}