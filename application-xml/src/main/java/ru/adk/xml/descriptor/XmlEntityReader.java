package ru.adk.xml.descriptor;

import lombok.NoArgsConstructor;
import ru.adk.entity.XmlEntity;
import ru.adk.entity.XmlEntity.XmlEntityBuilder;
import ru.adk.xml.exception.XmlMappingException;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyMap;
import static java.util.Objects.isNull;
import static javax.xml.stream.XMLStreamConstants.*;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.extension.FileExtensions.readFile;
import static ru.adk.core.factory.CollectionsFactory.mapOf;
import static ru.adk.entity.XmlEntity.xmlEntityBuilder;
import static ru.adk.xml.constants.XmlMappingExceptionMessages.*;
import static ru.adk.xml.module.XmlModule.xmlModule;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;


@NoArgsConstructor(access = PRIVATE)
public class XmlEntityReader {
    public static XmlEntity readXml(XMLInputFactory xmlInputFactory, String xml) {
        if (isNull(xmlInputFactory)) throw new XmlMappingException(XML_FACTORY_IS_NULL);
        if (isEmpty(xml)) return xmlEntityBuilder().create();
        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes(UTF_8));
            XMLStreamReader parser = xmlInputFactory.createXMLStreamReader(is);
            XmlEntityBuilder root = getRootElement(parser);
            return root.create();
        } catch (Exception e) {
            throw new XmlMappingException(e);
        }
    }

    public static XmlEntity readXml(String xml) {
        return readXml(xmlModule().getXmlInputFactory(), xml);
    }

    public static XmlEntity readXml(Path path) {
        return readXml(readFile(path));
    }

    private static XmlEntityBuilder getRootElement(XMLStreamReader parser) throws XMLStreamException {
        if (parser.next() == START_ELEMENT) {
            String prefix = parser.getPrefix();
            Map<String, String> attributes = getAttributes(parser);
            Map<String, String> namespaces = getNamespaces(parser);
            XmlEntityBuilder rootElement = parseXml(parser);
            rootElement.stringAttributeFields(attributes);
            rootElement.namespaceFields(namespaces);
            rootElement.prefix(prefix);
            return rootElement;
        }
        throw new XmlMappingException(INCORRECT_XML_STRUCTURE);
    }

    private static XmlEntityBuilder parseXml(XMLStreamReader parser) throws XMLStreamException {
        XmlEntityBuilder xmlEntityBuilder = xmlEntityBuilder();

        int eventType;

        while (parser.hasNext()) {

            eventType = parser.next();

            if (parser.isWhiteSpace()) {
                continue;
            }

            switch (eventType) {
                case START_ELEMENT:
                    Map<String, String> attributes = getAttributes(parser);
                    Map<String, String> namespaces = getNamespaces(parser);
                    XmlEntityBuilder child = parseXml(parser);
                    child.stringAttributeFields(attributes);
                    child.namespaceFields(namespaces);
                    xmlEntityBuilder.child(child.create());
                    break;

                case CDATA:
                case SPACE:
                case CHARACTERS:
                    xmlEntityBuilder.value(parser.getText());
                    break;

                case END_ELEMENT:
                    xmlEntityBuilder.tag(parser.getLocalName());
                    xmlEntityBuilder.prefix(parser.getPrefix());
                    xmlEntityBuilder.namespace(parser.getNamespaceURI());
                    return xmlEntityBuilder;

                case END_DOCUMENT:
                    return xmlEntityBuilder;
            }
        }

        throw new XmlMappingException(XML_FILE_HASNT_END_DOCUMENT_TAG);
    }

    private static Map<String, String> getAttributes(XMLStreamReader parser) {
        int attributeCount = parser.getAttributeCount();
        if (attributeCount == 0) {
            return emptyMap();
        }

        Map<String, String> attributes = mapOf(attributeCount);
        for (int i = 0; i < attributeCount; i++) {
            attributes.put(parser.getAttributeLocalName(i), parser.getAttributeValue(i));
        }
        return attributes;
    }

    private static Map<String, String> getNamespaces(XMLStreamReader parser) {
        int namespaceCount = parser.getNamespaceCount();
        if (namespaceCount == 0) {
            return emptyMap();
        }

        Map<String, String> namespaces = mapOf(namespaceCount);
        for (int i = 0; i < namespaceCount; i++) {
            namespaces.put(parser.getNamespacePrefix(i), parser.getNamespaceURI(i));
        }
        return namespaces;
    }
}