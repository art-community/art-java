package io.art.meta.validator;

import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.meta.constants.MetaConstants.Errors.*;
import static java.text.MessageFormat.*;

@UtilityClass
public class MetaClassValidator {
    public static ValidationResult validate(MetaClass<?> metaClass) {
        if (!metaClass.creator().isValid()) {
            return new ValidationResult(false, format(CLASS_CREATOR_INVALID, metaClass));
        }
        return new ValidationResult(true, EMPTY_STRING);
    }
}
