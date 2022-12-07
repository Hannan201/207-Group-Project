import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.Account;
import user.Database;
import user.User;

import javax.xml.crypto.Data;
import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class SavingDataTests {
    String pathToUserFile = "Tests/userData.txt";
    String pathToAccountsFile = "Tests/accountData.txt";

    String header = "none .\n" + "username,password,salt,theme,pos";

    private void restoreFiles() throws FileNotFoundException {
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
    void testSaveUserData() throws FileNotFoundException {
        Database.setUserSource(pathToUserFile);
        Database.setAccountsSource(pathToAccountsFile);
        Database.registerUser("Joe", "1234");
        User newUser = Database.getUser();
        assertNotNull(newUser);
        Account a1 = new Account("Joe", "GitHub");
        a1.addCodes("1234");
        a1.addCodes("1234");
        a1.addCodes("1234");
        a1.addCodes("1234");
        newUser.addNewAccount(a1);
        Database.logUserOut();

        try {
            FileReader reader = new FileReader(pathToUserFile);
            BufferedReader readFile = new BufferedReader(reader);

            assertEquals(readFile.readLine().strip(), "Joe");
            assertEquals(readFile.readLine().strip(), "1");
            assertEquals(readFile.readLine().strip(), "Joe");
            assertEquals(readFile.readLine().strip(), "GitHub");
            assertEquals(readFile.readLine().strip(), "4");
            assertEquals(readFile.readLine().strip(), "1234");
            assertEquals(readFile.readLine().strip(), "1234");
            assertEquals(readFile.readLine().strip(), "1234");
            assertEquals(readFile.readLine().strip(), "1234");
        } catch (Exception e) {
            e.printStackTrace();
        }

        restoreFiles();
    }

    public static void main(String[] args) throws IOException {
        new SavingDataTests().restoreFiles();
    }

    @Test
    void testSaveUserLogins() {
        Database.setUserSource("Tests/userData.txt");
        Database.setAccountsSource("Tests/accountData.txt");
        Database.registerUser("Hannan", "12345");
        User user = Database.getUser();
        Account account = new Account("Joe", "1234");
        account.addCodes("1234");
        account.addCodes("1234");
        account.addCodes("1234");
        user.addNewAccount(account);
        Database.logUserOut();
    }
}
