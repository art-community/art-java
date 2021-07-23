package io.art.meta.test;

import lombok.experimental.*;
import reactor.core.publisher.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.core.constants.DateTimeConstants.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.test.TestingMetaConfigurationModel.*;
import static java.util.Collections.*;
import java.time.*;
import java.util.*;

@UtilityClass
@SuppressWarnings(UNCHECKED)
public class TestingMetaConfigurationGenerator {
    public static TestingMetaConfiguration generateModel() {
        TestingMetaConfigurationModel model = builder()
                .f1(1)
                .f2((short) 1)
                .f3(1)
                .f4(1)
                .f5(1)
                .f6(true)
                .f7('1')
                .f8((byte) 1)
                .f9(1)
                .f10((short) 1)
                .f11(1.0)
                .f12(1F)
                .f13(1L)
                .f14(false)
                .f15('1')
                .f16("test")
                .f17(new int[]{1})
                .f18(new short[]{1})
                .f19(new double[]{1})
                .f20(new float[]{1})
                .f21(new long[]{1})
                .f22(new boolean[]{true})
                .f23(new char[]{'c'})
                .f25(new Integer[]{1})
                .f26(new Short[]{1})
                .f27(new Double[]{1.})
                .f28(new Float[]{1f})
                .f29(new Long[]{1L})
                .f30(new Boolean[]{true})
                .f31(new Character[]{'c'})
                .f32(new String[]{"test"})
                .f33(dynamicArrayOf("test", "test"))
                .f34(setOf("test"))
                .f35(dynamicArrayOf("test"))
                .f36(immutableArrayOf("test"))
                .f37(immutableSetOf("test"))
                .f38(() -> "test")
                .f39(lazy(() -> "test"))
                .f40(singletonList(new int[]{1}))
                .f42(singletonList(new String[]{"test"}))
                .f44(new List[]{singletonList(new int[]{1})})
                .f45(new List[]{singletonList(new String[]{"test"})})
                .f46(singletonList(singletonList("test")))
                .f47(singletonList(new List[]{singletonList("test")}))
                .f49(mapOf("test", new String[]{"test"}))
                .f50(mapOf("test", dynamicArrayOf("test")))
                .f51(mapOf("test", new Map[]{mapOf("test", "test")}))
                .f52(immutableMapOf(mapOf("test", new Map[]{mapOf("test", "test")})))
                .f54(LocalDateTime.parse("2021-07-23T21:01:21.2043414+03:00", TRANSPORTABLE_FORMATTER))
                .f55(ZonedDateTime.parse("2021-07-23T21:01:21.2043414+03:00", TRANSPORTABLE_FORMATTER))
                .f56(Duration.ofDays(1))
                .f57(ModelEnum.FIRST)
                .f58(Optional.of(dynamicArrayOf(lazy(() -> "test"), lazy(() -> "test2"))))
                .f62(Mono.just("test"))
                .f63(Flux.just("test"))
                .f65(Mono.just(new String[]{"test"}))
                .f66(null)
                .f67("")
                .build();

        return model.toBuilder()
                .f53(model)
                .f59(dynamicArrayOf(model))
                .f60(setOf(model))
                .f61(mapOf("test", model))
                .f64(streamOf("test"))
                .build();
    }
}
