package io.art.tarantool.test.factory;

import io.art.tarantool.test.model.*;
import lombok.experimental.*;

@UtilityClass
public class TarantoolTestDataFactory {
    public static TestData testData(int id) {
        return TestData.builder()
                .id(id)
                .content("content: " + id)
                .inner(TestData.Inner.builder().content("inner: " + id).build())
                .build();
    }
}
