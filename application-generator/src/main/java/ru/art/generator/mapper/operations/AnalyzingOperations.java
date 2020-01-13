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

package ru.art.generator.mapper.operations;

import ru.art.generator.mapper.Generator;
import ru.art.generator.mapper.annotation.IgnoreGeneration;
import ru.art.generator.mapper.exception.DefinitionException;
import ru.art.generator.mapper.models.GenerationPackageModel;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

import static java.io.File.separator;
import static java.text.MessageFormat.format;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.art.generator.mapper.constants.Constants.*;
import static ru.art.generator.mapper.constants.Constants.PathAndPackageConstants.*;
import static ru.art.generator.mapper.constants.ExceptionConstants.DefinitionExceptions.UNABLE_TO_DEFINE_CLASS;

/**
 * Class containing static methods for analyzing
 * model and mapping packages before starting generating
 * new mappers.
 */
public final class AnalyzingOperations {
    private AnalyzingOperations() {
    }

    /**
     * Getting class with URLClassLoader by path and file's name.
     *
     * @param generationInfo - information about packages and path for generated class.
     * @param fileName    - name of the file, which class need to be taken.
     * @return Class instance.
     * @throws DefinitionException is thrown when unable to define class by url.
     */
    public static Class getClass(GenerationPackageModel generationInfo, String fileName) throws DefinitionException {
        URL[] urls = new URL[1];
        File file = new File(generationInfo.getJarPathToMain());
        try {
            urls[0] = file.toURI().toURL();
            return new URLClassLoader(urls, Generator.class.getClassLoader()) {
                @Override
                public Class<?> loadClass(String name) throws ClassNotFoundException {
                    String classFile = generationInfo.getJarPathToMain().substring(0, generationInfo.getJarPathToMain().indexOf(MAIN) + MAIN.length())
                            + separator
                            + name.replace(DOT, separator)
                            + DOT_CLASS;
                    if (!new File(classFile).exists()) {
                        return super.loadClass(name);
                    }
                    return findClass(name);
                }
            }.loadClass(generationInfo.getModelPackage() + DOT + fileName);
        } catch (MalformedURLException | ClassNotFoundException throwable) {
            throw new DefinitionException(format(UNABLE_TO_DEFINE_CLASS, fileName), throwable);
        }
    }

    /**
     * Getting list of files in compiled package including inner packages.
     *
     * @param path - path to compiled package.
     * @return List of files in package.
     */
    public static List<File> getListOfFilesInCompiledPackage(String path) {
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
     * Classes marked as @IgnoreGeneration are never automatically deleted.
     *
     * @param mappingFilesList      - list of files in mapping non-compiled package.
     * @param modelFilesList        - list of files in model non-compiled package.
     * @param generationPackageInfo - information about packages and path for generated class.
     * @param files                 - map of files in model non-compiled package.
     */
    @SuppressWarnings("all")
    public static void deleteNonExistedFiles(List<File> mappingFilesList, List<File> modelFilesList, GenerationPackageModel generationPackageInfo, Map<String, Integer> files) {
        for (File mappingFile : mappingFilesList) {
            deleteFile(mappingFile, modelFilesList, generationPackageInfo, files);
            if (modelFilesList.isEmpty()) {
                File mappingPackage = new File(generationPackageInfo.getGenPackagePath());
                mappingPackage.delete();
            }
        }
    }

    /**
     * Method checks if model file was deleted and if is was, then
     * delete matching mapping file.
     * @param currentFile    - processed file.
     * @param modelFilesList - list of files in model non-compiled package.
     * @param generationInfo - information about packages and path for generated class.
     * @param files          - map of files in model non-compiled package.
     */
    public static void deleteFile(File currentFile, List<File> modelFilesList, GenerationPackageModel generationInfo, Map<String, Integer> files) {
        //for all model package try to find which files were deleted
        File modelNonCompiledFile = null;
        for (File modelFile : modelFilesList) {
            String modelFileName = modelFile.getName().replace(DOT_CLASS, EMPTY_STRING);
            String checkingFileName = currentFile.getName().replace(MAPPER, EMPTY_STRING).replace(DOT_CLASS, EMPTY_STRING);
            if (checkingFileName.equals(modelFileName) || checkingFileName.contains(REQUEST + RESPONSE)) {
                if (isNotEmpty(files.get(checkingFileName.replace(REQUEST, EMPTY_STRING))) &&
                        isNotEmpty(files.get(checkingFileName.replace(RESPONSE, EMPTY_STRING)))) {
                    modelNonCompiledFile = modelFile;
                    break;
                }
            }
        }
        if (modelNonCompiledFile != null && !currentFile.isDirectory())
            return;

        if (!currentFile.isDirectory()) {
            try {
                String mappingFileName = currentFile.getName().replace(DOT_CLASS, EMPTY_STRING);
                Class<?> clazz = getClass(generationInfo, mappingFileName);
                if (!isClassHasIgnoreGenerationAnnotation(clazz)) {
                    String pathname = generationInfo.getGenPackagePath() + separator + mappingFileName + DOT_JAVA;
                    new File(pathname).delete();
                }
            } catch (DefinitionException throwable) {
                throwable.printStackTrace();
            }
        } else {
            if (isEmpty(modelNonCompiledFile) || isEmpty(modelNonCompiledFile.listFiles()) || modelNonCompiledFile.listFiles().length == 0) {
                currentFile.delete();
            }
        }
    }

    /**
     * Method checks if class has @IgnoreGeneration annotation.
     * Need to check this manually, cause clazz.isAnnotationPresent doesn't wor properly.
     * @param clazz - class to check
     * @return true - if class has @IgnoreGeneration
     *         false - if class hasn't @IgnoreGeneration
     */
    public static boolean isClassHasIgnoreGenerationAnnotation(Class<?> clazz) {
        for (Annotation annotation : clazz.getAnnotations()) {
            if (annotation.annotationType().getCanonicalName().equals(IgnoreGeneration.class.getCanonicalName()))
                return true;
        }
        return false;
    }
}
