import controllers.AccountViewController;
import models.Account;
import models.User;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class SearchAccountTests {

    private List<Account> createAccounts() {
        return new ArrayList<>(
                List.of(
                        new Account(1, "Joe", "Reddit"),
                        new Account(2, "Random", "Slack"),
                        new Account(3, "Oof", "Google"),
                        new Account(4, "Razor", "Origin")
                )
        );
    }

    /**
     * Test for when there's an exact match
     * of an account.
     */
    @Test
    void testWhenAccountExists() {
        List<Account> result = AccountViewController.searchAccounts(createAccounts(), "Joe");
        assertEquals(1, result.size());
        assertEquals("Joe", result.get(0).getName());
        assertEquals(
                "Reddit",
                result.get(0).getSocialMediaType()
        );
    }

    /**
     * Test for when searching an empty
     * account.
     */
    @Test
    void testWhenEmpty() {
        List<Account> result = AccountViewController.searchAccounts(
                new ArrayList<>(),
                ""
        );

        assertEquals(0, result.size());
    }


    /**
     * Test for when there are no accounts
     * present with a given name.
     */
    @Test
    void testWhenNoMatches() {
        List<Account> result = AccountViewController.searchAccounts(
                createAccounts(),
                "Gordan Ramsey"
        );
        assertEquals(0, result.size());
    }

    /**
     * Test for when there are multiple matches.
     */
    @Test
    void testWhenMultipleMatches() {
        List<Account> result = AccountViewController.searchAccounts(
                createAccounts(),
                "Ra"
        );
        assertEquals(2, result.size());

        // First result.
        assertEquals(
                "Random",
                result.get(0)
                        .getName()
        );
        assertEquals(
                "Slack",
                result.get(0)
                        .getSocialMediaType()
        );

        // Second result.
        assertEquals(
                "Razor",
                result.get(1)
                        .getName()
        );
        assertEquals(
                "Origin",
                result.get(1)
                        .getSocialMediaType()
        );
    }
 }
