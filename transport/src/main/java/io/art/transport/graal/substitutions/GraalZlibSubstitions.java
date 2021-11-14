package io.art.transport.graal.substitutions;

import com.oracle.svm.core.annotate.*;
import io.netty.handler.codec.compression.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;

@SuppressWarnings(UNUSED)
@TargetClass(value = ZlibCodecFactory.class)
final class TargetNettyZlibCodecFactory {
    @Substitute
    public static ZlibEncoder newZlibEncoder(int compressionLevel) {
        return new JdkZlibEncoder(compressionLevel);
    }

    @Substitute
    public static ZlibEncoder newZlibEncoder(ZlibWrapper wrapper) {
        return new JdkZlibEncoder(wrapper);
    }

    @Substitute
    public static ZlibEncoder newZlibEncoder(ZlibWrapper wrapper, int compressionLevel) {
        return new JdkZlibEncoder(wrapper, compressionLevel);
    }

    @Substitute
    public static ZlibEncoder newZlibEncoder(ZlibWrapper wrapper, int compressionLevel, int windowBits, int memLevel) {
        return new JdkZlibEncoder(wrapper, compressionLevel);
    }

    @Substitute
    public static ZlibEncoder newZlibEncoder(byte[] dictionary) {
        return new JdkZlibEncoder(dictionary);
    }

    @Substitute
    public static ZlibEncoder newZlibEncoder(int compressionLevel, byte[] dictionary) {
        return new JdkZlibEncoder(compressionLevel, dictionary);
    }

    @Substitute
    public static ZlibEncoder newZlibEncoder(int compressionLevel, int windowBits, int memLevel, byte[] dictionary) {
        return new JdkZlibEncoder(compressionLevel, dictionary);
    }

    @Substitute
    public static ZlibDecoder newZlibDecoder() {
        return new JdkZlibDecoder();
    }

    @Substitute
    public static ZlibDecoder newZlibDecoder(ZlibWrapper wrapper) {
        return new JdkZlibDecoder(wrapper);
    }

    @Substitute
    public static ZlibDecoder newZlibDecoder(byte[] dictionary) {
        return new JdkZlibDecoder(dictionary);
    }
}

@SuppressWarnings(UNUSED)
class GraalZlibSubstitutions {

}
