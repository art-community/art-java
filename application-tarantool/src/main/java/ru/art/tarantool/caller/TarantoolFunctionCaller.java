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

package ru.art.tarantool.caller;

import lombok.NoArgsConstructor;
import org.tarantool.TarantoolClient;
import ru.art.tarantool.exception.TarantoolExecutionException;
import static java.text.MessageFormat.format;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.caster.Caster.cast;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.*;
import static ru.art.tarantool.module.TarantoolModule.tarantoolModule;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = PRIVATE)
public final class TarantoolFunctionCaller {
    public static List<?> callTarantoolFunction(TarantoolClient client, String functionName) {
        logFunctionCall(functionName, emptyList(), CALLING_FUNCTION);
        try {
            List<?> result = cast(client.syncOps().call(functionName));
            logFunctionCall(functionName, result, CALLED_FUNCTION);
            return result;
        } catch (Throwable e) {
            logException(functionName, e);
            throw new TarantoolExecutionException(e);
        }
    }

    public static List<?> callTarantoolFunction(TarantoolClient client, String functionName, Collection<?> args) {
        logFunctionCall(functionName, args, CALLING_FUNCTION);
        try {
            List<?> result = cast(client.syncOps().call(functionName, args.toArray(new Object[0])));
            logFunctionCall(functionName, result, CALLED_FUNCTION);
            return result;
        } catch (Throwable e) {
            logException(functionName, e);
            throw new TarantoolExecutionException(e);
        }
    }

    public static CompletableFuture<List<?>> callTarantoolFunctionAsync(TarantoolClient client, String functionName, Collection<?> args) {
        logFunctionCall(functionName, args, CALLING_FUNCTION);
        try {
            return ((CompletableFuture<List<?>>) client.asyncOps()
                    .call(functionName, args.toArray(new Object[0])))
                    .handle((result, e) -> {
                        if (isNull(e)) {
                            logFunctionCall(functionName, result, CALLED_FUNCTION);
                            return result;
                        }
                        logException(functionName, (Exception) e);
                        throw new TarantoolExecutionException(e);
                    });

        } catch (Throwable e) {
            logException(functionName, e);
            throw new TarantoolExecutionException(e);
        }
    }

    private static void logFunctionCall(String functionName, Collection<?> arguments, String callingState) {
        if (!tarantoolModule().isEnableTracing()) {
            return;
        }
        loggingModule()
                .getLogger(TarantoolFunctionCaller.class)
                .trace(format(callingState, functionName, arguments));
    }

    private static void logException(String functionName, Throwable e) {
        if (!tarantoolModule().isEnableTracing()) {
            return;
        }
        loggingModule()
                .getLogger(TarantoolFunctionCaller.class)
                .error(format(FAILED_FUNCTION, functionName), e);
    }
}
