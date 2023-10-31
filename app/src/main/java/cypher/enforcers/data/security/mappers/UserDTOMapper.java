package cypher.enforcers.data.security.mappers;

import cypher.enforcers.data.entities.UserEntity;
import cypher.enforcers.data.security.dtos.User;

import java.util.function.Function;

/**
 * Converts a user object to a user data-transfer-object.
 */
public class UserDTOMapper implements Function<UserEntity, User> {
    @Override
    public User apply(UserEntity user) {
        return new User(user.getID(), user.getUsername(), user.getTheme());
    }
}
