package ru.art.tarantool.configuration.lua;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jtwig.JtwigModel;
import static org.jtwig.JtwigTemplate.classpathTemplate;
import static ru.art.tarantool.constants.TarantoolModuleConstants.JTW_EXTENSION;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Templates.CREATE_SEQUENCE;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Templates.SEQUENCE_MANAGEMENT;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor(staticName = "tarantoolSequence")
@SuppressWarnings("Duplicates")
public class TarantoolSequenceConfiguration {
    private final String sequenceName;
    private String start;
    private String min;
    private String max;
    private String cycle;
    private String cache;
    private String step;

    public String toCreateSequenceLua() {
        return classpathTemplate(CREATE_SEQUENCE + JTW_EXTENSION)
                .render(new JtwigModel()
                        .with(SEQUENCE_NAME, sequenceName)
                        .with(START, start)
                        .with(MIN, min)
                        .with(MAX, max)
                        .with(CYCLE, cycle)
                        .with(CACHE, cache)
                        .with(STEP, step));
    }

    public String toManageSequenceLua() {
        return classpathTemplate(SEQUENCE_MANAGEMENT + JTW_EXTENSION)
                .render(new JtwigModel().with(SEQUENCE_NAME, sequenceName));
    }
}
