package cypher.enforcers.data.spis;

import cypher.enforcers.models.UserEntity;

import java.util.Optional;

/*
 Interface for the User Repository. Behaves as a collection of
 accounts to help ease on making any changes.
 */
public interface UserRepository {

    /**
     * Create a new user. Usernames are case-insensitive.
     *
     * @param user The user to create.
     * @return An optional containing the user if created successfully,
     * null otherwise.
     */
    Optional<UserEntity> create(UserEntity user);

    /**
     * Read a user by its ID.
     *
     * @param id The ID of the user.
     * @return An Optional containing the user if found, null otherwise.
     */
    Optional<UserEntity> read(long id);

    /**
     * Read a user by its username. The search is case-insensitive.
     *
     * @param username The username to search for.
     * @return An Optional containing the user if found, null otherwise.
     */
    Optional<UserEntity> read(String username);

    /**
     * Find the current user that's logged in.
     *
     * @return An Optional containing the user if found, null otherwise.
     */
    Optional<UserEntity> findLoggedInUser();

    /**
     * Update a user.
     *
     * @return An Optional containing the user if updated, null otherwise.
     */
    Optional<UserEntity> update(UserEntity user);

}
