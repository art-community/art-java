package io.art.transport.graal.substitutions;

import com.oracle.svm.core.annotate.*;
import com.oracle.svm.core.jdk.*;
import io.art.core.exception.*;
import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.channel.embedded.*;
import io.netty.channel.nio.*;
import io.netty.handler.codec.compression.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http2.*;
import io.netty.handler.ssl.*;
import io.netty.handler.ssl.ApplicationProtocolConfig.*;
import io.netty.util.*;
import io.netty.util.internal.*;
import io.netty.util.internal.logging.*;
import static com.oracle.svm.core.annotate.RecomputeFieldValue.Kind.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.transport.constants.TransportModuleConstants.GraalConstants.*;
import static io.netty.handler.codec.compression.ZlibCodecFactory.*;
import static io.netty.handler.codec.compression.ZlibWrapper.*;
import static io.netty.handler.codec.http.HttpHeaderValues.GZIP;
import static io.netty.handler.codec.http.HttpHeaderValues.*;
import static io.netty.handler.ssl.ApplicationProtocolConfig.SelectorFailureBehavior.*;
import static java.text.MessageFormat.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import javax.net.ssl.*;
import java.net.*;
import java.nio.*;
import java.security.*;
import java.security.cert.*;
import java.util.*;
import java.util.concurrent.*;

@SuppressWarnings(UNUSED)
@TargetClass(value = InternalLoggerFactory.class)
final class TargetNettyInternalLoggerFactory {

    @Substitute
    private static InternalLoggerFactory newDefaultFactory(String name) {
        return JdkLoggerFactory.INSTANCE;
    }
}

@SuppressWarnings(UNUSED)
@TargetClass(value = SslProvider.class)
final class TargetNettySslProvider {
    @Substitute
    public static boolean isAlpnSupported(final SslProvider provider) {
        switch (provider) {
            case JDK:
                return TargetNettySslJdkAlpnApplicationProtocolNegotiator.isAlpnSupported();
            case OPENSSL:
            case OPENSSL_REFCNT:
                return false;
            default:
                throw new UnsupportedOperationException(format(NETTY_SSL_PROVIDER_UNSUPPORTED_EXCEPTION, provider));
        }
    }
}

@SuppressWarnings(DEPRECATION)
@TargetClass(value = JdkAlpnApplicationProtocolNegotiator.class)
final class TargetNettySslJdkAlpnApplicationProtocolNegotiator {
    @Alias
    static boolean isAlpnSupported() {
        return true;
    }
}

@SuppressWarnings({UNUSED, DEPRECATION})
@TargetClass(className = NETTY_JDK_ALPN_PROTOCOL_NEGOTIATOR_ALPN_WRAPPER_CLASS, onlyWith = JDK11OrLater.class)
final class TargetNettyJdkAlpnApplicationProtocolNegotiatorAlpnWrapperJava11 {
    @Substitute
    public SSLEngine wrapSslEngine(SSLEngine engine, ByteBufAllocator alloc,
                                   JdkApplicationProtocolNegotiator applicationNegotiator, boolean isServer) {
        return cast(new TargetNettyJdkAlpnSslEngine(engine, applicationNegotiator, isServer));
    }

}

@SuppressWarnings({UNUSED, DEPRECATION})
@TargetClass(className = NETTY_JDK_ALPN_PROTOCOL_NEGOTIATOR_ALPN_WRAPPER_CLASS, onlyWith = JDK8OrEarlier.class)
final class TargetNettyJdkAlpnApplicationProtocolNegotiator_AlpnWrapperJava8 {
    @Substitute
    public SSLEngine wrapSslEngine(SSLEngine engine, ByteBufAllocator alloc,
                                   JdkApplicationProtocolNegotiator applicationNegotiator, boolean isServer) {
        if (TargetNettyJettyAlpnSslEngine.isAvailable()) {
            return isServer
                    ? cast(TargetNettyJettyAlpnSslEngine.newServerEngine(engine, applicationNegotiator))
                    : cast(TargetNettyJettyAlpnSslEngine.newClientEngine(engine, applicationNegotiator));
        }
        throw new InternalRuntimeException(format(NETTY_UNABLE_TO_WRAP_SSL_ENGINE_EXCEPTION, engine.getClass().getName()));
    }

}

