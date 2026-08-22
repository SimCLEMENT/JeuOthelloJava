package fr.unicaen.info.othello.tokens;

import fr.unicaen.info.othello.state.Team;

/**
 * Classe abstraite représentant un token sur le plateau.
 * Un token peut être un pion ({@link Pawn}) ou un anneau ({@link Ring}).
 */
public abstract class Token {

    /** L'équipe à laquelle appartient ce token. */
    protected Team team;

    /**
     * Construit un token appartenant à l'équipe donnée.
     *
     * @param team l'équipe du token.
     */
    public Token(Team team) {
        this.team = team;
    }

    /**
     * Renvoie l'équipe à laquelle appartient ce token.
     *
     * @return l'équipe du token.
     */
    public Team getTeam() { return team; }

    /**
     * Modifie l'équipe de ce token.
     *
     * @param team la nouvelle équipe.
     */
    public void setTeam(Team team) { this.team = team; }

    /**
     * Renvoie la représentation console du token.
     *
     * @return un caractère représentant le token ({@code "."}, {@code "x"}, {@code "o"} ou {@code "O"}).
     */
    public abstract String charRepr();

    /**
     * Renvoie une copie de ce token avec la même équipe.
     *
     * @return un nouveau token identique.
     */
    public abstract Token clone();

    /**
     * Renvoie la représentation console du token.
     *
     * @return la chaîne renvoyée par {@link #charRepr()}.
     */
    @Override
    public String toString() { return charRepr(); }
}