package cypher.enforcers.data.implementations;

import cypher.enforcers.data.spis.DatabaseService;
import jakarta.persistence.*;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/*
 Implementation for the database service. This service acts as a way to
 send queries to the database, ensure connection reliability and
 deliver any important alerts.
 */
public class DatabaseServiceImpl implements DatabaseService {

    // Name to the properties file (persistence.xml as defined
    // by the JPA specification).
    private static final String DEFAULT_NAME = "cypher.enforcers.database";

    // Factory to create an EntityManager (allows to open
    // a session in hibernate).
    private EntityManagerFactory factory;

    /**
     * Create a connection to the database by providing the
     * properties.
     *
     * @param properties Properties to apply when making the
     *                   connection.
     */
    @Override
    public void connect(Map properties) {
        factory = new HibernatePersistenceProvider()
                .createContainerEntityManagerFactory(
                    archiverPersistenceUnitInfo(), properties
                );
    }

    /**
     * Connect to the database with the default properties.
     */
    @Override
    public void connect() {
        factory = Persistence.createEntityManagerFactory(DEFAULT_NAME);
    }

    /**
     * Get the entity manager provided by this database.
     *
     * @return Entity manager connected to this database.
     */
    @Override
    public EntityManager getManager() {
        return factory.createEntityManager();
    }

    /**
     * Disconnect from the current database.
     */
    @Override
    public void disconnect() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }

    /**
     * Return a custom implementation of the PersistenceUitIfo
     * to make a connection to a database without a configuration file.
     *
     * @return Custom implementation of the PersistenceUnitInfo.
     */
    private static PersistenceUnitInfo archiverPersistenceUnitInfo() {
        return new PersistenceUnitInfo() {
            @Override
            public String getPersistenceUnitName() {
                return "ApplicationPersistenceUnit";
            }

            @Override
            public String getPersistenceProviderClassName() {
                return "org.hibernate.jpa.HibernatePersistenceProvider";
            }

            @Override
            public PersistenceUnitTransactionType getTransactionType() {
                return PersistenceUnitTransactionType.RESOURCE_LOCAL;
            }

            @Override
            public DataSource getJtaDataSource() {
                return null;
            }

            @Override
            public DataSource getNonJtaDataSource() {
                return null;
            }

            @Override
            public List<String> getMappingFileNames() {
                return Collections.emptyList();
            }

            @Override
            public List<URL> getJarFileUrls() {
                try {
                    return Collections.list(this.getClass()
                            .getClassLoader()
                            .getResources(""));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }

            @Override
            public URL getPersistenceUnitRootUrl() {
                return null;
            }

            @Override
            public List<String> getManagedClassNames() {
                return Collections.emptyList();
            }

            @Override
            public boolean excludeUnlistedClasses() {
                return false;
            }

            @Override
            public SharedCacheMode getSharedCacheMode() {
                return null;
            }

            @Override
            public ValidationMode getValidationMode() {
                return null;
            }

            @Override
            public Properties getProperties() {
                return new Properties();
            }

            @Override
            public String getPersistenceXMLSchemaVersion() {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {
                return null;
            }

            @Override
            public void addTransformer(ClassTransformer transformer) {

            }

            @Override
            public ClassLoader getNewTempClassLoader() {
                return null;
            }
        };
    }
}
