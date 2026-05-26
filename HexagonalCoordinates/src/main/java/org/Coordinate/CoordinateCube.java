package org.Coordinate;

import org.Exceptions.DifferentAxisException;
import org.Enums.Mode;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du système de coordonnées cubiques pour un plateau hexagonal.
 * Repose sur trois axes (q, r, s) disposés à 120° les uns des autres.
 * La contrainte {@code q + r + s = 0} doit toujours être respectée.
 * La case centrale du terrain est en {@code [0, 0, 0]}.
 */
public class CoordinateCube extends Coordinate {

    private final int q;
    private final int r;
    private final int s;

    /**
     * Construit une coordonnée cubique.
     *
     * @param q la composante q.
     * @param r la composante r.
     * @param s la composante s.
     * @throws IllegalArgumentException si {@code q + r + s != 0}.
     */
    public CoordinateCube(int q, int r, int s) {
        if (q + r + s != 0) {
            throw new IllegalArgumentException("La somme des coordonnées doit être égale à 0");
        }
        this.q = q;
        this.r = r;
        this.s = s;
    }

    /**
     * Renvoie la composante q.
     *
     * @return la valeur de q.
     */
    public int getQ() { return this.q; }

    /**
     * Renvoie la composante r.
     *
     * @return la valeur de r.
     */
    public int getR() { return this.r; }

    /**
     * Renvoie la composante s.
     *
     * @return la valeur de s.
     */
    public int getS() { return this.s; }

    /**
     * {@inheritDoc}
     * Convertit la coordonnée cubique en point 2D centré sur [9, 5].
     */
    @Override
    public Point to2DCoordinate() {
        int x = 2 * this.q + this.r + 9;
        int y = this.r + 5;
        return new Point(x, y);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException si {@code to} n'est pas une {@link CoordinateCube}.
     * @throws DifferentAxisException   si les deux coordonnées ne sont pas sur le même axe.
     */
    @Override
    public List<Coordinate> between(Mode mode, Coordinate to) throws DifferentAxisException {
        if (!(to instanceof CoordinateCube target)) {
            throw new IllegalArgumentException("La coordonnée de destination doit être de type CoordinateCube");
        }
        if (this.q != target.getQ() && this.r != target.getR() && this.s != target.getS()) {
            throw new DifferentAxisException("Les deux coordonnées ne sont pas sur le même axe");
        }
        List<Coordinate> coordinatesBetween = new ArrayList<>();
        int dq = target.getQ() - this.q;
        int dr = target.getR() - this.r;
        int ds = target.getS() - this.s;
        int distance = Math.max(Math.abs(dq), Math.max(Math.abs(dr), Math.abs(ds)));
        int stepQ = (dq == 0) ? 0 : dq / distance;
        int stepR = (dr == 0) ? 0 : dr / distance;
        int stepS = (ds == 0) ? 0 : ds / distance;
        for (int i = 1; i < distance; i++) {
            coordinatesBetween.add(new CoordinateCube(this.q + i * stepQ, this.r + i * stepR, this.s + i * stepS));
        }
        return coordinatesBetween;
    }

    /** {@inheritDoc} */
    @Override
    public Coordinate NO(Mode mode) {
        if (mode == Mode.POINTY) return new CoordinateCube(this.q, this.r - 1, this.s + 1);
        return new CoordinateCube(this.q - 1, this.r, this.s + 1);
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidParameterException si le mode est {@link Mode#POINTY}.
     */
    @Override
    public Coordinate N(Mode mode) {
        if (mode == Mode.POINTY) throw new InvalidParameterException("La direction N n'est pas acceptée en mode POINTY");
        return new CoordinateCube(this.q, this.r - 1, this.s + 1);
    }

    /** {@inheritDoc} */
    @Override
    public Coordinate NE(Mode mode) {
        return new CoordinateCube(this.q + 1, this.r - 1, this.s);
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidParameterException si le mode est {@link Mode#FLAT}.
     */
    @Override
    public Coordinate E(Mode mode) {
        if (mode == Mode.FLAT) throw new InvalidParameterException("La direction E n'est pas acceptée en mode FLAT");
        return new CoordinateCube(this.q + 1, this.r, this.s - 1);
    }

    /** {@inheritDoc} */
    @Override
    public Coordinate SE(Mode mode) {
        if (mode == Mode.POINTY) return new CoordinateCube(this.q, this.r + 1, this.s - 1);
        return new CoordinateCube(this.q + 1, this.r, this.s - 1);
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidParameterException si le mode est {@link Mode#POINTY}.
     */
    @Override
    public Coordinate S(Mode mode) {
        if (mode == Mode.POINTY) throw new InvalidParameterException("La direction S n'est pas acceptée en mode POINTY");
        return new CoordinateCube(this.q, this.r + 1, this.s - 1);
    }

    /** {@inheritDoc} */
    @Override
    public Coordinate SO(Mode mode) {
        return new CoordinateCube(this.q - 1, this.r + 1, this.s);
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidParameterException si le mode est {@link Mode#FLAT}.
     */
    @Override
    public Coordinate O(Mode mode) {
        if (mode == Mode.FLAT) throw new InvalidParameterException("La direction O n'est pas acceptée en mode FLAT");
        return new CoordinateCube(this.q - 1, this.r, this.s + 1);
    }

    /**
     * Vérifie l'égalité entre deux coordonnées cubiques.
     *
     * @param obj l'objet à comparer.
     * @return {@code true} si les deux coordonnées ont les mêmes composantes q, r et s.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CoordinateCube other)) return false;
        return this.q == other.q && this.r == other.r && this.s == other.s;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = q;
        result = 31 * result + r;
        result = 31 * result + s;
        return result;
    }

    /**
     * Renvoie une représentation textuelle de la coordonnée au format {@code [q, r, s]}.
     *
     * @return la chaîne représentant la coordonnée.
     */
    @Override
    public String toString() {
        return "[" + q + ", " + r + ", " + s + "]";
    }
}