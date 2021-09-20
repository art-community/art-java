package io.art.tests.constants;

public interface TestsModuleConstants {
    interface Methods {
        String SETUP_METHOD_NAME = "setup";
        String CLEANUP_METHOD_NAME = "cleanup";
        String BEFORE_TEST_METHOD_NAME = "beforeTest";
        String AFTER_TEST_METHOD_NAME = "afterTest";
        String TEST_METHOD_PREFIX = "test";
    }

    interface Messages {
        String TEST_INVOCATION_MESSAGE = "Test {0}";
        String TEST_COMPLETED_MESSAGE = "Test {0} completed";
        String TEST_FAILED_MESSAGE = "Test {0} failed:\n{1}";
    }
}
