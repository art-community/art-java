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

import io.art.entity.immutable.*;
import lombok.experimental.*;
import io.art.entity.immutable.XmlEntity.*;
import io.art.xml.exception.*;
import static java.nio.charset.StandardCharsets.*;
import static java.util.Collections.*;
import static javax.xml.stream.XMLStreamConstants.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.core.extensions.InputStreamExtensions.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.entity.immutable.XmlEntity.*;
import static io.art.xml.constants.XmlMappingExceptionMessages.*;
import static io.art.xml.module.XmlModule.*;
import javax.xml.stream.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@UtilityClass
public class XmlEntityReader {
    public static XmlEntity readXml(byte[] bytes) {
        return readXml(new String(bytes, contextConfiguration().getCharset()));
    }

    public static XmlEntity readXml(InputStream inputStream) {
        return readXml(toByteArray(inputStream));
    }

    public static XmlEntity readXml(Path path) {
        return readXml(readFile(path));
    }

    public static XmlEntity readXml(String xml) {
        return readXml(xmlModule().getXmlInputFactory(), xml);
    }

    public static XmlEntity readXml(XMLInputFactory xmlInputFactory, String xml) {
        if (isEmpty(xml)) return xmlEntityBuilder().create();
        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes(UTF_8));
            XMLStreamReader parser = xmlInputFactory.createXMLStreamReader(is);
            XmlEntityBuilder root = getRootElement(parser);
            return root.create();
        } catch (Throwable throwable) {
            throw new XmlMappingException(throwable);
        }
    }


    private static XmlEntityBuilder getRootElement(XMLStreamReader parser) throws XMLStreamException {
        if (parser.next() == START_ELEMENT) {
            String prefix = parser.getPrefix();
            Map<String, String> attributes = getAttributes(parser);
            Map<String, String> namespaces = getNamespaces(parser);
            XmlEntityBuilder rootElement = parseXml(parser);
            rootElement.stringAttributes(attributes);
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
                    child.stringAttributes(attributes);
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

        throw new XmlMappingException(XML_FILE_HAS_NOT_END_DOCUMENT_TAG);
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
