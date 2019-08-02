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

package ru.art.http.constants;


import ru.art.http.mime.MimeType;
import static java.nio.charset.Charset.forName;
import static ru.art.core.constants.StringConstants.WILDCARD;
import static ru.art.http.mime.MimeType.mimeType;

public interface HttpMimeTypes {
    MimeType ALL = mimeType(WILDCARD, WILDCARD);
    MimeType APPLICATION_ATOM_XML = mimeType("application", "atom+xml");
    MimeType APPLICATION_SOAP_XML = mimeType("application", "soap+xml");
    MimeType APPLICATION_FORM_URLENCODED = mimeType("application", "x-www-form-urlencoded");
    MimeType APPLICATION_JSON = mimeType("application", "json");
    MimeType APPLICATION_JSON_UTF8 = mimeType("application", "json", forName("UTF-8"));
    MimeType APPLICATION_JSON_WIN_1251 = mimeType("application", "json", forName("windows-1251"));
    MimeType APPLICATION_OCTET_STREAM = mimeType("application", "octet-stream");
    MimeType APPLICATION_OCTET_STREAM_UTF_8 = mimeType("application", "octet-stream", forName("UTF-8"));
    MimeType APPLICATION_PDF = mimeType("application", "pdf");
    MimeType APPLICATION_RSS_XML = mimeType("application", "rss+xml");
    MimeType APPLICATION_XHTML_XML = mimeType("application", "xhtml+xml");
    MimeType APPLICATION_XML = mimeType("application", "xml");
    MimeType IMAGE_GIF = mimeType("image", "gif");
    MimeType IMAGE_JPEG = mimeType("image", "jpeg");
    MimeType IMAGE_PNG = mimeType("image", "png");
    MimeType IMAGE_WEBP = mimeType("image", "webp");
    MimeType MULTIPART_FORM_DATA = mimeType("multipart", "form-data");
    MimeType TEXT_EVENT_STREAM = mimeType("text", "event-stream");
    MimeType TEXT_HTML = mimeType("text", "html");
    MimeType TEXT_HTML_UTF_8 = mimeType("text", "html", forName("UTF-8"));
    MimeType TEXT_HTML_WIN_1251 = mimeType("text", "html", forName("windows-1251"));
    MimeType TEXT_MARKDOWN = mimeType("text", "markdown");
    MimeType TEXT_PLAIN = mimeType("text", "plain");
    MimeType TEXT_XML = mimeType("text", "xml");
    MimeType TEXT_XML_WIN_1251 = mimeType("text", "xml", forName("windows-1251"));
    MimeType TEXT_CSV = mimeType("text", "csv");
    MimeType TEXT_CSV_UTF_8 = mimeType("text", "csv", forName("UTF-8"));
    MimeType TEXT_XML_UTF_8 = mimeType("text", "xml", forName("UTF-8"));
    MimeType TEXT_CSS = mimeType("text", "css");
    MimeType TEXT_JS = mimeType("text", "javascript");
}
