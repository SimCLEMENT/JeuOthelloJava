package fr.unicaen.info.othello.tokens;

import fr.unicaen.info.othello.state.Team;

/**
 * Représente un pion sur le plateau.
 * Un pion blanc est affiché {@code "."} et un pion noir {@code "x"}.
 */
public class Pawn extends Token {

    /**
     * Construit un pion de la couleur donnée.
     *
     * @param color la couleur du pion.
     */
    public Pawn(Team color) {
        super(color);
    }

    /**
     * Inverse la couleur du pion.
     * Utilisé lorsqu'un anneau survole ce pion lors d'un déplacement.
     */
    public void changeTeam() {
        this.team = this.team.opposite();
    }

    /**
     * Renvoie la représentation console du pion.
     *
     * @return {@code "x"} si le pion est noir, {@code "."} s'il est blanc.
     */
    @Override
    public String charRepr() {
        return this.team == Team.BLACK ? "x" : ".";
    }

    /**
     * Renvoie une copie de ce pion avec la même équipe.
     *
     * @return un nouveau {@link Pawn} identique.
     */
    @Override
    public Token clone() {
        return new Pawn(this.team);
    }
}