package exceptions;

import java.lang.Exception;

public class LongueTourneeException extends Exception{
    // Constructeur par défaut avec un message par défaut
    public LongueTourneeException() {
        super("Une erreur d'impasse s'est produite.");
    }

    // Constructeur qui accepte un message personnalisé
    public LongueTourneeException(String message) {
        super(message);
    }

    // Constructeur qui accepte un message personnalisé et une cause (une autre exception)
    public LongueTourneeException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructeur qui accepte une cause sans message personnalisé
    public LongueTourneeException(Throwable cause) {
        super(cause);
    }
}
