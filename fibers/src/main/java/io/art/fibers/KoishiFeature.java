package io.art.fibers;

import com.oracle.svm.core.annotate.*;
import com.oracle.svm.hosted.*;
import com.oracle.svm.hosted.c.*;
import org.graalvm.nativeimage.hosted.*;

@AutomaticFeature
public class KoishiFeature implements Feature {
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        NativeLibraries nativeLibraries = ((FeatureImpl.BeforeAnalysisAccessImpl) access).getNativeLibraries();
        nativeLibraries.addStaticNonJniLibrary("koishi");
    }
}