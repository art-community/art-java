package io.art.core.constants;

import io.art.core.annotation.*;

@Generation
public interface CompilerSuppressingWarnings {
    String ALL = "all";
    String WARNINGS = "warnings";
    String OPTIONAL_USED_AS_FIELD = "OptionalUsedAsFieldOrParameterType";
    String UNCHECKED = "unchecked";
    String NULLABLE_PROBLEMS = "NullableProblems";
    String UNSTABLE_API_USAGE = "UnstableApiUsage";
    String CONSTANT_CONDITIONS = "ConstantConditions";
    String UNUSED = "unused";
    String DEPRECATION = "deprecation";
    String RESULT_IGNORED = "ResultOfMethodCallIgnored";
    String FINAL_FIELD = "FieldMayBeFinal";
    String CALLING_SUBSCRIBE_IN_NON_BLOCKING_SCOPE = "CallingSubscribeInNonBlockingScope";
}
