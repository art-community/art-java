package ru.art.entity.named;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.art.entity.Value;

@Getter
@AllArgsConstructor(staticName = "namedValue")
public class NamedValue {
    private final String name;
    private final Value value;
}
