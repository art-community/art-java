package ru.art.http.server.specification;

import ru.art.http.server.model.HttpService;
import ru.art.service.Specification;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.http.server.constants.HttpServerModuleConstants.HTTP_SERVICE_TYPE;
import java.util.List;

public interface HttpServiceSpecification extends Specification {
    HttpService getHttpService();

    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(HTTP_SERVICE_TYPE);
    }
}
