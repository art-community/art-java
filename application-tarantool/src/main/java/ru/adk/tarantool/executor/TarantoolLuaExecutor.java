package ru.adk.tarantool.executor;

import lombok.NoArgsConstructor;
import ru.adk.core.extension.InputStreamExtensions;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.logging.LoggingModule.loggingModule;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.LoggingMessages.EVALUATING_LUA_SCRIPT;
import static ru.adk.tarantool.module.TarantoolModule.tarantoolModule;
import static ru.adk.tarantool.module.TarantoolModule.tarantoolModuleState;
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
