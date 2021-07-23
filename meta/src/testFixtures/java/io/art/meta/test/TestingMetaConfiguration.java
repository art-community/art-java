package io.art.meta.test;

import io.art.core.collection.*;
import io.art.core.property.*;
import lombok.*;
import org.junit.jupiter.api.*;
import reactor.core.publisher.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.ArrayFactory.*;
import static java.util.Objects.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@Getter
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
@SuppressWarnings(OPTIONAL_USED_AS_FIELD)
public class TestingMetaConfiguration {
    int f1;
    short f2;
    double f3;
    float f4;
    long f5;
    boolean f6;
    char f7;
    byte f8;
    Integer f9;
    Short f10;
    Double f11;
    Float f12;
    Long f13;
    Boolean f14;
    Character f15;
    String f16;
    int[] f17;
    short[] f18;
    double[] f19;
    float[] f20;
    long[] f21;
    boolean[] f22;
    char[] f23;
    Integer[] f24;
    Short[] f25;
    Double[] f26;
    Float[] f27;
    Long[] f28;
    Boolean[] f29;
    Character[] f30;
    String[] f31;
    List<String> f32;
    Set<String> f33;
    Collection<String> f34;
    ImmutableArray<String> f35;
    ImmutableSet<String> f36;
    Supplier<String> f37;
    LazyProperty<String> f38;
    List<int[]> f39;
    List<String[]> f40;
    List<int[]>[] f41;
    List<String[]>[] f42;
    List<List<String>> f43;
    List<List<String>[]> f44;
    Map<String, String[]> f45;
    Map<String, List<String>> f46;
    Map<String, Map<String, String>[]> f47;
    ImmutableMap<String, Map<String, String>[]> f48;
    TestingMetaConfiguration f49;
    LocalDateTime f50;
    ZonedDateTime f51;
    Duration f52;
    ModelEnum f53;
    Optional<List<LazyProperty<String>>> f54;
    List<TestingMetaConfiguration> f55;
    Set<TestingMetaConfiguration> f56;
    Map<String, TestingMetaConfiguration> f57;
    Mono<String> f58;
    Flux<String> f59;
    Stream<String> f60;
    Mono<String[]> f61;
    String f62;
    String f63;

    public enum ModelEnum {
        FIRST,
        SECOND
    }


