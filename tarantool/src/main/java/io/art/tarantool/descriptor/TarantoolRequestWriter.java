package io.art.tarantool.descriptor;

import io.art.tarantool.exception.*;
import io.art.tarantool.model.*;
import io.netty.buffer.*;
import lombok.experimental.*;
import org.msgpack.core.*;
import org.msgpack.value.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProtocolConstants.*;
import static io.art.transport.allocator.WriteBufferAllocator.*;
import static io.art.transport.module.TransportModule.*;
import static org.msgpack.core.MessagePack.*;
import static org.msgpack.value.ValueFactory.*;
import java.io.*;
import java.util.*;

@UtilityClass
public class TarantoolRequestWriter {
    public static ByteBuf writeTarantoolRequest(TarantoolHeader header, Value body) {
        MessageBufferPacker packer = newDefaultBufferPacker();
        try {
            Map<IntegerValue, IntegerValue> headerMap = map(2);
            headerMap.put(IPROTO_CODE, header.getCode());
            headerMap.put(IPROTO_SYNC, header.getSyncId());
            packer.packValue(newMap(headerMap));
            packer.packValue(body);
            ByteBuf buffer = allocateWriteBuffer(transportModule().configuration());
            long outputSize = packer.getTotalWrittenBytes();
            buffer.capacity((int) (outputSize + SIZE_BYTES));
            byte[] output = packer.toByteArray();
            packer.clear();
            packer.packLong(outputSize);
            buffer.writeBytes(packer.toByteArray());
            packer.close();
            return buffer.writeBytes(output);
        } catch (IOException ioException) {
            throw new TarantoolException(ioException);
        }
    }
}
