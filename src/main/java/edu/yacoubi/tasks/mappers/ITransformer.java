package edu.yacoubi.tasks.mappers;

@FunctionalInterface
public interface ITransformer<T, R> {
    /**
     * Transformiert eine Instanz der Eingabetyp {@code T} in eine Instanz des Rückgabetyps {@code R}.
     *
     * @param t die zu transformierende Instanz der Eingabetyp
     * @return die transformierte Instanz des Rückgabetyps
     */
    R transform(T t);
}
