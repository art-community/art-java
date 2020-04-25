import ru.art.generator.compiler.annotation.*;

@Mapper
public class Test {
    private String string;
    private Integer integer;

    public static void main(String[] args) throws Exception {
        com.sun.tools.javac.Main.main(new String[]{
                "-processor", "lombok.core.AnnotationProcessor",
                "/Users/anton/Development/Projects/art/application-test/src/main/java/Test.java"});
    }
}
