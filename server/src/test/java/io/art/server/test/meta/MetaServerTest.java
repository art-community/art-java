package io.art.server.test.meta;

import static io.art.meta.model.MetaType.metaArray;
import static io.art.meta.model.MetaType.metaEnum;
import static io.art.meta.model.MetaType.metaType;

import io.art.core.property.LazyProperty;
import io.art.meta.model.MetaClass;
import io.art.meta.model.MetaConstructor;
import io.art.meta.model.MetaLibrary;
import io.art.meta.model.MetaPackage;
import io.art.meta.model.MetaParameter;
import io.art.meta.model.StaticMetaMethod;

@SuppressWarnings({"all","unchecked","unused"})
public class MetaServerTest extends MetaLibrary {
  private final MetaIoPackage ioPackage = register(new MetaIoPackage());

  public MetaServerTest(MetaLibrary... dependencies) {
    super(dependencies);
  }

  public MetaIoPackage ioPackage() {
    return ioPackage;
  }

  public static final class MetaIoPackage extends MetaPackage {
    private final MetaArtPackage artPackage = register(new MetaArtPackage());

    private MetaIoPackage() {
      super("io");
    }

    public MetaArtPackage artPackage() {
      return artPackage;
    }

    public static final class MetaArtPackage extends MetaPackage {
      private final MetaServerPackage serverPackage = register(new MetaServerPackage());

      private MetaArtPackage() {
        super("art");
      }

      public MetaServerPackage serverPackage() {
        return serverPackage;
      }

      public static final class MetaServerPackage extends MetaPackage {
        private final MetaTestPackage testPackage = register(new MetaTestPackage());

        private MetaServerPackage() {
          super("server");
        }

        public MetaTestPackage testPackage() {
          return testPackage;
        }

        public static final class MetaTestPackage extends MetaPackage {
          private final MetaServicePackage servicePackage = register(new MetaServicePackage());

          private MetaTestPackage() {
            super("test");
          }

          public MetaServicePackage servicePackage() {
            return servicePackage;
          }

          public static final class MetaServicePackage extends MetaPackage {
            private final MetaBenchmarkServiceClass benchmarkServiceClass = register(new MetaBenchmarkServiceClass());

            private final MetaTestServiceClass testServiceClass = register(new MetaTestServiceClass());

            private MetaServicePackage() {
              super("service");
            }

            public MetaBenchmarkServiceClass benchmarkServiceClass() {
              return benchmarkServiceClass;
            }

            public MetaTestServiceClass testServiceClass() {
              return testServiceClass;
            }

            public static final class MetaBenchmarkServiceClass extends MetaClass<io.art.server.test.service.BenchmarkService> {
              private static final LazyProperty<MetaBenchmarkServiceClass> self = MetaClass.self(io.art.server.test.service.BenchmarkService.class);

              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor(this));

              private final MetaM1Method m1Method = register(new MetaM1Method(this));

              private final MetaM2Method m2Method = register(new MetaM2Method(this));

              private final MetaM3Method m3Method = register(new MetaM3Method(this));

              private final MetaM4Method m4Method = register(new MetaM4Method(this));

              private final MetaM5Method m5Method = register(new MetaM5Method(this));

              private final MetaM6Method m6Method = register(new MetaM6Method(this));

              private final MetaM7Method m7Method = register(new MetaM7Method(this));

              private final MetaM8Method m8Method = register(new MetaM8Method(this));

              private final MetaM9Method m9Method = register(new MetaM9Method(this));

              private final MetaM10Method m10Method = register(new MetaM10Method(this));

              private final MetaM11Method m11Method = register(new MetaM11Method(this));

              private final MetaM12Method m12Method = register(new MetaM12Method(this));

              private final MetaM13Method m13Method = register(new MetaM13Method(this));

              private final MetaM14Method m14Method = register(new MetaM14Method(this));

              private final MetaM15Method m15Method = register(new MetaM15Method(this));

              private final MetaM16Method m16Method = register(new MetaM16Method(this));

              private MetaBenchmarkServiceClass() {
                super(metaType(io.art.server.test.service.BenchmarkService.class));
              }

