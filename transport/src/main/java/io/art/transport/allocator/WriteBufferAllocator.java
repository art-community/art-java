package io.art.transport.allocator;

import io.art.transport.configuration.*;
import io.netty.buffer.*;
import lombok.experimental.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.netty.buffer.ByteBufAllocator.*;

@UtilityClass
public class WriteBufferAllocator {
    public static ByteBuf allocateWriteBuffer(TransportModuleConfiguration configuration) {
        if (!withTransport()) return DEFAULT.ioBuffer();
        int writeBufferInitialCapacity = configuration.getWriteBufferInitialCapacity();
        int writeBufferMaxCapacity = configuration.getWriteBufferMaxCapacity();
        switch (configuration.getWriteBufferType()) {
            case DEFAULT:
                return DEFAULT.buffer(writeBufferInitialCapacity, writeBufferMaxCapacity);
            case HEAP:
                return DEFAULT.heapBuffer(writeBufferInitialCapacity, writeBufferMaxCapacity);
            case IO:
                return DEFAULT.ioBuffer(writeBufferInitialCapacity, writeBufferMaxCapacity);
            case DIRECT:
                return DEFAULT.directBuffer(writeBufferInitialCapacity, writeBufferMaxCapacity);
        }
        return DEFAULT.ioBuffer();
    }
}