@SuppressWarnings({UNUSED, DEPRECATION})
@TargetClass(className = NETTY_SSL_JETTY_ALPN_SSL_ENGINE_CLASS, onlyWith = JDK8OrEarlier.class)
final class TargetNettyJettyAlpnSslEngine {
    @Substitute
    static boolean isAvailable() {
        return false;
    }

    @Substitute
    static TargetNettyJettyAlpnSslEngine newClientEngine(SSLEngine engine,
                                                         JdkApplicationProtocolNegotiator applicationNegotiator) {
        return null;
    }

    @Substitute
    static TargetNettyJettyAlpnSslEngine newServerEngine(SSLEngine engine,
                                                         JdkApplicationProtocolNegotiator applicationNegotiator) {
        return null;
    }
}

@SuppressWarnings({UNUSED, DEPRECATION})
@TargetClass(className = NETTY_SSL_JDK_ALPN_SSL_ENGINE_CLASS, onlyWith = JDK11OrLater.class)
final class TargetNettyJdkAlpnSslEngine {
    @Alias
    TargetNettyJdkAlpnSslEngine(final SSLEngine engine,
                                final JdkApplicationProtocolNegotiator applicationNegotiator, final boolean isServer) {

    }
}

@SuppressWarnings({UNUSED, FINAL_FIELD})
@TargetClass(value = OpenSsl.class)
final class TargetNettyOpenSsl {

    @Alias
    @RecomputeFieldValue(kind = FromAlias)
    private static Throwable UNAVAILABILITY_CAUSE = new IllegalArgumentException(NETTY_OPEN_SSL_UNSUPPORTED_EXCEPTION);

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

@SuppressWarnings(UNUSED)
final class TargetNettyJdkSslServerContext {

    @Alias
    TargetNettyJdkSslServerContext(Provider provider,
                                   X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory,
                                   X509Certificate[] keyCertChain, PrivateKey key, String keyPassword,
                                   KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter,
                                   ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout,
                                   ClientAuth clientAuth, String[] protocols, boolean startTls,
                                   String keyStore) throws SSLException {
    }
}

@SuppressWarnings({UNUSED, DEPRECATION})
@TargetClass(value = JdkSslClientContext.class)
final class TargetNettyJdkSslClientContext {

    @Alias
    TargetNettyJdkSslClientContext(Provider sslContextProvider, X509Certificate[] trustCertCollection,
                                   TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key,
                                   String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers,
                                   CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, String[] protocols,
                                   long sessionCacheSize, long sessionTimeout, String keyStoreType) throws SSLException {
    }
}

@SuppressWarnings(UNUSED)
@TargetClass(className = NETTY_SSL_ENGINE_TYPE_CLASS)
final class TargetNettySslEngineType {

    @Alias
    public static TargetNettySslEngineType JDK;

    @Substitute
    static TargetNettySslEngineType forEngine(SSLEngine engine) {
        return JDK;
    }
}

@SuppressWarnings(UNUSED)
@TargetClass(value = SslContext.class)
final class TargetNettySslContext {

    @Substitute
    @SafeVarargs
    static SslContext newServerContextInternal(SslProvider provider, Provider sslContextProvider,
                                               X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory,
                                               X509Certificate[] keyCertChain,
                                               PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers,
                                               CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout,
                                               ClientAuth clientAuth, String[] protocols, boolean startTls, boolean enableOcsp, String keyStoreType,
                                               Map.Entry<SslContextOption<?>, Object>... ctxOptions) throws SSLException {
        if (enableOcsp) {
            throw new IllegalArgumentException(format(NETTY_OSCP_EXCEPTION, provider));
        }

        return cast(new TargetNettyJdkSslServerContext(sslContextProvider,
                trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword,
                keyManagerFactory, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout,
                clientAuth, protocols, startTls, keyStoreType));
    }

    @Substitute
    @SafeVarargs
    static SslContext newClientContextInternal(SslProvider provider, Provider sslContextProvider,
                                               X509Certificate[] trustCert,
                                               TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword,
                                               KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter,
                                               ApplicationProtocolConfig apn, String[] protocols, long sessionCacheSize, long sessionTimeout,
                                               boolean enableOcsp,
                                               String keyStoreType, Map.Entry<SslContextOption<?>, Object>... options) throws SSLException {
        if (enableOcsp) {
            throw new IllegalArgumentException(format(NETTY_OSCP_EXCEPTION, provider));
        }

        return cast(new TargetNettyJdkSslClientContext(sslContextProvider,
                trustCert, trustManagerFactory, keyCertChain, key, keyPassword,
                keyManagerFactory, ciphers, cipherFilter, apn, protocols, sessionCacheSize,
                sessionTimeout, keyStoreType));
    }

}

@SuppressWarnings(UNUSED)
@TargetClass(className = NETTY_JDK_DEFAULT_APPLICATION_PROTOCOL_NEGOTIATOR_CLASS)
final class TargetNettyJdkDefaultApplicationProtocolNegotiator {

