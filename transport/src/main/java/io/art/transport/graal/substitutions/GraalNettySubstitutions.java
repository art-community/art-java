package io.art.transport.graal.substitutions;

import com.oracle.svm.core.annotate.*;
import com.oracle.svm.core.jdk.*;
import io.netty.bootstrap.ChannelFactory;
import io.netty.bootstrap.*;
import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.channel.embedded.*;
import io.netty.handler.codec.compression.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http2.*;
import io.netty.handler.ssl.*;
import io.netty.handler.ssl.ApplicationProtocolConfig.*;
import io.netty.util.concurrent.*;
import io.netty.util.internal.*;
import io.netty.util.internal.logging.*;
import static com.oracle.svm.core.annotate.RecomputeFieldValue.Kind.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.netty.handler.codec.compression.ZlibWrapper.*;
import static io.netty.handler.codec.http.HttpHeaderValues.GZIP;
import static io.netty.handler.codec.http.HttpHeaderValues.*;
import static io.netty.handler.ssl.ApplicationProtocolConfig.SelectorFailureBehavior.*;
import static java.util.Collections.*;
import javax.net.ssl.*;
import java.net.*;
import java.nio.*;
import java.security.*;
import java.security.cert.*;
import java.util.*;
import java.util.concurrent.*;

@TargetClass(className = "io.netty.util.internal.logging.InternalLoggerFactory")
final class Target_io_netty_util_internal_logging_InternalLoggerFactory {

    @Substitute
    private static InternalLoggerFactory newDefaultFactory(String name) {
        return JdkLoggerFactory.INSTANCE;
    }
}

@TargetClass(className = "io.netty.handler.ssl.SslProvider")
final class Target_io_netty_handler_ssl_SslProvider {
    @Substitute
    public static boolean isAlpnSupported(final SslProvider provider) {
        switch (provider) {
            case JDK:
                return Target_io_netty_handler_ssl_JdkAlpnApplicationProtocolNegotiator.isAlpnSupported();
            case OPENSSL:
            case OPENSSL_REFCNT:
                return false;
            default:
                throw new Error("SslProvider unsupported on Quarkus " + provider);
        }
    }
}

@TargetClass(className = "io.netty.handler.ssl.JdkAlpnApplicationProtocolNegotiator")
final class Target_io_netty_handler_ssl_JdkAlpnApplicationProtocolNegotiator {
    @Alias
    static boolean isAlpnSupported() {
        return true;
    }
}

@SuppressWarnings(ALL)
@TargetClass(className = "io.netty.handler.ssl.OpenSsl")
final class Target_io_netty_handler_ssl_OpenSsl {

    @Alias
    @RecomputeFieldValue(kind = FromAlias)
    private static Throwable UNAVAILABILITY_CAUSE = new RuntimeException("OpenSsl unsupported on Quarkus");

    @Alias
    @RecomputeFieldValue(kind = FromAlias)
    static List<String> DEFAULT_CIPHERS = emptyList();

    @Alias
    @RecomputeFieldValue(kind = FromAlias)
    static Set<String> AVAILABLE_CIPHER_SUITES = emptySet();

    @Alias
    @RecomputeFieldValue(kind = FromAlias)
    private static Set<String> AVAILABLE_OPENSSL_CIPHER_SUITES = emptySet();

    @Alias
    @RecomputeFieldValue(kind = FromAlias)
    private static Set<String> AVAILABLE_JAVA_CIPHER_SUITES = emptySet();

    @Alias
    @RecomputeFieldValue(kind = FromAlias)
    private static boolean SUPPORTS_KEYMANAGER_FACTORY = false;

    @Alias
    @RecomputeFieldValue(kind = FromAlias)
    private static boolean SUPPORTS_OCSP = false;

    @Alias
    @RecomputeFieldValue(kind = FromAlias)
    static Set<String> SUPPORTED_PROTOCOLS_SET = emptySet();

    @Substitute
    public static boolean isAvailable() {
        return false;
    }

    @Substitute
    public static int version() {
        return -1;
    }

    @Substitute
    public static String versionString() {
        return null;
    }

    @Substitute
    public static boolean isCipherSuiteAvailable(String cipherSuite) {
        return false;
    }
}

@TargetClass(className = "io.netty.handler.ssl.JdkSslServerContext")
final class Target_io_netty_handler_ssl_JdkSslServerContext {

