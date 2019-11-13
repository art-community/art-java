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

import lombok.*;
import ru.art.generator.soap.model.*;
import static ru.art.generator.soap.service.ParserService.*;

public class SoapGeneratorService {
    @Builder
    public static class SoapGenerationRequest {
        private String wsdlUrl;
        private String packageName;
        private SoapGenerationMode generationMode;
        private String absolutePathToSrcMainJava;
    }

    public static void performGeneration(SoapGenerationRequest request) {
        new SourceCodeGenService(request.packageName, request.absolutePathToSrcMainJava).sourceGen(parseWsdl(request.wsdlUrl), request.generationMode);
    }

    public static void main(String[] args) {
        SoapGeneratorService.performGeneration(SoapGenerationRequest.builder().generationMode(SoapGenerationMode.CLIENT)
                .absolutePathToSrcMainJava("C:\\Development\\Projects\\ART\\application-generator\\src\\main")
                .wsdlUrl("https://github.com/art-community/ART/files/3819206/errorWsdl.txt")
                .packageName("wsdl")
                .build());
    }
}
