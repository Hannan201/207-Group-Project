package cypher.enforcers.utilities.sqliteutilities.argumentsetters;

public interface TriConsumer<T, U, V> {

    void accept(T first, U second, V third);

}