    @Alias
    Target_io_netty_handler_ssl_JdkSslServerContext(Provider provider,
                                                    X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory,
                                                    X509Certificate[] keyCertChain, PrivateKey key, String keyPassword,
                                                    KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter,
                                                    ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout,
                                                    ClientAuth clientAuth, String[] protocols, boolean startTls,
                                                    String keyStore)
            throws SSLException {
    }
}

@TargetClass(className = "io.netty.handler.ssl.JdkSslClientContext")
final class Target_io_netty_handler_ssl_JdkSslClientContext {

    @Alias
    Target_io_netty_handler_ssl_JdkSslClientContext(Provider sslContextProvider, X509Certificate[] trustCertCollection,
                                                    TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key,
                                                    String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers,
                                                    CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, String[] protocols,
                                                    long sessionCacheSize, long sessionTimeout, String keyStoreType)
            throws SSLException {

    }
}

@TargetClass(className = "io.netty.handler.ssl.SslHandler$SslEngineType")
final class Target_io_netty_handler_ssl_SslHandler$SslEngineType {

    @Alias
    public static Target_io_netty_handler_ssl_SslHandler$SslEngineType JDK;

    @Substitute
    static Target_io_netty_handler_ssl_SslHandler$SslEngineType forEngine(SSLEngine engine) {
        return JDK;
    }
}

@SuppressWarnings(ALL)
@TargetClass(className = "io.netty.handler.ssl.JdkAlpnApplicationProtocolNegotiator$AlpnWrapper", onlyWith = JDK11OrLater.class)
final class Target_io_netty_handler_ssl_JdkAlpnApplicationProtocolNegotiator_AlpnWrapper {
    @Substitute
    public SSLEngine wrapSslEngine(SSLEngine engine, ByteBufAllocator alloc,
                                   JdkApplicationProtocolNegotiator applicationNegotiator, boolean isServer) {
        return (SSLEngine) (Object) new Target_io_netty_handler_ssl_JdkAlpnSslEngine(engine, applicationNegotiator,
                isServer);
    }

}

@SuppressWarnings(ALL)
@TargetClass(className = "io.netty.handler.ssl.JdkAlpnApplicationProtocolNegotiator$AlpnWrapper", onlyWith = JDK8OrEarlier.class)
final class Target_io_netty_handler_ssl_JdkAlpnApplicationProtocolNegotiator_AlpnWrapperJava8 {
    @Substitute
    public SSLEngine wrapSslEngine(SSLEngine engine, ByteBufAllocator alloc,
                                   JdkApplicationProtocolNegotiator applicationNegotiator, boolean isServer) {
        if (Target_io_netty_handler_ssl_JettyAlpnSslEngine.isAvailable()) {
            return isServer
                    ? (SSLEngine) (Object) Target_io_netty_handler_ssl_JettyAlpnSslEngine.newServerEngine(engine,
                    applicationNegotiator)
                    : (SSLEngine) (Object) Target_io_netty_handler_ssl_JettyAlpnSslEngine.newClientEngine(engine,
                    applicationNegotiator);
        }
        throw new RuntimeException("Unable to wrap SSLEngine of type " + engine.getClass().getName());
    }

}

@TargetClass(className = "io.netty.handler.ssl.JettyAlpnSslEngine", onlyWith = JDK8OrEarlier.class)
final class Target_io_netty_handler_ssl_JettyAlpnSslEngine {
    @Substitute
    static boolean isAvailable() {
        return false;
    }

    @Substitute
    static Target_io_netty_handler_ssl_JettyAlpnSslEngine newClientEngine(SSLEngine engine,
                                                                          JdkApplicationProtocolNegotiator applicationNegotiator) {
        return null;
    }

    @Substitute
    static Target_io_netty_handler_ssl_JettyAlpnSslEngine newServerEngine(SSLEngine engine,
                                                                          JdkApplicationProtocolNegotiator applicationNegotiator) {
        return null;
    }
}

@TargetClass(className = "io.netty.handler.ssl.JdkAlpnSslEngine", onlyWith = JDK11OrLater.class)
final class Target_io_netty_handler_ssl_JdkAlpnSslEngine {
    @Alias
    Target_io_netty_handler_ssl_JdkAlpnSslEngine(final SSLEngine engine,
                                                 final JdkApplicationProtocolNegotiator applicationNegotiator, final boolean isServer) {

    }
}

