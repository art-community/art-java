package io.art.core.constants;

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
            "sun.instrument.InstrumentationImpl"
    };
}