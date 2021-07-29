package io.art.rsocket.test.meta;

import static io.art.meta.model.MetaType.metaArray;
import static io.art.meta.model.MetaType.metaEnum;
import static io.art.meta.model.MetaType.metaType;

import io.art.meta.model.InstanceMetaMethod;
import io.art.meta.model.MetaClass;
import io.art.meta.model.MetaConstructor;
import io.art.meta.model.MetaLibrary;
import io.art.meta.model.MetaPackage;
import io.art.meta.model.MetaParameter;
import io.art.meta.model.StaticMetaMethod;

@SuppressWarnings({"all","unchecked","unused"})
public class MetaRsocketTest extends MetaLibrary {
  private final MetaIoPackage ioPackage = register(new MetaIoPackage());

  public MetaRsocketTest(MetaLibrary... dependencies) {
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
      private final MetaRsocketPackage rsocketPackage = register(new MetaRsocketPackage());

      private MetaArtPackage() {
        super("art");
      }

      public MetaRsocketPackage rsocketPackage() {
        return rsocketPackage;
      }

      public static final class MetaRsocketPackage extends MetaPackage {
        private final MetaTestPackage testPackage = register(new MetaTestPackage());

        private MetaRsocketPackage() {
          super("rsocket");
        }

        public MetaTestPackage testPackage() {
          return testPackage;
        }

        public static final class MetaTestPackage extends MetaPackage {
          private final MetaRsocketTestClass rsocketTestClass = register(new MetaRsocketTestClass());

          private final MetaServicePackage servicePackage = register(new MetaServicePackage());

          private MetaTestPackage() {
            super("test");
          }

          public MetaRsocketTestClass rsocketTestClass() {
            return rsocketTestClass;
          }

          public MetaServicePackage servicePackage() {
            return servicePackage;
          }

          public static final class MetaRsocketTestClass extends MetaClass<io.art.rsocket.test.RsocketTest> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaSetupMethod setupMethod = register(new MetaSetupMethod());

            private final MetaTestMethod testMethod = register(new MetaTestMethod());

            private MetaRsocketTestClass() {
              super(metaType(io.art.rsocket.test.RsocketTest.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaSetupMethod setupMethod() {
              return setupMethod;
            }

            public MetaTestMethod testMethod() {
              return testMethod;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.test.RsocketTest> {
              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.test.RsocketTest.class));
              }

              @Override
              public io.art.rsocket.test.RsocketTest invoke(Object[] arguments) throws Throwable {
                return new io.art.rsocket.test.RsocketTest();
              }

              @Override
              public io.art.rsocket.test.RsocketTest invoke() throws Throwable {
                return new io.art.rsocket.test.RsocketTest();
              }
            }

            public static final class MetaSetupMethod extends StaticMetaMethod<Void> {
              private MetaSetupMethod() {
                super("setup",metaType(Void.class));
              }

              @Override
              public Object invoke(Object[] arguments) throws Throwable {
                io.art.rsocket.test.RsocketTest.setup();
                return null;
              }

              @Override
              public Object invoke() throws Throwable {
                io.art.rsocket.test.RsocketTest.setup();
                return null;
              }
            }

            public static final class MetaTestMethod extends InstanceMetaMethod<io.art.rsocket.test.RsocketTest, Void> {
              private MetaTestMethod() {
                super("test",metaType(Void.class));
              }

              @Override
              public Object invoke(io.art.rsocket.test.RsocketTest instance, Object[] arguments)
                  throws Throwable {
                instance.test();
                return null;
              }

              @Override
              public Object invoke(io.art.rsocket.test.RsocketTest instance) throws Throwable {
                instance.test();
                return null;
              }
            }
          }

          public static final class MetaServicePackage extends MetaPackage {
            private final MetaTestRsocketServiceClass testRsocketServiceClass = register(new MetaTestRsocketServiceClass());

            private MetaServicePackage() {
              super("service");
            }

            public MetaTestRsocketServiceClass testRsocketServiceClass() {
              return testRsocketServiceClass;
            }

            public static final class MetaTestRsocketServiceClass extends MetaClass<io.art.rsocket.test.service.TestRsocketService> {
              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

              private final MetaMMethod mMethod = register(new MetaMMethod());

              private MetaTestRsocketServiceClass() {
                super(metaType(io.art.rsocket.test.service.TestRsocketService.class));
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaMMethod mMethod() {
                return mMethod;
              }

              public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.test.service.TestRsocketService> {
                private MetaConstructorConstructor() {
                  super(metaType(io.art.rsocket.test.service.TestRsocketService.class));
                }

                @Override
                public io.art.rsocket.test.service.TestRsocketService invoke(Object[] arguments)
                    throws Throwable {
                  return new io.art.rsocket.test.service.TestRsocketService();
                }

                @Override
                public io.art.rsocket.test.service.TestRsocketService invoke() throws Throwable {
                  return new io.art.rsocket.test.service.TestRsocketService();
                }
              }

              public static final class MetaMMethod extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, Void> {
                private final MetaParameter<java.lang.String> reqParameter = register(new MetaParameter<>(0, "req",metaType(java.lang.String.class)));

                private MetaMMethod() {
                  super("m",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  instance.m((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object argument) throws Throwable {
                  instance.m((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> reqParameter() {
                  return reqParameter;
                }
              }
            }
          }
        }
      }
    }
  }
}