@SuppressWarnings(ALL)
@TargetClass(className = "io.netty.handler.ssl.SslContext")
final class Target_io_netty_handler_ssl_SslContext {

    @Substitute
    static SslContext newServerContextInternal(SslProvider provider, Provider sslContextProvider,
                                               X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory,
                                               X509Certificate[] keyCertChain,
                                               PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers,
                                               CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout,
                                               ClientAuth clientAuth, String[] protocols, boolean startTls, boolean enableOcsp, String keyStoreType,
                                               Map.Entry<SslContextOption<?>, Object>... ctxOptions) throws SSLException {
        if (enableOcsp) {
            throw new IllegalArgumentException("OCSP is not supported with this SslProvider: " + provider);
        }
        return (SslContext) (Object) new Target_io_netty_handler_ssl_JdkSslServerContext(sslContextProvider,
                trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword,
                keyManagerFactory, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout,
                clientAuth, protocols, startTls, keyStoreType);
    }

    @Substitute
    static SslContext newClientContextInternal(SslProvider provider, Provider sslContextProvider,
                                               X509Certificate[] trustCert,
                                               TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword,
                                               KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter,
                                               ApplicationProtocolConfig apn, String[] protocols, long sessionCacheSize, long sessionTimeout,
                                               boolean enableOcsp,
                                               String keyStoreType, Map.Entry<SslContextOption<?>, Object>... options) throws SSLException {
        if (enableOcsp) {
            throw new IllegalArgumentException("OCSP is not supported with this SslProvider: " + provider);
        }
        return (SslContext) (Object) new Target_io_netty_handler_ssl_JdkSslClientContext(sslContextProvider,
                trustCert, trustManagerFactory, keyCertChain, key, keyPassword,
                keyManagerFactory, ciphers, cipherFilter, apn, protocols, sessionCacheSize,
                sessionTimeout, keyStoreType);
    }

}

@TargetClass(className = "io.netty.handler.ssl.JdkDefaultApplicationProtocolNegotiator")
final class Target_io_netty_handler_ssl_JdkDefaultApplicationProtocolNegotiator {

    @Alias
    public static Target_io_netty_handler_ssl_JdkDefaultApplicationProtocolNegotiator INSTANCE;
}

@SuppressWarnings(ALL)
@TargetClass(className = "io.netty.handler.ssl.JdkSslContext")
final class Target_io_netty_handler_ssl_JdkSslContext {

    @Substitute
    static JdkApplicationProtocolNegotiator toNegotiator(ApplicationProtocolConfig config, boolean isServer) {
        if (config == null) {
            return (JdkApplicationProtocolNegotiator) (Object) Target_io_netty_handler_ssl_JdkDefaultApplicationProtocolNegotiator.INSTANCE;
        }

        switch (config.protocol()) {
            case NONE:
                return (JdkApplicationProtocolNegotiator) (Object) Target_io_netty_handler_ssl_JdkDefaultApplicationProtocolNegotiator.INSTANCE;
            case ALPN:
                if (isServer) {
                    SelectorFailureBehavior behavior = config.selectorFailureBehavior();
                    if (behavior == FATAL_ALERT) {
                        return new JdkAlpnApplicationProtocolNegotiator(true, config.supportedProtocols());
                    }
                    if (behavior == SelectorFailureBehavior.NO_ADVERTISE) {
                        return new JdkAlpnApplicationProtocolNegotiator(false, config.supportedProtocols());
                    }
                    throw new UnsupportedOperationException(new StringBuilder("JDK provider does not support ")
                            .append(config.selectorFailureBehavior())
                            .append(" failure behavior")
                            .toString());
                } else {
                    switch (config.selectedListenerFailureBehavior()) {
                        case ACCEPT:
                            return new JdkAlpnApplicationProtocolNegotiator(false, config.supportedProtocols());
                        case FATAL_ALERT:
                            return new JdkAlpnApplicationProtocolNegotiator(true, config.supportedProtocols());
                        default:
                            throw new UnsupportedOperationException(new StringBuilder("JDK provider does not support ")
                                    .append(config.selectedListenerFailureBehavior()).append(" failure behavior")
                                    .toString());
                    }
                }
            default:
                throw new UnsupportedOperationException(
                        new StringBuilder("JDK provider does not support ").append(config.protocol())
                                .append(" protocol")
                                .toString());
        }
    }

}

