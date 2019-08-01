package ru.adk.http.server.parser;

import ru.adk.core.factory.CollectionsFactory.MapBuilder;
import ru.adk.entity.StringParametersMap;
import ru.adk.http.server.model.HttpService;
import static java.util.stream.Collectors.toMap;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.constants.StringConstants.SLASH;
import static ru.adk.core.factory.CollectionsFactory.mapOf;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

public interface HttpParametersParser {
    static StringParametersMap parseQueryParameters(HttpServletRequest request) {
        Map<String, String> parameters = request
                .getParameterMap()
                .keySet()
                .stream()
                .collect(toMap(name -> name, name -> request.getParameterValues(name)[0]));
        return StringParametersMap.builder()
                .parameters(parameters)
                .build();
    }

    static StringParametersMap parsePathParameters(HttpServletRequest request, HttpService.HttpMethod methodConfig) {
        String pathParameterString = request.getPathInfo();
        if (isEmpty(pathParameterString)) return StringParametersMap.builder().build();
        String[] pathParameterValues = pathParameterString.split(SLASH);
        if (isEmpty(pathParameterValues) || pathParameterValues.length == 1)
            return StringParametersMap.builder().build();
        Set<String> pathParameterNames = methodConfig.getPath().getPathParameters();
        int pathParameterIndex = 1;
        MapBuilder<String, String> pathParameters = mapOf();
        for (String name : pathParameterNames) {
            pathParameters.add(name, pathParameterValues[pathParameterIndex]);
            pathParameterIndex++;
        }
        return StringParametersMap.builder()
                .parameters(cast(pathParameters))
                .build();
    }
}

