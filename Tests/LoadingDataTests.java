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
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class LoadingDataTests {
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

    public static void main(String[] args) throws FileNotFoundException {
        restoreFiles();
        //System.out.println("\n".getBytes(StandardCharsets.UTF_8).length);
        RandomAccessFile raf = null;
        File file;
        FileReader in;
        BufferedReader readFile;
        String line;
        try {
            file = new File(pathToAccountsFile);
            in = new FileReader(file);
            readFile = new BufferedReader(in);
            raf = new RandomAccessFile(pathToUserFile, "r");

            while ((line = readFile.readLine()) != null) {
                System.out.println(line.getBytes(StandardCharsets.UTF_8).length);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void userLoginLoaded() {
        Database.setUserSource(pathToUserFile);
        Database.setAccountsSource(pathToAccountsFile);
        // assertTrue(Database.getLoginStatus());
        assertTrue(Database.checkUsername("haNNan"));
        assertTrue(Database.authenticateUser("Hannan", "12345"));
    }
}