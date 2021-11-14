package io.art.meta.transformer;

import io.art.meta.model.*;
import lombok.*;
import java.util.function.*;

@Getter
@AllArgsConstructor
public class CustomTransformers {
    private final Function<MetaType<?>, MetaTransformer<?>> input;
    private final Function<MetaType<?>, MetaTransformer<?>> output;
}
