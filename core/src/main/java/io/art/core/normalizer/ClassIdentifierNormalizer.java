package io.art.core.normalizer;

import lombok.experimental.*;
import static io.art.core.constants.CharacterConstants.*;
import static io.art.core.constants.TransportConstants.*;
import static io.art.core.factory.SetFactory.*;
import static java.lang.Character.*;
import static java.util.Arrays.*;

@UtilityClass
public class ClassIdentifierNormalizer {
    public String asId(Class<?> owner) {
        String id = owner.getSimpleName();
        for (String suffix : setOf(CONNECTOR_CLASS_SUFFIX, SERVICE_CLASS_SUFFIX, COMMUNICATOR_CLASS_SUFFIX)) {
            id = removeSuffix(id, suffix);
        }
        char[] normalized = new char[id.length() * 2];
        char[] current = id.toCharArray();
        normalized[0] = toLowerCase(current[0]);
        int count = 1;
        for (int index = 1; index < current.length; index++) {
            char character = current[index];
            if (isUpperCase(character) || isDigit(character)) {
                normalized[count] = DASH;
                normalized[count + 1] = toLowerCase(character);
                count += 2;
                continue;
            }

            if (character == UNDERSCORE) {
                normalized[count] = DASH;
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