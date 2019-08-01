package ru.art.entity;

import ru.art.entity.mapper.ValueFromModelMapper.PrimitiveFromModelMapper;
import ru.art.entity.mapper.ValueMapper;
import ru.art.entity.mapper.ValueToModelMapper.PrimitiveToModelMapper;
import ru.art.entity.mapper.ValueToModelMapper.StringDataPrimitiveToModelMapper;
import static ru.art.core.caster.Caster.cast;
import static ru.art.entity.PrimitivesFactory.primitiveFromString;
import static ru.art.entity.constants.ValueType.PrimitiveType.*;
import static ru.art.entity.mapper.ValueFromModelMapper.StringDataPrimitiveFromModelMapper;
import static ru.art.entity.mapper.ValueMapper.mapper;

public interface PrimitiveMapping {
    ValueMapper<String, Primitive> stringMapper = mapper(StringPrimitive.fromModel, StringPrimitive.toModel);
    ValueMapper<Integer, Primitive> intMapper = mapper(IntPrimitive.fromModel, IntPrimitive.toModel);
    ValueMapper<Long, Primitive> longMapper = mapper(LongPrimitive.fromModel, LongPrimitive.toModel);
    ValueMapper<Double, Primitive> doubleMapper = mapper(DoublePrimitive.fromModel, DoublePrimitive.toModel);
    ValueMapper<Boolean, Primitive> boolMapper = mapper(BoolPrimitive.fromModel, BoolPrimitive.toModel);
    ValueMapper<Byte, Primitive> byteMapper = mapper(BytePrimitive.fromModel, BytePrimitive.toModel);
    ValueMapper<Float, Primitive> floatMapper = mapper(FloatPrimitive.fromModel, FloatPrimitive.toModel);

    ValueMapper<Integer, Primitive> intFromStringMapper = mapper(cast(StringPrimitive.intFromStringModel), IntPrimitive.toModel);
    ValueMapper<Double, Primitive> doubleFromStringMapper = mapper(cast(StringPrimitive.doubleFromStringModel), DoublePrimitive.toModel);
    ValueMapper<Long, Primitive> longFromStringMapper = mapper(cast(StringPrimitive.longFromStringModel), LongPrimitive.toModel);
    ValueMapper<Boolean, Primitive> boolFromStringMapper = mapper(cast(StringPrimitive.boolFromStringModel), BoolPrimitive.toModel);
    ValueMapper<Byte, Primitive> byteFromStringMapper = mapper(cast(StringPrimitive.byteFromStringModel), BytePrimitive.toModel);
    ValueMapper<Float, Primitive> floatFromStringMapper = mapper(cast(StringPrimitive.floatFromStringModel), FloatPrimitive.toModel);

    interface StringPrimitive {
        PrimitiveFromModelMapper<String> fromModel = PrimitivesFactory::stringPrimitive;
        PrimitiveToModelMapper<String> toModel = Primitive::getString;

        StringDataPrimitiveFromModelMapper intFromStringModel = str -> primitiveFromString(str, INT);
        StringDataPrimitiveFromModelMapper longFromStringModel = str -> primitiveFromString(str, LONG);
        StringDataPrimitiveFromModelMapper doubleFromStringModel = str -> primitiveFromString(str, DOUBLE);
        StringDataPrimitiveFromModelMapper boolFromStringModel = str -> primitiveFromString(str, BOOL);
        StringDataPrimitiveFromModelMapper byteFromStringModel = str -> primitiveFromString(str, BYTE);
        StringDataPrimitiveFromModelMapper floatFromStringModel = str -> primitiveFromString(str, FLOAT);
    }

    interface IntPrimitive {
        PrimitiveFromModelMapper<Integer> fromModel = PrimitivesFactory::intPrimitive;
        PrimitiveToModelMapper<Integer> toModel = Primitive::getInt;

        StringDataPrimitiveToModelMapper<Integer> parseToModel = Primitive::parseInt;
    }

    interface LongPrimitive {
        PrimitiveFromModelMapper<Long> fromModel = PrimitivesFactory::longPrimitive;
        PrimitiveToModelMapper<Long> toModel = Primitive::getLong;

        StringDataPrimitiveToModelMapper<Long> parseToModel = Primitive::parseLong;
    }

    interface DoublePrimitive {
        PrimitiveFromModelMapper<Double> fromModel = PrimitivesFactory::doublePrimitive;
        PrimitiveToModelMapper<Double> toModel = Primitive::getDouble;

        StringDataPrimitiveToModelMapper<Double> parseToModel = Primitive::parseDouble;
    }

    interface BoolPrimitive {
        PrimitiveFromModelMapper<Boolean> fromModel = PrimitivesFactory::boolPrimitive;
        PrimitiveToModelMapper<Boolean> toModel = Primitive::getBool;

        StringDataPrimitiveToModelMapper<Boolean> parseToModel = Primitive::parseBool;
    }

    interface BytePrimitive {
        PrimitiveFromModelMapper<Byte> fromModel = PrimitivesFactory::bytePrimitive;
        PrimitiveToModelMapper<Byte> toModel = Primitive::getByte;

        StringDataPrimitiveToModelMapper<Byte> parseToModel = Primitive::parseByte;
    }

    interface FloatPrimitive {
        PrimitiveFromModelMapper<Float> fromModel = PrimitivesFactory::floatPrimitive;
        PrimitiveToModelMapper<Float> toModel = Primitive::getFloat;

        StringDataPrimitiveToModelMapper<Float> parseToModel = Primitive::parseFloat;
    }
}
