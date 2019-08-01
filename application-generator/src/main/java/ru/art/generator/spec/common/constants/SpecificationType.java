package ru.art.generator.spec.common.constants;

/**
 * Possible specification's types.
 */
public enum SpecificationType {
    httpServiceSpec("httpService"),
    httpProxySpec("httpProxy"),
    soapServiceSpec("soapService"),
    soapProxySpec("soapProxy"),
    grpcServiceSpec("grpcService"),
    grpcProxySpec("grpcProxy");

    String name;

    SpecificationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }}
