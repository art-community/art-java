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

package ru.art.tarantool.configuration;

import lombok.*;
import static java.io.File.*;
import static java.nio.file.Paths.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.*;
import java.util.*;

@Getter
@Builder
public class TarantoolLocalConfiguration {
    @Builder.Default
    private final List<String> executableCommand = DEFAULT_TARANTOOL_EXECUTABLE_COMMAND;
    @Builder.Default
    private final String workingDirectory = get(EMPTY_STRING).toAbsolutePath().toString()
            + separator
            + TARANTOOL;
}
