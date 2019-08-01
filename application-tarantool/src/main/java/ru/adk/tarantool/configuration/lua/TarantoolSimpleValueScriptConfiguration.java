package ru.adk.tarantool.configuration.lua;

import lombok.*;
import org.jtwig.JtwigModel;
import static org.jtwig.JtwigTemplate.classpathTemplate;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.JTW_EXTENSION;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.SPACE_NAME;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.Templates.SIMPLE_VALUE;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(staticName = "tarantoolSimpleValueScript")
public class TarantoolSimpleValueScriptConfiguration {
    private final String spaceName;

    public String toLua() {
        return classpathTemplate(SIMPLE_VALUE + JTW_EXTENSION)
                .render(new JtwigModel()
                        .with(SPACE_NAME, spaceName));
    }
}
