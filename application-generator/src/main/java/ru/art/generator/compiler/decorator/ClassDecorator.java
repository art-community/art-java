package ru.art.generator.compiler.decorator;

import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.*;
import lombok.*;
import ru.art.generator.compiler.declaration.*;
import static java.util.Objects.nonNull;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.generator.compiler.service.MakerService.*;
import static ru.art.generator.compiler.state.CompilationState.*;
import java.util.List;
import java.util.*;

@RequiredArgsConstructor
public class ClassDecorator {
    private final static Map<String, ClassDecorator> decorators = concurrentHashMap();

    private final JCCompilationUnit compilationUnit;
    private final JCClassDecl declaration;
    private final Set<String> uniqueImports = setOf();
    private final List<JCFieldAccess> imports = linkedListOf();
    private final List<JCFieldAccess> staticImports = linkedListOf();
    private final List<JCVariableDecl> fields = linkedListOf();

    public static ClassDecorator decorator(JCClassDecl declaration) {
        ClassDecorator decorator = decorators.get(declaration.name.toString());
        if (nonNull(decorator)) {
            return decorator;
        }
        decorator = new ClassDecorator(elements().getTreeAndTopLevel(declaration.sym, null, null).snd, declaration);
        decorators.put(declaration.name.toString(), decorator);
        return decorator;
    }

    public ClassDecorator field(FieldDeclaration fieldDeclaration) {
        maker().at(declaration.pos);
        Class<?> type = fieldDeclaration.getType();
        List<Class<?>> typeParameters = fieldDeclaration.getTypeParameters();
        JCExpression fieldTypeIdentifier = typeParameters.isEmpty() ? simpleTypeIdentifier(type) : simpleParameterizedType(type, typeParameters);
        fields.add(maker().VarDef(
                maker().Modifiers(fieldDeclaration.getFlags()),
                name(fieldDeclaration.getName()),
                fieldTypeIdentifier,
                fieldDeclaration.getInitializer()
        ));

        if (uniqueImports.contains(type.getName())) {
            return this;
        }
        uniqueImports.add(type.getName());

        for (Class<?> typeParameter : typeParameters) {
            if (uniqueImports.contains(typeParameter.getName())) {
                return this;
            }
            uniqueImports.add(typeParameter.getName());
        }

        imports.add(maker().Select(identifier(type.getPackageName()), name(type.getSimpleName())));
        return this;
    }

    public JCClassDecl decorate() {
        maker().at(declaration.pos);
        ListBuffer<JCTree> definitions = new ListBuffer<>();
        fields.forEach(definitions::append);
        definitions.addAll(declaration.defs);
        declaration.defs = definitions.toList();

        maker().at(compilationUnit.pos);
        ListBuffer<JCTree> importDefinitions = new ListBuffer<>();
        imports.stream().map(classImport -> maker().Import(classImport, false)).forEach(importDefinitions::append);
        staticImports.stream().map(staticImport -> maker().Import(staticImport, true)).forEach(importDefinitions::append);
        importDefinitions.addAll(compilationUnit.defs);
        compilationUnit.defs = importDefinitions.toList();

        return declaration;
    }
}
