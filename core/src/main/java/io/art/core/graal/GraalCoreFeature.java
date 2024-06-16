package io.art.core.graal;

import org.graalvm.nativeimage.hosted.Feature;

import static io.art.core.constants.GraalConstants.GRAAL_NATIVE_CORE_CLASSES;
import static io.art.core.graal.GraalNativeRegistrator.registerForNativeUsage;

public class GraalCoreFeature implements Feature {
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        registerForNativeUsage(GRAAL_NATIVE_CORE_CLASSES);
    }
}
