/*
 * ART
 *
 * Copyright 2019-2021 ART
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

public interface SoapClientModuleExceptionMessages {
    String SOAP_RESPONSE_HAS_NOT_ENVELOPE = "SOAP response hasn't 'Envelope' tag on first level of response XML";
    String SOAP_RESPONSE_HAS_NOT_BODY = "SOAP response hasn't 'Body' tag on second level of response XML";
    String INVALID_SOAP_COMMUNICATION_CONFIGURATION = "Some required fields in SOAP communication configuration are null: ";
}
