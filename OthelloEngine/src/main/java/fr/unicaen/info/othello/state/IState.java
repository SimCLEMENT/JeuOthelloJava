package fr.unicaen.info.othello.state;

import fr.unicaen.info.othello.tokens.Token;
import fr.unicaen.info.othello.actions.Move;
import org.Coordinate.Coordinate;
import org.Enums.Mode;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface générique représentant l'état du jeu.
 * Permet de dissocier l'implémentation ({@link State}) de son utilisation.
 */
public interface IState {

    /**
     * Renvoie la map du plateau associant chaque coordonnée à son token.
     *
     * @return la map du plateau.
     */
    Map<Coordinate, Token> board();

    /**
     * Renvoie l'équipe dont c'est le tour de jouer.
     *
     * @return l'équipe courante.
     */
    Team turn();

    /**
     * Renvoie la liste des lignes de 5 pions actuellement présentes sur le plateau.
     *
     * @return la liste des lignes actives.
     */
    List<Set<Coordinate>> lines();

    /**
     * Déplace un anneau selon l'action donnée et renvoie le nouvel état.
     *
     * @param move l'action de déplacement.
     * @return le nouvel état après déplacement.
     * @throws IllegalArgumentException si l'anneau ou la destination est invalide.
     * @throws RuntimeException         si une ligne est en attente de suppression.
     */
    IState move(Move move) throws IllegalArgumentException;

    /**
     * Supprime une ligne de pions et un anneau, puis renvoie le nouvel état.
     *
     * @param removeLine l'action de suppression.
     * @return le nouvel état après suppression.
     * @throws RuntimeException si la ligne est invalide.
     */
    IState removeLine(Object removeLine);

    /**
     * Cherche et renvoie toutes les lignes de 5 pions sur le plateau.
     *
     * @return la liste des lignes de 5 pions.
     */
    List<Set<Coordinate>> getPawnLines();

    /**
     * Renvoie les coordonnées des anneaux de chaque équipe.
     *
     * @return une map associant chaque équipe à la liste de ses anneaux.
     */
    Map<Team, List<Coordinate>> rings();

    /**
     * Supprime le token à la coordonnée donnée et renvoie le nouvel état.
     *
     * @param c la coordonnée du token à supprimer.
     * @return le nouvel état sans le token.
     * @throws IndexOutOfBoundsException si la coordonnée est hors du terrain.
     */
    IState removeToken(Coordinate c);

    /**
     * Place ou retire dynamiquement un token via réflexion Java.
     * Si la case contient déjà le même token de la même équipe, on le supprime.
     *
     * @param position   la coordonnée ciblée.
     * @param team       l'équipe du token.
     * @param token      la classe du token à instancier.
     * @return le nouvel état mis à jour.
     */
    IState toggleToken(Coordinate position, Team team, Class<?> token);

    /**
     * Renvoie l'équipe gagnante, ou {@code null} si la partie n'est pas terminée.
     *
     * @return l'équipe gagnante ou {@code null}.
     */
    Team winner();

    /**
     * Renvoie les cases accessibles depuis la coordonnée donnée.
     *
     * @param from la coordonnée de départ.
     * @return l'ensemble des cases accessibles.
     */
    Set<Coordinate> availableMoves(Coordinate from);

    /**
     * Renvoie les cases accessibles depuis la coordonnée donnée selon le mode spécifié.
     *
     * @param from la coordonnée de départ.
     * @param mode le mode d'orientation du plateau.
     * @return l'ensemble des cases accessibles.
     */
    Set<Coordinate> availableMoves(Coordinate from, Mode mode);

    /**
     * Renvoie {@code true} si la partie est terminée.
     *
     * @return {@code true} si un joueur a gagné ou si aucun anneau ne peut bouger.
     */
    boolean isOver();

    /**
     * Renvoie une représentation 2D du plateau sous forme de tableau.
     *
     * @return un tableau 2D des équipes par case.
     */
    Team[][] getBoard();
}