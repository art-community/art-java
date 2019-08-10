/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.generator.mapper;

import ru.art.generator.mapper.annotation.NonGenerated;
import ru.art.generator.mapper.exception.MappingGeneratorException;
import ru.art.generator.mapper.operations.AnalyzingOperations;
import static java.io.File.separator;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.generator.mapper.constants.Constants.PathAndPackageConstants.*;
import static ru.art.generator.mapper.constants.Constants.REQUEST;
import static ru.art.generator.mapper.constants.Constants.RESPONSE;
import static ru.art.generator.mapper.operations.AnalyzingOperations.deleteFile;
import static ru.art.generator.mapper.operations.AnalyzingOperations.getListOfFilesInCompiledPackage;
import static ru.art.generator.mapper.operations.CommonOperations.printError;
import static ru.art.generator.mapper.operations.GeneratorOperations.*;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Main class for generating mappers based on classes in package "model".
 * Name for generating class equals "ModelClassName" + "Mapper".
 * Example of Model mapper:
 * interface ModelMapper {
 * ValueToModelMapper<Model, Entity> toModel = entity -> Model.builder()
 * .build();
 * <p>
 * ValueFromModelMapper<Model, Entity> fromModel = model -> Entity.entityBuilder()
 * .build();
 * }
 */
public class Generator {

    /**
     * Perform generation of mapping classes basing on model package.
     * Before generating, deleting redundant classes in mapping package.
     *
     * @param startPackagePath     - path to parent package for model package.
     * @param modelPackageName     - name of package which contains models.
     * @param generatedPackageName - name of package which will contain mappers.
     */
    public static void performGeneration(String startPackagePath, String modelPackageName, String generatedPackageName) {
        String genPackageParentPath = startPackagePath.replace(SLASH_MODEL, SLASH_MAPPING);
        String fullModelPackagePath = startPackagePath + separator + modelPackageName;
        String fullGenPackagePath = genPackageParentPath + separator + generatedPackageName;


        String parentPackage = startPackagePath.substring(startPackagePath.indexOf(RU))
                .replace(SLASH, DOT)
                .replace(BACKWARD_SLASH, DOT);
        String genParentPackage = genPackageParentPath.substring(genPackageParentPath.indexOf(RU))
                .replace(SLASH, DOT)
                .replace(BACKWARD_SLASH, DOT);
        String packageModel = parentPackage + DOT + modelPackageName;
        String genPackage = genParentPackage + DOT + generatedPackageName;

        List<File> modelFileList = getListOfFilesInCompiledPackage(fullModelPackagePath);
        packageMappingPreparation(genPackageParentPath, genPackage, fullGenPackagePath, modelFileList);
        mapperGeneration(startPackagePath, genPackage, packageModel, modelFileList);
    }

    /**
     * Analyze model package and delete redundant classes in mapping package.
     *
     * @param genPackageParentPath - path of parent package for generated one taken from jar.
     * @param genPackage           - package which will contain mappers.
     * @param fullGenPackagePath   - absolute path of genPackage.
     * @param modelFileList        - list of files in compiled model package.
     */
    private static void packageMappingPreparation(String genPackageParentPath, String genPackage, String fullGenPackagePath, List<File> modelFileList) {
        /*
        Map files was created for convenient way to get file by it's name without searching it
         */
        Map<String, Integer> files = mapOf();
        for (int i = 0; i < modelFileList.size(); i++)
            files.put(modelFileList.get(i).getName().replace(DOT_CLASS, EMPTY_STRING), i);

        List<File> mappingFile = getListOfFilesInCompiledPackage(fullGenPackagePath);
        deleteFile(mappingFile, modelFileList, genPackageParentPath, genPackage, files);
    }

    /**
     * Generate classes in mapping package basing on model package classes.
     * Name for generating class equals "ModelClassName" + "Mapper".
     * For classes which contains word "Request" in name perform search class
     * with the same name, but containing "Response" (and vise versa).
     * F.e. for "ModelClassRequest" trying to find "ModelClassResponse".
     * If both classes exist, generator creates one mapping class with name
     * equals "ModelClass" + "RequestResponseMapper".
     *
     * @param genPackagePath - path to model package's parent package.
     * @param genPackage     - string value of mapper's package.
     * @param packageModel   - string value of model package.
     * @param modelFileList  - list of files in compiled model package.
     */
    private static void mapperGeneration(String genPackagePath, String genPackage, String packageModel, List<File> modelFileList) {
        /*
        Map files was created for convenient way to get file by it's name without searching it
         */
        Map<String, Integer> files = mapOf();
        for (int i = 0; i < modelFileList.size(); i++)
            files.put(modelFileList.get(i).getName(), i);
        String jarPathToMain = genPackagePath.substring(0, genPackagePath.lastIndexOf(MAIN) + 5);
        for (int i = 0; i < modelFileList.size(); i++) {
            if (isEmpty(modelFileList.get(i))) continue;
            String currentModelFileName = modelFileList.get(i).getName();
            if (modelFileList.get(i).isDirectory()) {
                performGeneration(modelFileList.get(i).getPath().replace(separator + currentModelFileName, EMPTY_STRING),
                        currentModelFileName,
                        currentModelFileName);
                continue;
            }

            if (currentModelFileName.contains(REQUEST)) {
                if (isNotEmpty(files.get(currentModelFileName.replace(REQUEST, RESPONSE)))) {
                    try {
                        Class request = AnalyzingOperations.getClass(jarPathToMain, currentModelFileName.replace(DOT_CLASS, EMPTY_STRING), packageModel);
                        Class response = AnalyzingOperations.getClass(jarPathToMain, currentModelFileName.replace(DOT_CLASS, EMPTY_STRING).replace(REQUEST, RESPONSE), packageModel);
                        if (!request.isAnnotationPresent(NonGenerated.class) &&
                                !response.isAnnotationPresent(NonGenerated.class) &&
                                !request.isEnum()) {
                            createRequestResponseMapperClass(request, response, genPackage, jarPathToMain);
                            modelFileList.set(files.get(currentModelFileName.replace(REQUEST, RESPONSE)), null);
                            files.remove(currentModelFileName);
                            files.remove(currentModelFileName.replace(REQUEST, RESPONSE));
                        }
                    } catch (MappingGeneratorException e) {
                        printError(e.getMessage());
                    }
                } else {
                    try {
                        createMapper(genPackage, packageModel, jarPathToMain, currentModelFileName);
                    } catch (MappingGeneratorException e) {
                        printError(e.getMessage());
                    }
                }
            } else {
                try {
                    createMapper(genPackage, packageModel, jarPathToMain, currentModelFileName);
                } catch (MappingGeneratorException e) {
                    printError(e.getMessage());
                }
            }
        }
        generatedFiles.clear();
    }

    /**
     * Method for creating simple mapper class.
     *
     * @param genPackage           - string value of mapper's package.
     * @param packageModel         - string value of model package.
     * @param jarPathToMain        - classpath from root to main.
     * @param currentModelFileName - name of current model file.
     */
    private static void createMapper(String genPackage, String packageModel, String jarPathToMain, String currentModelFileName) {
        Class clazz = AnalyzingOperations.getClass(jarPathToMain, currentModelFileName.replace(DOT_CLASS, EMPTY_STRING), packageModel);
        if (!clazz.isAnnotationPresent(NonGenerated.class) && !clazz.isEnum())
            createMapperClass(clazz, genPackage, jarPathToMain);
    }
}