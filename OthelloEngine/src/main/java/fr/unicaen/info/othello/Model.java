package fr.unicaen.info.othello;

import fr.unicaen.info.othello.state.IState;
import fr.unicaen.info.othello.state.Team;
import fr.unicaen.info.othello.actions.Move;
import fr.unicaen.info.othello.actions.RemoveLine;

/**
 * Classe s'occupant de stocker l'état du jeu en cours et de le mettre à jour.
 * Les méthodes qui agissent sur l'état modifient l'état stocké dans le modèle
 * et ne renvoient donc rien.
 */
public class Model {

    /** L'état courant du jeu. */
    private IState currentState;

    /**
     * Construit un modèle avec l'état initial donné.
     *
     * @param initialState l'état initial du jeu.
     */
    public Model(IState initialState) {
        this.currentState = initialState;
    }

    /**
     * Renvoie l'état courant du jeu.
     *
     * @return l'état courant.
     */
    public IState getCurrentState() {
        return currentState;
    }

    /**
     * Renvoie l'équipe dont c'est le tour de jouer.
     *
     * @return l'équipe courante.
     */
    public Team getCurrentTeam() {
        return currentState.turn();
    }

    /**
     * Exécute un déplacement d'anneau et met à jour l'état interne.
     *
     * @param move l'action de déplacement à effectuer.
     */
    public void moveRing(Move move) {
        this.currentState = currentState.move(move);
    }

    /**
     * Supprime une ligne et un anneau, puis met à jour l'état interne.
     *
     * @param removeLineAction l'action de suppression à effectuer.
     */
    public void removeLine(RemoveLine removeLineAction) {
        this.currentState = currentState.removeLine(removeLineAction);
    }

    /**
     * Remplace directement l'état courant par un nouvel état.
     *
     * @param newState le nouvel état à utiliser.
     */
    public void setState(IState newState) {
        this.currentState = newState;
    }

    /**
     * Renvoie un résumé du modèle via l'état courant.
     *
     * @return une chaîne décrivant l'état courant.
     */
    @Override
    public String toString() {
        return "Model{" + currentState + "}";
    }
}