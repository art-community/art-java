package ru.art.generator.soap.service;

import com.predic8.wsdl.*;
import lombok.experimental.*;
import ru.art.generator.soap.model.*;
import static ru.art.generator.soap.factory.FieldFactory.*;
import java.util.*;
import java.util.stream.*;

/**
 * Class for parsing wsdl
 */
@UtilityClass
public class ParserService {

    private Definitions wsdlDefinitions;

    public static List<OperationSoapGen> parseWsdl(String fullPath) {
        wsdlDefinitions = new WSDLParser().parse(fullPath);
        return wsdlDefinitions.getOperations().stream().map(ParserService::mapOperationToOperationSoapGen)
                .collect(Collectors.toList());
    }


    private static OperationSoapGen mapOperationToOperationSoapGen(Operation operation) {
        String inputMessageName = operation.getInput().getMessage().getName();
        String outputMessageName = operation.getOutput().getMessage().getName();

        List<Field> faultFields = new ArrayList<>();
        operation
                .getFaults()
                .stream()
                .map(fault -> createFieldsByMessage(fault.getMessage().getName(), wsdlDefinitions))
                .forEach(faultFields::addAll);

        return OperationSoapGen.builder()
                .name(operation.getName())
                .input(createFieldsByMessage(inputMessageName, wsdlDefinitions))
                .output(createFieldsByMessage(outputMessageName, wsdlDefinitions))
                .fault(faultFields)
                .build();
    }

}
