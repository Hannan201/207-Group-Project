import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.Account;
import user.Database;
import user.User;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SaveAndLoadDataTests {

    static String pathToUserFile = "Tests/userData.txt";
    static String pathToAccountsFile = "Tests/accountData.txt";

    static String header = "none .\n" + "username,password,salt,theme,pos";

    private static void restoreFiles() throws FileNotFoundException {
        PrintWriter w = new PrintWriter(pathToAccountsFile);
        w.write("");
        w.close();

        w = new PrintWriter(pathToUserFile);
        w.close();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathToUserFile));
            writer.write(header);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    void saveDataWithExistingUser() {
        Database.setUserSource(pathToUserFile);
        Database.setAccountsSource(pathToAccountsFile);
        Database.registerUser("Test", "hellobro");
        User user = Database.getUser();
        assertNotNull(user);

        Account a1 = new Account("One", "Discord");
        for (int i = 0; i < 16; i++) {
            a1.addCodes(TempData.generateCodeAlphaNumeric(4, "-"));
        }

        Account a2 = new Account("Two", "GitHub");
        for (int i = 0; i < 16; i++) {
            a2.addCodes(TempData.generateCodeAlphaNumeric(5, "-"));
        }

        Account a3 = new Account("Three", "Google");
        for (int i = 0; i < 16; i++) {
            a3.addCodes(TempData.generateCodeNumbers(4, " "));
        }

        user.addNewAccount(a1);
        user.addNewAccount(a2);
        user.addNewAccount(a3);
        Database.logUserOut();
    }

    @Test
    void loadDataWithExistingUser() {
        Database.setUserSource(pathToUserFile);
        Database.setAccountsSource(pathToAccountsFile);
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
    }

}
