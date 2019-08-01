package ru.art.generator.spec.http.servicespec.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Model representing possible imports to be included in file.
 */
@Setter
@Getter
public class StaticImports {
    private boolean hasServiceMethods;      //responsible for importing Service, Mapper's classes
    private boolean hasCaster;              //responsible for importing Caster.cast() method
    private boolean hasValidationPolicy;    //responsible for importing RequestValidationPolicy class
    private boolean hasMimeTypes;           //responsible for importing MimeToContentTypeMapper class
    private boolean hasPrimitiveMapper;     //responsible for importing PrimitiveMapping class
    private boolean hasInterceptor;         //responsible for importing HttpClientInterceptor class
}
