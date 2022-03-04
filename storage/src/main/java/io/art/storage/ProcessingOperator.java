package io.art.storage;

import lombok.*;
import static io.art.storage.StorageConstants.*;

@Getter
@AllArgsConstructor
public class ProcessingOperator {
    private final ProcessingOperation operation;
    private final Object value;
}