    public void assertEquals(TestingMetaConfiguration model) {
        Assertions.assertEquals(f1, model.f1, "f1");
        Assertions.assertEquals(f2, model.f2, "f2");
        Assertions.assertEquals(f3, model.f3, "f3");
        Assertions.assertEquals(f4, model.f4, "f4");
        Assertions.assertEquals(f5, model.f4, "f4");
        Assertions.assertEquals(f6, model.f6, "f6");
        Assertions.assertEquals(f7, model.f7, "f7");
        Assertions.assertEquals(f8, model.f8, "f8");
        Assertions.assertEquals(f9, model.f9, "f9");
        Assertions.assertEquals(f10, model.f10, "f10");
        Assertions.assertEquals(f11, model.f11, "f11");
        Assertions.assertEquals(f12, model.f12, "f12");
        Assertions.assertEquals(f13, model.f13, "f13");
        Assertions.assertEquals(f14, model.f14, "f14");
        Assertions.assertEquals(f15, model.f15, "f15");
        Assertions.assertEquals(f16, model.f16, "f16");

        assertArrayEquals(f17, model.f17, "f17");
        assertArrayEquals(f18, model.f18, "f18");
        assertArrayEquals(f19, model.f19, "f19");
        assertArrayEquals(f20, model.f20, "f20");
        assertArrayEquals(f21, model.f21, "f21");
        assertArrayEquals(f22, model.f22, "f22");
        assertArrayEquals(f23, model.f23, "f23");
        assertArrayEquals(f24, model.f24, "f24");
        assertArrayEquals(f25, model.f25, "f25");
        assertArrayEquals(f26, model.f26, "f26");
        assertArrayEquals(f27, model.f27, "f27");
        assertArrayEquals(f28, model.f28, "f28");
        assertArrayEquals(f29, model.f29, "f29");
        assertArrayEquals(f30, model.f30, "f30");
        assertArrayEquals(f31, model.f31, "f31");

        Assertions.assertEquals(f32, model.f32, "f32");
        Assertions.assertEquals(f33, model.f33, "f33");
        Assertions.assertEquals(f34, model.f34, "f34");
        Assertions.assertEquals(f35, model.f35, "f35");
        Assertions.assertEquals(f36, model.f36, "f36");
        Assertions.assertEquals(f37.get(), model.f37.get(), "f37");
        Assertions.assertEquals(f38, model.f38, "f38");

        Assertions.assertEquals(f39.size(), model.f39.size(), "f39");
        for (int index = 0; index < f39.size(); index++) {
            assertArrayEquals(f39.get(index), model.f39.get(index), "f39");
        }

        Assertions.assertEquals(f40.size(), model.f40.size(), "f40");
        for (int index = 0; index < f40.size(); index++) {
            assertArrayEquals(f40.get(index), model.f40.get(index), "f40");
        }

        Assertions.assertEquals(f41.length, model.f41.length, "f41");
        for (int index = 0; index < f41.length; index++) {
            List<int[]> elements = f41[index];
            List<int[]> modelElements = model.f41[index];
            Assertions.assertEquals(elements.size(), modelElements.size(), "f41");
            for (int inner = 0; inner < elements.size(); inner++) {
                assertArrayEquals(elements.get(inner), modelElements.get(inner), "f41");
            }
        }

        Assertions.assertEquals(f42.length, model.f42.length, "f42");
        for (int index = 0; index < f42.length; index++) {
            List<String[]> elements = f42[index];
            List<String[]> modelElements = model.f42[index];
            Assertions.assertEquals(elements.size(), modelElements.size(), "f42");
            for (int inner = 0; inner < elements.size(); inner++) {
                assertArrayEquals(elements.get(inner), modelElements.get(inner), "f42");
            }
        }

        Assertions.assertEquals(f43, model.f43, "f43");

        Assertions.assertEquals(f44.size(), model.f44.size(), "f44");
        for (int index = 0; index < f44.size(); index++) {
            List<String>[] elements = f44.get(index);
            List<String>[] modelElements = model.f44.get(index);
            assertArrayEquals(elements, modelElements, "f44");
        }

        Assertions.assertEquals(f45.size(), model.f45.size(), "f45");
        for (Map.Entry<String, String[]> entry : f45.entrySet()) {
            assertArrayEquals(entry.getValue(), model.f45.get(entry.getKey()), "f45");
        }

        Assertions.assertEquals(f46, model.f46, "f46");

        Assertions.assertEquals(f47.size(), model.f47.size(), "f47");
        for (Map.Entry<String, Map<String, String>[]> entry : f47.entrySet()) {
            assertArrayEquals(entry.getValue(), model.f47.get(entry.getKey()), "f47");
        }


        Assertions.assertEquals(f48.size(), model.f48.size(), "f48");
        for (Map.Entry<String, Map<String, String>[]> entry : f48.entrySet()) {
            assertArrayEquals(entry.getValue(), model.f48.get(entry.getKey()), "f48");
        }

        if (nonNull(model.f49)) {
            f49.assertEquals(model.f49);
        }

        Assertions.assertEquals(f50, model.f50, "f50");
        assertTrue(f51.isEqual(model.f51), "f51");
        Assertions.assertEquals(f52, model.f52, "f52");
        Assertions.assertEquals(f53, model.f53, "f53");
        Assertions.assertEquals(f54, model.f54, "f54");

        if (nonNull(model.f55)) {
            Assertions.assertEquals(f55.size(), model.f55.size(), "f55");
            for (int index = 0; index < f55.size(); index++) {
                f55.get(index).assertEquals(model.f55.get(index));
            }
        }

        if (nonNull(model.f56)) {
            Assertions.assertEquals(f56.size(), model.f56.size(), "f56");
            List<TestingMetaConfiguration> list = fixedArrayOf(f56);
            List<TestingMetaConfiguration> modelList = fixedArrayOf(model.f56);
            Assertions.assertEquals(list.size(), modelList.size(), "f56");
            for (int index = 0; index < list.size(); index++) {
                list.get(index).assertEquals(modelList.get(index));
            }
        }

        if (nonNull(model.f57)) {
            Assertions.assertEquals(f57.size(), model.f57.size(), "f57");
            for (Map.Entry<String, TestingMetaConfiguration> entry : f57.entrySet()) {
                entry.getValue().assertEquals(model.f57.get(entry.getKey()));
            }
        }

        Assertions.assertEquals(f58.block(), model.f58.block(), "f58");
        Assertions.assertEquals(f59.toStream().collect(listCollector()), model.f59.toStream().collect(listCollector()), "f59");

        if (nonNull(model.f60)) {
            assertNotNull(f60, "f60");
        }

        assertArrayEquals(f61.block(), model.f61.block(), "f61");

        assertNull(f62, "f62");

        Assertions.assertEquals(f63, EMPTY_STRING, "f63");
    }
}
