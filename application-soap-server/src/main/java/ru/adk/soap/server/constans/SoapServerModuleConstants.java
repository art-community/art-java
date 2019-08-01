package ru.adk.soap.server.constans;

public interface SoapServerModuleConstants {
    String SOAP_SERVER_MODULE_ID = "SOAP_SERVER_MODULE";
    String EXECUTE_SOAP_SERVICE = "EXECUTE_SOAP_SERVICE";
    String GET_SERVICE_WSDL = "GET_SERVICE_WSDL";
    String ENVELOPE = "Envelope";
    String HEADER = "Header";
    String BODY = "Body";
    String FAULT = "Fault";
    String CODE = "Code";
    String VALUE = "Value";
    String REASON = "Reason";
    String TEXT = "Text";
    String PREFIX = "soapenv";
    String NAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";
    String WSDL_EXTENSION = ".wsdl";
    String SOAP_SERVICE_URL = "serviceUrl";
    String SOAP_SERVICE_TYPE = "SOAP";

    interface ResponseFaultConstants {
        String UNEXPECTED_ERROR = "UNEXPECTED_ERROR";
        String UNEXPECTED_ERROR_TEXT = "Unexpected Error";
    }
}
