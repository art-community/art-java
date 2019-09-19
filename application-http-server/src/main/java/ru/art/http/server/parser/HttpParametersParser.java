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

package ru.art.http.server.parser;

import ru.art.core.factory.CollectionsFactory.*;
import ru.art.entity.*;
import ru.art.http.server.model.*;
import static java.util.stream.Collectors.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.factory.CollectionsFactory.*;
import javax.servlet.http.*;
import java.util.*;

public interface HttpParametersParser {
    static StringParametersMap parseQueryParameters(HttpServletRequest request) {
        Map<String, String> parameters = request
                .getParameterMap()
                .keySet()
                .stream()
                .collect(toMap(name -> name, name -> request.getParameterValues(name)[0]));
        return StringParametersMap.builder()
                .parameters(parameters)
                .build();
    }

    static StringParametersMap parsePathParameters(HttpServletRequest request, HttpService.HttpMethod methodConfig) {
        String pathParameterString = request.getPathInfo();
        if (isEmpty(pathParameterString)) return StringParametersMap.builder().build();
        String[] pathParameterValues = pathParameterString.split(SLASH);
        if (isEmpty(pathParameterValues) || pathParameterValues.length == 1)
            return StringParametersMap.builder().build();
        Set<String> pathParameterNames = methodConfig.getPath().getParameters();
        if (isEmpty(pathParameterNames)) {
            return StringParametersMap.builder().build();
        }
        int pathParameterIndex = 1;
        MapBuilder<String, String> pathParameters = mapOf();
        String lastParameter = EMPTY_STRING;
        for (String name : pathParameterNames) {
            lastParameter = name;
            pathParameters.add(name, pathParameterValues[pathParameterIndex]);
            pathParameterIndex++;
        }
        if (isEmpty(lastParameter)) {
            return StringParametersMap.builder().build();
        }
        if (pathParameters.size() < pathParameterIndex) {
            for (int i = pathParameterIndex; i < pathParameterValues.length; i++) {
                int index = i;
                pathParameters.computeIfPresent(lastParameter, (key, value) -> value + SLASH + pathParameterValues[index]);
            }
        }
        return StringParametersMap.builder()
                .parameters(cast(pathParameters))
                .build();
    }
}

