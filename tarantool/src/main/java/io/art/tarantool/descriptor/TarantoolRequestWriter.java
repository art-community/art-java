package io.art.tarantool.descriptor;

import io.art.tarantool.exception.*;
import io.art.tarantool.model.*;
import io.netty.buffer.*;
import lombok.experimental.*;
import org.msgpack.core.*;
import org.msgpack.value.*;
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
            Map<IntegerValue, IntegerValue> headerMap = new HashMap<>();
            headerMap.put(newInteger(IPROTO_CODE), newInteger(header.getCode()));
            headerMap.put(newInteger(IPROTO_SYNC), newInteger(header.getSyncId()));
            packer.packValue(newMap(headerMap));
            packer.packValue(body);
            ByteBuf buffer = allocateWriteBuffer(transportModule().configuration());
            long outputSize = packer.getTotalWrittenBytes();
            buffer.capacity((int) (outputSize + MINIMAL_HEADER_SIZE));
            byte[] output = packer.toByteArray();
            packer.clear();
            packer.packLong(outputSize);
            buffer.writeBytes(packer.toByteArray());
            packer.close();
            return buffer.writeBytes(output);
        } catch (IOException ioException) {
            throw new TarantoolModuleException(ioException);
        }
    }
}
