package edu.yacoubi.tasks.domain.exceptions;

/**
 * 409 – Domainregel verletzt
 * Beispiel: Archivieren, obwohl noch offene Tasks existieren.
 */
public class DomainRuleViolationException extends RuntimeException {
    public DomainRuleViolationException(String message) {
        super(message);
    }
}
