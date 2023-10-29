package cypher.enforcers.data.security;

/**
 * Data Transfer Object for the account. This prevents internal details that
 * aren't important from being leaked.
 */
public record AccountDTO(long id, String name, String type) {
}
