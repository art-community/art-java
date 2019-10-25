/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
