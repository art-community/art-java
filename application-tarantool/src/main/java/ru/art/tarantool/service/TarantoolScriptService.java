package ru.art.tarantool.service;

import lombok.NoArgsConstructor;
import ru.art.tarantool.configuration.lua.TarantoolCommonScriptConfiguration;
import ru.art.tarantool.configuration.lua.TarantoolValueScriptConfiguration;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.tarantool.configuration.lua.TarantoolCommonScriptConfiguration.*;
import static ru.art.tarantool.configuration.lua.TarantoolValueScriptConfiguration.*;
import static ru.art.tarantool.executor.TarantoolLuaExecutor.evaluateLuaScript;
import static ru.art.tarantool.module.TarantoolModule.tarantoolModuleState;

@NoArgsConstructor(access = PRIVATE)
@SuppressWarnings("Duplicates")
public final class TarantoolScriptService {
    public static void evaluateValueScript(String instanceId, String spaceName) {
        TarantoolValueScriptConfiguration valueScriptConfiguration = tarantoolValueScript(spaceName);
        if (tarantoolModuleState().getLoadedValueScripts().contains(valueScriptConfiguration)) {
            return;
        }
        evaluateLuaScript(instanceId, valueScriptConfiguration.toLua());
        tarantoolModuleState().getLoadedValueScripts().add(valueScriptConfiguration);
    }

    public static void evaluateCommonScript(String instanceId, String spaceName) {
        TarantoolCommonScriptConfiguration scriptConfiguration = tarantoolCommonScript(spaceName);
        if (tarantoolModuleState().getLoadedCommonScripts().contains(scriptConfiguration)) {
            return;
        }
        evaluateLuaScript(instanceId, scriptConfiguration.toLua());
        tarantoolModuleState().getLoadedCommonScripts().add(scriptConfiguration);
    }

    public static void evaluateValueScript(String instanceId, String spaceName, String indexName) {
        TarantoolValueScriptConfiguration valueScriptConfiguration = tarantoolValueScript(spaceName, indexName);
        if (tarantoolModuleState().getLoadedValueScripts().contains(valueScriptConfiguration)) {
            return;
        }
        evaluateLuaScript(instanceId, valueScriptConfiguration.toLua());
        tarantoolModuleState().getLoadedValueScripts().add(valueScriptConfiguration);
    }
}
