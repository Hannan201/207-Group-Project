package cypher.enforcers.data.security;

/**
 * Data Transfer Object for the code. This prevents internal details that
 * aren't important from being leaked.
 */
public record Code(long id, String code) {
}
