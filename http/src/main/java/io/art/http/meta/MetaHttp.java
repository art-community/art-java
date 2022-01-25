package io.art.http.meta;

import static io.art.meta.model.MetaType.metaArray;
import static io.art.meta.model.MetaType.metaType;

import io.art.http.portal.*;
import io.art.meta.model.InstanceMetaMethod;
import io.art.meta.model.MetaClass;
import io.art.meta.model.MetaLibrary;
import io.art.meta.model.MetaMethod;
import io.art.meta.model.MetaPackage;
import io.art.meta.model.MetaParameter;
import io.art.meta.model.MetaProxy;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings({"all","unchecked","unused"})
public class MetaHttp extends MetaLibrary {
  private final MetaIoPackage ioPackage = register(new MetaIoPackage());

  public MetaHttp(MetaLibrary... dependencies) {
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
      private final MetaHttpPackage httpPackage = register(new MetaHttpPackage());

      private MetaArtPackage() {
        super("art");
      }

      public MetaHttpPackage httpPackage() {
        return httpPackage;
      }

      public static final class MetaHttpPackage extends MetaPackage {
        private final MetaPortalPackage portalPackage = register(new MetaPortalPackage());

        private MetaHttpPackage() {
          super("http");
        }

        public MetaPortalPackage portalPackage() {
          return portalPackage;
        }

        public static final class MetaPortalPackage extends MetaPackage {
          private final MetaHttpDefaultPortalClass httpDefaultPortalClass = register(new MetaHttpDefaultPortalClass());

          private MetaPortalPackage() {
            super("portal");
          }

          public MetaHttpDefaultPortalClass httpDefaultPortalClass() {
            return httpDefaultPortalClass;
          }

          public static final class MetaHttpDefaultPortalClass extends MetaClass<io.art.http.portal.HttpDefaultPortal> {
            private final MetaHttpExecutionCommunicatorClass httpExecutionCommunicatorClass = register(new MetaHttpExecutionCommunicatorClass());

            private MetaHttpDefaultPortalClass() {
              super(metaType(io.art.http.portal.HttpDefaultPortal.class));
            }

            @Override
            public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
              return new MetaHttpDefaultPortalProxy(invocations);
            }

            public MetaHttpExecutionCommunicatorClass httpExecutionCommunicatorClass() {
              return httpExecutionCommunicatorClass;
            }

            public class MetaHttpDefaultPortalProxy extends MetaProxy implements io.art.http.portal.HttpDefaultPortal {
              public MetaHttpDefaultPortalProxy(
                  Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                super(invocations);
              }
            }

            public static final class MetaHttpExecutionCommunicatorClass extends MetaClass<HttpDefaultPortal.HttpBuiltinCommunicator> {
              private final MetaExecuteMethod executeMethod = register(new MetaExecuteMethod());

              private MetaHttpExecutionCommunicatorClass() {
                super(metaType(HttpDefaultPortal.HttpBuiltinCommunicator.class));
              }

              public MetaExecuteMethod executeMethod() {
                return executeMethod;
              }

              @Override
              public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                return new MetaHttpExecutionCommunicatorProxy(invocations);
              }

              public static final class MetaExecuteMethod extends InstanceMetaMethod<HttpDefaultPortal.HttpBuiltinCommunicator, reactor.core.publisher.Flux<byte[]>> {
                private final MetaParameter<reactor.core.publisher.Flux<byte[]>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaArray(byte[].class, byte[]::new, metaType(byte.class)))));

                private MetaExecuteMethod() {
                  super("execute",metaType(reactor.core.publisher.Flux.class,metaArray(byte[].class, byte[]::new, metaType(byte.class))));
                }

                @Override
                public Object invoke(
                    HttpDefaultPortal.HttpBuiltinCommunicator instance,
                    Object[] arguments) throws Throwable {
                  return instance.execute((reactor.core.publisher.Flux<byte[]>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    HttpDefaultPortal.HttpBuiltinCommunicator instance,
                    Object argument) throws Throwable {
                  return instance.execute((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<byte[]>> inputParameter() {
                  return inputParameter;
                }
              }

              public class MetaHttpExecutionCommunicatorProxy extends MetaProxy implements HttpDefaultPortal.HttpBuiltinCommunicator {
                private final Function<Object, Object> executeInvocation;

                public MetaHttpExecutionCommunicatorProxy(
                    Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                  super(invocations);
                  executeInvocation = invocations.get(executeMethod);
                }

                @Override
                public reactor.core.publisher.Flux<byte[]> execute(
                    reactor.core.publisher.Flux<byte[]> input) {
                  return (reactor.core.publisher.Flux<byte[]>)(executeInvocation.apply(input));
                }
              }
            }
          }
        }
      }
    }
  }
}
