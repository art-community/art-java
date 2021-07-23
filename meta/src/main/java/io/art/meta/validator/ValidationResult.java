
package io.art.meta.validator;

import lombok.*;

@Getter
@AllArgsConstructor
public class ValidationResult {
    private final boolean valid;
    private final String message;
}
