package io.art.communicator.test.meta;

import static io.art.meta.model.MetaType.metaArray;
import static io.art.meta.model.MetaType.metaEnum;
import static io.art.meta.model.MetaType.metaType;

import io.art.meta.model.InstanceMetaMethod;
import io.art.meta.model.MetaClass;
import io.art.meta.model.MetaLibrary;
import io.art.meta.model.MetaPackage;
import io.art.meta.model.MetaParameter;

@SuppressWarnings({"all","unchecked","unused"})
public class MetaCommunicatorTest extends MetaLibrary {
  private final MetaIoPackage ioPackage = register(new MetaIoPackage());

  public MetaCommunicatorTest(MetaLibrary... dependencies) {
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
      private final MetaCommunicatorPackage communicatorPackage = register(new MetaCommunicatorPackage());

      private MetaArtPackage() {
        super("art");
      }

      public MetaCommunicatorPackage communicatorPackage() {
        return communicatorPackage;
      }

      public static final class MetaCommunicatorPackage extends MetaPackage {
        private final MetaTestPackage testPackage = register(new MetaTestPackage());

        private MetaCommunicatorPackage() {
          super("communicator");
        }

        public MetaTestPackage testPackage() {
          return testPackage;
        }

        public static final class MetaTestPackage extends MetaPackage {
          private final MetaProxyPackage proxyPackage = register(new MetaProxyPackage());

          private MetaTestPackage() {
            super("test");
          }

          public MetaProxyPackage proxyPackage() {
            return proxyPackage;
          }

          public static final class MetaProxyPackage extends MetaPackage {
            private final MetaTestCommunicatorClass testCommunicatorClass = register(new MetaTestCommunicatorClass());

            private MetaProxyPackage() {
              super("proxy");
            }

            public MetaTestCommunicatorClass testCommunicatorClass() {
              return testCommunicatorClass;
            }

            public static final class MetaTestCommunicatorClass extends MetaClass<io.art.communicator.test.proxy.TestCommunicator> {
              private final MetaM1Method m1Method = register(new MetaM1Method());

              private MetaTestCommunicatorClass() {
                super(metaType(io.art.communicator.test.proxy.TestCommunicator.class));
              }

              public MetaM1Method m1Method() {
                return m1Method;
              }

              public static final class MetaM1Method extends InstanceMetaMethod<io.art.communicator.test.proxy.TestCommunicator, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM1Method() {
                  super("m1",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  return instance.m1((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object argument) throws Throwable {
                  return instance.m1((reactor.core.publisher.Flux)(argument));
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
