package io.art.meta.test;

import lombok.experimental.*;
import reactor.core.publisher.*;
import static io.art.core.constants.DateTimeConstants.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.core.property.LazyProperty.*;
import static java.util.Collections.*;
import java.time.*;
import java.util.*;

@UtilityClass
public class TestingMetaConfigurationGenerator {
    public static TestingMetaConfiguration generateTestingConfiguration() {
        TestingMetaConfiguration model = TestingMetaConfiguration.builder()
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
                .f24(new Integer[]{1})
                .f25(new Short[]{1})
                .f26(new Double[]{1.})
                .f27(new Float[]{1f})
                .f28(new Long[]{1L})
                .f29(new Boolean[]{true})
                .f30(new Character[]{'c'})
                .f31(new String[]{"test"})
                .f32(dynamicArrayOf("test", "test"))
                .f33(setOf("test"))
                .f34(dynamicArrayOf("test"))
                .f35(immutableArrayOf("test"))
                .f36(immutableSetOf("test"))
                .f37(() -> "test")
                .f38(lazy(() -> "test"))
                .f39(singletonList(new int[]{1}))
                .f40(singletonList(new String[]{"test"}))
                .f41(new List[]{singletonList(new int[]{1})})
                .f42(new List[]{singletonList(new String[]{"test"})})
                .f43(singletonList(singletonList("test")))
                .f44(singletonList(new List[]{singletonList("test")}))
                .f45(mapOf("test", new String[]{"test"}))
                .f46(mapOf("test", dynamicArrayOf("test")))
                .f47(mapOf("test", new Map[]{mapOf("test", "test")}))
                .f48(immutableMapOf(mapOf("test", new Map[]{mapOf("test", "test")})))
                .f50(LocalDateTime.parse("2021-07-23T21:01:21.2043414+03:00", TRANSPORTABLE_FORMATTER))
                .f51(ZonedDateTime.parse("2021-07-23T21:01:21.2043414+03:00", TRANSPORTABLE_FORMATTER))
                .f52(Duration.ofDays(1))
                .f53(TestingMetaConfiguration.ModelEnum.FIRST)
                .f54(Optional.of(dynamicArrayOf(lazy(() -> "test"), lazy(() -> "test2"))))
                .f58(Mono.just("test"))
                .f59(Flux.just("test"))
                .f61(Mono.just(new String[]{"test"}))
                .f62(null)
                .f63("")
                .build();

        return model.toBuilder()
                .f49(model)
                .f55(dynamicArrayOf(model))
                .f56(setOf(model))
                .f57(mapOf("test", model))
                .f60(streamOf("test"))
                .build();
    }
}
