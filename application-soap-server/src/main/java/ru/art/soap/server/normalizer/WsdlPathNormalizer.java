package ru.art.soap.server.normalizer;

import static ru.art.core.constants.CharConstants.SLASH;

public interface WsdlPathNormalizer {
    static String normalizeUrlPath(String path) {
        if (path.charAt(0) == SLASH) {
            return path.substring(1);
        }
        return path;
    }
}
