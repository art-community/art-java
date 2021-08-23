package io.art.core.graal;

import com.oracle.svm.core.jni.*;
import lombok.experimental.*;
import org.graalvm.nativeimage.hosted.*;
import java.lang.reflect.*;

@UtilityClass
public class GraalNativeRegistrator {
    public static void registerForNativeUsage(String... classes) {
        for (String name : classes) {
            try {
                registerForNativeUsage(Class.forName(name, false, GraalNativeRegistrator.class.getClassLoader()));
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
}
