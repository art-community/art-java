/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
