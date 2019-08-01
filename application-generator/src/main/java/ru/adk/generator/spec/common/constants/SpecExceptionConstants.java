package ru.adk.generator.spec.common.constants;

/**
 * Interface for constants for exceptions occurred in all specification's generators.
 */
public interface SpecExceptionConstants {

    interface SpecificationGeneratorExceptions {
        String UNABLE_TO_GENERATE_SPECIFICATION = "Unable to generate specification for class ''{0}'' because of the ''{1}''.";
        String MAIN_ANNOTATION_ABSENT = "Annotation @{0} is not present for class ''{1}''. Specification was not generated.";
        String SERVICE_MARKED_IS_NON_GENERATED = "Service ''{0}'' is marked with @NonGenerated annotation and will not be generated.";
    }

    interface DefinitionExceptions {
        String UNABLE_TO_DEFINE_METHOD = "Unable to define method ''{0}'' in class ''{1}''.";
        String UNABLE_TO_DEFINE_ANNOTATION = "Unable to define annotation @{0}.";
        String UNABLE_TO_DEFINE_SPECIFICATION_TYPE = "Unable to define specification type ''{0}''.";
        String UNABLE_TO_DEFINE_NECESSITY_OF_IMPORT = "Unable to define if import is necessary because of exception: ";
    }
}
