package io.art.transport.constants;

import lombok.*;
import static io.art.core.constants.ThreadConstants.*;
import static io.art.transport.constants.TransportModuleConstants.BufferType.*;
import static java.lang.Integer.*;
import static java.lang.Math.max;
import static java.lang.Math.*;
import static java.time.Duration.*;
import static reactor.netty.resources.ConnectionProvider.*;
import java.time.*;

public interface TransportModuleConstants {
    String TRANSPORT_LOGGER = "transport";

    interface Defaults {
        String DEFAULT_LOOP_RESOURCES_PREFIX = "common-transport";
        String DEFAULT_CONNECTION_PROVIDER_NAME = "common-connection";
        int DEFAULT_SELECTORS_COUNT = 2;
        int DEFAULT_WORKERS_COUNT = (int) max(ceil((double) DEFAULT_THREAD_POOL_SIZE / 2), 2);
        Duration DEFAULT_EVICTION_INTERVAL = ZERO;
        int DEFAULT_MAX_CONNECTIONS = 1024;
        int DEFAULT_PENDING_ACQUIRE_MAX_COUNT = -2;
        Duration DEFAULT_PENDING_ACQUIRE_TIMEOUT = ofSeconds(30);
        Duration DEFAULT_MAX_LIFE_TIME = ofMillis(-1);
        Duration DEFAULT_MAX_IDLE_TIME = ofMillis(-1);
        String DEFAULT_LEASING_STRATEGY = LEASING_STRATEGY_FIFO;
        BufferType DEFAULT_BUFFER_TYPE = IO;
        int DEFAULT_BUFFER_INITIAL_CAPACITY = 2048;
        int DEFAULT_BUFFER_MAX_CAPACITY = MAX_VALUE;
    }

    interface Messages {
        String TRANSPORT_CONFIGURING_MESSAGE = "Common transport resources configured by transport module";
    }

    interface ConfigurationKeys {
        String TRANSPORT_COMMON_SECTION = "transport.common";
        String PENDING_ACQUIRE_TIMEOUT_KEY = "pendingAcquireTimeout";
        String MAX_CONNECTIONS_KEY = "maxConnections";
        String PENDING_ACQUIRE_MAX_COUNT_KEY = "pendingAcquireMaxCount";
        String MAX_IDLE_TIME_KEY = "maxIdleTime";
        String MAX_LIFE_TIME_KEY = "maxLifeTime";
        String metrics_KEY = "metrics";
        String LEASING_STRATEGY_KEY = "leasingStrategy";
        String EVICTION_KEY = "eviction";
        String EVICTION_INTERVAL_KEY = "evictionInterval";
        String WORKERS_COUNT_KEY = "workersCount";
        String SELECTORS_COUNT_KEY = "selectorsCount";
        String CONNECTION_PROVIDER_NAME_KEY = "connectionProviderName";
        String LOOP_RESOURCES_PREFIX_KEY = "loopResourcesPrefix";
        String DISPOSE_INACTIVE_KEY = "disposeInactive";
        String DISPOSE_INTERVAL_KEY = "disposeInterval";
        String POOL_INACTIVITY_KEY = "poolInactivity";
        String BUFFER_WRITE_SECTION = "buffer.write";
        String BUFFER_TYPE_KEY = "type";
        String BUFFER_INITIAL_CAPACITY_KEY = "initialCapacity";
        String BUFFER_MAX_CAPACITY_KEY = "maxCapacity";

        String SSL_SECTION = "ssl";
        String DATA_FORMAT_KEY = "dataFormat";
        String META_DATA_FORMAT_KEY = "metaDataFormat";
        String SERVICE_ID_KEY = "serviceId";
        String METHOD_ID_KEY = "methodId";
        String TIMEOUT_KEY = "timeout";
        String PAYLOAD_DECODER_KEY = "payloadDecoder";
        String CERTIFICATE_KEY = "certificate";
        String KEY_KEY = "key";
        String PASSWORD_KEY = "password";
        String DEACTIVATED_KEY = "deactivated";
        String LOGGING_KEY = "logging";
    }

    @Getter
    @AllArgsConstructor
    enum DataFormat {
        JSON("json"),
        YAML("yaml"),
        MESSAGE_PACK("messagePack"),
        STRING("STRING"),
        BYTES("bytes");

        private final String format;

