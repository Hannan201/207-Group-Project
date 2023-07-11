import data.Storage;
import data.security.Token;
import code.readers.Code;
import org.junit.jupiter.api.Test;
import models.Account;
import data.database.Database;
import models.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SaveAndLoadDataTests {
    @Test
    void saveDataWithExistingUser() {
        Database.setConnectionSource("./Tests/test.db");

        Token token = Database.registerUser("Test", "hellobro");
        assertNotNull(token);

        User user = Database.getUser(token);
        assertNotNull(user);

        int id = Database.addAccount(token, "One", "Discord");
        int codeID = Database.addCode(token, id, "yuyk-775x");
        codeID = Database.addCode(token, id, "5n6v-xgbo");
        codeID = Database.addCode(token, id, "aqtl-zszl");
        codeID = Database.addCode(token, id, "pf6m-lbsi");
        codeID = Database.addCode(token, id, "8mhm-kawb");
        codeID = Database.addCode(token, id, "9u15-hd56");
        codeID = Database.addCode(token, id, "5nqz-84l3");
        codeID = Database.addCode(token, id, "7bfa-icwn");
        codeID = Database.addCode(token, id, "jzug-ys5l");
        codeID = Database.addCode(token, id, "amvu-nius");
        codeID = Database.addCode(token, id, "s1ow-8ssl");
        codeID = Database.addCode(token, id, "mgv2-vqsj");
        codeID = Database.addCode(token, id, "koeo-hzeg");
        codeID = Database.addCode(token, id, "e7dh-gqlt");
        codeID = Database.addCode(token, id, "mk3o-7njl");
        codeID = Database.addCode(token, id, "osyv-2vjq");

        id = Database.addAccount(token, "Two", "GitHub");
        codeID = Database.addCode(token, id, "8a4rv-hi4qt");
        codeID = Database.addCode(token, id, "cv13u-1e4xp");
        codeID = Database.addCode(token, id, "zq44v-rderj");
        codeID = Database.addCode(token, id, "gg7xd-amtjq");
        codeID = Database.addCode(token, id, "jgz5t-h79hw");
        codeID = Database.addCode(token, id, "3bhws-wufnj");
        codeID = Database.addCode(token, id, "ik3uo-ctgaj");
        codeID = Database.addCode(token, id, "o8vnj-29b2j");
        codeID = Database.addCode(token, id, "mfovl-oym5b");
        codeID = Database.addCode(token, id, "isfxx-6v29n");
        codeID = Database.addCode(token, id, "scxxr-rjdg6");
        codeID = Database.addCode(token, id, "62mec-8kjw5");
        codeID = Database.addCode(token, id, "ddu4v-2xruj");
        codeID = Database.addCode(token, id, "8ceb8-h1bek");
        codeID = Database.addCode(token, id, "wjtrm-nkpwb");
        codeID = Database.addCode(token, id, "2k3uw-imada");

        id = Database.addAccount(token, "Three", "Google");
        codeID = Database.addCode(token, id, "7294 8105");
        codeID = Database.addCode(token, id, "4328 0505");
        codeID = Database.addCode(token, id, "8829 8563");
        codeID = Database.addCode(token, id, "5349 1328");
        codeID = Database.addCode(token, id, "5804 5408");
        codeID = Database.addCode(token, id, "3772 5051");
        codeID = Database.addCode(token, id, "0084 5571");
        codeID = Database.addCode(token, id, "9576 0658");
        codeID = Database.addCode(token, id, "4231 7248");
        codeID = Database.addCode(token, id, "8203 6638");
        codeID = Database.addCode(token, id, "9286 1762");
        codeID = Database.addCode(token, id, "6762 0652");
        codeID = Database.addCode(token, id, "0886 4325");
        codeID = Database.addCode(token, id, "0857 3901");
        codeID = Database.addCode(token, id, "0609 7737");
        codeID = Database.addCode(token, id, "1578 1639");

        Database.logUserOut(token);
        Database.disconnect();
    }

    @Test
    void loadDataWithExistingUser() {
        Database.setConnectionSource("./Tests/test.db");

        Token token = Database.authenticateUser("Test", "hellobro");
        assertNotNull(token);

        User user = Database.getUser(token);
        assertNotNull(user);
        assertEquals(user.getUsername(), "test");

        List<Account> accounts = Database.getAccounts(token);
        assertEquals(accounts.size(), 3);

        Account account = accounts.get(0);
        assertEquals(account.getName(), "One");
        assertEquals(account.getSocialMediaType(), "Discord");

        List<Code> codes = Database.getCodes(token, account.getID());
        assertEquals(codes.size(), 16);

        String[] expectedCodes = new String[]{"yuyk-775x",
                                              "5n6v-xgbo",
                                              "aqtl-zszl",
                                              "pf6m-lbsi",
                                              "8mhm-kawb",
                                              "9u15-hd56",
                                              "5nqz-84l3",
                                              "7bfa-icwn",
                                              "jzug-ys5l",
                                              "amvu-nius",
                                              "s1ow-8ssl",
                                              "mgv2-vqsj",
                                              "koeo-hzeg",
                                              "e7dh-gqlt",
                                              "mk3o-7njl",
                                              "osyv-2vjq"};

        String[] actualCodes = codes.stream()
                        .map(Code::getCode)
                        .toArray(String[]::new);
        assertArrayEquals(expectedCodes, actualCodes);

        account = accounts.get(1);
        assertEquals(account.getName(), "Two");
        assertEquals(account.getSocialMediaType(), "GitHub");

        codes = Database.getCodes(token, account.getID());
        assertEquals(codes.size(), 16);

        expectedCodes = new String[]{"8a4rv-hi4qt",
                                     "cv13u-1e4xp",
                                     "zq44v-rderj",
                                     "gg7xd-amtjq",
                                     "jgz5t-h79hw",
                                     "3bhws-wufnj",
                                     "ik3uo-ctgaj",
                                     "o8vnj-29b2j",
                                     "mfovl-oym5b",
                                     "isfxx-6v29n",
                                     "scxxr-rjdg6",
                                     "62mec-8kjw5",
                                     "ddu4v-2xruj",
                                     "8ceb8-h1bek",
                                     "wjtrm-nkpwb",
                                     "2k3uw-imada"};

        actualCodes = codes.stream()
                .map(Code::getCode)
                .toArray(String[]::new);
        assertArrayEquals(actualCodes, expectedCodes);

        account = accounts.get(2);
        assertEquals(account.getName(), "Three");
        assertEquals(account.getSocialMediaType(), "Google");

        codes = Database.getCodes(token, account.getID());
        assertEquals(codes.size(), 16);

        expectedCodes = new String[]{"7294 8105",
                                     "4328 0505",
                                     "8829 8563",
                                     "5349 1328",
                                     "5804 5408",
                                     "3772 5051",
                                     "0084 5571",
                                     "9576 0658",
                                     "4231 7248",
                                     "8203 6638",
                                     "9286 1762",
                                     "6762 0652",
                                     "0886 4325",
                                     "0857 3901",
                                     "0609 7737",
                                     "1578 1639"};

        actualCodes = codes.stream()
                .map(Code::getCode)
                .toArray(String[]::new);

        assertArrayEquals(expectedCodes, actualCodes);

        Database.logUserOut(token);
        Database.disconnect();
    }

    @Test
    void testWhenUserDeletesAccount() {
        Database.setConnectionSource("./Tests/test.db");

        Token token = Database.authenticateUser("Test", "hellobro");
        assertNotNull(token);

        User user = Database.getUser(token);
        assertNotNull(user);

        Account account = Database.getAccountByName(token, "One");
        assertNotNull(account);

        Database.removeAccount(token, account.getID());

        Database.logUserOut(token);
        Database.disconnect();

        Database.setConnectionSource("./Tests/test.db");

        token = Database.authenticateUser("Test", "hellobro");
        assertNotNull(token);

        user = Database.getUser(token);
        assertNotNull(user);

        account = Database.getAccount(token, account.getID());
        assertNull(account);

        Database.logUserOut(token);
        Database.disconnect();
    }

    @Test
    void testWhenDeleteAllCodes() {
        Database.setConnectionSource("./Tests/test.db");

        Token token = Database.authenticateUser("Test", "hellobro");
        assertNotNull(token);

        User user = Database.getUser(token);
        assertNotNull(user);

        Account account = Database.getAccountByName(token, "Two");
        assertNotNull(account);

        List<Code> codes = Database.getCodes(token, account.getID());
        assertEquals(codes.size(), 16);

        Database.clearAllCodes(token, account.getID());

        Database.logUserOut(token);
        Database.disconnect();

        Database.setConnectionSource("./Tests/test.db");

        token = Database.authenticateUser("Test", "hellobro");
        assertNotNull(token);

        user = Database.getUser(token);
        assertNotNull(user);

        codes = Database.getCodes(token, account.getID());
        assertEquals(codes.size(), 0);

        Database.logUserOut(token);
        Database.disconnect();
    }

    @Test
    void testWhenGetCode() {
        Database.setConnectionSource("./Tests/test.db");

        Token token = Database.authenticateUser("Test", "hellobro");
        assertNotNull(token);

        User user = Database.getUser(token);
        assertNotNull(user);

        Account account = Database.getAccountByName(token, "Two");
        assertNotNull(account);

        int id = Database.addCode(token, account.getID(), "4EW C0D3");
        Code c = Database.getCode(token, id);
        assertNotNull(c);
        assertEquals(c.getCode(), "4EW C0D3");

        id = Database.addCode(token, account.getID(), "123 456");
        c = Database.getCode(token, id);
        assertNotNull(c);
        assertEquals(c.getCode(), "123 456");

        Database.logUserOut(token);
        Database.disconnect();
    }

    @Test
    void testWhenDeleteCode() {
        Database.setConnectionSource("./Tests/test.db");

        Token token = Database.authenticateUser("Test", "hellobro");
        assertNotNull(token);

        User user = Database.getUser(token);
        assertNotNull(user);

        Account account = Database.getAccountByName(token, "Two");
        assertNotNull(account);

        List<Code> codes = Database.getCodes(token, account.getID());
        assertEquals(codes.size(), 2);

        int id = -1;
        for (Code c : codes) {
            if (c.getCode().equals("123 456")) {
                id = c.getID();
                break;
            }
        }

        Database.removeCode(token, id);

        codes = Database.getCodes(token, account.getID());
        assertEquals(codes.size(), 1);

        assertEquals(codes.get(0).getCode(), "4EW C0D3");

        Database.logUserOut(token);
        Database.disconnect();
    }

    @Test
    void testWhenUpdateCode() {
        Database.setConnectionSource("./Tests/test.db");

        Token token = Database.authenticateUser("Test", "hellobro");
        assertNotNull(token);

        User user = Database.getUser(token);
        assertNotNull(user);

        Account account = Database.getAccountByName(token, "Two");
        assertNotNull(account);

        List<Code> code = Database.getCodes(token, account.getID());
        assertEquals(code.size(), 1);
        Code c = code.get(0);
        int id = c.getID();

        Database.updateCode(token, c.getID(), "U9D47ED");

        c = Database.getCode(token, id);
        assertNotNull(c);
        assertEquals(c.getCode(), "U9D47ED");

        Database.logUserOut(token);
        Database.disconnect();
    }

    @Test
    void saveDataWhenUserMakesChanges() {
        Database.setConnectionSource("./Tests/test.db");

        Token token = Database.authenticateUser("Hannan", "12345");
        assertNotNull(token);

        User user = Database.getUser(token);
        assertNotNull(user);

        Account account = Database.getAccountByName(token, "Joe");
        assertNotNull(account);

        List<Code> codes = Database.getCodes(token, account.getID());
        int before = codes.size();

        for (int i = 0; i < 5; i++) {
            Database.addCode(token, account.getID(), "EEEEEEEEEEE");
        }

        Database.logUserOut(token);
        Database.disconnect();

        Database.setConnectionSource("./Tests/test.db");

        token = Database.authenticateUser("Hannan", "12345");
        assertNotNull(token);

        user = Database.getUser(token);
        assertNotNull(user);

        account = Database.getAccountByName(token, "Joe");
        assertNotNull(account);

        codes = Database.getCodes(token, account.getID());
        assertEquals(codes.size(), before + 5);

        Database.logUserOut(token);
        Database.disconnect();
    }

    @Test
    void saveUserDataWhenNotLoggedOut() {
        Database.setConnectionSource("./Tests/test.db");

        Token token = Database.authenticateUser("Hannan", "12345");
        assertNotNull(token);

        User user = Database.getUser(token);
        assertNotNull(user);

        Database.disconnect();
    }

    @Test
    void loadUserDataWhenNotLoggedOut() {
        Database.setConnectionSource("./Tests/test.db");

        Token token = Storage.getToken();
        assertNotNull(token);

        User user = Database.getUser(token);
        assertNotNull(user);

        Database.logUserOut(token);
        Database.disconnect();
    }

    @Test
    void loadCorrectTheme() {
        Database.setConnectionSource("./Tests/test.db");

        Token token = Database.authenticateUser("Hannan", "12345");
        assertNotNull(token);

        User user = Database.getUser(token);
        assertNotNull(user);

        assertEquals(Database.getTheme(token), "light mode");
        assertEquals(user.getTheme(), "light mode");

        Database.updateTheme(token, "high contrast mode");
        Database.logUserOut(token);
        Database.disconnect();

        Database.setConnectionSource("./Tests/test.db");

        token = Database.authenticateUser("Hannan", "12345");
        assertNotNull(token);

        user = Database.getUser(token);
        assertNotNull(user);

        assertEquals(Database.getTheme(token), "high contrast mode");
        assertEquals(user.getTheme(), "high contrast mode");

        Database.logUserOut(token);
        Database.disconnect();
    }

    @Test
    void testClearUserData() {
        Database.setConnectionSource("./Tests/test.db");

        Token token = Database.authenticateUser("Hannan", "12345");
        assertNotNull(token);

        User user = Database.getUser(token);
        assertNotNull(user);

        List<Account> accounts = Database.getAccounts(token);
        assertTrue(accounts.size() > 0);

        Database.clearAllAccounts(token);

        Database.logUserOut(token);
        Database.disconnect();

        Database.setConnectionSource("./Tests/test.db");

        token = Database.authenticateUser("Hannan", "12345");
        assertNotNull(token);

        user = Database.getUser(token);
        assertNotNull(user);

        accounts = Database.getAccounts(token);
        assertEquals(accounts.size(), 0);

        Database.logUserOut(token);
        Database.disconnect();

        try {
            Files.deleteIfExists(Path.of("./Tests/test.db"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
