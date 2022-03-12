package io.art.storage.index;

import io.art.meta.model.*;

public interface Indexes<T> {
    Index1<T, ?> id();

    default <F1> Index1<T, F1> index(MetaField<? extends MetaClass<T>, F1> field1) {
        return new IndexOneField<>(field1);
    }

    default <F1, F2> Index2<F1, F2> index(MetaField<? extends MetaClass<T>, F1> field1, MetaField<? extends MetaClass<T>, F2> field2) {
        return new IndexTwoFields<>(field1, field2);
    }

    default <F1, F2, F3> Index3<F1, F2, F3> index(MetaField<? extends MetaClass<T>, F1> field1,
                                                  MetaField<? extends MetaClass<T>, F2> field2,
                                                  MetaField<? extends MetaClass<T>, F3> field3) {
        return new IndexThreeFields<>(field1, field2, field3);
    }

    default <F1, F2, F3, F4> Index4<F1, F2, F3, F4> index(MetaField<? extends MetaClass<T>, F1> field1,
                                                          MetaField<? extends MetaClass<T>, F2> field2,
                                                          MetaField<? extends MetaClass<T>, F3> field3,
                                                          MetaField<? extends MetaClass<T>, F4> field4) {
        return new IndexFourFields<>(field1, field2, field3, field4);
    }

    default <F1, F2, F3, F4, F5> Index5<F1, F2, F3, F4, F5> index(MetaField<? extends MetaClass<T>, F1> field1,
                                                                  MetaField<? extends MetaClass<T>, F2> field2,
                                                                  MetaField<? extends MetaClass<T>, F3> field3,
                                                                  MetaField<? extends MetaClass<T>, F4> field4,
                                                                  MetaField<? extends MetaClass<T>, F5> field5) {
        return new IndexFiveFields<>(field1, field2, field3, field4, field5);
    }
}
