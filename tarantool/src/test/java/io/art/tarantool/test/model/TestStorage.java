package io.art.tarantool.test.model;

import io.art.meta.test.*;
import io.art.storage.index.*;
import io.art.storage.sharder.*;
import io.art.tarantool.communicator.*;
import lombok.*;
import lombok.experimental.*;
import reactor.core.publisher.*;
import static io.art.meta.test.meta.MetaMetaTest.MetaIoPackage.MetaArtPackage.MetaMetaPackage.MetaTestPackage.MetaTestingMetaModelClass.*;
import static io.art.tarantool.Tarantool.*;
import static io.art.tarantool.test.meta.MetaTarantoolTest.MetaIoPackage.MetaArtPackage.MetaTarantoolPackage.MetaTestPackage.MetaModelPackage.MetaOtherSpaceClass.*;

public interface TestStorage extends TarantoolStorage<TestStorage> {
    void testSubscription();

    Flux<String> testChannel();

    String testMapper(TestingMetaModel input);

    boolean testFilter(TestingMetaModel input);

    @Getter
    @Accessors(fluent = true)
    class TestModelIndexes implements Indexes<TestingMetaModel> {
        private final Index1<TestingMetaModel, Integer> id = index(testingMetaModel().f1Field());
        private final Index2<TestingMetaModel, Integer, String> f9f16 = index(testingMetaModel().f9Field(), testingMetaModel().f16Field());
    }

    @Getter
    @Accessors(fluent = true)
    class TestModelSharders implements Sharders<TestingMetaModel> {
        private final Sharder1<TestingMetaModel, String> string = this::sharder;
    }

    static TestModelIndexes testModelIndexes() {
        return tarantool().indexes(TestStorage.class, TestingMetaModel.class);
    }

    static TestModelSharders testModelSharders() {
        return tarantool().sharders(TestStorage.class, TestingMetaModel.class);
    }

    @Getter
    @Accessors(fluent = true)
    class OtherSpaceIndexes implements Indexes<OtherSpace> {
        private final Index1<OtherSpace, Integer> id = index(otherSpace().keyField());
        private final Index2<OtherSpace, String, Integer> valueNumber = index(otherSpace().valueField(), otherSpace().numberField());
    }

    static OtherSpaceIndexes otherSpaceIndexes() {
        return tarantool().indexes(TestStorage.class, OtherSpace.class);
    }
}
