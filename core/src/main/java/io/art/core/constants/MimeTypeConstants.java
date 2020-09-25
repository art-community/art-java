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

package io.art.core.constants;

import lombok.*;
import static io.art.core.constants.CharacterConstants.*;
import static lombok.AccessLevel.*;
import java.util.*;

@NoArgsConstructor(access = PRIVATE)
public class MimeTypeConstants {
    public static final String PARAM_CHARSET = "charset";
    public static final String PARAM_Q = "q";
    public static final String WILDCARD_FULL_TYPE = "*/*";
    public static final String WILDCARD_WITH_ANY_STRING = "*+";
    public static final BitSet TOKEN;
    public static final String INVALID_MIME_TYPE_MESSAGE = "{0}. Value: ''{1}''";
    private static final int TOKEN_SIZE = 128;

    static {
        BitSet ctl = new BitSet(TOKEN_SIZE);
        for (int i = 0; i <= 31; i++) {
            ctl.set(i);
        }
        ctl.set(TOKEN_SIZE - 1);

        BitSet separators = new BitSet(TOKEN_SIZE);
        separators.set(OPENING_BRACKET);
        separators.set(CLOSING_BRACKET);
        separators.set(LESS);
        separators.set(MORE);
        separators.set(AT);
        separators.set(COMMA);
        separators.set(SEMICOLON);
        separators.set(COLON);
        separators.set(BACK_SLASH);
        separators.set(TWO_QUOTES);
        separators.set(SLASH);
        separators.set(OPENING_SQUARE_BRACKET);
        separators.set(CLOSING_SQUARE_BRACKET);
        separators.set(QUESTION);
        separators.set(EQUAL);
        separators.set(OPENING_BRACES);
        separators.set(CLOSING_BRACES);
        separators.set(SPACE);
        separators.set(TABULATION);

        TOKEN = new BitSet(TOKEN_SIZE);
        TOKEN.set(0, TOKEN_SIZE);
        TOKEN.andNot(ctl);
        TOKEN.andNot(separators);
    }
}
