import ru.art.generator.compiler.annotation.*;
import ru.art.generator.compiler.processor.*;

@Model
public class Test {
    private String string;
    private Integer integer;

    public static void main(String[] args) throws Exception {
        com.sun.tools.javac.Main.main(new String[]{
                "-processor", MapperAnnotationProcessor.class.getName(),
                "-cp", "/Users/anton/Development/Projects/art/application-generator/build/classes/java/main",
                "/Users/anton/Development/Projects/art/application-test/src/main/java/Test.java"});
    }
}
