package fr.unicaen.info.othello.ai;

import fr.unicaen.info.othello.actions.Action;
import fr.unicaen.info.othello.state.IState;

/**
 * Interface représentant une intelligence artificielle capable de jouer au jeu.
 * Toute IA doit implémenter cette interface.
 */
public interface AI {

    /**
     * Choisit et renvoie la meilleure action à jouer pour l'état donné.
     *
     * @param state l'état courant du jeu.
     * @return l'action choisie par l'IA, ou {@code null} si aucun coup n'est possible.
     */
    Action chooseMove(IState state);
}