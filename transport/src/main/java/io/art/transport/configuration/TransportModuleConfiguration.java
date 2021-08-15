package io.art.transport.configuration;

import io.art.core.module.*;
import io.art.core.source.*;
import io.art.transport.constants.TransportModuleConstants.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.transport.constants.TransportModuleConstants.BufferType.*;
import static io.art.transport.constants.TransportModuleConstants.ConfigurationKeys.*;
import static io.art.transport.constants.TransportModuleConstants.Defaults.*;

@Getter
public class TransportModuleConfiguration implements ModuleConfiguration {
    private TransportPoolConfiguration commonPoolConfiguration = TransportPoolConfiguration.defaults();
    private BufferType writeBufferType = DEFAULT_BUFFER_TYPE;
    private int writeBufferInitialCapacity = DEFAULT_BUFFER_INITIAL_CAPACITY;
    private int writeBufferMaxCapacity = DEFAULT_BUFFER_MAX_CAPACITY;

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<TransportModuleConfiguration, Configurator> {
        private final TransportModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            if (source.has(TRANSPORT_COMMON_SECTION)) {
                this.configuration.commonPoolConfiguration = TransportPoolConfiguration.from(source, configuration.getCommonPoolConfiguration());
            }
            if (source.has(BUFFER_WRITE_SECTION)) {
                NestedConfiguration buffer = source.getNested(BUFFER_WRITE_SECTION);
                configuration.writeBufferType = bufferType(buffer.getString(BUFFER_TYPE_KEY), configuration.getWriteBufferType());
                configuration.writeBufferInitialCapacity = orElse(buffer.getInteger(BUFFER_INITIAL_CAPACITY_KEY), configuration.getWriteBufferInitialCapacity());
                configuration.writeBufferMaxCapacity = orElse(buffer.getInteger(BUFFER_MAX_CAPACITY_KEY), configuration.getWriteBufferMaxCapacity());
            }
            return this;
        }

        @Override
        public Configurator initialize(TransportModuleConfiguration configuration) {
            this.configuration.writeBufferType = configuration.getWriteBufferType();
            this.configuration.writeBufferInitialCapacity = configuration.getWriteBufferInitialCapacity();
            this.configuration.writeBufferMaxCapacity = configuration.getWriteBufferMaxCapacity();
            this.configuration.commonPoolConfiguration = configuration.getCommonPoolConfiguration();
            return this;
        }
    }
}
