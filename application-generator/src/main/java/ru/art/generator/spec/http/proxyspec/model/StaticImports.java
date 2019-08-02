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

package ru.art.generator.spec.http.proxyspec.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Model representing possible imports to be included in file.
 */
@Setter
@Getter
public class StaticImports {
    private boolean hasMappers;                      //responsible for importing Mapper's classes
    private boolean hasMethodWithResponse;           //responsible for importing Caster.cast() method
    private boolean hasHttpClientProxyWithoutParams; //responsible for importing HttpClientProxyBuilder.httpClientProxy method
    private boolean hasInterceptor;                  //responsible for importing HttpClientInterceptor class
    private boolean hasPrimitiveMapper;              //responsible for importing PrimitiveMapping class
    private boolean hasMimeTypes;                    //responsible for importing MimeToContentTypeMapper class
}
