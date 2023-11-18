package cypher.enforcers.data.security.dtos;

/**
 * Data Transfer Object for the account. This prevents internal details that
 * aren't important from being leaked.
 *
 * @param id              The ID of the account.
 * @param name            The name of the account.
 * @param socialMediaType The social media type of the account.
 */
public record Account(long id, String name, String socialMediaType) {
}
