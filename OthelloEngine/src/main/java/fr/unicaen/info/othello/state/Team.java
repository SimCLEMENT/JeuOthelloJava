package fr.unicaen.info.othello.state;

/**
 * Représente les deux équipes du jeu Yinsh.
 * Chaque équipe est identifiée par sa couleur.
 */
public enum Team {

    /** L'équipe blanche. */
    WHITE,

    /** L'équipe noire. */
    BLACK;

    /**
     * Renvoie l'équipe adverse.
     *
     * @return {@code BLACK} si l'équipe courante est {@code WHITE}, et inversement.
     */
    public Team opposite() {
        return this == WHITE ? BLACK : WHITE;
    }
}