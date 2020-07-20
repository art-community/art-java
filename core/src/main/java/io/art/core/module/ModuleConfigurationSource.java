/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.core.module;

import java.time.*;
import java.util.*;

public interface ModuleConfigurationSource {
    Integer getInt(String path);

    Long getLong(String path);

    Boolean getBool(String path);

    Double getDouble(String path);

    Float getFloat(String path);

    String getString(String path);

    Duration getDuration(String path);

    ModuleConfigurationSource getInner(String path);

    List<Integer> getIntList(String path);

    List<Long> getLongList(String path);

    List<Boolean> getBoolList(String path);

    List<Double> getDoubleList(String path);

    List<String> getStringList(String path);

    List<Duration> getDurationList(String path);

    List<ModuleConfigurationSource> getInnerList(String path);

    Map<String, Integer> getIntMap(String path);

    Map<String, Long> getLongMap(String path);

    Map<String, Boolean> getBoolMap(String path);

    Map<String, Double> getDoubleMap(String path);

    Map<String, String> getStringMap(String path);

    Map<String, Duration> getDurationMap(String path);

    Map<String, ModuleConfigurationSource> getInnerMap(String path);

    ModuleConfigurationSourceType getType();

    Set<String> getKeys();

    boolean has(String path);

    interface ModuleConfigurationSourceType {
        int getOrder();
    }
}
