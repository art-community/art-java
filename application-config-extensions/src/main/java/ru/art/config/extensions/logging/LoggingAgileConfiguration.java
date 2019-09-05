/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.config.extensions.logging;

import lombok.*;
import org.apache.logging.log4j.*;
import ru.art.logging.LoggingModuleConfiguration.*;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.config.extensions.logging.LoggingConfigKeys.*;
import static ru.art.core.extension.ExceptionExtensions.*;

@Getter
public class LoggingAgileConfiguration extends LoggingModuleDefaultConfiguration {
    private Level level;

    public LoggingAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        String levelString = configString(LOGGING_SECTION_ID, LEVEL);
        if (isEmpty(levelString)) {
            level = super.getLevel();
            return;
        }
        level = ifException(() -> Level.getLevel(levelString.toUpperCase()), super.getLevel());
        super.refresh();
    }
}
