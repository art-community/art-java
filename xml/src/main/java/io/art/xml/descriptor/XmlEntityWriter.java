/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.xml.descriptor;

import io.art.entity.constants.*;
import io.art.entity.immutable.*;
import io.art.xml.exception.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.ArrayConstants.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.logging.LoggingModule.*;
import static io.art.xml.constants.XmlDocumentConstants.*;
import static io.art.xml.constants.XmlLoggingMessages.*;
import static io.art.xml.module.XmlModule.*;
import static java.nio.charset.StandardCharsets.*;
import static java.util.Objects.*;
import javax.xml.stream.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@UtilityClass
public class XmlEntityWriter {
    public static byte[] writeXmlToBytes(XmlEntity entity) throws XmlMappingException {
        String xml = writeXml(xmlModule().configuration().getXmlOutputFactory(), entity);
        if (isNull(xml) || isEmpty(xml)) return EMPTY_BYTES;
        return xml.getBytes(contextConfiguration().getCharset());
    }

    public static void writeXml(XmlEntity entity, OutputStream outputStream) throws XmlMappingException {
        try {
            String xml = writeXml(xmlModule().configuration().getXmlOutputFactory(), entity);
            if (isNull(xml) || isEmpty(xml)) return;
            outputStream.write(xml.getBytes());
        } catch (Throwable throwable) {
            throw new XmlMappingException(throwable);
        }
    }

    public static void writeXml(XmlEntity entity, Path path) throws XmlMappingException {
        String xml = writeXml(xmlModule().configuration().getXmlOutputFactory(), entity);
        if (isNull(xml) || isEmpty(xml)) return;
        writeFileQuietly(path, xml);
    }

    public static String writeXml(XmlEntity entity) throws XmlMappingException {
        return writeXml(xmlModule().configuration().getXmlOutputFactory(), entity);
    }

    public static String writeXml(XMLOutputFactory outputFactory, XmlEntity entity) throws XmlMappingException {
        if (isNull(entity)) return null;
        XMLStreamWriter xmlStreamWriter = null;
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            xmlStreamWriter = outputFactory.createXMLStreamWriter(outputStream, UTF_8.name());
            writeAllElements(xmlStreamWriter, entity);
            return outputStream.toString();
        } catch (Throwable throwable) {
            throw new XmlMappingException(throwable);
        } finally {
            if (nonNull(xmlStreamWriter)) {
                try {
                    xmlStreamWriter.flush();
                    xmlStreamWriter.close();
                } catch (Throwable throwable) {
                    loggingModule().configuration().getLogger(XmlEntityWriter.class).error(XML_GENERATOR_CLOSING_ERROR, throwable);
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
        List<XmlEntity> children = entity.getChildren();

        if (isEmpty(children) && isEmpty(entity.getValue())) return;

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
            String cDataValue = writeXml(xmlModule().configuration().getXmlOutputFactory(), (XmlEntity) xmlValue.getValue());

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
