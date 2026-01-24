package edu.yacoubi.tasks.domain.exception;

//🧩 Warum so aufgebaut?
//RuntimeException: Domain‑Fehler sind unchecked,
// weil sie fachliche Regeln verletzen und nicht durch den Compiler erzwungen werden sollen.
//
// Klare Trennung: DomainException = fachlich
// NotFoundException = technisch (Persistenz)
// PersistenceException = technisch (DB, Infrastruktur)
//
// Erweiterbar:
// Man kann später Subklassen bauen, z.B.:
//
//        TaskOwnershipException
//
//        InvalidTaskStateException
//
//        TaskListInvariantException

/**
 * Repräsentiert eine fachliche Ausnahme innerhalb des Domain-Layers.
 * <p>
 * Diese Exception wird geworfen, wenn eine fachliche Regel oder Invariante
 * verletzt wird. Sie dient dazu, die Business-Logik klar von technischen
 * Fehlern (z. B. Persistenz, Netzwerk) zu trennen.
 *
 * Beispiele:
 * <ul>
 *   <li>Ein Task gehört nicht zur angegebenen TaskList</li>
 *   <li>Eine Invariante im Aggregate wird verletzt</li>
 *   <li>Eine Domain-Operation ist in diesem Zustand nicht erlaubt</li>
 * </ul>
 */
public class DomainException extends RuntimeException {

    /**
     * Erstellt eine neue DomainException mit einer Nachricht.
     *
     * @param message die Beschreibung des fachlichen Fehlers
     */
    public DomainException(final String message) {
        super(message);
    }

    /**
     * Erstellt eine neue DomainException mit einer Nachricht und einer Ursache.
     *
     * @param message die Beschreibung des fachlichen Fehlers
     * @param cause   die zugrunde liegende Exception
     */
    public DomainException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

