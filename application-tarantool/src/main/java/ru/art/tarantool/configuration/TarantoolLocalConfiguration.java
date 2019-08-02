/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.tarantool.configuration;

import lombok.Builder;
import lombok.Getter;
import static java.nio.file.Paths.get;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.tarantool.constants.TarantoolModuleConstants.DEFAULT_STARTUP_TIMEOUT_SECONDS;
import static ru.art.tarantool.constants.TarantoolModuleConstants.DEFAULT_TARANTOOL_EXECUTABLE;

@Getter
@Builder
public class TarantoolLocalConfiguration {
    @Builder.Default
    private final String executableApplicationName = DEFAULT_TARANTOOL_EXECUTABLE;
    @Builder.Default
    private final long startupTimeoutSeconds = DEFAULT_STARTUP_TIMEOUT_SECONDS;
    @Builder.Default
    private final String workingDirectory = get(EMPTY_STRING).toAbsolutePath().toString();
}
