package cypher.enforcers;

import cypher.enforcers.data.entities.AccountEntity;
import cypher.enforcers.data.security.dtos.Account;
import cypher.enforcers.data.security.mappers.AccountDTOMapper;
import cypher.enforcers.data.spis.AccountRepository;
import cypher.enforcers.models.AccountModel;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class SearchAccountTests {

    private final AccountDTOMapper mapper = new AccountDTOMapper();

    private final AccountRepository mockRepo = new MockRepository();

    private List<Account> createAccounts() {
        AccountEntity one = new AccountEntity();
        one.setId(1);
        one.setName("Joe");
        one.setSocialMediaType("Reddit");
        one.setUserId(1);

        AccountEntity two = new AccountEntity();
        two.setId(2);
        two.setName("Random");
        two.setSocialMediaType("Slack");
        two.setUserId(1);

        AccountEntity three = new AccountEntity();
        three.setId(3);
        three.setName("Oof");
        three.setSocialMediaType("Google");
        three.setUserId(1);

        AccountEntity four = new AccountEntity();
        four.setId(4);
        four.setName("Razor");
        four.setSocialMediaType("Origin");
        four.setUserId(1);
        return Stream.of(one, two, three, four).map(mapper)
                .toList();
    }

    /**
     * Test for when there's an exact match
     * of an account.
     */
    @Test
    void testWhenAccountExists() {
        AccountModel model = new AccountModel(mockRepo, mapper);
        model.setAccounts(FXCollections.observableList(createAccounts()));

        List<Account> result = model.searchAccounts("Joe");
        assertEquals(1, result.size());
        assertEquals("Joe", result.get(0).name());
        assertEquals(
                "Reddit",
                result.get(0).socialMediaType()
        );
    }

    /**
     * Test for when searching an empty
     * account.
     */
    @Test
    void testWhenEmpty() {
        AccountModel model = new AccountModel(mockRepo, mapper);
        model.setAccounts(FXCollections.emptyObservableList());

        List<Account> result = model.searchAccounts("");

        assertEquals(0, result.size());
    }


    /**
     * Test for when there are no accounts
     * present with a given name.
     */
    @Test
    void testWhenNoMatches() {
        AccountModel model = new AccountModel(mockRepo, mapper);
        model.setAccounts(FXCollections.observableList(createAccounts()));
        List<Account> result = model.searchAccounts("Gordan Ramsey");
        assertEquals(0, result.size());
    }

    /**
     * Test for when there are multiple matches.
     */
    @Test
    void testWhenMultipleMatches() {
        AccountModel model = new AccountModel(mockRepo, mapper);
        model.setAccounts(FXCollections.observableList(createAccounts()));

        List<Account> result = model.searchAccounts("Ra");
        assertEquals(2, result.size());

        // First result.
        assertEquals("Random", result.get(0).name());
        assertEquals("Slack", result.get(0).socialMediaType());

        // Second result.
        assertEquals("Razor", result.get(1).name());
        assertEquals("Origin", result.get(1).socialMediaType());
    }
 }

/**
 * This class acts as a fake account repository for testing.
 */
 class MockRepository implements AccountRepository {

     @Override
     public Optional<AccountEntity> create(AccountEntity account) {
         return Optional.empty();
     }

     @Override
     public List<AccountEntity> readAll(long id) {
         return null;
     }

     @Override
     public Optional<AccountEntity> read(long accountID) {
         return Optional.empty();
     }

     @Override
     public Optional<AccountEntity> delete(long id) {
         return Optional.empty();
     }

     @Override
     public List<AccountEntity> deleteAll(long id) {
         return null;
     }
 }