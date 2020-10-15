/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.rsocket.configuration;

import io.art.core.source.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.entity.constants.EntityConstants.*;
import static io.art.entity.constants.EntityConstants.DataFormat.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;

@Getter
@RequiredArgsConstructor
public class RsocketMethodConfiguration {
    private final DataFormat defaultDataFormat;
    private final boolean tracing;

    public static RsocketMethodConfiguration from(RsocketServiceConfiguration serviceConfiguration, ConfigurationSource source) {
        boolean tracing = orElse(source.getBool(TRACING_KEY), serviceConfiguration.isTracing());
        DataFormat dataFormat = dataFormat(source.getString(DATA_FORMAT_KEY), serviceConfiguration.getDefaultDataFormat());
        return new RsocketMethodConfiguration(dataFormat, tracing);
    }
}
