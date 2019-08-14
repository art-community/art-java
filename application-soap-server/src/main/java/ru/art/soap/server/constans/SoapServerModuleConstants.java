/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.soap.server.constans;

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
    String SOAP_SERVICE_TYPE = "SOAP_SERVICE";
    String SOAP_EXECUTION_SERVICE_TYPE = "SOAP_EXECUTION_SERVICE";
    String EXECUTE_SOAP_FUNCTION = "EXECUTE_SOAP_FUNCTION";

    interface ResponseFaultConstants {
        String UNEXPECTED_ERROR = "UNEXPECTED_ERROR";
        String UNEXPECTED_ERROR_TEXT = "Unexpected Error";
        String WSDL_ERROR = "WSDL_ERROR";
        String WSDL_RESOURCE_PATH_IS_EMPTY = "WSDL resource path is empty";
        String WSDL_SERVICE_URL_IS_EMPTY = "WSDL service url is empty";
        String WSDL_IS_EMPTY = "WSDL is empty";
    }
}
