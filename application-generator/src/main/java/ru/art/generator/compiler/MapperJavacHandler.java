package ru.art.generator.compiler;

import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.*;
import lombok.core.*;
import lombok.javac.*;
import org.kohsuke.*;
import ru.art.entity.*;
import ru.art.entity.Entity.*;
import ru.art.entity.mapper.*;
import ru.art.generator.compiler.annotation.*;
import static com.sun.tools.javac.code.Flags.PARAMETER;
import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.util.List.*;
import static java.util.Arrays.*;
import static javax.lang.model.element.ElementKind.*;
import static lombok.javac.handlers.JavacHandlerUtil.*;
import java.util.concurrent.atomic.*;

@MetaInfServices(JavacAnnotationHandler.class)
public class MapperJavacHandler extends JavacAnnotationHandler<Mapper> {
    private final AtomicReference<JavacTreeMaker> maker = new AtomicReference<>();
    private final AtomicReference<JavacNode> node = new AtomicReference<>();


    @Override
    public void handle(AnnotationValues<Mapper> annotation, JCTree.JCAnnotation ast, JavacNode modelClassNode) {
        Context context = modelClassNode.getContext();

        Javac8BasedLombokOptions.replaceWithDelombokOptions(context).deleteLombokAnnotations();
        Javac9BasedLombokOptions.replaceWithDelombokOptions(context).deleteLombokAnnotations();

        deleteAnnotationIfNeccessary(modelClassNode, Mapper.class);
        deleteImportFromCompilationUnit(modelClassNode, "lombok.AccessLevel");

        JavacNode node = modelClassNode.up();
        JavacTreeMaker maker = node.getTreeMaker();
        this.node.set(node);
        this.maker.set(maker);

        generateToModelField();
    }

    private void generateToModelField() {
        JCExpression selfType = type(node().getName());
        JCExpression entityType = type(Entity.class);
        JCExpression mapperType = type(ValueFromModelMapper.class);

        resetMaker();

        JCVariableDecl parameter = parameter("model", selfType);

        JCExpression statement = node()
                .getElement()
                .getEnclosedElements().stream().filter(element -> element.getKind() == FIELD)
                .map(element -> (VarSymbol) element)
                .reduce(
                        maker().Apply(nil(), chainDotsString(node(), entityType.toString() + "." + "entityBuilder"), nil()),
                        (builder, field) -> {
                            if (String.class.getName().equals(field.asType().toString())) {
                                builder = maker().Apply(
                                        nil(),
                                        chainDotsString(node(), builder + "." + "stringField"),
                                        of(maker().Literal(field.name.toString()), maker().Select(maker().Ident(parameter.name), node().toName(field.name.toString())))
                                );
                            }
                            return builder;
                        }, (current, next) -> next);

        JCBlock body = maker().Block(0L, of(maker().Return(maker().Apply(nil(), methodCall(statement, "build"), nil()))));
        JCLambda lambda = lambda(body, parameter);
        JCVariableDecl field = publicStaticFinalField("toModel", genericType(mapperType, selfType, entityType), lambda);

        injectField(node(), field);
    }

    private JCExpression methodCall(JCExpression entityType, String method) {
        return chainDotsString(node(), entityType.toString() + "." + method);
    }

    private TreeMaker resetMaker() {
        return maker().at(node().getStartPos());
    }

    private void addFactoryMethod(JavacNode singletonClass, JavacTreeMaker singletonClassTreeMaker, JavacNode holderInnerClass) {
        JCModifiers modifiers = singletonClassTreeMaker.Modifiers(PUBLIC | STATIC);

        JCClassDecl singletonClassDecl = (JCClassDecl) singletonClass.get();
        JCIdent singletonClassType = singletonClassTreeMaker.Ident(singletonClassDecl.name);

        JCBlock block = addReturnBlock(singletonClassTreeMaker, holderInnerClass);

        JCMethodDecl factoryMethod = singletonClassTreeMaker.MethodDef(modifiers, singletonClass.toName("getInstance"), singletonClassType, nil(), nil(), nil(), block, null);
        injectMethod(singletonClass, factoryMethod);
    }

    private JCBlock addReturnBlock(JavacTreeMaker singletonClassTreeMaker, JavacNode holderInnerClass) {

        JCClassDecl holderInnerClassDecl = (JCClassDecl) holderInnerClass.get();
        JavacTreeMaker holderInnerClassTreeMaker = holderInnerClass.getTreeMaker();
        JCIdent holderInnerClassType = holderInnerClassTreeMaker.Ident(holderInnerClassDecl.name);

        JCFieldAccess instanceVarAccess = holderInnerClassTreeMaker.Select(holderInnerClassType, holderInnerClass.toName("INSTANCE"));
        JCReturn returnValue = singletonClassTreeMaker.Return(instanceVarAccess);

        ListBuffer<JCStatement> statements = new ListBuffer<>();
        statements.append(returnValue);

        return singletonClassTreeMaker.Block(0L, statements.toList());
    }

    private JCLambda lambda(JCBlock body, JCVariableDecl... parameters) {
        ListBuffer<JCVariableDecl> buffer = new ListBuffer<>();
        buffer.addAll(asList(parameters));
        return maker().Lambda(buffer.toList(), body);
    }

    private JCVariableDecl parameter(String name, JCExpression type) {
        return maker().VarDef(maker().Modifiers(FINAL | PARAMETER), node().toName(name), type, null);
    }

    private JCExpression type(Class<?> type) {
        return genTypeRef(node(), type.getName());
    }

    private JCExpression type(String name) {
        return genTypeRef(node(), name);
    }

    private JCExpression genericType(JCExpression type, JCExpression... parameters) {
        return maker().TypeApply(type, from(parameters));
    }

    private JCVariableDecl publicStaticFinalField(String name, JCExpression type, JCExpression initializer) {
        return maker().VarDef(maker().Modifiers(PUBLIC | STATIC | FINAL), node().toName(name), type, initializer);
    }

    private TreeMaker maker() {
        return maker.get().getUnderlyingTreeMaker();
    }

    private JavacNode node() {
        return node.get();
    }

}