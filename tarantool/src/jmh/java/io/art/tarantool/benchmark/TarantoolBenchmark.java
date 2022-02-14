/*
 * ART
 *
 * Copyright 2019-2022 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.tarantool.benchmark;

import io.art.meta.test.*;
import io.art.meta.test.meta.*;
import io.art.storage.*;
import io.art.tarantool.*;
import io.art.tarantool.test.meta.*;
import io.art.tarantool.test.model.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.test.meta.MetaMetaTest.MetaIoPackage.MetaArtPackage.MetaMetaPackage.MetaTestPackage.MetaTestingShortMetaModelClass.*;
import static io.art.tarantool.model.TarantoolIndexConfiguration.*;
import static io.art.tarantool.model.TarantoolIndexPartConfiguration.*;
import static io.art.tarantool.model.TarantoolSpaceConfiguration.*;
import static io.art.tarantool.module.TarantoolActivator.*;
import static io.art.tarantool.test.constants.TestTarantoolConstants.*;
import static io.art.tarantool.test.storage.TestTarantoolStorage.*;
import static io.art.transport.module.TransportActivator.*;
import static java.util.concurrent.TimeUnit.*;
import static org.openjdk.jmh.annotations.Mode.*;

@Fork(value = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
@BenchmarkMode(Throughput)
@OutputTimeUnit(SECONDS)
public class TarantoolBenchmark {
    @State(value = Scope.Benchmark)
    public static class BenchmarkState {
        SpaceService<Integer, TestingShortMetaModel> space;
        TestingShortMetaModel model;

        @Setup
        public void setup() {
            initializeStorage();
            initialize(logging(),
                    meta(() -> new MetaTarantoolTest(new MetaMetaTest())),
                    transport(),
                    tarantool(tarantool -> tarantool
                            .storage(TestStorage.class, storage -> storage.client(client -> client
                                    .port(STORAGE_PORT)
                                    .username(USERNAME)
                                    .logging(true)
                                    .password(PASSWORD)))
                            .space(TestStorage.class, int.class, TestingShortMetaModel.class)
                    )
            );
            Tarantool.tarantool()
                    .schema(TestStorage.class)
                    .createSpace(spaceFor(TestingShortMetaModel.class).ifNotExists(true).build())
                    .createIndex(indexFor(TestingShortMetaModel.class, testingShortMetaModel().idField())
                            .ifNotExists(true)
                            .unique(true)
                            .part(indexPartFor(testingShortMetaModel().idField()))
                            .build());
            space = Tarantool.tarantool().space(TestingShortMetaModel.class);
            model = TestingShortMetaModel.builder().id(1).name("test").inner(TestingShortMetaModel.Inner.builder().id(2).name("test").build()).build();
            space.put(model);
        }
    }

    @Benchmark
    public void putBenchmark(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.space.reactive().findFirst(1).subscribe(blackhole::consume));
    }
}
