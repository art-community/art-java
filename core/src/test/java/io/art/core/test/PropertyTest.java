package io.art.core.test;

import io.art.core.property.*;
import org.junit.jupiter.api.*;
import static io.art.core.property.DisposableProperty.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.atomic.*;

public class PropertyTest {
    @Test
    public void testDisposable() {
        String test = "test";
        AtomicInteger counter = new AtomicInteger();
        DisposableProperty<String> property = disposable(() -> test)
                .initialized(value -> assertEquals(test, value)).initialized(ignore -> counter.incrementAndGet())
                .disposed(value -> assertEquals(test, value)).disposed(ignore -> counter.incrementAndGet())
                .initialize();
        property.dispose();
        property.initialize().dispose();
        assertEquals(4, counter.get());
    }
}
