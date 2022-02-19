package io.art.core.constants;

import lombok.*;

public interface GraalConstants {
    String[] GRAAL_NATIVE_CORE_CLASSES = new String[]{
            "java.io.IOException",
            "java.lang.ClassLoader",
            "java.lang.OutOfMemoryError",
            "java.lang.RuntimeException",
            "java.net.InetSocketAddress",
            "java.net.PortUnreachableException",
            "java.nio.Buffer",
            "java.nio.DirectByteBuffer",
            "java.nio.channels.ClosedChannelException",
            "jdk.internal.loader.ClassLoaders$PlatformClassLoader",
            "org.graalvm.nativebridge.jni.JNIExceptionWrapperEntryPoints",
            "sun.management.VMManagementImpl",
            "java.io.FileDescriptor",
            "sun.nio.ch.FileChannelImpl",
            "java.lang.String",
            "java.nio.ByteBuffer",
            "sun.net.dns.ResolverConfigurationImpl",
            "sun.instrument.InstrumentationImpl",
            "sun.net.dns.ResolverConfiguration",
            "sun.net.dns.OptionsImpl"
    };

    String GRAAL_WORKING_PATH_PROPERTY = "working-path";
    String GRAAL_LIBRARY_EXTRACTION_DIRECTORY_POSTFIX = "-library";

    @Getter
    @AllArgsConstructor
    enum GraalLinuxLibrary {
        SSL("ssl"),
        CRYPTO("crypto"),
        SELINUX("selinux"),
        UTIL("util"),
        CAP("cap");

        private final String library;
    }
}
