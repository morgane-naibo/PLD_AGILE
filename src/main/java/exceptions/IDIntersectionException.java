package exceptions;

import java.lang.Exception;

public class IDIntersectionException extends Exception {
    // Constructeur par défaut avec un message par défaut
    public IDIntersectionException() {
        super("Une erreur d'impasse s'est produite.");
    }

    // Constructeur qui accepte un message personnalisé
    public IDIntersectionException(String message) {
        super(message);
    }

    // Constructeur qui accepte un message personnalisé et une cause (une autre exception)
    public IDIntersectionException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructeur qui accepte une cause sans message personnalisé
    public IDIntersectionException(Throwable cause) {
        super(cause);
    }
}
