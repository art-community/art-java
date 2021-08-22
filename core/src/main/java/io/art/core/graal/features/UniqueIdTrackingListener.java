package io.art.core.graal.features;

import org.junit.platform.engine.*;
import org.junit.platform.launcher.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;

public class UniqueIdTrackingListener implements TestExecutionListener {
    public static final String OUTPUT_DIRECTORY_PROPERTY_NAME = "native.test.directory";
    public static final String OUTPUT_FILE_PREFIX_PROPERTY_NAME = "native.test.prefix";
    public static final String DEFAULT_OUTPUT_FILE_PREFIX = "junit-platform-unique-ids";

    private final List<String> uniqueIds = dynamicArray();

    @Override
    public void executionSkipped(TestIdentifier testIdentifier, String reason) {
        trackTestUid(testIdentifier);
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        trackTestUid(testIdentifier);
    }

    private void trackTestUid(TestIdentifier testIdentifier) {
        if (testIdentifier.isTest()) {
            this.uniqueIds.add(testIdentifier.getUniqueId());
        }
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        Path outputFile;
        try {
            outputFile = getOutputFile();
        } catch (IOException ex) {
            return;
        }

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8))) {
            this.uniqueIds.forEach(writer::println);
            writer.flush();
        } catch (IOException ignored) {
        }
    }

    private Path getOutputFile() throws IOException {
        String prefix = System.getProperty(OUTPUT_FILE_PREFIX_PROPERTY_NAME, DEFAULT_OUTPUT_FILE_PREFIX);
        String filename = String.format("%s-%d.txt", prefix, Math.abs(new SecureRandom().nextLong()));
        Path outputFile = getOutputDirectory().resolve(filename);

        if (Files.exists(outputFile)) {
            Files.delete(outputFile);
        }

        Files.createFile(outputFile);

        return outputFile;
    }

    Path getOutputDirectory() {
        Path outputDir = Paths.get(System.getProperty(OUTPUT_DIRECTORY_PROPERTY_NAME));

        touchDirectory(outputDir);

        return outputDir;
    }
}
