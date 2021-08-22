package io.art.core.graal.features;

import io.art.core.extensions.*;
import org.graalvm.nativeimage.*;
import org.graalvm.nativeimage.hosted.*;
import org.junit.platform.console.*;
import org.junit.platform.engine.*;
import org.junit.platform.engine.discovery.*;
import org.junit.platform.engine.support.descriptor.*;
import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static java.nio.file.Files.*;
import static java.util.Objects.*;
import static java.util.stream.Stream.*;
import static org.junit.platform.engine.discovery.DiscoverySelectors.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@SuppressWarnings(ALL)
public final class GraalTestFeature implements Feature {
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        RuntimeClassInitialization.initializeAtBuildTime(ConsoleLauncher.class);

        List<Path> classpathRoots = access.getApplicationClassPath();
        List<? extends DiscoverySelector> selectors = getSelectors(classpathRoots);

        Launcher launcher = LauncherFactory.create();
        TestPlan testplan = registerTestClasses(launcher, selectors);
        ImageSingletons.add(GraalJUnitLauncher.class, new GraalJUnitLauncher(launcher, testplan));
    }

    private List<? extends DiscoverySelector> getSelectors(List<Path> classpathRoots) {
        Path output = Paths.get(System.getProperty("graal.test.directory"));
        String prefix = System.getProperty("graal.test.prefix");
        List<UniqueIdSelector> selectors = readAllFiles(output, prefix)
                .map(DiscoverySelectors::selectUniqueId)
                .collect(Collectors.toList());
        if (!selectors.isEmpty()) {
            return selectors;
        }

        return selectClasspathRoots(new HashSet<>(classpathRoots));
    }

    private TestPlan registerTestClasses(Launcher launcher, List<? extends DiscoverySelector> selectors) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectors)
                .build();

        TestPlan testPlan = launcher.discover(request);

        testPlan.getRoots().stream()
                .flatMap(rootIdentifier -> testPlan.getDescendants(rootIdentifier).stream())
                .map(TestIdentifier::getSource)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(ClassSource.class::isInstance)
                .map(ClassSource.class::cast)
                .map(ClassSource::getJavaClass)
                .forEach(this::registerTestClass);

        return testPlan;
    }

    private void registerTestClass(Class<?> testClass) {
        Class<?> superClass = testClass.getSuperclass();
        if (nonNull(superClass) && superClass != Object.class) {
            registerTestClass(superClass);
        }
    }


    private Stream<String> readAllFiles(Path directory, String prefix) {
        return findFiles(directory, prefix)
                .map(FileExtensions::readFileQuietly)
                .flatMap(string -> Arrays.stream(string.split(NEW_LINE)));
    }

    private static Stream<Path> findFiles(Path directory, String prefix) {
        if (!exists(directory)) return empty();
        BiPredicate<Path, BasicFileAttributes> predicate = (path, basicFileAttributes) -> (basicFileAttributes.isRegularFile() && path.getFileName()
                .toString()
                .startsWith(prefix));
        return wrapExceptionCall(() -> find(directory, Integer.MAX_VALUE, predicate));
    }

}
