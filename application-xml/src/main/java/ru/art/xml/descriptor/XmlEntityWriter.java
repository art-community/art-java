package ru.art.xml.descriptor;

import lombok.NoArgsConstructor;
import ru.art.entity.XmlEntity;
import ru.art.entity.XmlValue;
import ru.art.entity.constants.ValueType;
import ru.art.xml.exception.XmlMappingException;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.extension.FileExtensions.writeFileQuietly;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.xml.constants.XmlDocumentConstants.XML_VERSION;
import static ru.art.xml.constants.XmlLoggingMessages.XML_GENERATOR_CLOSING_ERROR;
import static ru.art.xml.constants.XmlMappingExceptionMessages.XML_FACTORY_IS_NULL;
import static ru.art.xml.module.XmlModule.xmlModule;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = PRIVATE)
public class XmlEntityWriter {
    public static String writeXml(XmlEntity xmlEntity) throws XmlMappingException {
        return writeXml(xmlModule().getXmlOutputFactory(), xmlEntity);
    }

    public static void writeXml(XmlEntity xmlEntity, Path path) throws XmlMappingException {
        writeFileQuietly(path, writeXml(xmlModule().getXmlOutputFactory(), xmlEntity));
    }

    public static String writeXml(XMLOutputFactory xmlOutputFactory, XmlEntity xmlEntity) throws XmlMappingException {
        if (isNull(xmlOutputFactory)) throw new XmlMappingException(XML_FACTORY_IS_NULL);
        if (isNull(xmlEntity)) return EMPTY_STRING;
        XMLStreamWriter xmlStreamWriter = null;
        try {
            OutputStream os = new ByteArrayOutputStream();
            xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(os, UTF_8.name());
            writeAllElements(xmlStreamWriter, xmlEntity);
            return os.toString();
        } catch (Exception e) {
            throw new XmlMappingException(e);
        } finally {
            if (nonNull(xmlStreamWriter)) {
                try {
                    xmlStreamWriter.flush();
                    xmlStreamWriter.close();
                } catch (Exception e) {
                    loggingModule().getLogger(XmlEntityWriter.class).error(XML_GENERATOR_CLOSING_ERROR, e);
                }
            }
        }
    }


    private static void writeAllElements(XMLStreamWriter xmlStreamWriter, XmlEntity xmlEntity) throws XMLStreamException {
        writeStartDocument(xmlStreamWriter);
        writeXmlEntity(xmlStreamWriter, xmlEntity);
        writeEndDocument(xmlStreamWriter);
    }

    private static void writeXmlEntity(XMLStreamWriter xmlStreamWriter, XmlEntity entity) throws XMLStreamException {
        //gather all child elements
        List<XmlEntity> children = entity.getChildren();
        if (isEmpty(entity.getTag())) {
            for (XmlEntity xmlEntity : children) {
                if (isEmpty(xmlEntity)) continue;
                writeXmlEntity(xmlStreamWriter, xmlEntity);
            }
            return;
        }

        writeStartElement(xmlStreamWriter, entity);

        writeNamespaces(xmlStreamWriter, entity);
        writeAttributes(xmlStreamWriter, entity);

        //gather elements sequence
        for (XmlEntity xmlEntity : children) {
            if (isEmpty(xmlEntity)) continue;
            writeXmlEntity(xmlStreamWriter, xmlEntity);
        }
        writeValue(xmlStreamWriter, entity);


        writeEndElement(xmlStreamWriter);
    }

    private static void writeValue(XMLStreamWriter xmlStreamWriter, XmlEntity entity) throws XMLStreamException {
        if (entity.isCData()) {
            writeCData(xmlStreamWriter, entity);
            return;
        }
        writeCharacters(xmlStreamWriter, entity);
    }

    private static void writeStartElement(XMLStreamWriter xmlStreamWriter, XmlEntity entity) throws XMLStreamException {
        String namespace = entity.getNamespace();
        String prefix = entity.getPrefix();

        xmlStreamWriter.writeCharacters(NEW_LINE);
        if (!isEmpty(prefix) && !isEmpty(namespace)) {
            xmlStreamWriter.writeStartElement(prefix, entity.getTag(), namespace);
            return;
        }
        xmlStreamWriter.writeStartElement(entity.getTag());
    }

    private static void writeNamespaces(XMLStreamWriter xmlStreamWriter, XmlEntity entity) throws XMLStreamException {
        Map<String, String> namespaces = entity.getNamespaces();
        for (Map.Entry<String, String> entry : namespaces.entrySet()) {
            xmlStreamWriter.writeNamespace(entry.getKey(), entry.getValue());
        }
    }

    private static void writeAttributes(XMLStreamWriter xmlStreamWriter, XmlEntity entity) throws XMLStreamException {
        Map<String, String> attributes = entity.getAttributes();
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            xmlStreamWriter.writeAttribute(entry.getKey(), entry.getValue());
        }
    }

    private static void writeCharacters(XMLStreamWriter xmlStreamWriter, XmlEntity entity) throws XMLStreamException {
        String value = entity.getValue();
        if (!isEmpty(value)) {
            xmlStreamWriter.writeCharacters(value);
        }
    }

    private static void writeCData(XMLStreamWriter xmlStreamWriter, XmlEntity entity) throws XMLStreamException {
        XmlValue<?> xmlValue = entity.getXmlValue();
        if (ValueType.XmlValueType.CDATA.equals(xmlValue.getType())) {
            String cDataValue = writeXml(xmlModule().getXmlOutputFactory(), (XmlEntity) xmlValue.getValue());

            if (!isEmpty(xmlValue)) {
                xmlStreamWriter.writeCData(cDataValue);
            }
        }
    }

    private static void writeEndElement(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        xmlStreamWriter.writeEndElement();
    }

    private static void writeStartDocument(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        xmlStreamWriter.writeStartDocument(UTF_8.name(), XML_VERSION);
        xmlStreamWriter.writeCharacters(LINE_DELIMITER);
    }

    private static void writeEndDocument(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        xmlStreamWriter.writeCharacters(LINE_DELIMITER);
        xmlStreamWriter.writeEndDocument();
    }
}