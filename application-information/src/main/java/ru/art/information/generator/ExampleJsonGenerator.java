package ru.art.information.generator;

import lombok.experimental.*;
import org.jeasy.random.*;
import ru.art.entity.mapper.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.reflection.LambdaGenericParametersExtractor.*;
import static ru.art.json.module.JsonModule.*;

@UtilityClass
public class ExampleJsonGenerator {
    private static final EasyRandom RANDOMIZER = new EasyRandom(new EasyRandomParameters()
            .collectionSizeRange(1, 1)
            .ignoreRandomizationErrors(true)
            .stringLengthRange(1, 5));

    public static String generateExampleJson(ValueFromModelMapper<?, ?> mapper) {
        try {
            return jsonModule().getObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(RANDOMIZER.nextObject(extractFirstLambdaGenericParameters(mapper)));
        } catch (Throwable throwable) {
            return EMPTY_STRING;
        }
    }

    public static String generateExampleJson(ValueToModelMapper<?, ?> mapper) {
        try {
            return jsonModule().getObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(RANDOMIZER.nextObject(extractFirstLambdaGenericParameters(mapper)));
        } catch (Throwable throwable) {
            return EMPTY_STRING;
        }
    }
}
