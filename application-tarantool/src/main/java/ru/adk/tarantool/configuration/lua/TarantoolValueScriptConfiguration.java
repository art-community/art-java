package ru.adk.tarantool.configuration.lua;

import lombok.*;
import org.jtwig.JtwigModel;
import static org.jtwig.JtwigTemplate.classpathTemplate;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.JTW_EXTENSION;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.*;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.Templates.VALUE;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(staticName = "tarantoolValueScript")
@RequiredArgsConstructor(staticName = "tarantoolValueScript")
public class TarantoolValueScriptConfiguration {
    private final String spaceName;
    private String indexName;

    public String toLua() {
        return classpathTemplate(VALUE + JTW_EXTENSION)
                .render(new JtwigModel()
                        .with(SPACE_NAME, spaceName)
                        .with(INDEX_NAME, indexName));
    }

}
