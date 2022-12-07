package data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/*
This class is responsible for saving data related to users in this
application.
 */
public class DataSaver {

    // The file to save the data.
    private String source;

    // The object to save.
    private Object object;

    public DataSaver(String source, Object o) {
        this.source = source;
        this.object = o;
    }

    /**
     * Save the object to the source file.
     */
    public void save() {
        FileOutputStream out = null;
        ObjectOutputStream writeObject = null;
        try {
            out = new FileOutputStream(this.source);
            writeObject = new ObjectOutputStream(out);
            writeObject.writeObject(this.object);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writeObject != null) {
                    writeObject.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
