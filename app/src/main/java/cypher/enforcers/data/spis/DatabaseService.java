package cypher.enforcers.data.spis;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Map;
import java.util.function.Consumer;

/*
 Interface for the database service. This service acts as a way to
 send queries to the database, ensure connection reliability and
 deliver any important alerts.
 */
public interface DatabaseService {

    /**
     * Create a connection to the database by providing the
     * properties.
     *
     * @param properties Properties to apply when making the
     *                   connection.
     */
    void connect(Map properties);

    /**
     * Connect to the database with the default properties.
     */
    void connect();

    /**
     * Get the entity manager provided by this database.
     *
     * @return Entity manager connected to this database.
     */
    EntityManager getManager();

    /**
     * Perform an action to this database in a transaction.
     * An EntityManager is provided to make any changes.
     * <br>
     * If any exception occurs during the transaction, the database
     * will be rolled back to its state before this transaction began.
     * <br>
     * NOTE: The JPA interfaces are used during the transaction.
     *
     * @param work Action to perform in the transaction, with an
     *             entity manager provided.
     */
    default void inTransaction(Consumer<EntityManager> work) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = getManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            work.accept(entityManager);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    /**
     * Disconnect from the current database.
     */
    void disconnect();

}
