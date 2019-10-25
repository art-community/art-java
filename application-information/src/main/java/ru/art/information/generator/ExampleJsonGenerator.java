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

package ru.art.information.generator;

import lombok.experimental.*;
import org.jeasy.random.*;
import ru.art.entity.*;
import ru.art.entity.mapper.*;
import static java.util.Arrays.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.reflection.LambdaGenericParametersExtractor.*;
import static ru.art.json.module.JsonModule.*;
import java.util.*;

@UtilityClass
public class ExampleJsonGenerator {
    private static final EasyRandom RANDOMIZER = new EasyRandom(new EasyRandomParameters()
            .collectionSizeRange(1, 1)
            .ignoreRandomizationErrors(true)
            .stringLengthRange(1, 5));

    public static String generateExampleJson(ValueFromModelMapper<?, ?> mapper) {
        try {
            Optional<Class<?>> lambdaParameter = extractFirstLambdaGenericParameters(mapper);
            if (!lambdaParameter.isPresent()) {
                return EMPTY_STRING;
            }
            Class<?> parameter = lambdaParameter.get();
            if (stream(parameter.getInterfaces()).anyMatch(parent -> parent == Value.class)) {
                return EMPTY_STRING;
            }
            return jsonModule().getObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(RANDOMIZER.nextObject(parameter));
        } catch (Throwable throwable) {
            return EMPTY_STRING;
        }
    }

    public static String generateExampleJson(ValueToModelMapper<?, ?> mapper) {
        try {
            Optional<? extends Class<?>> returnParameter = extractLambdaReturnValue(mapper);
            if (!returnParameter.isPresent()) {
                return EMPTY_STRING;
            }
            Class<?> parameter = returnParameter.get();

            if (stream(parameter.getInterfaces()).anyMatch(parent -> parent == Value.class)) {
                return EMPTY_STRING;
            }
            return jsonModule().getObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(RANDOMIZER.nextObject(parameter));
        } catch (Throwable throwable) {
            return EMPTY_STRING;
        }
    }
}
