package ru.art.tarantool.configuration.lua;

import lombok.*;
import org.jtwig.JtwigModel;
import static org.jtwig.JtwigTemplate.classpathTemplate;
import static ru.art.tarantool.constants.TarantoolModuleConstants.JTW_EXTENSION;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.SPACE_NAME;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Templates.COMMON;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(staticName = "tarantoolCommonScript")
public class TarantoolCommonScriptConfiguration {
    private final String spaceName;

    public String toLua() {
        return classpathTemplate(COMMON + JTW_EXTENSION)
                .render(new JtwigModel().with(SPACE_NAME, spaceName));
    }

}
