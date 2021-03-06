package io.art.configurator.source;

import io.art.core.collection.*;
import io.art.core.source.*;
import lombok.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.ConfigurationSourceType.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.constants.StringConstants.*;
import static java.lang.String.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;


@RequiredArgsConstructor
public class DelegateConfigurationSource implements ConfigurationSource {
    private final ImmutableArray<ConfigurationSource> sources;
    @Getter
    private final ModuleConfigurationSourceType type = DELEGATE;
    @Getter
    private final String section = EMPTY_STRING;
    @Getter(lazy = true)
    private final String path = join(NEXT_ARROW, sources.stream().map(ConfigurationSource::getPath).collect(listCollector()));

    @Override
    public NestedConfiguration getNested(String path) {
        for (ConfigurationSource source : sources.reverse()) {
            NestedConfiguration nested = source.getNested(path);
            if (nonNull(nested)) {
                return nested;
            }
        }
        return null;
    }

    @Override
    public String dump() {
        return sources.stream().map(ConfigurationSource::dump).collect(joining(NEW_LINE));
    }

    @Override
    public ImmutableSet<String> getKeys() {
        return sources
                .stream()
                .flatMap(source -> source.getKeys().stream())
                .collect(immutableSetCollector());
    }
}
