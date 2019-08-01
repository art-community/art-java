package ru.art.entity.tuple.schema;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.art.entity.StringParametersMap;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.art.entity.constants.ValueType.STRING_PARAMETERS_MAP;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
public class StringParametersSchema extends ValueSchema {
    private final List<String> stringParametersSchema = dynamicArrayOf();

    StringParametersSchema(StringParametersMap parameters) {
        super(STRING_PARAMETERS_MAP);
        stringParametersSchema.addAll(parameters.getParameters().keySet());
    }

    private StringParametersSchema() {
        super(STRING_PARAMETERS_MAP);
    }

    @Override
    public List<?> toTuple() {
        List<?> tuple = dynamicArrayOf(getType().name());
        tuple.add(cast(stringParametersSchema));
        return tuple;
    }

    public static StringParametersSchema fromTuple(List<?> tuple) {
        StringParametersSchema schema = new StringParametersSchema();
        tuple.stream()
                .skip(1)
                .map(element -> (List<?>) element)
                .map(element -> (String) element.get(0))
                .forEach(schema.stringParametersSchema::add);
        return schema;
    }
}