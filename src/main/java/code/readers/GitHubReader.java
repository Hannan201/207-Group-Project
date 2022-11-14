package code.readers;

import behaviors.interfaces.ReadCodeBehavior;

import java.util.List;

public class GitHubReader extends CodeReader implements ReadCodeBehavior {

    @Override
    public List<String> extractCodes(String fileName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> readCodes() {
        return extractCodes(this.getFilePath());
    }
}
