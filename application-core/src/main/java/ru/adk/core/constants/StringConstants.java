package ru.adk.core.constants;

import static java.util.regex.Pattern.compile;
import java.util.regex.Pattern;

public interface StringConstants {
    // @formatter:off
    String ADK_BANNER =
                    "\033[1;31m               AAA                    DDDDDDDDDDDDD             KKKKKKKKK    KKKKKKK\033[0m\n" +
                    "\033[0;32m              A:::A                   D::::::::::::DDD          K:::::::K    K:::::K\033[0m\n" +
                    "\033[1;33m             A:::::A                  D:::::::::::::::DD        K:::::::K    K:::::K\033[0m\n" +
                    "\033[0;34m            A:::::::A                 DDD:::::DDDDD:::::D       K:::::::K   K::::::K\033[0m\n" +
                    "\033[1;35m           A:::::::::A                  D:::::D    D:::::D      KK::::::K  K:::::KKK\033[0m\n" +
                    "\033[0;36m          A:::::A:::::A                 D:::::D     D:::::D       K:::::K K:::::K   \033[0m\n" +
                    "\033[1;37m         A:::::A A:::::A                D:::::D     D:::::D       K::::::K:::::K    \033[0m\n" +
                    "\033[1;31m        A:::::A   A:::::A               D:::::D     D:::::D       K:::::::::::K     \033[0m\n" +
                    "\033[0;32m       A:::::A     A:::::A              D:::::D     D:::::D       K:::::::::::K     \033[0m\n" +
                    "\033[1;33m      A:::::AAAAAAAAA:::::A             D:::::D     D:::::D       K::::::K:::::K    \033[0m\n" +
                    "\033[0;34m     A:::::::::::::::::::::A            D:::::D     D:::::D       K:::::K K:::::K   \033[0m\n" +
                    "\033[1;35m    A:::::AAAAAAAAAAAAA:::::A           D:::::D    D:::::D      KK::::::K  K:::::KKK\033[0m\n" +
                    "\033[0;36m   A:::::A             A:::::A        DDD:::::DDDDD:::::D       K:::::::K   K::::::K\033[0m\n" +
                    "\033[1;37m  A:::::A               A:::::A       D:::::::::::::::DD        K:::::::K    K:::::K\033[0m\n" +
                    "\033[0;31m A:::::A                 A:::::A      D::::::::::::DDD          K:::::::K    K:::::K\033[0m\n" +
                    "\033[1;32mAAAAAAA                   AAAAAAA     DDDDDDDDDDDDD             KKKKKKKKK    KKKKKKK\033[0m";
    String TRUE_NUMERIC = "1";
    String FALSE_NUMERIC = "0";
    String SLASH = "/";
    String DOUBLE_QUOTES = "\"";
    String WILDCARD = "*";
    String EMPTY_STRING = "";
    String SEMICOLON = ";";
    String COLON = ":";
    String DOT = ".";
    String EQUAL = "=";
    String NULL_STRING = "null";
    String SINGLE_QUOTE = "'";
    String COMMA = ",";
    String BRACKETS = "()";
    String BRACES = "{}";
    String SQUARE_BRACES = "[]";
    String UNDERSCORE = "_";
    String SPACE = " ";
    String PLUS = "+";
    String OPENING_BRACKET = "(";
    String CLOSING_BRACKET = ")";
    String OPENING_BRACES = "{";
    String CLOSING_BRACES = "}";
    String OPENING_SQUARE_BRACES = "[";
    String CLOSING_SQUARE_BRACES = "]";
    String AMPERSAND = "&";
    String SCHEME_DELIMITER = "://";
    String DASH = "-";
    String NEW_LINE = "\n";
    String LINE_DELIMITER = "\r\n";
    String ESCAPED_DOT = "\\.";
    String IP_ADDRESS_REGEX = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    Pattern IP_4_REGEX_PATTERN = compile(IP_ADDRESS_REGEX);
    String TABULATION = "\t";
    String DOUBLE_TABULATION = "\t\t";
    String DOLLAR = "$";
    String AT_SIGN = "@";
}
