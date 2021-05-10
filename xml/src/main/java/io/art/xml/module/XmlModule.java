/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.xml.module;

import io.art.core.module.*;
import io.art.xml.configuration.*;
import lombok.*;
import static io.art.core.context.Context.*;
import static lombok.AccessLevel.*;

@Getter
public class XmlModule implements StatelessModule<XmlModuleConfiguration, XmlModuleConfiguration.Configurator> {
    @Getter(lazy = true, value = PRIVATE)
    private static final StatelessModuleProxy<XmlModuleConfiguration> xmlModule = context().getStatelessModule(XmlModule.class.getSimpleName());
    private final String id = XmlModule.class.getSimpleName();
    private final XmlModuleConfiguration configuration = new XmlModuleConfiguration();
    private final XmlModuleConfiguration.Configurator configurator = new XmlModuleConfiguration.Configurator(configuration);

    static {
        registerDefault(XmlModule.class.getSimpleName(), XmlModule::new);
    }

    public static StatelessModuleProxy<XmlModuleConfiguration> xmlModule() {
        return getXmlModule();
    }
}
