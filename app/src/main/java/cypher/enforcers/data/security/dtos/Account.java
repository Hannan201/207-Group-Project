package cypher.enforcers.data.security.dtos;

/**
 * Data Transfer Object for the account. This prevents internal details that
 * aren't important from being leaked.
 */
public record Account(long id, String name, String socialMediaType) {
}
