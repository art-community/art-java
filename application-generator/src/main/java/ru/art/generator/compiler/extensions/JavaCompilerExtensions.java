package ru.art.generator.compiler.extensions;

import com.fasterxml.jackson.databind.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.List;
import ru.art.core.exception.*;
import ru.art.generator.compiler.model.*;
import static com.sun.source.tree.Tree.Kind.VARIABLE;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static ru.art.core.constants.StringConstants.DOT;
import static ru.art.generator.compiler.constants.CompilationConstants.JAVA_LANG_PACKAGE;
import java.util.*;
import java.util.stream.*;

public class JavaCompilerExtensions {
    private static String extractPackage(String typeName) {
        return typeName.substring(0, typeName.lastIndexOf(DOT));
    }

    private static String extractType(String typeName) {
        return typeName.substring(extractPackage(typeName).length() + 1);
    }

    public static <T> List<T> fromStream(Stream<T> stream) {
        ListBuffer<T> list = new ListBuffer<>();
        list.addAll(stream.collect(toList()));
        return list.toList();
    }

    public static Map<String, TypedParameter> getFields(JCTree.JCClassDecl declaration) {
        return declaration.getMembers()
                .stream()
                .filter(member -> member.getKind() == VARIABLE)
                .map(field -> (JCTree.JCVariableDecl) field)
                .collect(toMap(field -> field.name.toString(), field -> TypedParameter.builder()
                        .type(classFor(field.vartype.toString()))
                        .parameter(field.name.toString())
                        .build()));
    }

    public static Class<?> classFor(String name) {
        try {
            return Class.forName(name);
        } catch (Exception exception) {
            try {
                return Class.forName(JAVA_LANG_PACKAGE + DOT +  name);
            } catch (Exception newException) {
                throw new InternalRuntimeException(newException);
            }
        }
    }

    public static boolean classExists(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
