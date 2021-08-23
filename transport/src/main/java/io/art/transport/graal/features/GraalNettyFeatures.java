package io.art.transport.graal.features;

import io.art.core.property.*;
import io.art.logging.*;
import io.art.logging.netty.*;
import io.netty.util.internal.logging.*;
import org.graalvm.nativeimage.hosted.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.graal.GraalNativeRegistrator.*;
import static io.art.core.property.DisposableProperty.*;
import static io.art.transport.constants.TransportModuleConstants.GraalConstants.*;
import static io.netty.util.internal.MacAddressUtil.*;
import static java.lang.System.*;
import static org.graalvm.nativeimage.ImageInfo.*;
import java.util.*;
import java.util.function.*;

public class GraalNettyFeatures implements Feature {
    public final static DisposableProperty<Function<String, InternalLogger>> NETTY_LOGGER = disposable(GraalNettyFeatures::createLogger);

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

    @Override
    public void afterImageWrite(AfterImageWriteAccess access) {
        NETTY_LOGGER.dispose();
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

    private static Function<String, InternalLogger> createLogger() {
        if (inImageBuildtimeCode() || !withLogging()) {
            JdkLoggerFactory defaultFactory = cast(JdkLoggerFactory.INSTANCE);
            return defaultFactory::newInstance;
        }
        return name -> new NettyLogger(Logging.logger(name));
    }
}

