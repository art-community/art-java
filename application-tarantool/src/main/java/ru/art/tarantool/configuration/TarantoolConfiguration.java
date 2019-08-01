package ru.art.tarantool.configuration;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import ru.art.tarantool.configuration.lua.TarantoolInitialConfiguration;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolInstanceMode;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolInstanceMode.LOCAL;
import java.util.Map;

@Getter
@Builder
public class TarantoolConfiguration {
    private TarantoolConnectionConfiguration connectionConfiguration;
    private TarantoolInitialConfiguration initialConfiguration;
    @Builder.Default
    private final TarantoolInstanceMode instanceMode = LOCAL;
    private final Map<String, TarantoolEntityMapping> entityMapping = mapOf();

    @Getter
    @Builder(buildMethodName = "map", builderMethodName = "mapping")
    public static class TarantoolEntityMapping {
        @Singular("field")
        private final Map<String, Integer> fieldMapping;

        public int number(String fieldName) {
            return fieldMapping.get(fieldName);
        }
    }
}
