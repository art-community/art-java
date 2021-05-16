/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.logging.configuration;

import io.art.core.source.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.logging.constants.LoggingModuleConstants.ConfigurationKeys.*;
import java.nio.file.*;

@Getter
@Builder(toBuilder = true)
public class UnixTcpWriterConfiguration {
    private final Path socketPath;

    public static UnixTcpWriterConfiguration from(ConfigurationSource source, UnixTcpWriterConfiguration fallback) {
        UnixTcpWriterConfigurationBuilder builder = builder();
        builder.socketPath(let(source.getString(SOCKET_KEY), Paths::get, fallback.socketPath));
        return builder.build();
    }

    public static UnixTcpWriterConfiguration defaults() {
        return builder().build();
    }
}