@SuppressWarnings(ALL)
@TargetClass(className = "io.netty.bootstrap.AbstractBootstrap")
final class Target_io_netty_bootstrap_AbstractBootstrap {

    @Alias
    private ChannelFactory channelFactory;

    @Alias
    void init(Channel channel) throws Exception {
    }

    @Alias
    public AbstractBootstrap config() {
        return null;
    }

    @Substitute
    final ChannelFuture initAndRegister() {
        Channel channel = null;
        try {
            channel = channelFactory.newChannel();
            init(channel);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            if (channel != null) {
                channel.unsafe().closeForcibly();
            }
            return new DefaultChannelPromise(channel, GlobalEventExecutor.INSTANCE).setFailure(throwable);
        }

        ChannelFuture regFuture = config().group().register(channel);
        if (regFuture.cause() != null) {
            if (channel.isRegistered()) {
                channel.close();
            } else {
                channel.unsafe().closeForcibly();
            }
        }

        return regFuture;

    }
}

@TargetClass(className = "io.netty.channel.nio.NioEventLoop")
final class Target_io_netty_channel_nio_NioEventLoop {

    @Substitute
    private static Queue<Runnable> newTaskQueue0(int maxPendingTasks) {
        return new LinkedBlockingDeque<>();
    }
}

@TargetClass(className = "io.netty.buffer.AbstractReferenceCountedByteBuf")
final class Target_io_netty_buffer_AbstractReferenceCountedByteBuf {

    @Alias
    @RecomputeFieldValue(kind = FieldOffset, name = "refCnt")
    private static long REFCNT_FIELD_OFFSET;
}

@TargetClass(className = "io.netty.util.AbstractReferenceCounted")
final class Target_io_netty_util_AbstractReferenceCounted {

    @Alias
    @RecomputeFieldValue(kind = FieldOffset, name = "refCnt")
    private static long REFCNT_FIELD_OFFSET;
}

// This class is runtime-initialized by NettyProcessor
final class Holder_io_netty_util_concurrent_ScheduledFutureTask {
    static final long START_TIME = System.nanoTime();
}

@TargetClass(className = "io.netty.util.concurrent.ScheduledFutureTask")
final class Target_io_netty_util_concurrent_ScheduledFutureTask {

    // The START_TIME field is kept but not used.
    // All the accesses to it have been replaced with Holder_io_netty_util_concurrent_ScheduledFutureTask

    @Substitute
    static long initialNanoTime() {
        return Holder_io_netty_util_concurrent_ScheduledFutureTask.START_TIME;
    }

    @Substitute
    static long nanoTime() {
        return System.nanoTime() - Holder_io_netty_util_concurrent_ScheduledFutureTask.START_TIME;
    }

    @Alias
    public long deadlineNanos() {
        return 0;
    }

    @Substitute
    public long delayNanos(long currentTimeNanos) {
        return Math.max(0,
                deadlineNanos() - (currentTimeNanos - Holder_io_netty_util_concurrent_ScheduledFutureTask.START_TIME));
    }
}

@SuppressWarnings(ALL)
@TargetClass(className = "io.netty.channel.ChannelHandlerMask")
final class Target_io_netty_channel_ChannelHandlerMask {
    @Substitute
    private static boolean isSkippable(final Class<?> handlerType, final String methodName, final Class... paramTypes) {
        return false;
    }
}

@TargetClass(className = "io.netty.buffer.EmptyByteBuf")
final class Target_io_netty_buffer_EmptyByteBuf {
    public static final class EmptyByteBufStub {
        private static final ByteBuffer EMPTY_BYTE_BUFFER = ByteBuffer.allocateDirect(0);
        private static final long EMPTY_BYTE_BUFFER_ADDRESS;

        static {
            long emptyByteBufferAddress = 0;
            try {
                if (PlatformDependent.hasUnsafe()) {
                    emptyByteBufferAddress = PlatformDependent.directBufferAddress(EMPTY_BYTE_BUFFER);
                }
            } catch (Throwable t) {
                // Ignore
            }
            EMPTY_BYTE_BUFFER_ADDRESS = emptyByteBufferAddress;
        }

