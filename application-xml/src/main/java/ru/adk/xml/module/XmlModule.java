package ru.adk.xml.module;

import lombok.Getter;
import ru.adk.core.module.Module;
import ru.adk.core.module.ModuleState;
import ru.adk.xml.configuration.XmlModuleConfiguration;
import ru.adk.xml.configuration.XmlModuleConfiguration.XmlModuleDefaultConfiguration;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.context.Context.context;
import static ru.adk.xml.constants.XmlModuleConstants.XML_MODULE_ID;

@Getter
public class XmlModule implements Module<XmlModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private final static XmlModuleConfiguration xmlModule = context().getModule(XML_MODULE_ID, new XmlModule());
    @Getter
    private final XmlModuleConfiguration defaultConfiguration = new XmlModuleDefaultConfiguration();

    private final String id = XML_MODULE_ID;

    public static XmlModuleConfiguration xmlModule() {
        return getXmlModule();
    }
}
