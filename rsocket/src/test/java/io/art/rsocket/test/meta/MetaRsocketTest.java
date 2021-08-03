package io.art.rsocket.test.meta;

import static io.art.meta.model.MetaType.metaArray;
import static io.art.meta.model.MetaType.metaEnum;
import static io.art.meta.model.MetaType.metaType;

import io.art.meta.model.InstanceMetaMethod;
import io.art.meta.model.MetaClass;
import io.art.meta.model.MetaConstructor;
import io.art.meta.model.MetaLibrary;
import io.art.meta.model.MetaMethod;
import io.art.meta.model.MetaPackage;
import io.art.meta.model.MetaParameter;
import io.art.meta.model.MetaProxy;
import io.art.meta.model.StaticMetaMethod;
import java.util.Map;
import java.util.function.Function;

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

          private final MetaCommunicatorPackage communicatorPackage = register(new MetaCommunicatorPackage());

          private final MetaServicePackage servicePackage = register(new MetaServicePackage());

          private MetaTestPackage() {
            super("test");
          }

          public MetaRsocketTestClass rsocketTestClass() {
            return rsocketTestClass;
          }

          public MetaCommunicatorPackage communicatorPackage() {
            return communicatorPackage;
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

          public static final class MetaCommunicatorPackage extends MetaPackage {
            private final MetaTestRsockClass testRsockClass = register(new MetaTestRsockClass());

            private final MetaTestRsocketClass testRsocketClass = register(new MetaTestRsocketClass());

            private final MetaTestRsocketConnectorClass testRsocketConnectorClass = register(new MetaTestRsocketConnectorClass());

            private MetaCommunicatorPackage() {
              super("communicator");
            }

            public MetaTestRsockClass testRsockClass() {
              return testRsockClass;
            }

            public MetaTestRsocketClass testRsocketClass() {
              return testRsocketClass;
            }

            public MetaTestRsocketConnectorClass testRsocketConnectorClass() {
              return testRsocketConnectorClass;
            }

            public static final class MetaTestRsockClass extends MetaClass<io.art.rsocket.test.communicator.TestRsock> {
              private final MetaMMethod mMethod = register(new MetaMMethod());

              private MetaTestRsockClass() {
                super(metaType(io.art.rsocket.test.communicator.TestRsock.class));
              }

              public MetaMMethod mMethod() {
                return mMethod;
              }

              @Override
              public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                return new MetaTestRsockProxy(invocations);
              }

              public static final class MetaMMethod extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsock, Void> {
                private final MetaParameter<java.lang.String> reqParameter = register(new MetaParameter<>(0, "req",metaType(java.lang.String.class)));

                private MetaMMethod() {
                  super("m",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsock instance,
                    Object[] arguments) throws Throwable {
                  instance.m((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsock instance,
                    Object argument) throws Throwable {
                  instance.m((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> reqParameter() {
                  return reqParameter;
                }
              }

              public class MetaTestRsockProxy extends MetaProxy implements io.art.rsocket.test.communicator.TestRsock {
                private final Function<Object, Object> mInvocation;

                public MetaTestRsockProxy(
                    Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                  super(invocations);
                  mInvocation = invocations.get(mMethod);
                }

                @Override
                public void m(java.lang.String req) {
                  mInvocation.apply(req);
                }
              }
            }

            public static final class MetaTestRsocketClass extends MetaClass<io.art.rsocket.test.communicator.TestRsocket> {
              private final MetaMMethod mMethod = register(new MetaMMethod());

              private final MetaM2Method m2Method = register(new MetaM2Method());

              private MetaTestRsocketClass() {
                super(metaType(io.art.rsocket.test.communicator.TestRsocket.class));
              }

              public MetaMMethod mMethod() {
                return mMethod;
              }

              public MetaM2Method m2Method() {
                return m2Method;
              }

              @Override
              public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                return new MetaTestRsocketProxy(invocations);
              }

              public static final class MetaMMethod extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, Void> {
                private final MetaParameter<java.lang.String> reqParameter = register(new MetaParameter<>(0, "req",metaType(java.lang.String.class)));

                private MetaMMethod() {
                  super("m",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  instance.m((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object argument) throws Throwable {
                  instance.m((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> reqParameter() {
                  return reqParameter;
                }
              }

              public static final class MetaM2Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, Void> {
                private final MetaParameter<java.lang.String> reqParameter = register(new MetaParameter<>(0, "req",metaType(java.lang.String.class)));

                private MetaM2Method() {
                  super("m2",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  instance.m2((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object argument) throws Throwable {
                  instance.m2((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> reqParameter() {
                  return reqParameter;
                }
              }

              public class MetaTestRsocketProxy extends MetaProxy implements io.art.rsocket.test.communicator.TestRsocket {
                private final Function<Object, Object> mInvocation;

                private final Function<Object, Object> m2Invocation;

                public MetaTestRsocketProxy(
                    Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                  super(invocations);
                  mInvocation = invocations.get(mMethod);
                  m2Invocation = invocations.get(m2Method);
                }

                @Override
                public void m(java.lang.String req) {
                  mInvocation.apply(req);
                }

                @Override
                public void m2(java.lang.String req) {
                  m2Invocation.apply(req);
                }
              }
            }

            public static final class MetaTestRsocketConnectorClass extends MetaClass<io.art.rsocket.test.communicator.TestRsocketConnector> {
              private final MetaTestRsocketMethod testRsocketMethod = register(new MetaTestRsocketMethod());

              private final MetaTestOtherRsocketMethod testOtherRsocketMethod = register(new MetaTestOtherRsocketMethod());

              private MetaTestRsocketConnectorClass() {
                super(metaType(io.art.rsocket.test.communicator.TestRsocketConnector.class));
              }

              public MetaTestRsocketMethod testRsocketMethod() {
                return testRsocketMethod;
              }

              public MetaTestOtherRsocketMethod testOtherRsocketMethod() {
                return testOtherRsocketMethod;
              }

              @Override
              public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                return new MetaTestRsocketConnectorProxy(invocations);
              }

              public static final class MetaTestRsocketMethod extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocketConnector, io.art.rsocket.test.communicator.TestRsocket> {
                private MetaTestRsocketMethod() {
                  super("testRsocket",metaType(io.art.rsocket.test.communicator.TestRsocket.class));
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocketConnector instance,
                    Object[] arguments) throws Throwable {
                  return instance.testRsocket();
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocketConnector instance)
                    throws Throwable {
                  return instance.testRsocket();
                }
              }

              public static final class MetaTestOtherRsocketMethod extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocketConnector, io.art.rsocket.test.communicator.TestRsock> {
                private MetaTestOtherRsocketMethod() {
                  super("testOtherRsocket",metaType(io.art.rsocket.test.communicator.TestRsock.class));
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocketConnector instance,
                    Object[] arguments) throws Throwable {
                  return instance.testOtherRsocket();
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocketConnector instance)
                    throws Throwable {
                  return instance.testOtherRsocket();
                }
              }

              public class MetaTestRsocketConnectorProxy extends MetaProxy implements io.art.rsocket.test.communicator.TestRsocketConnector {
                private final Function<Object, Object> testRsocketInvocation;

                private final Function<Object, Object> testOtherRsocketInvocation;

                public MetaTestRsocketConnectorProxy(
                    Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                  super(invocations);
                  testRsocketInvocation = invocations.get(testRsocketMethod);
                  testOtherRsocketInvocation = invocations.get(testOtherRsocketMethod);
                }

                @Override
                public io.art.rsocket.test.communicator.TestRsocket testRsocket() {
                  return (io.art.rsocket.test.communicator.TestRsocket)(testRsocketInvocation.apply(null));
                }

                @Override
                public io.art.rsocket.test.communicator.TestRsock testOtherRsocket() {
                  return (io.art.rsocket.test.communicator.TestRsock)(testOtherRsocketInvocation.apply(null));
                }
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

              private final MetaM2Method m2Method = register(new MetaM2Method());

              private MetaTestRsocketServiceClass() {
                super(metaType(io.art.rsocket.test.service.TestRsocketService.class));
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaMMethod mMethod() {
                return mMethod;
              }

              public MetaM2Method m2Method() {
                return m2Method;
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

              public static final class MetaM2Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, Void> {
                private final MetaParameter<java.lang.String> reqParameter = register(new MetaParameter<>(0, "req",metaType(java.lang.String.class)));

                private MetaM2Method() {
                  super("m2",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  instance.m2((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object argument) throws Throwable {
                  instance.m2((java.lang.String)(argument));
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
