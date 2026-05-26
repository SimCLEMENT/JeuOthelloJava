package org.Coordinate;

import org.Enums.Direction;
import org.Enums.Mode;
import org.Exceptions.DifferentAxisException;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite représentant une coordonnée sur un plateau hexagonal.
 * Sert d'interface commune pour les différents systèmes de coordonnées
 * ({@link CoordinateCube}, {@link CoordinateDoubled}).
 */
public abstract class Coordinate {

    /**
     * Convertit cette coordonnée en un {@link Point} 2D équivalent à une {@link CoordinateDoubled}.
     *
     * @return le point 2D correspondant à cette coordonnée.
     */
    public abstract Point to2DCoordinate();

    /**
     * Renvoie le voisin de cette coordonnée dans la direction donnée.
     *
     * @param mode      le mode d'orientation du plateau ({@link Mode#FLAT} ou {@link Mode#POINTY}).
     * @param direction la direction vers laquelle se déplacer.
     * @return la coordonnée voisine dans la direction spécifiée.
     * @throws InvalidParameterException si la direction n'est pas valide pour le mode donné.
     */
    public Coordinate toDir(Mode mode, Direction direction) {
        return switch (direction) {
            case NO -> NO(mode);
            case N -> N(mode);
            case NE -> NE(mode);
            case E -> E(mode);
            case SE -> SE(mode);
            case S -> S(mode);
            case SO -> SO(mode);
            case O -> O(mode);
        };
    }

    /**
     * Renvoie les 6 voisins de cette coordonnée selon le mode donné.
     * Renvoie toujours exactement 6 voisins, même si certains sont hors du terrain.
     *
     * @param mode le mode d'orientation du plateau.
     * @return la liste des 6 coordonnées voisines.
     * @throws InvalidParameterException si le mode est inconnu.
     */
    public List<Coordinate> getNeighbors(Mode mode) {
        List<Coordinate> neighbors = new ArrayList<>();

        if (mode == Mode.FLAT) {
            neighbors.add(NO(mode));
            neighbors.add(N(mode));
            neighbors.add(NE(mode));
            neighbors.add(SE(mode));
            neighbors.add(S(mode));
            neighbors.add(SO(mode));
            return neighbors;
        }

        if (mode == Mode.POINTY) {
            neighbors.add(NO(mode));
            neighbors.add(NE(mode));
            neighbors.add(E(mode));
            neighbors.add(SE(mode));
            neighbors.add(SO(mode));
            neighbors.add(O(mode));
            return neighbors;
        }

        throw new InvalidParameterException("Mode inconnu : " + mode);
    }

    /**
     * Renvoie la liste des coordonnées situées entre cette coordonnée et {@code to} (exclusifs).
     *
     * @param mode le mode d'orientation du plateau.
     * @param to   la coordonnée de destination.
     * @return la liste des coordonnées intermédiaires.
     * @throws DifferentAxisException si les deux coordonnées ne sont pas sur le même axe.
     */
    public abstract List<Coordinate> between(Mode mode, Coordinate to) throws DifferentAxisException;

    /**
     * Renvoie le voisin Nord-Ouest.
     *
     * @param mode le mode d'orientation du plateau.
     * @return la coordonnée voisine au Nord-Ouest.
     */
    public abstract Coordinate NO(Mode mode);

    /**
     * Renvoie le voisin Nord.
     * Non valide en mode {@link Mode#POINTY}.
     *
     * @param mode le mode d'orientation du plateau.
     * @return la coordonnée voisine au Nord.
     * @throws InvalidParameterException si le mode est {@link Mode#POINTY}.
     */
    public abstract Coordinate N(Mode mode);

    /**
     * Renvoie le voisin Nord-Est.
     *
     * @param mode le mode d'orientation du plateau.
     * @return la coordonnée voisine au Nord-Est.
     */
    public abstract Coordinate NE(Mode mode);

    /**
     * Renvoie le voisin Est.
     * Non valide en mode {@link Mode#FLAT}.
     *
     * @param mode le mode d'orientation du plateau.
     * @return la coordonnée voisine à l'Est.
     * @throws InvalidParameterException si le mode est {@link Mode#FLAT}.
     */
    public abstract Coordinate E(Mode mode);

    /**
     * Renvoie le voisin Sud-Est.
     *
     * @param mode le mode d'orientation du plateau.
     * @return la coordonnée voisine au Sud-Est.
     */
    public abstract Coordinate SE(Mode mode);

    /**
     * Renvoie le voisin Sud.
     * Non valide en mode {@link Mode#POINTY}.
     *
     * @param mode le mode d'orientation du plateau.
     * @return la coordonnée voisine au Sud.
     * @throws InvalidParameterException si le mode est {@link Mode#POINTY}.
     */
    public abstract Coordinate S(Mode mode);

    /**
     * Renvoie le voisin Sud-Ouest.
     *
     * @param mode le mode d'orientation du plateau.
     * @return la coordonnée voisine au Sud-Ouest.
     */
    public abstract Coordinate SO(Mode mode);

    /**
     * Renvoie le voisin Ouest.
     * Non valide en mode {@link Mode#FLAT}.
     *
     * @param mode le mode d'orientation du plateau.
     * @return la coordonnée voisine à l'Ouest.
     * @throws InvalidParameterException si le mode est {@link Mode#FLAT}.
     */
    public abstract Coordinate O(Mode mode);
}