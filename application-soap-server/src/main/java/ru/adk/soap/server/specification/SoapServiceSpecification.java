package ru.adk.soap.server.specification;

import ru.adk.service.Specification;
import ru.adk.soap.server.model.SoapService;
import static ru.adk.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.adk.soap.server.constans.SoapServerModuleConstants.SOAP_SERVICE_TYPE;
import java.util.List;

public interface SoapServiceSpecification extends Specification {
    SoapService getSoapService();

    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(SOAP_SERVICE_TYPE);
    }
}
