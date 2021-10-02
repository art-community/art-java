package io.art.core.graal;

import com.oracle.svm.core.jdk.*;
import com.oracle.svm.core.jni.*;
import com.oracle.svm.hosted.c.*;
import lombok.experimental.*;
import org.graalvm.nativeimage.hosted.*;
import static com.oracle.svm.hosted.FeatureImpl.*;
import java.io.*;
import java.lang.reflect.*;

@UtilityClass
public class GraalNativeRegistrator {
    public static void registerForNativeUsage(String... classes) {
        for (String name : classes) {
            try {
                registerForNativeUsage(Class.forName(name, false, GraalNativeRegistrator.class.getClassLoader()));
            } catch (ClassNotFoundException classNotFoundException) {
                // Ignore
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public static void registerForNativeUsage(Class<?>... classes) {
        for (Class<?> owner : classes) {
            registerForNativeUsage(owner);
        }
    }

    public static void registerForNativeUsage(Class<?> owner) {
        try {
            JNIRuntimeAccess.register(owner);
            RuntimeReflection.register(owner);
            for (final Method method : owner.getDeclaredMethods()) {
                JNIRuntimeAccess.register(method);
                RuntimeReflection.register(method);
            }
            for (final Field field : owner.getDeclaredFields()) {
                JNIRuntimeAccess.register(field);
                RuntimeReflection.register(field);
            }
            for (final Constructor<?> constructor : owner.getDeclaredConstructors()) {
                JNIRuntimeAccess.register(constructor);
                RuntimeReflection.register(constructor);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void registerStaticNativeLibrary(BeforeAnalysisAccessImpl access, GraalStaticLibraryConfiguration configuration) {
        File libraryPath = configuration.getLibraryPath().toFile();
        if (!libraryPath.exists()) return;

        NativeLibrarySupport.singleton().preregisterUninitializedBuiltinLibrary(configuration.getLibraryName());
        PlatformNativeLibrarySupport nativeLibrarySupport = PlatformNativeLibrarySupport.singleton();
        configuration.getSymbolPrefixes().forEach(nativeLibrarySupport::addBuiltinPkgNativePrefix);

        NativeLibraries nativeLibraries = access.getNativeLibraries();
        nativeLibraries.addStaticJniLibrary(configuration.getLibraryName());
    }
}
