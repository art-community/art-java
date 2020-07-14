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

package io.art.tarantool.executor;

import lombok.*;
import lombok.experimental.*;
import org.apache.logging.log4j.*;
import io.art.core.extension.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import static io.art.logging.LoggingModule.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.*;
import static io.art.tarantool.module.TarantoolModule.*;
import java.io.*;

@UtilityClass
public final class TarantoolLuaExecutor {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = loggingModule().getLogger(TarantoolLuaExecutor.class);

    public static void executeLuaScript(String instanceId, String scriptName) {
        String script;
        InputStream scriptStream = TarantoolLuaExecutor.class
                .getClassLoader()
                .getResourceAsStream(scriptName);
        if (isNull(scriptStream)) {
            return;
        }
        script = InputStreamExtensions.toString(scriptStream);
        evaluateLuaScript(instanceId, script);
    }

    public static void evaluateLuaScript(String instanceId, String script) {
        if (tarantoolModule().isEnableTracing()) {
            getLogger().trace(format(EVALUATING_LUA_SCRIPT, script));
        }
        tarantoolModuleState().getClient(instanceId).syncOps().eval(script);
    }
}
