package ru.art.xml.rules.builder;

public interface XmlEntityMappingBuilder {
    XmlEntityPartBuilder addRule();

    XmlEntityMapping build();
}
