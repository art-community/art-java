package io.art.core.graal.features;

import org.graalvm.nativeimage.*;
import org.junit.platform.launcher.*;
import org.junit.platform.launcher.listeners.*;
import java.io.*;

public class GraalTestLauncher {
    final Launcher launcher;
    final TestPlan testPlan;

    public GraalTestLauncher(Launcher launcher, TestPlan testPlan) {
        this.launcher = launcher;
        this.testPlan = testPlan;
    }

    public static void main(String... args) {
        if (!ImageInfo.inImageCode()) {
            System.err.println("NativeImageJUnitLauncher can only be used for native-image compiled tests.");
            System.exit(1);
        }

        PrintWriter out = new PrintWriter(System.out);
        GraalTestLauncher launcher = ImageSingletons.lookup(GraalTestLauncher.class);

        out.println("JUnit Platform on Native Image - report");
        out.println("----------------------------------------\n");
        out.flush();

        SummaryGeneratingListener summaryListener = new SummaryGeneratingListener();
        launcher.launcher.registerTestExecutionListeners(summaryListener);
        launcher.launcher.execute(launcher.testPlan);

        TestExecutionSummary summary = summaryListener.getSummary();
        summary.printFailuresTo(out);
        summary.printTo(out);

        long failedCount = summary.getTestsFailedCount() + summary.getTestsAbortedCount();
        System.exit((int) failedCount);
    }
}
