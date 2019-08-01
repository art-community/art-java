package ru.art.http.client.specification;

import ru.art.http.client.model.HttpCommunicationTargetConfiguration;
import ru.art.service.Specification;
import static ru.art.core.constants.StringConstants.COLON;
import static ru.art.core.constants.StringConstants.SCHEME_DELIMITER;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.http.client.constants.HttpClientModuleConstants.HTTP_COMMUNICATION_SERVICE_TYPE;
import static ru.art.http.client.module.HttpClientModule.httpClientModule;
import static ru.art.http.constants.HttpCommonConstants.HTTP_SCHEME;
import java.util.List;

public interface HttpCommunicationSpecification extends Specification {
    default HttpCommunicationTargetConfiguration communicationTarget(String serviceId) {
        return httpClientModule().getCommunicationTargetConfiguration(serviceId);
    }

    default String serviceId(String host, int port, String path) {
        return getScheme() + SCHEME_DELIMITER + host + COLON + port + path;
    }

    default String getScheme() {
        return HTTP_SCHEME;
    }

    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(HTTP_COMMUNICATION_SERVICE_TYPE);
    }
}
