package ru.art.tarantool.configuration;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import ru.art.tarantool.configuration.lua.TarantoolInitialConfiguration;
import ru.art.tarantool.model.TarantoolEntityFieldsMapping;
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
    @Singular("entityFieldsMappings")
    private final Map<String, TarantoolEntityFieldsMapping> entityFieldsMappings;
}
