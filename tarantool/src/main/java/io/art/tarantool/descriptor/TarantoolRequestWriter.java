package io.art.tarantool.descriptor;

import io.art.tarantool.exception.*;
import io.netty.buffer.*;
import lombok.experimental.*;
import org.msgpack.core.*;
import org.msgpack.value.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProtocolConstants.*;
import static io.art.transport.allocator.WriteBufferAllocator.*;
import static io.art.transport.module.TransportModule.*;
import static org.msgpack.value.ValueFactory.*;
import java.io.*;
import java.util.*;

@UtilityClass
public class TarantoolRequestWriter {
    public static ByteBuf writeTarantoolRequest(int syncId, int typeId, Value body) {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        try {
            Map<IntegerValue, IntegerValue> header = new HashMap<>();
            header.put(newInteger(IPROTO_CODE), newInteger(typeId));
            header.put(newInteger(IPROTO_SYNC), newInteger(syncId));
            packer.packValue(newMap(header));
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
