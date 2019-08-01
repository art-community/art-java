package ru.adk.soap.client.communicator;

import lombok.NoArgsConstructor;
import ru.adk.entity.XmlEntity;
import ru.adk.soap.client.exception.SoapClientModuleException;
import static lombok.AccessLevel.PACKAGE;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.entity.XmlEntity.xmlEntityBuilder;
import static ru.adk.soap.client.constants.SoapClientModuleConstants.SOAP_BODY_TAG;
import static ru.adk.soap.client.constants.SoapClientModuleConstants.SOAP_ENVELOPE_TAG;
import static ru.adk.soap.client.constants.SoapClientModuleExceptionMessages.SOAP_RESPONSE_HAS_NOT_BODY;
import static ru.adk.soap.client.constants.SoapClientModuleExceptionMessages.SOAP_RESPONSE_HAS_NOT_ENVELOPE;
import java.util.List;

@NoArgsConstructor(access = PACKAGE)
class SoapEnvelopWrappingManager {
    static XmlEntity wrapToSoapEnvelop(XmlEntity xmlEntity, SoapCommunicationConfiguration proxyConfig) {
        String operationPrefix = proxyConfig.getOperationPrefix();
        String operationNamespace = proxyConfig.getOperationNamespace();
        XmlEntity operation = xmlEntityBuilder()
                .tag(proxyConfig.getOperationId())
                .prefix(operationPrefix)
                .namespace(operationNamespace)
                .namespaceField(operationPrefix, operationNamespace)
                .child(xmlEntity)
                .create();
        XmlEntity body = xmlEntityBuilder()
                .tag(SOAP_BODY_TAG)
                .prefix(proxyConfig.getBodyPrefix())
                .namespace(proxyConfig.getBodyNamespace())
                .namespaceField(proxyConfig.getBodyPrefix(), proxyConfig.getBodyNamespace())
                .child(operation)
                .create();
        return xmlEntityBuilder()
                .tag(SOAP_ENVELOPE_TAG)
                .prefix(proxyConfig.getEnvelopePrefix())
                .namespace(proxyConfig.getEnvelopeNamespace())
                .namespaceField(proxyConfig.getEnvelopePrefix(), proxyConfig.getEnvelopeNamespace())
                .child(body)
                .create();
    }

    static XmlEntity unwrapFromSoapEnvelope(XmlEntity xmlEntity) {
        if (isEmpty(xmlEntity) || !SOAP_ENVELOPE_TAG.equalsIgnoreCase(xmlEntity.getTag())) {
            throw new SoapClientModuleException(SOAP_RESPONSE_HAS_NOT_ENVELOPE);
        }
        XmlEntity bodyEntity;
        if (isEmpty(xmlEntity.getChildren()) || isEmpty(bodyEntity = xmlEntity.find(SOAP_BODY_TAG))) {
            throw new SoapClientModuleException(SOAP_RESPONSE_HAS_NOT_BODY);
        }
        List<XmlEntity> bodyChildren = bodyEntity.getChildren();
        XmlEntity requestEntity;
        if (isEmpty(bodyChildren) || isEmpty(requestEntity = bodyChildren.get(0))) {
            return xmlEntityBuilder().create();
        }
        return requestEntity;
    }
}
