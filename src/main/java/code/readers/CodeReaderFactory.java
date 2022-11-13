package code.readers;

public class CodeReaderFactory {

    public static CodeReader makeCodeReader(String readerType) {
        //Clean input
        readerType = readerType.strip();

        // Give correct reader based on the type.
        if (readerType.equalsIgnoreCase("github")) {
            return new GitHubReader();
        } else if (readerType.equalsIgnoreCase("google")) {
            return new GoogleReader();
        } else if (readerType.equalsIgnoreCase("discord")) {
            return new DiscordReader();
        } else if (readerType.equalsIgnoreCase("reddit")) {
            return new RedditReader();
        }

        // Not a valid type.
        return null;
    }

}
