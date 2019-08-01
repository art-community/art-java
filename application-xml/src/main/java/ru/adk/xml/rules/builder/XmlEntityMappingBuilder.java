package ru.adk.xml.rules.builder;

public interface XmlEntityMappingBuilder {
    XmlEntityPartBuilder addRule();

    XmlEntityMapping build();
}
