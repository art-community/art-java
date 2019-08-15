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

package ru.art.tarantool.executor;

import lombok.NoArgsConstructor;
import ru.art.core.extension.InputStreamExtensions;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.EVALUATING_LUA_SCRIPT;
import static ru.art.tarantool.module.TarantoolModule.tarantoolModule;
import static ru.art.tarantool.module.TarantoolModule.tarantoolModuleState;
import java.io.InputStream;

@NoArgsConstructor(access = PRIVATE)
public final class TarantoolLuaExecutor {
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
            loggingModule().getLogger(TarantoolLuaExecutor.class).trace(format(EVALUATING_LUA_SCRIPT, script));
        }
        tarantoolModuleState().getClient(instanceId).syncOps().eval(script);
    }
}
