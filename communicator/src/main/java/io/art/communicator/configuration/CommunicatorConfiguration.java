/*
 * ART
 *
 * Copyright 2019-2022 ART
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

package io.art.communicator.configuration;

import io.art.communicator.refresher.*;
import io.art.communicator.registry.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.core.source.*;
import lombok.Builder;
import lombok.*;
import static io.art.communicator.configuration.CommunicatorActionsConfiguration.*;
import static io.art.communicator.constants.CommunicatorConstants.ConfigurationKeys.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.property.LazyProperty.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import java.util.*;
import java.util.function.*;

@Builder(toBuilder = true)
public class CommunicatorConfiguration {
    private final CommunicatorRefresher refresher;

    @Getter
    private final CommunicatorRefresher.Consumer consumer;

    @Getter
    private final LazyProperty<ImmutableMap<String, CommunicatorActionsConfiguration>> configurations;

    @Getter
    private final CommunicatorRegistry communicators;


    public Optional<CommunicatorActionConfiguration> getActionConfiguration(CommunicatorActionIdentifier id) {
        CommunicatorActionsConfiguration actionsConfiguration = configurations.get().get(id.getCommunicatorId());
        return ofNullable(actionsConfiguration).map(configuration -> configuration.getActions().get(id.getActionId()));
    }

    public boolean isLogging(CommunicatorActionIdentifier identifier) {
        boolean hasAction = getActionConfiguration(identifier).isPresent();
        boolean communicator = checkCommunicator(identifier, CommunicatorActionsConfiguration::getLogging, false);
        if (!hasAction) return communicator;
        return checkAction(identifier, CommunicatorActionConfiguration::getLogging, communicator);
    }

    public boolean isDeactivated(CommunicatorActionIdentifier identifier) {
        boolean hasAction = getActionConfiguration(identifier).isPresent();
        boolean communicator = checkCommunicator(identifier, CommunicatorActionsConfiguration::getDeactivated, false);
        if (!hasAction) return communicator;
        return checkAction(identifier, CommunicatorActionConfiguration::getDeactivated, communicator);
    }


    private <T> T checkCommunicator(CommunicatorActionIdentifier identifier, Function<CommunicatorActionsConfiguration, T> mapper, T defaultValue) {
        CommunicatorActionsConfiguration actionsConfiguration = configurations.get().get(identifier.getCommunicatorId());
        if (isNull(actionsConfiguration)) {
            return defaultValue;
        }
        return orElse(mapper.apply(actionsConfiguration), defaultValue);
    }

    private <T> T checkAction(CommunicatorActionIdentifier identifier, Function<CommunicatorActionConfiguration, T> mapper, T defaultValue) {
        CommunicatorActionsConfiguration actionsConfiguration = configurations.get().get(identifier.getCommunicatorId());
        if (isNull(actionsConfiguration)) {
            return defaultValue;
        }
        CommunicatorActionConfiguration actionConfiguration = actionsConfiguration.getActions().get(identifier.getActionId());
        if (isNull(actionConfiguration)) {
            return defaultValue;
        }
        return orElse(mapper.apply(actionConfiguration), defaultValue);
    }

    public static CommunicatorConfiguration communicatorConfiguration(CommunicatorRefresher refresher, CommunicatorConfiguration current, ConfigurationSource source) {
        CommunicatorConfigurationBuilder builder = current.toBuilder();
        builder.configurations(lazy(() -> merge(current.configurations.get(), ofNullable(source)
                .map(communicator -> communicator.getNestedMap(CLIENTS_SECTION, actions -> getActions(current, builder, actions)))
                .orElse(emptyImmutableMap()))));
        refresher.produce();
        return builder.build();
    }

    private static CommunicatorActionsConfiguration getActions(CommunicatorConfiguration current, CommunicatorConfigurationBuilder builder, NestedConfiguration actions) {
        return communicatorActionsConfiguration(builder.refresher, current.configurations.get().get(actions.getParent()), actions);
    }


    public static CommunicatorConfiguration communicatorConfiguration(CommunicatorRefresher refresher) {
        return CommunicatorConfiguration.builder()
                .refresher(refresher)
                .consumer(refresher.consumer())
                .communicators(new CommunicatorRegistry(lazy(ImmutableMap::emptyImmutableMap)))
                .configurations(lazy(ImmutableMap::emptyImmutableMap))
                .build();
    }
}
