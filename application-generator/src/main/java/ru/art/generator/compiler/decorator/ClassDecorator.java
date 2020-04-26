package ru.art.generator.compiler.decorator;

import com.sun.source.tree.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.*;
import lombok.*;
import ru.art.generator.compiler.declaration.*;
import ru.art.generator.compiler.model.*;
import static com.sun.source.tree.Tree.Kind.PACKAGE;
import static java.util.Objects.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.extension.NullCheckingExtensions.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.generator.compiler.service.MakerService.*;
import static ru.art.generator.compiler.state.CompilationState.*;
import java.util.List;
import java.util.*;

@Getter
public class ClassDecorator {
    private final static Map<String, ClassDecorator> decorators = concurrentHashMap();

    private final JCCompilationUnit compilationUnit;
    private final JCClassDecl declaration;
    private final String packageName;
    private final String className;
    private final Set<String> uniqueImports = setOf();
    private final List<JCFieldAccess> imports = linkedListOf();
    private final List<JCFieldAccess> staticImports = linkedListOf();
    private final List<JCVariableDecl> fields = linkedListOf();

    public ClassDecorator(JCClassDecl declaration) {
        this.declaration = declaration;
        compilationUnit = elements().getTreeAndTopLevel(declaration.sym, null, null).snd;
        packageName = doIfNotNull(compilationUnit.getPackageName(), Object::toString);
        className = declaration.name.toString();
    }

    public static ClassDecorator decorator(JCClassDecl declaration) {
        ClassDecorator decorator = decorators.get(declaration.name.toString());
        if (nonNull(decorator)) {
            return decorator;
        }
        decorator = new ClassDecorator(declaration);
        decorators.put(declaration.name.toString(), decorator);
        return decorator;
    }

    public ClassDecorator field(FieldDeclaration fieldDeclaration) {
        maker().at(declaration.pos);
        Class<?> type = fieldDeclaration.getType();
        List<TypedParameter> typeParameters = fieldDeclaration.getTypeParameters();
        JCExpression fieldTypeIdentifier = typeParameters.isEmpty() ? simpleTypeIdentifier(type) : simpleParameterizedType(type, typeParameters);
        fields.add(maker().VarDef(
                maker().Modifiers(fieldDeclaration.getFlags()),
                name(fieldDeclaration.getName()),
                fieldTypeIdentifier,
                fieldDeclaration.getInitializer()
        ));

        if (!uniqueImports.contains(type.getName()) && !type.getName().equals(declaration.name.toString())) {
            uniqueImports.add(type.getName());
            imports.add(maker().Select(identifier(type.getPackageName()), name(type.getSimpleName())));
        }

        for (TypedParameter typeParameter : typeParameters) {
            if (!uniqueImports.contains(typeParameter.getTypeName()) && !typeParameter.getTypeName().equals(declaration.name.toString())) {
                uniqueImports.add(typeParameter.getTypeName());
                String packageName = typeParameter.getPackageName();
                String typeName = typeParameter.getTypeName();
                imports.add(maker().Select(identifier(isEmpty(packageName) ? typeName : packageName), name(isEmpty(packageName) ? EMPTY_STRING : typeName)));
            }
        }
        return this;
    }

    public ClassDecorator addImport(Class<?> type) {
        if (uniqueImports.contains(type.getName()) || type.getName().equals(declaration.name.toString())) {
            return this;
        }
        uniqueImports.add(type.getName());
        imports.add(maker().Select(identifier(type.getCanonicalName().substring(0, type.getCanonicalName().lastIndexOf(DOT))), name(type.getSimpleName())));
        return this;
    }

    public JCClassDecl decorate() {
        maker().at(declaration.pos);
        ListBuffer<JCTree> definitions = new ListBuffer<>();
        definitions.addAll(declaration.defs);
        fields.forEach(definitions::append);
        declaration.defs = definitions.toList();

        maker().at(compilationUnit.pos);
        ListBuffer<JCTree> importDefinitions = new ListBuffer<>();
        List<JCTree> initialDefinitions = compilationUnit.defs;

        if (nonNull(compilationUnit.defs.head) && compilationUnit.defs.head.getKind() == PACKAGE) {
            importDefinitions.add(compilationUnit.defs.head);
            initialDefinitions = compilationUnit.defs.subList(1, compilationUnit.defs.length());
        }

        imports.stream().map(classImport -> maker().Import(classImport, false)).forEach(importDefinitions::append);
        staticImports.stream().map(staticImport -> maker().Import(staticImport, true)).forEach(importDefinitions::append);

        importDefinitions.addAll(initialDefinitions);

        compilationUnit.defs = importDefinitions.toList();

        return declaration;
    }
}
