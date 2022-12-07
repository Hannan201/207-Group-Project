import org.junit.jupiter.api.Test;
import user.Account;
import data.Database;
import user.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SaveAndLoadDataTests {

    String pathToUserFile = "Tests/users.ser";

    String pathToConfigFile = "Tests/configurations.ser";

    @Test
    void saveDataWithExistingUser() {
        Database.setConfigurationsSource(pathToConfigFile);
        Database.setUsersSource(pathToUserFile);
        Database.registerUser("Test", "hellobro");
        User user = Database.getUser();
        assertNotNull(user);

        Account a1 = new Account("One", "Discord");
        a1.addCodes("yuyk-775x");
        a1.addCodes("5n6v-xgbo");
        a1.addCodes("aqtl-zszl");
        a1.addCodes("pf6m-lbsi");
        a1.addCodes("8mhm-kawb");
        a1.addCodes("9u15-hd56");
        a1.addCodes("5nqz-84l3");
        a1.addCodes("7bfa-icwn");
        a1.addCodes("jzug-ys5l");
        a1.addCodes("amvu-nius");
        a1.addCodes("s1ow-8ssl");
        a1.addCodes("mgv2-vqsj");
        a1.addCodes("koeo-hzeg");
        a1.addCodes("e7dh-gqlt");
        a1.addCodes("mk3o-7njl");
        a1.addCodes("osyv-2vjq");

        Account a2 = new Account("Two", "GitHub");
        a2.addCodes("8a4rv-hi4qt");
        a2.addCodes("cv13u-1e4xp");
        a2.addCodes("zq44v-rderj");
        a2.addCodes("gg7xd-amtjq");
        a2.addCodes("jgz5t-h79hw");
        a2.addCodes("3bhws-wufnj");
        a2.addCodes("ik3uo-ctgaj");
        a2.addCodes("o8vnj-29b2j");
        a2.addCodes("mfovl-oym5b");
        a2.addCodes("isfxx-6v29n");
        a2.addCodes("scxxr-rjdg6");
        a2.addCodes("62mec-8kjw5");
        a2.addCodes("ddu4v-2xruj");
        a2.addCodes("8ceb8-h1bek");
        a2.addCodes("wjtrm-nkpwb");
        a2.addCodes("2k3uw-imada");

        Account a3 = new Account("Three", "Google");
        a3.addCodes("7294 8105");
        a3.addCodes("4328 0505");
        a3.addCodes("8829 8563");
        a3.addCodes("5349 1328");
        a3.addCodes("5804 5408");
        a3.addCodes("3772 5051");
        a3.addCodes("0084 5571");
        a3.addCodes("9576 0658");
        a3.addCodes("4231 7248");
        a3.addCodes("8203 6638");
        a3.addCodes("9286 1762");
        a3.addCodes("6762 0652");
        a3.addCodes("0886 4325");
        a3.addCodes("0857 3901");
        a3.addCodes("0609 7737");
        a3.addCodes("1578 1639");

        user.addNewAccount(a1);
        user.addNewAccount(a2);
        user.addNewAccount(a3);
        Database.logUserOut();
    }

    @Test
    void loadDataWithExistingUser() {
        Database.setConfigurationsSource(pathToConfigFile);
        Database.setUsersSource(pathToUserFile);
        Database.authenticateUser("Test", "hellobro");
        User user = Database.getUser();
        assertNotNull(user);
        assertEquals(user.getUsername(), "Test");
        List<Account> accounts = user.getAccounts();
        assertEquals(accounts.size(), 3);
        Account account = accounts.get(0);
        assertEquals(account.getName(), "One");
        assertEquals(account.getSocialMediaType(), "Discord");
        assertEquals(account.getUserCodes().size(), 16);

        String[] codes = new String[]{"yuyk-775x",
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

        String[] actualCodes = account.getUserCodes().toArray(new String[0]);
        assertArrayEquals(actualCodes, codes);

        account = accounts.get(1);
        assertEquals(account.getName(), "Two");
        assertEquals(account.getSocialMediaType(), "GitHub");
        assertEquals(account.getUserCodes().size(), 16);

        codes = new String[]{"8a4rv-hi4qt",
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

        actualCodes = account.getUserCodes().toArray(new String[0]);
        assertArrayEquals(actualCodes, codes);

        account = accounts.get(2);
        assertEquals(account.getName(), "Three");
        assertEquals(account.getSocialMediaType(), "Google");
        assertEquals(account.getUserCodes().size(), 16);

        codes = new String[]{"7294 8105",
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

        actualCodes = account.getUserCodes().toArray(new String[0]);
        assertArrayEquals(actualCodes, codes);
        Database.logUserOut();
    }

    @Test
    void saveDataWhenUserMakesChanges() {
        Database.setConfigurationsSource(pathToConfigFile);
        Database.setUsersSource(pathToUserFile);
        Database.authenticateUser("Hannan", "12345");
        User user = Database.getUser();
        assertNotNull(user);
        Account account = user.getAccountByName("Joe");
        int before = account.getUserCodes().size();

        for (int i = 0; i < 5; i++) {
            account.addCodes("EEEEEEEEEEE");
        }

        Database.logUserOut();

        Database.authenticateUser("Hannan", "12345");
        user = Database.getUser();
        assertNotNull(user);
        account = user.getAccountByName("Joe");
        assertEquals(account.getUserCodes().size(), before + 5);
        Database.logUserOut();
    }

    @Test
    void saveUserDataWhenNotLoggedOut() {
        Database.setConfigurationsSource(pathToConfigFile);
        Database.setUsersSource(pathToUserFile);
        Database.authenticateUser("Hannan", "12345");
        User user = Database.getUser();
        assertNotNull(user);
        Database.saveUserData();
    }

    @Test
    void loadUserDataWhenNotLoggedOut() {
        Database.setConfigurationsSource(pathToConfigFile);
        Database.setUsersSource(pathToUserFile);
        assertTrue(Database.getLoginStatus());
        User user = Database.getUser();
        assertNotNull(user);
        Database.logUserOut();
    }

    @Test
    void loadCorrectTheme() {
        Database.setConfigurationsSource(pathToConfigFile);
        Database.setUsersSource(pathToUserFile);
        Database.authenticateUser("Hannan", "12345");
        User user = Database.getUser();
        assertNotNull(user);
        assertEquals(Database.getCurrentTheme(), "Light");
        Database.setCurrentTheme("High Contrast");
        Database.logUserOut();

        Database.authenticateUser("Hannan", "12345");
        user = Database.getUser();
        assertNotNull(user);
        assertEquals(Database.getCurrentTheme(), "High Contrast");
        Database.logUserOut();
    }

    @Test
    void testClearUserData() {
        Database.setConfigurationsSource(pathToConfigFile);
        Database.setUsersSource(pathToUserFile);
        Database.authenticateUser("Hannan", "12345");
        User user = Database.getUser();
        assertNotNull(user);
        assertTrue(user.getAccounts().size() > 0);
        Database.clearUserData();
        Database.logUserOut();

        Database.authenticateUser("Hannan", "12345");
        user = Database.getUser();
        assertNotNull(user);
        assertEquals(user.getAccounts().size(), 0);
        Database.logUserOut();
    }
}
