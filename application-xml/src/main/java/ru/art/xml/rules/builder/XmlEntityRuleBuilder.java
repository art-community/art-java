package ru.art.xml.rules.builder;

import java.util.Collection;

public interface XmlEntityRuleBuilder {
    XmlEntityRuleBuilder input(String oldValue);

    XmlEntityRuleBuilder input(Collection<String> oldValues);

    XmlEntityMappingBuilder output(String newValue);
}
