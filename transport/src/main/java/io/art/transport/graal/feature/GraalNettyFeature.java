package io.art.transport.graal.feature;

import com.oracle.svm.core.annotate.*;
import io.art.core.graal.*;
import org.graalvm.nativeimage.*;
import org.graalvm.nativeimage.hosted.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.core.graal.GraalNativeLibraryConfiguration.Type.*;
import static io.art.core.graal.GraalNativeRegistrator.*;
import static io.art.transport.constants.TransportModuleConstants.GraalConstants.*;
import static io.netty.util.internal.MacAddressUtil.*;
import static java.lang.Boolean.*;
import static java.lang.System.*;
import java.util.*;

public class GraalNettyFeature implements Feature {
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
        registerMacOsClasses();
        linkStatic(access);
    }

    private void linkStatic(BeforeAnalysisAccess access) {
        String property = getProperty(NETTY_STATIC_LINK_PROPERTY);
        if (!TRUE.toString().equalsIgnoreCase(property)) return;
        if (!Platform.includedIn(Platform.LINUX.class)) return;

        GraalNativeLibraryConfiguration epoll = GraalNativeLibraryConfiguration.builder()
                .builtin(true)
                .type(STATIC)
                .builtinSymbolPrefixes(setOf(NETTY_NATIVE_LIBRARY_PREFIXES))
                .location(GraalNativeLibraryLocation.builder()
                        .extractionDirectory(NETTY_STATIC_LIBRARIES_RELATIVE_PATH)
                        .resource(NETTY_TRANSPORT_NATIVE_EPOLL_LIBRARY_REGEX)
                        .build())
                .name(NETTY_TRANSPORT_NATIVE_EPOLL_LIBRARY_NAME)
                .build();

        GraalNativeLibraryConfiguration unix = GraalNativeLibraryConfiguration.builder()
                .builtin(true)
                .type(STATIC)
                .builtinSymbolPrefixes(setOf(NETTY_NATIVE_LIBRARY_PREFIXES))
                .location(GraalNativeLibraryLocation.builder()
                        .extractionDirectory(NETTY_TRANSPORT_NATIVE_UNIX_LIBRARY_REGEX)
                        .resource(NETTY_TRANSPORT_NATIVE_EPOLL_LIBRARY_REGEX)
                        .build())
                .name(NETTY_TRANSPORT_NATIVE_UNIX_LIBRARY_NAME)
                .build();

        registerNativeLibraries(access, epoll, unix);
    }

    private void registerKqueue() {
        try {
            Class.forName(NETTY_KQUEUE_CLASS_NAME, false, GraalNettyFeature.class.getClassLoader());
            registerForNativeUsage(NETTY_NATIVE_KQUEUE_CLASSES);
        } catch (ClassNotFoundException classNotFoundException) {
            // Ignore
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void registerEpoll() {
        try {
            Class.forName(NETTY_EPOLL_CLASS_NAME, false, GraalNettyFeature.class.getClassLoader());
            registerForNativeUsage(NETTY_NATIVE_EPOLL_CLASSES);
        } catch (ClassNotFoundException classNotFoundException) {
            // Ignore
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void registerMacOsClasses() {
        try {
            if (!Platform.includedIn(Platform.DARWIN.class)) return;
            registerForNativeUsage(NETTY_MAC_OS_CLASSES);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}

