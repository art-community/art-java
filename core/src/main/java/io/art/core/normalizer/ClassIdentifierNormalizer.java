package io.art.core.normalizer;

import lombok.experimental.*;
import static io.art.core.constants.CharacterConstants.*;
import static io.art.core.constants.KnownClassNameSuffixes.*;
import static io.art.core.factory.SetFactory.*;
import static java.lang.Character.*;
import static java.util.Arrays.*;
import java.util.*;

@UtilityClass
public class ClassIdentifierNormalizer {
    public String idByDash(Class<?> owner) {
        return asId(owner, DASH);
    }

    public String idByDot(Class<?> owner) {
        return asId(owner, DOT);
    }

    public String asId(Class<?> owner, char delimiter) {
        String id = owner.getSimpleName();
        Set<String> knownSuffixes = setOf(
                PORTAL_CLASS_SUFFIX,
                SERVICE_CLASS_SUFFIX,
                COMMUNICATOR_CLASS_SUFFIX,
                SPACE_CLASS_SUFFIX,
                STORAGE_CLASS_SUFFIX
        );
        for (String suffix : knownSuffixes) {
            id = removeSuffix(id, suffix);
        }
        char[] normalized = new char[id.length() * 2];
        char[] current = id.toCharArray();
        normalized[0] = toLowerCase(current[0]);
        int count = 1;
        for (int index = 1; index < current.length; index++) {
            char character = current[index];
            if (isUpperCase(character) || isDigit(character)) {
                normalized[count] = delimiter;
                normalized[count + 1] = toLowerCase(character);
                count += 2;
                continue;
            }

            if (character == UNDERSCORE) {
                normalized[count] = delimiter;
                count++;
                continue;
            }

            normalized[count] = character;
            count++;
        }
        return new String(copyOf(normalized, count));
    }

    private String removeSuffix(String string, String suffix) {
        int suffixIndex = string.toLowerCase().lastIndexOf(suffix);
        if (suffixIndex == -1) {
            return string;
        }
        if (suffixIndex + suffix.length() >= string.length()) {
            return string.substring(0, suffixIndex);
        }
        return string.substring(0, suffixIndex) + string.substring(suffixIndex + suffix.length());
    }
}
