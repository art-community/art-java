/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.grpc.client.module;

import io.grpc.ManagedChannel;
import lombok.Getter;
import org.apache.logging.log4j.Logger;
import io.art.core.module.Module;
import io.art.grpc.client.configuration.GrpcClientModuleConfiguration;
import io.art.grpc.client.constants.GrpcClientModuleConstants;
import io.art.grpc.client.state.GrpcClientModuleState;
import static java.text.MessageFormat.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static lombok.AccessLevel.PRIVATE;
import static io.art.core.context.Context.context;
import static io.art.core.context.Context.contextIsNotReady;
import static io.art.core.wrapper.ExceptionWrapper.ignoreException;
import static io.art.grpc.client.configuration.GrpcClientModuleConfiguration.DEFAULT_CONFIGURATION;
import static io.art.grpc.client.configuration.GrpcClientModuleConfiguration.GrpcClientModuleDefaultConfiguration;
import static io.art.grpc.client.constants.GrpcClientModuleConstants.*;
import static io.art.logging.LoggingModule.loggingModule;

@Getter
public class GrpcClientModule implements Module<GrpcClientModuleConfiguration, GrpcClientModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final GrpcClientModuleConfiguration grpcModule = context().getModule(GRPC_CLIENT_MODULE_ID, GrpcClientModule::new);
    @Getter(lazy = true, value = PRIVATE)
    private static final GrpcClientModuleState grpcModuleState = context().getModuleState(GRPC_CLIENT_MODULE_ID, GrpcClientModule::new);
    private final String id = GrpcClientModuleConstants.GRPC_CLIENT_MODULE_ID;
    private final GrpcClientModuleConfiguration defaultConfiguration = GrpcClientModuleDefaultConfiguration.DEFAULT_CONFIGURATION;
    private final GrpcClientModuleState state = new GrpcClientModuleState();
    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = loggingModule().getLogger(GrpcClientModule.class);

    public static GrpcClientModuleConfiguration grpcClientModule() {
        if (contextIsNotReady()) {
            return DEFAULT_CONFIGURATION;
        }
        return getGrpcModule();
    }

    public static GrpcClientModuleState grpcClientModuleState() {
        return getGrpcModuleState();
    }

    @Override
    public void onUnload() {
        grpcClientModuleState().getChannels().forEach(this::shutdownChannel);
    }

    private void shutdownChannel(ManagedChannel channel) {
        ignoreException(() -> {
            if (channel.isShutdown() || channel.isTerminated()) {
                return;
            }
            channel.shutdownNow();
            channel.awaitTermination(GRPC_CHANNEL_SHUTDOWN_TIMEOUT, MILLISECONDS);
            getLogger().info(format(GRPC_CHANNEL_SHUTDOWN, channel.toString()));
        }, getLogger()::error);
    }
}
