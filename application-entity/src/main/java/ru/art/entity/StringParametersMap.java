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

package ru.art.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import ru.art.core.checker.CheckerForEmptiness;
import ru.art.entity.constants.ValueType;
import static ru.art.entity.constants.ValueType.STRING_PARAMETERS_MAP;
import java.util.Map;


@Getter
@Builder
@EqualsAndHashCode
public class StringParametersMap implements Value {
    private final ValueType type = STRING_PARAMETERS_MAP;
    @Singular
    private Map<String, String> parameters;

    public String getParameter(String key) {
        return parameters.get(key);
    }

    @Override
    public String toString() {
        return parameters.toString();
    }

    @Override
    public boolean isEmpty() {
        return CheckerForEmptiness.isEmpty(parameters);
    }
}
