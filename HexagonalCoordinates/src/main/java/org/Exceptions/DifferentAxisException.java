package org.Exceptions;

/**
 * Exception levée lorsque deux coordonnées ne sont pas sur le même axe hexagonal.
 * Utilisée notamment par la méthode {@code between} des coordonnées.
 */
public class DifferentAxisException extends RuntimeException {

    /**
     * Construit une exception avec un message descriptif.
     *
     * @param message le message décrivant la cause de l'exception.
     */
    public DifferentAxisException(String message) {
        super(message);
    }
}