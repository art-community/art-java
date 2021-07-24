package io.art.server.test.meta;

import static io.art.meta.model.MetaType.metaArray;
import static io.art.meta.model.MetaType.metaEnum;
import static io.art.meta.model.MetaType.metaType;

import io.art.meta.model.InstanceMetaMethod;
import io.art.meta.model.MetaClass;
import io.art.meta.model.MetaConstructor;
import io.art.meta.model.MetaField;
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
          private final MetaServiceMethodSpecificationTestClass serviceMethodSpecificationTestClass = register(new MetaServiceMethodSpecificationTestClass());

          private final MetaRegistryPackage registryPackage = register(new MetaRegistryPackage());

          private final MetaServicePackage servicePackage = register(new MetaServicePackage());

          private MetaTestPackage() {
            super("test");
          }

          public MetaServiceMethodSpecificationTestClass serviceMethodSpecificationTestClass() {
            return serviceMethodSpecificationTestClass;
          }

          public MetaRegistryPackage registryPackage() {
            return registryPackage;
          }

          public MetaServicePackage servicePackage() {
            return servicePackage;
          }

          public static final class MetaServiceMethodSpecificationTestClass extends MetaClass<io.art.server.test.ServiceMethodSpecificationTest> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaSetupMethod setupMethod = register(new MetaSetupMethod());

            private final MetaTestServiceMethodExecutionMethod testServiceMethodExecutionMethod = register(new MetaTestServiceMethodExecutionMethod());

            private MetaServiceMethodSpecificationTestClass() {
              super(metaType(io.art.server.test.ServiceMethodSpecificationTest.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaSetupMethod setupMethod() {
              return setupMethod;
            }

            public MetaTestServiceMethodExecutionMethod testServiceMethodExecutionMethod() {
              return testServiceMethodExecutionMethod;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.server.test.ServiceMethodSpecificationTest> {
              private MetaConstructorConstructor() {
                super(metaType(io.art.server.test.ServiceMethodSpecificationTest.class));
              }

              @Override
              public io.art.server.test.ServiceMethodSpecificationTest invoke(
                  java.lang.Object[] arguments) throws Throwable {
                return new io.art.server.test.ServiceMethodSpecificationTest();
              }

              @Override
              public io.art.server.test.ServiceMethodSpecificationTest invoke() throws Throwable {
                return new io.art.server.test.ServiceMethodSpecificationTest();
              }
            }

            public static final class MetaSetupMethod extends StaticMetaMethod<Void> {
              private MetaSetupMethod() {
                super("setup",metaType(Void.class));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws Throwable {
                io.art.server.test.ServiceMethodSpecificationTest.setup();
                return null;
              }

              @Override
              public java.lang.Object invoke() throws Throwable {
                io.art.server.test.ServiceMethodSpecificationTest.setup();
                return null;
              }
            }

            public static final class MetaTestServiceMethodExecutionMethod extends InstanceMetaMethod<io.art.server.test.ServiceMethodSpecificationTest, Void> {
              private MetaTestServiceMethodExecutionMethod() {
                super("testServiceMethodExecution",metaType(Void.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.server.test.ServiceMethodSpecificationTest instance,
                  java.lang.Object[] arguments) throws Throwable {
                instance.testServiceMethodExecution();
                return null;
              }

              @Override
              public java.lang.Object invoke(
                  io.art.server.test.ServiceMethodSpecificationTest instance) throws Throwable {
                instance.testServiceMethodExecution();
                return null;
              }
            }
          }

          public static final class MetaRegistryPackage extends MetaPackage {
            private final MetaTestServiceExecutionRegistryClass testServiceExecutionRegistryClass = register(new MetaTestServiceExecutionRegistryClass());

            private MetaRegistryPackage() {
              super("registry");
            }

            public MetaTestServiceExecutionRegistryClass testServiceExecutionRegistryClass() {
              return testServiceExecutionRegistryClass;
            }

            public static final class MetaTestServiceExecutionRegistryClass extends MetaClass<io.art.server.test.registry.TestServiceExecutionRegistry> {
              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

              private final MetaField<java.util.Map<java.lang.String, java.lang.Object>> executionsField = register(new MetaField<>("executions",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.Object.class)),false));

              private final MetaRegisterMethod registerMethod = register(new MetaRegisterMethod());

              private final MetaExecutionsMethod executionsMethod = register(new MetaExecutionsMethod());

              private MetaTestServiceExecutionRegistryClass() {
                super(metaType(io.art.server.test.registry.TestServiceExecutionRegistry.class));
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaField<java.util.Map<java.lang.String, java.lang.Object>> executionsField(
                  ) {
                return executionsField;
              }

              public MetaRegisterMethod registerMethod() {
                return registerMethod;
              }

              public MetaExecutionsMethod executionsMethod() {
                return executionsMethod;
              }

              public static final class MetaConstructorConstructor extends MetaConstructor<io.art.server.test.registry.TestServiceExecutionRegistry> {
                private MetaConstructorConstructor() {
                  super(metaType(io.art.server.test.registry.TestServiceExecutionRegistry.class));
                }

                @Override
                public io.art.server.test.registry.TestServiceExecutionRegistry invoke(
                    java.lang.Object[] arguments) throws Throwable {
                  return new io.art.server.test.registry.TestServiceExecutionRegistry();
                }

                @Override
                public io.art.server.test.registry.TestServiceExecutionRegistry invoke() throws
                    Throwable {
                  return new io.art.server.test.registry.TestServiceExecutionRegistry();
                }
              }

              public static final class MetaRegisterMethod extends StaticMetaMethod<Void> {
                private final MetaParameter<java.lang.String> methodParameter = register(new MetaParameter<>(0, "method",metaType(java.lang.String.class)));

                private final MetaParameter<java.lang.Object> inputParameter = register(new MetaParameter<>(1, "input",metaType(java.lang.Object.class)));

                private MetaRegisterMethod() {
                  super("register",metaType(Void.class));
                }

                @Override
                public java.lang.Object invoke(java.lang.Object[] arguments) throws Throwable {
                  io.art.server.test.registry.TestServiceExecutionRegistry.register((java.lang.String)(arguments[0]),(java.lang.Object)(arguments[1]));
                  return null;
                }

                public MetaParameter<java.lang.String> methodParameter() {
                  return methodParameter;
                }

                public MetaParameter<java.lang.Object> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaExecutionsMethod extends StaticMetaMethod<java.util.Map<java.lang.String, java.lang.Object>> {
                private MetaExecutionsMethod() {
                  super("executions",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.Object.class)));
                }

                @Override
                public java.lang.Object invoke(java.lang.Object[] arguments) throws Throwable {
                  return io.art.server.test.registry.TestServiceExecutionRegistry.executions();
                }

                @Override
                public java.lang.Object invoke() throws Throwable {
                  return io.art.server.test.registry.TestServiceExecutionRegistry.executions();
                }
              }
            }
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
                public io.art.server.test.service.TestService invoke(java.lang.Object[] arguments)
                    throws Throwable {
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
                public java.lang.Object invoke(java.lang.Object[] arguments) throws Throwable {
                  io.art.server.test.service.TestService.m1();
                  return null;
                }

                @Override
                public java.lang.Object invoke() throws Throwable {
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
                public java.lang.Object invoke(java.lang.Object[] arguments) throws Throwable {
                  io.art.server.test.service.TestService.m2((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public java.lang.Object invoke(java.lang.Object argument) throws Throwable {
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
                public java.lang.Object invoke(java.lang.Object[] arguments) throws Throwable {
                  io.art.server.test.service.TestService.m3((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public java.lang.Object invoke(java.lang.Object argument) throws Throwable {
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
                public java.lang.Object invoke(java.lang.Object[] arguments) throws Throwable {
                  io.art.server.test.service.TestService.m4((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public java.lang.Object invoke(java.lang.Object argument) throws Throwable {
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
                public java.lang.Object invoke(java.lang.Object[] arguments) throws Throwable {
                  return io.art.server.test.service.TestService.m5();
                }

                @Override
                public java.lang.Object invoke() throws Throwable {
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
