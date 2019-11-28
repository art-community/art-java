package ru.art.generator.soap.service;

import java.util.List;
import ru.art.generator.soap.model.OperationSoapGen;

public class Main {

  public static void main(String[] args) {
    List<OperationSoapGen> operationSoapGens = ParserService
        .parseWsdl("C:\\Users\\Robert_Mirzakhanian\\Desktop\\trWsdl.wsdl");
    System.out.println(operationSoapGens);
  }
}
