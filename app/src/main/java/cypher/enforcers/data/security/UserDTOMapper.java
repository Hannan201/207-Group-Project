package cypher.enforcers.data.security;

import cypher.enforcers.models.User;

import java.util.function.Function;

/**
 * Converts a user object to a user data-transfer-object.
 */
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(user.getID(), user.getUsername(), user.getTheme());
    }
}
