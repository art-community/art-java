package ru.adk.http.server.specification;

import ru.adk.http.server.model.HttpService;
import ru.adk.service.Specification;
import static ru.adk.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HTTP_SERVICE_TYPE;
import java.util.List;

public interface HttpServiceSpecification extends Specification {
    HttpService getHttpService();

    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(HTTP_SERVICE_TYPE);
    }
}
