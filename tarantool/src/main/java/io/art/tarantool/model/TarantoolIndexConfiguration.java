package io.art.tarantool.model;

import io.art.core.annotation.*;
import io.art.meta.model.*;
import io.art.storage.index.*;
import io.art.tarantool.constants.TarantoolModuleConstants.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.tarantool.model.TarantoolIndexPartConfiguration.*;
import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import java.util.*;

@Public
@Getter
@Accessors(fluent = true)
@Builder(toBuilder = true)
public class TarantoolIndexConfiguration<C, M extends MetaClass<C>> {
    private final String spaceName;
    private final String indexName;
    private final Boolean ifNotExists;
    private final Integer id;
    private final IndexType type;
    private final Boolean unique;
    @Singular("part")
    private final List<TarantoolIndexPartConfiguration<C, M>> parts;
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

    @RequiredArgsConstructor
    public static class TarantoolIndexConfigurator<C, M extends MetaClass<C>> {
        private final M type;
        private boolean primary;
        private final List<TarantoolIndexPartConfiguration<C, M>> parts = dynamicArray();
        private final List<MetaField<M, ?>> fields = dynamicArray();

        public TarantoolIndexConfigurator<C, M> primary() {
            this.primary = true;
            return this;
        }

        public TarantoolIndexConfigurator<C, M> part(TarantoolIndexPartConfiguration<C, M> part) {
            parts.add(part);
            return this;
        }

        public TarantoolIndexConfigurator<C, M> field(MetaField<M, ?> field) {
            parts.add(indexPartFor(field).build());
            fields.add(field);
            return this;
        }

        public TarantoolIndexConfigurationBuilder<C, M> configure() {
            TarantoolIndexConfigurationBuilder<C, M> builder = TarantoolIndexConfiguration.<C, M>builder()
                    .spaceName(idByDash(type.definition().type()))
                    .indexName(fields.stream().map(MetaField::name).collect(joining()))
                    .parts(this.parts);
            if (primary) builder.id(0);
            return builder;
        }
    }

    @SafeVarargs
    public static <C, M extends MetaClass<C>> TarantoolIndexConfigurator<C, M> indexFor(M type, MetaField<M, ?>... fields) {
        TarantoolIndexConfigurator<C, M> configurator = new TarantoolIndexConfigurator<>(type);
        stream(fields).forEach(configurator::field);
        return configurator;
    }

    public static <C, M extends MetaClass<C>> TarantoolIndexConfigurator<C, M> indexFor(M type, Index index) {
        TarantoolIndexConfigurator<C, M> configurator = new TarantoolIndexConfigurator<>(type);
        index.fields().forEach(field -> configurator.field(cast(field)));
        return configurator;
    }
}
