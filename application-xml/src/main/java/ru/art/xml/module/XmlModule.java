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
    private final static XmlModuleConfiguration xmlModule = context().getModule(XML_MODULE_ID, new XmlModule());
    @Getter
    private final XmlModuleConfiguration defaultConfiguration = new XmlModuleDefaultConfiguration();

    private final String id = XML_MODULE_ID;

    public static XmlModuleConfiguration xmlModule() {
        return getXmlModule();
    }
}
