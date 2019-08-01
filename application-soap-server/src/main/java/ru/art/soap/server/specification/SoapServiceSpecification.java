package ru.art.soap.server.specification;

import ru.art.service.Specification;
import ru.art.soap.server.model.SoapService;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.soap.server.constans.SoapServerModuleConstants.SOAP_SERVICE_TYPE;
import java.util.List;

public interface SoapServiceSpecification extends Specification {
    SoapService getSoapService();

    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(SOAP_SERVICE_TYPE);
    }
}
