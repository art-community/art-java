package io.art.http.meta;

import static io.art.meta.model.MetaType.metaArray;
import static io.art.meta.model.MetaType.metaType;

import io.art.core.property.LazyProperty;
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
  private final MetaIoPackage ioPackage = registerPackage(new MetaIoPackage());

  public MetaHttp(MetaLibrary... dependencies) {
    super(dependencies);
  }

  public MetaIoPackage ioPackage() {
    return ioPackage;
  }

  public static final class MetaIoPackage extends MetaPackage {
    private final MetaArtPackage artPackage = registerPackage(new MetaArtPackage());

    private MetaIoPackage() {
      super("io");
    }

    public MetaArtPackage artPackage() {
      return artPackage;
    }

    public static final class MetaArtPackage extends MetaPackage {
      private final MetaHttpPackage httpPackage = registerPackage(new MetaHttpPackage());

      private MetaArtPackage() {
        super("art");
      }

      public MetaHttpPackage httpPackage() {
        return httpPackage;
      }

      public static final class MetaHttpPackage extends MetaPackage {
        private final MetaCommunicatorPackage communicatorPackage = registerPackage(new MetaCommunicatorPackage());

        private MetaHttpPackage() {
          super("http");
        }

        public MetaCommunicatorPackage communicatorPackage() {
          return communicatorPackage;
        }

        public static final class MetaCommunicatorPackage extends MetaPackage {
          private final MetaHttpBuiltinCommunicatorClass httpBuiltinCommunicatorClass = registerClass(new MetaHttpBuiltinCommunicatorClass());

          private MetaCommunicatorPackage() {
            super("communicator");
          }

          public MetaHttpBuiltinCommunicatorClass httpBuiltinCommunicatorClass() {
            return httpBuiltinCommunicatorClass;
          }

          public static final class MetaHttpBuiltinCommunicatorClass extends MetaClass<io.art.http.communicator.HttpBuiltinCommunicator> {
            private static final LazyProperty<MetaHttpBuiltinCommunicatorClass> self = MetaClass.self(io.art.http.communicator.HttpBuiltinCommunicator.class);

            private final MetaExecuteMethod executeMethod = registerMethod(new MetaExecuteMethod(this));

            private final MetaDecorateMethod decorateMethod = registerMethod(new MetaDecorateMethod(this));

            private MetaHttpBuiltinCommunicatorClass() {
              super(metaType(io.art.http.communicator.HttpBuiltinCommunicator.class));
            }

            public static MetaHttpBuiltinCommunicatorClass httpBuiltinCommunicator() {
              return self.get();
            }

            public MetaExecuteMethod executeMethod() {
              return executeMethod;
            }

            public MetaDecorateMethod decorateMethod() {
              return decorateMethod;
            }

            @Override
            public MetaProxy proxy(
                Map<MetaMethod<MetaClass<?>, ?>, Function<Object, Object>> invocations) {
              return new MetaHttpBuiltinCommunicatorProxy(invocations);
            }

            public final class MetaExecuteMethod extends InstanceMetaMethod<MetaHttpBuiltinCommunicatorClass, io.art.http.communicator.HttpBuiltinCommunicator, reactor.core.publisher.Flux<byte[]>> {
              private final MetaParameter<reactor.core.publisher.Flux<byte[]>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaArray(byte[].class, byte[]::new, metaType(byte.class)))));

              private MetaExecuteMethod(MetaHttpBuiltinCommunicatorClass owner) {
                super("execute",metaType(reactor.core.publisher.Flux.class,metaArray(byte[].class, byte[]::new, metaType(byte.class))),owner);
              }

              @Override
              public Object invoke(io.art.http.communicator.HttpBuiltinCommunicator instance,
                  Object[] arguments) throws Throwable {
                return instance.execute((reactor.core.publisher.Flux<byte[]>)(arguments[0]));
              }

              @Override
              public Object invoke(io.art.http.communicator.HttpBuiltinCommunicator instance,
                  Object argument) throws Throwable {
                return instance.execute((reactor.core.publisher.Flux)(argument));
              }

              public MetaParameter<reactor.core.publisher.Flux<byte[]>> inputParameter() {
                return inputParameter;
              }
            }

            public final class MetaDecorateMethod extends InstanceMetaMethod<MetaHttpBuiltinCommunicatorClass, io.art.http.communicator.HttpBuiltinCommunicator, io.art.http.communicator.HttpBuiltinCommunicator> {
              private final MetaParameter<java.util.function.UnaryOperator<io.art.http.communicator.HttpCommunicationDecorator>> decoratorParameter = register(new MetaParameter<>(0, "decorator",metaType(java.util.function.UnaryOperator.class,metaType(io.art.http.communicator.HttpCommunicationDecorator.class))));

              private MetaDecorateMethod(MetaHttpBuiltinCommunicatorClass owner) {
                super("decorate",metaType(io.art.http.communicator.HttpBuiltinCommunicator.class),owner);
              }

              @Override
              public Object invoke(io.art.http.communicator.HttpBuiltinCommunicator instance,
                  Object[] arguments) throws Throwable {
                return instance.decorate((java.util.function.UnaryOperator<io.art.http.communicator.HttpCommunicationDecorator>)(arguments[0]));
              }

              @Override
              public Object invoke(io.art.http.communicator.HttpBuiltinCommunicator instance,
                  Object argument) throws Throwable {
                return instance.decorate((java.util.function.UnaryOperator)(argument));
              }

              public MetaParameter<java.util.function.UnaryOperator<io.art.http.communicator.HttpCommunicationDecorator>> decoratorParameter(
                  ) {
                return decoratorParameter;
              }
            }

            public class MetaHttpBuiltinCommunicatorProxy extends MetaProxy implements io.art.http.communicator.HttpBuiltinCommunicator {
              private final Function<Object, Object> executeInvocation;

              public MetaHttpBuiltinCommunicatorProxy(
                  Map<MetaMethod<MetaClass<?>, ?>, Function<Object, Object>> invocations) {
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
