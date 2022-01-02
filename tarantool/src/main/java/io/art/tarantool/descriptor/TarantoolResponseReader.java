package io.art.tarantool.descriptor;

import io.art.tarantool.exception.*;
import io.art.tarantool.model.*;
import io.netty.buffer.*;
import lombok.experimental.*;
import org.msgpack.core.*;
import org.msgpack.value.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProtocolConstants.*;
import static org.msgpack.core.MessagePack.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;

@UtilityClass
public class TarantoolResponseReader {
    public static TarantoolResponse readTarantoolResponse(ByteBuf bytes) {
        try {
            ByteBuf sizeBuffer = bytes.readBytes(MINIMAL_HEADER_SIZE);
            int size;
            try (ByteBufInputStream inputStream = new ByteBufInputStream(sizeBuffer)) {
                MessageUnpacker unpacker = newDefaultUnpacker(inputStream);
                size = unpacker.unpackInt();
            }
            sizeBuffer.release();
            if (size > 0) {
                ByteBuf bodyBuffer = bytes.readBytes(size);
                TarantoolResponse response;
                try (ByteBufInputStream inputStream = new ByteBufInputStream(bodyBuffer)) {
                    MessageUnpacker unpacker = newDefaultUnpacker(inputStream);
                    Map<Value, Value> header = unpacker.unpackValue().asMapValue().map();
                    long syncId = header.get(newInteger(IPROTO_SYNC)).asIntegerValue().asLong();
                    long code = header.get(newInteger(IPROTO_CODE)).asIntegerValue().asLong();
                    TarantoolHeader tarantoolHeader = new TarantoolHeader(syncId, code);
                    response = unpacker.hasNext()
                            ? new TarantoolResponse(tarantoolHeader, code == IPROTO_OK, unpacker.unpackValue())
                            : new TarantoolResponse(tarantoolHeader, code == IPROTO_OK);
                }
                bodyBuffer.release();
                return response;
            }
            throw new TarantoolModuleException("");
        } catch (Throwable throwable) {
            throw new TarantoolModuleException(throwable);
        }
    }
}
