package ru.adk.soap.client.constants;

public interface SoapClientModuleExceptionMessages {
    String SOAP_RESPONSE_HAS_NOT_ENVELOPE = "SOAP response hasn't 'Envelope' tag on first level of response XML";
    String SOAP_RESPONSE_HAS_NOT_BODY = "SOAP response hasn't 'Body' tag on second level of response XML";
    String INVALID_SOAP_COMMUNICATION_CONFIGURATION = "Some required fields in SOAP communication configuration are null: ";
}
