package ru.art.configurator.api.entity;

import lombok.*;
import ru.art.configurator.api.exception.ModuleKeyParsingException;
import static java.lang.String.join;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.core.constants.StringConstants.COLON;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ModuleKey {
    String profileId;
    String moduleId;

    public static ModuleKey parseKey(String key) {
        String[] keyParts = key.split(COLON);
        if (isEmpty(keyParts)) throw new ModuleKeyParsingException(key);
        ModuleKeyBuilder builder = ModuleKey.builder();
        builder.profileId = keyParts[0];
        if (keyParts.length > 1) {
            builder.moduleId = keyParts[1];
        }
        return builder.build();
    }

    public String formatKey() {
        if (isNotEmpty(profileId) && isNotEmpty(moduleId)) {
            return join(COLON, profileId, moduleId);
        }

        if (isNotEmpty(profileId)) {
            return profileId;
        }

        return EMPTY_STRING;
    }
}
