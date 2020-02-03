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

package ru.art.tarantool.service;

import lombok.experimental.*;
import ru.art.tarantool.configuration.lua.*;
import static ru.art.tarantool.configuration.lua.TarantoolCommonScriptConfiguration.*;
import static ru.art.tarantool.configuration.lua.TarantoolValueScriptConfiguration.*;
import static ru.art.tarantool.executor.TarantoolLuaExecutor.*;
import static ru.art.tarantool.module.TarantoolModule.*;
import java.util.concurrent.locks.*;

@UtilityClass
@SuppressWarnings("Duplicates")
public final class TarantoolScriptService {
    private static final ReentrantLock LOCK = new ReentrantLock();

    public static void evaluateValueScript(String instanceId, String spaceName) {
        TarantoolValueScriptConfiguration valueScriptConfiguration = tarantoolValueScript(spaceName);
        if (tarantoolModuleState().getLoadedValueScripts().contains(valueScriptConfiguration)) {
            return;
        }
        LOCK.lock();
        try {
            if (tarantoolModuleState().getLoadedValueScripts().contains(valueScriptConfiguration)) {
                return;
            }
            evaluateLuaScript(instanceId, valueScriptConfiguration.toLua());
            tarantoolModuleState().getLoadedValueScripts().add(valueScriptConfiguration);
        } finally {
            LOCK.unlock();
        }
    }

    public static void evaluateCommonScript(String instanceId, String spaceName) {
        TarantoolCommonScriptConfiguration commonScriptConfiguration = tarantoolCommonScript(spaceName);
        if (tarantoolModuleState().getLoadedCommonScripts().contains(commonScriptConfiguration)) {
            return;
        }
        LOCK.lock();
        try {
            if (tarantoolModuleState().getLoadedCommonScripts().contains(commonScriptConfiguration)) {
                return;
            }
            evaluateLuaScript(instanceId, commonScriptConfiguration.toLua());
            tarantoolModuleState().getLoadedCommonScripts().add(commonScriptConfiguration);
        } finally {
            LOCK.unlock();
        }
    }

    public static void evaluateValueScript(String instanceId, String spaceName, String indexName) {
        TarantoolValueScriptConfiguration valueScriptConfiguration = tarantoolValueScript(spaceName, indexName);
        if (tarantoolModuleState().getLoadedValueScripts().contains(valueScriptConfiguration)) {
            return;
        }
        LOCK.lock();
        try {
            if (tarantoolModuleState().getLoadedValueScripts().contains(valueScriptConfiguration)) {
                return;
            }
            evaluateLuaScript(instanceId, valueScriptConfiguration.toLua());
            tarantoolModuleState().getLoadedValueScripts().add(valueScriptConfiguration);
        } finally {
            LOCK.unlock();
        }
    }
}
