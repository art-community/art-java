package io.art.json.test.generator;

import io.art.json.test.model.*;
import lombok.experimental.*;
import reactor.core.publisher.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.core.property.LazyProperty.*;
import static java.util.Collections.*;
import java.time.*;
import java.util.*;

@UtilityClass
public class JsonTestModelGenerator {
    public static Model generateModel() {
        Model model = Model.builder()
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
                .f24(new byte[]{1})
                .f25(new Integer[]{1})
                .f26(new Short[]{1})
                .f27(new Double[]{1.})
                .f28(new Float[]{1f})
                .f29(new Long[]{1L})
                .f30(new Boolean[]{true})
                .f31(new Character[]{'c'})
                .f32(new String[]{"test"})
                .f33(fixedArrayOf("test", null, "test"))
                .f34(setOf("test"))
                .f35(fixedArrayOf("test"))
                .f36(immutableArrayOf("test"))
                .f37(immutableSetOf("test"))
                .f38(() -> "test")
                .f39(lazy(() -> "test"))
                .mono1(Mono.just("test"))
                .flux(Flux.just("test"))
                .f43(singletonList(new int[]{1}))
                .f44(singletonList(new byte[]{1}))
                .f45(singletonList(new String[]{"test"}))
                .mono2(Mono.just(new String[]{"test"}))
                .f47(new List[]{singletonList(new byte[]{1})})
                .f48(new List[]{singletonList(new int[]{1})})
                .f49(new List[]{singletonList(new String[]{"test"})})
                .f50(singletonList(singletonList("test")))
                .f51(singletonList(new List[]{singletonList("test")}))
                .f52(mapOf(1, "test"))
                .f53(mapOf("test", new String[]{"test"}))
                .f55(mapOf("test", fixedArrayOf("test")))
                .f56(mapOf("test", new Map[]{mapOf("test", "test")}))
                .f57(immutableMapOf(mapOf("test", new Map[]{mapOf("test", "test")})))
                .f59(LocalDateTime.now())
                .f60(ZonedDateTime.now())
                .f61(Duration.ofDays(1))
                .f62(Model.E.FIRST)
                .f63(Optional.of(fixedArrayOf(lazy(() -> "test"), null, lazy(() -> "test2"))))
                .f64ForNull(null)
                .f65ForEmpty("")
                .build();
        return model.toBuilder()
                .streamField(streamOf("test"))
                .f58(model)
                .f66(fixedArrayOf(model))
                .f67(setOf(model))
                .f68(mapOf("test", model))
                .build();
    }
}
