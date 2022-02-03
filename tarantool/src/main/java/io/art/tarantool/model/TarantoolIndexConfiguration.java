package io.art.tarantool.model;

import io.art.core.annotation.*;
import io.art.meta.model.*;
import io.art.tarantool.constants.TarantoolModuleConstants.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.meta.Meta.*;
import java.util.*;
import java.util.function.*;

@Public
@Builder(toBuilder = true)
@Getter
@Accessors(fluent = true)
public class TarantoolIndexConfiguration {
    private final String spaceName;
    private final String indexName;
    private final Boolean ifNotExists;
    private final Integer id;
    private final IndexType type;
    private final Boolean unique;
    @Singular("part")
    private final List<TarantoolIndexPartConfiguration> parts;
    private final TarantoolRtreeIndexConfiguration rtreeConfiguration;
    private final TarantoolTreeIndexConfiguration treeConfiguration;
    private final TarantoolVinylIndexConfiguration vinylConfiguration;
    private final String sequence;
    private final String func;

    @Public
    @Builder
    @Getter
    @Accessors(fluent = true)
    public static class TarantoolRtreeIndexConfiguration {
        private final Integer dimension;
        private final String distance;
    }

    @Public
    @Builder
    @Getter
    @Accessors(fluent = true)
    public static class TarantoolTreeIndexConfiguration {
        private final Boolean hint;
    }

    @Public
    @Builder
    @Getter
    @Accessors(fluent = true)
    public static class TarantoolVinylIndexConfiguration {
        private final int bloomFrp;
        private final int pageSize;
        private final int rangeSize;
        private final int runCountPerLevel;
        private final int runSizeRatio;
    }

    public static <T extends MetaClass<?>> TarantoolIndexConfiguration.TarantoolIndexConfigurationBuilder indexFor(Class<?> type, Function<T, MetaField<?>> fieldExtractor) {
        T meta = cast(declaration(type));
        return TarantoolIndexConfiguration.builder()
                .spaceName(idByDash(type))
                .indexName(fieldExtractor.apply(meta).name());
    }
}
