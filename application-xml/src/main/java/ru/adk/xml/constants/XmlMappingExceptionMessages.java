package ru.adk.xml.constants;

public interface XmlMappingExceptionMessages {
    String XML_ENTITY_IS_NULL = "Xml entity is null";
    String XML_MAPPING_RULES_ARE_EMPTY = "Xml mapping rules are empty";
    String XML_IS_EMPTY = "Xml is empty";
    String XML_FACTORY_IS_NULL = "Xml factory is null";
    String INCORRECT_XML_STRUCTURE = "Incorrect XML structure";
    String XML_FILE_HASNT_END_DOCUMENT_TAG = "XML file hasn't END_DOCUMENT tag";
    String SAME_INPUT_OUTPUT = "Forbidden to have a rules with same input or output. Incorrect rule: ''{0}''";
    String UNEXPECTED_PARSED_PART_IN_RULE = "Unexpected parsed part ''{0}'' in rule: ''{1}''";
}
