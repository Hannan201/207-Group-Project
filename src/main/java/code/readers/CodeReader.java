package code.readers;

import java.util.List;

public abstract class CodeReader {

    private String filePath;

    public abstract List<String> extractCodes(String fileName);

    public void setFilePath(String newPath) {
        this.filePath = newPath;
    }

    public String getFilePath() {
        return this.filePath;
    }
}