              public static MetaBenchmarkServiceClass benchmarkService() {
                return self.get();
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaM1Method m1Method() {
                return m1Method;
              }

              public MetaM2Method m2Method() {
                return m2Method;
              }

              public MetaM3Method m3Method() {
                return m3Method;
              }

              public MetaM4Method m4Method() {
                return m4Method;
              }

              public MetaM5Method m5Method() {
                return m5Method;
              }

              public MetaM6Method m6Method() {
                return m6Method;
              }

              public MetaM7Method m7Method() {
                return m7Method;
              }

              public MetaM8Method m8Method() {
                return m8Method;
              }

              public MetaM9Method m9Method() {
                return m9Method;
              }

              public MetaM10Method m10Method() {
                return m10Method;
              }

              public MetaM11Method m11Method() {
                return m11Method;
              }

              public MetaM12Method m12Method() {
                return m12Method;
              }

              public MetaM13Method m13Method() {
                return m13Method;
              }

              public MetaM14Method m14Method() {
                return m14Method;
              }

              public MetaM15Method m15Method() {
                return m15Method;
              }

              public MetaM16Method m16Method() {
                return m16Method;
              }

              public final class MetaConstructorConstructor extends MetaConstructor<MetaBenchmarkServiceClass, io.art.server.test.service.BenchmarkService> {
                private MetaConstructorConstructor(MetaBenchmarkServiceClass owner) {
                  super(metaType(io.art.server.test.service.BenchmarkService.class),owner);
                }

                @Override
                public io.art.server.test.service.BenchmarkService invoke(Object[] arguments) throws
                    Throwable {
                  return new io.art.server.test.service.BenchmarkService();
                }

                @Override
                public io.art.server.test.service.BenchmarkService invoke() throws Throwable {
                  return new io.art.server.test.service.BenchmarkService();
                }
              }

              public final class MetaM1Method extends StaticMetaMethod<MetaBenchmarkServiceClass, Void> {
                private MetaM1Method(MetaBenchmarkServiceClass owner) {
                  super("m1",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  io.art.server.test.service.BenchmarkService.m1();
                  return null;
                }

                @Override
                public Object invoke() throws Throwable {
                  io.art.server.test.service.BenchmarkService.m1();
                  return null;
                }
              }

              public final class MetaM2Method extends StaticMetaMethod<MetaBenchmarkServiceClass, java.lang.String> {
                private MetaM2Method(MetaBenchmarkServiceClass owner) {
                  super("m2",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m2();
                }

                @Override
                public Object invoke() throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m2();
                }
              }

              public final class MetaM3Method extends StaticMetaMethod<MetaBenchmarkServiceClass, reactor.core.publisher.Mono<java.lang.String>> {
                private MetaM3Method(MetaBenchmarkServiceClass owner) {
                  super("m3",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m3();
                }

                @Override
                public Object invoke() throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m3();
                }
              }

              public final class MetaM4Method extends StaticMetaMethod<MetaBenchmarkServiceClass, reactor.core.publisher.Flux<java.lang.String>> {
                private MetaM4Method(MetaBenchmarkServiceClass owner) {
                  super("m4",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m4();
                }

                @Override
                public Object invoke() throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m4();
                }
              }

              public final class MetaM5Method extends StaticMetaMethod<MetaBenchmarkServiceClass, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM5Method(MetaBenchmarkServiceClass owner) {
                  super("m5",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  io.art.server.test.service.BenchmarkService.m5((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  io.art.server.test.service.BenchmarkService.m5((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM6Method extends StaticMetaMethod<MetaBenchmarkServiceClass, java.lang.String> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM6Method(MetaBenchmarkServiceClass owner) {
                  super("m6",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m6((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m6((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM7Method extends StaticMetaMethod<MetaBenchmarkServiceClass, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM7Method(MetaBenchmarkServiceClass owner) {
                  super("m7",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m7((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m7((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM8Method extends StaticMetaMethod<MetaBenchmarkServiceClass, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM8Method(MetaBenchmarkServiceClass owner) {
                  super("m8",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m8((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m8((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM9Method extends StaticMetaMethod<MetaBenchmarkServiceClass, Void> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM9Method(MetaBenchmarkServiceClass owner) {
                  super("m9",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  io.art.server.test.service.BenchmarkService.m9((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  io.art.server.test.service.BenchmarkService.m9((reactor.core.publisher.Mono)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM10Method extends StaticMetaMethod<MetaBenchmarkServiceClass, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM10Method(MetaBenchmarkServiceClass owner) {
                  super("m10",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m10((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m10((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM11Method extends StaticMetaMethod<MetaBenchmarkServiceClass, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM11Method(MetaBenchmarkServiceClass owner) {
                  super("m11",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m11((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m11((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM12Method extends StaticMetaMethod<MetaBenchmarkServiceClass, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM12Method(MetaBenchmarkServiceClass owner) {
                  super("m12",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m12((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m12((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM13Method extends StaticMetaMethod<MetaBenchmarkServiceClass, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM13Method(MetaBenchmarkServiceClass owner) {
                  super("m13",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  io.art.server.test.service.BenchmarkService.m13((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  io.art.server.test.service.BenchmarkService.m13((reactor.core.publisher.Flux)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM14Method extends StaticMetaMethod<MetaBenchmarkServiceClass, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM14Method(MetaBenchmarkServiceClass owner) {
                  super("m14",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m14((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m14((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM15Method extends StaticMetaMethod<MetaBenchmarkServiceClass, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM15Method(MetaBenchmarkServiceClass owner) {
                  super("m15",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m15((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m15((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM16Method extends StaticMetaMethod<MetaBenchmarkServiceClass, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM16Method(MetaBenchmarkServiceClass owner) {
                  super("m16",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m16((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  return io.art.server.test.service.BenchmarkService.m16((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }
            }

            public static final class MetaTestServiceClass extends MetaClass<io.art.server.test.service.TestService> {
              private static final LazyProperty<MetaTestServiceClass> self = MetaClass.self(io.art.server.test.service.TestService.class);

              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor(this));

              private final MetaM1Method m1Method = register(new MetaM1Method(this));

              private final MetaM2Method m2Method = register(new MetaM2Method(this));

              private final MetaM3Method m3Method = register(new MetaM3Method(this));

              private final MetaM4Method m4Method = register(new MetaM4Method(this));

              private final MetaM5Method m5Method = register(new MetaM5Method(this));

              private final MetaM6Method m6Method = register(new MetaM6Method(this));

              private final MetaM7Method m7Method = register(new MetaM7Method(this));

              private final MetaM8Method m8Method = register(new MetaM8Method(this));

              private final MetaM9Method m9Method = register(new MetaM9Method(this));

              private final MetaM10Method m10Method = register(new MetaM10Method(this));

              private final MetaM11Method m11Method = register(new MetaM11Method(this));

              private final MetaM12Method m12Method = register(new MetaM12Method(this));

              private final MetaM13Method m13Method = register(new MetaM13Method(this));

              private final MetaM14Method m14Method = register(new MetaM14Method(this));

              private final MetaM15Method m15Method = register(new MetaM15Method(this));

              private final MetaM16Method m16Method = register(new MetaM16Method(this));

              private MetaTestServiceClass() {
                super(metaType(io.art.server.test.service.TestService.class));
              }

              public static MetaTestServiceClass testService() {
                return self.get();
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaM1Method m1Method() {
                return m1Method;
              }

              public MetaM2Method m2Method() {
                return m2Method;
              }

              public MetaM3Method m3Method() {
                return m3Method;
              }

              public MetaM4Method m4Method() {
                return m4Method;
              }

              public MetaM5Method m5Method() {
                return m5Method;
              }

              public MetaM6Method m6Method() {
                return m6Method;
              }

              public MetaM7Method m7Method() {
                return m7Method;
              }

              public MetaM8Method m8Method() {
                return m8Method;
              }

              public MetaM9Method m9Method() {
                return m9Method;
              }

              public MetaM10Method m10Method() {
                return m10Method;
              }

              public MetaM11Method m11Method() {
                return m11Method;
              }

              public MetaM12Method m12Method() {
                return m12Method;
              }

              public MetaM13Method m13Method() {
                return m13Method;
              }

              public MetaM14Method m14Method() {
                return m14Method;
              }

              public MetaM15Method m15Method() {
                return m15Method;
              }

              public MetaM16Method m16Method() {
                return m16Method;
              }

              public final class MetaConstructorConstructor extends MetaConstructor<MetaTestServiceClass, io.art.server.test.service.TestService> {
                private MetaConstructorConstructor(MetaTestServiceClass owner) {
                  super(metaType(io.art.server.test.service.TestService.class),owner);
                }

                @Override
                public io.art.server.test.service.TestService invoke(Object[] arguments) throws
                    Throwable {
                  return new io.art.server.test.service.TestService();
                }

                @Override
                public io.art.server.test.service.TestService invoke() throws Throwable {
                  return new io.art.server.test.service.TestService();
                }
              }

              public final class MetaM1Method extends StaticMetaMethod<MetaTestServiceClass, Void> {
                private MetaM1Method(MetaTestServiceClass owner) {
                  super("m1",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  io.art.server.test.service.TestService.m1();
                  return null;
                }

                @Override
                public Object invoke() throws Throwable {
                  io.art.server.test.service.TestService.m1();
                  return null;
                }
              }

              public final class MetaM2Method extends StaticMetaMethod<MetaTestServiceClass, java.lang.String> {
                private MetaM2Method(MetaTestServiceClass owner) {
                  super("m2",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.TestService.m2();
                }

                @Override
                public Object invoke() throws Throwable {
                  return io.art.server.test.service.TestService.m2();
                }
              }

              public final class MetaM3Method extends StaticMetaMethod<MetaTestServiceClass, reactor.core.publisher.Mono<java.lang.String>> {
                private MetaM3Method(MetaTestServiceClass owner) {
                  super("m3",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.TestService.m3();
                }

                @Override
                public Object invoke() throws Throwable {
                  return io.art.server.test.service.TestService.m3();
                }
              }

              public final class MetaM4Method extends StaticMetaMethod<MetaTestServiceClass, reactor.core.publisher.Flux<java.lang.String>> {
                private MetaM4Method(MetaTestServiceClass owner) {
                  super("m4",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.TestService.m4();
                }

                @Override
                public Object invoke() throws Throwable {
                  return io.art.server.test.service.TestService.m4();
                }
              }

              public final class MetaM5Method extends StaticMetaMethod<MetaTestServiceClass, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM5Method(MetaTestServiceClass owner) {
                  super("m5",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  io.art.server.test.service.TestService.m5((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  io.art.server.test.service.TestService.m5((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM6Method extends StaticMetaMethod<MetaTestServiceClass, java.lang.String> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM6Method(MetaTestServiceClass owner) {
                  super("m6",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.TestService.m6((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  return io.art.server.test.service.TestService.m6((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM7Method extends StaticMetaMethod<MetaTestServiceClass, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM7Method(MetaTestServiceClass owner) {
                  super("m7",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.TestService.m7((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  return io.art.server.test.service.TestService.m7((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM8Method extends StaticMetaMethod<MetaTestServiceClass, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM8Method(MetaTestServiceClass owner) {
                  super("m8",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.TestService.m8((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  return io.art.server.test.service.TestService.m8((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM9Method extends StaticMetaMethod<MetaTestServiceClass, Void> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM9Method(MetaTestServiceClass owner) {
                  super("m9",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  io.art.server.test.service.TestService.m9((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  io.art.server.test.service.TestService.m9((reactor.core.publisher.Mono)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM10Method extends StaticMetaMethod<MetaTestServiceClass, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM10Method(MetaTestServiceClass owner) {
                  super("m10",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.TestService.m10((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  return io.art.server.test.service.TestService.m10((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM11Method extends StaticMetaMethod<MetaTestServiceClass, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM11Method(MetaTestServiceClass owner) {
                  super("m11",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.TestService.m11((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  return io.art.server.test.service.TestService.m11((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM12Method extends StaticMetaMethod<MetaTestServiceClass, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM12Method(MetaTestServiceClass owner) {
                  super("m12",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.TestService.m12((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  return io.art.server.test.service.TestService.m12((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM13Method extends StaticMetaMethod<MetaTestServiceClass, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM13Method(MetaTestServiceClass owner) {
                  super("m13",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  io.art.server.test.service.TestService.m13((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  io.art.server.test.service.TestService.m13((reactor.core.publisher.Flux)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM14Method extends StaticMetaMethod<MetaTestServiceClass, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM14Method(MetaTestServiceClass owner) {
                  super("m14",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.TestService.m14((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  return io.art.server.test.service.TestService.m14((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM15Method extends StaticMetaMethod<MetaTestServiceClass, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM15Method(MetaTestServiceClass owner) {
                  super("m15",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.TestService.m15((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  return io.art.server.test.service.TestService.m15((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM16Method extends StaticMetaMethod<MetaTestServiceClass, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM16Method(MetaTestServiceClass owner) {
                  super("m16",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.TestService.m16((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  return io.art.server.test.service.TestService.m16((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }
            }
          }
        }
      }
    }
  }
}
