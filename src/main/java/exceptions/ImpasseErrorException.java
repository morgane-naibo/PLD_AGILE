package exceptions;

import java.lang.Exception;

public class ImpasseErrorException extends Exception {

    // Constructeur par défaut avec un message par défaut
    public ImpasseErrorException() {
        super("Une erreur d'impasse s'est produite.");
    }

    // Constructeur qui accepte un message personnalisé
    public ImpasseErrorException(String message) {
        super(message);
    }

    // Constructeur qui accepte un message personnalisé et une cause (une autre exception)
    public ImpasseErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructeur qui accepte une cause sans message personnalisé
    public ImpasseErrorException(Throwable cause) {
        super(cause);
    }
}