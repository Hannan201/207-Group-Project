package cypher.enforcers;

import cypher.enforcers.data.implementations.*;
import cypher.enforcers.data.spis.AccountDAO;
import cypher.enforcers.data.spis.AccountRepository;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.data.entities.AccountEntity;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AccountSavingAndLoadingTests {

    @Test
    public void accountCreation() {
        DatabaseService dbService = new SQLiteHelper();
        dbService.connect("/cypher/enforcers/account_create_r.db");

        AccountDAO accountDAO = new AccountDAOImpl(dbService);
        AccountRepository accountRepository = new AccountRepositoryImpl(accountDAO);

        List<AccountEntity> accounts = accountRepository.readAll(1);

        assertEquals(accounts.size(), 1, "Total accounts do not match.");

        assertEquals(accounts.get(0).getID(), 1, "ID should be 1.");
        assertEquals(accounts.get(0).getName(), "Joe", "Name should be Joe.");
        assertEquals(accounts.get(0).getSocialMediaType(), "1234", "Type should be 1234.");
        assertEquals(accounts.get(0).getUserId(), 1, "User ID should be 1.");

        AccountEntity account = new AccountEntity();
        account.setName("Apple Boy99");
        account.setSocialMediaType("Google");
        account.setUserId(1);

        Optional<AccountEntity> optionalAccount = accountRepository.create(account);
        assertTrue(optionalAccount.isPresent(), "Account cannot be created.");

        dbService.disconnect();
        dbService.connect("/cypher/enforcers/account_create_r.db");

        accounts = accountRepository.readAll(1);

        assertEquals(accounts.size(), 2, "Total accounts do not match.");

        assertEquals(accounts.get(0).getID(), 1, "ID should be 1.");
        assertEquals(accounts.get(0).getName(), "Joe", "Name should be Joe.");
        assertEquals(accounts.get(0).getSocialMediaType(), "1234", "Type should be 1234.");
        assertEquals(accounts.get(0).getUserId(), 1, "User ID should be 1.");

        assertEquals(accounts.get(1).getID(), 3, "ID should be 3.");
        assertEquals(accounts.get(1).getName(), "Apple Boy99", "Name should be Apple Boy99.");
        assertEquals(accounts.get(1).getSocialMediaType(), "Google", "Type should be Google.");
        assertEquals(accounts.get(1).getUserId(), 1, "User ID should be 1.");

        dbService.disconnect();
    }

    @Test
    public void accountDestruction() {
        DatabaseService dbService = new SQLiteHelper();
        dbService.connect("/cypher/enforcers/account_delete_r.db");

        AccountDAO accountDAO = new AccountDAOImpl(dbService);
        AccountRepository accountRepository = new AccountRepositoryImpl(accountDAO);

        List<AccountEntity> accounts = accountRepository.readAll(2);

        assertEquals(accounts.size(), 1, "Total accounts do not match.");

        assertEquals(accounts.get(0).getID(), 2, "ID should be 2.");
        assertEquals(accounts.get(0).getName(), "Joe", "Name should be Joe.");
        assertEquals(accounts.get(0).getSocialMediaType(), "GitHub", "Type should be GitHub.");
        assertEquals(accounts.get(0).getUserId(), 2, "User ID should be 2.");

        AccountEntity delete = accounts.get(0);

        Optional<AccountEntity> optionalAccount = accountRepository.delete(delete.getID());
        assertTrue(optionalAccount.isPresent(), "Unable to delete account.");

        dbService.disconnect();
        dbService.connect("/cypher/enforcers/account_delete_r.db");

        accounts = accountRepository.readAll(2);

        assertEquals(accounts.size(), 0, "Total accounts do not match.");

        dbService.disconnect();
    }

}
