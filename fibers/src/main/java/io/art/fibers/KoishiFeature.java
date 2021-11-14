package io.art.fibers;

import io.art.core.graal.*;
import org.graalvm.nativeimage.hosted.*;
import static io.art.core.graal.GraalNativeLibraryConfiguration.Type.*;

public class KoishiFeature implements Feature {
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        GraalNativeRegistrator.registerNativeLibraries(access, GraalNativeLibraryConfiguration.builder()
                .name("koishi")
                .type(STATIC)
                .location(GraalNativeLibraryConfiguration.GraalNativeLibraryLocation.builder()
                        .extractionDirectory("koishi-library")
                        .include(".+koishi\\.h")
                        .library(".+koishi\\.a")
                        .build())
                .build());
    }
}
