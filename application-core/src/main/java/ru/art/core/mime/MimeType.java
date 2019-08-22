/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.core.mime;

import lombok.*;
import ru.art.core.exception.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;

import static java.lang.Float.*;
import static java.lang.String.*;
import static java.nio.charset.Charset.*;
import static java.text.MessageFormat.format;
import static java.util.Collections.*;
import static java.util.Locale.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.CharConstants.DOUBLE_QUOTES;
import static ru.art.core.constants.CharConstants.SEMICOLON;
import static ru.art.core.constants.ExceptionMessages.*;
import static ru.art.core.constants.MimeTypeConstants.*;
import static ru.art.core.constants.StringConstants.EQUAL;
import static ru.art.core.constants.StringConstants.PLUS;
import static ru.art.core.constants.StringConstants.SLASH;
import static ru.art.core.constants.StringConstants.WILDCARD;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.extension.NullCheckingExtensions.*;
import static ru.art.core.extension.StringExtensions.*;
import static ru.art.core.factory.CollectionsFactory.*;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MimeType implements Comparable<MimeType> {
    private final String type;
    private final String subtype;
    private final Map<String, String> parameters;

    public static MimeType mimeType(String type) {
        return mimeType(type, WILDCARD);
    }

    public static MimeType mimeType(String type, String subtype) {
        return mimeType(type, subtype, emptyMap());
    }

    public static MimeType mimeType(String type, String subtype, Charset charset) {
        return mimeType(type, subtype, mapOf(PARAM_CHARSET, charset.name()));
    }

    public static MimeType mimeType(String type, String subtype, Map<String, String> parameters) {
        if (isEmpty(type)) throw new InvalidMimeTypeException(type, MIME_TYPE_MUST_NOT_BE_EMPTY);
        if (isEmpty(subtype))
            throw new InvalidMimeTypeException(subtype, MIME_SUBTYPE_MUST_NOT_BE_EMPTY);
        checkToken(type);
        checkToken(subtype);
        type = type.toLowerCase(ENGLISH);
        subtype = subtype.toLowerCase(ENGLISH);
        if (!isEmpty(parameters)) {
            Map<String, String> map = mapOf(parameters.size());
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String attribute = entry.getKey();
                String value = entry.getValue();
                checkParameters(attribute, value);
                map.put(attribute.toLowerCase(), value.toLowerCase());
            }
            return new MimeType(type, subtype, map);
        }
        return new MimeType(type, subtype, mapOf());
    }

    public static MimeType valueOf(String value) {
        if (isEmpty(value)) {
            throw new InvalidMimeTypeException(value, MIME_TYPE_MUST_NOT_BE_EMPTY);
        }

        int index = value.indexOf(SEMICOLON);
        String fullType = (index >= 0 ? value.substring(0, index) : value).trim();
        if (fullType.isEmpty()) {
            throw new InvalidMimeTypeException(value, MIME_TYPE_MUST_NOT_BE_EMPTY);
        }

        if (WILDCARD.equals(fullType)) {
            fullType = WILDCARD_FULL_TYPE;
        }
        int subIndex = fullType.indexOf(SLASH);
        if (subIndex == -1) {
            throw new InvalidMimeTypeException(value, MIME_DOES_NOT_CONTAIN_SLASH);
        }
        if (subIndex == fullType.length() - 1) {
            throw new InvalidMimeTypeException(value, MIME_DOES_NOT_CONTAIN_SUBTYPE);
        }
        String type = fullType.substring(0, subIndex);
        String subtype = fullType.substring(subIndex + 1);
        if (WILDCARD.equals(type) && !WILDCARD.equals(subtype)) {
            throw new InvalidMimeTypeException(value, WILDCARD_TYPE_IS_LEGAL_ONLY_FOR_ALL_MIME_TYPES);
        }

        Map<String, String> parameters = null;
        do {
            int nextIndex = index + 1;
            boolean quoted = false;
            while (nextIndex < value.length()) {
                char ch = value.charAt(nextIndex);
                if (ch == SEMICOLON) {
                    if (!quoted) {
                        break;
                    }
                } else if (ch == DOUBLE_QUOTES) {
                    quoted = !quoted;
                }
                nextIndex++;
            }
            String parameter = value.substring(index + 1, nextIndex).trim();
            if (parameter.length() > 0) {
                if (isNull(parameters)) {
                    parameters = mapOf(4);
                }
                int eqIndex = parameter.indexOf(EQUAL);
                if (eqIndex >= 0) {
                    String attribute = parameter.substring(0, eqIndex);
                    String value1 = parameter.substring(eqIndex + 1);
                    parameters.put(attribute, value1);
                }
            }
            index = nextIndex;
        }
        while (index < value.length());
        return mimeType(type, subtype, parameters);
    }

    private static void checkToken(String token) {
        for (int i = 0; i < token.length(); i++) {
            char ch = token.charAt(i);
            if (!TOKEN.get(ch)) {
                throw new IllegalArgumentException(format(INVALID_TOKEN, ch, token));
            }
        }
    }

    private static void checkParameters(String attribute, String value) {
        if (isEmpty(attribute)) {
            throw new InvalidMimeTypeException(attribute, PARAMETER_ATTRIBUTE_MUST_NOT_BE_EMPTY);
        }
        if (isEmpty(value)) {
            throw new InvalidMimeTypeException(value, PARAMETER_VALUE_MUST_NOT_BE_EMPTY);
        }
        checkToken(attribute);
        if (PARAM_CHARSET.equals(attribute)) {
            value = unquote(value);
            forName(value);
        }
        if (!isQuotedString(value)) {
            checkToken(value);
        }
    }

    public boolean isWildcardType() {
        return WILDCARD.equals(type);
    }

    public boolean isWildcardSubtype() {
        return !WILDCARD.equals(subtype) && !subtype.startsWith(WILDCARD_WITH_ANY_STRING);
    }

    public Charset getCharset() {
        return doIfNotNull(getParameter(PARAM_CHARSET), (Function<String, Charset>) charset -> forName(unquote(charset)));
    }

    public float getQValue() {
        String qStr = getParameter(PARAM_Q);
        return qStr != null ? parseFloat(unquote(qStr)) : 1.f;
    }

    public String getParameter(String name) {
        return this.parameters.get(name);
    }

    public boolean isConcrete() {
        return !isWildcardType() && isWildcardSubtype();
    }

    public boolean includes(MimeType other) {
        if (isNull(other)) {
            return false;
        }
        if (this.isWildcardType()) return true;
        if (!type.equals(other.type)) {
            return false;
        }
        if (subtype.equals(other.subtype)) {
            return true;
        }
        if (this.isWildcardSubtype()) {
            return false;
        }
        int thisPlusIdx = subtype.indexOf(PLUS);
        if (thisPlusIdx == -1) {
            return true;
        }
        int otherPlusIdx = other.subtype.indexOf(PLUS);
        if (otherPlusIdx == -1) {
            return false;
        }
        String thisSubtypeNoSuffix = subtype.substring(0, thisPlusIdx);
        String thisSubtypeSuffix = subtype.substring(thisPlusIdx + 1);
        String otherSubtypeSuffix = other.subtype.substring(otherPlusIdx + 1);
        return thisSubtypeSuffix.equals(otherSubtypeSuffix) && WILDCARD.equals(thisSubtypeNoSuffix);
    }

    public boolean isCompatibleWith(MimeType other) {
        if (isNull(other)) {
            return false;
        }
        if (isWildcardType() || other.isWildcardType()) {
            return true;
        }
        if (!type.equals(other.type)) {
            return false;
        }
        if (subtype.equals(other.subtype)) {
            return true;
        }
        if (this.isWildcardSubtype() && other.isWildcardSubtype()) {
            return false;
        }

        int thisPlusIdx = subtype.indexOf(PLUS);
        int otherPlusIdx = other.subtype.indexOf(PLUS);

        if (thisPlusIdx == -1 && otherPlusIdx == -1) {
            return true;
        }
        if (thisPlusIdx != -1 && otherPlusIdx != -1) {
            String thisSubtypeNoSuffix = subtype.substring(0, thisPlusIdx);
            String otherSubtypeNoSuffix = other.subtype.substring(0, otherPlusIdx);

            String thisSubtypeSuffix = subtype.substring(thisPlusIdx + 1);
            String otherSubtypeSuffix = other.subtype.substring(otherPlusIdx + 1);

            return thisSubtypeSuffix.equals(otherSubtypeSuffix) && (WILDCARD.equals(thisSubtypeNoSuffix) || WILDCARD.equals(otherSubtypeNoSuffix));
        }
        return false;
    }

    public MimeType withParameter(String name, String value) {
        checkParameters(name, value);
        parameters.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        int result = this.type.hashCode();
        result = 31 * result + this.subtype.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        appendTo(builder);
        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MimeType)) {
            return false;
        }
        MimeType otherType = (MimeType) other;
        return (this.type.equalsIgnoreCase(otherType.type) && this.subtype.equalsIgnoreCase(otherType.subtype));
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") MimeType other) {
        if (isNull(other)) return -1;
        int comp = type.compareToIgnoreCase(other.type);
        if (comp != 0) {
            return comp;
        }
        comp = subtype.compareToIgnoreCase(other.subtype);
        if (comp != 0) {
            return comp;
        }
        comp = parameters.size() - other.parameters.size();
        if (comp != 0) {
            return comp;
        }
        TreeSet<String> thisAttributes = new TreeSet<>(CASE_INSENSITIVE_ORDER);
        thisAttributes.addAll(parameters.keySet());
        TreeSet<String> otherAttributes = new TreeSet<>(CASE_INSENSITIVE_ORDER);
        otherAttributes.addAll(other.parameters.keySet());
        Iterator<String> thisAttributesIterator = thisAttributes.iterator();
        Iterator<String> otherAttributesIterator = otherAttributes.iterator();
        while (thisAttributesIterator.hasNext()) {
            String thisAttribute = thisAttributesIterator.next();
            String otherAttribute = otherAttributesIterator.next();
            comp = thisAttribute.compareToIgnoreCase(otherAttribute);
            if (comp != 0) {
                return comp;
            }
            String thisValue = parameters.get(thisAttribute);
            String otherValue = other.parameters.get(otherAttribute);
            if (isNull(otherValue)) {
                otherValue = EMPTY_STRING;
            }
            comp = thisValue.compareTo(otherValue);
            if (comp != 0) {
                return comp;
            }
        }
        return 0;
    }

    private boolean parametersAreEqual(MimeType other) {
        if (this.parameters.size() != other.parameters.size()) {
            return false;
        }

        for (String key : this.parameters.keySet()) {
            if (!other.parameters.containsKey(key)) {
                return false;
            }

            if (PARAM_CHARSET.equals(key) && !Objects.equals(getCharset(), other.getCharset())) {
                return false;
            }
            if (!Objects.equals(this.parameters.get(key), other.parameters.get(key))) {
                return false;
            }
        }

        return true;
    }

    private void appendTo(StringBuilder builder) {
        builder.append(this.type);
        builder.append(SLASH);
        builder.append(this.subtype);
        appendTo(this.parameters, builder);
    }

    private void appendTo(Map<String, String> map, StringBuilder builder) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.append(SEMICOLON);
            builder.append(entry.getKey());
            builder.append(EQUAL);
            builder.append(entry.getValue());
        }
    }
}