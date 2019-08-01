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
