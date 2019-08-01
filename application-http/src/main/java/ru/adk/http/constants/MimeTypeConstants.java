package ru.adk.http.constants;

import lombok.NoArgsConstructor;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.constants.CharConstants.*;
import java.util.BitSet;

@NoArgsConstructor(access = PRIVATE)
public class MimeTypeConstants {
    public static final String PARAM_CHARSET = "charset";
    public static final String PARAM_Q = "q";
    public static final String WILDCARD_FULL_TYPE = "*/*";
    public static final String WILDCARD_WITH_ANY_STRING = "*+";
    public static final BitSet TOKEN;
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
