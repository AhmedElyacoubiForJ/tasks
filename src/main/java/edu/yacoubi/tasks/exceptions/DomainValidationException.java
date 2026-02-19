package edu.yacoubi.tasks.exceptions;

/**
 * 422 – Domain-Validierung fehlgeschlagen
 * Beispiel: fachliche Regeln zu Werten, Zuständen, Übergängen.
 */
public class DomainValidationException extends RuntimeException {
    public DomainValidationException(String message) {
        super(message);
    }
}
