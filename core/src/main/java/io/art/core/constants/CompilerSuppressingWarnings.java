package io.art.core.constants;

import io.art.core.annotation.*;

@ForGenerator
public interface CompilerSuppressingWarnings {
    String ALL = "all";
    String UNCHECKED = "unchecked";
    String NULLABLE_PROBLEMS = "NullableProblems";
    String UNSTABLE_API_USAGE = "UnstableApiUsage";
    String UNUSED_RETURN_VALUE = "UnusedReturnValue";
    String CONSTANT_CONDITIONS = "ConstantConditions";
    String UNUSED = "unused";
    String UNCHECKED_CAST = "UNCHECKED_CAST";
    String DEPRECATION = "deprecation";
    String IGNORE_RETURN_VALUE = "CanIgnoreReturnValue";
}
