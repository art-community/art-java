package io.art.core.constants;

import io.art.core.annotation.*;

@ForGenerator
public interface CompilerSuppressingWarnings {
    String ALL = "all";
    String WARNINGS = "warnings";
    String OPTIONAL_USED_AS_FIELD = "OptionalUsedAsFieldOrParameterType";
    String UNCHECKED = "unchecked";
    String NULLABLE_PROBLEMS = "NullableProblems";
    String UNSTABLE_API_USAGE = "UnstableApiUsage";
    String CONSTANT_CONDITIONS = "ConstantConditions";
    String UNUSED = "unused";
    String RESULT_IGNORED = "ResultOfMethodCallIgnored";
}
