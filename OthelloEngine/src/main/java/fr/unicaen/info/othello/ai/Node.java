package fr.unicaen.info.othello.ai;

import fr.unicaen.info.othello.actions.Action;
import fr.unicaen.info.othello.state.IState;

/**
 * Représente un nœud dans l'arbre de recherche Minimax.
 * Chaque nœud stocke un état de jeu, son nœud parent et l'action qui y a mené.
 */
public class Node {

    /** L'état du jeu représenté par ce nœud. */
    private final IState state;

    /** Le nœud parent qui a permis d'atteindre ce nœud. */
    private final Node parent;

    /** L'action qui a permis de rejoindre ce nœud depuis le parent. */
    private final Action action;

    /**
     * Construit un nœud avec l'état, le parent et l'action donnés.
     *
     * @param state  l'état du jeu.
     * @param parent le nœud parent, ou {@code null} pour la racine.
     * @param action l'action qui a mené à cet état, ou {@code null} pour la racine.
     */
    public Node(IState state, Node parent, Action action) {
        this.state = state;
        this.parent = parent;
        this.action = action;
    }

    /**
     * Renvoie l'état du jeu associé à ce nœud.
     *
     * @return l'état du jeu.
     */
    public IState getState() { return state; }

    /**
     * Renvoie le nœud parent.
     *
     * @return le nœud parent, ou {@code null} si ce nœud est la racine.
     */
    public Node getParent() { return parent; }

    /**
     * Renvoie l'action qui a mené à ce nœud.
     *
     * @return l'action, ou {@code null} si ce nœud est la racine.
     */
    public Action getAction() { return action; }

    /**
     * Remonte l'arbre jusqu'à trouver le nœud enfant direct de la racine.
     *
     * @return le nœud enfant direct de la racine.
     */
    public Node getRoot() {
        Node current = this;
        while (current.getParent() != null && current.getParent().getParent() != null) {
            current = current.getParent();
        }
        return current;
    }
}