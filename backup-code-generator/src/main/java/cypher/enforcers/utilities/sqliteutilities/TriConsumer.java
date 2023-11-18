package cypher.enforcers.utilities.sqliteutilities;

/**
 * A functional interface that accepts three values.
 *
 * @param <T> Type of the first value.
 * @param <U> Type of the second value.
 * @param <V> Type of the third value.
 */
public interface TriConsumer<T, U, V> {

    /**
     * Accept the three values to perform an action that should
     * not return anything.
     *
     * @param first The first value.
     * @param second The second value.
     * @param third The third value.
     */
    void accept(T first, U second, V third);

}
