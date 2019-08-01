import ru.art.example.module.ExampleModule;
import static ru.art.core.extension.ThreadExtensions.thread;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExampleTestInitializer {
    private static AtomicBoolean EXAMPLE_STARTED = new AtomicBoolean(false);

    static void startExample() {
        if (EXAMPLE_STARTED.get()) return;
        thread(ExampleModule::startExample);
        try {
            Thread.sleep(10000L);
            EXAMPLE_STARTED.set(true);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
