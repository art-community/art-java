package ru.art.generator.compiler.state;

import com.sun.tools.javac.model.*;
import com.sun.tools.javac.tree.*;
import java.util.concurrent.atomic.*;

public class CompilationState {
    private final static AtomicReference<TreeMaker> maker = new AtomicReference<>();
    private final static AtomicReference<JavacElements> elements = new AtomicReference<>();

    public static TreeMaker maker() {
        return maker.get();
    }

    public static TreeMaker maker(TreeMaker maker) {
        CompilationState.maker.set(maker);
        return maker;
    }

    public static JavacElements elements() {
        return elements.get();
    }

    public static JavacElements elements(JavacElements elements) {
        CompilationState.elements.set(elements);
        return elements;
    }
}