        public static ByteBuffer emptyByteBuffer() {
            return EMPTY_BYTE_BUFFER;
        }

        public static long emptyByteBufferAddress() {
            return EMPTY_BYTE_BUFFER_ADDRESS;
        }

        private EmptyByteBufStub() {
        }
    }


    @Alias
    @RecomputeFieldValue(kind = Reset)
    private static ByteBuffer EMPTY_BYTE_BUFFER;

    @Alias
    @RecomputeFieldValue(kind = Reset)
    private static long EMPTY_BYTE_BUFFER_ADDRESS;

    @Substitute
    public ByteBuffer nioBuffer() {
        return EmptyByteBufStub.emptyByteBuffer();
    }

    @Substitute
    public ByteBuffer[] nioBuffers() {
        return new ByteBuffer[]{EmptyByteBufStub.emptyByteBuffer()};
    }

    @Substitute
    public ByteBuffer internalNioBuffer(int index, int length) {
        return EmptyByteBufStub.emptyByteBuffer();
    }

    @Substitute
    public boolean hasMemoryAddress() {
        return EmptyByteBufStub.emptyByteBufferAddress() != 0;
    }

    @Substitute
    public long memoryAddress() {
        if (hasMemoryAddress()) {
            return EmptyByteBufStub.emptyByteBufferAddress();
        } else {
            throw new UnsupportedOperationException();
        }
    }

}

@TargetClass(className = "io.netty.handler.codec.http.HttpContentDecompressor")
final class Target_io_netty_handler_codec_http_HttpContentDecompressor {

    @Alias
    private boolean strict;

    @Alias
    protected ChannelHandlerContext ctx;

    @Substitute
    protected EmbeddedChannel newContentDecoder(String contentEncoding) throws Exception {
        if (GZIP.contentEqualsIgnoreCase(contentEncoding) ||
                X_GZIP.contentEqualsIgnoreCase(contentEncoding)) {
            return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(),
                    ctx.channel().config(), ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));
        }
        if (DEFLATE.contentEqualsIgnoreCase(contentEncoding) ||
                X_DEFLATE.contentEqualsIgnoreCase(contentEncoding)) {
            final ZlibWrapper wrapper = strict ? ZLIB : ZLIB_OR_NONE;
            // To be strict, 'deflate' means ZLIB, but some servers were not implemented correctly.
            return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(),
                    ctx.channel().config(), ZlibCodecFactory.newZlibDecoder(wrapper));
        }

        // 'identity' or unsupported
        return null;
    }
}

@SuppressWarnings(ALL)
@TargetClass(className = "io.netty.handler.codec.http2.DelegatingDecompressorFrameListener")
final class Target_io_netty_handler_codec_http2_DelegatingDecompressorFrameListener {

    @Alias
    boolean strict;

    @Substitute
    protected EmbeddedChannel newContentDecompressor(ChannelHandlerContext ctx, CharSequence contentEncoding)
            throws Http2Exception {
        if (!GZIP.contentEqualsIgnoreCase(contentEncoding)
                && !HttpHeaderValues.X_GZIP.contentEqualsIgnoreCase(contentEncoding)) {
            if (!HttpHeaderValues.DEFLATE.contentEqualsIgnoreCase(contentEncoding)
                    && !HttpHeaderValues.X_DEFLATE.contentEqualsIgnoreCase(contentEncoding)) {
                return null;
            } else {
                ZlibWrapper wrapper = this.strict ? ZLIB : ZLIB_OR_NONE;
                return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(), ctx.channel().config(),
                        new ChannelHandler[]{ZlibCodecFactory.newZlibDecoder(wrapper)});
            }
        } else {
            return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(), ctx.channel().config(),
                    new ChannelHandler[]{ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP)});
        }
    }
}

@TargetClass(className = "io.netty.resolver.dns.DirContextUtils")
final class DirContextUtils {
    @Substitute
    static void addNameServers(List<InetSocketAddress> defaultNameServers, int defaultPort) {
        defaultNameServers.add(new InetSocketAddress("8.8.8.8", defaultPort));
        defaultNameServers.add(new InetSocketAddress("4.4.4.4", defaultPort));
    }
}

class GraalNettySubstitutions {
}
