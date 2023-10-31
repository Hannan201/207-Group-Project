package cypher.enforcers;

import cypher.enforcers.data.implementations.*;
import cypher.enforcers.data.spis.AccountDAO;
import cypher.enforcers.data.spis.AccountRepository;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.data.entities.AccountEntity;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class AccountLoadingTests {

    @Test
    public void loadAllAccounts() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/account_read.db");

        AccountDAO accountDAO = new AccountDAOImpl(dbService);
        AccountRepository accountRepository = new AccountRepositoryImpl(accountDAO);

        List<AccountEntity> accounts = accountRepository.readAll(1);

        assertEquals(accounts.size(), 3, "Accounts do not match.");

        assertEquals(accounts.get(0).getID(), 1, "First account's ID does not match.");
        assertEquals(accounts.get(0).getName(), "Joe", "First account's name does not match.");
        assertEquals(accounts.get(0).getSocialMediaType(), "1234", "First account's social media socialMediaType does not match.");
        assertEquals(accounts.get(0).getUserId(), 1, "First account's user ID does not match.");

        assertEquals(accounts.get(1).getID(), 3, "Second account's ID does not match.");
        assertEquals(accounts.get(1).getName(), "Razor", "Second account's name does not match.");
        assertEquals(accounts.get(1).getSocialMediaType(), "Origin", "Second account's social media socialMediaType does not match.");
        assertEquals(accounts.get(1).getUserId(), 1, "Second account's user ID does not match.");

        assertEquals(accounts.get(2).getID(), 4, "Third account's ID does not match.");
        assertEquals(accounts.get(2).getName(), "ACOne", "Third account's name does not match.");
        assertEquals(accounts.get(2).getSocialMediaType(), "Discord", "Third account's social media socialMediaType does not match.");
        assertEquals(accounts.get(2).getUserId(), 1, "Third account's user ID does not match.");

        dbService.disconnect();
    }

    @Test
    public void loadingWithID() {
        DatabaseService dbService = new SqliteHelper();
        dbService.connect("/cypher/enforcers/account_read.db");

        AccountDAO accountDAO = new AccountDAOImpl(dbService);
        AccountRepository accountRepository = new AccountRepositoryImpl(accountDAO);

        Optional<AccountEntity> accountOptional = accountRepository.read(2);
        assertTrue(accountOptional.isPresent(), "First account is empty.");
        AccountEntity account = accountOptional.get();

        assertEquals(account.getID(), 2, "First account's ID does not match.");
        assertEquals(account.getName(), "Joe", "First account's name does not match.");
        assertEquals(account.getSocialMediaType(), "GitHub", "First account's social media socialMediaType does not match.");
        assertEquals(account.getUserId(), 2, "First account's user ID does not match.");

        accountOptional = accountRepository.read(5);
        assertTrue(accountOptional.isPresent(), "Second account is empty.");
        account = accountOptional.get();

        assertEquals(account.getID(), 5, "Second account's ID does not match.");
        assertEquals(account.getName(), "ACTwo", "Second account's name does not match.");
        assertEquals(account.getSocialMediaType(), "Slack", "Second account's social media socialMediaType does not match.");
        assertEquals(account.getUserId(), 2, "Second account's user ID does not match.");

        dbService.disconnect();
    }
}
