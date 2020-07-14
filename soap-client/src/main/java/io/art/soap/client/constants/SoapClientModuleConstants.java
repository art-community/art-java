/*
 * ART
 *
 * Copyright 2020 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.soap.client.constants;

public interface SoapClientModuleConstants {
    String SOAP_ENVELOPE_TAG = "Envelope";
    String SOAP_ENVELOPE_DEFAULT_PREFIX = "SoapEnv";
    String SOAP_ENVELOPE_DEFAULT_NAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";
    String SOAP_BODY_DEFAULT_PREFIX = "SoapBody";
    String SOAP_BODY_DEFAULT_NAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";
    String SOAP_BODY_TAG = "Body";
    String SOAP_CLIENT_MODULE_ID = "SOAP_CLIENT_MODULE";
    String SOAP_COMMUNICATION_SERVICE_TYPE = "SOAP_COMMUNICATION";

    enum OperationIdSource {
        REQUEST,
        CONFIGURATION
    }
}
