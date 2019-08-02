/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.generator.spec.common.mapping;

import ru.art.generator.spec.http.common.exception.MimeTypeDefinitionException;
import ru.art.generator.spec.http.proxyspec.constants.MimeTypes;
import ru.art.http.constants.MimeToContentTypeMapper;
import ru.art.http.mime.MimeType;
import static java.text.MessageFormat.format;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.generator.spec.http.proxyspec.constants.HttpProxySpecConstants.ErrorConstants.UNABLE_TO_DEFINE_MIME_TYPE;
import static ru.art.generator.spec.http.proxyspec.constants.HttpProxySpecConstants.ErrorConstants.UNABLE_TO_DEFINE_MIME_TYPE_METHOD;
import static ru.art.http.constants.HttpMimeTypes.*;

/**
 * Interface containing methods for mimeType's mappings.
 */
public interface MimeTypeToContentTypeMethodMapping {

    /**
     * Define method from {@link MimeToContentTypeMapper}
     * which corresponds to certain MimeType by MimeType's constant name.
     *
     * @param mimeType - string value of MimeType constant.
     * @return string value for method's name from {@link MimeToContentTypeMapper}.
     * @throws MimeTypeDefinitionException is thrown when unable to define MimeType or method for MimeType.
     */
    static String getMimeTypeToContentMethod(String mimeType) throws MimeTypeDefinitionException {
        if (isEmpty(mimeType))
            throw new MimeTypeDefinitionException(format(UNABLE_TO_DEFINE_MIME_TYPE_METHOD, mimeType));

        if (getMimeTypeName(ALL).equals(mimeType)) return MimeTypes.ALL.getMethod();
        if (getMimeTypeName(APPLICATION_ATOM_XML).equals(mimeType)) return MimeTypes.APPLICATION_ATOM_XML.getMethod();
        if (getMimeTypeName(APPLICATION_FORM_URLENCODED).equals(mimeType))
            return MimeTypes.APPLICATION_FORM_URLENCODED.getMethod();
        if (getMimeTypeName(APPLICATION_JSON).equals(mimeType)) return MimeTypes.APPLICATION_JSON.getMethod();
        if (getMimeTypeName(APPLICATION_JSON_UTF8).equals(mimeType)) return MimeTypes.APPLICATION_JSON_UTF8.getMethod();
        if (getMimeTypeName(APPLICATION_OCTET_STREAM).equals(mimeType))
            return MimeTypes.APPLICATION_OCTET_STREAM.getMethod();
        if (getMimeTypeName(APPLICATION_XHTML_XML).equals(mimeType)) return MimeTypes.APPLICATION_XHTML_XML.getMethod();
        if (getMimeTypeName(APPLICATION_XML).equals(mimeType)) return MimeTypes.APPLICATION_XML.getMethod();
        if (getMimeTypeName(IMAGE_GIF).equals(mimeType)) return MimeTypes.IMAGE_GIF.getMethod();
        if (getMimeTypeName(IMAGE_JPEG).equals(mimeType)) return MimeTypes.IMAGE_JPEG.getMethod();
        if (getMimeTypeName(IMAGE_PNG).equals(mimeType)) return MimeTypes.IMAGE_PNG.getMethod();
        if (getMimeTypeName(MULTIPART_FORM_DATA).equals(mimeType)) return MimeTypes.MULTIPART_FORM_DATA.getMethod();
        if (getMimeTypeName(TEXT_HTML).equals(mimeType)) return MimeTypes.TEXT_HTML.getMethod();
        if (getMimeTypeName(TEXT_PLAIN).equals(mimeType)) return MimeTypes.TEXT_PLAIN.getMethod();
        if (getMimeTypeName(TEXT_XML).equals(mimeType)) return MimeTypes.TEXT_XML.getMethod();
        if (getMimeTypeName(APPLICATION_SOAP_XML).equals(mimeType)) return MimeTypes.APPLICATION_SOAP_XML.getMethod();
        if (getMimeTypeName(APPLICATION_JSON_WIN_1251).equals(mimeType))
            return MimeTypes.APPLICATION_JSON_WIN_1251.getMethod();
        if (getMimeTypeName(APPLICATION_OCTET_STREAM_UTF_8).equals(mimeType))
            return MimeTypes.APPLICATION_OCTET_STREAM_UTF_8.getMethod();
        if (getMimeTypeName(IMAGE_WEBP).equals(mimeType)) return MimeTypes.IMAGE_WEBP.getMethod();
        if (getMimeTypeName(TEXT_XML_UTF_8).equals(mimeType)) return MimeTypes.TEXT_XML_UTF_8.getMethod();
        if (getMimeTypeName(TEXT_HTML_UTF_8).equals(mimeType)) return MimeTypes.TEXT_HTML_UTF_8.getMethod();
        if (getMimeTypeName(APPLICATION_PDF).equals(mimeType)) return MimeTypes.APPLICATION_PDF.getMethod();
        if (getMimeTypeName(APPLICATION_RSS_XML).equals(mimeType)) return MimeTypes.APPLICATION_RSS_XML.getMethod();
        if (getMimeTypeName(TEXT_CSV).equals(mimeType)) return MimeTypes.TEXT_CSV.getMethod();
        if (getMimeTypeName(TEXT_CSV_UTF_8).equals(mimeType)) return MimeTypes.TEXT_CSV_UTF_8.getMethod();
        if (getMimeTypeName(TEXT_CSS).equals(mimeType)) return MimeTypes.TEXT_CSS.getMethod();
        if (getMimeTypeName(TEXT_JS).equals(mimeType)) return MimeTypes.TEXT_JS.getMethod();
        if (getMimeTypeName(TEXT_EVENT_STREAM).equals(mimeType)) return MimeTypes.TEXT_EVENT_STREAM.getMethod();
        if (getMimeTypeName(TEXT_MARKDOWN).equals(mimeType)) return MimeTypes.TEXT_MARKDOWN.getMethod();
        if (getMimeTypeName(TEXT_XML_WIN_1251).equals(mimeType)) return MimeTypes.TEXT_XML_WIN_1251.getMethod();

        throw new MimeTypeDefinitionException(format(UNABLE_TO_DEFINE_MIME_TYPE_METHOD, mimeType));
    }

