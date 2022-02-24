package io.art.tarantool.descriptor;

import io.art.tarantool.exception.*;
import io.art.tarantool.model.*;
import io.netty.buffer.*;
import lombok.experimental.*;
import org.msgpack.core.*;
import org.msgpack.value.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProtocolConstants.*;
import static org.msgpack.core.MessagePack.*;
import java.util.*;

@UtilityClass
public class TarantoolResponseReader {
    public static TarantoolResponse readTarantoolResponseContent(ByteBuf bodyBuffer) {
        try {
            TarantoolResponse response;
            try (ByteBufInputStream inputStream = new ByteBufInputStream(bodyBuffer)) {
                MessageUnpacker unpacker = newDefaultUnpacker(inputStream);
                Map<Value, Value> header = unpacker.unpackValue().asMapValue().map();
                IntegerValue syncId = header.get(IPROTO_SYNC).asIntegerValue();
                IntegerValue code = header.get(IPROTO_CODE).asIntegerValue();
                TarantoolHeader tarantoolHeader = new TarantoolHeader(syncId, code);
                response = unpacker.hasNext()
                        ? new TarantoolResponse(tarantoolHeader, code.asInt() != IPROTO_OK.asInt(), unpacker.unpackValue())
                        : new TarantoolResponse(tarantoolHeader, code.asInt() != IPROTO_OK.asInt());
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
