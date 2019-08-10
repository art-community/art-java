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

package ru.art.generator.mapper.operations;

import ru.art.generator.mapper.Generator;
import ru.art.generator.mapper.annotation.NonGenerated;
import ru.art.generator.mapper.exception.DefinitionException;
import static java.text.MessageFormat.format;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.art.generator.mapper.constants.Constants.*;
import static ru.art.generator.mapper.constants.Constants.PathAndPackageConstants.*;
import static ru.art.generator.mapper.constants.ExceptionConstants.DefinitionExceptions.UNABLE_TO_DEFINE_CLASS;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

/**
 * Interface containing static methods for analyzing
 * model and mapping packages before starting generating
 * new mappers.
 */
public interface AnalyzingOperations {

    /**
     * Getting class with URLClassLoader by path and file's name.
     *
     * @param path        - compiled path taken from jar till main.
     * @param fileName    - name of the file, which class need to be taken.
     * @param packagePath - string value of package path.
     * @return Class instance.
     * @throws DefinitionException is thrown when unable to define class by url.
     */
    static Class getClass(String path, String fileName, String packagePath) throws DefinitionException {
        URL[] urls = new URL[1];
        File file = new File(path);
        try {
            urls[0] = file.toURI().toURL();
            return URLClassLoader.newInstance(urls, Generator.class.getClassLoader()).loadClass(packagePath + DOT + fileName);
        } catch (MalformedURLException | ClassNotFoundException e) {
            throw new DefinitionException(format(UNABLE_TO_DEFINE_CLASS, fileName), e);
        }
    }

    /**
     * Getting list of files in compiled package including inner packages.
     *
     * @param path - path to compiled package.
     * @return List of files in package.
     */
    static List<File> getListOfFilesInCompiledPackage(String path) {
        File packageInJar = new File(path);
        List<File> fileList =
                dynamicArrayOf(packageInJar.listFiles());
        fileList.removeIf(file -> !file.getName().contains(DOT_CLASS) && !file.isDirectory());
        fileList.removeIf(file -> file.getName().contains(DOLLAR) && !file.isDirectory());
        return fileList;
    }

    /**
     * Method deleting files in mapping package, which has no equivalent in model package.
     * If model package is empty, deleting mapping package.
     * Classes marked as @NonGenerated are never automatically deleted.
     *
     * @param mappingFilesList - list of files in mapping non-compiled package.
     * @param modelFilesList   - list of files in model non-compiled package.
     * @param path             - compiled package path of model directory.
     * @param packageMapping   - string value of mapping package.
     * @param files            - map of files in model non-compiled package.
     */
    static void deleteFile(List<File> mappingFilesList, List<File> modelFilesList, String path, String packageMapping, Map<String, Integer> files) {
        String nonCompiledMappingPackagePath = path.replace(BUILD_CLASSES_JAVA_MAIN, SRC_MAIN_JAVA) + SLASH_MAPPING;

        for (File mappingFile : mappingFilesList) {
            boolean modelFileWasDeleted = true;
            //for all model package try to find which files were deleted
            for (File modelFile : modelFilesList) {
                String modelFileName = modelFile.getName().replace(DOT_CLASS, EMPTY_STRING);
                String checkingFileName = mappingFile.getName().replace(MAPPER, EMPTY_STRING).replace(DOT_CLASS, EMPTY_STRING);

                if (checkingFileName.equals(modelFileName) || checkingFileName.contains(REQUEST + RESPONSE)) {
                    if (isNotEmpty(files.get(checkingFileName.replace(REQUEST, EMPTY_STRING))) &&
                            isNotEmpty(files.get(checkingFileName.replace(RESPONSE, EMPTY_STRING)))) {
                        modelFileWasDeleted = false;
                        break;
                    }
                }
            }
            //deleting mapping file
            if (modelFileWasDeleted) {
                try {
                    Class clazz = getClass(path, mappingFile.getName().replace(DOT_CLASS, EMPTY_STRING), packageMapping);
                    if (!clazz.isAnnotationPresent(NonGenerated.class)) {
                        File mappingFileNonCompiled =
                                new File(nonCompiledMappingPackagePath + File.separator + mappingFile.getName().replace(DOT_CLASS, EMPTY_STRING) + DOT_JAVA);
                        mappingFileNonCompiled.delete();
                    }
                } catch (DefinitionException e) {
                    e.printStackTrace();
                }
            }
        }

        if (modelFilesList.isEmpty()) {
            File mappingPackage = new File(nonCompiledMappingPackagePath);
            mappingPackage.delete();
        }
    }
}
