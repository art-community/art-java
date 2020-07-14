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

package io.art.core.configuration;

import lombok.*;
import io.art.core.context.*;
import io.art.core.network.provider.*;
import io.art.core.provider.*;
import static java.nio.charset.StandardCharsets.*;
import static java.util.Locale.Category.*;
import static java.util.Locale.*;
import static java.util.Optional.*;
import static io.art.core.constants.ContextConstants.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.security.*;
import java.util.*;

public interface ContextInitialConfiguration {
    String getMainModuleId();

    String getModuleJarName();

    Charset getCharset();

    boolean isUnloadModulesOnShutdown();

    String getIpAddress();

    Locale getLocale();

    PreconfiguredModuleProvider getPreconfiguredModulesProvider();

    @Getter
    @NoArgsConstructor
    class ContextInitialDefaultConfiguration implements ContextInitialConfiguration {
        private final Charset charset = UTF_8;
        private final boolean unloadModulesOnShutdown = true;
        private final String mainModuleId = DEFAULT_MAIN_MODULE_ID;
        private final String moduleJarName = DEFAULT_MODULE_JAR;
        private final String ipAddress = IpAddressProvider.getIpAddress();
        private final Locale locale = getDefault(FORMAT);
        private PreconfiguredModuleProvider preconfiguredModulesProvider;
    }

    @Getter
    @RequiredArgsConstructor
    class ApplicationContextConfiguration implements ContextInitialConfiguration {
        private final String mainModuleId;
        private final Charset charset = UTF_8;
        private final boolean unloadModulesOnShutdown = true;
        private final String ipAddress = IpAddressProvider.getIpAddress();
        private final Locale locale = getDefault(FORMAT);
        private final String moduleJarName = ofNullable(Context.class.getProtectionDomain())
                .map(ProtectionDomain::getCodeSource)
                .map(CodeSource::getLocation)
                .map(URL::getPath)
                .map(File::new)
                .map(File::getPath)
                .orElse(DEFAULT_MODULE_JAR);
        private PreconfiguredModuleProvider preconfiguredModulesProvider;
        public ApplicationContextConfiguration(String applicationModuleId, PreconfiguredModuleProvider preconfiguredModulesProvider) {
            this.mainModuleId = applicationModuleId;
            this.preconfiguredModulesProvider = preconfiguredModulesProvider;
        }
    }
}