    @Alias
    public static TargetNettyJdkDefaultApplicationProtocolNegotiator INSTANCE;
}

@SuppressWarnings({UNUSED, DEPRECATION})
@TargetClass(value = JdkSslContext.class)
final class TargetNettySslJdkSslContext {

    @Substitute
    static JdkApplicationProtocolNegotiator toNegotiator(ApplicationProtocolConfig config, boolean isServer) {
        if (isNull(config)) {
            return cast(TargetNettyJdkDefaultApplicationProtocolNegotiator.INSTANCE);
        }

        switch (config.protocol()) {
            case NONE:
                return cast(TargetNettyJdkDefaultApplicationProtocolNegotiator.INSTANCE);
            case ALPN:
                if (isServer) {
                    SelectorFailureBehavior behavior = config.selectorFailureBehavior();
                    if (behavior == FATAL_ALERT) {
                        return new JdkAlpnApplicationProtocolNegotiator(true, config.supportedProtocols());
                    }
                    if (behavior == SelectorFailureBehavior.NO_ADVERTISE) {
                        return new JdkAlpnApplicationProtocolNegotiator(false, config.supportedProtocols());
                    }
                    String message = NETTY_JDK_SSL_PROVIDER_EXCEPTION
                            + config.selectorFailureBehavior()
                            + NETTY_JDK_SSL_FAILURE_BEHAVIOR_EXCEPTION;
                    throw new UnsupportedOperationException(message);
                }
                switch (config.selectedListenerFailureBehavior()) {
                    case ACCEPT:
                        return new JdkAlpnApplicationProtocolNegotiator(false, config.supportedProtocols());
                    case FATAL_ALERT:
                        return new JdkAlpnApplicationProtocolNegotiator(true, config.supportedProtocols());
                    default:
                        String message = NETTY_JDK_SSL_PROVIDER_EXCEPTION
                                + config.selectedListenerFailureBehavior()
                                + NETTY_JDK_SSL_FAILURE_BEHAVIOR_EXCEPTION;
                        throw new UnsupportedOperationException(message);
                }
            default:
                String message = NETTY_JDK_SSL_PROVIDER_EXCEPTION
                        + config.protocol()
                        + NETTY_JDK_SSL_PROTOCOL_EXCEPTION;
                throw new UnsupportedOperationException(message);
        }
    }

}

@SuppressWarnings(UNUSED)
@TargetClass(value = NioEventLoop.class)
final class TargetNettyNioEventLoop {

    @Substitute
    private static Queue<Runnable> newTaskQueue0(int maxPendingTasks) {
        return new LinkedBlockingDeque<>();
    }
}

@SuppressWarnings(UNUSED)
@TargetClass(value = AbstractReferenceCountedByteBuf.class)
final class TargetNettyAbstractReferenceCountedByteBuf {

    @Alias
    @RecomputeFieldValue(kind = FieldOffset, name = NETTY_REF_CNT_NAME)
    private static long REFCNT_FIELD_OFFSET;
}

@SuppressWarnings(UNUSED)
@TargetClass(value = AbstractReferenceCounted.class)
final class TargetNettyAbstractReferenceCounted {

    @Alias
    @RecomputeFieldValue(kind = FieldOffset, name = NETTY_REF_CNT_NAME)
    private static long REFCNT_FIELD_OFFSET;
}

final class NettySchedulerFutureTaskHolder {
    static final long START_TIME = System.nanoTime();
}

@SuppressWarnings(UNUSED)
@TargetClass(className = NETTY_SCHEDULER_FUTURE_TASK_CLASS)
final class TargetNettySchedulerFutureTask {
    @Substitute
    static long initialNanoTime() {
        return NettySchedulerFutureTaskHolder.START_TIME;
    }

    @Substitute
    static long nanoTime() {
        return System.nanoTime() - NettySchedulerFutureTaskHolder.START_TIME;
    }

