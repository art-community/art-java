package ru.art.generator.spec.common.operations;

import ru.art.generator.spec.common.constants.SpecificationType;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.generator.spec.common.constants.CommonSpecGeneratorConstants.PathAndPackageConstants.SPEC;
import static ru.art.generator.spec.common.constants.CommonSpecGeneratorConstants.REGEXP_LOWER_CASE_LETTERS;
import static ru.art.generator.spec.common.constants.CommonSpecGeneratorConstants.REGEXP_UPPER_CASE_LETTERS;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface contains operations which analyses data.
 */
public interface IdCalculator {

    /**
     * Method gets specification's id in upper case by service's name and
     * specification's type.
     * Example:
     * Service's name - "ExampleService".
     * Specification's type - httpProxySpec.
     * Service's id = "EXAMPLE_SERVICE_HTTP_PROXY_SPEC".
     *
     * @param serviceName - name of service.
     * @param specType    - generating type of specification.
     * @return specification's id.
     */
    static String getSpecificationId(String serviceName, SpecificationType specType) {
        if (specType == null || isEmpty(serviceName)) return EMPTY_STRING;
        String specTypeStartedWithUpperCaseLetter = specType.getName().replace(String.valueOf(specType.getName().charAt(0)), specType.getName().substring(0, 1).toUpperCase());
        List<Character> parsedServiceName = parseStringToCharArray(serviceName + specTypeStartedWithUpperCaseLetter);
        StringBuilder serviceId = new StringBuilder();
        for (int i = 0; i < parsedServiceName.size(); i++) {
            if (parsedServiceName.get(i).toString().matches(REGEXP_UPPER_CASE_LETTERS) && (i != 0)) {
                serviceId.append(UNDERSCORE);
                serviceId.append(parsedServiceName.get(i));
            } else serviceId.append(parsedServiceName.get(i).toString());
        }
        return serviceId.append(UNDERSCORE).append(SPEC).toString().toUpperCase();
    }

    /**
     * Method parse string to array,  containing chars.
     *
     * @param string - string to parse.
     * @return ArrayList of Characters.
     */
    static List<Character> parseStringToCharArray(String string) {
        List<Character> stringToChar = new ArrayList<>();
        for (int i = 0; i < string.length(); i++) {
            stringToChar.add(string.charAt(i));
        }
        return stringToChar;
    }

    /**
     * Gets service method's id in upper case by string.
     * Example:
     * String - "/MethodPath".
     * Method's id = "METHOD_PATH".
     *
     * @param string - string to transform.
     * @return method's id.
     */
    static String getMethodId(String string) {
        if (isEmpty(string)) return EMPTY_STRING;
        List<Character> parsedString = parseStringToCharArray(string);
        StringBuilder methodId = new StringBuilder();
        for (int i = 0; i < parsedString.size(); i++) {
            if ((i == 0) && SLASH.equals(parsedString.get(i).toString())) continue;
            if (parsedString.get(i).toString().matches(REGEXP_UPPER_CASE_LETTERS)
                    && ((i != parsedString.size() - 1) && (parsedString.get(i + 1).toString().matches(REGEXP_LOWER_CASE_LETTERS)))
                    && (i != 0)) {
                methodId.append(UNDERSCORE);
                methodId.append(parsedString.get(i));
            } else methodId.append(parsedString.get(i).toString());
        }
        return methodId.toString().toUpperCase();
    }
}
