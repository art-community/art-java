package ru.art.generator.soap.service;

import lombok.experimental.*;
import ru.art.generator.soap.model.*;
import static ru.art.generator.soap.service.ParserService.*;

@UtilityClass
public class SoapGeneratorService {
    public static void performGeneration(String wsdlUrl, String packagePath, SoapGenerationMode soapGenerationMode) {
        new SourceCodeGenService(packagePath).sourceGen(parseWsdl(wsdlUrl), soapGenerationMode);
    }
}
