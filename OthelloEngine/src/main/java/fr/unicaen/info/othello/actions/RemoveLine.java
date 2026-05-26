package fr.unicaen.info.othello.actions;

import org.Coordinate.Coordinate;
import java.util.Set;

/**
 * Représente l'action de supprimer une ligne de 5 pions et un anneau du plateau.
 */
public class RemoveLine extends Action {

    /** L'ensemble des 5 coordonnées de la ligne à supprimer. */
    private Set<Coordinate> line;

    /** La coordonnée de l'anneau à retirer. */
    private Coordinate ring;

    /**
     * Construit une action de suppression de ligne.
     *
     * @param line l'ensemble des 5 coordonnées de la ligne.
     * @param ring la coordonnée de l'anneau à retirer.
     */
    public RemoveLine(Set<Coordinate> line, Coordinate ring) {
        this.line = line;
        this.ring = ring;
    }

    /**
     * Renvoie l'ensemble des coordonnées de la ligne à supprimer.
     *
     * @return les 5 coordonnées de la ligne.
     */
    public Set<Coordinate> getLine() { return line; }

    /**
     * Renvoie la coordonnée de l'anneau à retirer.
     *
     * @return la coordonnée de l'anneau.
     */
    public Coordinate getRing() { return ring; }

    /**
     * Modifie la ligne à supprimer.
     *
     * @param line le nouvel ensemble de coordonnées.
     */
    public void setLine(Set<Coordinate> line) { this.line = line; }

    /**
     * Modifie l'anneau à retirer.
     *
     * @param ring la nouvelle coordonnée de l'anneau.
     */
    public void setRing(Coordinate ring) { this.ring = ring; }
}