package io.art.core.graal;

import lombok.experimental.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.GraalConstants.*;
import static io.art.core.constants.StringConstants.*;
import static java.lang.System.*;

@UtilityClass
public class GraalConfiguration {
    public static String GRAAL_WORKING_PATH = orElse(getProperty(GRAAL_WORKING_PATH_PROPERTY), EMPTY_STRING);
}
