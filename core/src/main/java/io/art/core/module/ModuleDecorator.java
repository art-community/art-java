package io.art.core.module;

import java.util.function.*;

public interface ModuleDecorator<T extends Module> extends UnaryOperator<T> {
}
