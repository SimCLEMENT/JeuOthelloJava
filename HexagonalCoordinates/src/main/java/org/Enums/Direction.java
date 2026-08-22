package org.Enums;

/**
 * Représente les 8 directions possibles sur un plateau hexagonal.
 * Toutes les directions ne sont pas valides selon le mode utilisé
 * ({@link Mode#FLAT} ou {@link Mode#POINTY}).
 */
public enum Direction {

    /** Nord-Ouest. */
    NO,
    /** Nord. Non valide en mode {@link Mode#POINTY}. */
    N,
    /** Nord-Est. */
    NE,
    /** Est. Non valide en mode {@link Mode#FLAT}. */
    E,
    /** Sud-Est. */
    SE,
    /** Sud. Non valide en mode {@link Mode#POINTY}. */
    S,
    /** Sud-Ouest. */
    SO,
    /** Ouest. Non valide en mode {@link Mode#FLAT}. */
    O;

    /**
     * Renvoie la direction opposée.
     *
     * @return la direction diamétralement opposée.
     * @throws IllegalArgumentException si la direction est inconnue.
     */
    public Direction opposite() {
        switch (this) {
            case NO: return SE;
            case N:  return S;
            case NE: return SO;
            case E:  return O;
            case SE: return NO;
            case S:  return N;
            case SO: return NE;
            case O:  return E;
        }
        throw new IllegalArgumentException("Direction inconnue : " + this);
    }
}