        public static DataFormat dataFormat(String format, DataFormat fallback) {
            if (JSON.format.equalsIgnoreCase(format)) return JSON;
            if (YAML.format.equalsIgnoreCase(format)) return YAML;
            if (MESSAGE_PACK.format.equalsIgnoreCase(format)) return MESSAGE_PACK;
            if (BYTES.format.equalsIgnoreCase(format)) return BYTES;
            if (STRING.format.equalsIgnoreCase(format)) return STRING;
            return fallback;
        }
    }

    @Getter
    @AllArgsConstructor
    enum BufferType {
        DEFAULT("default"),
        HEAP("heap"),
        IO("io"),
        DIRECT("direct");

        private final String type;

        public static BufferType bufferType(String type, BufferType fallback) {
            if (DEFAULT.type.equalsIgnoreCase(type)) return DEFAULT;
            if (HEAP.type.equalsIgnoreCase(type)) return HEAP;
            if (IO.type.equalsIgnoreCase(type)) return IO;
            if (DIRECT.type.equalsIgnoreCase(type)) return DIRECT;
            return fallback;
        }
    }

    interface GraalConstants {
        int EUI64_MAC_ADDRESS_LENGTH = 8;
        String MAX_UPDATE_ARRAY_SIZE_PROPERTY = "sun.nio.ch.maxUpdateArraySize";
        String DEFAULT_MAX_UPDATE_ARRAY_SIZE = "100";
        String NETTY_MAX_ORDER_PROPERTY = "io.netty.allocator.maxOrder";
        String DEFAULT_NETTY_MAX_ORDER = "100";
        String NETTY_MACHINE_ID_PROPERTY = "io.netty.machineId";
        String NETTY_LEAK_DETECTION_PROPERTY = "io.netty.leakDetection.level";
        String DEFAULT_NETTY_LEAK_DETECTION = "DISABLED";

        String NETTY_SSL_ENGINE_TYPE_CLASS = "io.netty.handler.ssl.SslHandler$SslEngineType";
        String NETTY_CHANNEL_HANDLER_MASK_CLASS = "io.netty.channel.ChannelHandlerMask";
        String NETTY_SCHEDULER_FUTURE_TASK_CLASS = "io.netty.util.concurrent.ScheduledFutureTask";
        String NETTY_REF_CNT_NAME = "refCnt";
        String NETTY_JDK_DEFAULT_APPLICATION_PROTOCOL_NEGOTIATOR_CLASS = "io.netty.handler.ssl.JdkDefaultApplicationProtocolNegotiator";
        String NETTY_JDK_ALPN_PROTOCOL_NEGOTIATOR_ALPN_WRAPPER_CLASS = "io.netty.handler.ssl.JdkAlpnApplicationProtocolNegotiator$AlpnWrapper";
        String NETTY_SSL_JETTY_ALPN_SSL_ENGINE_CLASS = "io.netty.handler.ssl.JettyAlpnSslEngine";
        String NETTY_SSL_JDK_ALPN_SSL_ENGINE_CLASS = "io.netty.handler.ssl.JdkAlpnSslEngine";
        String NETTY_DIR_CONTEXT_UTILS_CLASS = "io.netty.resolver.dns.DirContextUtils";
        String NETTY_EPOLL_CLASS_NAME = "io.netty.channel.epoll.Epoll";
        String NETTY_KQUEUE_CLASS_NAME = "io.netty.channel.kqueue.KQueue";

        String NETTY_OSCP_EXCEPTION = "OCSP is not supported with this SslProvider: {0}";
        String NETTY_JDK_SSL_PROVIDER_EXCEPTION = "JDK provider does not support ";
        String NETTY_JDK_SSL_FAILURE_BEHAVIOR_EXCEPTION = " failure behavior";
        String NETTY_JDK_SSL_PROTOCOL_EXCEPTION = " protocol";
        String NETTY_SSL_PROVIDER_UNSUPPORTED_EXCEPTION = "SslProvider unsupported: {0}";
        String NETTY_OPEN_SSL_UNSUPPORTED_EXCEPTION = "OpenSSL unsupported";
        String NETTY_UNABLE_TO_WRAP_SSL_ENGINE_EXCEPTION = "Unable to wrap SSLEngine of type {0}";

