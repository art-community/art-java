package ru.adk.entity.constants;

public interface ValueMappingExceptionMessages {
    String FROM_MODULE_MAPPER_IS_NULL = "FromModel mapper is null";
    String TO_MODULE_MAPPER_IS_NULL = "ToModel mapper is null";
    String MAPPER_IS_NULL = "Mapper mapper is null";
    String VALUE_TYPE_IS_NULL = "Value type is null";
    String PRIMITIVE_TYPE_IS_NULL = "Primitive type is null";
    String PRIMITIVE_TYPE_IS_NULL_DURING_PARSING = "PrimitiveType is null during parsing from string";
    String NOT_PRIMITIVE_TYPE = "Not primitive type: ''{0}''";
    String NFL_COLLECTIONS_ELEMENTS = "Not collection elements type: ''{0}''";
    String XML_TAG_IS_UNFILLED = "Xml tag is unfilled";
    String REQUEST_LIST_ELEMENTS_TYPE_INVALID = "Trying to receive list of type: ''{0}'' but collection elements type is ''{1}''";
    String REQUEST_SET_ELEMENTS_TYPE_INVALID = "Trying to receive set of type: ''{0}'' but collection elements type is ''{1}''";
    String REQUEST_QUEUE_ELEMENTS_TYPE_INVALID = "Trying to receive queue of type: ''{0}'' but collection elements type is ''{1}''";
    String UNABLE_TO_PARSE_DATE = "Unable to parse date";
}
