package io.art.model.implementation.storage;

import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

import static io.art.core.factory.ListFactory.linkedListOf;

@Getter
public class TarantoolIndexModel {
    private final List<Class<?>> fieldTypes;

    public TarantoolIndexModel(Class<?>... fieldTypes){
        this.fieldTypes = linkedListOf(fieldTypes);
    }
}
