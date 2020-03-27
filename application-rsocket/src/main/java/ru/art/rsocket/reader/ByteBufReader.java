package ru.art.rsocket.reader;

import io.netty.buffer.*;
import lombok.experimental.*;
import ru.art.core.context.*;
import static ru.art.core.context.Context.contextConfiguration;

@UtilityClass
public class ByteBufReader {
    public static byte[] readByteBufToArray(ByteBuf byteBuf) {
        if (byteBuf.readableBytes() == 0) {
            return null;
        }
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return bytes;
    }

    public static String readByteBufToString(ByteBuf byteBuf) {
        if (byteBuf.readableBytes() == 0) {
            return null;
        }
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return new String(bytes, contextConfiguration().getCharset());
    }
}
