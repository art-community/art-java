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

import io.art.core.collection.*;
import io.art.core.extensions.*;
import io.art.core.stream.*;
import io.art.value.immutable.*;
import io.art.xml.exception.*;
import lombok.*;
import lombok.experimental.*;
import org.apache.logging.log4j.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.BufferConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.logging.LoggingModule.*;
import static io.art.value.constants.ValueModuleConstants.ValueType.XmlValueType.*;
import static io.art.xml.constants.XmlDocumentConstants.*;
import static io.art.xml.module.XmlModule.*;
import static java.nio.ByteBuffer.*;
import static java.nio.charset.StandardCharsets.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import javax.xml.stream.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;

@UtilityClass
public class XmlWriter {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(XmlWriter.class);

    public static byte[] writeXmlToBytes(XmlEntity entity) throws XmlException {
        ByteBuffer byteBuffer = allocate(DEFAULT_BUFFER_SIZE);
        try {
            try (NioByteBufferOutputStream outputStream = new NioByteBufferOutputStream(byteBuffer)) {
                writeXml(entity, outputStream);
            } catch (IOException ioException) {
                throw new XmlException(ioException);
            }
            return NioBufferExtensions.toByteArray(byteBuffer);
        } finally {
            byteBuffer.clear();
        }
    }

    public static void writeXml(XmlEntity entity, Path path) throws XmlException {
        try (OutputStream outputStream = fileOutputStream(path)) {
            writeXml(entity, outputStream);
        } catch (IOException ioException) {
            throw new XmlException(ioException);
        }
    }

    public static String writeXml(XmlEntity entity) throws XmlException {
        return new String(writeXmlToBytes(entity), context().configuration().getCharset());
    }

    public static void writeXml(XmlEntity entity, OutputStream outputStream) throws XmlException {
        try {
            writeXml(xmlModule().configuration().getXmlOutputFactory(), entity, outputStream);
        } catch (Throwable throwable) {
            throw new XmlException(throwable);
        }
    }

    public static void writeXml(XMLOutputFactory outputFactory, XmlEntity entity, OutputStream outputStream) throws XmlException {
        if (isNull(entity)) return;
        XMLStreamWriter writer = null;
        try {
            writer = outputFactory.createXMLStreamWriter(outputStream, context().configuration().getCharset().name());
            writeAllElements(writer, entity);
        } catch (Throwable throwable) {
            throw new XmlException(throwable);
        } finally {
            if (nonNull(writer)) {
                try {
                    writer.close();
                } catch (Throwable throwable) {
                    getLogger().error(throwable.getMessage(), throwable);
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
        ImmutableArray<XmlEntity> children = entity.getChildren();

        if (isEmpty(entity.getTag())) {
            for (XmlEntity xmlEntity : children) {
                if (isNull(xmlEntity)) continue;
                writeXmlEntity(xmlStreamWriter, xmlEntity);
            }
            return;
        }

        writeStartElement(xmlStreamWriter, entity);
        writeNamespaces(xmlStreamWriter, entity);
        writeAttributes(xmlStreamWriter, entity);

        for (XmlEntity xmlEntity : children) {
            if (isNull(xmlEntity)) continue;
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

        if (isNotEmpty(prefix) && isNotEmpty(namespace)) {
            xmlStreamWriter.writeStartElement(prefix, entity.getTag(), namespace);
            return;
        }
        xmlStreamWriter.writeStartElement(entity.getTag());
    }

    private static void writeNamespaces(XMLStreamWriter xmlStreamWriter, XmlEntity entity) throws XMLStreamException {
        ImmutableMap<String, String> namespaces = entity.getNamespaces();
        for (Map.Entry<String, String> entry : namespaces.entrySet()) {
            xmlStreamWriter.writeNamespace(entry.getKey(), entry.getValue());
        }
    }

    private static void writeAttributes(XMLStreamWriter xmlStreamWriter, XmlEntity entity) throws XMLStreamException {
        ImmutableMap<String, String> attributes = entity.getAttributes();
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            xmlStreamWriter.writeAttribute(entry.getKey(), entry.getValue());
        }
    }

    private static void writeCharacters(XMLStreamWriter xmlStreamWriter, XmlEntity entity) throws XMLStreamException {
        String value = entity.getValue();
        if (nonNull(value)) {
            xmlStreamWriter.writeCharacters(value);
        }
    }

    private static void writeCData(XMLStreamWriter xmlStreamWriter, XmlEntity entity) throws XMLStreamException {
        XmlValue<?> xmlValue = entity.getXmlValue();
        if (CDATA.equals(xmlValue.getType())) {
            xmlStreamWriter.writeCData(writeXml((XmlEntity) xmlValue.getValue()));
        }
    }

    private static void writeEndElement(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        xmlStreamWriter.writeEndElement();
    }

    private static void writeStartDocument(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        xmlStreamWriter.writeStartDocument(UTF_8.name(), XML_VERSION);
    }

    private static void writeEndDocument(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        xmlStreamWriter.writeEndDocument();
    }
}
