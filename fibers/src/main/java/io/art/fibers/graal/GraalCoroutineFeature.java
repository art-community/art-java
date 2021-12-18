package io.art.fibers.graal;

import com.oracle.svm.core.annotate.*;
import com.oracle.svm.hosted.*;
import com.oracle.svm.hosted.c.*;
import org.graalvm.nativeimage.hosted.*;
import static io.art.fibers.constants.FiberConstants.GraalConstants.*;

@AutomaticFeature
public class GraalCoroutineFeature implements Feature {
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        NativeLibraries nativeLibraries = ((FeatureImpl.BeforeAnalysisAccessImpl) access).getNativeLibraries();
        nativeLibraries.addStaticNonJniLibrary(COROUTINE_LIBRARY_NAME);
    }
}
