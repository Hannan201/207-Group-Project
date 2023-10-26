package cypher.enforcers;

import cypher.enforcers.controllers.AccountViewController;
import cypher.enforcers.models.Account;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

public class SearchAccountTests {

    private List<Account> createAccounts() {
        Account one = new Account();
        one.setId(1);
        one.setName("Joe");
        one.setSocialMediaType("Reddit");

        Account two = new Account();
        two.setId(2);
        two.setName("Random");
        two.setSocialMediaType("Slack");

        Account three = new Account();
        three.setId(3);
        three.setName("Oof");
        three.setSocialMediaType("Google");

        Account four = new Account();
        four.setId(4);
        four.setName("Razor");
        four.setSocialMediaType("Origin");
        return List.of(one, two, three, four);
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
