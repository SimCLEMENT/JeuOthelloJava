package fr.unicaen.info.othello.tokens;

import fr.unicaen.info.othello.state.Team;

/**
 * Représente un anneau sur le plateau.
 * Un anneau blanc est affiché {@code "o"} et un anneau noir {@code "O"}.
 */
public class Ring extends Token {

    /**
     * Construit un anneau de la couleur donnée.
     *
     * @param color la couleur de l'anneau.
     */
    public Ring(Team color) {
        super(color);
    }

    /**
     * Renvoie la représentation console de l'anneau.
     *
     * @return {@code "O"} si l'anneau est noir, {@code "o"} s'il est blanc.
     */
    @Override
    public String charRepr() {
        return this.team == Team.BLACK ? "O" : "o";
    }

    /**
     * Renvoie une copie de cet anneau avec la même équipe.
     *
     * @return un nouveau {@link Ring} identique.
     */
    @Override
    public Token clone() {
        return new Ring(this.team);
    }
}