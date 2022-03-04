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
public class TestingMetaModel {
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
    byte[] f24;
    Integer[] f25;
    Short[] f26;
    Double[] f27;
    Float[] f28;
    Long[] f29;
    Boolean[] f30;
    Character[] f31;
    String[] f32;
    List<String> f33;
    Set<String> f34;
    Collection<String> f35;
    ImmutableArray<String> f36;
    ImmutableSet<String> f37;
    Supplier<String> f38;
    LazyProperty<String> f39;
    List<int[]> f40;
    List<byte[]> f41;
    List<String[]> f42;
    List<byte[]>[] f43;
    List<int[]>[] f44;
    List<String[]>[] f45;
    List<List<String>> f46;
    List<List<String>[]> f47;
    Map<Integer, String> f48;
    Map<String, String[]> f49;
    Map<String, List<String>> f50;
    Map<String, Map<String, String>[]> f51;
    ImmutableMap<String, Map<String, String>[]> f52;
    TestingMetaModel f53;
    LocalDateTime f54;
    ZonedDateTime f55;
    Duration f56;
    ModelEnum f57;
    Optional<List<LazyProperty<String>>> f58;
    List<TestingMetaModel> f59;
    Set<TestingMetaModel> f60;
    Map<String, TestingMetaModel> f61;
    Mono<String> f62;
    Flux<String> f63;
    Stream<String> f64;
    Mono<String[]> f65;
    String f66;
    String f67;


    public enum ModelEnum {
        FIRST,
        SECOND
    }


    public void assertEquals(TestingMetaModel model) {
        Assertions.assertEquals(f1, model.f1, "f1");
        Assertions.assertEquals(f2, model.f2, "f2");
        Assertions.assertEquals(f3, model.f3, "f3");
        Assertions.assertEquals(f4, model.f4, "f4");
        Assertions.assertEquals(f5, model.f5, "f5");
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
        assertArrayEquals(f32, model.f32, "f32");

        Assertions.assertEquals(f33, model.f33, "f33");
        Assertions.assertEquals(f34, model.f34, "f34");
        Assertions.assertEquals(f35, model.f35, "f35");
        Assertions.assertEquals(f36, model.f36, "f36");
        Assertions.assertEquals(f37, model.f37, "f37");
        Assertions.assertEquals(f38.get(), model.f38.get(), "f38");
        Assertions.assertEquals(f39, model.f39, "f39");

        Assertions.assertEquals(f40.size(), model.f40.size(), "f40");
        for (int index = 0; index < f40.size(); index++) {
            assertArrayEquals(f40.get(index), model.f40.get(index), "f40");
        }

        Assertions.assertEquals(f41.size(), model.f41.size(), "f41");
        for (int index = 0; index < f41.size(); index++) {
            assertArrayEquals(f41.get(index), model.f41.get(index), "f41");
        }

        Assertions.assertEquals(f42.size(), model.f42.size(), "f42");
        for (int index = 0; index < f42.size(); index++) {
            assertArrayEquals(f42.get(index), model.f42.get(index), "f42");
        }

        Assertions.assertEquals(f43.length, model.f43.length, "f43");
        for (int index = 0; index < f43.length; index++) {
            List<byte[]> elements = f43[index];
            List<byte[]> modelElements = model.f43[index];
            Assertions.assertEquals(elements.size(), modelElements.size(), "f43");
            for (int inner = 0; inner < elements.size(); inner++) {
                assertArrayEquals(elements.get(inner), modelElements.get(inner), "f43");
            }
        }

        Assertions.assertEquals(f44.length, model.f44.length, "f44");
        for (int index = 0; index < f44.length; index++) {
            List<int[]> elements = f44[index];
            List<int[]> modelElements = model.f44[index];
            Assertions.assertEquals(elements.size(), modelElements.size(), "f44");
            for (int inner = 0; inner < elements.size(); inner++) {
                assertArrayEquals(elements.get(inner), modelElements.get(inner), "f44");
            }
        }

        Assertions.assertEquals(f45.length, model.f45.length, "f45");
        for (int index = 0; index < f45.length; index++) {
            List<String[]> elements = f45[index];
            List<String[]> modelElements = model.f45[index];
            Assertions.assertEquals(elements.size(), modelElements.size(), "f45");
            for (int inner = 0; inner < elements.size(); inner++) {
                assertArrayEquals(elements.get(inner), modelElements.get(inner), "f45");
            }
        }

        Assertions.assertEquals(f46, model.f46, "f46");

        Assertions.assertEquals(f47.size(), model.f47.size(), "f47");
        for (int index = 0; index < f47.size(); index++) {
            List<String>[] elements = f47.get(index);
            List<String>[] modelElements = model.f47.get(index);
            assertArrayEquals(elements, modelElements, "f47");
        }

        Assertions.assertEquals(f48, model.f48, "f48");

        Assertions.assertEquals(f49.size(), model.f49.size(), "f49");
        for (Map.Entry<String, String[]> entry : f49.entrySet()) {
            assertArrayEquals(entry.getValue(), model.f49.get(entry.getKey()), "f49");
        }

        Assertions.assertEquals(f50, model.f50, "f50");

        Assertions.assertEquals(f51.size(), model.f51.size(), "f51");
        for (Map.Entry<String, Map<String, String>[]> entry : f51.entrySet()) {
            assertArrayEquals(entry.getValue(), model.f51.get(entry.getKey()), "f51");
        }


        Assertions.assertEquals(f52.size(), model.f52.size(), "f52");
        for (Map.Entry<String, Map<String, String>[]> entry : f52.entrySet()) {
            assertArrayEquals(entry.getValue(), model.f52.get(entry.getKey()), "f52");
        }

        if (nonNull(model.f53)) {
            f53.assertEquals(model.f53);
        }

        Assertions.assertEquals(f54, model.f54, "f54");
        assertTrue(f55.isEqual(model.f55), "f55");
        Assertions.assertEquals(f56, model.f56, "f56");
        Assertions.assertEquals(f57, model.f57, "f57");
        Assertions.assertEquals(f58, model.f58, "f58");

        if (nonNull(model.f59)) {
            Assertions.assertEquals(f59.size(), model.f59.size(), "f59");
            for (int index = 0; index < f59.size(); index++) {
                f59.get(index).assertEquals(model.f59.get(index));
            }
        }

        if (nonNull(model.f60)) {
            Assertions.assertEquals(f60.size(), model.f59.size(), "f60");
            List<TestingMetaModel> list = fixedArrayOf(f60);
            List<TestingMetaModel> modelList = fixedArrayOf(model.f60);
            Assertions.assertEquals(list.size(), modelList.size(), "f60");
            for (int index = 0; index < list.size(); index++) {
                list.get(index).assertEquals(modelList.get(index));
            }
        }

        if (nonNull(model.f61)) {
            Assertions.assertEquals(f61.size(), model.f61.size(), "f61");
            for (Map.Entry<String, TestingMetaModel> entry : f61.entrySet()) {
                entry.getValue().assertEquals(model.f61.get(entry.getKey()));
            }
        }

        Assertions.assertEquals(f62.block(), model.f62.block(), "f62");
        Assertions.assertEquals(f63.toStream().collect(listCollector()), model.f63.toStream().collect(listCollector()), "f63");

        if (nonNull(model.f64)) {
            assertNotNull(f64, "f64");
        }

        assertArrayEquals(f65.block(), model.f65.block(), "f65");

        assertNull(f66, "f66");

        Assertions.assertEquals(f67, EMPTY_STRING, "f67");
    }
}
