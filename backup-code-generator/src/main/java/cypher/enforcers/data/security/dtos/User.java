package cypher.enforcers.data.security.dtos;

import cypher.enforcers.views.themes.Theme;

/**
 * Data Transfer Object for the user. This prevents internal details that
 * aren't important from being leaked.
 *
 * @param id       The ID of the user.
 * @param username The username of the user.
 * @param theme    The theme of the user.
 */
public record User(long id, String username, Theme theme) {

}
