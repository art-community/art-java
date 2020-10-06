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

package ru.art.tarantool.storage.caller;

import lombok.experimental.*;
import org.tarantool.*;
import ru.art.tarantool.exception.*;
import static java.text.MessageFormat.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.logging.LoggingModule.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.*;
import static ru.art.tarantool.module.TarantoolModule.*;
import java.util.*;
import java.util.concurrent.*;

@UtilityClass
public final class TarantoolFunctionCaller {
    public static List<?> callTarantoolFunction(TarantoolClient client, String functionName) {
        logFunctionCall(functionName, emptyList(), CALLING_FUNCTION);
        try {
            List<?> result = cast(client.syncOps().call(functionName));
            logFunctionCall(functionName, result, CALLED_FUNCTION);
            return result;
        } catch (Throwable throwable) {
            logException(functionName, throwable);
            throw new TarantoolExecutionException(throwable);
        }
    }

    public static List<?> callTarantoolFunction(TarantoolClient client, String functionName, Collection<?> args) {
        logFunctionCall(functionName, args, CALLING_FUNCTION);
        try {
            List<?> result = cast(client.syncOps().call(functionName, args.toArray(new Object[0])));
            logFunctionCall(functionName, result, CALLED_FUNCTION);
            return result;
        } catch (Throwable throwable) {
            logException(functionName, throwable);
            throw new TarantoolExecutionException(throwable);
        }
    }

    public static CompletableFuture<List<?>> callTarantoolFunctionAsync(TarantoolClient client, String functionName, Collection<?> args) {
        logFunctionCall(functionName, args, CALLING_FUNCTION);
        try {
            return ((CompletableFuture<List<?>>) client.asyncOps()
                    .call(functionName, args.toArray(new Object[0])))
                    .handle((result, throwable) -> {
                        if (isNull(throwable)) {
                            logFunctionCall(functionName, result, CALLED_FUNCTION);
                            return result;
                        }
                        logException(functionName, throwable);
                        throw new TarantoolExecutionException(throwable);
                    });

        } catch (Throwable throwable) {
            logException(functionName, throwable);
            throw new TarantoolExecutionException(throwable);
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

    private static void logException(String functionName, Throwable throwable) {
        if (!tarantoolModule().isEnableTracing()) {
            return;
        }
        loggingModule()
                .getLogger(TarantoolFunctionCaller.class)
                .error(format(FAILED_FUNCTION, functionName), throwable);
    }
}
