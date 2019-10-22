package ru.art.generator.soap.service;

import ru.art.generator.soap.model.*;
import static ru.art.generator.soap.service.ParserService.*;
import java.util.concurrent.atomic.*;

public class SoapGeneratorService {
    public static final AtomicReference<String> SRC_MAIN_JAVA_ABSOLUTE_PATH = new AtomicReference<>();

    public static void performGeneration(String wsdlUrl, String packageName, SoapGenerationMode generationMode) {
        new SourceCodeGenService(packageName).sourceGen(parseWsdl(wsdlUrl), generationMode);
    }
}
