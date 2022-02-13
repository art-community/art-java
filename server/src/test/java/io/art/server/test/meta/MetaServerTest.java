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

              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

              private final MetaM1Method m1Method = register(new MetaM1Method());

              private final MetaM2Method m2Method = register(new MetaM2Method());

              private final MetaM3Method m3Method = register(new MetaM3Method());

              private final MetaM4Method m4Method = register(new MetaM4Method());

              private final MetaM5Method m5Method = register(new MetaM5Method());

              private final MetaM6Method m6Method = register(new MetaM6Method());

              private final MetaM7Method m7Method = register(new MetaM7Method());

              private final MetaM8Method m8Method = register(new MetaM8Method());

              private final MetaM9Method m9Method = register(new MetaM9Method());

              private final MetaM10Method m10Method = register(new MetaM10Method());

              private final MetaM11Method m11Method = register(new MetaM11Method());

              private final MetaM12Method m12Method = register(new MetaM12Method());

              private final MetaM13Method m13Method = register(new MetaM13Method());

              private final MetaM14Method m14Method = register(new MetaM14Method());

              private final MetaM15Method m15Method = register(new MetaM15Method());

              private final MetaM16Method m16Method = register(new MetaM16Method());

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

              public static final class MetaConstructorConstructor extends MetaConstructor<io.art.server.test.service.BenchmarkService> {
                private MetaConstructorConstructor() {
                  super(metaType(io.art.server.test.service.BenchmarkService.class));
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

              public static final class MetaM1Method extends StaticMetaMethod<Void> {
                private MetaM1Method() {
                  super("m1",metaType(Void.class));
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

              public static final class MetaM2Method extends StaticMetaMethod<java.lang.String> {
                private MetaM2Method() {
                  super("m2",metaType(java.lang.String.class));
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

              public static final class MetaM3Method extends StaticMetaMethod<reactor.core.publisher.Mono<java.lang.String>> {
                private MetaM3Method() {
                  super("m3",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
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

              public static final class MetaM4Method extends StaticMetaMethod<reactor.core.publisher.Flux<java.lang.String>> {
                private MetaM4Method() {
                  super("m4",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
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

              public static final class MetaM5Method extends StaticMetaMethod<Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM5Method() {
                  super("m5",metaType(Void.class));
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

              public static final class MetaM6Method extends StaticMetaMethod<java.lang.String> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM6Method() {
                  super("m6",metaType(java.lang.String.class));
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

              public static final class MetaM7Method extends StaticMetaMethod<reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM7Method() {
                  super("m7",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
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

              public static final class MetaM8Method extends StaticMetaMethod<reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM8Method() {
                  super("m8",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
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

              public static final class MetaM9Method extends StaticMetaMethod<Void> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM9Method() {
                  super("m9",metaType(Void.class));
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

              public static final class MetaM10Method extends StaticMetaMethod<java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM10Method() {
                  super("m10",metaType(java.lang.String.class));
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

              public static final class MetaM11Method extends StaticMetaMethod<reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM11Method() {
                  super("m11",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
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

              public static final class MetaM12Method extends StaticMetaMethod<reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM12Method() {
                  super("m12",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
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

              public static final class MetaM13Method extends StaticMetaMethod<Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM13Method() {
                  super("m13",metaType(Void.class));
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

              public static final class MetaM14Method extends StaticMetaMethod<java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM14Method() {
                  super("m14",metaType(java.lang.String.class));
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

              public static final class MetaM15Method extends StaticMetaMethod<reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM15Method() {
                  super("m15",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
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

              public static final class MetaM16Method extends StaticMetaMethod<reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM16Method() {
                  super("m16",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
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

              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

              private final MetaM1Method m1Method = register(new MetaM1Method());

              private final MetaM2Method m2Method = register(new MetaM2Method());

              private final MetaM3Method m3Method = register(new MetaM3Method());

              private final MetaM4Method m4Method = register(new MetaM4Method());

              private final MetaM5Method m5Method = register(new MetaM5Method());

              private final MetaM6Method m6Method = register(new MetaM6Method());

              private final MetaM7Method m7Method = register(new MetaM7Method());

              private final MetaM8Method m8Method = register(new MetaM8Method());

              private final MetaM9Method m9Method = register(new MetaM9Method());

              private final MetaM10Method m10Method = register(new MetaM10Method());

              private final MetaM11Method m11Method = register(new MetaM11Method());

              private final MetaM12Method m12Method = register(new MetaM12Method());

              private final MetaM13Method m13Method = register(new MetaM13Method());

              private final MetaM14Method m14Method = register(new MetaM14Method());

              private final MetaM15Method m15Method = register(new MetaM15Method());

              private final MetaM16Method m16Method = register(new MetaM16Method());

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

              public static final class MetaConstructorConstructor extends MetaConstructor<io.art.server.test.service.TestService> {
                private MetaConstructorConstructor() {
                  super(metaType(io.art.server.test.service.TestService.class));
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

              public static final class MetaM1Method extends StaticMetaMethod<Void> {
                private MetaM1Method() {
                  super("m1",metaType(Void.class));
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

              public static final class MetaM2Method extends StaticMetaMethod<java.lang.String> {
                private MetaM2Method() {
                  super("m2",metaType(java.lang.String.class));
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

              public static final class MetaM3Method extends StaticMetaMethod<reactor.core.publisher.Mono<java.lang.String>> {
                private MetaM3Method() {
                  super("m3",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
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

              public static final class MetaM4Method extends StaticMetaMethod<reactor.core.publisher.Flux<java.lang.String>> {
                private MetaM4Method() {
                  super("m4",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
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

              public static final class MetaM5Method extends StaticMetaMethod<Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM5Method() {
                  super("m5",metaType(Void.class));
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

              public static final class MetaM6Method extends StaticMetaMethod<java.lang.String> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM6Method() {
                  super("m6",metaType(java.lang.String.class));
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

              public static final class MetaM7Method extends StaticMetaMethod<reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM7Method() {
                  super("m7",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
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

              public static final class MetaM8Method extends StaticMetaMethod<reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM8Method() {
                  super("m8",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
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

              public static final class MetaM9Method extends StaticMetaMethod<Void> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM9Method() {
                  super("m9",metaType(Void.class));
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

              public static final class MetaM10Method extends StaticMetaMethod<java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM10Method() {
                  super("m10",metaType(java.lang.String.class));
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

              public static final class MetaM11Method extends StaticMetaMethod<reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM11Method() {
                  super("m11",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
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

              public static final class MetaM12Method extends StaticMetaMethod<reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM12Method() {
                  super("m12",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
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

              public static final class MetaM13Method extends StaticMetaMethod<Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM13Method() {
                  super("m13",metaType(Void.class));
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

              public static final class MetaM14Method extends StaticMetaMethod<java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM14Method() {
                  super("m14",metaType(java.lang.String.class));
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

              public static final class MetaM15Method extends StaticMetaMethod<reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM15Method() {
                  super("m15",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
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

              public static final class MetaM16Method extends StaticMetaMethod<reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM16Method() {
                  super("m16",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
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
