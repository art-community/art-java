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
