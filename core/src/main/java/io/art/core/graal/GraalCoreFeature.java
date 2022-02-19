package io.art.core.graal;

import com.oracle.svm.core.annotate.*;
import org.graalvm.nativeimage.hosted.*;
import static io.art.core.constants.GraalConstants.*;
import static io.art.core.graal.GraalNativeRegistrator.*;

@AutomaticFeature
public class GraalCoreFeature implements Feature {
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        registerForNativeUsage(GRAAL_NATIVE_CORE_CLASSES);
    }
}
