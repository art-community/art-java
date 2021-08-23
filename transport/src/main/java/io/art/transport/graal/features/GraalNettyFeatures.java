package io.art.transport.graal.features;

import io.netty.util.internal.logging.*;
import org.graalvm.nativeimage.hosted.*;
import static io.art.core.graal.GraalNativeRegistrator.*;
import static io.art.transport.constants.TransportModuleConstants.GraalConstants.*;
import static io.netty.util.internal.MacAddressUtil.*;
import static io.netty.util.internal.logging.InternalLoggerFactory.*;
import static java.lang.System.*;
import java.util.*;

public class GraalNettyFeatures implements Feature {
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        setDefaultFactory(JdkLoggerFactory.INSTANCE);
        setProperty(MAX_UPDATE_ARRAY_SIZE_PROPERTY, DEFAULT_MAX_UPDATE_ARRAY_SIZE);
        setProperty(NETTY_MAX_ORDER_PROPERTY, DEFAULT_NETTY_MAX_ORDER);
        final byte[] machineIdBytes = new byte[EUI64_MAC_ADDRESS_LENGTH];
        new Random().nextBytes(machineIdBytes);
        final String nettyMachineId = formatAddress(machineIdBytes);
        setProperty(NETTY_MACHINE_ID_PROPERTY, nettyMachineId);
        setProperty(NETTY_LEAK_DETECTION_PROPERTY, DEFAULT_NETTY_LEAK_DETECTION);
        try {
            Class.forName(NETTY_EPOLL_CLASS_NAME, false, GraalNettyFeatures.class.getClassLoader());
            registerForNativeUsage(nettyEpollClasses());
        } catch (ClassNotFoundException classNotFoundException) {
            // Ignore
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}

