package ru.art.generator.compiler.processor;

import com.google.auto.service.*;
import com.sun.tools.javac.processing.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.*;
import ru.art.entity.*;
import ru.art.entity.mapper.*;
import ru.art.generator.compiler.annotation.*;
import ru.art.generator.compiler.declaration.*;
import ru.art.generator.compiler.decorator.*;
import ru.art.generator.compiler.model.*;
import ru.art.generator.compiler.service.*;
import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.util.List.*;
import static javax.lang.model.SourceVersion.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.generator.compiler.decorator.ClassDecorator.*;
import static ru.art.generator.compiler.extensions.JavaCompilerExtensions.*;
import static ru.art.generator.compiler.service.JavaCompilerService.*;
import static ru.art.generator.compiler.service.MakerService.*;
import static ru.art.generator.compiler.state.CompilationState.*;
import static ru.art.generator.mapper.constants.Constants.SupportedJavaClasses.*;
import javax.annotation.processing.*;
import javax.lang.model.*;
import javax.lang.model.element.*;
import java.util.*;

@AutoService(Processor.class)
public class MapperAnnotationProcessor extends AbstractProcessor {
    private JavacProcessingEnvironment processingEnvironment;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.processingEnvironment = (JavacProcessingEnvironment) processingEnvironment;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return setOf(Model.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latest();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (roundEnvironment.processingOver()) {
            return false;
        }
        javaCompilerService(processingEnvironment, (JavacRoundEnvironment) roundEnvironment).forAnnotatedClasses(Model.class, MapperAnnotationProcessor::generateModelFields);
        return false;
    }

    private static void generateModelFields(JCClassDecl modelClass, JavaCompilerService service) {
        Map<String, TypedParameter> fields = getFields(modelClass);
        ClassDecorator decorator = decorator(modelClass);
        decorator
                .field(generateToModelField(modelClass, fields, decorator))
                .field(generateFromModelField(modelClass, fields, decorator))
                .decorate();
    }

    private static FieldDeclaration generateToModelField(JCClassDecl modelClass, Map<String, TypedParameter> fields, ClassDecorator decorator) {
        ListBuffer<JCStatement> statements = new ListBuffer<>();

        statements.add(variable(
                "model",
                identifier(decorator.getClassName()),
                maker().NewClass(null, nil(), identifier(modelClass.name.toString()), nil(), null)
        ));

        for (TypedParameter field : fields.values()) {
            switch (field.getFullTypeName()) {
                case CLASS_STRING:
                    statements.add(
                            maker().Exec(maker().Assign(
                                    maker().Select(identifier("model"), name(field.getParameter())),
                                    maker().Apply(
                                            nil(),
                                            maker().Select(identifier("value"), name("getString")),
                                            of(maker().Literal(field.getParameter()))
                                    ))
                            )
                    );
                    break;
                case CLASS_INTEGER:
                    statements.add(
                            maker().Exec(maker().Assign(
                                    maker().Select(identifier("model"), name(field.getParameter())),
                                    maker().Apply(
                                            nil(),
                                            maker().Select(identifier("value"), name("getInt")),
                                            of(maker().Literal(field.getParameter()))
                                    )
                            ))
                    );

                    break;
            }
        }
        statements.add(maker().Return(identifier("model")));

        return FieldDeclaration.builder()
                .name("toModel")
                .flags(PUBLIC | FINAL | STATIC)
                .type(ValueToModelMapper.class)
                .typeParameter(TypedParameter.builder().packageName(decorator.getPackageName()).typeName(decorator.getClassName()).build())
                .typeParameter(TypedParameter.builder().packageName(Entity.class.getPackageName()).typeName(Entity.class.getSimpleName()).build())
                .initializer(LambdaDeclaration.builder()
                        .parameter(TypedParameter.builder().packageName(Entity.class.getPackageName()).typeName(Entity.class.getSimpleName()).parameter("value").build())
                        .body(maker().Block(0L, statements.toList()))
                        .build()
                        .make())
                .build();
    }

    private static FieldDeclaration generateFromModelField(JCClassDecl modelClass, Map<String, TypedParameter> fields, ClassDecorator decorator) {
        JCMethodInvocation entityBuilder = maker().Apply(nil(), maker().Select(simpleTypeIdentifier(Entity.class), name("entityBuilder")), nil());

        for (TypedParameter field : fields.values()) {
            switch (field.getFullTypeName()) {
                case CLASS_STRING:
                    entityBuilder = maker().Apply(nil(),
                            maker().Select(entityBuilder, name("stringField")),
                            of(maker().Literal(field.getParameter()), maker().Select(identifier("model"), name(field.getParameter())))
                    );
                    break;
                case CLASS_INTEGER:
                    entityBuilder = maker().Apply(nil(),
                            maker().Select(entityBuilder, name("intField")),
                            of(maker().Literal(field.getParameter()), maker().Select(identifier("model"), name(field.getParameter())))
                    );
                    break;
            }
        }
        return FieldDeclaration.builder()
                .name("fromModel")
                .flags(PUBLIC | FINAL | STATIC)
                .type(ValueFromModelMapper.class)
                .typeParameter(TypedParameter.builder().packageName(decorator.getPackageName()).typeName(decorator.getClassName()).build())
                .typeParameter(TypedParameter.builder().packageName(Entity.class.getPackageName()).typeName(Entity.class.getSimpleName()).build())
                .initializer(LambdaDeclaration.builder()
                        .parameter(TypedParameter.builder().packageName(decorator.getPackageName()).typeName(decorator.getClassName()).parameter("model").build())
                        .body(maker().Block(0L, of(maker().Return(maker().Apply(nil(), maker().Select(entityBuilder, name("build")), nil())))))
                        .build()
                        .make())
                .build();
    }
}
