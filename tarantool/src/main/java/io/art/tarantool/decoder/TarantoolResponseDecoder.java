package io.art.tarantool.decoder;

import io.art.core.exception.*;
import io.art.tarantool.model.*;
import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.handler.codec.*;
import static io.art.tarantool.decoder.TarantoolResponseDecoder.State.*;
import static io.art.tarantool.descriptor.TarantoolResponseReader.*;
import java.util.*;

public class TarantoolResponseDecoder extends ReplayingDecoder<TarantoolResponseDecoder.State> {
    private int size;

    public TarantoolResponseDecoder() {
        super(LENGTH);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf bytes, List<Object> list) throws Exception {
        switch (state()) {
            case LENGTH:
                size = readTarantoolResponseSize(bytes);
                checkpoint(CONTENT);
            case CONTENT:
                if (size > 0) {
                    if (bytes.readableBytes() < size) return;
                    TarantoolResponse tarantoolResponse = readTarantoolResponseContent(bytes.readBytes(size));
                    list.add(tarantoolResponse);
                    size = 0;
                }
                checkpoint(LENGTH);
                break;
            default:
                throw new ImpossibleSituationException();
        }
    }

    protected enum State {
        LENGTH,
        CONTENT
    }
}
