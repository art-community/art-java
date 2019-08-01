package ru.adk.soap.client.specification;

import ru.adk.http.client.specification.HttpCommunicationSpecification;
import static ru.adk.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.adk.soap.client.constants.SoapClientModuleConstants.SOAP_COMMUNICATION_SERVICE_TYPE;
import java.util.List;

public interface SoapCommunicationSpecification extends HttpCommunicationSpecification {
    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(SOAP_COMMUNICATION_SERVICE_TYPE);
    }
}
