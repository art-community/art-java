package ru.art.generator.soap.service;

import java.util.List;
import ru.art.generator.soap.model.ModeGeneration;
import ru.art.generator.soap.model.OperationSoapGen;

public class GeneratorService {

  public void generateCode(String filePath, String packageString, ModeGeneration modeGeneration) {
    List<OperationSoapGen> operationSoapGens = ParserService.parseWsdl(filePath);
    SourceCodeGenService sourceCodeGenService = new SourceCodeGenService(packageString);
    sourceCodeGenService.sourceGen(operationSoapGens, modeGeneration);
  }
}
