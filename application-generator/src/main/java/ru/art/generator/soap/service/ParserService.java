package ru.art.generator.soap.service;

import static ru.art.generator.soap.factory.AbstractFieldFactory.createFieldsByMessage;

import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.WSDLParser;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import ru.art.generator.soap.model.Field;
import ru.art.generator.soap.model.OperationSoapGen;

/** Class for parsing wsdl
 *
 */
@UtilityClass
public class ParserService {

  private Definitions wsdlDefinitions;

  public static List<OperationSoapGen> parseWsdl(String fullPath) {
    WSDLParser parser = new WSDLParser();
    wsdlDefinitions = parser.parse(fullPath);
    return wsdlDefinitions.getOperations().stream().map(ParserService::mapOperationToOperationSoapGen)
        .collect(Collectors.toList());
  }


  private static OperationSoapGen mapOperationToOperationSoapGen(Operation operation) {
    String inputMessageName = operation.getInput().getMessage().getName();
    String outputMessageName = operation.getOutput().getMessage().getName();

    List<Field> faultFields = new ArrayList<>();
    operation.getFaults().stream().map(
        fault -> createFieldsByMessage(fault.getMessage().getName(), wsdlDefinitions)
    ).forEach(faultFields::addAll);

    return OperationSoapGen.builder()
        .name(operation.getName())
        .input(createFieldsByMessage(inputMessageName, wsdlDefinitions))
        .output(createFieldsByMessage(outputMessageName, wsdlDefinitions))
        .fault(faultFields)
        .build();
  }

}
