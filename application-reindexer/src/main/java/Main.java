import java.io.*;
import java.nio.file.*;

public class Main {
   public static native ReindexerError initializeReindexer(String directory);

    static {
        System.load("C:\\Development\\Projects\\ART\\application-reindexer\\build\\classes\\java\\main\\reindexer.x64.dll");
    }

    public static void main(String[] args) throws IOException {
        Path dbPath = Paths.get("db");
        Files.createDirectories(dbPath);
        System.out.println(initializeReindexer(dbPath.toAbsolutePath().toString()));
    }
}
