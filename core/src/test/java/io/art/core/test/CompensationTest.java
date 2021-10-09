package io.art.core.test;

import org.junit.jupiter.api.*;
import reactor.core.publisher.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class CompensationTest {
    @Test
    public void testCompensationOnNext() {
        List<String> result = compensate(Flux.just("valid", "failed"), value -> value.equals("failed"), failed -> Flux.just("compensated"))
                .toStream()
                .collect(listCollector());
        assertEquals(2, result.size());
        assertEquals("valid", result.get(0));
        assertEquals("compensated", result.get(1));
    }

    @Test
    public void testCompensationOnError() {
        Flux<String> flux = Flux.create(emitter -> {
            emitter.next("valid");
            emitter.error(new RuntimeException());
            emitter.complete();
        });
        List<String> result = compensateOnError(flux, failed -> Flux.just("compensated"))
                .toStream()
                .collect(listCollector());
        assertEquals(2, result.size());
        assertEquals("valid", result.get(0));
        assertEquals("compensated", result.get(1));
    }
}
