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

package ru.art.xml.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import ru.art.xml.configuration.XmlModuleConfiguration;
import ru.art.xml.configuration.XmlModuleConfiguration.XmlModuleDefaultConfiguration;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.context.Context.context;
import static ru.art.xml.constants.XmlModuleConstants.XML_MODULE_ID;

@Getter
public class XmlModule implements Module<XmlModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private final static XmlModuleConfiguration xmlModule = context().getModule(XML_MODULE_ID, XmlModule::new);
    private final String id = XML_MODULE_ID;
    private final XmlModuleConfiguration defaultConfiguration = new XmlModuleDefaultConfiguration();


    public static XmlModuleConfiguration xmlModule() {
        return getXmlModule();
    }
}
