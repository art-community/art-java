package ru.art.generator.compiler.service;

import com.sun.tools.javac.code.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.*;
import static com.sun.tools.javac.code.Flags.*;
import static ru.art.generator.compiler.constants.CompilationConstants.*;
import static ru.art.generator.compiler.extensions.JavaCompilerExtensions.*;
import static ru.art.generator.compiler.state.CompilationState.*;
import java.util.List;

public class MakerService {
    public static Name name(String name) {
        return elements().getName(name);
    }

    public static JCTree.JCIdent identifier(String name) {
        return maker().Ident(name(name));
    }

    public static Name typeName(Class<?> type) {
        return name(type.getName().startsWith(JAVA_LANG_PACKAGE) ? type.getSimpleName() : type.getName());
    }

    public static Name simpleTypeName(Class<?> type) {
        return name(type.getSimpleName());
    }


    public static JCTree.JCIdent typeIdentifier(Class<?> type) {
        return maker().Ident(typeName(type));
    }

    public static JCTree.JCIdent simpleTypeIdentifier(Class<?> type) {
        return maker().Ident(simpleTypeName(type));
    }

    public static JCTree.JCIdent typeIdentifier(String typeName) {
        return maker().Ident(name(typeName));
    }


    public static JCTree.JCVariableDecl parameter(String name, JCTree.JCIdent typeIdentifier) {
        return maker().VarDef(maker().Modifiers(FINAL | PARAMETER), name(name), typeIdentifier, null);
    }

    public static JCTree.JCTypeApply parameterizedType(Class<?> type, List<Class<?>> parameterTypes) {
        return maker().TypeApply(typeIdentifier(type), fromStream(parameterTypes.stream().map(MakerService::typeIdentifier)));
    }

    public static JCTree.JCTypeApply simpleParameterizedType(Class<?> type, List<Class<?>> parameterTypes) {
        return maker().TypeApply(simpleTypeIdentifier(type), fromStream(parameterTypes.stream().map(MakerService::simpleTypeIdentifier)));
    }

    public static JCTree.JCCompilationUnit getCompilationUnit(Symbol symbol) {
        return elements().getTreeAndTopLevel(symbol, null, null).snd;
    }
}
