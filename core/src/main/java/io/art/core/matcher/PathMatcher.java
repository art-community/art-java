package io.art.core.matcher;

import io.art.core.constants.*;
import lombok.*;
import static io.art.core.constants.CharacterConstants.WILDCARD;
import static io.art.core.constants.CharacterConstants.*;
import static io.art.core.constants.StringConstants.*;
import java.io.*;
import java.nio.file.*;

@Builder
public class PathMatcher {
    @Builder.Default
    private final char pathSeparator = File.separatorChar;
    private final boolean ignoreCase;
    private final boolean matchStart;
    private final boolean trimTokens;

    public static boolean matches(String pattern, String path) {
        return PathMatcher.builder().build().isMatch(pattern, path);
    }

    public static boolean matches(String pattern, File path) {
        return PathMatcher.builder().build().isMatch(pattern, path.getAbsolutePath());
    }

    public static boolean matches(String pattern, Path path) {
        return PathMatcher.builder().build().isMatch(pattern, path.toAbsolutePath().toString());
    }


    public boolean isMatch(String pattern, String path) {
        if (pattern.isEmpty()) {
            return path.isEmpty();
        }

        if (path.isEmpty() && pattern.charAt(0) == pathSeparator) {
            if (matchStart) {
                return true;
            }

            if (pattern.length() == 2 && pattern.charAt(1) == WILDCARD) {
                return false;
            }

            return isMatch(pattern.substring(1), path);
        }

        char patternStart = pattern.charAt(0);
        if (patternStart == WILDCARD) {
            if (pattern.length() == 1) {
                return path.isEmpty() || path.charAt(0) != pathSeparator && isMatch(pattern, path.substring(1));
            }

            if (pattern.charAt(1) == WILDCARD && doubleWildcardMatch(pattern, path)) {
                return true;
            }

            int start = 0;
            while (start < path.length()) {
                if (isMatch(pattern.substring(1), path.substring(start))) {
                    return true;
                }
                start++;
            }
            return isMatch(pattern.substring(1), path.substring(start));
        }

        int pointer = skipBlanks(path);

        if (path.isEmpty()) {
            return false;
        }

        if (!equals(path.charAt(pointer), patternStart) && patternStart != CharacterConstants.QUESTION) {
            return false;
        }

        return isMatch(pattern.substring(1), path.substring(pointer + 1));
    }

    private boolean doubleWildcardMatch(String pattern, String path) {
        if (pattern.length() > 2) {
            return isMatch(pattern.substring(3), path);
        }

        return false;
    }

    private int skipBlanks(String path) {
        int pointer = 0;
        if (trimTokens) {
            while (!path.isEmpty() && pointer < path.length() && path.charAt(pointer) == BLANK) {
                pointer++;
            }
        }
        return pointer;
    }

    private boolean equals(char pathChar, char patternChar) {
        if (ignoreCase) {
            return pathChar == patternChar ||
                    ((pathChar > patternChar) ?
                            pathChar == patternChar + ASCII_CASE_DIFFERENCE_VALUE :
                            pathChar == patternChar - ASCII_CASE_DIFFERENCE_VALUE);
        }
        return pathChar == patternChar;
    }
}
