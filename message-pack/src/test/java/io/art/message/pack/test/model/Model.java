package io.art.message.pack.test.model;

import io.art.core.collection.*;
import io.art.core.property.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import java.time.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@SuppressWarnings(OPTIONAL_USED_AS_FIELD)
public class Model {
    Mono<String> mono1;
    Flux<String> flux;
    Stream<String> stream;
    Mono<String[]> mono2;
    String nullField;
    String emptyString;

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
    Model f53;
    LocalDateTime f54;
    ZonedDateTime f55;
    Duration f56;
    ModelEnum f57;
    Optional<List<LazyProperty<String>>> f58;
    List<Model> f59;
    Set<Model> f60;
    Map<String, Model> f61;

    public enum ModelEnum {
        FIRST,
        SECOND
    }
}
