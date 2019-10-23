package ru.art.core.constants;

import static java.util.regex.Pattern.compile;
import java.util.regex.*;

public interface ReflectionConstants {
    String WRITE_REPLACE = "writeReplace";
    Pattern SIGNATURE_REGEX = compile("\\(L(.+);\\).+");
    String CLASS_PREFIX = "L";
}