        String[] NETTY_NATIVE_EPOLL_CLASSES = new String[]{
                "io.netty.channel.DefaultFileRegion",
                "io.netty.channel.ChannelException",
                "io.netty.channel.epoll.LinuxSocket",
                "io.netty.channel.epoll.Native",
                "io.netty.channel.epoll.NativeStaticallyReferencedJniMethods",
                "io.netty.channel.epoll.NativeDatagramPacketArray$NativeDatagramPacket",
                "io.netty.channel.unix.PeerCredentials",
                "io.netty.channel.unix.Buffer",
                "io.netty.channel.unix.ErrorsStaticallyReferencedJniMethods",
                "io.netty.channel.unix.FileDescriptor",
                "io.netty.channel.unix.LimitsStaticallyReferencedJniMethods",
                "io.netty.channel.unix.DatagramSocketAddress",
                "io.netty.channel.unix.Socket",
                "io.netty.channel.unix.Unix",
        };

        String[] NETTY_NATIVE_KQUEUE_CLASSES = new String[]{
                "io.netty.channel.DefaultFileRegion",
                "io.netty.channel.ChannelException",
                "io.netty.channel.kqueue.BsdSocket",
                "io.netty.channel.kqueue.Native",
                "io.netty.channel.kqueue.KQueueStaticallyReferencedJniMethods",
                "io.netty.channel.kqueue.KQueueEventArray",
                "io.netty.channel.unix.PeerCredentials",
                "io.netty.channel.unix.Buffer",
                "io.netty.channel.unix.ErrorsStaticallyReferencedJniMethods",
                "io.netty.channel.unix.FileDescriptor",
                "io.netty.channel.unix.LimitsStaticallyReferencedJniMethods",
                "io.netty.channel.unix.DatagramSocketAddress",
                "io.netty.channel.unix.Socket",
                "io.netty.channel.unix.Unix"
        };

        String[] NETTY_MAC_OS_CLASSES = new String[]{
                "io.netty.resolver.dns.macos.MacOSDnsServerAddressStreamProvider",
                "io.netty.resolver.dns.macos.DnsResolver"
        };

