package io.art.json.test.model;

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
@Builder(toBuilder = true)
@SuppressWarnings(OPTIONAL_USED_AS_FIELD)
public class Model {
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
    Mono<String> f40;
    Flux<String> f41;
    Stream<String> f42;
    List<int[]> f43;
    List<byte[]> f44;
    List<String[]> f45;
    Mono<String[]> f46;
    List<byte[]>[] f47;
    List<int[]>[] f48;
    List<String[]>[] f49;
    List<List<String>> f50;
    List<List<String>[]> f51;
    Map<Integer, String> f52;
    Map<String, String[]> f53;
    Map<String, List<String>> f55;
    Map<String, Map<String, String>[]> f56;
    ImmutableMap<String, Map<String, String>[]> f57;
    Model f58;
    LocalDateTime f59;
    ZonedDateTime f60;
    Duration f61;
    E f62;
    Optional<List<LazyProperty<String>>> f63;
    String f64ForNull;
    String f65ForEmpty;
    List<Model> f66;
    Set<Model> f67;
    Map<String, Model> f68;

    public enum E {
        FIRST,
        SECOND
    }
}
