package cypher.enforcers;

import cypher.enforcers.controllers.AccountViewController;
import cypher.enforcers.data.entities.AccountEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

public class SearchAccountTests {

    private List<AccountEntity> createAccounts() {
        AccountEntity one = new AccountEntity();
        one.setId(1);
        one.setName("Joe");
        one.setSocialMediaType("Reddit");

        AccountEntity two = new AccountEntity();
        two.setId(2);
        two.setName("Random");
        two.setSocialMediaType("Slack");

        AccountEntity three = new AccountEntity();
        three.setId(3);
        three.setName("Oof");
        three.setSocialMediaType("Google");

        AccountEntity four = new AccountEntity();
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
        List<AccountEntity> result = AccountViewController.searchAccounts(createAccounts(), "Joe");
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
        List<AccountEntity> result = AccountViewController.searchAccounts(
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
        List<AccountEntity> result = AccountViewController.searchAccounts(
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
        List<AccountEntity> result = AccountViewController.searchAccounts(
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