        String[] NETTY_NATIVE_LIBRARY_PREFIXES = new String[]{
                "io_netty_channel_epoll_Native_epollBusyWait0",
                "io_netty_channel_epoll_Native_epollCreate",
                "io_netty_channel_epoll_Native_epollCtlAdd0",
                "io_netty_channel_epoll_Native_epollCtlDel0",
                "io_netty_channel_epoll_Native_epollCtlMod0",
                "io_netty_channel_epoll_Native_epollWait",
                "io_netty_channel_epoll_Native_epollWait0",
                "io_netty_channel_epoll_Native_eventFd",
                "io_netty_channel_epoll_Native_eventFdRead",
                "io_netty_channel_epoll_Native_eventFdWrite",
                "io_netty_channel_epoll_Native_isSupportingUdpSegment",
                "io_netty_channel_epoll_Native_offsetofEpollData",
                "io_netty_channel_epoll_Native_registerUnix",
                "io_netty_channel_epoll_Native_sizeofEpollEvent",
                "io_netty_channel_epoll_Native_splice0",
                "io_netty_channel_epoll_NativeStaticallyReferencedJniMethods_epollerr",
                "io_netty_channel_epoll_NativeStaticallyReferencedJniMethods_epollet",
                "io_netty_channel_epoll_NativeStaticallyReferencedJniMethods_epollin",
                "io_netty_channel_epoll_NativeStaticallyReferencedJniMethods_epollout",
                "io_netty_channel_epoll_NativeStaticallyReferencedJniMethods_epollrdhup",
                "io_netty_channel_epoll_NativeStaticallyReferencedJniMethods_isSupportingRecvmmsg",
                "io_netty_channel_epoll_NativeStaticallyReferencedJniMethods_isSupportingSendmmsg",
                "io_netty_channel_epoll_NativeStaticallyReferencedJniMethods_kernelVersion",
                "io_netty_channel_epoll_NativeStaticallyReferencedJniMethods_tcpFastopenMode",
                "io_netty_channel_epoll_NativeStaticallyReferencedJniMethods_tcpMd5SigMaxKeyLen",
                "io_netty_channel_epoll_Native_timerFd",
                "io_netty_channel_epoll_Native_timerFdRead",
                "io_netty_channel_epoll_Native_timerFdSetTime",
                "io_netty_channel_epoll_LinuxSocket_getInterface",
                "io_netty_channel_epoll_LinuxSocket_getIpMulticastLoop",
                "io_netty_channel_epoll_LinuxSocket_getSoBusyPoll",
                "io_netty_channel_epoll_LinuxSocket_getTcpDeferAccept",
                "io_netty_channel_epoll_LinuxSocket_getTcpInfo",
                "io_netty_channel_epoll_LinuxSocket_getTcpKeepCnt",
                "io_netty_channel_epoll_LinuxSocket_getTcpKeepIdle",
                "io_netty_channel_epoll_LinuxSocket_getTcpKeepIntvl",
                "io_netty_channel_epoll_LinuxSocket_getTcpNotSentLowAt",
                "io_netty_channel_epoll_LinuxSocket_getTcpUserTimeout",
                "io_netty_channel_epoll_LinuxSocket_getTimeToLive",
                "io_netty_channel_epoll_LinuxSocket_isIpFreeBind",
                "io_netty_channel_epoll_LinuxSocket_isIpRecvOrigDestAddr",
                "io_netty_channel_epoll_LinuxSocket_isIpTransparent",
                "io_netty_channel_epoll_LinuxSocket_isTcpCork",
                "io_netty_channel_epoll_LinuxSocket_isTcpQuickAck",
                "io_netty_channel_epoll_LinuxSocket_isUdpGro",
                "io_netty_channel_epoll_LinuxSocket_joinGroup",
                "io_netty_channel_epoll_LinuxSocket_joinSsmGroup",
                "io_netty_channel_epoll_LinuxSocket_leaveGroup",
                "io_netty_channel_epoll_LinuxSocket_leaveSsmGroup",
                "io_netty_channel_epoll_LinuxSocket_setInterface",
                "io_netty_channel_epoll_LinuxSocket_setIpFreeBind",
                "io_netty_channel_epoll_LinuxSocket_setIpMulticastLoop",
                "io_netty_channel_epoll_LinuxSocket_setIpRecvOrigDestAddr",
                "io_netty_channel_epoll_LinuxSocket_setIpTransparent",
                "io_netty_channel_epoll_LinuxSocket_setSoBusyPoll",
                "io_netty_channel_epoll_LinuxSocket_setTcpCork",
                "io_netty_channel_epoll_LinuxSocket_setTcpDeferAccept",
                "io_netty_channel_epoll_LinuxSocket_setTcpFastOpen",
                "io_netty_channel_epoll_LinuxSocket_setTcpKeepCnt",
                "io_netty_channel_epoll_LinuxSocket_setTcpKeepIdle",
                "io_netty_channel_epoll_LinuxSocket_setTcpKeepIntvl",
                "io_netty_channel_epoll_LinuxSocket_setTcpMd5Sig",
                "io_netty_channel_epoll_LinuxSocket_setTcpNotSentLowAt",
                "io_netty_channel_epoll_LinuxSocket_setTcpQuickAck",
                "io_netty_channel_epoll_LinuxSocket_setTcpUserTimeout",
                "io_netty_channel_epoll_LinuxSocket_setTimeToLive",
                "io_netty_channel_epoll_LinuxSocket_setUdpGro",
                "io_netty_channel_unix_Buffer_addressSize0",
                "io_netty_channel_unix_Buffer_memoryAddress0",
                "io_netty_channel_unix_ErrorsStaticallyReferencedJniMethods_errnoEAGAIN",
                "io_netty_channel_unix_ErrorsStaticallyReferencedJniMethods_errnoEBADF",
                "io_netty_channel_unix_ErrorsStaticallyReferencedJniMethods_errnoECONNRESET",
                "io_netty_channel_unix_ErrorsStaticallyReferencedJniMethods_errnoEINPROGRESS",
                "io_netty_channel_unix_ErrorsStaticallyReferencedJniMethods_errnoENOENT",
                "io_netty_channel_unix_ErrorsStaticallyReferencedJniMethods_errnoENOTCONN",
                "io_netty_channel_unix_ErrorsStaticallyReferencedJniMethods_errnoEPIPE",
                "io_netty_channel_unix_ErrorsStaticallyReferencedJniMethods_errnoEWOULDBLOCK",
                "io_netty_channel_unix_ErrorsStaticallyReferencedJniMethods_errorEALREADY",
                "io_netty_channel_unix_ErrorsStaticallyReferencedJniMethods_errorECONNREFUSED",
                "io_netty_channel_unix_ErrorsStaticallyReferencedJniMethods_errorEISCONN",
                "io_netty_channel_unix_ErrorsStaticallyReferencedJniMethods_errorENETUNREACH",
                "io_netty_channel_unix_ErrorsStaticallyReferencedJniMethods_strError",
                "io_netty_channel_unix_FileDescriptor_close",
                "io_netty_channel_unix_FileDescriptor_newPipe",
                "io_netty_channel_unix_FileDescriptor_open",
                "io_netty_channel_unix_FileDescriptor_read",
                "io_netty_channel_unix_FileDescriptor_readAddress",
                "io_netty_channel_unix_FileDescriptor_write",
                "io_netty_channel_unix_FileDescriptor_writeAddress",
                "io_netty_channel_unix_FileDescriptor_writev",
                "io_netty_channel_unix_FileDescriptor_writevAddresses",
                "io_netty_channel_unix_LimitsStaticallyReferencedJniMethods_iovMax",
                "io_netty_channel_unix_LimitsStaticallyReferencedJniMethods_sizeOfjlong",
                "io_netty_channel_unix_LimitsStaticallyReferencedJniMethods_ssizeMax",
                "io_netty_channel_unix_LimitsStaticallyReferencedJniMethods_udsSunPathSize",
                "io_netty_channel_unix_LimitsStaticallyReferencedJniMethods_uioMaxIov",
                "io_netty_channel_unix_Socket_accept",
                "io_netty_channel_unix_Socket_bind",
                "io_netty_channel_unix_Socket_bindDomainSocket",
                "io_netty_channel_unix_Socket_connect",
                "io_netty_channel_unix_Socket_connectDomainSocket",
                "io_netty_channel_unix_Socket_disconnect",
                "io_netty_channel_unix_Socket_finishConnect",
                "io_netty_channel_unix_Socket_getReceiveBufferSize",
                "io_netty_channel_unix_Socket_getSendBufferSize",
                "io_netty_channel_unix_Socket_getSoError",
                "io_netty_channel_unix_Socket_getSoLinger",
                "io_netty_channel_unix_Socket_getTrafficClass",
                "io_netty_channel_unix_Socket_initialize",
                "io_netty_channel_unix_Socket_isBroadcast",
                "io_netty_channel_unix_Socket_isIPv6",
                "io_netty_channel_unix_Socket_isIPv6Preferred",
                "io_netty_channel_unix_Socket_isKeepAlive",
                "io_netty_channel_unix_Socket_isReuseAddress",
                "io_netty_channel_unix_Socket_isReusePort",
                "io_netty_channel_unix_Socket_isTcpNoDelay",
                "io_netty_channel_unix_Socket_listen",
                "io_netty_channel_unix_Socket_localAddress",
                "io_netty_channel_unix_Socket_msgFastopen",
                "io_netty_channel_unix_Socket_newSocketDgramFd",
                "io_netty_channel_unix_Socket_newSocketDomainDgramFd",
                "io_netty_channel_unix_Socket_newSocketDomainFd",
                "io_netty_channel_unix_Socket_newSocketStreamFd",
                "io_netty_channel_unix_Socket_recvFd",
                "io_netty_channel_unix_Socket_remoteAddress",
                "io_netty_channel_unix_Socket_sendFd",
                "io_netty_channel_unix_Socket_sendTo",
                "io_netty_channel_unix_Socket_sendToAddress",
                "io_netty_channel_unix_Socket_sendToAddressDomainSocket",
                "io_netty_channel_unix_Socket_sendToAddresses",
                "io_netty_channel_unix_Socket_sendToAddressesDomainSocket",
                "io_netty_channel_unix_Socket_sendToDomainSocket",
                "io_netty_channel_unix_Socket_setBroadcast",
                "io_netty_channel_unix_Socket_setKeepAlive",
                "io_netty_channel_unix_Socket_setReceiveBufferSize",
                "io_netty_channel_unix_Socket_setReuseAddress",
                "io_netty_channel_unix_Socket_setReusePort",
                "io_netty_channel_unix_Socket_setSendBufferSize",
                "io_netty_channel_unix_Socket_setSoLinger",
                "io_netty_channel_unix_Socket_setTcpNoDelay",
                "io_netty_channel_unix_Socket_setTrafficClass",
                "io_netty_channel_unix_Socket_shutdown",
        };

        String NETTY_STATIC_LINK_PROPERTY = "netty.static";

        String[] NETTY_EPOLL_LIBRARY_NAMES = new String[]{
                "netty_transport_native_epoll",
                "netty_transport_native_unix"
        };

        String[] NETTY_EPOLL_LIBRARY_REGEXPS = new String[]{
                ".+libnetty_transport_native_epoll\\.a",
                ".+libnetty_transport_native_unix\\.a"
        };

        String NETTY_STATIC_LIBRARIES_RELATIVE_PATH = "netty-static-linux-libraries";
    }
}
