package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.model.*;
import io.art.tarantool.storage.*;
import org.msgpack.value.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.function.*;

@Public
public class TarantoolSchemaService {
    private final Supplier<TarantoolStorage> storage;
    private final TarantoolModelWriter writer;
    private final TarantoolModelReader reader;

    public TarantoolSchemaService(Supplier<TarantoolStorage> storage) {
        this.storage = storage;
        writer = tarantoolModule().configuration().getWriter();
        reader = tarantoolModule().configuration().getReader();
    }


    public void createSpace(TarantoolSpaceConfiguration configuration) {
        ArrayValue input = newArray(newString(configuration.name()));
        storage.get().immutable().call(SCHEMA_CREATE_SPACE, input).subscribe();
    }

    public void createIndex(TarantoolIndexConfiguration configuration) {
        ArrayValue input = newArray(newString(configuration.spaceName()), newString(configuration.indexName()), newArray(
                newString("unique"), newBoolean(true),
                newString("parts"), newArray(newArray(newString("field"), newInteger(1)), newArray(newString("field"), newString("unsigned")))
                )
        );
        storage.get().immutable().call(SCHEMA_CREATE_INDEX, input).subscribe();
    }
}
