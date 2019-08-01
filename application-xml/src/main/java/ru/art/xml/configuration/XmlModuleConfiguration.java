package ru.art.xml.configuration;

import lombok.Getter;
import ru.art.core.module.ModuleConfiguration;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

public interface XmlModuleConfiguration extends ModuleConfiguration {
    XMLInputFactory getXmlInputFactory();

    XMLOutputFactory getXmlOutputFactory();

    @Getter
    class XmlModuleDefaultConfiguration implements XmlModuleConfiguration {
        private final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        private final XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
    }
}
