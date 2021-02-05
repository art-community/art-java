package io.art.configurator.source;

import io.art.core.collection.*;
import io.art.core.source.*;
import lombok.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.ConfigurationSourceType.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.constants.StringConstants.*;
import static java.util.Objects.*;


@RequiredArgsConstructor
public class DelegateConfigurationSource implements ConfigurationSource {
    @Getter
    private final ModuleConfigurationSourceType type = DELEGATE;
    @Getter
    private final String section = EMPTY_STRING;

    private final ImmutableArray<ConfigurationSource> sources;

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
    public ImmutableSet<String> getKeys() {
        return sources
                .stream()
                .flatMap(source -> source.getKeys().stream())
                .collect(immutableSetCollector());
    }
}
