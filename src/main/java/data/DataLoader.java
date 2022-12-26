package data;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/*
This class is responsible for loading data related to all users
in this application.
 */
public class DataLoader {

    // Source to load data from.
    private final String source;

    public DataLoader(String source) {
        this.source = source;
    }

    /**
     * Load the object in the source file.
     *
     * @return The object after being loaded.
     */
    public Object load() {
        FileInputStream in = null;
        ObjectInputStream readObject = null;
        try {
            in = new FileInputStream(this.source);
            readObject = new ObjectInputStream(in);
            return readObject.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (readObject != null) {
                    readObject.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
