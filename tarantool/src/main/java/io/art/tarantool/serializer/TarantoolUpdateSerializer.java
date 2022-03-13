package io.art.tarantool.serializer;

import io.art.meta.model.*;
import io.art.storage.updater.*;
import io.art.storage.updater.UpdaterImplementation.*;
import io.art.tarantool.descriptor.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.UpdateOperation.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;

@RequiredArgsConstructor
public class TarantoolUpdateSerializer {
    private final TarantoolModelWriter writer;

    @AllArgsConstructor
    private static class UpdateGroup {
        Set<MetaField<?, ?>> fields;
        List<UpdateOperator> operators;
    }

    public ImmutableValue serializeUpdate(UpdaterImplementation<?> updater) {
        List<UpdateGroup> groups = linkedList();

        UpdateGroup group = new UpdateGroup(set(), linkedList());
        groups.add(group);

        for (UpdateOperator operator : updater.getOperators()) {
            if (group.fields.contains(operator.getField())) {
                groups.add(group = new UpdateGroup(setOf(operator.getField()), linkedListOf(operator)));
                continue;
            }
            group.fields.add(operator.getField());
            group.operators.add(operator);
        }

        return serializeGroups(groups);
    }

    private ImmutableArrayValue serializeGroups(List<UpdateGroup> groups) {
        List<ImmutableValue> serializedGroups = linkedList();
        for (UpdateGroup group : groups) {
            List<ImmutableValue> serializedGroup = linkedList();
            for (UpdateOperator operator : group.operators) {
                switch (operator.getOperation()) {
                    case ADD:
                        serializedGroup.add(newArray(
                                ADDITION,
                                newInteger(operator.getField().index() + 1),
                                serializeValue(operator.getField().type(), operator.getValues().get(0))
                        ));
                        break;
                    case SUBTRACT:
                        serializedGroup.add(newArray(
                                SUBTRACTION,
                                newInteger(operator.getField().index() + 1),
                                serializeValue(operator.getField().type(), operator.getValues().get(0))
                        ));
                        break;
                    case BITWISE_AND:
                        serializedGroup.add(newArray(
                                BITWISE_AND,
                                newInteger(operator.getField().index() + 1),
                                serializeValue(operator.getField().type(), operator.getValues().get(0))
                        ));
                        break;
                    case BITWISE_OR:
                        serializedGroup.add(newArray(
                                BITWISE_OR,
                                newInteger(operator.getField().index() + 1),
                                serializeValue(operator.getField().type(), operator.getValues().get(0))
                        ));
                        break;
                    case BITWISE_XOR:
                        serializedGroup.add(newArray(
                                BITWISE_XOR,
                                newInteger(operator.getField().index() + 1),
                                serializeValue(operator.getField().type(), operator.getValues().get(0))
                        ));
                        break;
                    case SET:
                        serializedGroup.add(newArray(
                                ASSIGMENT,
                                newInteger(operator.getField().index() + 1),
                                serializeValue(operator.getField().type(), operator.getValues().get(0))
                        ));
                        break;
                    case SPLICE:
                        serializedGroup.add(newArray(
                                SPLICE,
                                newInteger(operator.getField().index() + 1),
                                serializeValue(operator.getField().type(), operator.getValues().get(0)),
                                serializeValue(operator.getField().type(), operator.getValues().get(1)),
                                serializeValue(operator.getField().type(), operator.getValues().get(2)),
                                serializeValue(operator.getField().type(), operator.getValues().get(3))
                        ));
                        break;
                }
            }
            serializedGroups.add(newArray(serializedGroup));
        }
        return newArray(serializedGroups);
    }

    private Value serializeValue(MetaType<?> type, Object value) {
        return writer.write(type, value);
    }
}
