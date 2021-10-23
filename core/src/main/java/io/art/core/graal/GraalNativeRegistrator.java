package io.art.core.graal;

import com.oracle.svm.core.jdk.*;
import com.oracle.svm.core.jni.*;
import lombok.experimental.*;
import org.graalvm.nativeimage.hosted.*;
import static com.oracle.svm.hosted.FeatureImpl.*;
import static java.util.Arrays.*;
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
                System.out.println("owner: " + owner.getName() + " m: " + method.toString());
                method.setAccessible(true);
                JNIRuntimeAccess.register(method);
                RuntimeReflection.register(method);
            }
            for (final Field field : owner.getDeclaredFields()) {
                field.setAccessible(true);
                JNIRuntimeAccess.register(field);
                RuntimeReflection.register(field);
            }
            for (final Constructor<?> constructor : owner.getDeclaredConstructors()) {
                constructor.setAccessible(true);
                JNIRuntimeAccess.register(constructor);
                RuntimeReflection.register(constructor);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void registerStaticNativeLibrary(BeforeAnalysisAccessImpl access, GraalStaticLibraryConfiguration configuration) {
        stream(configuration.getLibraryNames()).forEach(NativeLibrarySupport.singleton()::preregisterUninitializedBuiltinLibrary);
        stream(configuration.getSymbolPrefixes()).forEach(PlatformNativeLibrarySupport.singleton()::addBuiltinPkgNativePrefix);
        stream(configuration.getLibraryNames()).forEach(access.getNativeLibraries()::addStaticJniLibrary);
    }
}
