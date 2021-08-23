package io.art.core.graal;

import org.graalvm.nativeimage.hosted.*;
import static io.art.core.constants.GraalConstants.*;
import static io.art.core.graal.GraalNativeRegistrator.*;

public class GraalCoreFeatures implements Feature {
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        registerForNativeUsage(GRAAL_NATIVE_CORE_CLASSES);
    }
}
