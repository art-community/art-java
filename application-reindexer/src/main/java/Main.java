public class Main {
    public native void initializeReindexer();

    static {
        System.load("C:\\Development\\Projects\\ART\\application-reindexer\\build\\classes\\java\\main\\reindexer.x64.dll");
    }

    public static void main(String[] args) {
        new Main().initializeReindexer();
    }
}
