package io.art.transport.graal.features;

import org.graalvm.nativeimage.hosted.*;
import static io.art.core.graal.GraalNativeRegistrator.*;
import static io.art.transport.constants.TransportModuleConstants.GraalConstants.*;
import static io.netty.util.internal.MacAddressUtil.*;
import static java.lang.System.*;
import java.util.*;

public class GraalNettyFeatures implements Feature {
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        setProperty(MAX_UPDATE_ARRAY_SIZE_PROPERTY, DEFAULT_MAX_UPDATE_ARRAY_SIZE);
        setProperty(NETTY_MAX_ORDER_PROPERTY, DEFAULT_NETTY_MAX_ORDER);
        setProperty(NETTY_LEAK_DETECTION_PROPERTY, DEFAULT_NETTY_LEAK_DETECTION);
        final byte[] machineIdBytes = new byte[EUI64_MAC_ADDRESS_LENGTH];
        new Random().nextBytes(machineIdBytes);
        final String nettyMachineId = formatAddress(machineIdBytes);
        setProperty(NETTY_MACHINE_ID_PROPERTY, nettyMachineId);
        registerEpoll();
        registerKqueue();
    }

    private void registerKqueue() {
        try {
            Class.forName(NETTY_KQUEUE_CLASS_NAME, false, GraalNettyFeatures.class.getClassLoader());
            registerForNativeUsage(NETTY_NATIVE_KQUEUE_CLASSES);
        } catch (ClassNotFoundException classNotFoundException) {
            // Ignore
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void registerEpoll() {
        try {
            Class.forName(NETTY_EPOLL_CLASS_NAME, false, GraalNettyFeatures.class.getClassLoader());
            registerForNativeUsage(NETTY_NATIVE_EPOLL_CLASSES);
        } catch (ClassNotFoundException classNotFoundException) {
            // Ignore
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}

