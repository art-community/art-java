package ru.art.xml.rules.builder;

import lombok.Builder;
import lombok.Getter;
import static ru.art.core.factory.CollectionsFactory.setOf;
import java.util.Set;

@Builder
@Getter
class XmlEntityPart {
    private final Set<String> xmlParts = setOf();
}
