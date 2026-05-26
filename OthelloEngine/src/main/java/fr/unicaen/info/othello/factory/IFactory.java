package fr.unicaen.info.othello.factory;

import fr.unicaen.info.othello.state.State;

/**
 * Interface définissant les méthodes de création des états de jeu.
 * Implémentée par {@link FactoryCube} et {@link FactoryDoubled}.
 */
public interface IFactory {

    /**
     * Crée et renvoie un terrain vierge sans pion ni anneau.
     *
     * @return un état initial vide.
     */
    State emptyState();

    /**
     * Crée et renvoie un état de test avec une ligne de 5 pions blancs.
     *
     * @return un état avec une ligne blanche prête à être supprimée.
     */
    State stateForWhiteLineTest();

    /**
     * Crée et renvoie un état de test avec une ligne de 5 pions noirs.
     *
     * @return un état avec une ligne noire prête à être supprimée.
     */
    State stateForBlackLineTest();

    /**
     * Crée et renvoie un état de test standard avec anneaux et pions.
     *
     * @return un état de test général.
     */
    State testState();

    /**
     * Crée et renvoie un état de test avec deux lignes simultanées.
     *
     * @return un état avec deux lignes prêtes à être supprimées.
     */
    State doubleLineStateTest();
}