    /**
     * Define MimeType constant's name by {@link MimeType}.
     *
     * @param mimeType - MimeType to find.
     * @return string value for MimeType constant's name.
     * @throws MimeTypeDefinitionException is thrown when unable to define MimeType.
     */
    static String getMimeTypeName(MimeType mimeType) throws MimeTypeDefinitionException {
        if (isEmpty(mimeType)) throw new MimeTypeDefinitionException(format(UNABLE_TO_DEFINE_MIME_TYPE, mimeType));

        if (ALL.equals(mimeType)) return MimeTypes.ALL.toString();
        if (APPLICATION_ATOM_XML.equals(mimeType)) return MimeTypes.APPLICATION_ATOM_XML.toString();
        if (APPLICATION_FORM_URLENCODED.equals(mimeType)) return MimeTypes.APPLICATION_FORM_URLENCODED.toString();
        if (APPLICATION_JSON.equals(mimeType) && isEmpty(mimeType.getCharset()))
            return MimeTypes.APPLICATION_JSON.toString();
        if (APPLICATION_JSON_UTF8.equals(mimeType) && isNotEmpty(mimeType.getCharset())
                && mimeType.getCharset().equals(APPLICATION_JSON_UTF8.getCharset()))
            return MimeTypes.APPLICATION_JSON_UTF8.toString();
        if (APPLICATION_OCTET_STREAM.equals(mimeType)) return MimeTypes.APPLICATION_OCTET_STREAM.toString();
        if (APPLICATION_XHTML_XML.equals(mimeType)) return MimeTypes.APPLICATION_XHTML_XML.toString();
        if (APPLICATION_XML.equals(mimeType)) return MimeTypes.APPLICATION_XML.toString();
        if (IMAGE_GIF.equals(mimeType)) return MimeTypes.IMAGE_GIF.toString();
        if (IMAGE_JPEG.equals(mimeType)) return MimeTypes.IMAGE_JPEG.toString();
        if (IMAGE_PNG.equals(mimeType)) return MimeTypes.IMAGE_PNG.toString();
        if (MULTIPART_FORM_DATA.equals(mimeType)) return MimeTypes.MULTIPART_FORM_DATA.toString();
        if (TEXT_HTML.equals(mimeType) && isEmpty(mimeType.getCharset())) return MimeTypes.TEXT_HTML.toString();
        if (TEXT_PLAIN.equals(mimeType)) return MimeTypes.TEXT_PLAIN.toString();
        if (TEXT_XML.equals(mimeType) && isEmpty(mimeType.getCharset())) return MimeTypes.TEXT_XML.toString();
        if (APPLICATION_SOAP_XML.equals(mimeType)) return MimeTypes.APPLICATION_SOAP_XML.toString();
        if (APPLICATION_JSON_WIN_1251.equals(mimeType) && isNotEmpty(mimeType.getCharset())
                && mimeType.getCharset().equals(APPLICATION_JSON_WIN_1251.getCharset()))
            return MimeTypes.APPLICATION_JSON_WIN_1251.toString();
        if (APPLICATION_OCTET_STREAM_UTF_8.equals(mimeType)) return MimeTypes.APPLICATION_OCTET_STREAM_UTF_8.toString();
        if (IMAGE_WEBP.equals(mimeType)) return MimeTypes.IMAGE_WEBP.toString();
        if (TEXT_XML_UTF_8.equals(mimeType) && isNotEmpty(mimeType.getCharset())
                && mimeType.getCharset().equals(TEXT_XML_UTF_8.getCharset()))
            return MimeTypes.TEXT_XML_UTF_8.toString();
        if (TEXT_HTML_UTF_8.equals(mimeType) && isNotEmpty(mimeType.getCharset())
                && mimeType.getCharset().equals(TEXT_HTML_UTF_8.getCharset()))
            return MimeTypes.TEXT_HTML_UTF_8.toString();
        if (APPLICATION_PDF.equals(mimeType)) return MimeTypes.APPLICATION_PDF.toString();
        if (APPLICATION_RSS_XML.equals(mimeType)) return MimeTypes.APPLICATION_RSS_XML.toString();
        if (TEXT_CSV.equals(mimeType) && isEmpty(mimeType.getCharset())) return MimeTypes.TEXT_CSV.toString();
        if (TEXT_CSV_UTF_8.equals(mimeType) && isNotEmpty(mimeType.getCharset())
                && mimeType.getCharset().equals(TEXT_CSV_UTF_8.getCharset()))
            return MimeTypes.TEXT_CSV_UTF_8.toString();
        if (TEXT_CSS.equals(mimeType)) return MimeTypes.TEXT_CSS.toString();
        if (TEXT_JS.equals(mimeType)) return MimeTypes.TEXT_JS.toString();
        if (TEXT_EVENT_STREAM.equals(mimeType)) return MimeTypes.TEXT_EVENT_STREAM.toString();
        if (TEXT_MARKDOWN.equals(mimeType)) return MimeTypes.TEXT_MARKDOWN.toString();
        if (TEXT_XML_WIN_1251.equals(mimeType) && isNotEmpty(mimeType.getCharset())
                && mimeType.getCharset().equals(TEXT_XML_WIN_1251.getCharset()))
            return MimeTypes.TEXT_XML_WIN_1251.toString();

        throw new MimeTypeDefinitionException(format(UNABLE_TO_DEFINE_MIME_TYPE, mimeType));
    }
}