    @Alias
    public long deadlineNanos() {
        return 0;
    }

    @Substitute
    public long delayNanos(long currentTimeNanos) {
        return Math.max(0, deadlineNanos() - (currentTimeNanos - NettySchedulerFutureTaskHolder.START_TIME));
    }
}

@SuppressWarnings(UNUSED)
@TargetClass(className = NETTY_CHANNEL_HANDLER_MASK_CLASS)
final class TargetNettyChannelHandlerMask {
    @Substitute
    private static boolean isSkippable(final Class<?> handlerType, final String methodName, final Class<?>... paramTypes) {
        return false;
    }
}

@SuppressWarnings(UNUSED)
@TargetClass(value = EmptyByteBuf.class)
final class TargetNettyEmptyByteBuf {
    public static final class EmptyByteBufStub {
        private static final ByteBuffer EMPTY_BYTE_BUFFER = ByteBuffer.allocateDirect(0);
        private static final long EMPTY_BYTE_BUFFER_ADDRESS;

        static {
            long emptyByteBufferAddress = 0;
            try {
                if (PlatformDependent.hasUnsafe()) {
                    emptyByteBufferAddress = PlatformDependent.directBufferAddress(EMPTY_BYTE_BUFFER);
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
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
        }
        throw new UnsupportedOperationException();
    }

}

@SuppressWarnings(UNUSED)
@TargetClass(value = HttpContentDecompressor.class)
final class TargetNettyHttpContentDecompressor {

    @Alias
    private boolean strict;

    @Alias
    protected ChannelHandlerContext ctx;

    @Substitute
    protected EmbeddedChannel newContentDecoder(String contentEncoding) throws Exception {
        boolean hasDisconnect = ctx.channel().metadata().hasDisconnect();
        if (GZIP.contentEqualsIgnoreCase(contentEncoding) || X_GZIP.contentEqualsIgnoreCase(contentEncoding)) {
            return new EmbeddedChannel(ctx.channel().id(), hasDisconnect, ctx.channel().config(), newZlibDecoder(ZlibWrapper.GZIP));
        }
        if (DEFLATE.contentEqualsIgnoreCase(contentEncoding) || X_DEFLATE.contentEqualsIgnoreCase(contentEncoding)) {
            final ZlibWrapper wrapper = strict ? ZLIB : ZLIB_OR_NONE;
            return new EmbeddedChannel(ctx.channel().id(), hasDisconnect, ctx.channel().config(), newZlibDecoder(wrapper));
        }

        return null;
    }
}

@SuppressWarnings(UNUSED)
@TargetClass(value = DelegatingDecompressorFrameListener.class)
final class TargetNettyDelegatingDecompressorFrameListener {

    @Alias
    boolean strict;

    @Substitute
    protected EmbeddedChannel newContentDecompressor(ChannelHandlerContext ctx, CharSequence contentEncoding) throws Http2Exception {
        boolean hasDisconnect = ctx.channel().metadata().hasDisconnect();
        if (!GZIP.contentEqualsIgnoreCase(contentEncoding) && !X_GZIP.contentEqualsIgnoreCase(contentEncoding)) {
            if (!DEFLATE.contentEqualsIgnoreCase(contentEncoding) && !X_DEFLATE.contentEqualsIgnoreCase(contentEncoding)) {
                return null;
            }
            ZlibWrapper wrapper = this.strict ? ZLIB : ZLIB_OR_NONE;
            ChannelHandler[] handlers = {newZlibDecoder(wrapper)};
            return new EmbeddedChannel(ctx.channel().id(), hasDisconnect, ctx.channel().config(), handlers);
        }
        ChannelHandler[] handlers = {newZlibDecoder(ZlibWrapper.GZIP)};
        return new EmbeddedChannel(ctx.channel().id(), hasDisconnect, ctx.channel().config(), handlers);

    }
}

@SuppressWarnings(UNUSED)
@TargetClass(className = NETTY_DIR_CONTEXT_UTILS_CLASS)
final class TargetDirContextUtils {
    @Substitute
    static void addNameServers(List<InetSocketAddress> defaultNameServers, int defaultPort) {
        defaultNameServers.addAll(DEFAULT_DNS_SERVERS.stream()
                .map(address -> new InetSocketAddress(address, defaultPort))
                .collect(listCollector()));
    }
}

@SuppressWarnings(UNUSED)
class GraalNettySubstitutions {
}
