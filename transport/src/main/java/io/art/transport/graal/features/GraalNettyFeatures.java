package io.art.transport.graal.features;

import com.oracle.svm.core.*;
import com.oracle.svm.core.option.*;
import com.oracle.svm.core.os.*;
import com.oracle.svm.hosted.FeatureImpl.*;
import io.art.core.graal.*;
import org.graalvm.nativeimage.*;
import org.graalvm.nativeimage.hosted.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.GraalConstants.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.JarExtensions.*;
import static io.art.core.graal.GraalNativeRegistrator.*;
import static io.art.transport.constants.TransportModuleConstants.GraalConstants.*;
import static io.netty.util.internal.MacAddressUtil.*;
import static java.lang.Boolean.*;
import static java.lang.System.*;
import java.io.*;
import java.util.*;

public class GraalNettyFeatures implements Feature {
    @Override
    public void duringSetup(DuringSetupAccess access) {
        String property = getProperty(NETTY_STATIC_LINK_PROPERTY);
        if (!TRUE.toString().equalsIgnoreCase(property)) return;
        if (!Platform.includedIn(Platform.LINUX.class)) return;

        String workingPath = orElse(getProperty(GRAAL_WORKING_PATH_PROPERTY), EMPTY_STRING);
        File libraryDirectory = new File(workingPath);
        for (String name : NETTY_EPOLL_LIBRARY_REGEXPS) {
            extractCurrentJarEntry(GraalNettyFeatures.class, name, libraryDirectory.getAbsolutePath());
        }

        LocatableMultiOptionValue<String> current = cast(SubstrateOptions.CLibraryPath.getValue());
        current.valueUpdate(libraryDirectory.toPath().resolve(NETTY_STATIC_LIBRARIES_RELATIVE_PATH).toFile().getAbsolutePath());
        out.println(SubstrateOptions.CLibraryPath.getValue().values());

    }

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
        linkStatic((BeforeAnalysisAccessImpl) access);
    }

    private void linkStatic(BeforeAnalysisAccessImpl access) {

        GraalStaticLibraryConfiguration nettyLibrary = GraalStaticLibraryConfiguration.builder()
                .libraryNames(NETTY_EPOLL_LIBRARY_NAMES)
                .symbolPrefixes(NETTY_NATIVE_LIBRARY_PREFIXES)
                .build();

        registerStaticNativeLibrary(access, nettyLibrary);
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

    private void registerMacOsClasses() {
        try {
            if (IsDefined.isDarwin()) {
                registerForNativeUsage(NETTY_MAC_OS_CLASSES);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}

