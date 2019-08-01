package ru.adk.entity.named;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.adk.entity.Value;

@Getter
@AllArgsConstructor(staticName = "namedValue")
public class NamedValue {
    private final String name;
    private final Value value;
}
