package io.art.rsocket.meta;

import static io.art.meta.model.MetaType.metaArray;
import static io.art.meta.model.MetaType.metaEnum;
import static io.art.meta.model.MetaType.metaType;

import io.art.meta.model.InstanceMetaMethod;
import io.art.meta.model.MetaClass;
import io.art.meta.model.MetaField;
import io.art.meta.model.MetaLibrary;
import io.art.meta.model.MetaPackage;
import io.art.meta.model.MetaParameter;

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

        private MetaRsocketPackage() {
          super("rsocket");
        }

        public MetaModelPackage modelPackage() {
          return modelPackage;
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
            private final MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> dataFormatField = register(new MetaField<>("dataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf),false));

            private final MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> metadataFormatField = register(new MetaField<>("metadataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf),false));

            private final MetaField<io.art.core.model.ServiceMethodIdentifier> idField = register(new MetaField<>("id",metaType(io.art.core.model.ServiceMethodIdentifier.class),false));

            private final MetaToBuilderMethod toBuilderMethod = register(new MetaToBuilderMethod());

            private final MetaRsocketSetupPayloadBuilderClass rsocketSetupPayloadBuilderClass = register(new MetaRsocketSetupPayloadBuilderClass());

            private MetaRsocketSetupPayloadClass() {
              super(metaType(io.art.rsocket.model.RsocketSetupPayload.class));
            }

            public MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> dataFormatField(
                ) {
              return dataFormatField;
            }

            public MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> metadataFormatField(
                ) {
              return metadataFormatField;
            }

            public MetaField<io.art.core.model.ServiceMethodIdentifier> idField() {
              return idField;
            }

            public MetaToBuilderMethod toBuilderMethod() {
              return toBuilderMethod;
            }

            public MetaRsocketSetupPayloadBuilderClass rsocketSetupPayloadBuilderClass() {
              return rsocketSetupPayloadBuilderClass;
            }

            public static final class MetaToBuilderMethod extends InstanceMetaMethod<io.art.rsocket.model.RsocketSetupPayload, io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder> {
              private MetaToBuilderMethod() {
                super("toBuilder",metaType(io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder.class));
              }

              @Override
              public Object invoke(io.art.rsocket.model.RsocketSetupPayload instance,
                  Object[] arguments) throws Throwable {
                return instance.toBuilder();
              }

              @Override
              public Object invoke(io.art.rsocket.model.RsocketSetupPayload instance) throws
                  Throwable {
                return instance.toBuilder();
              }
            }

            public static final class MetaRsocketSetupPayloadBuilderClass extends MetaClass<io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder> {
              private final MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> dataFormatField = register(new MetaField<>("dataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf),false));

              private final MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> metadataFormatField = register(new MetaField<>("metadataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf),false));

              private final MetaField<io.art.core.model.ServiceMethodIdentifier> idField = register(new MetaField<>("id",metaType(io.art.core.model.ServiceMethodIdentifier.class),false));

              private final MetaDataFormatMethod dataFormatMethod = register(new MetaDataFormatMethod());

              private final MetaMetadataFormatMethod metadataFormatMethod = register(new MetaMetadataFormatMethod());

              private final MetaIdMethod idMethod = register(new MetaIdMethod());

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

              public MetaField<io.art.core.model.ServiceMethodIdentifier> idField() {
                return idField;
              }

              public MetaDataFormatMethod dataFormatMethod() {
                return dataFormatMethod;
              }

              public MetaMetadataFormatMethod metadataFormatMethod() {
                return metadataFormatMethod;
              }

              public MetaIdMethod idMethod() {
                return idMethod;
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
                public Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.dataFormat((io.art.transport.constants.TransportModuleConstants.DataFormat)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    Object argument) throws Throwable {
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
                public Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.metadataFormat((io.art.transport.constants.TransportModuleConstants.DataFormat)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    Object argument) throws Throwable {
                  return instance.metadataFormat((io.art.transport.constants.TransportModuleConstants.DataFormat)(argument));
                }

                public MetaParameter<io.art.transport.constants.TransportModuleConstants.DataFormat> metadataFormatParameter(
                    ) {
                  return metadataFormatParameter;
                }
              }

              public static final class MetaIdMethod extends InstanceMetaMethod<io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder, io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder> {
                private final MetaParameter<io.art.core.model.ServiceMethodIdentifier> idParameter = register(new MetaParameter<>(0, "id",metaType(io.art.core.model.ServiceMethodIdentifier.class)));

                private MetaIdMethod() {
                  super("id",metaType(io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder.class));
                }

                @Override
                public Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.id((io.art.core.model.ServiceMethodIdentifier)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    Object argument) throws Throwable {
                  return instance.id((io.art.core.model.ServiceMethodIdentifier)(argument));
                }

                public MetaParameter<io.art.core.model.ServiceMethodIdentifier> idParameter() {
                  return idParameter;
                }
              }

              public static final class MetaBuildMethod extends InstanceMetaMethod<io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder, io.art.rsocket.model.RsocketSetupPayload> {
                private MetaBuildMethod() {
                  super("build",metaType(io.art.rsocket.model.RsocketSetupPayload.class));
                }

                @Override
                public Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.build();
                }

                @Override
                public Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance)
                    throws Throwable {
                  return instance.build();
                }
              }
            }
          }
        }
      }
    }
  }
}
