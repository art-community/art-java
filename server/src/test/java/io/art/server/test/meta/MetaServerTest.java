package io.art.server.test.meta;

import static io.art.meta.model.MetaType.metaArray;
import static io.art.meta.model.MetaType.metaEnum;
import static io.art.meta.model.MetaType.metaType;

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
            private final MetaTestServiceClass testServiceClass = register(new MetaTestServiceClass());

            private MetaServicePackage() {
              super("service");
            }

            public MetaTestServiceClass testServiceClass() {
              return testServiceClass;
            }

            public static final class MetaTestServiceClass extends MetaClass<io.art.server.test.service.TestService> {
              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

              private final MetaM1Method m1Method = register(new MetaM1Method());

              private final MetaM2Method m2Method = register(new MetaM2Method());

              private final MetaM3Method m3Method = register(new MetaM3Method());

              private final MetaM4Method m4Method = register(new MetaM4Method());

              private final MetaM5Method m5Method = register(new MetaM5Method());

              private MetaTestServiceClass() {
                super(metaType(io.art.server.test.service.TestService.class));
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

              public static final class MetaM2Method extends StaticMetaMethod<Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM2Method() {
                  super("m2",metaType(Void.class));
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  io.art.server.test.service.TestService.m2((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  io.art.server.test.service.TestService.m2((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaM3Method extends StaticMetaMethod<Void> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM3Method() {
                  super("m3",metaType(Void.class));
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  io.art.server.test.service.TestService.m3((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  io.art.server.test.service.TestService.m3((reactor.core.publisher.Mono)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaM4Method extends StaticMetaMethod<Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM4Method() {
                  super("m4",metaType(Void.class));
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  io.art.server.test.service.TestService.m4((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(Object argument) throws Throwable {
                  io.art.server.test.service.TestService.m4((reactor.core.publisher.Flux)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaM5Method extends StaticMetaMethod<java.lang.String> {
                private MetaM5Method() {
                  super("m5",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(Object[] arguments) throws Throwable {
                  return io.art.server.test.service.TestService.m5();
                }

                @Override
                public Object invoke() throws Throwable {
                  return io.art.server.test.service.TestService.m5();
                }
              }
            }
          }
        }
      }
    }
  }
}
