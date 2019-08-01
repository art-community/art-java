package ru.art.http.server.path;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import ru.art.core.constants.StringConstants;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.CharConstants.SLASH;
import static ru.art.core.constants.StringConstants.COLON;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import java.util.Iterator;
import java.util.Set;


@Getter
@Builder
public class HttpPath {
    private final String contextPath;
    @Singular
    private final Set<String> pathParameters;

    @Override
    public String toString() {
        return contextPath + buildPathParams();
    }

    private String buildPathParams() {
        if (isEmpty(pathParameters)) return EMPTY_STRING;
        StringBuilder path = new StringBuilder(StringConstants.SLASH);
        Iterator<String> parametersIt = pathParameters.iterator();
        while (parametersIt.hasNext()) {
            path.append(COLON).append(parametersIt.next());
            if (parametersIt.hasNext()) {
                path.append(SLASH);
            }
        }
        return path.toString();
    }
}
