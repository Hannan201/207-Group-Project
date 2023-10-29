package cypher.enforcers.data.security;

import cypher.enforcers.views.themes.Theme;

/**
 * Data Transfer Object for the user. This prevents internal details that
 * aren't important from being leaked.
 */
public record UserDTO(long id, String username, Theme theme) {

}
