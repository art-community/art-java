package io.art.transport.graal.features;

import org.graalvm.nativeimage.hosted.*;
import static io.netty.util.internal.MacAddressUtil.*;
import static io.netty.util.internal.logging.InternalLoggerFactory.*;
import static io.netty.util.internal.logging.JdkLoggerFactory.*;
import static java.lang.System.*;
import java.util.*;

public class GraalNettyFeatures implements Feature {
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        setDefaultFactory(INSTANCE);
        setProperty("sun.nio.ch.maxUpdateArraySize", "100");
        setProperty("io.netty.allocator.maxOrder", "3");
        final int EUI64_MAC_ADDRESS_LENGTH = 8;
        final byte[] machineIdBytes = new byte[EUI64_MAC_ADDRESS_LENGTH];
        new Random().nextBytes(machineIdBytes);
        final String nettyMachineId = formatAddress(machineIdBytes);
        setProperty("io.netty.machineId", nettyMachineId);
        setProperty("io.netty.leakDetection.level", "DISABLED");
    }
}
