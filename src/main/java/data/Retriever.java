package data;

/**
 * This class helps on retrieving a single piece of data
 * from a column based on the result of a query in a SQLite
 * database. Main idea behind creating it was to abstract
 * a lot of the messy details.
 */
public abstract class Retriever {

    // Name of column to retrieve from the result of the SQLite query.
    protected String key;

    /**
     * Create a new retriever.
     *
     * @param key Name of column to retrieve.
     */
    public Retriever(String key) {
        this.key = key;
    }
}