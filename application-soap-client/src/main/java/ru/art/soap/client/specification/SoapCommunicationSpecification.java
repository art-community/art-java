package ru.art.soap.client.specification;

import ru.art.http.client.specification.HttpCommunicationSpecification;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.soap.client.constants.SoapClientModuleConstants.SOAP_COMMUNICATION_SERVICE_TYPE;
import java.util.List;

public interface SoapCommunicationSpecification extends HttpCommunicationSpecification {
    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(SOAP_COMMUNICATION_SERVICE_TYPE);
    }
}
