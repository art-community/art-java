package io.art.core.graal;

import com.oracle.svm.core.jdk.NativeLibrarySupport;
import com.oracle.svm.core.jdk.PlatformNativeLibrarySupport;
import com.oracle.svm.core.jni.JNIRuntimeAccess;
import com.oracle.svm.hosted.FeatureImpl;
import com.oracle.svm.hosted.c.NativeLibraries;
import io.art.core.checker.NullityChecker;
import lombok.experimental.UtilityClass;
import org.graalvm.nativeimage.hosted.Feature.BeforeAnalysisAccess;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import static io.art.core.graal.GraalNativeLibraryConfiguration.Type.STATIC;

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

    public static void registerNativeLibraries(BeforeAnalysisAccess access, GraalNativeLibraryConfiguration... libraries) {
        NativeLibrarySupport nativeLibrarySupport = NativeLibrarySupport.singleton();
        PlatformNativeLibrarySupport platformNativeLibrarySupport = PlatformNativeLibrarySupport.singleton();
        NativeLibraries nativeLibraries = ((FeatureImpl.BeforeAnalysisAccessImpl) access).getNativeLibraries();
        Collection<String> libraryPaths = nativeLibraries.getLibraryPaths();
        for (GraalNativeLibraryConfiguration library : libraries) {
            libraryPaths.add(library.getLocation().resolve().toString());
            if (library.isBuiltin() && library.getType() == STATIC) {
                nativeLibrarySupport.preregisterUninitializedBuiltinLibrary(library.getName());
                NullityChecker.forEach(library.getBuiltinSymbolPrefixes(), platformNativeLibrarySupport::addBuiltinPkgNativePrefix);
                nativeLibraries.addStaticJniLibrary(library.getName());
                continue;
            }
            switch (library.getType()) {
                case DYNAMIC:
                    nativeLibraries.addDynamicNonJniLibrary(library.getName());
                    continue;
                case STATIC:
                    nativeLibraries.addStaticNonJniLibrary(library.getName());
                    break;
            }
        }
    }

    public static void registerStaticNonJniLibrary(BeforeAnalysisAccess access, String name) {
        NativeLibraries nativeLibraries = ((FeatureImpl.BeforeAnalysisAccessImpl) access).getNativeLibraries();
        nativeLibraries.addStaticNonJniLibrary(name);
    }
}
