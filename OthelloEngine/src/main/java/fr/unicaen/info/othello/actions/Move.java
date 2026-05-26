package fr.unicaen.info.othello.actions;

import org.Coordinate.Coordinate;

/**
 * Représente l'action de déplacer un anneau d'une case à une autre.
 */
public class Move extends Action {

    /** La coordonnée de départ de l'anneau. */
    Coordinate from;

    /** La coordonnée d'arrivée de l'anneau. */
    Coordinate to;

    /**
     * Construit un mouvement de {@code from} vers {@code to}.
     *
     * @param from la coordonnée de départ.
     * @param to   la coordonnée d'arrivée.
     */
    public Move(Coordinate from, Coordinate to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Renvoie la coordonnée de départ.
     *
     * @return la case de départ.
     */
    public Coordinate getFrom() { return from; }

    /**
     * Renvoie la coordonnée d'arrivée.
     *
     * @return la case d'arrivée.
     */
    public Coordinate getTo() { return to; }

    /**
     * Modifie la coordonnée de départ.
     *
     * @param from la nouvelle case de départ.
     */
    public void setFrom(Coordinate from) { this.from = from; }

    /**
     * Modifie la coordonnée d'arrivée.
     *
     * @param to la nouvelle case d'arrivée.
     */
    public void setTo(Coordinate to) { this.to = to; }
}