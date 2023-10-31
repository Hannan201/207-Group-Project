package cypher.enforcers.utilities.sqliteutilities;

import cypher.enforcers.data.entities.CodeEntity;
import cypher.enforcers.data.entities.AccountEntity;
import cypher.enforcers.data.entities.UserEntity;
import cypher.enforcers.views.themes.Theme;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.Function;

/**
 * This class is used to define how different types of object should be
 * retrieved from a result set after executing a SELECT SQL statement.
 */
public class Retrievers {

    // How to retrieve a user from a result set.
    private static final Function<ResultSet, UserEntity> FOR_USER = resultSet -> {
        UserEntity user;
        try {
            if (!resultSet.isBeforeFirst()) {
                return null;
            }

            user = new UserEntity();
            user.setID(resultSet.getLong("id"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setTheme(Theme.values()[resultSet.getInt("theme_value")]);
            boolean result = resultSet.getShort("logged_in") == 1;
            user.setLoggedIn(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    };

    // How to retrieve a theme from the result set.
    private static final Function<ResultSet, Theme> FOR_THEME = resultSet -> {
        try {
            return Theme.values()[resultSet.getInt("theme_value")];
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    // How to retrieve an account from the result set.
    private static final Function<ResultSet, AccountEntity> FOR_ACCOUNT = resultSet -> {
        try {
            if (resultSet.getRow() == 0 && !resultSet.isBeforeFirst()) {
                return null;
            }

            AccountEntity account = new AccountEntity();
            account.setId(resultSet.getLong("id"));
            account.setName(resultSet.getString("name"));
            account.setSocialMediaType(resultSet.getString("type"));
            account.setUserId(resultSet.getLong("user_id"));
            return account;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    // How to retrieve a code from the result set.
    private static final Function<ResultSet, CodeEntity> FOR_CODE = resultSet -> {
        try {
            if (resultSet.getRow() == 0 && !resultSet.isBeforeFirst()) {
                return null;
            }

            CodeEntity code = new CodeEntity();
            code.setId(resultSet.getLong("id"));
            code.setCode(resultSet.getString("code"));
            code.setAccountID(resultSet.getLong("account_id"));
            return code;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    // Maps the socialMediaType of object to how it should be retrieved.
    private static final Map<Class<?>, Function<ResultSet, ?>> OBJECT_TYPE_TO_RETRIEVER =
            Map.ofEntries(
                    Map.entry(UserEntity.class, FOR_USER),
                    Map.entry(Theme.class, FOR_THEME),
                    Map.entry(AccountEntity.class, FOR_ACCOUNT),
                    Map.entry(CodeEntity.class, FOR_CODE)
            );

    /**
     * Get a retriever that can retrieve a specific socialMediaType of object from
     * a result set.
     *
     * @param type The class of the object to retrieve.
     * @param <T> The socialMediaType of data to retrieve.
     * @return A retriever that knows how to retrieve the object from
     * the result set.
     */
    @SuppressWarnings("unchecked")
    public static <T> Function<ResultSet, T> get(Class<T> type) {
        return (Function<ResultSet, T>) OBJECT_TYPE_TO_RETRIEVER.get(type);
    }
}
