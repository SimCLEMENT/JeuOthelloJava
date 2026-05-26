package fr.unicaen.info.othello.ai;

import fr.unicaen.info.othello.actions.Action;
import fr.unicaen.info.othello.factory.FactoryDoubled;
import fr.unicaen.info.othello.state.IState;

/**
 * Classe principale permettant de faire jouer l'IA contre elle-même.
 * Utilise un état de test généré par {@link FactoryDoubled} et fait jouer
 * {@link MinmaxAI} jusqu'à la fin de la partie.
 */
public class MainAI {

    /**
     * Point d'entrée de la simulation IA vs IA.
     *
     * @param args les arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        FactoryDoubled factory = new FactoryDoubled();
        IState state = factory.testState();
        MinmaxAI ai = new MinmaxAI();

        while (!state.isOver()) {
            Action action = ai.chooseMove(state);
            if (action == null) {
                break;
            }
            state = ai.applyAction(state, action);
            System.out.println("Action jouée : " + action);
            System.out.println(state);
        }

        System.out.println("Fin de partie. Vainqueur : " + state.winner());
    }
}