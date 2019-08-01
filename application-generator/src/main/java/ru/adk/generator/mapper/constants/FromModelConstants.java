package ru.adk.generator.mapper.constants;

import ru.adk.entity.mapper.ValueFromModelMapper;

/**
 * Interface for fromModel variable's constants of mapper generator
 *
 * @see ValueFromModelMapper
 */
public interface FromModelConstants {
    String FROM_MODEL = "from";
    String MODEL_TO_ENTITY_LAMBDA = "model -> $T.entityBuilder()";

    String STRING_FIELD = ".stringField($N, model.$L())";
    String INT_FIELD = ".intField($N, model.$L())";
    String DOUBLE_FIELD = ".doubleField($N, model.$L())";
    String LONG_FIELD = ".longField($N, model.$L())";
    String BYTE_FIELD = ".byteField($N, model.$L())";
    String BOOL_FIELD = ".boolField($N, model.$L())";
    String DATE_FIELD = ".dateField($N, model.$L())";
    String FLOAT_FIELD = ".floatField($N, model.$L())";

    String ENUM_FILED = ".stringField($N, $T.$N(model.$L()))";
    String ENTITY_FIELD = ".entityField($N, model.$L(), $T.$N)";
    String ENTITY_COLLECTION_FIELD = ".entityCollectionField($N, model.$L(), $T.$N)";

    String STRING_COLLECTION_FIELD = ".stringCollectionField($N, model.$L())";
    String INT_COLLECTION_FIELD = ".intCollectionField($N, model.$L())";
    String DOUBLE_COLLECTION_FIELD = ".doubleCollectionField($N, model.$L())";
    String LONG_COLLECTION_FIELD = ".longCollectionField($N, model.$L())";
    String BYTE_COLLECTION_FIELD = ".byteCollectionField($N, model.$L())";
    String BOOL_COLLECTION_FIELD = ".boolCollectionField($N, model.$L())";
    String FLOAT_COLLECTION_FIELD = ".floatCollectionField($N, model.$L())";

    String MAP_FIELD_PRIMITIVE_KEY = ".mapField($N, model.$L(), $T.$N, $N)";
    String MAP_FIELD_PRIMITIVE_VALUE = ".mapField($N, model.$L(), $N, $T.$N)";
    String MAP_FIELD_PRIMITIVE_KEY_VALUE = ".mapField($N, model.$L(), $T.$N, $T.$N)";
    String MAP_FIELD = ".mapField($N, model.$L(), $N, $N)";

    String STRING_FROM_MODEL = "StringPrimitive.fromModel";
    String INT_FROM_MODEL = "IntPrimitive.fromModel";
    String LONG_FROM_MODEL = "LongPrimitive.fromModel";
    String DOUBLE_FROM_MODEL = "DoublePrimitive.fromModel";
    String BOOL_FROM_MODEL = "BoolPrimitive.fromModel";
    String BYTE_FROM_MODEL = "BytePrimitive.fromModel";
    String FLOAT_FROM_MODEL = "FloatPrimitive.fromModel";
}
