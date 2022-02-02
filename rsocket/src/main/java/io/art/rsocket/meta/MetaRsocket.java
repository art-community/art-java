package io.art.rsocket.meta;

import static io.art.meta.model.MetaType.metaArray;
import static io.art.meta.model.MetaType.metaEnum;
import static io.art.meta.model.MetaType.metaType;

import io.art.meta.model.InstanceMetaMethod;
import io.art.meta.model.MetaClass;
import io.art.meta.model.MetaConstructor;
import io.art.meta.model.MetaField;
import io.art.meta.model.MetaLibrary;
import io.art.meta.model.MetaMethod;
import io.art.meta.model.MetaPackage;
import io.art.meta.model.MetaParameter;
import io.art.meta.model.MetaProxy;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings({"all","unchecked","unused"})
public class MetaRsocket extends MetaLibrary {
  private final MetaIoPackage ioPackage = register(new MetaIoPackage());

  public MetaRsocket(MetaLibrary... dependencies) {
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
        private final MetaModelPackage modelPackage = register(new MetaModelPackage());

        private final MetaPortalPackage portalPackage = register(new MetaPortalPackage());

        private MetaRsocketPackage() {
          super("rsocket");
        }

        public MetaModelPackage modelPackage() {
          return modelPackage;
        }

        public MetaPortalPackage portalPackage() {
          return portalPackage;
        }

        public static final class MetaModelPackage extends MetaPackage {
          private final MetaRsocketSetupPayloadClass rsocketSetupPayloadClass = register(new MetaRsocketSetupPayloadClass());

          private MetaModelPackage() {
            super("model");
          }

          public MetaRsocketSetupPayloadClass rsocketSetupPayloadClass() {
            return rsocketSetupPayloadClass;
          }

          public static final class MetaRsocketSetupPayloadClass extends MetaClass<io.art.rsocket.model.RsocketSetupPayload> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> dataFormatField = register(new MetaField<>("dataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf),false));

            private final MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> metadataFormatField = register(new MetaField<>("metadataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf),false));

            private final MetaField<java.lang.String> serviceIdField = register(new MetaField<>("serviceId",metaType(java.lang.String.class),false));

            private final MetaField<java.lang.String> methodIdField = register(new MetaField<>("methodId",metaType(java.lang.String.class),false));

            private final MetaToBuilderMethod toBuilderMethod = register(new MetaToBuilderMethod());

            private final MetaGetDataFormatMethod getDataFormatMethod = register(new MetaGetDataFormatMethod());

            private final MetaGetMetadataFormatMethod getMetadataFormatMethod = register(new MetaGetMetadataFormatMethod());

            private final MetaGetServiceIdMethod getServiceIdMethod = register(new MetaGetServiceIdMethod());

            private final MetaGetMethodIdMethod getMethodIdMethod = register(new MetaGetMethodIdMethod());

            private final MetaRsocketSetupPayloadBuilderClass rsocketSetupPayloadBuilderClass = register(new MetaRsocketSetupPayloadBuilderClass());

            private MetaRsocketSetupPayloadClass() {
              super(metaType(io.art.rsocket.model.RsocketSetupPayload.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> dataFormatField(
                ) {
              return dataFormatField;
            }

            public MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> metadataFormatField(
                ) {
              return metadataFormatField;
            }

            public MetaField<java.lang.String> serviceIdField() {
              return serviceIdField;
            }

            public MetaField<java.lang.String> methodIdField() {
              return methodIdField;
            }

            public MetaToBuilderMethod toBuilderMethod() {
              return toBuilderMethod;
            }

            public MetaGetDataFormatMethod getDataFormatMethod() {
              return getDataFormatMethod;
            }

            public MetaGetMetadataFormatMethod getMetadataFormatMethod() {
              return getMetadataFormatMethod;
            }

            public MetaGetServiceIdMethod getServiceIdMethod() {
              return getServiceIdMethod;
            }

            public MetaGetMethodIdMethod getMethodIdMethod() {
              return getMethodIdMethod;
            }

            public MetaRsocketSetupPayloadBuilderClass rsocketSetupPayloadBuilderClass() {
              return rsocketSetupPayloadBuilderClass;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.model.RsocketSetupPayload> {
              private final MetaParameter<io.art.transport.constants.TransportModuleConstants.DataFormat> dataFormatParameter = register(new MetaParameter<>(0, "dataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf)));

              private final MetaParameter<io.art.transport.constants.TransportModuleConstants.DataFormat> metadataFormatParameter = register(new MetaParameter<>(1, "metadataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf)));

              private final MetaParameter<java.lang.String> serviceIdParameter = register(new MetaParameter<>(2, "serviceId",metaType(java.lang.String.class)));

              private final MetaParameter<java.lang.String> methodIdParameter = register(new MetaParameter<>(3, "methodId",metaType(java.lang.String.class)));

              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.model.RsocketSetupPayload.class));
              }

              @Override
              public io.art.rsocket.model.RsocketSetupPayload invoke(java.lang.Object[] arguments)
                  throws Throwable {
                return new io.art.rsocket.model.RsocketSetupPayload((io.art.transport.constants.TransportModuleConstants.DataFormat)(arguments[0]),(io.art.transport.constants.TransportModuleConstants.DataFormat)(arguments[1]),(java.lang.String)(arguments[2]),(java.lang.String)(arguments[3]));
              }

              public MetaParameter<io.art.transport.constants.TransportModuleConstants.DataFormat> dataFormatParameter(
                  ) {
                return dataFormatParameter;
              }

              public MetaParameter<io.art.transport.constants.TransportModuleConstants.DataFormat> metadataFormatParameter(
                  ) {
                return metadataFormatParameter;
              }

              public MetaParameter<java.lang.String> serviceIdParameter() {
                return serviceIdParameter;
              }

              public MetaParameter<java.lang.String> methodIdParameter() {
                return methodIdParameter;
              }
            }

            public static final class MetaToBuilderMethod extends InstanceMetaMethod<io.art.rsocket.model.RsocketSetupPayload, io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder> {
              private MetaToBuilderMethod() {
                super("toBuilder",metaType(io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.model.RsocketSetupPayload instance,
                  java.lang.Object[] arguments) throws Throwable {
                return instance.toBuilder();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.model.RsocketSetupPayload instance)
                  throws Throwable {
                return instance.toBuilder();
              }
            }

            public static final class MetaGetDataFormatMethod extends InstanceMetaMethod<io.art.rsocket.model.RsocketSetupPayload, io.art.transport.constants.TransportModuleConstants.DataFormat> {
              private MetaGetDataFormatMethod() {
                super("getDataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.model.RsocketSetupPayload instance,
                  java.lang.Object[] arguments) throws Throwable {
                return instance.getDataFormat();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.model.RsocketSetupPayload instance)
                  throws Throwable {
                return instance.getDataFormat();
              }
            }

            public static final class MetaGetMetadataFormatMethod extends InstanceMetaMethod<io.art.rsocket.model.RsocketSetupPayload, io.art.transport.constants.TransportModuleConstants.DataFormat> {
              private MetaGetMetadataFormatMethod() {
                super("getMetadataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.model.RsocketSetupPayload instance,
                  java.lang.Object[] arguments) throws Throwable {
                return instance.getMetadataFormat();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.model.RsocketSetupPayload instance)
                  throws Throwable {
                return instance.getMetadataFormat();
              }
            }

            public static final class MetaGetServiceIdMethod extends InstanceMetaMethod<io.art.rsocket.model.RsocketSetupPayload, java.lang.String> {
              private MetaGetServiceIdMethod() {
                super("getServiceId",metaType(java.lang.String.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.model.RsocketSetupPayload instance,
                  java.lang.Object[] arguments) throws Throwable {
                return instance.getServiceId();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.model.RsocketSetupPayload instance)
                  throws Throwable {
                return instance.getServiceId();
              }
            }

            public static final class MetaGetMethodIdMethod extends InstanceMetaMethod<io.art.rsocket.model.RsocketSetupPayload, java.lang.String> {
              private MetaGetMethodIdMethod() {
                super("getMethodId",metaType(java.lang.String.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.model.RsocketSetupPayload instance,
                  java.lang.Object[] arguments) throws Throwable {
                return instance.getMethodId();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.model.RsocketSetupPayload instance)
                  throws Throwable {
                return instance.getMethodId();
              }
            }

            public static final class MetaRsocketSetupPayloadBuilderClass extends MetaClass<io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder> {
              private final MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> dataFormatField = register(new MetaField<>("dataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf),false));

              private final MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> metadataFormatField = register(new MetaField<>("metadataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf),false));

              private final MetaField<java.lang.String> serviceIdField = register(new MetaField<>("serviceId",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> methodIdField = register(new MetaField<>("methodId",metaType(java.lang.String.class),false));

              private final MetaDataFormatMethod dataFormatMethod = register(new MetaDataFormatMethod());

              private final MetaMetadataFormatMethod metadataFormatMethod = register(new MetaMetadataFormatMethod());

              private final MetaServiceIdMethod serviceIdMethod = register(new MetaServiceIdMethod());

              private final MetaMethodIdMethod methodIdMethod = register(new MetaMethodIdMethod());

              private final MetaBuildMethod buildMethod = register(new MetaBuildMethod());

              private MetaRsocketSetupPayloadBuilderClass() {
                super(metaType(io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder.class));
              }

              public MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> dataFormatField(
                  ) {
                return dataFormatField;
              }

              public MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> metadataFormatField(
                  ) {
                return metadataFormatField;
              }

              public MetaField<java.lang.String> serviceIdField() {
                return serviceIdField;
              }

              public MetaField<java.lang.String> methodIdField() {
                return methodIdField;
              }

              public MetaDataFormatMethod dataFormatMethod() {
                return dataFormatMethod;
              }

              public MetaMetadataFormatMethod metadataFormatMethod() {
                return metadataFormatMethod;
              }

              public MetaServiceIdMethod serviceIdMethod() {
                return serviceIdMethod;
              }

              public MetaMethodIdMethod methodIdMethod() {
                return methodIdMethod;
              }

              public MetaBuildMethod buildMethod() {
                return buildMethod;
              }

              public static final class MetaDataFormatMethod extends InstanceMetaMethod<io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder, io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder> {
                private final MetaParameter<io.art.transport.constants.TransportModuleConstants.DataFormat> dataFormatParameter = register(new MetaParameter<>(0, "dataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf)));

                private MetaDataFormatMethod() {
                  super("dataFormat",metaType(io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.dataFormat((io.art.transport.constants.TransportModuleConstants.DataFormat)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    java.lang.Object argument) throws Throwable {
                  return instance.dataFormat((io.art.transport.constants.TransportModuleConstants.DataFormat)(argument));
                }

                public MetaParameter<io.art.transport.constants.TransportModuleConstants.DataFormat> dataFormatParameter(
                    ) {
                  return dataFormatParameter;
                }
              }

              public static final class MetaMetadataFormatMethod extends InstanceMetaMethod<io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder, io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder> {
                private final MetaParameter<io.art.transport.constants.TransportModuleConstants.DataFormat> metadataFormatParameter = register(new MetaParameter<>(0, "metadataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf)));

                private MetaMetadataFormatMethod() {
                  super("metadataFormat",metaType(io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.metadataFormat((io.art.transport.constants.TransportModuleConstants.DataFormat)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    java.lang.Object argument) throws Throwable {
                  return instance.metadataFormat((io.art.transport.constants.TransportModuleConstants.DataFormat)(argument));
                }

                public MetaParameter<io.art.transport.constants.TransportModuleConstants.DataFormat> metadataFormatParameter(
                    ) {
                  return metadataFormatParameter;
                }
              }

              public static final class MetaServiceIdMethod extends InstanceMetaMethod<io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder, io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder> {
                private final MetaParameter<java.lang.String> serviceIdParameter = register(new MetaParameter<>(0, "serviceId",metaType(java.lang.String.class)));

                private MetaServiceIdMethod() {
                  super("serviceId",metaType(io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.serviceId((java.lang.String)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    java.lang.Object argument) throws Throwable {
                  return instance.serviceId((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> serviceIdParameter() {
                  return serviceIdParameter;
                }
              }

              public static final class MetaMethodIdMethod extends InstanceMetaMethod<io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder, io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder> {
                private final MetaParameter<java.lang.String> methodIdParameter = register(new MetaParameter<>(0, "methodId",metaType(java.lang.String.class)));

                private MetaMethodIdMethod() {
                  super("methodId",metaType(io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.methodId((java.lang.String)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    java.lang.Object argument) throws Throwable {
                  return instance.methodId((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> methodIdParameter() {
                  return methodIdParameter;
                }
              }

              public static final class MetaBuildMethod extends InstanceMetaMethod<io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder, io.art.rsocket.model.RsocketSetupPayload> {
                private MetaBuildMethod() {
                  super("build",metaType(io.art.rsocket.model.RsocketSetupPayload.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.build();
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance)
                    throws Throwable {
                  return instance.build();
                }
              }
            }
          }
        }

        public static final class MetaPortalPackage extends MetaPackage {
          private final MetaRsocketDefaultPortalClass rsocketDefaultPortalClass = register(new MetaRsocketDefaultPortalClass());

          private MetaPortalPackage() {
            super("portal");
          }

          public MetaRsocketDefaultPortalClass rsocketDefaultPortalClass() {
            return rsocketDefaultPortalClass;
          }

          public static final class MetaRsocketDefaultPortalClass extends MetaClass<io.art.rsocket.portal.RsocketDefaultPortal> {
            private final MetaRsocketBuiltinCommunicatorClass rsocketBuiltinCommunicatorClass = register(new MetaRsocketBuiltinCommunicatorClass());

            private MetaRsocketDefaultPortalClass() {
              super(metaType(io.art.rsocket.portal.RsocketDefaultPortal.class));
            }

            @Override
            public MetaProxy proxy(
                Map<MetaMethod<?>, Function<java.lang.Object, java.lang.Object>> invocations) {
              return new MetaRsocketDefaultPortalProxy(invocations);
            }

            public MetaRsocketBuiltinCommunicatorClass rsocketBuiltinCommunicatorClass() {
              return rsocketBuiltinCommunicatorClass;
            }

            public class MetaRsocketDefaultPortalProxy extends MetaProxy implements io.art.rsocket.portal.RsocketDefaultPortal {
              public MetaRsocketDefaultPortalProxy(
                  Map<MetaMethod<?>, Function<java.lang.Object, java.lang.Object>> invocations) {
                super(invocations);
              }
            }

            public static final class MetaRsocketBuiltinCommunicatorClass extends MetaClass<io.art.rsocket.portal.RsocketDefaultPortal.RsocketBuiltinCommunicator> {
              private final MetaFireAndForgetMethod fireAndForgetMethod = register(new MetaFireAndForgetMethod());

              private final MetaRequestResponseMethod requestResponseMethod = register(new MetaRequestResponseMethod());

              private final MetaRequestStreamMethod requestStreamMethod = register(new MetaRequestStreamMethod());

              private final MetaRequestChannelMethod requestChannelMethod = register(new MetaRequestChannelMethod());

              private MetaRsocketBuiltinCommunicatorClass() {
                super(metaType(io.art.rsocket.portal.RsocketDefaultPortal.RsocketBuiltinCommunicator.class));
              }

              public MetaFireAndForgetMethod fireAndForgetMethod() {
                return fireAndForgetMethod;
              }

              public MetaRequestResponseMethod requestResponseMethod() {
                return requestResponseMethod;
              }

              public MetaRequestStreamMethod requestStreamMethod() {
                return requestStreamMethod;
              }

              public MetaRequestChannelMethod requestChannelMethod() {
                return requestChannelMethod;
              }

              @Override
              public MetaProxy proxy(
                  Map<MetaMethod<?>, Function<java.lang.Object, java.lang.Object>> invocations) {
                return new MetaRsocketBuiltinCommunicatorProxy(invocations);
              }

              public static final class MetaFireAndForgetMethod extends InstanceMetaMethod<io.art.rsocket.portal.RsocketDefaultPortal.RsocketBuiltinCommunicator, Void> {
                private final MetaParameter<reactor.core.publisher.Mono<byte[]>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaArray(byte[].class, byte[]::new, metaType(byte.class)))));

                private MetaFireAndForgetMethod() {
                  super("fireAndForget",metaType(Void.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.portal.RsocketDefaultPortal.RsocketBuiltinCommunicator instance,
                    java.lang.Object[] arguments) throws Throwable {
                  instance.fireAndForget((reactor.core.publisher.Mono<byte[]>)(arguments[0]));
                  return null;
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.portal.RsocketDefaultPortal.RsocketBuiltinCommunicator instance,
                    java.lang.Object argument) throws Throwable {
                  instance.fireAndForget((reactor.core.publisher.Mono)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Mono<byte[]>> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaRequestResponseMethod extends InstanceMetaMethod<io.art.rsocket.portal.RsocketDefaultPortal.RsocketBuiltinCommunicator, reactor.core.publisher.Mono<byte[]>> {
                private final MetaParameter<reactor.core.publisher.Mono<byte[]>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaArray(byte[].class, byte[]::new, metaType(byte.class)))));

                private MetaRequestResponseMethod() {
                  super("requestResponse",metaType(reactor.core.publisher.Mono.class,metaArray(byte[].class, byte[]::new, metaType(byte.class))));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.portal.RsocketDefaultPortal.RsocketBuiltinCommunicator instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.requestResponse((reactor.core.publisher.Mono<byte[]>)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.portal.RsocketDefaultPortal.RsocketBuiltinCommunicator instance,
                    java.lang.Object argument) throws Throwable {
                  return instance.requestResponse((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<byte[]>> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaRequestStreamMethod extends InstanceMetaMethod<io.art.rsocket.portal.RsocketDefaultPortal.RsocketBuiltinCommunicator, reactor.core.publisher.Flux<byte[]>> {
                private final MetaParameter<reactor.core.publisher.Mono<byte[]>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaArray(byte[].class, byte[]::new, metaType(byte.class)))));

                private MetaRequestStreamMethod() {
                  super("requestStream",metaType(reactor.core.publisher.Flux.class,metaArray(byte[].class, byte[]::new, metaType(byte.class))));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.portal.RsocketDefaultPortal.RsocketBuiltinCommunicator instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.requestStream((reactor.core.publisher.Mono<byte[]>)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.portal.RsocketDefaultPortal.RsocketBuiltinCommunicator instance,
                    java.lang.Object argument) throws Throwable {
                  return instance.requestStream((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<byte[]>> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaRequestChannelMethod extends InstanceMetaMethod<io.art.rsocket.portal.RsocketDefaultPortal.RsocketBuiltinCommunicator, reactor.core.publisher.Flux<byte[]>> {
                private final MetaParameter<reactor.core.publisher.Flux<byte[]>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaArray(byte[].class, byte[]::new, metaType(byte.class)))));

                private MetaRequestChannelMethod() {
                  super("requestChannel",metaType(reactor.core.publisher.Flux.class,metaArray(byte[].class, byte[]::new, metaType(byte.class))));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.portal.RsocketDefaultPortal.RsocketBuiltinCommunicator instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.requestChannel((reactor.core.publisher.Flux<byte[]>)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.portal.RsocketDefaultPortal.RsocketBuiltinCommunicator instance,
                    java.lang.Object argument) throws Throwable {
                  return instance.requestChannel((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<byte[]>> inputParameter() {
                  return inputParameter;
                }
              }

              public class MetaRsocketBuiltinCommunicatorProxy extends MetaProxy implements io.art.rsocket.portal.RsocketDefaultPortal.RsocketBuiltinCommunicator {
                private final Function<java.lang.Object, java.lang.Object> fireAndForgetInvocation;

                private final Function<java.lang.Object, java.lang.Object> requestResponseInvocation;

                private final Function<java.lang.Object, java.lang.Object> requestStreamInvocation;

                private final Function<java.lang.Object, java.lang.Object> requestChannelInvocation;

                public MetaRsocketBuiltinCommunicatorProxy(
                    Map<MetaMethod<?>, Function<java.lang.Object, java.lang.Object>> invocations) {
                  super(invocations);
                  fireAndForgetInvocation = invocations.get(fireAndForgetMethod);
                  requestResponseInvocation = invocations.get(requestResponseMethod);
                  requestStreamInvocation = invocations.get(requestStreamMethod);
                  requestChannelInvocation = invocations.get(requestChannelMethod);
                }

                @Override
                public void fireAndForget(reactor.core.publisher.Mono<byte[]> input) {
                  fireAndForgetInvocation.apply(input);
                }

                @Override
                public reactor.core.publisher.Mono<byte[]> requestResponse(
                    reactor.core.publisher.Mono<byte[]> input) {
                  return (reactor.core.publisher.Mono<byte[]>)(requestResponseInvocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Flux<byte[]> requestStream(
                    reactor.core.publisher.Mono<byte[]> input) {
                  return (reactor.core.publisher.Flux<byte[]>)(requestStreamInvocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Flux<byte[]> requestChannel(
                    reactor.core.publisher.Flux<byte[]> input) {
                  return (reactor.core.publisher.Flux<byte[]>)(requestChannelInvocation.apply(input));
                }
              }
            }
          }
        }
      }
    }
  }
}
