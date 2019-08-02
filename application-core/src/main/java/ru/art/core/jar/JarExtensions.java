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

package ru.art.core.jar;

import ru.art.core.context.Context;
import ru.art.core.exception.InternalRuntimeException;
import static java.nio.file.Files.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@SuppressWarnings("Duplicates")
public class JarExtensions {
    public static boolean insideJar() {
        return ofNullable(Context.class.getProtectionDomain())
                .map(ProtectionDomain::getCodeSource)
                .map(CodeSource::getLocation)
                .map(URL::getPath)
                .map(File::new)
                .map(File::getPath)
                .filter(file -> !isDirectory(Paths.get(file)))
                .isPresent();
    }

    public static void extractCurrentJar() {
        ofNullable(Context.class.getProtectionDomain())
                .map(ProtectionDomain::getCodeSource)
                .map(CodeSource::getLocation)
                .map(URL::getPath)
                .map(File::new)
                .map(File::getPath)
                .filter(file -> !isDirectory(Paths.get(file)))
                .ifPresent(JarExtensions::extractJar);
    }

    public static void extractCurrentJar(String directory) {
        ofNullable(Context.class.getProtectionDomain())
                .map(ProtectionDomain::getCodeSource)
                .map(CodeSource::getLocation)
                .map(URL::getPath)
                .map(File::new)
                .map(File::getPath)
                .filter(file -> !isDirectory(Paths.get(file)))
                .ifPresent(file -> extractJar(file, directory));
    }

    public static void extractJar(String jarPath) {
        extractJar(jarPath, EMPTY_STRING);
    }

    public static void extractJar(String jarPath, String directory) {
        ZipFile jarArchive = null;
        try {
            createDirectories(Paths.get(directory));
            jarArchive = new ZipFile(jarPath);
            for (ZipEntry entry : jarArchive.stream().collect(toList())) {
                Path entryDestination = Paths.get(directory).resolve(entry.getName());
                if (entry.isDirectory() && !exists(entryDestination)) {
                    createDirectory(entryDestination);
                    continue;
                }
                copy(jarArchive.getInputStream(entry), entryDestination, REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new InternalRuntimeException(e);
        } finally {
            if (nonNull(jarArchive)) {
                try {
                    jarArchive.close();
                } catch (IOException ignored) {
                    // Ignore cause unnecessary to handle this exception
                }
            }
        }
    }

    public static void extractCurrentJarEntry(String entryRegex) {
        ofNullable(Context.class.getProtectionDomain())
                .map(ProtectionDomain::getCodeSource)
                .map(CodeSource::getLocation)
                .map(URL::getPath)
                .map(File::new)
                .map(File::getPath)
                .filter(file -> !isDirectory(Paths.get(file)))
                .ifPresent(jarPath -> extractJarEntry(jarPath, entryRegex, EMPTY_STRING));
    }

    public static void extractCurrentJarEntry(String entryRegex, String directory) {
        ofNullable(Context.class.getProtectionDomain())
                .map(ProtectionDomain::getCodeSource)
                .map(CodeSource::getLocation)
                .map(URL::getPath)
                .map(File::new)
                .map(File::getPath)
                .filter(file -> !isDirectory(Paths.get(file)))
                .ifPresent(jarPath -> extractJarEntry(jarPath, entryRegex, directory));
    }

    public static void extractJarEntry(String jarPath, String entryRegex) {
        extractJarEntry(jarPath, entryRegex, EMPTY_STRING);
    }

    public static void extractJarEntry(String jarPath, String entryRegex, String directory) {
        ZipFile jarArchive = null;
        try {
            createDirectories(Paths.get(directory));
            jarArchive = new ZipFile(jarPath);
            List<? extends ZipEntry> entries = jarArchive
                    .stream()
                    .filter(entry -> compile(entryRegex).matcher(entry.getName()).matches())
                    .collect(toList());
            for (ZipEntry entry : entries) {
                Path entryDest = Paths.get(directory).resolve(entry.getName());

                if (entry.isDirectory() && !exists(entryDest)) {
                    createDirectory(entryDest);
                    continue;
                }
                copy(jarArchive.getInputStream(entry), entryDest, REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new InternalRuntimeException(e);
        } finally {
            if (nonNull(jarArchive)) {
                try {
                    jarArchive.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
