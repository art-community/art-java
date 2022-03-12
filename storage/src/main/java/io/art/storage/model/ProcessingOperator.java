package io.art.storage.model;

import lombok.*;
import static io.art.storage.constants.StorageConstants.*;

@Getter
@AllArgsConstructor
public class ProcessingOperator {
    private final ProcessingOperation operation;
    private final Object value;
}
