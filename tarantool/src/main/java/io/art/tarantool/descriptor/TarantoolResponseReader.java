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
    public static TarantoolResponse readTarantoolResponseContent(ByteBuf bytes, int size) {
        try {
            ByteBuf bodyBuffer = bytes.readBytes(size);
            TarantoolResponse response;
            try (ByteBufInputStream inputStream = new ByteBufInputStream(bodyBuffer)) {
                MessageUnpacker unpacker = newDefaultUnpacker(inputStream);
                Map<Value, Value> header = unpacker.unpackValue().asMapValue().map();
                int syncId = header.get(newInteger(IPROTO_SYNC)).asIntegerValue().asInt();
                long code = header.get(newInteger(IPROTO_CODE)).asIntegerValue().asLong();
                TarantoolHeader tarantoolHeader = new TarantoolHeader(syncId, code);
                response = unpacker.hasNext()
                        ? new TarantoolResponse(tarantoolHeader, code != IPROTO_OK, unpacker.unpackValue())
                        : new TarantoolResponse(tarantoolHeader, code != IPROTO_OK);
                unpacker.close();
            }
            bodyBuffer.release();
            return response;
        } catch (Throwable throwable) {
            throw new TarantoolException(throwable);
        }
    }

    public static int readTarantoolResponseSize(ByteBuf bytes) {
        ByteBuf sizeBuffer = bytes.readBytes(SIZE_BYTES);
        int size;
        try (ByteBufInputStream inputStream = new ByteBufInputStream(sizeBuffer)) {
            MessageUnpacker unpacker = newDefaultUnpacker(inputStream);
            size = unpacker.unpackInt();
            unpacker.close();
        } catch (Throwable throwable) {
            throw new TarantoolException(throwable);
        }
        sizeBuffer.release();
        return size;
    }
}
