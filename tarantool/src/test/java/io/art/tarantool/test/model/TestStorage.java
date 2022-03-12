package io.art.tarantool.test.model;

import io.art.meta.test.*;
import io.art.storage.index.*;
import io.art.tarantool.communicator.*;
import lombok.*;
import lombok.experimental.*;
import reactor.core.publisher.*;
import static io.art.meta.test.meta.MetaMetaTest.MetaIoPackage.MetaArtPackage.MetaMetaPackage.MetaTestPackage.MetaTestingMetaModelClass.*;
import static io.art.tarantool.test.meta.MetaTarantoolTest.MetaIoPackage.MetaArtPackage.MetaTarantoolPackage.MetaTestPackage.MetaModelPackage.MetaOtherSpaceClass.*;

public interface TestStorage extends TarantoolStorage<TestStorage> {
    void testSubscription();

    Flux<String> testChannel();

    String testMapper(TestData input);

    boolean testFilter(TestData input);

    @Getter
    @Accessors(fluent = true)
    class TestModelIndexes implements Indexes<TestingMetaModel> {
        private final Index1<TestingMetaModel, Integer> id = index(testingMetaModel().f1Field());
        private final Index2<Integer, String> f9f16 = index(testingMetaModel().f9Field(), testingMetaModel().f16Field());
    }

    @Getter
    @Accessors(fluent = true)
    class OtherSpaceIndexes implements Indexes<OtherSpace> {
        private final Index1<OtherSpace, Integer> id = index(otherSpace().keyField());
        private final Index2<String, Integer> valueNumber = index(otherSpace().valueField(), otherSpace().numberField());
    }
}
