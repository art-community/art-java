package io.art.tarantool.serializer;

import io.art.meta.model.*;
import io.art.storage.updater.*;
import io.art.tarantool.descriptor.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.UpdateOperatation.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;

@RequiredArgsConstructor
public class TarantoolUpdateSerializer {
    private final TarantoolModelWriter writer;

    public ImmutableValue serializeUpdate(UpdaterImplementation<?> updater) {
        List<ImmutableValue> serialized = linkedList();
        for (UpdaterImplementation.UpdateOperator operator : updater.getOperators()) {
            switch (operator.getOperation()) {
                case ADD:
                    serialized.add(newArray(
                            ADDITION,
                            newInteger(operator.getField().index() + 1),
                            serializeValue(operator.getField().type(), operator.getValues().get(0))
                    ));
                    break;
                case SUBTRACT:
                    serialized.add(newArray(
                            SUBTRACTION,
                            newInteger(operator.getField().index() + 1),
                            serializeValue(operator.getField().type(), operator.getValues().get(0))
                    ));
                    break;
                case BITWISE_AND:
                    serialized.add(newArray(
                            BITWISE_AND,
                            newInteger(operator.getField().index() + 1),
                            serializeValue(operator.getField().type(), operator.getValues().get(0))
                    ));
                    break;
                case BITWISE_OR:
                    serialized.add(newArray(
                            BITWISE_OR,
                            newInteger(operator.getField().index() + 1),
                            serializeValue(operator.getField().type(), operator.getValues().get(0))
                    ));
                    break;
                case BITWISE_XOR:
                    serialized.add(newArray(
                            BITWISE_XOR,
                            newInteger(operator.getField().index() + 1),
                            serializeValue(operator.getField().type(), operator.getValues().get(0))
                    ));
                    break;
                case INSERT:
                    serialized.add(newArray(
                            INSERTION,
                            newInteger(operator.getField().index() + 1),
                            serializeValue(operator.getField().type(), operator.getValues().get(0))
                    ));
                    break;
                case SET:
                    serialized.add(newArray(
                            ASSIGMENT,
                            newInteger(operator.getField().index() + 1),
                            serializeValue(operator.getField().type(), operator.getValues().get(0))
                    ));
                    break;
                case DELETE:
                    serialized.add(newArray(
                            DELETION,
                            newInteger(operator.getField().index() + 1),
                            serializeValue(operator.getField().type(), operator.getValues().get(0))
                    ));
                    break;
                case SPLICE:
                    serialized.add(newArray(
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
        return newArray(serialized);
    }

    private Value serializeValue(MetaType<?> type, Object value) {
        return writer.write(type, value);
    }
}
