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
import io.art.meta.model.StaticMetaMethod;
import java.util.Map;

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
        private final MetaConstantsPackage constantsPackage = register(new MetaConstantsPackage());

        private final MetaManagerPackage managerPackage = register(new MetaManagerPackage());

        private final MetaConfigurationPackage configurationPackage = register(new MetaConfigurationPackage());

        private final MetaModulePackage modulePackage = register(new MetaModulePackage());

        private final MetaReaderPackage readerPackage = register(new MetaReaderPackage());

        private final MetaCommunicatorPackage communicatorPackage = register(new MetaCommunicatorPackage());

        private final MetaStatePackage statePackage = register(new MetaStatePackage());

        private final MetaRefresherPackage refresherPackage = register(new MetaRefresherPackage());

        private final MetaModelPackage modelPackage = register(new MetaModelPackage());

        private final MetaServerPackage serverPackage = register(new MetaServerPackage());

        private final MetaExceptionPackage exceptionPackage = register(new MetaExceptionPackage());

        private final MetaInterceptorPackage interceptorPackage = register(new MetaInterceptorPackage());

        private final MetaSocketPackage socketPackage = register(new MetaSocketPackage());

        private MetaRsocketPackage() {
          super("rsocket");
        }

        public MetaConstantsPackage constantsPackage() {
          return constantsPackage;
        }

        public MetaManagerPackage managerPackage() {
          return managerPackage;
        }

        public MetaConfigurationPackage configurationPackage() {
          return configurationPackage;
        }

        public MetaModulePackage modulePackage() {
          return modulePackage;
        }

        public MetaReaderPackage readerPackage() {
          return readerPackage;
        }

        public MetaCommunicatorPackage communicatorPackage() {
          return communicatorPackage;
        }

        public MetaStatePackage statePackage() {
          return statePackage;
        }

        public MetaRefresherPackage refresherPackage() {
          return refresherPackage;
        }

        public MetaModelPackage modelPackage() {
          return modelPackage;
        }

        public MetaServerPackage serverPackage() {
          return serverPackage;
        }

        public MetaExceptionPackage exceptionPackage() {
          return exceptionPackage;
        }

        public MetaInterceptorPackage interceptorPackage() {
          return interceptorPackage;
        }

        public MetaSocketPackage socketPackage() {
          return socketPackage;
        }

        public static final class MetaConstantsPackage extends MetaPackage {
          private final MetaRsocketModuleConstantsClass rsocketModuleConstantsClass = register(new MetaRsocketModuleConstantsClass());

          private MetaConstantsPackage() {
            super("constants");
          }

          public MetaRsocketModuleConstantsClass rsocketModuleConstantsClass() {
            return rsocketModuleConstantsClass;
          }

          public static final class MetaRsocketModuleConstantsClass extends MetaClass<io.art.rsocket.constants.RsocketModuleConstants> {
            private final MetaField<io.rsocket.util.EmptyPayload> EMPTY_PAYLOADField = register(new MetaField<>("EMPTY_PAYLOAD",metaType(io.rsocket.util.EmptyPayload.class),false));

            private final MetaField<io.art.core.property.LazyProperty<reactor.core.publisher.Mono<io.rsocket.util.EmptyPayload>>> EMPTY_PAYLOAD_MONOField = register(new MetaField<>("EMPTY_PAYLOAD_MONO",metaType(io.art.core.property.LazyProperty.class,metaType(reactor.core.publisher.Mono.class,metaType(io.rsocket.util.EmptyPayload.class))),false));

            private final MetaFieldsClass fieldsClass = register(new MetaFieldsClass());

            private final MetaDefaultsClass defaultsClass = register(new MetaDefaultsClass());

            private final MetaContextKeysClass contextKeysClass = register(new MetaContextKeysClass());

            private final MetaConfigurationKeysClass configurationKeysClass = register(new MetaConfigurationKeysClass());

            private final MetaLoggingMessagesClass loggingMessagesClass = register(new MetaLoggingMessagesClass());

            private final MetaExceptionMessagesClass exceptionMessagesClass = register(new MetaExceptionMessagesClass());

            private MetaRsocketModuleConstantsClass() {
              super(metaType(io.art.rsocket.constants.RsocketModuleConstants.class));
            }

            public MetaField<io.rsocket.util.EmptyPayload> EMPTY_PAYLOADField() {
              return EMPTY_PAYLOADField;
            }

            public MetaField<io.art.core.property.LazyProperty<reactor.core.publisher.Mono<io.rsocket.util.EmptyPayload>>> EMPTY_PAYLOAD_MONOField(
                ) {
              return EMPTY_PAYLOAD_MONOField;
            }

            @Override
            public MetaProxy proxy(
                Map<MetaMethod<?>, java.util.function.Function<java.lang.Object, java.lang.Object>> invocations) {
              return new MetaRsocketModuleConstantsProxy(invocations);
            }

            public MetaFieldsClass fieldsClass() {
              return fieldsClass;
            }

            public MetaDefaultsClass defaultsClass() {
              return defaultsClass;
            }

            public MetaContextKeysClass contextKeysClass() {
              return contextKeysClass;
            }

            public MetaConfigurationKeysClass configurationKeysClass() {
              return configurationKeysClass;
            }

            public MetaLoggingMessagesClass loggingMessagesClass() {
              return loggingMessagesClass;
            }

            public MetaExceptionMessagesClass exceptionMessagesClass() {
              return exceptionMessagesClass;
            }

            public class MetaRsocketModuleConstantsProxy extends MetaProxy implements io.art.rsocket.constants.RsocketModuleConstants {
              public MetaRsocketModuleConstantsProxy(
                  Map<MetaMethod<?>, java.util.function.Function<java.lang.Object, java.lang.Object>> invocations) {
                super(invocations);
              }
            }

            public static final class MetaFieldsClass extends MetaClass<io.art.rsocket.constants.RsocketModuleConstants.Fields> {
              private final MetaField<java.lang.String> SETUP_PAYLOAD_DATA_FORMAT_FIELDField = register(new MetaField<>("SETUP_PAYLOAD_DATA_FORMAT_FIELD",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> SETUP_PAYLOAD_META_DATA_FORMAT_FIELDField = register(new MetaField<>("SETUP_PAYLOAD_META_DATA_FORMAT_FIELD",metaType(java.lang.String.class),false));

              private MetaFieldsClass() {
                super(metaType(io.art.rsocket.constants.RsocketModuleConstants.Fields.class));
              }

              public MetaField<java.lang.String> SETUP_PAYLOAD_DATA_FORMAT_FIELDField() {
                return SETUP_PAYLOAD_DATA_FORMAT_FIELDField;
              }

              public MetaField<java.lang.String> SETUP_PAYLOAD_META_DATA_FORMAT_FIELDField() {
                return SETUP_PAYLOAD_META_DATA_FORMAT_FIELDField;
              }

              @Override
              public MetaProxy proxy(
                  Map<MetaMethod<?>, java.util.function.Function<java.lang.Object, java.lang.Object>> invocations) {
                return new MetaFieldsProxy(invocations);
              }

              public class MetaFieldsProxy extends MetaProxy implements io.art.rsocket.constants.RsocketModuleConstants.Fields {
                public MetaFieldsProxy(
                    Map<MetaMethod<?>, java.util.function.Function<java.lang.Object, java.lang.Object>> invocations) {
                  super(invocations);
                }
              }
            }

            public static final class MetaDefaultsClass extends MetaClass<io.art.rsocket.constants.RsocketModuleConstants.Defaults> {
              private final MetaField<Long> DEFAULT_RETRY_MAX_ATTEMPTSField = register(new MetaField<>("DEFAULT_RETRY_MAX_ATTEMPTS",metaType(long.class),false));

              private final MetaField<java.time.Duration> DEFAULT_RETRY_MIN_BACKOFFField = register(new MetaField<>("DEFAULT_RETRY_MIN_BACKOFF",metaType(java.time.Duration.class),false));

              private final MetaField<java.time.Duration> DEFAULT_RETRY_FIXED_DELAYField = register(new MetaField<>("DEFAULT_RETRY_FIXED_DELAY",metaType(java.time.Duration.class),false));

              private final MetaField<Integer> DEFAULT_RETRY_MAXField = register(new MetaField<>("DEFAULT_RETRY_MAX",metaType(int.class),false));

              private final MetaField<Integer> DEFAULT_RETRY_MAX_IN_ROWField = register(new MetaField<>("DEFAULT_RETRY_MAX_IN_ROW",metaType(int.class),false));

              private final MetaField<Integer> DEFAULT_PORTField = register(new MetaField<>("DEFAULT_PORT",metaType(int.class),false));

              private final MetaField<java.time.Duration> DEFAULT_RESUME_SESSION_DURATIONField = register(new MetaField<>("DEFAULT_RESUME_SESSION_DURATION",metaType(java.time.Duration.class),false));

              private final MetaField<java.time.Duration> DEFAULT_RESUME_STREAM_TIMEOUTField = register(new MetaField<>("DEFAULT_RESUME_STREAM_TIMEOUT",metaType(java.time.Duration.class),false));

              private final MetaField<java.time.Duration> DEFAULT_KEEP_ALIVE_INTERVALField = register(new MetaField<>("DEFAULT_KEEP_ALIVE_INTERVAL",metaType(java.time.Duration.class),false));

              private final MetaField<java.time.Duration> DEFAULT_KEEP_ALIVE_MAX_LIFE_TIMEField = register(new MetaField<>("DEFAULT_KEEP_ALIVE_MAX_LIFE_TIME",metaType(java.time.Duration.class),false));

              private MetaDefaultsClass() {
                super(metaType(io.art.rsocket.constants.RsocketModuleConstants.Defaults.class));
              }

              public MetaField<Long> DEFAULT_RETRY_MAX_ATTEMPTSField() {
                return DEFAULT_RETRY_MAX_ATTEMPTSField;
              }

              public MetaField<java.time.Duration> DEFAULT_RETRY_MIN_BACKOFFField() {
                return DEFAULT_RETRY_MIN_BACKOFFField;
              }

              public MetaField<java.time.Duration> DEFAULT_RETRY_FIXED_DELAYField() {
                return DEFAULT_RETRY_FIXED_DELAYField;
              }

              public MetaField<Integer> DEFAULT_RETRY_MAXField() {
                return DEFAULT_RETRY_MAXField;
              }

              public MetaField<Integer> DEFAULT_RETRY_MAX_IN_ROWField() {
                return DEFAULT_RETRY_MAX_IN_ROWField;
              }

              public MetaField<Integer> DEFAULT_PORTField() {
                return DEFAULT_PORTField;
              }

              public MetaField<java.time.Duration> DEFAULT_RESUME_SESSION_DURATIONField() {
                return DEFAULT_RESUME_SESSION_DURATIONField;
              }

              public MetaField<java.time.Duration> DEFAULT_RESUME_STREAM_TIMEOUTField() {
                return DEFAULT_RESUME_STREAM_TIMEOUTField;
              }

              public MetaField<java.time.Duration> DEFAULT_KEEP_ALIVE_INTERVALField() {
                return DEFAULT_KEEP_ALIVE_INTERVALField;
              }

              public MetaField<java.time.Duration> DEFAULT_KEEP_ALIVE_MAX_LIFE_TIMEField() {
                return DEFAULT_KEEP_ALIVE_MAX_LIFE_TIMEField;
              }

              @Override
              public MetaProxy proxy(
                  Map<MetaMethod<?>, java.util.function.Function<java.lang.Object, java.lang.Object>> invocations) {
                return new MetaDefaultsProxy(invocations);
              }

              public class MetaDefaultsProxy extends MetaProxy implements io.art.rsocket.constants.RsocketModuleConstants.Defaults {
                public MetaDefaultsProxy(
                    Map<MetaMethod<?>, java.util.function.Function<java.lang.Object, java.lang.Object>> invocations) {
                  super(invocations);
                }
              }
            }

            public static final class MetaContextKeysClass extends MetaClass<io.art.rsocket.constants.RsocketModuleConstants.ContextKeys> {
              private final MetaField<java.lang.String> REQUESTER_RSOCKET_KEYField = register(new MetaField<>("REQUESTER_RSOCKET_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> SETUP_PAYLOAD_KEYField = register(new MetaField<>("SETUP_PAYLOAD_KEY",metaType(java.lang.String.class),false));

              private MetaContextKeysClass() {
                super(metaType(io.art.rsocket.constants.RsocketModuleConstants.ContextKeys.class));
              }

              public MetaField<java.lang.String> REQUESTER_RSOCKET_KEYField() {
                return REQUESTER_RSOCKET_KEYField;
              }

              public MetaField<java.lang.String> SETUP_PAYLOAD_KEYField() {
                return SETUP_PAYLOAD_KEYField;
              }

              @Override
              public MetaProxy proxy(
                  Map<MetaMethod<?>, java.util.function.Function<java.lang.Object, java.lang.Object>> invocations) {
                return new MetaContextKeysProxy(invocations);
              }

              public class MetaContextKeysProxy extends MetaProxy implements io.art.rsocket.constants.RsocketModuleConstants.ContextKeys {
                public MetaContextKeysProxy(
                    Map<MetaMethod<?>, java.util.function.Function<java.lang.Object, java.lang.Object>> invocations) {
                  super(invocations);
                }
              }
            }

            public static final class MetaConfigurationKeysClass extends MetaClass<io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys> {
              private final MetaField<java.lang.String> RSOCKET_SECTIONField = register(new MetaField<>("RSOCKET_SECTION",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> SERVER_SECTIONField = register(new MetaField<>("SERVER_SECTION",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> COMMUNICATOR_SECTIONField = register(new MetaField<>("COMMUNICATOR_SECTION",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> RESUME_SECTIONField = register(new MetaField<>("RESUME_SECTION",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> RETRY_SECTIONField = register(new MetaField<>("RETRY_SECTION",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> RECONNECT_SECTIONField = register(new MetaField<>("RECONNECT_SECTION",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> KEEP_ALIVE_SECTIONField = register(new MetaField<>("KEEP_ALIVE_SECTION",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> DEFAULT_SECTIONField = register(new MetaField<>("DEFAULT_SECTION",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> DATA_FORMAT_KEYField = register(new MetaField<>("DATA_FORMAT_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> META_DATA_FORMAT_KEYField = register(new MetaField<>("META_DATA_FORMAT_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> SERVICE_ID_KEYField = register(new MetaField<>("SERVICE_ID_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> METHOD_ID_KEYField = register(new MetaField<>("METHOD_ID_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> TRANSPORT_SECTIONField = register(new MetaField<>("TRANSPORT_SECTION",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> TRANSPORT_MODE_KEYField = register(new MetaField<>("TRANSPORT_MODE_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> TRANSPORT_PORT_KEYField = register(new MetaField<>("TRANSPORT_PORT_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> TRANSPORT_HOST_KEYField = register(new MetaField<>("TRANSPORT_HOST_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> TRANSPORT_TCP_HOST_KEYField = register(new MetaField<>("TRANSPORT_TCP_HOST_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> TRANSPORT_TCP_PORT_KEYField = register(new MetaField<>("TRANSPORT_TCP_PORT_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> TRANSPORT_WS_BASE_URL_KEYField = register(new MetaField<>("TRANSPORT_WS_BASE_URL_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> TRANSPORT_WS_PATH_KEYField = register(new MetaField<>("TRANSPORT_WS_PATH_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> TRANSPORT_TCP_MAX_FRAME_LENGTHField = register(new MetaField<>("TRANSPORT_TCP_MAX_FRAME_LENGTH",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> PAYLOAD_DECODER_KEYField = register(new MetaField<>("PAYLOAD_DECODER_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> LOGGING_KEYField = register(new MetaField<>("LOGGING_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> FRAGMENTATION_MTU_KEYField = register(new MetaField<>("FRAGMENTATION_MTU_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> CLEANUP_STORE_ON_KEEP_ALIVE_KEYField = register(new MetaField<>("CLEANUP_STORE_ON_KEEP_ALIVE_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> SESSION_DURATION_KEYField = register(new MetaField<>("SESSION_DURATION_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> STREAM_TIMEOUT_KEYField = register(new MetaField<>("STREAM_TIMEOUT_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> POLICY_KEYField = register(new MetaField<>("POLICY_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> BACKOFF_MAX_ATTEMPTS_KEYField = register(new MetaField<>("BACKOFF_MAX_ATTEMPTS_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> BACKOFF_MIN_BACKOFF_KEYField = register(new MetaField<>("BACKOFF_MIN_BACKOFF_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> FIXED_DELAY_MAX_ATTEMPTS_KEYField = register(new MetaField<>("FIXED_DELAY_MAX_ATTEMPTS_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> FIXED_DELAY_KEYField = register(new MetaField<>("FIXED_DELAY_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> MAX_KEYField = register(new MetaField<>("MAX_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> MAX_IN_ROW_KEYField = register(new MetaField<>("MAX_IN_ROW_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> MAX_INBOUND_PAYLOAD_SIZE_KEYField = register(new MetaField<>("MAX_INBOUND_PAYLOAD_SIZE_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> SERVICES_KEYField = register(new MetaField<>("SERVICES_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> INTERVAL_KEYField = register(new MetaField<>("INTERVAL_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> MAX_LIFE_TIME_KEYField = register(new MetaField<>("MAX_LIFE_TIME_KEY",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> CONNECTORS_KEYField = register(new MetaField<>("CONNECTORS_KEY",metaType(java.lang.String.class),false));

              private MetaConfigurationKeysClass() {
                super(metaType(io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.class));
              }

              public MetaField<java.lang.String> RSOCKET_SECTIONField() {
                return RSOCKET_SECTIONField;
              }

              public MetaField<java.lang.String> SERVER_SECTIONField() {
                return SERVER_SECTIONField;
              }

              public MetaField<java.lang.String> COMMUNICATOR_SECTIONField() {
                return COMMUNICATOR_SECTIONField;
              }

              public MetaField<java.lang.String> RESUME_SECTIONField() {
                return RESUME_SECTIONField;
              }

              public MetaField<java.lang.String> RETRY_SECTIONField() {
                return RETRY_SECTIONField;
              }

              public MetaField<java.lang.String> RECONNECT_SECTIONField() {
                return RECONNECT_SECTIONField;
              }

              public MetaField<java.lang.String> KEEP_ALIVE_SECTIONField() {
                return KEEP_ALIVE_SECTIONField;
              }

              public MetaField<java.lang.String> DEFAULT_SECTIONField() {
                return DEFAULT_SECTIONField;
              }

              public MetaField<java.lang.String> DATA_FORMAT_KEYField() {
                return DATA_FORMAT_KEYField;
              }

              public MetaField<java.lang.String> META_DATA_FORMAT_KEYField() {
                return META_DATA_FORMAT_KEYField;
              }

              public MetaField<java.lang.String> SERVICE_ID_KEYField() {
                return SERVICE_ID_KEYField;
              }

              public MetaField<java.lang.String> METHOD_ID_KEYField() {
                return METHOD_ID_KEYField;
              }

              public MetaField<java.lang.String> TRANSPORT_SECTIONField() {
                return TRANSPORT_SECTIONField;
              }

              public MetaField<java.lang.String> TRANSPORT_MODE_KEYField() {
                return TRANSPORT_MODE_KEYField;
              }

              public MetaField<java.lang.String> TRANSPORT_PORT_KEYField() {
                return TRANSPORT_PORT_KEYField;
              }

              public MetaField<java.lang.String> TRANSPORT_HOST_KEYField() {
                return TRANSPORT_HOST_KEYField;
              }

              public MetaField<java.lang.String> TRANSPORT_TCP_HOST_KEYField() {
                return TRANSPORT_TCP_HOST_KEYField;
              }

              public MetaField<java.lang.String> TRANSPORT_TCP_PORT_KEYField() {
                return TRANSPORT_TCP_PORT_KEYField;
              }

              public MetaField<java.lang.String> TRANSPORT_WS_BASE_URL_KEYField() {
                return TRANSPORT_WS_BASE_URL_KEYField;
              }

              public MetaField<java.lang.String> TRANSPORT_WS_PATH_KEYField() {
                return TRANSPORT_WS_PATH_KEYField;
              }

              public MetaField<java.lang.String> TRANSPORT_TCP_MAX_FRAME_LENGTHField() {
                return TRANSPORT_TCP_MAX_FRAME_LENGTHField;
              }

              public MetaField<java.lang.String> PAYLOAD_DECODER_KEYField() {
                return PAYLOAD_DECODER_KEYField;
              }

              public MetaField<java.lang.String> LOGGING_KEYField() {
                return LOGGING_KEYField;
              }

              public MetaField<java.lang.String> FRAGMENTATION_MTU_KEYField() {
                return FRAGMENTATION_MTU_KEYField;
              }

              public MetaField<java.lang.String> CLEANUP_STORE_ON_KEEP_ALIVE_KEYField() {
                return CLEANUP_STORE_ON_KEEP_ALIVE_KEYField;
              }

              public MetaField<java.lang.String> SESSION_DURATION_KEYField() {
                return SESSION_DURATION_KEYField;
              }

              public MetaField<java.lang.String> STREAM_TIMEOUT_KEYField() {
                return STREAM_TIMEOUT_KEYField;
              }

              public MetaField<java.lang.String> POLICY_KEYField() {
                return POLICY_KEYField;
              }

              public MetaField<java.lang.String> BACKOFF_MAX_ATTEMPTS_KEYField() {
                return BACKOFF_MAX_ATTEMPTS_KEYField;
              }

              public MetaField<java.lang.String> BACKOFF_MIN_BACKOFF_KEYField() {
                return BACKOFF_MIN_BACKOFF_KEYField;
              }

              public MetaField<java.lang.String> FIXED_DELAY_MAX_ATTEMPTS_KEYField() {
                return FIXED_DELAY_MAX_ATTEMPTS_KEYField;
              }

              public MetaField<java.lang.String> FIXED_DELAY_KEYField() {
                return FIXED_DELAY_KEYField;
              }

              public MetaField<java.lang.String> MAX_KEYField() {
                return MAX_KEYField;
              }

              public MetaField<java.lang.String> MAX_IN_ROW_KEYField() {
                return MAX_IN_ROW_KEYField;
              }

              public MetaField<java.lang.String> MAX_INBOUND_PAYLOAD_SIZE_KEYField() {
                return MAX_INBOUND_PAYLOAD_SIZE_KEYField;
              }

              public MetaField<java.lang.String> SERVICES_KEYField() {
                return SERVICES_KEYField;
              }

              public MetaField<java.lang.String> INTERVAL_KEYField() {
                return INTERVAL_KEYField;
              }

              public MetaField<java.lang.String> MAX_LIFE_TIME_KEYField() {
                return MAX_LIFE_TIME_KEYField;
              }

              public MetaField<java.lang.String> CONNECTORS_KEYField() {
                return CONNECTORS_KEYField;
              }

              @Override
              public MetaProxy proxy(
                  Map<MetaMethod<?>, java.util.function.Function<java.lang.Object, java.lang.Object>> invocations) {
                return new MetaConfigurationKeysProxy(invocations);
              }

              public class MetaConfigurationKeysProxy extends MetaProxy implements io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys {
                public MetaConfigurationKeysProxy(
                    Map<MetaMethod<?>, java.util.function.Function<java.lang.Object, java.lang.Object>> invocations) {
                  super(invocations);
                }
              }
            }

            public static final class MetaLoggingMessagesClass extends MetaClass<io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages> {
              private final MetaField<java.lang.String> RSOCKET_DISPOSINGField = register(new MetaField<>("RSOCKET_DISPOSING",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> SERVER_STARTEDField = register(new MetaField<>("SERVER_STARTED",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> SERVER_STOPPEDField = register(new MetaField<>("SERVER_STOPPED",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> COMMUNICATOR_STARTEDField = register(new MetaField<>("COMMUNICATOR_STARTED",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> COMMUNICATOR_STOPPEDField = register(new MetaField<>("COMMUNICATOR_STOPPED",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> FIRE_AND_FORGET_REQUEST_LOGField = register(new MetaField<>("FIRE_AND_FORGET_REQUEST_LOG",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> FIRE_AND_FORGET_RESPONSE_LOGField = register(new MetaField<>("FIRE_AND_FORGET_RESPONSE_LOG",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> FIRE_AND_FORGET_EXCEPTION_LOGField = register(new MetaField<>("FIRE_AND_FORGET_EXCEPTION_LOG",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> METADATA_PUSH_REQUEST_LOGField = register(new MetaField<>("METADATA_PUSH_REQUEST_LOG",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> METADATA_PUSH_RESPONSE_LOGField = register(new MetaField<>("METADATA_PUSH_RESPONSE_LOG",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> METADATA_PUSH_EXCEPTION_LOGField = register(new MetaField<>("METADATA_PUSH_EXCEPTION_LOG",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> REQUEST_RESPONSE_REQUEST_LOGField = register(new MetaField<>("REQUEST_RESPONSE_REQUEST_LOG",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> RESPONSE_RESPONSE_LOGField = register(new MetaField<>("RESPONSE_RESPONSE_LOG",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> REQUEST_RESPONSE_EXCEPTION_LOGField = register(new MetaField<>("REQUEST_RESPONSE_EXCEPTION_LOG",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> REQUEST_STREAM_REQUEST_LOGField = register(new MetaField<>("REQUEST_STREAM_REQUEST_LOG",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> REQUEST_STREAM_RESPONSE_LOGField = register(new MetaField<>("REQUEST_STREAM_RESPONSE_LOG",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> REQUEST_STREAM_EXCEPTION_LOGField = register(new MetaField<>("REQUEST_STREAM_EXCEPTION_LOG",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> REQUEST_CHANNEL_REQUEST_LOGField = register(new MetaField<>("REQUEST_CHANNEL_REQUEST_LOG",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> REQUEST_CHANNEL_RESPONSE_LOGField = register(new MetaField<>("REQUEST_CHANNEL_RESPONSE_LOG",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> REQUEST_CHANNEL_EXCEPTION_LOGField = register(new MetaField<>("REQUEST_CHANNEL_EXCEPTION_LOG",metaType(java.lang.String.class),false));

              private MetaLoggingMessagesClass() {
                super(metaType(io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.class));
              }

              public MetaField<java.lang.String> RSOCKET_DISPOSINGField() {
                return RSOCKET_DISPOSINGField;
              }

              public MetaField<java.lang.String> SERVER_STARTEDField() {
                return SERVER_STARTEDField;
              }

              public MetaField<java.lang.String> SERVER_STOPPEDField() {
                return SERVER_STOPPEDField;
              }

              public MetaField<java.lang.String> COMMUNICATOR_STARTEDField() {
                return COMMUNICATOR_STARTEDField;
              }

              public MetaField<java.lang.String> COMMUNICATOR_STOPPEDField() {
                return COMMUNICATOR_STOPPEDField;
              }

              public MetaField<java.lang.String> FIRE_AND_FORGET_REQUEST_LOGField() {
                return FIRE_AND_FORGET_REQUEST_LOGField;
              }

              public MetaField<java.lang.String> FIRE_AND_FORGET_RESPONSE_LOGField() {
                return FIRE_AND_FORGET_RESPONSE_LOGField;
              }

              public MetaField<java.lang.String> FIRE_AND_FORGET_EXCEPTION_LOGField() {
                return FIRE_AND_FORGET_EXCEPTION_LOGField;
              }

              public MetaField<java.lang.String> METADATA_PUSH_REQUEST_LOGField() {
                return METADATA_PUSH_REQUEST_LOGField;
              }

              public MetaField<java.lang.String> METADATA_PUSH_RESPONSE_LOGField() {
                return METADATA_PUSH_RESPONSE_LOGField;
              }

              public MetaField<java.lang.String> METADATA_PUSH_EXCEPTION_LOGField() {
                return METADATA_PUSH_EXCEPTION_LOGField;
              }

              public MetaField<java.lang.String> REQUEST_RESPONSE_REQUEST_LOGField() {
                return REQUEST_RESPONSE_REQUEST_LOGField;
              }

              public MetaField<java.lang.String> RESPONSE_RESPONSE_LOGField() {
                return RESPONSE_RESPONSE_LOGField;
              }

              public MetaField<java.lang.String> REQUEST_RESPONSE_EXCEPTION_LOGField() {
                return REQUEST_RESPONSE_EXCEPTION_LOGField;
              }

              public MetaField<java.lang.String> REQUEST_STREAM_REQUEST_LOGField() {
                return REQUEST_STREAM_REQUEST_LOGField;
              }

              public MetaField<java.lang.String> REQUEST_STREAM_RESPONSE_LOGField() {
                return REQUEST_STREAM_RESPONSE_LOGField;
              }

              public MetaField<java.lang.String> REQUEST_STREAM_EXCEPTION_LOGField() {
                return REQUEST_STREAM_EXCEPTION_LOGField;
              }

              public MetaField<java.lang.String> REQUEST_CHANNEL_REQUEST_LOGField() {
                return REQUEST_CHANNEL_REQUEST_LOGField;
              }

              public MetaField<java.lang.String> REQUEST_CHANNEL_RESPONSE_LOGField() {
                return REQUEST_CHANNEL_RESPONSE_LOGField;
              }

              public MetaField<java.lang.String> REQUEST_CHANNEL_EXCEPTION_LOGField() {
                return REQUEST_CHANNEL_EXCEPTION_LOGField;
              }

              @Override
              public MetaProxy proxy(
                  Map<MetaMethod<?>, java.util.function.Function<java.lang.Object, java.lang.Object>> invocations) {
                return new MetaLoggingMessagesProxy(invocations);
              }

              public class MetaLoggingMessagesProxy extends MetaProxy implements io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages {
                public MetaLoggingMessagesProxy(
                    Map<MetaMethod<?>, java.util.function.Function<java.lang.Object, java.lang.Object>> invocations) {
                  super(invocations);
                }
              }
            }

            public static final class MetaExceptionMessagesClass extends MetaClass<io.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages> {
              private final MetaField<java.lang.String> SPECIFICATION_NOT_FOUNDField = register(new MetaField<>("SPECIFICATION_NOT_FOUND",metaType(java.lang.String.class),false));

              private final MetaField<java.lang.String> CONFIGURATION_PARAMETER_NOT_EXISTSField = register(new MetaField<>("CONFIGURATION_PARAMETER_NOT_EXISTS",metaType(java.lang.String.class),false));

              private MetaExceptionMessagesClass() {
                super(metaType(io.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.class));
              }

              public MetaField<java.lang.String> SPECIFICATION_NOT_FOUNDField() {
                return SPECIFICATION_NOT_FOUNDField;
              }

              public MetaField<java.lang.String> CONFIGURATION_PARAMETER_NOT_EXISTSField() {
                return CONFIGURATION_PARAMETER_NOT_EXISTSField;
              }

              @Override
              public MetaProxy proxy(
                  Map<MetaMethod<?>, java.util.function.Function<java.lang.Object, java.lang.Object>> invocations) {
                return new MetaExceptionMessagesProxy(invocations);
              }

              public class MetaExceptionMessagesProxy extends MetaProxy implements io.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages {
                public MetaExceptionMessagesProxy(
                    Map<MetaMethod<?>, java.util.function.Function<java.lang.Object, java.lang.Object>> invocations) {
                  super(invocations);
                }
              }
            }
          }
        }

        public static final class MetaManagerPackage extends MetaPackage {
          private final MetaRsocketManagerClass rsocketManagerClass = register(new MetaRsocketManagerClass());

          private MetaManagerPackage() {
            super("manager");
          }

          public MetaRsocketManagerClass rsocketManagerClass() {
            return rsocketManagerClass;
          }

          public static final class MetaRsocketManagerClass extends MetaClass<io.art.rsocket.manager.RsocketManager> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaField<java.util.concurrent.atomic.AtomicReference<java.lang.Object>> loggerField = register(new MetaField<>("logger",metaType(java.util.concurrent.atomic.AtomicReference.class,metaType(java.lang.Object.class)),false));

            private final MetaField<io.art.rsocket.server.RsocketServer> serverField = register(new MetaField<>("server",metaType(io.art.rsocket.server.RsocketServer.class),false));

            private final MetaInitializeCommunicatorsMethod initializeCommunicatorsMethod = register(new MetaInitializeCommunicatorsMethod());

            private final MetaDisposeCommunicatorsMethod disposeCommunicatorsMethod = register(new MetaDisposeCommunicatorsMethod());

            private final MetaInitializeServerMethod initializeServerMethod = register(new MetaInitializeServerMethod());

            private final MetaDisposeServerMethod disposeServerMethod = register(new MetaDisposeServerMethod());

            private final MetaDisposeRsocketMethod disposeRsocketMethod = register(new MetaDisposeRsocketMethod());

            private MetaRsocketManagerClass() {
              super(metaType(io.art.rsocket.manager.RsocketManager.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<java.util.concurrent.atomic.AtomicReference<java.lang.Object>> loggerField(
                ) {
              return loggerField;
            }

            public MetaField<io.art.rsocket.server.RsocketServer> serverField() {
              return serverField;
            }

            public MetaInitializeCommunicatorsMethod initializeCommunicatorsMethod() {
              return initializeCommunicatorsMethod;
            }

            public MetaDisposeCommunicatorsMethod disposeCommunicatorsMethod() {
              return disposeCommunicatorsMethod;
            }

            public MetaInitializeServerMethod initializeServerMethod() {
              return initializeServerMethod;
            }

            public MetaDisposeServerMethod disposeServerMethod() {
              return disposeServerMethod;
            }

            public MetaDisposeRsocketMethod disposeRsocketMethod() {
              return disposeRsocketMethod;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.manager.RsocketManager> {
              private final MetaParameter<io.art.rsocket.refresher.RsocketModuleRefresher> refresherParameter = register(new MetaParameter<>(0, "refresher",metaType(io.art.rsocket.refresher.RsocketModuleRefresher.class)));

              private final MetaParameter<io.art.rsocket.configuration.RsocketModuleConfiguration> configurationParameter = register(new MetaParameter<>(1, "configuration",metaType(io.art.rsocket.configuration.RsocketModuleConfiguration.class)));

              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.manager.RsocketManager.class));
              }

              @Override
              public io.art.rsocket.manager.RsocketManager invoke(java.lang.Object[] arguments)
                  throws java.lang.Throwable {
                return new io.art.rsocket.manager.RsocketManager((io.art.rsocket.refresher.RsocketModuleRefresher)(arguments[0]),(io.art.rsocket.configuration.RsocketModuleConfiguration)(arguments[1]));
              }

              public MetaParameter<io.art.rsocket.refresher.RsocketModuleRefresher> refresherParameter(
                  ) {
                return refresherParameter;
              }

              public MetaParameter<io.art.rsocket.configuration.RsocketModuleConfiguration> configurationParameter(
                  ) {
                return configurationParameter;
              }
            }

            public static final class MetaInitializeCommunicatorsMethod extends InstanceMetaMethod<io.art.rsocket.manager.RsocketManager, java.lang.Void> {
              private MetaInitializeCommunicatorsMethod() {
                super("initializeCommunicators",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.manager.RsocketManager instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.initializeCommunicators();
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.manager.RsocketManager instance) throws
                  java.lang.Throwable {
                instance.initializeCommunicators();
                return null;
              }
            }

            public static final class MetaDisposeCommunicatorsMethod extends InstanceMetaMethod<io.art.rsocket.manager.RsocketManager, java.lang.Void> {
              private MetaDisposeCommunicatorsMethod() {
                super("disposeCommunicators",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.manager.RsocketManager instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.disposeCommunicators();
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.manager.RsocketManager instance) throws
                  java.lang.Throwable {
                instance.disposeCommunicators();
                return null;
              }
            }

            public static final class MetaInitializeServerMethod extends InstanceMetaMethod<io.art.rsocket.manager.RsocketManager, java.lang.Void> {
              private MetaInitializeServerMethod() {
                super("initializeServer",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.manager.RsocketManager instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.initializeServer();
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.manager.RsocketManager instance) throws
                  java.lang.Throwable {
                instance.initializeServer();
                return null;
              }
            }

            public static final class MetaDisposeServerMethod extends InstanceMetaMethod<io.art.rsocket.manager.RsocketManager, java.lang.Void> {
              private MetaDisposeServerMethod() {
                super("disposeServer",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.manager.RsocketManager instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.disposeServer();
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.manager.RsocketManager instance) throws
                  java.lang.Throwable {
                instance.disposeServer();
                return null;
              }
            }

            public static final class MetaDisposeRsocketMethod extends StaticMetaMethod<java.lang.Void> {
              private final MetaParameter<reactor.core.Disposable> rsocketParameter = register(new MetaParameter<>(0, "rsocket",metaType(reactor.core.Disposable.class)));

              private MetaDisposeRsocketMethod() {
                super("disposeRsocket",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                io.art.rsocket.manager.RsocketManager.disposeRsocket((reactor.core.Disposable)(arguments[0]));
                return null;
              }

              @Override
              public java.lang.Object invoke(java.lang.Object argument) throws java.lang.Throwable {
                io.art.rsocket.manager.RsocketManager.disposeRsocket((reactor.core.Disposable)(argument));
                return null;
              }

              public MetaParameter<reactor.core.Disposable> rsocketParameter() {
                return rsocketParameter;
              }
            }
          }
        }

        public static final class MetaConfigurationPackage extends MetaPackage {
          private final MetaRsocketModuleConfigurationClass rsocketModuleConfigurationClass = register(new MetaRsocketModuleConfigurationClass());

          private final MetaRsocketServerConfigurationClass rsocketServerConfigurationClass = register(new MetaRsocketServerConfigurationClass());

          private final MetaRsocketKeepAliveConfigurationClass rsocketKeepAliveConfigurationClass = register(new MetaRsocketKeepAliveConfigurationClass());

          private final MetaRsocketResumeConfigurationClass rsocketResumeConfigurationClass = register(new MetaRsocketResumeConfigurationClass());

          private final MetaRsocketRetryConfigurationClass rsocketRetryConfigurationClass = register(new MetaRsocketRetryConfigurationClass());

          private final MetaRsocketConnectorConfigurationClass rsocketConnectorConfigurationClass = register(new MetaRsocketConnectorConfigurationClass());

          private final MetaRsocketCommunicatorConfigurationClass rsocketCommunicatorConfigurationClass = register(new MetaRsocketCommunicatorConfigurationClass());

          private MetaConfigurationPackage() {
            super("configuration");
          }

          public MetaRsocketModuleConfigurationClass rsocketModuleConfigurationClass() {
            return rsocketModuleConfigurationClass;
          }

          public MetaRsocketServerConfigurationClass rsocketServerConfigurationClass() {
            return rsocketServerConfigurationClass;
          }

          public MetaRsocketKeepAliveConfigurationClass rsocketKeepAliveConfigurationClass() {
            return rsocketKeepAliveConfigurationClass;
          }

          public MetaRsocketResumeConfigurationClass rsocketResumeConfigurationClass() {
            return rsocketResumeConfigurationClass;
          }

          public MetaRsocketRetryConfigurationClass rsocketRetryConfigurationClass() {
            return rsocketRetryConfigurationClass;
          }

          public MetaRsocketConnectorConfigurationClass rsocketConnectorConfigurationClass() {
            return rsocketConnectorConfigurationClass;
          }

          public MetaRsocketCommunicatorConfigurationClass rsocketCommunicatorConfigurationClass() {
            return rsocketCommunicatorConfigurationClass;
          }

          public static final class MetaRsocketModuleConfigurationClass extends MetaClass<io.art.rsocket.configuration.RsocketModuleConfiguration> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaField<io.art.rsocket.refresher.RsocketModuleRefresher> refresherField = register(new MetaField<>("refresher",metaType(io.art.rsocket.refresher.RsocketModuleRefresher.class),false));

            private final MetaField<java.util.concurrent.atomic.AtomicReference<java.lang.Object>> consumerField = register(new MetaField<>("consumer",metaType(java.util.concurrent.atomic.AtomicReference.class,metaType(java.lang.Object.class)),false));

            private final MetaField<io.art.rsocket.configuration.RsocketServerConfiguration> serverConfigurationField = register(new MetaField<>("serverConfiguration",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.class),false));

            private final MetaField<io.art.rsocket.configuration.RsocketCommunicatorConfiguration> communicatorConfigurationField = register(new MetaField<>("communicatorConfiguration",metaType(io.art.rsocket.configuration.RsocketCommunicatorConfiguration.class),false));

            private final MetaField<java.lang.Boolean> activateServerField = register(new MetaField<>("activateServer",metaType(boolean.class),false));

            private final MetaField<java.lang.Boolean> activateCommunicatorField = register(new MetaField<>("activateCommunicator",metaType(boolean.class),false));

            private final MetaGetConsumerMethod getConsumerMethod = register(new MetaGetConsumerMethod());

            private final MetaGetServerConfigurationMethod getServerConfigurationMethod = register(new MetaGetServerConfigurationMethod());

            private final MetaGetCommunicatorConfigurationMethod getCommunicatorConfigurationMethod = register(new MetaGetCommunicatorConfigurationMethod());

            private final MetaIsActivateServerMethod isActivateServerMethod = register(new MetaIsActivateServerMethod());

            private final MetaIsActivateCommunicatorMethod isActivateCommunicatorMethod = register(new MetaIsActivateCommunicatorMethod());

            private final MetaConfiguratorClass configuratorClass = register(new MetaConfiguratorClass());

            private MetaRsocketModuleConfigurationClass() {
              super(metaType(io.art.rsocket.configuration.RsocketModuleConfiguration.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<io.art.rsocket.refresher.RsocketModuleRefresher> refresherField() {
              return refresherField;
            }

            public MetaField<java.util.concurrent.atomic.AtomicReference<java.lang.Object>> consumerField(
                ) {
              return consumerField;
            }

            public MetaField<io.art.rsocket.configuration.RsocketServerConfiguration> serverConfigurationField(
                ) {
              return serverConfigurationField;
            }

            public MetaField<io.art.rsocket.configuration.RsocketCommunicatorConfiguration> communicatorConfigurationField(
                ) {
              return communicatorConfigurationField;
            }

            public MetaField<java.lang.Boolean> activateServerField() {
              return activateServerField;
            }

            public MetaField<java.lang.Boolean> activateCommunicatorField() {
              return activateCommunicatorField;
            }

            public MetaGetConsumerMethod getConsumerMethod() {
              return getConsumerMethod;
            }

            public MetaGetServerConfigurationMethod getServerConfigurationMethod() {
              return getServerConfigurationMethod;
            }

            public MetaGetCommunicatorConfigurationMethod getCommunicatorConfigurationMethod() {
              return getCommunicatorConfigurationMethod;
            }

            public MetaIsActivateServerMethod isActivateServerMethod() {
              return isActivateServerMethod;
            }

            public MetaIsActivateCommunicatorMethod isActivateCommunicatorMethod() {
              return isActivateCommunicatorMethod;
            }

            public MetaConfiguratorClass configuratorClass() {
              return configuratorClass;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.configuration.RsocketModuleConfiguration> {
              private final MetaParameter<io.art.rsocket.refresher.RsocketModuleRefresher> refresherParameter = register(new MetaParameter<>(0, "refresher",metaType(io.art.rsocket.refresher.RsocketModuleRefresher.class)));

              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.configuration.RsocketModuleConfiguration.class));
              }

              @Override
              public io.art.rsocket.configuration.RsocketModuleConfiguration invoke(
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return new io.art.rsocket.configuration.RsocketModuleConfiguration((io.art.rsocket.refresher.RsocketModuleRefresher)(arguments[0]));
              }

              @Override
              public io.art.rsocket.configuration.RsocketModuleConfiguration invoke(
                  java.lang.Object argument) throws java.lang.Throwable {
                return new io.art.rsocket.configuration.RsocketModuleConfiguration((io.art.rsocket.refresher.RsocketModuleRefresher)(argument));
              }

              public MetaParameter<io.art.rsocket.refresher.RsocketModuleRefresher> refresherParameter(
                  ) {
                return refresherParameter;
              }
            }

            public static final class MetaGetConsumerMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketModuleConfiguration, io.art.rsocket.refresher.RsocketModuleRefresher.Consumer> {
              private MetaGetConsumerMethod() {
                super("getConsumer",metaType(io.art.rsocket.refresher.RsocketModuleRefresher.Consumer.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketModuleConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getConsumer();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketModuleConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getConsumer();
              }
            }

            public static final class MetaGetServerConfigurationMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketModuleConfiguration, io.art.rsocket.configuration.RsocketServerConfiguration> {
              private MetaGetServerConfigurationMethod() {
                super("getServerConfiguration",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketModuleConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getServerConfiguration();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketModuleConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getServerConfiguration();
              }
            }

            public static final class MetaGetCommunicatorConfigurationMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketModuleConfiguration, io.art.rsocket.configuration.RsocketCommunicatorConfiguration> {
              private MetaGetCommunicatorConfigurationMethod() {
                super("getCommunicatorConfiguration",metaType(io.art.rsocket.configuration.RsocketCommunicatorConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketModuleConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getCommunicatorConfiguration();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketModuleConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getCommunicatorConfiguration();
              }
            }

            public static final class MetaIsActivateServerMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketModuleConfiguration, java.lang.Boolean> {
              private MetaIsActivateServerMethod() {
                super("isActivateServer",metaType(boolean.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketModuleConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.isActivateServer();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketModuleConfiguration instance) throws
                  java.lang.Throwable {
                return instance.isActivateServer();
              }
            }

            public static final class MetaIsActivateCommunicatorMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketModuleConfiguration, java.lang.Boolean> {
              private MetaIsActivateCommunicatorMethod() {
                super("isActivateCommunicator",metaType(boolean.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketModuleConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.isActivateCommunicator();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketModuleConfiguration instance) throws
                  java.lang.Throwable {
                return instance.isActivateCommunicator();
              }
            }

            public static final class MetaConfiguratorClass extends MetaClass<io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator> {
              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

              private final MetaField<io.art.rsocket.configuration.RsocketModuleConfiguration> configurationField = register(new MetaField<>("configuration",metaType(io.art.rsocket.configuration.RsocketModuleConfiguration.class),false));

              private final MetaFromMethod fromMethod = register(new MetaFromMethod());

              private final MetaInitializeMethod initializeMethod = register(new MetaInitializeMethod());

              private MetaConfiguratorClass() {
                super(metaType(io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator.class));
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaField<io.art.rsocket.configuration.RsocketModuleConfiguration> configurationField(
                  ) {
                return configurationField;
              }

              public MetaFromMethod fromMethod() {
                return fromMethod;
              }

              public MetaInitializeMethod initializeMethod() {
                return initializeMethod;
              }

              public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator> {
                private final MetaParameter<io.art.rsocket.configuration.RsocketModuleConfiguration> configurationParameter = register(new MetaParameter<>(0, "configuration",metaType(io.art.rsocket.configuration.RsocketModuleConfiguration.class)));

                private MetaConstructorConstructor() {
                  super(metaType(io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator.class));
                }

                @Override
                public io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator invoke(
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return new io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator((io.art.rsocket.configuration.RsocketModuleConfiguration)(arguments[0]));
                }

                @Override
                public io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator invoke(
                    java.lang.Object argument) throws java.lang.Throwable {
                  return new io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator((io.art.rsocket.configuration.RsocketModuleConfiguration)(argument));
                }

                public MetaParameter<io.art.rsocket.configuration.RsocketModuleConfiguration> configurationParameter(
                    ) {
                  return configurationParameter;
                }
              }

              public static final class MetaFromMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator, io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator> {
                private final MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter = register(new MetaParameter<>(0, "source",metaType(io.art.core.source.ConfigurationSource.class)));

                private MetaFromMethod() {
                  super("from",metaType(io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.from((io.art.core.source.ConfigurationSource)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator instance,
                    java.lang.Object argument) throws java.lang.Throwable {
                  return instance.from((io.art.core.source.ConfigurationSource)(argument));
                }

                public MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter() {
                  return sourceParameter;
                }
              }

              public static final class MetaInitializeMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator, io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator> {
                private final MetaParameter<io.art.rsocket.configuration.RsocketModuleConfiguration> configurationParameter = register(new MetaParameter<>(0, "configuration",metaType(io.art.rsocket.configuration.RsocketModuleConfiguration.class)));

                private MetaInitializeMethod() {
                  super("initialize",metaType(io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.initialize((io.art.rsocket.configuration.RsocketModuleConfiguration)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator instance,
                    java.lang.Object argument) throws java.lang.Throwable {
                  return instance.initialize((io.art.rsocket.configuration.RsocketModuleConfiguration)(argument));
                }

                public MetaParameter<io.art.rsocket.configuration.RsocketModuleConfiguration> configurationParameter(
                    ) {
                  return configurationParameter;
                }
              }
            }
          }

          public static final class MetaRsocketServerConfigurationClass extends MetaClass<io.art.rsocket.configuration.RsocketServerConfiguration> {
            private final MetaField<reactor.netty.tcp.TcpServer> tcpServerField = register(new MetaField<>("tcpServer",metaType(reactor.netty.tcp.TcpServer.class),false));

            private final MetaField<reactor.netty.http.server.HttpServer> httpWebSocketServerField = register(new MetaField<>("httpWebSocketServer",metaType(reactor.netty.http.server.HttpServer.class),false));

            private final MetaField<io.art.core.model.ServiceMethodIdentifier> defaultServiceMethodField = register(new MetaField<>("defaultServiceMethod",metaType(io.art.core.model.ServiceMethodIdentifier.class),false));

            private final MetaField<Integer> tcpMaxFrameLengthField = register(new MetaField<>("tcpMaxFrameLength",metaType(int.class),false));

            private final MetaField<java.lang.Boolean> loggingField = register(new MetaField<>("logging",metaType(boolean.class),false));

            private final MetaField<Integer> fragmentationMtuField = register(new MetaField<>("fragmentationMtu",metaType(int.class),false));

            private final MetaField<io.art.rsocket.configuration.RsocketResumeConfiguration> resumeField = register(new MetaField<>("resume",metaType(io.art.rsocket.configuration.RsocketResumeConfiguration.class),false));

            private final MetaField<io.rsocket.frame.decoder.PayloadDecoder> payloadDecoderField = register(new MetaField<>("payloadDecoder",metaType(io.rsocket.frame.decoder.PayloadDecoder.class),false));

            private final MetaField<Integer> maxInboundPayloadSizeField = register(new MetaField<>("maxInboundPayloadSize",metaType(int.class),false));

            private final MetaField<io.art.rsocket.constants.RsocketModuleConstants.TransportMode> transportField = register(new MetaField<>("transport",metaEnum(io.art.rsocket.constants.RsocketModuleConstants.TransportMode.class, io.art.rsocket.constants.RsocketModuleConstants.TransportMode::valueOf),false));

            private final MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> defaultDataFormatField = register(new MetaField<>("defaultDataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf),false));

            private final MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> defaultMetaDataFormatField = register(new MetaField<>("defaultMetaDataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf),false));

            private final MetaDefaultsMethod defaultsMethod = register(new MetaDefaultsMethod());

            private final MetaFromMethod fromMethod = register(new MetaFromMethod());

            private final MetaToBuilderMethod toBuilderMethod = register(new MetaToBuilderMethod());

            private final MetaGetTcpServerMethod getTcpServerMethod = register(new MetaGetTcpServerMethod());

            private final MetaGetHttpWebSocketServerMethod getHttpWebSocketServerMethod = register(new MetaGetHttpWebSocketServerMethod());

            private final MetaGetDefaultServiceMethodMethod getDefaultServiceMethodMethod = register(new MetaGetDefaultServiceMethodMethod());

            private final MetaGetTcpMaxFrameLengthMethod getTcpMaxFrameLengthMethod = register(new MetaGetTcpMaxFrameLengthMethod());

            private final MetaIsLoggingMethod isLoggingMethod = register(new MetaIsLoggingMethod());

            private final MetaGetFragmentationMtuMethod getFragmentationMtuMethod = register(new MetaGetFragmentationMtuMethod());

            private final MetaGetResumeMethod getResumeMethod = register(new MetaGetResumeMethod());

            private final MetaGetPayloadDecoderMethod getPayloadDecoderMethod = register(new MetaGetPayloadDecoderMethod());

            private final MetaGetMaxInboundPayloadSizeMethod getMaxInboundPayloadSizeMethod = register(new MetaGetMaxInboundPayloadSizeMethod());

            private final MetaGetTransportMethod getTransportMethod = register(new MetaGetTransportMethod());

            private final MetaGetDefaultDataFormatMethod getDefaultDataFormatMethod = register(new MetaGetDefaultDataFormatMethod());

            private final MetaGetDefaultMetaDataFormatMethod getDefaultMetaDataFormatMethod = register(new MetaGetDefaultMetaDataFormatMethod());

            private final MetaRsocketServerConfigurationBuilderClass rsocketServerConfigurationBuilderClass = register(new MetaRsocketServerConfigurationBuilderClass());

            private MetaRsocketServerConfigurationClass() {
              super(metaType(io.art.rsocket.configuration.RsocketServerConfiguration.class));
            }

            public MetaField<reactor.netty.tcp.TcpServer> tcpServerField() {
              return tcpServerField;
            }

            public MetaField<reactor.netty.http.server.HttpServer> httpWebSocketServerField() {
              return httpWebSocketServerField;
            }

            public MetaField<io.art.core.model.ServiceMethodIdentifier> defaultServiceMethodField(
                ) {
              return defaultServiceMethodField;
            }

            public MetaField<Integer> tcpMaxFrameLengthField() {
              return tcpMaxFrameLengthField;
            }

            public MetaField<java.lang.Boolean> loggingField() {
              return loggingField;
            }

            public MetaField<Integer> fragmentationMtuField() {
              return fragmentationMtuField;
            }

            public MetaField<io.art.rsocket.configuration.RsocketResumeConfiguration> resumeField(
                ) {
              return resumeField;
            }

            public MetaField<io.rsocket.frame.decoder.PayloadDecoder> payloadDecoderField() {
              return payloadDecoderField;
            }

            public MetaField<Integer> maxInboundPayloadSizeField() {
              return maxInboundPayloadSizeField;
            }

            public MetaField<io.art.rsocket.constants.RsocketModuleConstants.TransportMode> transportField(
                ) {
              return transportField;
            }

            public MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> defaultDataFormatField(
                ) {
              return defaultDataFormatField;
            }

            public MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> defaultMetaDataFormatField(
                ) {
              return defaultMetaDataFormatField;
            }

            public MetaDefaultsMethod defaultsMethod() {
              return defaultsMethod;
            }

            public MetaFromMethod fromMethod() {
              return fromMethod;
            }

            public MetaToBuilderMethod toBuilderMethod() {
              return toBuilderMethod;
            }

            public MetaGetTcpServerMethod getTcpServerMethod() {
              return getTcpServerMethod;
            }

            public MetaGetHttpWebSocketServerMethod getHttpWebSocketServerMethod() {
              return getHttpWebSocketServerMethod;
            }

            public MetaGetDefaultServiceMethodMethod getDefaultServiceMethodMethod() {
              return getDefaultServiceMethodMethod;
            }

            public MetaGetTcpMaxFrameLengthMethod getTcpMaxFrameLengthMethod() {
              return getTcpMaxFrameLengthMethod;
            }

            public MetaIsLoggingMethod isLoggingMethod() {
              return isLoggingMethod;
            }

            public MetaGetFragmentationMtuMethod getFragmentationMtuMethod() {
              return getFragmentationMtuMethod;
            }

            public MetaGetResumeMethod getResumeMethod() {
              return getResumeMethod;
            }

            public MetaGetPayloadDecoderMethod getPayloadDecoderMethod() {
              return getPayloadDecoderMethod;
            }

            public MetaGetMaxInboundPayloadSizeMethod getMaxInboundPayloadSizeMethod() {
              return getMaxInboundPayloadSizeMethod;
            }

            public MetaGetTransportMethod getTransportMethod() {
              return getTransportMethod;
            }

            public MetaGetDefaultDataFormatMethod getDefaultDataFormatMethod() {
              return getDefaultDataFormatMethod;
            }

            public MetaGetDefaultMetaDataFormatMethod getDefaultMetaDataFormatMethod() {
              return getDefaultMetaDataFormatMethod;
            }

            public MetaRsocketServerConfigurationBuilderClass rsocketServerConfigurationBuilderClass(
                ) {
              return rsocketServerConfigurationBuilderClass;
            }

            public static final class MetaDefaultsMethod extends StaticMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration> {
              private MetaDefaultsMethod() {
                super("defaults",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                return io.art.rsocket.configuration.RsocketServerConfiguration.defaults();
              }

              @Override
              public java.lang.Object invoke() throws java.lang.Throwable {
                return io.art.rsocket.configuration.RsocketServerConfiguration.defaults();
              }
            }

            public static final class MetaFromMethod extends StaticMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration> {
              private final MetaParameter<io.art.rsocket.refresher.RsocketModuleRefresher> refresherParameter = register(new MetaParameter<>(0, "refresher",metaType(io.art.rsocket.refresher.RsocketModuleRefresher.class)));

              private final MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter = register(new MetaParameter<>(1, "source",metaType(io.art.core.source.ConfigurationSource.class)));

              private MetaFromMethod() {
                super("from",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                return io.art.rsocket.configuration.RsocketServerConfiguration.from((io.art.rsocket.refresher.RsocketModuleRefresher)(arguments[0]),(io.art.core.source.ConfigurationSource)(arguments[1]));
              }

              public MetaParameter<io.art.rsocket.refresher.RsocketModuleRefresher> refresherParameter(
                  ) {
                return refresherParameter;
              }

              public MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter() {
                return sourceParameter;
              }
            }

            public static final class MetaToBuilderMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration, io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder> {
              private MetaToBuilderMethod() {
                super("toBuilder",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.toBuilder();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance) throws
                  java.lang.Throwable {
                return instance.toBuilder();
              }
            }

            public static final class MetaGetTcpServerMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration, reactor.netty.tcp.TcpServer> {
              private MetaGetTcpServerMethod() {
                super("getTcpServer",metaType(reactor.netty.tcp.TcpServer.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getTcpServer();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getTcpServer();
              }
            }

            public static final class MetaGetHttpWebSocketServerMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration, reactor.netty.http.server.HttpServer> {
              private MetaGetHttpWebSocketServerMethod() {
                super("getHttpWebSocketServer",metaType(reactor.netty.http.server.HttpServer.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getHttpWebSocketServer();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getHttpWebSocketServer();
              }
            }

            public static final class MetaGetDefaultServiceMethodMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration, io.art.core.model.ServiceMethodIdentifier> {
              private MetaGetDefaultServiceMethodMethod() {
                super("getDefaultServiceMethod",metaType(io.art.core.model.ServiceMethodIdentifier.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getDefaultServiceMethod();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getDefaultServiceMethod();
              }
            }

            public static final class MetaGetTcpMaxFrameLengthMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration, Integer> {
              private MetaGetTcpMaxFrameLengthMethod() {
                super("getTcpMaxFrameLength",metaType(int.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getTcpMaxFrameLength();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getTcpMaxFrameLength();
              }
            }

            public static final class MetaIsLoggingMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration, java.lang.Boolean> {
              private MetaIsLoggingMethod() {
                super("isLogging",metaType(boolean.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.isLogging();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance) throws
                  java.lang.Throwable {
                return instance.isLogging();
              }
            }

            public static final class MetaGetFragmentationMtuMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration, Integer> {
              private MetaGetFragmentationMtuMethod() {
                super("getFragmentationMtu",metaType(int.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getFragmentationMtu();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getFragmentationMtu();
              }
            }

            public static final class MetaGetResumeMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration, io.art.rsocket.configuration.RsocketResumeConfiguration> {
              private MetaGetResumeMethod() {
                super("getResume",metaType(io.art.rsocket.configuration.RsocketResumeConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getResume();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getResume();
              }
            }

            public static final class MetaGetPayloadDecoderMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration, io.rsocket.frame.decoder.PayloadDecoder> {
              private MetaGetPayloadDecoderMethod() {
                super("getPayloadDecoder",metaType(io.rsocket.frame.decoder.PayloadDecoder.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getPayloadDecoder();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getPayloadDecoder();
              }
            }

            public static final class MetaGetMaxInboundPayloadSizeMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration, Integer> {
              private MetaGetMaxInboundPayloadSizeMethod() {
                super("getMaxInboundPayloadSize",metaType(int.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getMaxInboundPayloadSize();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getMaxInboundPayloadSize();
              }
            }

            public static final class MetaGetTransportMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration, io.art.rsocket.constants.RsocketModuleConstants.TransportMode> {
              private MetaGetTransportMethod() {
                super("getTransport",metaEnum(io.art.rsocket.constants.RsocketModuleConstants.TransportMode.class, io.art.rsocket.constants.RsocketModuleConstants.TransportMode::valueOf));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getTransport();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getTransport();
              }
            }

            public static final class MetaGetDefaultDataFormatMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration, io.art.transport.constants.TransportModuleConstants.DataFormat> {
              private MetaGetDefaultDataFormatMethod() {
                super("getDefaultDataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getDefaultDataFormat();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getDefaultDataFormat();
              }
            }

            public static final class MetaGetDefaultMetaDataFormatMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration, io.art.transport.constants.TransportModuleConstants.DataFormat> {
              private MetaGetDefaultMetaDataFormatMethod() {
                super("getDefaultMetaDataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getDefaultMetaDataFormat();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketServerConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getDefaultMetaDataFormat();
              }
            }

            public static final class MetaRsocketServerConfigurationBuilderClass extends MetaClass<io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder> {
              private final MetaField<reactor.netty.tcp.TcpServer> tcpServerField = register(new MetaField<>("tcpServer",metaType(reactor.netty.tcp.TcpServer.class),false));

              private final MetaField<reactor.netty.http.server.HttpServer> httpWebSocketServerField = register(new MetaField<>("httpWebSocketServer",metaType(reactor.netty.http.server.HttpServer.class),false));

              private final MetaField<io.art.core.model.ServiceMethodIdentifier> defaultServiceMethodField = register(new MetaField<>("defaultServiceMethod",metaType(io.art.core.model.ServiceMethodIdentifier.class),false));

              private final MetaField<Integer> tcpMaxFrameLengthField = register(new MetaField<>("tcpMaxFrameLength",metaType(int.class),false));

              private final MetaField<java.lang.Boolean> loggingField = register(new MetaField<>("logging",metaType(boolean.class),false));

              private final MetaField<Integer> fragmentationMtuField = register(new MetaField<>("fragmentationMtu",metaType(int.class),false));

              private final MetaField<io.art.rsocket.configuration.RsocketResumeConfiguration> resumeField = register(new MetaField<>("resume",metaType(io.art.rsocket.configuration.RsocketResumeConfiguration.class),false));

              private final MetaField<io.rsocket.frame.decoder.PayloadDecoder> payloadDecoderField = register(new MetaField<>("payloadDecoder",metaType(io.rsocket.frame.decoder.PayloadDecoder.class),false));

              private final MetaField<Integer> maxInboundPayloadSizeField = register(new MetaField<>("maxInboundPayloadSize",metaType(int.class),false));

              private final MetaField<io.art.rsocket.constants.RsocketModuleConstants.TransportMode> transportField = register(new MetaField<>("transport",metaEnum(io.art.rsocket.constants.RsocketModuleConstants.TransportMode.class, io.art.rsocket.constants.RsocketModuleConstants.TransportMode::valueOf),false));

              private final MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> defaultDataFormatField = register(new MetaField<>("defaultDataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf),false));

              private final MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> defaultMetaDataFormatField = register(new MetaField<>("defaultMetaDataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf),false));

              private final MetaTcpServerMethod tcpServerMethod = register(new MetaTcpServerMethod());

              private final MetaHttpWebSocketServerMethod httpWebSocketServerMethod = register(new MetaHttpWebSocketServerMethod());

              private final MetaDefaultServiceMethodMethod defaultServiceMethodMethod = register(new MetaDefaultServiceMethodMethod());

              private final MetaTcpMaxFrameLengthMethod tcpMaxFrameLengthMethod = register(new MetaTcpMaxFrameLengthMethod());

              private final MetaLoggingMethod loggingMethod = register(new MetaLoggingMethod());

              private final MetaFragmentationMtuMethod fragmentationMtuMethod = register(new MetaFragmentationMtuMethod());

              private final MetaResumeMethod resumeMethod = register(new MetaResumeMethod());

              private final MetaPayloadDecoderMethod payloadDecoderMethod = register(new MetaPayloadDecoderMethod());

              private final MetaMaxInboundPayloadSizeMethod maxInboundPayloadSizeMethod = register(new MetaMaxInboundPayloadSizeMethod());

              private final MetaTransportMethod transportMethod = register(new MetaTransportMethod());

              private final MetaDefaultDataFormatMethod defaultDataFormatMethod = register(new MetaDefaultDataFormatMethod());

              private final MetaDefaultMetaDataFormatMethod defaultMetaDataFormatMethod = register(new MetaDefaultMetaDataFormatMethod());

              private final MetaBuildMethod buildMethod = register(new MetaBuildMethod());

              private MetaRsocketServerConfigurationBuilderClass() {
                super(metaType(io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder.class));
              }

              public MetaField<reactor.netty.tcp.TcpServer> tcpServerField() {
                return tcpServerField;
              }

              public MetaField<reactor.netty.http.server.HttpServer> httpWebSocketServerField() {
                return httpWebSocketServerField;
              }

              public MetaField<io.art.core.model.ServiceMethodIdentifier> defaultServiceMethodField(
                  ) {
                return defaultServiceMethodField;
              }

              public MetaField<Integer> tcpMaxFrameLengthField() {
                return tcpMaxFrameLengthField;
              }

              public MetaField<java.lang.Boolean> loggingField() {
                return loggingField;
              }

              public MetaField<Integer> fragmentationMtuField() {
                return fragmentationMtuField;
              }

              public MetaField<io.art.rsocket.configuration.RsocketResumeConfiguration> resumeField(
                  ) {
                return resumeField;
              }

              public MetaField<io.rsocket.frame.decoder.PayloadDecoder> payloadDecoderField() {
                return payloadDecoderField;
              }

              public MetaField<Integer> maxInboundPayloadSizeField() {
                return maxInboundPayloadSizeField;
              }

              public MetaField<io.art.rsocket.constants.RsocketModuleConstants.TransportMode> transportField(
                  ) {
                return transportField;
              }

              public MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> defaultDataFormatField(
                  ) {
                return defaultDataFormatField;
              }

              public MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> defaultMetaDataFormatField(
                  ) {
                return defaultMetaDataFormatField;
              }

              public MetaTcpServerMethod tcpServerMethod() {
                return tcpServerMethod;
              }

              public MetaHttpWebSocketServerMethod httpWebSocketServerMethod() {
                return httpWebSocketServerMethod;
              }

              public MetaDefaultServiceMethodMethod defaultServiceMethodMethod() {
                return defaultServiceMethodMethod;
              }

              public MetaTcpMaxFrameLengthMethod tcpMaxFrameLengthMethod() {
                return tcpMaxFrameLengthMethod;
              }

              public MetaLoggingMethod loggingMethod() {
                return loggingMethod;
              }

              public MetaFragmentationMtuMethod fragmentationMtuMethod() {
                return fragmentationMtuMethod;
              }

              public MetaResumeMethod resumeMethod() {
                return resumeMethod;
              }

              public MetaPayloadDecoderMethod payloadDecoderMethod() {
                return payloadDecoderMethod;
              }

              public MetaMaxInboundPayloadSizeMethod maxInboundPayloadSizeMethod() {
                return maxInboundPayloadSizeMethod;
              }

              public MetaTransportMethod transportMethod() {
                return transportMethod;
              }

              public MetaDefaultDataFormatMethod defaultDataFormatMethod() {
                return defaultDataFormatMethod;
              }

              public MetaDefaultMetaDataFormatMethod defaultMetaDataFormatMethod() {
                return defaultMetaDataFormatMethod;
              }

              public MetaBuildMethod buildMethod() {
                return buildMethod;
              }

              public static final class MetaTcpServerMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder, io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder> {
                private final MetaParameter<reactor.netty.tcp.TcpServer> tcpServerParameter = register(new MetaParameter<>(0, "tcpServer",metaType(reactor.netty.tcp.TcpServer.class)));

                private MetaTcpServerMethod() {
                  super("tcpServer",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.tcpServer((reactor.netty.tcp.TcpServer)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object argument) throws java.lang.Throwable {
                  return instance.tcpServer((reactor.netty.tcp.TcpServer)(argument));
                }

                public MetaParameter<reactor.netty.tcp.TcpServer> tcpServerParameter() {
                  return tcpServerParameter;
                }
              }

              public static final class MetaHttpWebSocketServerMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder, io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder> {
                private final MetaParameter<reactor.netty.http.server.HttpServer> httpWebSocketServerParameter = register(new MetaParameter<>(0, "httpWebSocketServer",metaType(reactor.netty.http.server.HttpServer.class)));

                private MetaHttpWebSocketServerMethod() {
                  super("httpWebSocketServer",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.httpWebSocketServer((reactor.netty.http.server.HttpServer)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object argument) throws java.lang.Throwable {
                  return instance.httpWebSocketServer((reactor.netty.http.server.HttpServer)(argument));
                }

                public MetaParameter<reactor.netty.http.server.HttpServer> httpWebSocketServerParameter(
                    ) {
                  return httpWebSocketServerParameter;
                }
              }

              public static final class MetaDefaultServiceMethodMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder, io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder> {
                private final MetaParameter<io.art.core.model.ServiceMethodIdentifier> defaultServiceMethodParameter = register(new MetaParameter<>(0, "defaultServiceMethod",metaType(io.art.core.model.ServiceMethodIdentifier.class)));

                private MetaDefaultServiceMethodMethod() {
                  super("defaultServiceMethod",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.defaultServiceMethod((io.art.core.model.ServiceMethodIdentifier)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object argument) throws java.lang.Throwable {
                  return instance.defaultServiceMethod((io.art.core.model.ServiceMethodIdentifier)(argument));
                }

                public MetaParameter<io.art.core.model.ServiceMethodIdentifier> defaultServiceMethodParameter(
                    ) {
                  return defaultServiceMethodParameter;
                }
              }

              public static final class MetaTcpMaxFrameLengthMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder, io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder> {
                private final MetaParameter<Integer> tcpMaxFrameLengthParameter = register(new MetaParameter<>(0, "tcpMaxFrameLength",metaType(int.class)));

                private MetaTcpMaxFrameLengthMethod() {
                  super("tcpMaxFrameLength",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.tcpMaxFrameLength((int)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object argument) throws java.lang.Throwable {
                  return instance.tcpMaxFrameLength((int)(argument));
                }

                public MetaParameter<Integer> tcpMaxFrameLengthParameter() {
                  return tcpMaxFrameLengthParameter;
                }
              }

              public static final class MetaLoggingMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder, io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder> {
                private final MetaParameter<java.lang.Boolean> loggingParameter = register(new MetaParameter<>(0, "logging",metaType(boolean.class)));

                private MetaLoggingMethod() {
                  super("logging",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.logging((boolean)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object argument) throws java.lang.Throwable {
                  return instance.logging((boolean)(argument));
                }

                public MetaParameter<java.lang.Boolean> loggingParameter() {
                  return loggingParameter;
                }
              }

              public static final class MetaFragmentationMtuMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder, io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder> {
                private final MetaParameter<Integer> fragmentationMtuParameter = register(new MetaParameter<>(0, "fragmentationMtu",metaType(int.class)));

                private MetaFragmentationMtuMethod() {
                  super("fragmentationMtu",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.fragmentationMtu((int)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object argument) throws java.lang.Throwable {
                  return instance.fragmentationMtu((int)(argument));
                }

                public MetaParameter<Integer> fragmentationMtuParameter() {
                  return fragmentationMtuParameter;
                }
              }

              public static final class MetaResumeMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder, io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder> {
                private final MetaParameter<io.art.rsocket.configuration.RsocketResumeConfiguration> resumeParameter = register(new MetaParameter<>(0, "resume",metaType(io.art.rsocket.configuration.RsocketResumeConfiguration.class)));

                private MetaResumeMethod() {
                  super("resume",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.resume((io.art.rsocket.configuration.RsocketResumeConfiguration)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object argument) throws java.lang.Throwable {
                  return instance.resume((io.art.rsocket.configuration.RsocketResumeConfiguration)(argument));
                }

                public MetaParameter<io.art.rsocket.configuration.RsocketResumeConfiguration> resumeParameter(
                    ) {
                  return resumeParameter;
                }
              }

              public static final class MetaPayloadDecoderMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder, io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder> {
                private final MetaParameter<io.rsocket.frame.decoder.PayloadDecoder> payloadDecoderParameter = register(new MetaParameter<>(0, "payloadDecoder",metaType(io.rsocket.frame.decoder.PayloadDecoder.class)));

                private MetaPayloadDecoderMethod() {
                  super("payloadDecoder",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.payloadDecoder((io.rsocket.frame.decoder.PayloadDecoder)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object argument) throws java.lang.Throwable {
                  return instance.payloadDecoder((io.rsocket.frame.decoder.PayloadDecoder)(argument));
                }

                public MetaParameter<io.rsocket.frame.decoder.PayloadDecoder> payloadDecoderParameter(
                    ) {
                  return payloadDecoderParameter;
                }
              }

              public static final class MetaMaxInboundPayloadSizeMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder, io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder> {
                private final MetaParameter<Integer> maxInboundPayloadSizeParameter = register(new MetaParameter<>(0, "maxInboundPayloadSize",metaType(int.class)));

                private MetaMaxInboundPayloadSizeMethod() {
                  super("maxInboundPayloadSize",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.maxInboundPayloadSize((int)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object argument) throws java.lang.Throwable {
                  return instance.maxInboundPayloadSize((int)(argument));
                }

                public MetaParameter<Integer> maxInboundPayloadSizeParameter() {
                  return maxInboundPayloadSizeParameter;
                }
              }

              public static final class MetaTransportMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder, io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder> {
                private final MetaParameter<io.art.rsocket.constants.RsocketModuleConstants.TransportMode> transportParameter = register(new MetaParameter<>(0, "transport",metaEnum(io.art.rsocket.constants.RsocketModuleConstants.TransportMode.class, io.art.rsocket.constants.RsocketModuleConstants.TransportMode::valueOf)));

                private MetaTransportMethod() {
                  super("transport",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.transport((io.art.rsocket.constants.RsocketModuleConstants.TransportMode)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object argument) throws java.lang.Throwable {
                  return instance.transport((io.art.rsocket.constants.RsocketModuleConstants.TransportMode)(argument));
                }

                public MetaParameter<io.art.rsocket.constants.RsocketModuleConstants.TransportMode> transportParameter(
                    ) {
                  return transportParameter;
                }
              }

              public static final class MetaDefaultDataFormatMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder, io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder> {
                private final MetaParameter<io.art.transport.constants.TransportModuleConstants.DataFormat> defaultDataFormatParameter = register(new MetaParameter<>(0, "defaultDataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf)));

                private MetaDefaultDataFormatMethod() {
                  super("defaultDataFormat",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.defaultDataFormat((io.art.transport.constants.TransportModuleConstants.DataFormat)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object argument) throws java.lang.Throwable {
                  return instance.defaultDataFormat((io.art.transport.constants.TransportModuleConstants.DataFormat)(argument));
                }

                public MetaParameter<io.art.transport.constants.TransportModuleConstants.DataFormat> defaultDataFormatParameter(
                    ) {
                  return defaultDataFormatParameter;
                }
              }

              public static final class MetaDefaultMetaDataFormatMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder, io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder> {
                private final MetaParameter<io.art.transport.constants.TransportModuleConstants.DataFormat> defaultMetaDataFormatParameter = register(new MetaParameter<>(0, "defaultMetaDataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf)));

                private MetaDefaultMetaDataFormatMethod() {
                  super("defaultMetaDataFormat",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.defaultMetaDataFormat((io.art.transport.constants.TransportModuleConstants.DataFormat)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object argument) throws java.lang.Throwable {
                  return instance.defaultMetaDataFormat((io.art.transport.constants.TransportModuleConstants.DataFormat)(argument));
                }

                public MetaParameter<io.art.transport.constants.TransportModuleConstants.DataFormat> defaultMetaDataFormatParameter(
                    ) {
                  return defaultMetaDataFormatParameter;
                }
              }

              public static final class MetaBuildMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder, io.art.rsocket.configuration.RsocketServerConfiguration> {
                private MetaBuildMethod() {
                  super("build",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.build();
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.configuration.RsocketServerConfiguration.RsocketServerConfigurationBuilder instance)
                    throws java.lang.Throwable {
                  return instance.build();
                }
              }
            }
          }

          public static final class MetaRsocketKeepAliveConfigurationClass extends MetaClass<io.art.rsocket.configuration.RsocketKeepAliveConfiguration> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaField<java.time.Duration> intervalField = register(new MetaField<>("interval",metaType(java.time.Duration.class),false));

            private final MetaField<java.time.Duration> maxLifeTimeField = register(new MetaField<>("maxLifeTime",metaType(java.time.Duration.class),false));

            private final MetaRsocketKeepAliveMethod rsocketKeepAliveMethod = register(new MetaRsocketKeepAliveMethod());

            private final MetaRsocketKeepAlive_1Method rsocketKeepAlive_1Method = register(new MetaRsocketKeepAlive_1Method());

            private final MetaGetIntervalMethod getIntervalMethod = register(new MetaGetIntervalMethod());

            private final MetaGetMaxLifeTimeMethod getMaxLifeTimeMethod = register(new MetaGetMaxLifeTimeMethod());

            private MetaRsocketKeepAliveConfigurationClass() {
              super(metaType(io.art.rsocket.configuration.RsocketKeepAliveConfiguration.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<java.time.Duration> intervalField() {
              return intervalField;
            }

            public MetaField<java.time.Duration> maxLifeTimeField() {
              return maxLifeTimeField;
            }

            public MetaRsocketKeepAliveMethod rsocketKeepAliveMethod() {
              return rsocketKeepAliveMethod;
            }

            public MetaRsocketKeepAlive_1Method rsocketKeepAlive_1Method() {
              return rsocketKeepAlive_1Method;
            }

            public MetaGetIntervalMethod getIntervalMethod() {
              return getIntervalMethod;
            }

            public MetaGetMaxLifeTimeMethod getMaxLifeTimeMethod() {
              return getMaxLifeTimeMethod;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.configuration.RsocketKeepAliveConfiguration> {
              private final MetaParameter<java.time.Duration> intervalParameter = register(new MetaParameter<>(0, "interval",metaType(java.time.Duration.class)));

              private final MetaParameter<java.time.Duration> maxLifeTimeParameter = register(new MetaParameter<>(1, "maxLifeTime",metaType(java.time.Duration.class)));

              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.configuration.RsocketKeepAliveConfiguration.class));
              }

              @Override
              public io.art.rsocket.configuration.RsocketKeepAliveConfiguration invoke(
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return new io.art.rsocket.configuration.RsocketKeepAliveConfiguration((java.time.Duration)(arguments[0]),(java.time.Duration)(arguments[1]));
              }

              public MetaParameter<java.time.Duration> intervalParameter() {
                return intervalParameter;
              }

              public MetaParameter<java.time.Duration> maxLifeTimeParameter() {
                return maxLifeTimeParameter;
              }
            }

            public static final class MetaRsocketKeepAliveMethod extends StaticMetaMethod<io.art.rsocket.configuration.RsocketKeepAliveConfiguration> {
              private final MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter = register(new MetaParameter<>(0, "source",metaType(io.art.core.source.ConfigurationSource.class)));

              private MetaRsocketKeepAliveMethod() {
                super("rsocketKeepAlive",metaType(io.art.rsocket.configuration.RsocketKeepAliveConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                return io.art.rsocket.configuration.RsocketKeepAliveConfiguration.rsocketKeepAlive((io.art.core.source.ConfigurationSource)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object argument) throws java.lang.Throwable {
                return io.art.rsocket.configuration.RsocketKeepAliveConfiguration.rsocketKeepAlive((io.art.core.source.ConfigurationSource)(argument));
              }

              public MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter() {
                return sourceParameter;
              }
            }

            public static final class MetaRsocketKeepAlive_1Method extends StaticMetaMethod<io.art.rsocket.configuration.RsocketKeepAliveConfiguration> {
              private final MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter = register(new MetaParameter<>(0, "source",metaType(io.art.core.source.ConfigurationSource.class)));

              private final MetaParameter<io.art.rsocket.configuration.RsocketKeepAliveConfiguration> defaultKeepAliveParameter = register(new MetaParameter<>(1, "defaultKeepAlive",metaType(io.art.rsocket.configuration.RsocketKeepAliveConfiguration.class)));

              private MetaRsocketKeepAlive_1Method() {
                super("rsocketKeepAlive",metaType(io.art.rsocket.configuration.RsocketKeepAliveConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                return io.art.rsocket.configuration.RsocketKeepAliveConfiguration.rsocketKeepAlive((io.art.core.source.ConfigurationSource)(arguments[0]),(io.art.rsocket.configuration.RsocketKeepAliveConfiguration)(arguments[1]));
              }

              public MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter() {
                return sourceParameter;
              }

              public MetaParameter<io.art.rsocket.configuration.RsocketKeepAliveConfiguration> defaultKeepAliveParameter(
                  ) {
                return defaultKeepAliveParameter;
              }
            }

            public static final class MetaGetIntervalMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketKeepAliveConfiguration, java.time.Duration> {
              private MetaGetIntervalMethod() {
                super("getInterval",metaType(java.time.Duration.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketKeepAliveConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getInterval();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketKeepAliveConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getInterval();
              }
            }

            public static final class MetaGetMaxLifeTimeMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketKeepAliveConfiguration, java.time.Duration> {
              private MetaGetMaxLifeTimeMethod() {
                super("getMaxLifeTime",metaType(java.time.Duration.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketKeepAliveConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getMaxLifeTime();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketKeepAliveConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getMaxLifeTime();
              }
            }
          }

          public static final class MetaRsocketResumeConfigurationClass extends MetaClass<io.art.rsocket.configuration.RsocketResumeConfiguration> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaField<java.lang.Boolean> cleanupStoreOnKeepAliveField = register(new MetaField<>("cleanupStoreOnKeepAlive",metaType(boolean.class),false));

            private final MetaField<java.time.Duration> sessionDurationField = register(new MetaField<>("sessionDuration",metaType(java.time.Duration.class),false));

            private final MetaField<java.time.Duration> streamTimeoutField = register(new MetaField<>("streamTimeout",metaType(java.time.Duration.class),false));

            private final MetaField<io.art.rsocket.configuration.RsocketRetryConfiguration> retryConfigurationField = register(new MetaField<>("retryConfiguration",metaType(io.art.rsocket.configuration.RsocketRetryConfiguration.class),false));

            private final MetaToResumeMethod toResumeMethod = register(new MetaToResumeMethod());

            private final MetaRsocketResumeMethod rsocketResumeMethod = register(new MetaRsocketResumeMethod());

            private final MetaRsocketResume_1Method rsocketResume_1Method = register(new MetaRsocketResume_1Method());

            private final MetaIsCleanupStoreOnKeepAliveMethod isCleanupStoreOnKeepAliveMethod = register(new MetaIsCleanupStoreOnKeepAliveMethod());

            private final MetaGetSessionDurationMethod getSessionDurationMethod = register(new MetaGetSessionDurationMethod());

            private final MetaGetStreamTimeoutMethod getStreamTimeoutMethod = register(new MetaGetStreamTimeoutMethod());

            private final MetaGetRetryConfigurationMethod getRetryConfigurationMethod = register(new MetaGetRetryConfigurationMethod());

            private MetaRsocketResumeConfigurationClass() {
              super(metaType(io.art.rsocket.configuration.RsocketResumeConfiguration.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<java.lang.Boolean> cleanupStoreOnKeepAliveField() {
              return cleanupStoreOnKeepAliveField;
            }

            public MetaField<java.time.Duration> sessionDurationField() {
              return sessionDurationField;
            }

            public MetaField<java.time.Duration> streamTimeoutField() {
              return streamTimeoutField;
            }

            public MetaField<io.art.rsocket.configuration.RsocketRetryConfiguration> retryConfigurationField(
                ) {
              return retryConfigurationField;
            }

            public MetaToResumeMethod toResumeMethod() {
              return toResumeMethod;
            }

            public MetaRsocketResumeMethod rsocketResumeMethod() {
              return rsocketResumeMethod;
            }

            public MetaRsocketResume_1Method rsocketResume_1Method() {
              return rsocketResume_1Method;
            }

            public MetaIsCleanupStoreOnKeepAliveMethod isCleanupStoreOnKeepAliveMethod() {
              return isCleanupStoreOnKeepAliveMethod;
            }

            public MetaGetSessionDurationMethod getSessionDurationMethod() {
              return getSessionDurationMethod;
            }

            public MetaGetStreamTimeoutMethod getStreamTimeoutMethod() {
              return getStreamTimeoutMethod;
            }

            public MetaGetRetryConfigurationMethod getRetryConfigurationMethod() {
              return getRetryConfigurationMethod;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.configuration.RsocketResumeConfiguration> {
              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.configuration.RsocketResumeConfiguration.class));
              }

              @Override
              public io.art.rsocket.configuration.RsocketResumeConfiguration invoke(
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return new io.art.rsocket.configuration.RsocketResumeConfiguration();
              }

              @Override
              public io.art.rsocket.configuration.RsocketResumeConfiguration invoke() throws
                  java.lang.Throwable {
                return new io.art.rsocket.configuration.RsocketResumeConfiguration();
              }
            }

            public static final class MetaToResumeMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketResumeConfiguration, io.rsocket.core.Resume> {
              private MetaToResumeMethod() {
                super("toResume",metaType(io.rsocket.core.Resume.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketResumeConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.toResume();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketResumeConfiguration instance) throws
                  java.lang.Throwable {
                return instance.toResume();
              }
            }

            public static final class MetaRsocketResumeMethod extends StaticMetaMethod<io.art.rsocket.configuration.RsocketResumeConfiguration> {
              private final MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter = register(new MetaParameter<>(0, "source",metaType(io.art.core.source.ConfigurationSource.class)));

              private MetaRsocketResumeMethod() {
                super("rsocketResume",metaType(io.art.rsocket.configuration.RsocketResumeConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                return io.art.rsocket.configuration.RsocketResumeConfiguration.rsocketResume((io.art.core.source.ConfigurationSource)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object argument) throws java.lang.Throwable {
                return io.art.rsocket.configuration.RsocketResumeConfiguration.rsocketResume((io.art.core.source.ConfigurationSource)(argument));
              }

              public MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter() {
                return sourceParameter;
              }
            }

            public static final class MetaRsocketResume_1Method extends StaticMetaMethod<io.art.rsocket.configuration.RsocketResumeConfiguration> {
              private final MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter = register(new MetaParameter<>(0, "source",metaType(io.art.core.source.ConfigurationSource.class)));

              private final MetaParameter<io.art.rsocket.configuration.RsocketResumeConfiguration> defaultsParameter = register(new MetaParameter<>(1, "defaults",metaType(io.art.rsocket.configuration.RsocketResumeConfiguration.class)));

              private MetaRsocketResume_1Method() {
                super("rsocketResume",metaType(io.art.rsocket.configuration.RsocketResumeConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                return io.art.rsocket.configuration.RsocketResumeConfiguration.rsocketResume((io.art.core.source.ConfigurationSource)(arguments[0]),(io.art.rsocket.configuration.RsocketResumeConfiguration)(arguments[1]));
              }

              public MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter() {
                return sourceParameter;
              }

              public MetaParameter<io.art.rsocket.configuration.RsocketResumeConfiguration> defaultsParameter(
                  ) {
                return defaultsParameter;
              }
            }

            public static final class MetaIsCleanupStoreOnKeepAliveMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketResumeConfiguration, java.lang.Boolean> {
              private MetaIsCleanupStoreOnKeepAliveMethod() {
                super("isCleanupStoreOnKeepAlive",metaType(boolean.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketResumeConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.isCleanupStoreOnKeepAlive();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketResumeConfiguration instance) throws
                  java.lang.Throwable {
                return instance.isCleanupStoreOnKeepAlive();
              }
            }

            public static final class MetaGetSessionDurationMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketResumeConfiguration, java.time.Duration> {
              private MetaGetSessionDurationMethod() {
                super("getSessionDuration",metaType(java.time.Duration.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketResumeConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getSessionDuration();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketResumeConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getSessionDuration();
              }
            }

            public static final class MetaGetStreamTimeoutMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketResumeConfiguration, java.time.Duration> {
              private MetaGetStreamTimeoutMethod() {
                super("getStreamTimeout",metaType(java.time.Duration.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketResumeConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getStreamTimeout();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketResumeConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getStreamTimeout();
              }
            }

            public static final class MetaGetRetryConfigurationMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketResumeConfiguration, io.art.rsocket.configuration.RsocketRetryConfiguration> {
              private MetaGetRetryConfigurationMethod() {
                super("getRetryConfiguration",metaType(io.art.rsocket.configuration.RsocketRetryConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketResumeConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getRetryConfiguration();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketResumeConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getRetryConfiguration();
              }
            }
          }

          public static final class MetaRsocketRetryConfigurationClass extends MetaClass<io.art.rsocket.configuration.RsocketRetryConfiguration> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaField<java.time.Duration> minBackoffField = register(new MetaField<>("minBackoff",metaType(java.time.Duration.class),false));

            private final MetaField<Long> backOffMaxAttemptsField = register(new MetaField<>("backOffMaxAttempts",metaType(long.class),false));

            private final MetaField<Long> fixedDelayMaxAttemptsField = register(new MetaField<>("fixedDelayMaxAttempts",metaType(long.class),false));

            private final MetaField<io.art.rsocket.constants.RsocketModuleConstants.RetryPolicy> retryPolicyField = register(new MetaField<>("retryPolicy",metaEnum(io.art.rsocket.constants.RsocketModuleConstants.RetryPolicy.class, io.art.rsocket.constants.RsocketModuleConstants.RetryPolicy::valueOf),false));

            private final MetaField<java.time.Duration> fixedDelayField = register(new MetaField<>("fixedDelay",metaType(java.time.Duration.class),false));

            private final MetaField<Integer> maxInRowField = register(new MetaField<>("maxInRow",metaType(int.class),false));

            private final MetaField<Integer> maxField = register(new MetaField<>("max",metaType(int.class),false));

            private final MetaToRetryMethod toRetryMethod = register(new MetaToRetryMethod());

            private final MetaRsocketRetryMethod rsocketRetryMethod = register(new MetaRsocketRetryMethod());

            private final MetaRsocketRetry_1Method rsocketRetry_1Method = register(new MetaRsocketRetry_1Method());

            private final MetaGetMinBackoffMethod getMinBackoffMethod = register(new MetaGetMinBackoffMethod());

            private final MetaGetBackOffMaxAttemptsMethod getBackOffMaxAttemptsMethod = register(new MetaGetBackOffMaxAttemptsMethod());

            private final MetaGetFixedDelayMaxAttemptsMethod getFixedDelayMaxAttemptsMethod = register(new MetaGetFixedDelayMaxAttemptsMethod());

            private final MetaGetRetryPolicyMethod getRetryPolicyMethod = register(new MetaGetRetryPolicyMethod());

            private final MetaGetFixedDelayMethod getFixedDelayMethod = register(new MetaGetFixedDelayMethod());

            private final MetaGetMaxInRowMethod getMaxInRowMethod = register(new MetaGetMaxInRowMethod());

            private final MetaGetMaxMethod getMaxMethod = register(new MetaGetMaxMethod());

            private MetaRsocketRetryConfigurationClass() {
              super(metaType(io.art.rsocket.configuration.RsocketRetryConfiguration.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<java.time.Duration> minBackoffField() {
              return minBackoffField;
            }

            public MetaField<Long> backOffMaxAttemptsField() {
              return backOffMaxAttemptsField;
            }

            public MetaField<Long> fixedDelayMaxAttemptsField() {
              return fixedDelayMaxAttemptsField;
            }

            public MetaField<io.art.rsocket.constants.RsocketModuleConstants.RetryPolicy> retryPolicyField(
                ) {
              return retryPolicyField;
            }

            public MetaField<java.time.Duration> fixedDelayField() {
              return fixedDelayField;
            }

            public MetaField<Integer> maxInRowField() {
              return maxInRowField;
            }

            public MetaField<Integer> maxField() {
              return maxField;
            }

            public MetaToRetryMethod toRetryMethod() {
              return toRetryMethod;
            }

            public MetaRsocketRetryMethod rsocketRetryMethod() {
              return rsocketRetryMethod;
            }

            public MetaRsocketRetry_1Method rsocketRetry_1Method() {
              return rsocketRetry_1Method;
            }

            public MetaGetMinBackoffMethod getMinBackoffMethod() {
              return getMinBackoffMethod;
            }

            public MetaGetBackOffMaxAttemptsMethod getBackOffMaxAttemptsMethod() {
              return getBackOffMaxAttemptsMethod;
            }

            public MetaGetFixedDelayMaxAttemptsMethod getFixedDelayMaxAttemptsMethod() {
              return getFixedDelayMaxAttemptsMethod;
            }

            public MetaGetRetryPolicyMethod getRetryPolicyMethod() {
              return getRetryPolicyMethod;
            }

            public MetaGetFixedDelayMethod getFixedDelayMethod() {
              return getFixedDelayMethod;
            }

            public MetaGetMaxInRowMethod getMaxInRowMethod() {
              return getMaxInRowMethod;
            }

            public MetaGetMaxMethod getMaxMethod() {
              return getMaxMethod;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.configuration.RsocketRetryConfiguration> {
              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.configuration.RsocketRetryConfiguration.class));
              }

              @Override
              public io.art.rsocket.configuration.RsocketRetryConfiguration invoke(
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return new io.art.rsocket.configuration.RsocketRetryConfiguration();
              }

              @Override
              public io.art.rsocket.configuration.RsocketRetryConfiguration invoke() throws
                  java.lang.Throwable {
                return new io.art.rsocket.configuration.RsocketRetryConfiguration();
              }
            }

            public static final class MetaToRetryMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketRetryConfiguration, reactor.util.retry.Retry> {
              private MetaToRetryMethod() {
                super("toRetry",metaType(reactor.util.retry.Retry.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketRetryConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.toRetry();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketRetryConfiguration instance) throws
                  java.lang.Throwable {
                return instance.toRetry();
              }
            }

            public static final class MetaRsocketRetryMethod extends StaticMetaMethod<io.art.rsocket.configuration.RsocketRetryConfiguration> {
              private final MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter = register(new MetaParameter<>(0, "source",metaType(io.art.core.source.ConfigurationSource.class)));

              private MetaRsocketRetryMethod() {
                super("rsocketRetry",metaType(io.art.rsocket.configuration.RsocketRetryConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                return io.art.rsocket.configuration.RsocketRetryConfiguration.rsocketRetry((io.art.core.source.ConfigurationSource)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object argument) throws java.lang.Throwable {
                return io.art.rsocket.configuration.RsocketRetryConfiguration.rsocketRetry((io.art.core.source.ConfigurationSource)(argument));
              }

              public MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter() {
                return sourceParameter;
              }
            }

            public static final class MetaRsocketRetry_1Method extends StaticMetaMethod<io.art.rsocket.configuration.RsocketRetryConfiguration> {
              private final MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter = register(new MetaParameter<>(0, "source",metaType(io.art.core.source.ConfigurationSource.class)));

              private final MetaParameter<io.art.rsocket.configuration.RsocketRetryConfiguration> defaultsParameter = register(new MetaParameter<>(1, "defaults",metaType(io.art.rsocket.configuration.RsocketRetryConfiguration.class)));

              private MetaRsocketRetry_1Method() {
                super("rsocketRetry",metaType(io.art.rsocket.configuration.RsocketRetryConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                return io.art.rsocket.configuration.RsocketRetryConfiguration.rsocketRetry((io.art.core.source.ConfigurationSource)(arguments[0]),(io.art.rsocket.configuration.RsocketRetryConfiguration)(arguments[1]));
              }

              public MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter() {
                return sourceParameter;
              }

              public MetaParameter<io.art.rsocket.configuration.RsocketRetryConfiguration> defaultsParameter(
                  ) {
                return defaultsParameter;
              }
            }

            public static final class MetaGetMinBackoffMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketRetryConfiguration, java.time.Duration> {
              private MetaGetMinBackoffMethod() {
                super("getMinBackoff",metaType(java.time.Duration.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketRetryConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getMinBackoff();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketRetryConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getMinBackoff();
              }
            }

            public static final class MetaGetBackOffMaxAttemptsMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketRetryConfiguration, Long> {
              private MetaGetBackOffMaxAttemptsMethod() {
                super("getBackOffMaxAttempts",metaType(long.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketRetryConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getBackOffMaxAttempts();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketRetryConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getBackOffMaxAttempts();
              }
            }

            public static final class MetaGetFixedDelayMaxAttemptsMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketRetryConfiguration, Long> {
              private MetaGetFixedDelayMaxAttemptsMethod() {
                super("getFixedDelayMaxAttempts",metaType(long.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketRetryConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getFixedDelayMaxAttempts();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketRetryConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getFixedDelayMaxAttempts();
              }
            }

            public static final class MetaGetRetryPolicyMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketRetryConfiguration, io.art.rsocket.constants.RsocketModuleConstants.RetryPolicy> {
              private MetaGetRetryPolicyMethod() {
                super("getRetryPolicy",metaEnum(io.art.rsocket.constants.RsocketModuleConstants.RetryPolicy.class, io.art.rsocket.constants.RsocketModuleConstants.RetryPolicy::valueOf));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketRetryConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getRetryPolicy();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketRetryConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getRetryPolicy();
              }
            }

            public static final class MetaGetFixedDelayMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketRetryConfiguration, java.time.Duration> {
              private MetaGetFixedDelayMethod() {
                super("getFixedDelay",metaType(java.time.Duration.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketRetryConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getFixedDelay();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketRetryConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getFixedDelay();
              }
            }

            public static final class MetaGetMaxInRowMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketRetryConfiguration, Integer> {
              private MetaGetMaxInRowMethod() {
                super("getMaxInRow",metaType(int.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketRetryConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getMaxInRow();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketRetryConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getMaxInRow();
              }
            }

            public static final class MetaGetMaxMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketRetryConfiguration, Integer> {
              private MetaGetMaxMethod() {
                super("getMax",metaType(int.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketRetryConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getMax();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketRetryConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getMax();
              }
            }
          }

          public static final class MetaRsocketConnectorConfigurationClass extends MetaClass<io.art.rsocket.configuration.RsocketConnectorConfiguration> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaField<java.lang.String> connectorIdField = register(new MetaField<>("connectorId",metaType(java.lang.String.class),false));

            private final MetaField<io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode> payloadDecoderModeField = register(new MetaField<>("payloadDecoderMode",metaEnum(io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode.class, io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode::valueOf),false));

            private final MetaField<Integer> maxInboundPayloadSizeField = register(new MetaField<>("maxInboundPayloadSize",metaType(int.class),false));

            private final MetaField<Integer> fragmentField = register(new MetaField<>("fragment",metaType(int.class),false));

            private final MetaField<io.art.rsocket.configuration.RsocketKeepAliveConfiguration> keepAliveField = register(new MetaField<>("keepAlive",metaType(io.art.rsocket.configuration.RsocketKeepAliveConfiguration.class),false));

            private final MetaField<io.art.rsocket.configuration.RsocketResumeConfiguration> resumeField = register(new MetaField<>("resume",metaType(io.art.rsocket.configuration.RsocketResumeConfiguration.class),false));

            private final MetaField<io.art.rsocket.configuration.RsocketRetryConfiguration> retryField = register(new MetaField<>("retry",metaType(io.art.rsocket.configuration.RsocketRetryConfiguration.class),false));

            private final MetaField<io.art.rsocket.constants.RsocketModuleConstants.TransportMode> transportField = register(new MetaField<>("transport",metaEnum(io.art.rsocket.constants.RsocketModuleConstants.TransportMode.class, io.art.rsocket.constants.RsocketModuleConstants.TransportMode::valueOf),false));

            private final MetaField<reactor.netty.tcp.TcpClient> tcpClientField = register(new MetaField<>("tcpClient",metaType(reactor.netty.tcp.TcpClient.class),false));

            private final MetaField<Integer> tcpMaxFrameLengthField = register(new MetaField<>("tcpMaxFrameLength",metaType(int.class),false));

            private final MetaField<reactor.netty.http.client.HttpClient> httpWebSocketClientField = register(new MetaField<>("httpWebSocketClient",metaType(reactor.netty.http.client.HttpClient.class),false));

            private final MetaField<java.lang.String> httpWebSocketPathField = register(new MetaField<>("httpWebSocketPath",metaType(java.lang.String.class),false));

            private final MetaField<java.lang.Boolean> loggingField = register(new MetaField<>("logging",metaType(boolean.class),false));

            private final MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> dataFormatField = register(new MetaField<>("dataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf),false));

            private final MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> metaDataFormatField = register(new MetaField<>("metaDataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf),false));

            private final MetaDefaultsMethod defaultsMethod = register(new MetaDefaultsMethod());

            private final MetaRsocketConnectorMethod rsocketConnectorMethod = register(new MetaRsocketConnectorMethod());

            private final MetaRsocketConnector_1Method rsocketConnector_1Method = register(new MetaRsocketConnector_1Method());

            private final MetaGetConnectorIdMethod getConnectorIdMethod = register(new MetaGetConnectorIdMethod());

            private final MetaGetPayloadDecoderModeMethod getPayloadDecoderModeMethod = register(new MetaGetPayloadDecoderModeMethod());

            private final MetaGetMaxInboundPayloadSizeMethod getMaxInboundPayloadSizeMethod = register(new MetaGetMaxInboundPayloadSizeMethod());

            private final MetaGetFragmentMethod getFragmentMethod = register(new MetaGetFragmentMethod());

            private final MetaGetKeepAliveMethod getKeepAliveMethod = register(new MetaGetKeepAliveMethod());

            private final MetaGetResumeMethod getResumeMethod = register(new MetaGetResumeMethod());

            private final MetaGetRetryMethod getRetryMethod = register(new MetaGetRetryMethod());

            private final MetaGetTransportMethod getTransportMethod = register(new MetaGetTransportMethod());

            private final MetaGetTcpClientMethod getTcpClientMethod = register(new MetaGetTcpClientMethod());

            private final MetaGetTcpMaxFrameLengthMethod getTcpMaxFrameLengthMethod = register(new MetaGetTcpMaxFrameLengthMethod());

            private final MetaGetHttpWebSocketClientMethod getHttpWebSocketClientMethod = register(new MetaGetHttpWebSocketClientMethod());

            private final MetaGetHttpWebSocketPathMethod getHttpWebSocketPathMethod = register(new MetaGetHttpWebSocketPathMethod());

            private final MetaIsLoggingMethod isLoggingMethod = register(new MetaIsLoggingMethod());

            private final MetaGetDataFormatMethod getDataFormatMethod = register(new MetaGetDataFormatMethod());

            private final MetaGetMetaDataFormatMethod getMetaDataFormatMethod = register(new MetaGetMetaDataFormatMethod());

            private MetaRsocketConnectorConfigurationClass() {
              super(metaType(io.art.rsocket.configuration.RsocketConnectorConfiguration.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<java.lang.String> connectorIdField() {
              return connectorIdField;
            }

            public MetaField<io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode> payloadDecoderModeField(
                ) {
              return payloadDecoderModeField;
            }

            public MetaField<Integer> maxInboundPayloadSizeField() {
              return maxInboundPayloadSizeField;
            }

            public MetaField<Integer> fragmentField() {
              return fragmentField;
            }

            public MetaField<io.art.rsocket.configuration.RsocketKeepAliveConfiguration> keepAliveField(
                ) {
              return keepAliveField;
            }

            public MetaField<io.art.rsocket.configuration.RsocketResumeConfiguration> resumeField(
                ) {
              return resumeField;
            }

            public MetaField<io.art.rsocket.configuration.RsocketRetryConfiguration> retryField() {
              return retryField;
            }

            public MetaField<io.art.rsocket.constants.RsocketModuleConstants.TransportMode> transportField(
                ) {
              return transportField;
            }

            public MetaField<reactor.netty.tcp.TcpClient> tcpClientField() {
              return tcpClientField;
            }

            public MetaField<Integer> tcpMaxFrameLengthField() {
              return tcpMaxFrameLengthField;
            }

            public MetaField<reactor.netty.http.client.HttpClient> httpWebSocketClientField() {
              return httpWebSocketClientField;
            }

            public MetaField<java.lang.String> httpWebSocketPathField() {
              return httpWebSocketPathField;
            }

            public MetaField<java.lang.Boolean> loggingField() {
              return loggingField;
            }

            public MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> dataFormatField(
                ) {
              return dataFormatField;
            }

            public MetaField<io.art.transport.constants.TransportModuleConstants.DataFormat> metaDataFormatField(
                ) {
              return metaDataFormatField;
            }

            public MetaDefaultsMethod defaultsMethod() {
              return defaultsMethod;
            }

            public MetaRsocketConnectorMethod rsocketConnectorMethod() {
              return rsocketConnectorMethod;
            }

            public MetaRsocketConnector_1Method rsocketConnector_1Method() {
              return rsocketConnector_1Method;
            }

            public MetaGetConnectorIdMethod getConnectorIdMethod() {
              return getConnectorIdMethod;
            }

            public MetaGetPayloadDecoderModeMethod getPayloadDecoderModeMethod() {
              return getPayloadDecoderModeMethod;
            }

            public MetaGetMaxInboundPayloadSizeMethod getMaxInboundPayloadSizeMethod() {
              return getMaxInboundPayloadSizeMethod;
            }

            public MetaGetFragmentMethod getFragmentMethod() {
              return getFragmentMethod;
            }

            public MetaGetKeepAliveMethod getKeepAliveMethod() {
              return getKeepAliveMethod;
            }

            public MetaGetResumeMethod getResumeMethod() {
              return getResumeMethod;
            }

            public MetaGetRetryMethod getRetryMethod() {
              return getRetryMethod;
            }

            public MetaGetTransportMethod getTransportMethod() {
              return getTransportMethod;
            }

            public MetaGetTcpClientMethod getTcpClientMethod() {
              return getTcpClientMethod;
            }

            public MetaGetTcpMaxFrameLengthMethod getTcpMaxFrameLengthMethod() {
              return getTcpMaxFrameLengthMethod;
            }

            public MetaGetHttpWebSocketClientMethod getHttpWebSocketClientMethod() {
              return getHttpWebSocketClientMethod;
            }

            public MetaGetHttpWebSocketPathMethod getHttpWebSocketPathMethod() {
              return getHttpWebSocketPathMethod;
            }

            public MetaIsLoggingMethod isLoggingMethod() {
              return isLoggingMethod;
            }

            public MetaGetDataFormatMethod getDataFormatMethod() {
              return getDataFormatMethod;
            }

            public MetaGetMetaDataFormatMethod getMetaDataFormatMethod() {
              return getMetaDataFormatMethod;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.configuration.RsocketConnectorConfiguration> {
              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.configuration.RsocketConnectorConfiguration.class));
              }

              @Override
              public io.art.rsocket.configuration.RsocketConnectorConfiguration invoke(
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return new io.art.rsocket.configuration.RsocketConnectorConfiguration();
              }

              @Override
              public io.art.rsocket.configuration.RsocketConnectorConfiguration invoke() throws
                  java.lang.Throwable {
                return new io.art.rsocket.configuration.RsocketConnectorConfiguration();
              }
            }

            public static final class MetaDefaultsMethod extends StaticMetaMethod<io.art.rsocket.configuration.RsocketConnectorConfiguration> {
              private MetaDefaultsMethod() {
                super("defaults",metaType(io.art.rsocket.configuration.RsocketConnectorConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                return io.art.rsocket.configuration.RsocketConnectorConfiguration.defaults();
              }

              @Override
              public java.lang.Object invoke() throws java.lang.Throwable {
                return io.art.rsocket.configuration.RsocketConnectorConfiguration.defaults();
              }
            }

            public static final class MetaRsocketConnectorMethod extends StaticMetaMethod<io.art.rsocket.configuration.RsocketConnectorConfiguration> {
              private final MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter = register(new MetaParameter<>(0, "source",metaType(io.art.core.source.ConfigurationSource.class)));

              private MetaRsocketConnectorMethod() {
                super("rsocketConnector",metaType(io.art.rsocket.configuration.RsocketConnectorConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                return io.art.rsocket.configuration.RsocketConnectorConfiguration.rsocketConnector((io.art.core.source.ConfigurationSource)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object argument) throws java.lang.Throwable {
                return io.art.rsocket.configuration.RsocketConnectorConfiguration.rsocketConnector((io.art.core.source.ConfigurationSource)(argument));
              }

              public MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter() {
                return sourceParameter;
              }
            }

            public static final class MetaRsocketConnector_1Method extends StaticMetaMethod<io.art.rsocket.configuration.RsocketConnectorConfiguration> {
              private final MetaParameter<io.art.rsocket.refresher.RsocketModuleRefresher> refresherParameter = register(new MetaParameter<>(0, "refresher",metaType(io.art.rsocket.refresher.RsocketModuleRefresher.class)));

              private final MetaParameter<io.art.rsocket.configuration.RsocketConnectorConfiguration> defaultsParameter = register(new MetaParameter<>(1, "defaults",metaType(io.art.rsocket.configuration.RsocketConnectorConfiguration.class)));

              private final MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter = register(new MetaParameter<>(2, "source",metaType(io.art.core.source.ConfigurationSource.class)));

              private MetaRsocketConnector_1Method() {
                super("rsocketConnector",metaType(io.art.rsocket.configuration.RsocketConnectorConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                return io.art.rsocket.configuration.RsocketConnectorConfiguration.rsocketConnector((io.art.rsocket.refresher.RsocketModuleRefresher)(arguments[0]),(io.art.rsocket.configuration.RsocketConnectorConfiguration)(arguments[1]),(io.art.core.source.ConfigurationSource)(arguments[2]));
              }

              public MetaParameter<io.art.rsocket.refresher.RsocketModuleRefresher> refresherParameter(
                  ) {
                return refresherParameter;
              }

              public MetaParameter<io.art.rsocket.configuration.RsocketConnectorConfiguration> defaultsParameter(
                  ) {
                return defaultsParameter;
              }

              public MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter() {
                return sourceParameter;
              }
            }

            public static final class MetaGetConnectorIdMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketConnectorConfiguration, java.lang.String> {
              private MetaGetConnectorIdMethod() {
                super("getConnectorId",metaType(java.lang.String.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getConnectorId();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getConnectorId();
              }
            }

            public static final class MetaGetPayloadDecoderModeMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketConnectorConfiguration, io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode> {
              private MetaGetPayloadDecoderModeMethod() {
                super("getPayloadDecoderMode",metaEnum(io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode.class, io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode::valueOf));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getPayloadDecoderMode();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getPayloadDecoderMode();
              }
            }

            public static final class MetaGetMaxInboundPayloadSizeMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketConnectorConfiguration, Integer> {
              private MetaGetMaxInboundPayloadSizeMethod() {
                super("getMaxInboundPayloadSize",metaType(int.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getMaxInboundPayloadSize();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getMaxInboundPayloadSize();
              }
            }

            public static final class MetaGetFragmentMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketConnectorConfiguration, Integer> {
              private MetaGetFragmentMethod() {
                super("getFragment",metaType(int.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getFragment();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getFragment();
              }
            }

            public static final class MetaGetKeepAliveMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketConnectorConfiguration, io.art.rsocket.configuration.RsocketKeepAliveConfiguration> {
              private MetaGetKeepAliveMethod() {
                super("getKeepAlive",metaType(io.art.rsocket.configuration.RsocketKeepAliveConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getKeepAlive();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getKeepAlive();
              }
            }

            public static final class MetaGetResumeMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketConnectorConfiguration, io.art.rsocket.configuration.RsocketResumeConfiguration> {
              private MetaGetResumeMethod() {
                super("getResume",metaType(io.art.rsocket.configuration.RsocketResumeConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getResume();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getResume();
              }
            }

            public static final class MetaGetRetryMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketConnectorConfiguration, io.art.rsocket.configuration.RsocketRetryConfiguration> {
              private MetaGetRetryMethod() {
                super("getRetry",metaType(io.art.rsocket.configuration.RsocketRetryConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getRetry();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getRetry();
              }
            }

            public static final class MetaGetTransportMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketConnectorConfiguration, io.art.rsocket.constants.RsocketModuleConstants.TransportMode> {
              private MetaGetTransportMethod() {
                super("getTransport",metaEnum(io.art.rsocket.constants.RsocketModuleConstants.TransportMode.class, io.art.rsocket.constants.RsocketModuleConstants.TransportMode::valueOf));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getTransport();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getTransport();
              }
            }

            public static final class MetaGetTcpClientMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketConnectorConfiguration, reactor.netty.tcp.TcpClient> {
              private MetaGetTcpClientMethod() {
                super("getTcpClient",metaType(reactor.netty.tcp.TcpClient.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getTcpClient();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getTcpClient();
              }
            }

            public static final class MetaGetTcpMaxFrameLengthMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketConnectorConfiguration, Integer> {
              private MetaGetTcpMaxFrameLengthMethod() {
                super("getTcpMaxFrameLength",metaType(int.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getTcpMaxFrameLength();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getTcpMaxFrameLength();
              }
            }

            public static final class MetaGetHttpWebSocketClientMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketConnectorConfiguration, reactor.netty.http.client.HttpClient> {
              private MetaGetHttpWebSocketClientMethod() {
                super("getHttpWebSocketClient",metaType(reactor.netty.http.client.HttpClient.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getHttpWebSocketClient();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getHttpWebSocketClient();
              }
            }

            public static final class MetaGetHttpWebSocketPathMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketConnectorConfiguration, java.lang.String> {
              private MetaGetHttpWebSocketPathMethod() {
                super("getHttpWebSocketPath",metaType(java.lang.String.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getHttpWebSocketPath();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getHttpWebSocketPath();
              }
            }

            public static final class MetaIsLoggingMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketConnectorConfiguration, java.lang.Boolean> {
              private MetaIsLoggingMethod() {
                super("isLogging",metaType(boolean.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.isLogging();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance) throws
                  java.lang.Throwable {
                return instance.isLogging();
              }
            }

            public static final class MetaGetDataFormatMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketConnectorConfiguration, io.art.transport.constants.TransportModuleConstants.DataFormat> {
              private MetaGetDataFormatMethod() {
                super("getDataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getDataFormat();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getDataFormat();
              }
            }

            public static final class MetaGetMetaDataFormatMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketConnectorConfiguration, io.art.transport.constants.TransportModuleConstants.DataFormat> {
              private MetaGetMetaDataFormatMethod() {
                super("getMetaDataFormat",metaEnum(io.art.transport.constants.TransportModuleConstants.DataFormat.class, io.art.transport.constants.TransportModuleConstants.DataFormat::valueOf));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getMetaDataFormat();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketConnectorConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getMetaDataFormat();
              }
            }
          }

          public static final class MetaRsocketCommunicatorConfigurationClass extends MetaClass<io.art.rsocket.configuration.RsocketCommunicatorConfiguration> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaField<io.art.rsocket.configuration.RsocketConnectorConfiguration> defaultConnectorConfigurationField = register(new MetaField<>("defaultConnectorConfiguration",metaType(io.art.rsocket.configuration.RsocketConnectorConfiguration.class),false));

            private final MetaField<io.art.core.collection.ImmutableMap<java.lang.String, io.art.rsocket.configuration.RsocketConnectorConfiguration>> connectorConfigurationsField = register(new MetaField<>("connectorConfigurations",metaType(io.art.core.collection.ImmutableMap.class,metaType(java.lang.String.class),metaType(io.art.rsocket.configuration.RsocketConnectorConfiguration.class)),false));

            private final MetaIsLoggingMethod isLoggingMethod = register(new MetaIsLoggingMethod());

            private final MetaFromMethod fromMethod = register(new MetaFromMethod());

            private final MetaGetDefaultConnectorConfigurationMethod getDefaultConnectorConfigurationMethod = register(new MetaGetDefaultConnectorConfigurationMethod());

            private final MetaGetConnectorConfigurationsMethod getConnectorConfigurationsMethod = register(new MetaGetConnectorConfigurationsMethod());

            private MetaRsocketCommunicatorConfigurationClass() {
              super(metaType(io.art.rsocket.configuration.RsocketCommunicatorConfiguration.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<io.art.rsocket.configuration.RsocketConnectorConfiguration> defaultConnectorConfigurationField(
                ) {
              return defaultConnectorConfigurationField;
            }

            public MetaField<io.art.core.collection.ImmutableMap<java.lang.String, io.art.rsocket.configuration.RsocketConnectorConfiguration>> connectorConfigurationsField(
                ) {
              return connectorConfigurationsField;
            }

            public MetaIsLoggingMethod isLoggingMethod() {
              return isLoggingMethod;
            }

            public MetaFromMethod fromMethod() {
              return fromMethod;
            }

            public MetaGetDefaultConnectorConfigurationMethod getDefaultConnectorConfigurationMethod(
                ) {
              return getDefaultConnectorConfigurationMethod;
            }

            public MetaGetConnectorConfigurationsMethod getConnectorConfigurationsMethod() {
              return getConnectorConfigurationsMethod;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.configuration.RsocketCommunicatorConfiguration> {
              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.configuration.RsocketCommunicatorConfiguration.class));
              }

              @Override
              public io.art.rsocket.configuration.RsocketCommunicatorConfiguration invoke(
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return new io.art.rsocket.configuration.RsocketCommunicatorConfiguration();
              }

              @Override
              public io.art.rsocket.configuration.RsocketCommunicatorConfiguration invoke() throws
                  java.lang.Throwable {
                return new io.art.rsocket.configuration.RsocketCommunicatorConfiguration();
              }
            }

            public static final class MetaIsLoggingMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketCommunicatorConfiguration, java.lang.Boolean> {
              private final MetaParameter<java.lang.String> connectorIdParameter = register(new MetaParameter<>(0, "connectorId",metaType(java.lang.String.class)));

              private MetaIsLoggingMethod() {
                super("isLogging",metaType(boolean.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketCommunicatorConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.isLogging((java.lang.String)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketCommunicatorConfiguration instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.isLogging((java.lang.String)(argument));
              }

              public MetaParameter<java.lang.String> connectorIdParameter() {
                return connectorIdParameter;
              }
            }

            public static final class MetaFromMethod extends StaticMetaMethod<io.art.rsocket.configuration.RsocketCommunicatorConfiguration> {
              private final MetaParameter<io.art.rsocket.refresher.RsocketModuleRefresher> refresherParameter = register(new MetaParameter<>(0, "refresher",metaType(io.art.rsocket.refresher.RsocketModuleRefresher.class)));

              private final MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter = register(new MetaParameter<>(1, "source",metaType(io.art.core.source.ConfigurationSource.class)));

              private MetaFromMethod() {
                super("from",metaType(io.art.rsocket.configuration.RsocketCommunicatorConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                return io.art.rsocket.configuration.RsocketCommunicatorConfiguration.from((io.art.rsocket.refresher.RsocketModuleRefresher)(arguments[0]),(io.art.core.source.ConfigurationSource)(arguments[1]));
              }

              public MetaParameter<io.art.rsocket.refresher.RsocketModuleRefresher> refresherParameter(
                  ) {
                return refresherParameter;
              }

              public MetaParameter<io.art.core.source.ConfigurationSource> sourceParameter() {
                return sourceParameter;
              }
            }

            public static final class MetaGetDefaultConnectorConfigurationMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketCommunicatorConfiguration, io.art.rsocket.configuration.RsocketConnectorConfiguration> {
              private MetaGetDefaultConnectorConfigurationMethod() {
                super("getDefaultConnectorConfiguration",metaType(io.art.rsocket.configuration.RsocketConnectorConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketCommunicatorConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getDefaultConnectorConfiguration();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketCommunicatorConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getDefaultConnectorConfiguration();
              }
            }

            public static final class MetaGetConnectorConfigurationsMethod extends InstanceMetaMethod<io.art.rsocket.configuration.RsocketCommunicatorConfiguration, io.art.core.collection.ImmutableMap<java.lang.String, io.art.rsocket.configuration.RsocketConnectorConfiguration>> {
              private MetaGetConnectorConfigurationsMethod() {
                super("getConnectorConfigurations",metaType(io.art.core.collection.ImmutableMap.class,metaType(java.lang.String.class),metaType(io.art.rsocket.configuration.RsocketConnectorConfiguration.class)));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketCommunicatorConfiguration instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getConnectorConfigurations();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.configuration.RsocketCommunicatorConfiguration instance) throws
                  java.lang.Throwable {
                return instance.getConnectorConfigurations();
              }
            }
          }
        }

        public static final class MetaModulePackage extends MetaPackage {
          private final MetaRsocketModuleClass rsocketModuleClass = register(new MetaRsocketModuleClass());

          private final MetaRsocketActivatorClass rsocketActivatorClass = register(new MetaRsocketActivatorClass());

          private final MetaRsocketInitializerClass rsocketInitializerClass = register(new MetaRsocketInitializerClass());

          private MetaModulePackage() {
            super("module");
          }

          public MetaRsocketModuleClass rsocketModuleClass() {
            return rsocketModuleClass;
          }

          public MetaRsocketActivatorClass rsocketActivatorClass() {
            return rsocketActivatorClass;
          }

          public MetaRsocketInitializerClass rsocketInitializerClass() {
            return rsocketInitializerClass;
          }

          public static final class MetaRsocketModuleClass extends MetaClass<io.art.rsocket.module.RsocketModule> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaField<java.util.concurrent.atomic.AtomicReference<java.lang.Object>> rsocketModuleField = register(new MetaField<>("rsocketModule",metaType(java.util.concurrent.atomic.AtomicReference.class,metaType(java.lang.Object.class)),false));

            private final MetaField<java.util.concurrent.atomic.AtomicReference<java.lang.Object>> loggerField = register(new MetaField<>("logger",metaType(java.util.concurrent.atomic.AtomicReference.class,metaType(java.lang.Object.class)),false));

            private final MetaField<java.lang.String> idField = register(new MetaField<>("id",metaType(java.lang.String.class),false));

            private final MetaField<io.art.rsocket.state.RsocketModuleState> stateField = register(new MetaField<>("state",metaType(io.art.rsocket.state.RsocketModuleState.class),false));

            private final MetaField<io.art.rsocket.refresher.RsocketModuleRefresher> refresherField = register(new MetaField<>("refresher",metaType(io.art.rsocket.refresher.RsocketModuleRefresher.class),false));

            private final MetaField<io.art.rsocket.configuration.RsocketModuleConfiguration> configurationField = register(new MetaField<>("configuration",metaType(io.art.rsocket.configuration.RsocketModuleConfiguration.class),false));

            private final MetaField<io.art.rsocket.manager.RsocketManager> managerField = register(new MetaField<>("manager",metaType(io.art.rsocket.manager.RsocketManager.class),false));

            private final MetaField<io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator> configuratorField = register(new MetaField<>("configurator",metaType(io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator.class),false));

            private final MetaRsocketModuleMethod rsocketModuleMethod = register(new MetaRsocketModuleMethod());

            private final MetaOnLoadMethod onLoadMethod = register(new MetaOnLoadMethod());

            private final MetaOnUnloadMethod onUnloadMethod = register(new MetaOnUnloadMethod());

            private final MetaGetIdMethod getIdMethod = register(new MetaGetIdMethod());

            private final MetaGetStateMethod getStateMethod = register(new MetaGetStateMethod());

            private final MetaGetRefresherMethod getRefresherMethod = register(new MetaGetRefresherMethod());

            private final MetaGetConfigurationMethod getConfigurationMethod = register(new MetaGetConfigurationMethod());

            private final MetaGetManagerMethod getManagerMethod = register(new MetaGetManagerMethod());

            private final MetaGetConfiguratorMethod getConfiguratorMethod = register(new MetaGetConfiguratorMethod());

            private MetaRsocketModuleClass() {
              super(metaType(io.art.rsocket.module.RsocketModule.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<java.util.concurrent.atomic.AtomicReference<java.lang.Object>> rsocketModuleField(
                ) {
              return rsocketModuleField;
            }

            public MetaField<java.util.concurrent.atomic.AtomicReference<java.lang.Object>> loggerField(
                ) {
              return loggerField;
            }

            public MetaField<java.lang.String> idField() {
              return idField;
            }

            public MetaField<io.art.rsocket.state.RsocketModuleState> stateField() {
              return stateField;
            }

            public MetaField<io.art.rsocket.refresher.RsocketModuleRefresher> refresherField() {
              return refresherField;
            }

            public MetaField<io.art.rsocket.configuration.RsocketModuleConfiguration> configurationField(
                ) {
              return configurationField;
            }

            public MetaField<io.art.rsocket.manager.RsocketManager> managerField() {
              return managerField;
            }

            public MetaField<io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator> configuratorField(
                ) {
              return configuratorField;
            }

            public MetaRsocketModuleMethod rsocketModuleMethod() {
              return rsocketModuleMethod;
            }

            public MetaOnLoadMethod onLoadMethod() {
              return onLoadMethod;
            }

            public MetaOnUnloadMethod onUnloadMethod() {
              return onUnloadMethod;
            }

            public MetaGetIdMethod getIdMethod() {
              return getIdMethod;
            }

            public MetaGetStateMethod getStateMethod() {
              return getStateMethod;
            }

            public MetaGetRefresherMethod getRefresherMethod() {
              return getRefresherMethod;
            }

            public MetaGetConfigurationMethod getConfigurationMethod() {
              return getConfigurationMethod;
            }

            public MetaGetManagerMethod getManagerMethod() {
              return getManagerMethod;
            }

            public MetaGetConfiguratorMethod getConfiguratorMethod() {
              return getConfiguratorMethod;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.module.RsocketModule> {
              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.module.RsocketModule.class));
              }

              @Override
              public io.art.rsocket.module.RsocketModule invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                return new io.art.rsocket.module.RsocketModule();
              }

              @Override
              public io.art.rsocket.module.RsocketModule invoke() throws java.lang.Throwable {
                return new io.art.rsocket.module.RsocketModule();
              }
            }

            public static final class MetaRsocketModuleMethod extends StaticMetaMethod<io.art.core.module.StatefulModuleProxy<io.art.rsocket.configuration.RsocketModuleConfiguration, io.art.rsocket.state.RsocketModuleState>> {
              private MetaRsocketModuleMethod() {
                super("rsocketModule",metaType(io.art.core.module.StatefulModuleProxy.class,metaType(io.art.rsocket.configuration.RsocketModuleConfiguration.class),metaType(io.art.rsocket.state.RsocketModuleState.class)));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                return io.art.rsocket.module.RsocketModule.rsocketModule();
              }

              @Override
              public java.lang.Object invoke() throws java.lang.Throwable {
                return io.art.rsocket.module.RsocketModule.rsocketModule();
              }
            }

            public static final class MetaOnLoadMethod extends InstanceMetaMethod<io.art.rsocket.module.RsocketModule, java.lang.Void> {
              private final MetaParameter<io.art.core.context.Context.Service> contextServiceParameter = register(new MetaParameter<>(0, "contextService",metaType(io.art.core.context.Context.Service.class)));

              private MetaOnLoadMethod() {
                super("onLoad",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketModule instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.onLoad((io.art.core.context.Context.Service)(arguments[0]));
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketModule instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                instance.onLoad((io.art.core.context.Context.Service)(argument));
                return null;
              }

              public MetaParameter<io.art.core.context.Context.Service> contextServiceParameter() {
                return contextServiceParameter;
              }
            }

            public static final class MetaOnUnloadMethod extends InstanceMetaMethod<io.art.rsocket.module.RsocketModule, java.lang.Void> {
              private final MetaParameter<io.art.core.context.Context.Service> contextServiceParameter = register(new MetaParameter<>(0, "contextService",metaType(io.art.core.context.Context.Service.class)));

              private MetaOnUnloadMethod() {
                super("onUnload",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketModule instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.onUnload((io.art.core.context.Context.Service)(arguments[0]));
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketModule instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                instance.onUnload((io.art.core.context.Context.Service)(argument));
                return null;
              }

              public MetaParameter<io.art.core.context.Context.Service> contextServiceParameter() {
                return contextServiceParameter;
              }
            }

            public static final class MetaGetIdMethod extends InstanceMetaMethod<io.art.rsocket.module.RsocketModule, java.lang.String> {
              private MetaGetIdMethod() {
                super("getId",metaType(java.lang.String.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketModule instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getId();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketModule instance) throws
                  java.lang.Throwable {
                return instance.getId();
              }
            }

            public static final class MetaGetStateMethod extends InstanceMetaMethod<io.art.rsocket.module.RsocketModule, io.art.rsocket.state.RsocketModuleState> {
              private MetaGetStateMethod() {
                super("getState",metaType(io.art.rsocket.state.RsocketModuleState.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketModule instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getState();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketModule instance) throws
                  java.lang.Throwable {
                return instance.getState();
              }
            }

            public static final class MetaGetRefresherMethod extends InstanceMetaMethod<io.art.rsocket.module.RsocketModule, io.art.rsocket.refresher.RsocketModuleRefresher> {
              private MetaGetRefresherMethod() {
                super("getRefresher",metaType(io.art.rsocket.refresher.RsocketModuleRefresher.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketModule instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getRefresher();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketModule instance) throws
                  java.lang.Throwable {
                return instance.getRefresher();
              }
            }

            public static final class MetaGetConfigurationMethod extends InstanceMetaMethod<io.art.rsocket.module.RsocketModule, io.art.rsocket.configuration.RsocketModuleConfiguration> {
              private MetaGetConfigurationMethod() {
                super("getConfiguration",metaType(io.art.rsocket.configuration.RsocketModuleConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketModule instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getConfiguration();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketModule instance) throws
                  java.lang.Throwable {
                return instance.getConfiguration();
              }
            }

            public static final class MetaGetManagerMethod extends InstanceMetaMethod<io.art.rsocket.module.RsocketModule, io.art.rsocket.manager.RsocketManager> {
              private MetaGetManagerMethod() {
                super("getManager",metaType(io.art.rsocket.manager.RsocketManager.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketModule instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getManager();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketModule instance) throws
                  java.lang.Throwable {
                return instance.getManager();
              }
            }

            public static final class MetaGetConfiguratorMethod extends InstanceMetaMethod<io.art.rsocket.module.RsocketModule, io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator> {
              private MetaGetConfiguratorMethod() {
                super("getConfigurator",metaType(io.art.rsocket.configuration.RsocketModuleConfiguration.Configurator.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketModule instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getConfigurator();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketModule instance) throws
                  java.lang.Throwable {
                return instance.getConfigurator();
              }
            }
          }

          public static final class MetaRsocketActivatorClass extends MetaClass<io.art.rsocket.module.RsocketActivator> {
            private final MetaRsocketMethod rsocketMethod = register(new MetaRsocketMethod());

            private final MetaRsocket_1Method rsocket_1Method = register(new MetaRsocket_1Method());

            private MetaRsocketActivatorClass() {
              super(metaType(io.art.rsocket.module.RsocketActivator.class));
            }

            public MetaRsocketMethod rsocketMethod() {
              return rsocketMethod;
            }

            public MetaRsocket_1Method rsocket_1Method() {
              return rsocket_1Method;
            }

            public static final class MetaRsocketMethod extends StaticMetaMethod<io.art.core.module.ModuleActivator> {
              private MetaRsocketMethod() {
                super("rsocket",metaType(io.art.core.module.ModuleActivator.class));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                return io.art.rsocket.module.RsocketActivator.rsocket();
              }

              @Override
              public java.lang.Object invoke() throws java.lang.Throwable {
                return io.art.rsocket.module.RsocketActivator.rsocket();
              }
            }

            public static final class MetaRsocket_1Method extends StaticMetaMethod<io.art.core.module.ModuleActivator> {
              private final MetaParameter<java.util.function.UnaryOperator<io.art.rsocket.module.RsocketInitializer>> initializerParameter = register(new MetaParameter<>(0, "initializer",metaType(java.util.function.UnaryOperator.class,metaType(io.art.rsocket.module.RsocketInitializer.class))));

              private MetaRsocket_1Method() {
                super("rsocket",metaType(io.art.core.module.ModuleActivator.class));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                return io.art.rsocket.module.RsocketActivator.rsocket((java.util.function.UnaryOperator<io.art.rsocket.module.RsocketInitializer>)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object argument) throws java.lang.Throwable {
                return io.art.rsocket.module.RsocketActivator.rsocket((java.util.function.UnaryOperator)(argument));
              }

              public MetaParameter<java.util.function.UnaryOperator<io.art.rsocket.module.RsocketInitializer>> initializerParameter(
                  ) {
                return initializerParameter;
              }
            }
          }

          public static final class MetaRsocketInitializerClass extends MetaClass<io.art.rsocket.module.RsocketInitializer> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaField<java.lang.Boolean> activateServerField = register(new MetaField<>("activateServer",metaType(boolean.class),false));

            private final MetaField<java.lang.Boolean> activateCommunicatorField = register(new MetaField<>("activateCommunicator",metaType(boolean.class),false));

            private final MetaField<java.lang.Boolean> serverLoggingField = register(new MetaField<>("serverLogging",metaType(boolean.class),false));

            private final MetaActivateServerMethod activateServerMethod = register(new MetaActivateServerMethod());

            private final MetaServerLoggingMethod serverLoggingMethod = register(new MetaServerLoggingMethod());

            private final MetaActivateCommunicatorMethod activateCommunicatorMethod = register(new MetaActivateCommunicatorMethod());

            private final MetaInitializeMethod initializeMethod = register(new MetaInitializeMethod());

            private final MetaInitialClass initialClass = register(new MetaInitialClass());

            private MetaRsocketInitializerClass() {
              super(metaType(io.art.rsocket.module.RsocketInitializer.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<java.lang.Boolean> activateServerField() {
              return activateServerField;
            }

            public MetaField<java.lang.Boolean> activateCommunicatorField() {
              return activateCommunicatorField;
            }

            public MetaField<java.lang.Boolean> serverLoggingField() {
              return serverLoggingField;
            }

            public MetaActivateServerMethod activateServerMethod() {
              return activateServerMethod;
            }

            public MetaServerLoggingMethod serverLoggingMethod() {
              return serverLoggingMethod;
            }

            public MetaActivateCommunicatorMethod activateCommunicatorMethod() {
              return activateCommunicatorMethod;
            }

            public MetaInitializeMethod initializeMethod() {
              return initializeMethod;
            }

            public MetaInitialClass initialClass() {
              return initialClass;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.module.RsocketInitializer> {
              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.module.RsocketInitializer.class));
              }

              @Override
              public io.art.rsocket.module.RsocketInitializer invoke(java.lang.Object[] arguments)
                  throws java.lang.Throwable {
                return new io.art.rsocket.module.RsocketInitializer();
              }

              @Override
              public io.art.rsocket.module.RsocketInitializer invoke() throws java.lang.Throwable {
                return new io.art.rsocket.module.RsocketInitializer();
              }
            }

            public static final class MetaActivateServerMethod extends InstanceMetaMethod<io.art.rsocket.module.RsocketInitializer, io.art.rsocket.module.RsocketInitializer> {
              private MetaActivateServerMethod() {
                super("activateServer",metaType(io.art.rsocket.module.RsocketInitializer.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketInitializer instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.activateServer();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketInitializer instance)
                  throws java.lang.Throwable {
                return instance.activateServer();
              }
            }

            public static final class MetaServerLoggingMethod extends InstanceMetaMethod<io.art.rsocket.module.RsocketInitializer, io.art.rsocket.module.RsocketInitializer> {
              private final MetaParameter<java.lang.Boolean> loggingParameter = register(new MetaParameter<>(0, "logging",metaType(boolean.class)));

              private MetaServerLoggingMethod() {
                super("serverLogging",metaType(io.art.rsocket.module.RsocketInitializer.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketInitializer instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.serverLogging((boolean)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketInitializer instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.serverLogging((boolean)(argument));
              }

              public MetaParameter<java.lang.Boolean> loggingParameter() {
                return loggingParameter;
              }
            }

            public static final class MetaActivateCommunicatorMethod extends InstanceMetaMethod<io.art.rsocket.module.RsocketInitializer, io.art.rsocket.module.RsocketInitializer> {
              private MetaActivateCommunicatorMethod() {
                super("activateCommunicator",metaType(io.art.rsocket.module.RsocketInitializer.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketInitializer instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.activateCommunicator();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketInitializer instance)
                  throws java.lang.Throwable {
                return instance.activateCommunicator();
              }
            }

            public static final class MetaInitializeMethod extends InstanceMetaMethod<io.art.rsocket.module.RsocketInitializer, io.art.rsocket.configuration.RsocketModuleConfiguration> {
              private final MetaParameter<io.art.rsocket.module.RsocketModule> moduleParameter = register(new MetaParameter<>(0, "module",metaType(io.art.rsocket.module.RsocketModule.class)));

              private MetaInitializeMethod() {
                super("initialize",metaType(io.art.rsocket.configuration.RsocketModuleConfiguration.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketInitializer instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.initialize((io.art.rsocket.module.RsocketModule)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.module.RsocketInitializer instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.initialize((io.art.rsocket.module.RsocketModule)(argument));
              }

              public MetaParameter<io.art.rsocket.module.RsocketModule> moduleParameter() {
                return moduleParameter;
              }
            }

            public static final class MetaInitialClass extends MetaClass<io.art.rsocket.module.RsocketInitializer.Initial> {
              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

              private final MetaField<io.art.rsocket.refresher.RsocketModuleRefresher> refresherField = register(new MetaField<>("refresher",metaType(io.art.rsocket.refresher.RsocketModuleRefresher.class),true));

              private final MetaField<java.util.concurrent.atomic.AtomicReference<java.lang.Object>> consumerField = register(new MetaField<>("consumer",metaType(java.util.concurrent.atomic.AtomicReference.class,metaType(java.lang.Object.class)),true));

              private final MetaField<io.art.rsocket.configuration.RsocketServerConfiguration> serverConfigurationField = register(new MetaField<>("serverConfiguration",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.class),true));

              private final MetaField<io.art.rsocket.configuration.RsocketCommunicatorConfiguration> communicatorConfigurationField = register(new MetaField<>("communicatorConfiguration",metaType(io.art.rsocket.configuration.RsocketCommunicatorConfiguration.class),true));

              private final MetaField<java.lang.Boolean> activateServerField = register(new MetaField<>("activateServer",metaType(boolean.class),true));

              private final MetaField<java.lang.Boolean> activateCommunicatorField = register(new MetaField<>("activateCommunicator",metaType(boolean.class),true));

              private final MetaGetConsumerMethod getConsumerMethod = register(new MetaGetConsumerMethod());

              private final MetaGetServerConfigurationMethod getServerConfigurationMethod = register(new MetaGetServerConfigurationMethod());

              private final MetaGetCommunicatorConfigurationMethod getCommunicatorConfigurationMethod = register(new MetaGetCommunicatorConfigurationMethod());

              private final MetaIsActivateServerMethod isActivateServerMethod = register(new MetaIsActivateServerMethod());

              private final MetaIsActivateCommunicatorMethod isActivateCommunicatorMethod = register(new MetaIsActivateCommunicatorMethod());

              private MetaInitialClass() {
                super(metaType(io.art.rsocket.module.RsocketInitializer.Initial.class));
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaField<io.art.rsocket.refresher.RsocketModuleRefresher> refresherField() {
                return refresherField;
              }

              public MetaField<java.util.concurrent.atomic.AtomicReference<java.lang.Object>> consumerField(
                  ) {
                return consumerField;
              }

              public MetaField<io.art.rsocket.configuration.RsocketServerConfiguration> serverConfigurationField(
                  ) {
                return serverConfigurationField;
              }

              public MetaField<io.art.rsocket.configuration.RsocketCommunicatorConfiguration> communicatorConfigurationField(
                  ) {
                return communicatorConfigurationField;
              }

              public MetaField<java.lang.Boolean> activateServerField() {
                return activateServerField;
              }

              public MetaField<java.lang.Boolean> activateCommunicatorField() {
                return activateCommunicatorField;
              }

              public MetaGetConsumerMethod getConsumerMethod() {
                return getConsumerMethod;
              }

              public MetaGetServerConfigurationMethod getServerConfigurationMethod() {
                return getServerConfigurationMethod;
              }

              public MetaGetCommunicatorConfigurationMethod getCommunicatorConfigurationMethod() {
                return getCommunicatorConfigurationMethod;
              }

              public MetaIsActivateServerMethod isActivateServerMethod() {
                return isActivateServerMethod;
              }

              public MetaIsActivateCommunicatorMethod isActivateCommunicatorMethod() {
                return isActivateCommunicatorMethod;
              }

              public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.module.RsocketInitializer.Initial> {
                private final MetaParameter<io.art.rsocket.refresher.RsocketModuleRefresher> refresherParameter = register(new MetaParameter<>(0, "refresher",metaType(io.art.rsocket.refresher.RsocketModuleRefresher.class)));

                private MetaConstructorConstructor() {
                  super(metaType(io.art.rsocket.module.RsocketInitializer.Initial.class));
                }

                @Override
                public io.art.rsocket.module.RsocketInitializer.Initial invoke(
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return new io.art.rsocket.module.RsocketInitializer.Initial((io.art.rsocket.refresher.RsocketModuleRefresher)(arguments[0]));
                }

                @Override
                public io.art.rsocket.module.RsocketInitializer.Initial invoke(
                    java.lang.Object argument) throws java.lang.Throwable {
                  return new io.art.rsocket.module.RsocketInitializer.Initial((io.art.rsocket.refresher.RsocketModuleRefresher)(argument));
                }

                public MetaParameter<io.art.rsocket.refresher.RsocketModuleRefresher> refresherParameter(
                    ) {
                  return refresherParameter;
                }
              }

              public static final class MetaGetConsumerMethod extends InstanceMetaMethod<io.art.rsocket.module.RsocketInitializer.Initial, io.art.rsocket.refresher.RsocketModuleRefresher.Consumer> {
                private MetaGetConsumerMethod() {
                  super("getConsumer",metaType(io.art.rsocket.refresher.RsocketModuleRefresher.Consumer.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.module.RsocketInitializer.Initial instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.getConsumer();
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.module.RsocketInitializer.Initial instance) throws
                    java.lang.Throwable {
                  return instance.getConsumer();
                }
              }

              public static final class MetaGetServerConfigurationMethod extends InstanceMetaMethod<io.art.rsocket.module.RsocketInitializer.Initial, io.art.rsocket.configuration.RsocketServerConfiguration> {
                private MetaGetServerConfigurationMethod() {
                  super("getServerConfiguration",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.module.RsocketInitializer.Initial instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.getServerConfiguration();
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.module.RsocketInitializer.Initial instance) throws
                    java.lang.Throwable {
                  return instance.getServerConfiguration();
                }
              }

              public static final class MetaGetCommunicatorConfigurationMethod extends InstanceMetaMethod<io.art.rsocket.module.RsocketInitializer.Initial, io.art.rsocket.configuration.RsocketCommunicatorConfiguration> {
                private MetaGetCommunicatorConfigurationMethod() {
                  super("getCommunicatorConfiguration",metaType(io.art.rsocket.configuration.RsocketCommunicatorConfiguration.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.module.RsocketInitializer.Initial instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.getCommunicatorConfiguration();
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.module.RsocketInitializer.Initial instance) throws
                    java.lang.Throwable {
                  return instance.getCommunicatorConfiguration();
                }
              }

              public static final class MetaIsActivateServerMethod extends InstanceMetaMethod<io.art.rsocket.module.RsocketInitializer.Initial, java.lang.Boolean> {
                private MetaIsActivateServerMethod() {
                  super("isActivateServer",metaType(boolean.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.module.RsocketInitializer.Initial instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.isActivateServer();
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.module.RsocketInitializer.Initial instance) throws
                    java.lang.Throwable {
                  return instance.isActivateServer();
                }
              }

              public static final class MetaIsActivateCommunicatorMethod extends InstanceMetaMethod<io.art.rsocket.module.RsocketInitializer.Initial, java.lang.Boolean> {
                private MetaIsActivateCommunicatorMethod() {
                  super("isActivateCommunicator",metaType(boolean.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.module.RsocketInitializer.Initial instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.isActivateCommunicator();
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.module.RsocketInitializer.Initial instance) throws
                    java.lang.Throwable {
                  return instance.isActivateCommunicator();
                }
              }
            }
          }
        }

        public static final class MetaReaderPackage extends MetaPackage {
          private final MetaRsocketPayloadReaderClass rsocketPayloadReaderClass = register(new MetaRsocketPayloadReaderClass());

          private MetaReaderPackage() {
            super("reader");
          }

          public MetaRsocketPayloadReaderClass rsocketPayloadReaderClass() {
            return rsocketPayloadReaderClass;
          }

          public static final class MetaRsocketPayloadReaderClass extends MetaClass<io.art.rsocket.reader.RsocketPayloadReader> {
            private final MetaReadRsocketPayloadMethod readRsocketPayloadMethod = register(new MetaReadRsocketPayloadMethod());

            private MetaRsocketPayloadReaderClass() {
              super(metaType(io.art.rsocket.reader.RsocketPayloadReader.class));
            }

            public MetaReadRsocketPayloadMethod readRsocketPayloadMethod() {
              return readRsocketPayloadMethod;
            }

            public static final class MetaReadRsocketPayloadMethod extends StaticMetaMethod<io.art.transport.payload.TransportPayload> {
              private final MetaParameter<io.art.transport.payload.TransportPayloadReader> readerParameter = register(new MetaParameter<>(0, "reader",metaType(io.art.transport.payload.TransportPayloadReader.class)));

              private final MetaParameter<io.rsocket.Payload> payloadParameter = register(new MetaParameter<>(1, "payload",metaType(io.rsocket.Payload.class)));

              private final MetaParameter<io.art.meta.model.MetaType<?>> typeParameter = register(new MetaParameter<>(2, "type",metaType(io.art.meta.model.MetaType.class,metaType(java.lang.Object.class))));

              private MetaReadRsocketPayloadMethod() {
                super("readRsocketPayload",metaType(io.art.transport.payload.TransportPayload.class));
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                return io.art.rsocket.reader.RsocketPayloadReader.readRsocketPayload((io.art.transport.payload.TransportPayloadReader)(arguments[0]),(io.rsocket.Payload)(arguments[1]),(io.art.meta.model.MetaType<?>)(arguments[2]));
              }

              public MetaParameter<io.art.transport.payload.TransportPayloadReader> readerParameter(
                  ) {
                return readerParameter;
              }

              public MetaParameter<io.rsocket.Payload> payloadParameter() {
                return payloadParameter;
              }

              public MetaParameter<io.art.meta.model.MetaType<?>> typeParameter() {
                return typeParameter;
              }
            }
          }
        }

        public static final class MetaCommunicatorPackage extends MetaPackage {
          private final MetaRsocketCommunicatorActionClass rsocketCommunicatorActionClass = register(new MetaRsocketCommunicatorActionClass());

          private MetaCommunicatorPackage() {
            super("communicator");
          }

          public MetaRsocketCommunicatorActionClass rsocketCommunicatorActionClass() {
            return rsocketCommunicatorActionClass;
          }

          public static final class MetaRsocketCommunicatorActionClass extends MetaClass<io.art.rsocket.communicator.RsocketCommunicatorAction> {
            private final MetaField<io.art.core.model.CommunicatorActionIdentifier> communicatorActionIdField = register(new MetaField<>("communicatorActionId",metaType(io.art.core.model.CommunicatorActionIdentifier.class),false));

            private final MetaField<java.util.concurrent.atomic.AtomicReference<java.lang.Object>> loggerField = register(new MetaField<>("logger",metaType(java.util.concurrent.atomic.AtomicReference.class,metaType(java.lang.Object.class)),false));

            private final MetaDisposeMethod disposeMethod = register(new MetaDisposeMethod());

            private final MetaCommunicateMethod communicateMethod = register(new MetaCommunicateMethod());

            private final MetaToBuilderMethod toBuilderMethod = register(new MetaToBuilderMethod());

            private final MetaRsocketCommunicatorActionBuilderClass rsocketCommunicatorActionBuilderClass = register(new MetaRsocketCommunicatorActionBuilderClass());

            private MetaRsocketCommunicatorActionClass() {
              super(metaType(io.art.rsocket.communicator.RsocketCommunicatorAction.class));
            }

            public MetaField<io.art.core.model.CommunicatorActionIdentifier> communicatorActionIdField(
                ) {
              return communicatorActionIdField;
            }

            public MetaField<java.util.concurrent.atomic.AtomicReference<java.lang.Object>> loggerField(
                ) {
              return loggerField;
            }

            public MetaDisposeMethod disposeMethod() {
              return disposeMethod;
            }

            public MetaCommunicateMethod communicateMethod() {
              return communicateMethod;
            }

            public MetaToBuilderMethod toBuilderMethod() {
              return toBuilderMethod;
            }

            public MetaRsocketCommunicatorActionBuilderClass rsocketCommunicatorActionBuilderClass(
                ) {
              return rsocketCommunicatorActionBuilderClass;
            }

            public static final class MetaDisposeMethod extends InstanceMetaMethod<io.art.rsocket.communicator.RsocketCommunicatorAction, java.lang.Void> {
              private MetaDisposeMethod() {
                super("dispose",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.communicator.RsocketCommunicatorAction instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.dispose();
                return null;
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.communicator.RsocketCommunicatorAction instance) throws
                  java.lang.Throwable {
                instance.dispose();
                return null;
              }
            }

            public static final class MetaCommunicateMethod extends InstanceMetaMethod<io.art.rsocket.communicator.RsocketCommunicatorAction, reactor.core.publisher.Flux<java.lang.Object>> {
              private final MetaParameter<reactor.core.publisher.Flux<java.lang.Object>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.Object.class))));

              private MetaCommunicateMethod() {
                super("communicate",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.Object.class)));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.communicator.RsocketCommunicatorAction instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.communicate((reactor.core.publisher.Flux<java.lang.Object>)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.communicator.RsocketCommunicatorAction instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.communicate((reactor.core.publisher.Flux)(argument));
              }

              public MetaParameter<reactor.core.publisher.Flux<java.lang.Object>> inputParameter() {
                return inputParameter;
              }
            }

            public static final class MetaToBuilderMethod extends InstanceMetaMethod<io.art.rsocket.communicator.RsocketCommunicatorAction, io.art.rsocket.communicator.RsocketCommunicatorAction.RsocketCommunicatorActionBuilder> {
              private MetaToBuilderMethod() {
                super("toBuilder",metaType(io.art.rsocket.communicator.RsocketCommunicatorAction.RsocketCommunicatorActionBuilder.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.communicator.RsocketCommunicatorAction instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.toBuilder();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.communicator.RsocketCommunicatorAction instance) throws
                  java.lang.Throwable {
                return instance.toBuilder();
              }
            }

            public static final class MetaRsocketCommunicatorActionBuilderClass extends MetaClass<io.art.rsocket.communicator.RsocketCommunicatorAction.RsocketCommunicatorActionBuilder> {
              private final MetaField<io.art.core.model.CommunicatorActionIdentifier> communicatorActionIdField = register(new MetaField<>("communicatorActionId",metaType(io.art.core.model.CommunicatorActionIdentifier.class),false));

              private final MetaCommunicatorActionIdMethod communicatorActionIdMethod = register(new MetaCommunicatorActionIdMethod());

              private final MetaBuildMethod buildMethod = register(new MetaBuildMethod());

              private MetaRsocketCommunicatorActionBuilderClass() {
                super(metaType(io.art.rsocket.communicator.RsocketCommunicatorAction.RsocketCommunicatorActionBuilder.class));
              }

              public MetaField<io.art.core.model.CommunicatorActionIdentifier> communicatorActionIdField(
                  ) {
                return communicatorActionIdField;
              }

              public MetaCommunicatorActionIdMethod communicatorActionIdMethod() {
                return communicatorActionIdMethod;
              }

              public MetaBuildMethod buildMethod() {
                return buildMethod;
              }

              public static final class MetaCommunicatorActionIdMethod extends InstanceMetaMethod<io.art.rsocket.communicator.RsocketCommunicatorAction.RsocketCommunicatorActionBuilder, io.art.rsocket.communicator.RsocketCommunicatorAction.RsocketCommunicatorActionBuilder> {
                private final MetaParameter<io.art.core.model.CommunicatorActionIdentifier> communicatorActionIdParameter = register(new MetaParameter<>(0, "communicatorActionId",metaType(io.art.core.model.CommunicatorActionIdentifier.class)));

                private MetaCommunicatorActionIdMethod() {
                  super("communicatorActionId",metaType(io.art.rsocket.communicator.RsocketCommunicatorAction.RsocketCommunicatorActionBuilder.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.communicator.RsocketCommunicatorAction.RsocketCommunicatorActionBuilder instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.communicatorActionId((io.art.core.model.CommunicatorActionIdentifier)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.communicator.RsocketCommunicatorAction.RsocketCommunicatorActionBuilder instance,
                    java.lang.Object argument) throws java.lang.Throwable {
                  return instance.communicatorActionId((io.art.core.model.CommunicatorActionIdentifier)(argument));
                }

                public MetaParameter<io.art.core.model.CommunicatorActionIdentifier> communicatorActionIdParameter(
                    ) {
                  return communicatorActionIdParameter;
                }
              }

              public static final class MetaBuildMethod extends InstanceMetaMethod<io.art.rsocket.communicator.RsocketCommunicatorAction.RsocketCommunicatorActionBuilder, io.art.rsocket.communicator.RsocketCommunicatorAction> {
                private MetaBuildMethod() {
                  super("build",metaType(io.art.rsocket.communicator.RsocketCommunicatorAction.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.communicator.RsocketCommunicatorAction.RsocketCommunicatorActionBuilder instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.build();
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.communicator.RsocketCommunicatorAction.RsocketCommunicatorActionBuilder instance)
                    throws java.lang.Throwable {
                  return instance.build();
                }
              }
            }
          }
        }

        public static final class MetaStatePackage extends MetaPackage {
          private final MetaRsocketModuleStateClass rsocketModuleStateClass = register(new MetaRsocketModuleStateClass());

          private MetaStatePackage() {
            super("state");
          }

          public MetaRsocketModuleStateClass rsocketModuleStateClass() {
            return rsocketModuleStateClass;
          }

          public static final class MetaRsocketModuleStateClass extends MetaClass<io.art.rsocket.state.RsocketModuleState> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaField<java.util.List<io.rsocket.RSocket>> requestersField = register(new MetaField<>("requesters",metaType(java.util.List.class,metaType(io.rsocket.RSocket.class)),false));

            private final MetaField<java.lang.ThreadLocal<io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState>> threadLocalStateField = register(new MetaField<>("threadLocalState",metaType(java.lang.ThreadLocal.class,metaType(io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.class)),false));

            private final MetaRegisterRequesterMethod registerRequesterMethod = register(new MetaRegisterRequesterMethod());

            private final MetaRemoveRequesterMethod removeRequesterMethod = register(new MetaRemoveRequesterMethod());

            private final MetaGetRequestersMethod getRequestersMethod = register(new MetaGetRequestersMethod());

            private final MetaLocalStateMethod localStateMethod = register(new MetaLocalStateMethod());

            private final MetaLocalState_1Method localState_1Method = register(new MetaLocalState_1Method());

            private final MetaLocalState_2Method localState_2Method = register(new MetaLocalState_2Method());

            private final MetaRsocketThreadLocalStateClass rsocketThreadLocalStateClass = register(new MetaRsocketThreadLocalStateClass());

            private MetaRsocketModuleStateClass() {
              super(metaType(io.art.rsocket.state.RsocketModuleState.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<java.util.List<io.rsocket.RSocket>> requestersField() {
              return requestersField;
            }

            public MetaField<java.lang.ThreadLocal<io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState>> threadLocalStateField(
                ) {
              return threadLocalStateField;
            }

            public MetaRegisterRequesterMethod registerRequesterMethod() {
              return registerRequesterMethod;
            }

            public MetaRemoveRequesterMethod removeRequesterMethod() {
              return removeRequesterMethod;
            }

            public MetaGetRequestersMethod getRequestersMethod() {
              return getRequestersMethod;
            }

            public MetaLocalStateMethod localStateMethod() {
              return localStateMethod;
            }

            public MetaLocalState_1Method localState_1Method() {
              return localState_1Method;
            }

            public MetaLocalState_2Method localState_2Method() {
              return localState_2Method;
            }

            public MetaRsocketThreadLocalStateClass rsocketThreadLocalStateClass() {
              return rsocketThreadLocalStateClass;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.state.RsocketModuleState> {
              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.state.RsocketModuleState.class));
              }

              @Override
              public io.art.rsocket.state.RsocketModuleState invoke(java.lang.Object[] arguments)
                  throws java.lang.Throwable {
                return new io.art.rsocket.state.RsocketModuleState();
              }

              @Override
              public io.art.rsocket.state.RsocketModuleState invoke() throws java.lang.Throwable {
                return new io.art.rsocket.state.RsocketModuleState();
              }
            }

            public static final class MetaRegisterRequesterMethod extends InstanceMetaMethod<io.art.rsocket.state.RsocketModuleState, java.lang.Void> {
              private final MetaParameter<io.rsocket.RSocket> socketParameter = register(new MetaParameter<>(0, "socket",metaType(io.rsocket.RSocket.class)));

              private MetaRegisterRequesterMethod() {
                super("registerRequester",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.state.RsocketModuleState instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.registerRequester((io.rsocket.RSocket)(arguments[0]));
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.state.RsocketModuleState instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                instance.registerRequester((io.rsocket.RSocket)(argument));
                return null;
              }

              public MetaParameter<io.rsocket.RSocket> socketParameter() {
                return socketParameter;
              }
            }

            public static final class MetaRemoveRequesterMethod extends InstanceMetaMethod<io.art.rsocket.state.RsocketModuleState, java.lang.Void> {
              private final MetaParameter<io.rsocket.RSocket> socketParameter = register(new MetaParameter<>(0, "socket",metaType(io.rsocket.RSocket.class)));

              private MetaRemoveRequesterMethod() {
                super("removeRequester",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.state.RsocketModuleState instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.removeRequester((io.rsocket.RSocket)(arguments[0]));
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.state.RsocketModuleState instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                instance.removeRequester((io.rsocket.RSocket)(argument));
                return null;
              }

              public MetaParameter<io.rsocket.RSocket> socketParameter() {
                return socketParameter;
              }
            }

            public static final class MetaGetRequestersMethod extends InstanceMetaMethod<io.art.rsocket.state.RsocketModuleState, io.art.core.collection.ImmutableArray<io.rsocket.RSocket>> {
              private MetaGetRequestersMethod() {
                super("getRequesters",metaType(io.art.core.collection.ImmutableArray.class,metaType(io.rsocket.RSocket.class)));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.state.RsocketModuleState instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getRequesters();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.state.RsocketModuleState instance)
                  throws java.lang.Throwable {
                return instance.getRequesters();
              }
            }

            public static final class MetaLocalStateMethod extends InstanceMetaMethod<io.art.rsocket.state.RsocketModuleState, java.lang.Void> {
              private final MetaParameter<java.util.function.Function<io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState, io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState>> functorParameter = register(new MetaParameter<>(0, "functor",metaType(java.util.function.Function.class,metaType(io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.class),metaType(io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.class))));

              private MetaLocalStateMethod() {
                super("localState",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.state.RsocketModuleState instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.localState((java.util.function.Function<io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState, io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState>)(arguments[0]));
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.state.RsocketModuleState instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                instance.localState((java.util.function.Function)(argument));
                return null;
              }

              public MetaParameter<java.util.function.Function<io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState, io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState>> functorParameter(
                  ) {
                return functorParameter;
              }
            }

            public static final class MetaLocalState_1Method extends InstanceMetaMethod<io.art.rsocket.state.RsocketModuleState, java.lang.Void> {
              private final MetaParameter<io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState> stateParameter = register(new MetaParameter<>(0, "state",metaType(io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.class)));

              private MetaLocalState_1Method() {
                super("localState",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.state.RsocketModuleState instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.localState((io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState)(arguments[0]));
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.state.RsocketModuleState instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                instance.localState((io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState)(argument));
                return null;
              }

              public MetaParameter<io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState> stateParameter(
                  ) {
                return stateParameter;
              }
            }

            public static final class MetaLocalState_2Method extends InstanceMetaMethod<io.art.rsocket.state.RsocketModuleState, io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState> {
              private MetaLocalState_2Method() {
                super("localState",metaType(io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.state.RsocketModuleState instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.localState();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.state.RsocketModuleState instance)
                  throws java.lang.Throwable {
                return instance.localState();
              }
            }

            public static final class MetaRsocketThreadLocalStateClass extends MetaClass<io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState> {
              private final MetaField<io.rsocket.RSocket> requesterRsocketField = register(new MetaField<>("requesterRsocket",metaType(io.rsocket.RSocket.class),false));

              private final MetaField<io.art.rsocket.model.RsocketSetupPayload> setupPayloadField = register(new MetaField<>("setupPayload",metaType(io.art.rsocket.model.RsocketSetupPayload.class),false));

              private final MetaFromContextMethod fromContextMethod = register(new MetaFromContextMethod());

              private final MetaToBuilderMethod toBuilderMethod = register(new MetaToBuilderMethod());

              private final MetaGetRequesterRsocketMethod getRequesterRsocketMethod = register(new MetaGetRequesterRsocketMethod());

              private final MetaGetSetupPayloadMethod getSetupPayloadMethod = register(new MetaGetSetupPayloadMethod());

              private final MetaRsocketThreadLocalStateBuilderClass rsocketThreadLocalStateBuilderClass = register(new MetaRsocketThreadLocalStateBuilderClass());

              private MetaRsocketThreadLocalStateClass() {
                super(metaType(io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.class));
              }

              public MetaField<io.rsocket.RSocket> requesterRsocketField() {
                return requesterRsocketField;
              }

              public MetaField<io.art.rsocket.model.RsocketSetupPayload> setupPayloadField() {
                return setupPayloadField;
              }

              public MetaFromContextMethod fromContextMethod() {
                return fromContextMethod;
              }

              public MetaToBuilderMethod toBuilderMethod() {
                return toBuilderMethod;
              }

              public MetaGetRequesterRsocketMethod getRequesterRsocketMethod() {
                return getRequesterRsocketMethod;
              }

              public MetaGetSetupPayloadMethod getSetupPayloadMethod() {
                return getSetupPayloadMethod;
              }

              public MetaRsocketThreadLocalStateBuilderClass rsocketThreadLocalStateBuilderClass() {
                return rsocketThreadLocalStateBuilderClass;
              }

              public static final class MetaFromContextMethod extends StaticMetaMethod<io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState> {
                private final MetaParameter<reactor.util.context.ContextView> contextParameter = register(new MetaParameter<>(0, "context",metaType(reactor.util.context.ContextView.class)));

                private MetaFromContextMethod() {
                  super("fromContext",metaType(io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.class));
                }

                @Override
                public java.lang.Object invoke(java.lang.Object[] arguments) throws
                    java.lang.Throwable {
                  return io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.fromContext((reactor.util.context.ContextView)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(java.lang.Object argument) throws
                    java.lang.Throwable {
                  return io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.fromContext((reactor.util.context.ContextView)(argument));
                }

                public MetaParameter<reactor.util.context.ContextView> contextParameter() {
                  return contextParameter;
                }
              }

              public static final class MetaToBuilderMethod extends InstanceMetaMethod<io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState, io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.RsocketThreadLocalStateBuilder> {
                private MetaToBuilderMethod() {
                  super("toBuilder",metaType(io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.RsocketThreadLocalStateBuilder.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.toBuilder();
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState instance) throws
                    java.lang.Throwable {
                  return instance.toBuilder();
                }
              }

              public static final class MetaGetRequesterRsocketMethod extends InstanceMetaMethod<io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState, io.rsocket.RSocket> {
                private MetaGetRequesterRsocketMethod() {
                  super("getRequesterRsocket",metaType(io.rsocket.RSocket.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.getRequesterRsocket();
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState instance) throws
                    java.lang.Throwable {
                  return instance.getRequesterRsocket();
                }
              }

              public static final class MetaGetSetupPayloadMethod extends InstanceMetaMethod<io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState, io.art.rsocket.model.RsocketSetupPayload> {
                private MetaGetSetupPayloadMethod() {
                  super("getSetupPayload",metaType(io.art.rsocket.model.RsocketSetupPayload.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.getSetupPayload();
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState instance) throws
                    java.lang.Throwable {
                  return instance.getSetupPayload();
                }
              }

              public static final class MetaRsocketThreadLocalStateBuilderClass extends MetaClass<io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.RsocketThreadLocalStateBuilder> {
                private final MetaField<io.rsocket.RSocket> requesterRsocketField = register(new MetaField<>("requesterRsocket",metaType(io.rsocket.RSocket.class),false));

                private final MetaField<io.art.rsocket.model.RsocketSetupPayload> setupPayloadField = register(new MetaField<>("setupPayload",metaType(io.art.rsocket.model.RsocketSetupPayload.class),false));

                private final MetaRequesterRsocketMethod requesterRsocketMethod = register(new MetaRequesterRsocketMethod());

                private final MetaSetupPayloadMethod setupPayloadMethod = register(new MetaSetupPayloadMethod());

                private final MetaBuildMethod buildMethod = register(new MetaBuildMethod());

                private MetaRsocketThreadLocalStateBuilderClass() {
                  super(metaType(io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.RsocketThreadLocalStateBuilder.class));
                }

                public MetaField<io.rsocket.RSocket> requesterRsocketField() {
                  return requesterRsocketField;
                }

                public MetaField<io.art.rsocket.model.RsocketSetupPayload> setupPayloadField() {
                  return setupPayloadField;
                }

                public MetaRequesterRsocketMethod requesterRsocketMethod() {
                  return requesterRsocketMethod;
                }

                public MetaSetupPayloadMethod setupPayloadMethod() {
                  return setupPayloadMethod;
                }

                public MetaBuildMethod buildMethod() {
                  return buildMethod;
                }

                public static final class MetaRequesterRsocketMethod extends InstanceMetaMethod<io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.RsocketThreadLocalStateBuilder, io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.RsocketThreadLocalStateBuilder> {
                  private final MetaParameter<io.rsocket.RSocket> requesterRsocketParameter = register(new MetaParameter<>(0, "requesterRsocket",metaType(io.rsocket.RSocket.class)));

                  private MetaRequesterRsocketMethod() {
                    super("requesterRsocket",metaType(io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.RsocketThreadLocalStateBuilder.class));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.RsocketThreadLocalStateBuilder instance,
                      java.lang.Object[] arguments) throws java.lang.Throwable {
                    return instance.requesterRsocket((io.rsocket.RSocket)(arguments[0]));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.RsocketThreadLocalStateBuilder instance,
                      java.lang.Object argument) throws java.lang.Throwable {
                    return instance.requesterRsocket((io.rsocket.RSocket)(argument));
                  }

                  public MetaParameter<io.rsocket.RSocket> requesterRsocketParameter() {
                    return requesterRsocketParameter;
                  }
                }

                public static final class MetaSetupPayloadMethod extends InstanceMetaMethod<io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.RsocketThreadLocalStateBuilder, io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.RsocketThreadLocalStateBuilder> {
                  private final MetaParameter<io.art.rsocket.model.RsocketSetupPayload> setupPayloadParameter = register(new MetaParameter<>(0, "setupPayload",metaType(io.art.rsocket.model.RsocketSetupPayload.class)));

                  private MetaSetupPayloadMethod() {
                    super("setupPayload",metaType(io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.RsocketThreadLocalStateBuilder.class));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.RsocketThreadLocalStateBuilder instance,
                      java.lang.Object[] arguments) throws java.lang.Throwable {
                    return instance.setupPayload((io.art.rsocket.model.RsocketSetupPayload)(arguments[0]));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.RsocketThreadLocalStateBuilder instance,
                      java.lang.Object argument) throws java.lang.Throwable {
                    return instance.setupPayload((io.art.rsocket.model.RsocketSetupPayload)(argument));
                  }

                  public MetaParameter<io.art.rsocket.model.RsocketSetupPayload> setupPayloadParameter(
                      ) {
                    return setupPayloadParameter;
                  }
                }

                public static final class MetaBuildMethod extends InstanceMetaMethod<io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.RsocketThreadLocalStateBuilder, io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState> {
                  private MetaBuildMethod() {
                    super("build",metaType(io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.class));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.RsocketThreadLocalStateBuilder instance,
                      java.lang.Object[] arguments) throws java.lang.Throwable {
                    return instance.build();
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.RsocketThreadLocalStateBuilder instance)
                      throws java.lang.Throwable {
                    return instance.build();
                  }
                }
              }
            }
          }
        }

        public static final class MetaRefresherPackage extends MetaPackage {
          private final MetaRsocketModuleRefresherClass rsocketModuleRefresherClass = register(new MetaRsocketModuleRefresherClass());

          private MetaRefresherPackage() {
            super("refresher");
          }

          public MetaRsocketModuleRefresherClass rsocketModuleRefresherClass() {
            return rsocketModuleRefresherClass;
          }

          public static final class MetaRsocketModuleRefresherClass extends MetaClass<io.art.rsocket.refresher.RsocketModuleRefresher> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaField<io.art.core.changes.ChangesListener> serverListenerField = register(new MetaField<>("serverListener",metaType(io.art.core.changes.ChangesListener.class),false));

            private final MetaField<io.art.core.changes.ChangesListener> serverLoggingListenerField = register(new MetaField<>("serverLoggingListener",metaType(io.art.core.changes.ChangesListener.class),false));

            private final MetaField<io.art.core.changes.ChangesListenerRegistry> connectorListenersField = register(new MetaField<>("connectorListeners",metaType(io.art.core.changes.ChangesListenerRegistry.class),false));

            private final MetaField<io.art.core.changes.ChangesListenerRegistry> connectorLoggingListenersField = register(new MetaField<>("connectorLoggingListeners",metaType(io.art.core.changes.ChangesListenerRegistry.class),false));

            private final MetaField<io.art.rsocket.refresher.RsocketModuleRefresher.Consumer> consumerField = register(new MetaField<>("consumer",metaType(io.art.rsocket.refresher.RsocketModuleRefresher.Consumer.class),false));

            private final MetaProduceMethod produceMethod = register(new MetaProduceMethod());

            private final MetaServerListenerMethod serverListenerMethod = register(new MetaServerListenerMethod());

            private final MetaServerLoggingListenerMethod serverLoggingListenerMethod = register(new MetaServerLoggingListenerMethod());

            private final MetaConnectorListenersMethod connectorListenersMethod = register(new MetaConnectorListenersMethod());

            private final MetaConnectorLoggingListenersMethod connectorLoggingListenersMethod = register(new MetaConnectorLoggingListenersMethod());

            private final MetaConsumerMethod consumerMethod = register(new MetaConsumerMethod());

            private final MetaConsumerClass consumerClass = register(new MetaConsumerClass());

            private MetaRsocketModuleRefresherClass() {
              super(metaType(io.art.rsocket.refresher.RsocketModuleRefresher.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<io.art.core.changes.ChangesListener> serverListenerField() {
              return serverListenerField;
            }

            public MetaField<io.art.core.changes.ChangesListener> serverLoggingListenerField() {
              return serverLoggingListenerField;
            }

            public MetaField<io.art.core.changes.ChangesListenerRegistry> connectorListenersField(
                ) {
              return connectorListenersField;
            }

            public MetaField<io.art.core.changes.ChangesListenerRegistry> connectorLoggingListenersField(
                ) {
              return connectorLoggingListenersField;
            }

            public MetaField<io.art.rsocket.refresher.RsocketModuleRefresher.Consumer> consumerField(
                ) {
              return consumerField;
            }

            public MetaProduceMethod produceMethod() {
              return produceMethod;
            }

            public MetaServerListenerMethod serverListenerMethod() {
              return serverListenerMethod;
            }

            public MetaServerLoggingListenerMethod serverLoggingListenerMethod() {
              return serverLoggingListenerMethod;
            }

            public MetaConnectorListenersMethod connectorListenersMethod() {
              return connectorListenersMethod;
            }

            public MetaConnectorLoggingListenersMethod connectorLoggingListenersMethod() {
              return connectorLoggingListenersMethod;
            }

            public MetaConsumerMethod consumerMethod() {
              return consumerMethod;
            }

            public MetaConsumerClass consumerClass() {
              return consumerClass;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.refresher.RsocketModuleRefresher> {
              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.refresher.RsocketModuleRefresher.class));
              }

              @Override
              public io.art.rsocket.refresher.RsocketModuleRefresher invoke(
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return new io.art.rsocket.refresher.RsocketModuleRefresher();
              }

              @Override
              public io.art.rsocket.refresher.RsocketModuleRefresher invoke() throws
                  java.lang.Throwable {
                return new io.art.rsocket.refresher.RsocketModuleRefresher();
              }
            }

            public static final class MetaProduceMethod extends InstanceMetaMethod<io.art.rsocket.refresher.RsocketModuleRefresher, io.art.rsocket.refresher.RsocketModuleRefresher> {
              private MetaProduceMethod() {
                super("produce",metaType(io.art.rsocket.refresher.RsocketModuleRefresher.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.refresher.RsocketModuleRefresher instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.produce();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.refresher.RsocketModuleRefresher instance) throws
                  java.lang.Throwable {
                return instance.produce();
              }
            }

            public static final class MetaServerListenerMethod extends InstanceMetaMethod<io.art.rsocket.refresher.RsocketModuleRefresher, io.art.core.changes.ChangesListener> {
              private MetaServerListenerMethod() {
                super("serverListener",metaType(io.art.core.changes.ChangesListener.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.refresher.RsocketModuleRefresher instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.serverListener();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.refresher.RsocketModuleRefresher instance) throws
                  java.lang.Throwable {
                return instance.serverListener();
              }
            }

            public static final class MetaServerLoggingListenerMethod extends InstanceMetaMethod<io.art.rsocket.refresher.RsocketModuleRefresher, io.art.core.changes.ChangesListener> {
              private MetaServerLoggingListenerMethod() {
                super("serverLoggingListener",metaType(io.art.core.changes.ChangesListener.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.refresher.RsocketModuleRefresher instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.serverLoggingListener();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.refresher.RsocketModuleRefresher instance) throws
                  java.lang.Throwable {
                return instance.serverLoggingListener();
              }
            }

            public static final class MetaConnectorListenersMethod extends InstanceMetaMethod<io.art.rsocket.refresher.RsocketModuleRefresher, io.art.core.changes.ChangesListenerRegistry> {
              private MetaConnectorListenersMethod() {
                super("connectorListeners",metaType(io.art.core.changes.ChangesListenerRegistry.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.refresher.RsocketModuleRefresher instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.connectorListeners();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.refresher.RsocketModuleRefresher instance) throws
                  java.lang.Throwable {
                return instance.connectorListeners();
              }
            }

            public static final class MetaConnectorLoggingListenersMethod extends InstanceMetaMethod<io.art.rsocket.refresher.RsocketModuleRefresher, io.art.core.changes.ChangesListenerRegistry> {
              private MetaConnectorLoggingListenersMethod() {
                super("connectorLoggingListeners",metaType(io.art.core.changes.ChangesListenerRegistry.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.refresher.RsocketModuleRefresher instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.connectorLoggingListeners();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.refresher.RsocketModuleRefresher instance) throws
                  java.lang.Throwable {
                return instance.connectorLoggingListeners();
              }
            }

            public static final class MetaConsumerMethod extends InstanceMetaMethod<io.art.rsocket.refresher.RsocketModuleRefresher, io.art.rsocket.refresher.RsocketModuleRefresher.Consumer> {
              private MetaConsumerMethod() {
                super("consumer",metaType(io.art.rsocket.refresher.RsocketModuleRefresher.Consumer.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.refresher.RsocketModuleRefresher instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.consumer();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.refresher.RsocketModuleRefresher instance) throws
                  java.lang.Throwable {
                return instance.consumer();
              }
            }

            public static final class MetaConsumerClass extends MetaClass<io.art.rsocket.refresher.RsocketModuleRefresher.Consumer> {
              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

              private final MetaField<io.art.core.changes.ChangesConsumer> serverConsumerField = register(new MetaField<>("serverConsumer",metaType(io.art.core.changes.ChangesConsumer.class),false));

              private final MetaField<io.art.core.changes.ChangesConsumer> serverLoggingConsumerField = register(new MetaField<>("serverLoggingConsumer",metaType(io.art.core.changes.ChangesConsumer.class),false));

              private final MetaField<io.art.core.changes.ChangesConsumerRegistry> connectorConsumersField = register(new MetaField<>("connectorConsumers",metaType(io.art.core.changes.ChangesConsumerRegistry.class),false));

              private final MetaField<io.art.core.changes.ChangesConsumerRegistry> connectorLoggingConsumersField = register(new MetaField<>("connectorLoggingConsumers",metaType(io.art.core.changes.ChangesConsumerRegistry.class),false));

              private final MetaServerConsumerMethod serverConsumerMethod = register(new MetaServerConsumerMethod());

              private final MetaServerLoggingConsumerMethod serverLoggingConsumerMethod = register(new MetaServerLoggingConsumerMethod());

              private final MetaConnectorConsumersMethod connectorConsumersMethod = register(new MetaConnectorConsumersMethod());

              private final MetaConnectorLoggingConsumersMethod connectorLoggingConsumersMethod = register(new MetaConnectorLoggingConsumersMethod());

              private MetaConsumerClass() {
                super(metaType(io.art.rsocket.refresher.RsocketModuleRefresher.Consumer.class));
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaField<io.art.core.changes.ChangesConsumer> serverConsumerField() {
                return serverConsumerField;
              }

              public MetaField<io.art.core.changes.ChangesConsumer> serverLoggingConsumerField() {
                return serverLoggingConsumerField;
              }

              public MetaField<io.art.core.changes.ChangesConsumerRegistry> connectorConsumersField(
                  ) {
                return connectorConsumersField;
              }

              public MetaField<io.art.core.changes.ChangesConsumerRegistry> connectorLoggingConsumersField(
                  ) {
                return connectorLoggingConsumersField;
              }

              public MetaServerConsumerMethod serverConsumerMethod() {
                return serverConsumerMethod;
              }

              public MetaServerLoggingConsumerMethod serverLoggingConsumerMethod() {
                return serverLoggingConsumerMethod;
              }

              public MetaConnectorConsumersMethod connectorConsumersMethod() {
                return connectorConsumersMethod;
              }

              public MetaConnectorLoggingConsumersMethod connectorLoggingConsumersMethod() {
                return connectorLoggingConsumersMethod;
              }

              public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.refresher.RsocketModuleRefresher.Consumer> {
                private MetaConstructorConstructor() {
                  super(metaType(io.art.rsocket.refresher.RsocketModuleRefresher.Consumer.class));
                }

                @Override
                public io.art.rsocket.refresher.RsocketModuleRefresher.Consumer invoke(
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return new io.art.rsocket.refresher.RsocketModuleRefresher.Consumer();
                }

                @Override
                public io.art.rsocket.refresher.RsocketModuleRefresher.Consumer invoke() throws
                    java.lang.Throwable {
                  return new io.art.rsocket.refresher.RsocketModuleRefresher.Consumer();
                }
              }

              public static final class MetaServerConsumerMethod extends InstanceMetaMethod<io.art.rsocket.refresher.RsocketModuleRefresher.Consumer, io.art.core.changes.ChangesConsumer> {
                private MetaServerConsumerMethod() {
                  super("serverConsumer",metaType(io.art.core.changes.ChangesConsumer.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.refresher.RsocketModuleRefresher.Consumer instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.serverConsumer();
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.refresher.RsocketModuleRefresher.Consumer instance) throws
                    java.lang.Throwable {
                  return instance.serverConsumer();
                }
              }

              public static final class MetaServerLoggingConsumerMethod extends InstanceMetaMethod<io.art.rsocket.refresher.RsocketModuleRefresher.Consumer, io.art.core.changes.ChangesConsumer> {
                private MetaServerLoggingConsumerMethod() {
                  super("serverLoggingConsumer",metaType(io.art.core.changes.ChangesConsumer.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.refresher.RsocketModuleRefresher.Consumer instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.serverLoggingConsumer();
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.refresher.RsocketModuleRefresher.Consumer instance) throws
                    java.lang.Throwable {
                  return instance.serverLoggingConsumer();
                }
              }

              public static final class MetaConnectorConsumersMethod extends InstanceMetaMethod<io.art.rsocket.refresher.RsocketModuleRefresher.Consumer, io.art.core.changes.ChangesConsumerRegistry> {
                private MetaConnectorConsumersMethod() {
                  super("connectorConsumers",metaType(io.art.core.changes.ChangesConsumerRegistry.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.refresher.RsocketModuleRefresher.Consumer instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.connectorConsumers();
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.refresher.RsocketModuleRefresher.Consumer instance) throws
                    java.lang.Throwable {
                  return instance.connectorConsumers();
                }
              }

              public static final class MetaConnectorLoggingConsumersMethod extends InstanceMetaMethod<io.art.rsocket.refresher.RsocketModuleRefresher.Consumer, io.art.core.changes.ChangesConsumerRegistry> {
                private MetaConnectorLoggingConsumersMethod() {
                  super("connectorLoggingConsumers",metaType(io.art.core.changes.ChangesConsumerRegistry.class));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.refresher.RsocketModuleRefresher.Consumer instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.connectorLoggingConsumers();
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.refresher.RsocketModuleRefresher.Consumer instance) throws
                    java.lang.Throwable {
                  return instance.connectorLoggingConsumers();
                }
              }
            }
          }
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
              public java.lang.Object invoke(io.art.rsocket.model.RsocketSetupPayload instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.toBuilder();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.model.RsocketSetupPayload instance)
                  throws java.lang.Throwable {
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
                public java.lang.Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.dataFormat((io.art.transport.constants.TransportModuleConstants.DataFormat)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    java.lang.Object argument) throws java.lang.Throwable {
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
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.metadataFormat((io.art.transport.constants.TransportModuleConstants.DataFormat)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    java.lang.Object argument) throws java.lang.Throwable {
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
                public java.lang.Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.id((io.art.core.model.ServiceMethodIdentifier)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    java.lang.Object argument) throws java.lang.Throwable {
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
                public java.lang.Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance,
                    java.lang.Object[] arguments) throws java.lang.Throwable {
                  return instance.build();
                }

                @Override
                public java.lang.Object invoke(
                    io.art.rsocket.model.RsocketSetupPayload.RsocketSetupPayloadBuilder instance)
                    throws java.lang.Throwable {
                  return instance.build();
                }
              }
            }
          }
        }

        public static final class MetaServerPackage extends MetaPackage {
          private final MetaRsocketServerClass rsocketServerClass = register(new MetaRsocketServerClass());

          private MetaServerPackage() {
            super("server");
          }

          public MetaRsocketServerClass rsocketServerClass() {
            return rsocketServerClass;
          }

          public static final class MetaRsocketServerClass extends MetaClass<io.art.rsocket.server.RsocketServer> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaConstructor_1Constructor constructor_1 = register(new MetaConstructor_1Constructor());

            private final MetaField<java.util.concurrent.atomic.AtomicReference<java.lang.Object>> loggerField = register(new MetaField<>("logger",metaType(java.util.concurrent.atomic.AtomicReference.class,metaType(java.lang.Object.class)),false));

            private final MetaField<io.art.rsocket.configuration.RsocketModuleConfiguration> configurationField = register(new MetaField<>("configuration",metaType(io.art.rsocket.configuration.RsocketModuleConfiguration.class),false));

            private final MetaField<io.art.core.property.Property<io.rsocket.transport.netty.server.CloseableChannel>> channelField = register(new MetaField<>("channel",metaType(io.art.core.property.Property.class,metaType(io.rsocket.transport.netty.server.CloseableChannel.class)),false));

            private final MetaField<reactor.core.publisher.Mono<java.lang.Void>> closerField = register(new MetaField<>("closer",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.Void.class)),false));

            private final MetaInitializeMethod initializeMethod = register(new MetaInitializeMethod());

            private final MetaDisposeMethod disposeMethod = register(new MetaDisposeMethod());

            private final MetaAvailableMethod availableMethod = register(new MetaAvailableMethod());

            private MetaRsocketServerClass() {
              super(metaType(io.art.rsocket.server.RsocketServer.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaConstructor_1Constructor constructor_1() {
              return constructor_1;
            }

            public MetaField<java.util.concurrent.atomic.AtomicReference<java.lang.Object>> loggerField(
                ) {
              return loggerField;
            }

            public MetaField<io.art.rsocket.configuration.RsocketModuleConfiguration> configurationField(
                ) {
              return configurationField;
            }

            public MetaField<io.art.core.property.Property<io.rsocket.transport.netty.server.CloseableChannel>> channelField(
                ) {
              return channelField;
            }

            public MetaField<reactor.core.publisher.Mono<java.lang.Void>> closerField() {
              return closerField;
            }

            public MetaInitializeMethod initializeMethod() {
              return initializeMethod;
            }

            public MetaDisposeMethod disposeMethod() {
              return disposeMethod;
            }

            public MetaAvailableMethod availableMethod() {
              return availableMethod;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.server.RsocketServer> {
              private final MetaParameter<io.art.rsocket.refresher.RsocketModuleRefresher> refresherParameter = register(new MetaParameter<>(0, "refresher",metaType(io.art.rsocket.refresher.RsocketModuleRefresher.class)));

              private final MetaParameter<io.art.rsocket.configuration.RsocketModuleConfiguration> configurationParameter = register(new MetaParameter<>(1, "configuration",metaType(io.art.rsocket.configuration.RsocketModuleConfiguration.class)));

              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.server.RsocketServer.class));
              }

              @Override
              public io.art.rsocket.server.RsocketServer invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                return new io.art.rsocket.server.RsocketServer((io.art.rsocket.refresher.RsocketModuleRefresher)(arguments[0]),(io.art.rsocket.configuration.RsocketModuleConfiguration)(arguments[1]));
              }

              public MetaParameter<io.art.rsocket.refresher.RsocketModuleRefresher> refresherParameter(
                  ) {
                return refresherParameter;
              }

              public MetaParameter<io.art.rsocket.configuration.RsocketModuleConfiguration> configurationParameter(
                  ) {
                return configurationParameter;
              }
            }

            public static final class MetaConstructor_1Constructor extends MetaConstructor<io.art.rsocket.server.RsocketServer> {
              private final MetaParameter<io.art.rsocket.configuration.RsocketModuleConfiguration> configurationParameter = register(new MetaParameter<>(0, "configuration",metaType(io.art.rsocket.configuration.RsocketModuleConfiguration.class)));

              private final MetaParameter<io.art.core.property.Property<io.rsocket.transport.netty.server.CloseableChannel>> channelParameter = register(new MetaParameter<>(1, "channel",metaType(io.art.core.property.Property.class,metaType(io.rsocket.transport.netty.server.CloseableChannel.class))));

              private MetaConstructor_1Constructor() {
                super(metaType(io.art.rsocket.server.RsocketServer.class));
              }

              @Override
              public io.art.rsocket.server.RsocketServer invoke(java.lang.Object[] arguments) throws
                  java.lang.Throwable {
                return new io.art.rsocket.server.RsocketServer((io.art.rsocket.configuration.RsocketModuleConfiguration)(arguments[0]),(io.art.core.property.Property<io.rsocket.transport.netty.server.CloseableChannel>)(arguments[1]));
              }

              public MetaParameter<io.art.rsocket.configuration.RsocketModuleConfiguration> configurationParameter(
                  ) {
                return configurationParameter;
              }

              public MetaParameter<io.art.core.property.Property<io.rsocket.transport.netty.server.CloseableChannel>> channelParameter(
                  ) {
                return channelParameter;
              }
            }

            public static final class MetaInitializeMethod extends InstanceMetaMethod<io.art.rsocket.server.RsocketServer, java.lang.Void> {
              private MetaInitializeMethod() {
                super("initialize",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.server.RsocketServer instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.initialize();
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.server.RsocketServer instance) throws
                  java.lang.Throwable {
                instance.initialize();
                return null;
              }
            }

            public static final class MetaDisposeMethod extends InstanceMetaMethod<io.art.rsocket.server.RsocketServer, java.lang.Void> {
              private MetaDisposeMethod() {
                super("dispose",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.server.RsocketServer instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.dispose();
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.server.RsocketServer instance) throws
                  java.lang.Throwable {
                instance.dispose();
                return null;
              }
            }

            public static final class MetaAvailableMethod extends InstanceMetaMethod<io.art.rsocket.server.RsocketServer, java.lang.Boolean> {
              private MetaAvailableMethod() {
                super("available",metaType(boolean.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.server.RsocketServer instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.available();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.server.RsocketServer instance) throws
                  java.lang.Throwable {
                return instance.available();
              }
            }
          }
        }

        public static final class MetaExceptionPackage extends MetaPackage {
          private final MetaRsocketExceptionClass rsocketExceptionClass = register(new MetaRsocketExceptionClass());

          private MetaExceptionPackage() {
            super("exception");
          }

          public MetaRsocketExceptionClass rsocketExceptionClass() {
            return rsocketExceptionClass;
          }

          public static final class MetaRsocketExceptionClass extends MetaClass<io.art.rsocket.exception.RsocketException> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaField<Long> serialVersionUIDField = register(new MetaField<>("serialVersionUID",metaType(long.class),true));

            private final MetaField<java.lang.Object> backtraceField = register(new MetaField<>("backtrace",metaType(java.lang.Object.class),true));

            private final MetaField<java.lang.String> detailMessageField = register(new MetaField<>("detailMessage",metaType(java.lang.String.class),true));

            private final MetaField<java.lang.StackTraceElement[]> UNASSIGNED_STACKField = register(new MetaField<>("UNASSIGNED_STACK",metaArray(java.lang.StackTraceElement[].class, java.lang.StackTraceElement[]::new, metaType(java.lang.StackTraceElement.class)),true));

            private final MetaField<java.lang.Throwable> causeField = register(new MetaField<>("cause",metaType(java.lang.Throwable.class),true));

            private final MetaField<java.lang.StackTraceElement[]> stackTraceField = register(new MetaField<>("stackTrace",metaArray(java.lang.StackTraceElement[].class, java.lang.StackTraceElement[]::new, metaType(java.lang.StackTraceElement.class)),true));

            private final MetaField<Integer> depthField = register(new MetaField<>("depth",metaType(int.class),true));

            private final MetaField<java.util.List<java.lang.Throwable>> SUPPRESSED_SENTINELField = register(new MetaField<>("SUPPRESSED_SENTINEL",metaType(java.util.List.class,metaType(java.lang.Throwable.class)),true));

            private final MetaField<java.util.List<java.lang.Throwable>> suppressedExceptionsField = register(new MetaField<>("suppressedExceptions",metaType(java.util.List.class,metaType(java.lang.Throwable.class)),true));

            private final MetaField<java.lang.String> NULL_CAUSE_MESSAGEField = register(new MetaField<>("NULL_CAUSE_MESSAGE",metaType(java.lang.String.class),true));

            private final MetaField<java.lang.String> SELF_SUPPRESSION_MESSAGEField = register(new MetaField<>("SELF_SUPPRESSION_MESSAGE",metaType(java.lang.String.class),true));

            private final MetaField<java.lang.String> CAUSE_CAPTIONField = register(new MetaField<>("CAUSE_CAPTION",metaType(java.lang.String.class),true));

            private final MetaField<java.lang.String> SUPPRESSED_CAPTIONField = register(new MetaField<>("SUPPRESSED_CAPTION",metaType(java.lang.String.class),true));

            private final MetaField<java.lang.Throwable[]> EMPTY_THROWABLE_ARRAYField = register(new MetaField<>("EMPTY_THROWABLE_ARRAY",metaArray(java.lang.Throwable[].class, java.lang.Throwable[]::new, metaType(java.lang.Throwable.class)),true));

            private final MetaGetMessageMethod getMessageMethod = register(new MetaGetMessageMethod());

            private final MetaGetLocalizedMessageMethod getLocalizedMessageMethod = register(new MetaGetLocalizedMessageMethod());

            private final MetaGetCauseMethod getCauseMethod = register(new MetaGetCauseMethod());

            private final MetaInitCauseMethod initCauseMethod = register(new MetaInitCauseMethod());

            private final MetaPrintStackTraceMethod printStackTraceMethod = register(new MetaPrintStackTraceMethod());

            private final MetaPrintStackTrace_1Method printStackTrace_1Method = register(new MetaPrintStackTrace_1Method());

            private final MetaPrintStackTrace_2Method printStackTrace_2Method = register(new MetaPrintStackTrace_2Method());

            private final MetaFillInStackTraceMethod fillInStackTraceMethod = register(new MetaFillInStackTraceMethod());

            private final MetaGetStackTraceMethod getStackTraceMethod = register(new MetaGetStackTraceMethod());

            private final MetaSetStackTraceMethod setStackTraceMethod = register(new MetaSetStackTraceMethod());

            private final MetaAddSuppressedMethod addSuppressedMethod = register(new MetaAddSuppressedMethod());

            private final MetaGetSuppressedMethod getSuppressedMethod = register(new MetaGetSuppressedMethod());

            private MetaRsocketExceptionClass() {
              super(metaType(io.art.rsocket.exception.RsocketException.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<Long> serialVersionUIDField() {
              return serialVersionUIDField;
            }

            public MetaField<java.lang.Object> backtraceField() {
              return backtraceField;
            }

            public MetaField<java.lang.String> detailMessageField() {
              return detailMessageField;
            }

            public MetaField<java.lang.StackTraceElement[]> UNASSIGNED_STACKField() {
              return UNASSIGNED_STACKField;
            }

            public MetaField<java.lang.Throwable> causeField() {
              return causeField;
            }

            public MetaField<java.lang.StackTraceElement[]> stackTraceField() {
              return stackTraceField;
            }

            public MetaField<Integer> depthField() {
              return depthField;
            }

            public MetaField<java.util.List<java.lang.Throwable>> SUPPRESSED_SENTINELField() {
              return SUPPRESSED_SENTINELField;
            }

            public MetaField<java.util.List<java.lang.Throwable>> suppressedExceptionsField() {
              return suppressedExceptionsField;
            }

            public MetaField<java.lang.String> NULL_CAUSE_MESSAGEField() {
              return NULL_CAUSE_MESSAGEField;
            }

            public MetaField<java.lang.String> SELF_SUPPRESSION_MESSAGEField() {
              return SELF_SUPPRESSION_MESSAGEField;
            }

            public MetaField<java.lang.String> CAUSE_CAPTIONField() {
              return CAUSE_CAPTIONField;
            }

            public MetaField<java.lang.String> SUPPRESSED_CAPTIONField() {
              return SUPPRESSED_CAPTIONField;
            }

            public MetaField<java.lang.Throwable[]> EMPTY_THROWABLE_ARRAYField() {
              return EMPTY_THROWABLE_ARRAYField;
            }

            public MetaGetMessageMethod getMessageMethod() {
              return getMessageMethod;
            }

            public MetaGetLocalizedMessageMethod getLocalizedMessageMethod() {
              return getLocalizedMessageMethod;
            }

            public MetaGetCauseMethod getCauseMethod() {
              return getCauseMethod;
            }

            public MetaInitCauseMethod initCauseMethod() {
              return initCauseMethod;
            }

            public MetaPrintStackTraceMethod printStackTraceMethod() {
              return printStackTraceMethod;
            }

            public MetaPrintStackTrace_1Method printStackTrace_1Method() {
              return printStackTrace_1Method;
            }

            public MetaPrintStackTrace_2Method printStackTrace_2Method() {
              return printStackTrace_2Method;
            }

            public MetaFillInStackTraceMethod fillInStackTraceMethod() {
              return fillInStackTraceMethod;
            }

            public MetaGetStackTraceMethod getStackTraceMethod() {
              return getStackTraceMethod;
            }

            public MetaSetStackTraceMethod setStackTraceMethod() {
              return setStackTraceMethod;
            }

            public MetaAddSuppressedMethod addSuppressedMethod() {
              return addSuppressedMethod;
            }

            public MetaGetSuppressedMethod getSuppressedMethod() {
              return getSuppressedMethod;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.exception.RsocketException> {
              private final MetaParameter<java.lang.String> messageParameter = register(new MetaParameter<>(0, "message",metaType(java.lang.String.class)));

              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.exception.RsocketException.class));
              }

              @Override
              public io.art.rsocket.exception.RsocketException invoke(java.lang.Object[] arguments)
                  throws java.lang.Throwable {
                return new io.art.rsocket.exception.RsocketException((java.lang.String)(arguments[0]));
              }

              @Override
              public io.art.rsocket.exception.RsocketException invoke(java.lang.Object argument)
                  throws java.lang.Throwable {
                return new io.art.rsocket.exception.RsocketException((java.lang.String)(argument));
              }

              public MetaParameter<java.lang.String> messageParameter() {
                return messageParameter;
              }
            }

            public static final class MetaGetMessageMethod extends InstanceMetaMethod<io.art.rsocket.exception.RsocketException, java.lang.String> {
              private MetaGetMessageMethod() {
                super("getMessage",metaType(java.lang.String.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getMessage();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance)
                  throws java.lang.Throwable {
                return instance.getMessage();
              }
            }

            public static final class MetaGetLocalizedMessageMethod extends InstanceMetaMethod<io.art.rsocket.exception.RsocketException, java.lang.String> {
              private MetaGetLocalizedMessageMethod() {
                super("getLocalizedMessage",metaType(java.lang.String.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getLocalizedMessage();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance)
                  throws java.lang.Throwable {
                return instance.getLocalizedMessage();
              }
            }

            public static final class MetaGetCauseMethod extends InstanceMetaMethod<io.art.rsocket.exception.RsocketException, java.lang.Throwable> {
              private MetaGetCauseMethod() {
                super("getCause",metaType(java.lang.Throwable.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getCause();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance)
                  throws java.lang.Throwable {
                return instance.getCause();
              }
            }

            public static final class MetaInitCauseMethod extends InstanceMetaMethod<io.art.rsocket.exception.RsocketException, java.lang.Throwable> {
              private final MetaParameter<java.lang.Throwable> causeParameter = register(new MetaParameter<>(0, "cause",metaType(java.lang.Throwable.class)));

              private MetaInitCauseMethod() {
                super("initCause",metaType(java.lang.Throwable.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.initCause((java.lang.Throwable)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.initCause((java.lang.Throwable)(argument));
              }

              public MetaParameter<java.lang.Throwable> causeParameter() {
                return causeParameter;
              }
            }

            public static final class MetaPrintStackTraceMethod extends InstanceMetaMethod<io.art.rsocket.exception.RsocketException, java.lang.Void> {
              private MetaPrintStackTraceMethod() {
                super("printStackTrace",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.printStackTrace();
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance)
                  throws java.lang.Throwable {
                instance.printStackTrace();
                return null;
              }
            }

            public static final class MetaPrintStackTrace_1Method extends InstanceMetaMethod<io.art.rsocket.exception.RsocketException, java.lang.Void> {
              private final MetaParameter<java.io.PrintStream> sParameter = register(new MetaParameter<>(0, "s",metaType(java.io.PrintStream.class)));

              private MetaPrintStackTrace_1Method() {
                super("printStackTrace",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.printStackTrace((java.io.PrintStream)(arguments[0]));
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                instance.printStackTrace((java.io.PrintStream)(argument));
                return null;
              }

              public MetaParameter<java.io.PrintStream> sParameter() {
                return sParameter;
              }
            }

            public static final class MetaPrintStackTrace_2Method extends InstanceMetaMethod<io.art.rsocket.exception.RsocketException, java.lang.Void> {
              private final MetaParameter<java.io.PrintWriter> sParameter = register(new MetaParameter<>(0, "s",metaType(java.io.PrintWriter.class)));

              private MetaPrintStackTrace_2Method() {
                super("printStackTrace",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.printStackTrace((java.io.PrintWriter)(arguments[0]));
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                instance.printStackTrace((java.io.PrintWriter)(argument));
                return null;
              }

              public MetaParameter<java.io.PrintWriter> sParameter() {
                return sParameter;
              }
            }

            public static final class MetaFillInStackTraceMethod extends InstanceMetaMethod<io.art.rsocket.exception.RsocketException, java.lang.Throwable> {
              private MetaFillInStackTraceMethod() {
                super("fillInStackTrace",metaType(java.lang.Throwable.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.fillInStackTrace();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance)
                  throws java.lang.Throwable {
                return instance.fillInStackTrace();
              }
            }

            public static final class MetaGetStackTraceMethod extends InstanceMetaMethod<io.art.rsocket.exception.RsocketException, java.lang.StackTraceElement[]> {
              private MetaGetStackTraceMethod() {
                super("getStackTrace",metaArray(java.lang.StackTraceElement[].class, java.lang.StackTraceElement[]::new, metaType(java.lang.StackTraceElement.class)));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getStackTrace();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance)
                  throws java.lang.Throwable {
                return instance.getStackTrace();
              }
            }

            public static final class MetaSetStackTraceMethod extends InstanceMetaMethod<io.art.rsocket.exception.RsocketException, java.lang.Void> {
              private final MetaParameter<java.lang.StackTraceElement[]> stackTraceParameter = register(new MetaParameter<>(0, "stackTrace",metaArray(java.lang.StackTraceElement[].class, java.lang.StackTraceElement[]::new, metaType(java.lang.StackTraceElement.class))));

              private MetaSetStackTraceMethod() {
                super("setStackTrace",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.setStackTrace((java.lang.StackTraceElement[])(arguments[0]));
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                instance.setStackTrace((java.lang.StackTraceElement[])(argument));
                return null;
              }

              public MetaParameter<java.lang.StackTraceElement[]> stackTraceParameter() {
                return stackTraceParameter;
              }
            }

            public static final class MetaAddSuppressedMethod extends InstanceMetaMethod<io.art.rsocket.exception.RsocketException, java.lang.Void> {
              private final MetaParameter<java.lang.Throwable> exceptionParameter = register(new MetaParameter<>(0, "exception",metaType(java.lang.Throwable.class)));

              private MetaAddSuppressedMethod() {
                super("addSuppressed",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.addSuppressed((java.lang.Throwable)(arguments[0]));
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                instance.addSuppressed((java.lang.Throwable)(argument));
                return null;
              }

              public MetaParameter<java.lang.Throwable> exceptionParameter() {
                return exceptionParameter;
              }
            }

            public static final class MetaGetSuppressedMethod extends InstanceMetaMethod<io.art.rsocket.exception.RsocketException, java.lang.Throwable[]> {
              private MetaGetSuppressedMethod() {
                super("getSuppressed",metaArray(java.lang.Throwable[].class, java.lang.Throwable[]::new, metaType(java.lang.Throwable.class)));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.getSuppressed();
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.exception.RsocketException instance)
                  throws java.lang.Throwable {
                return instance.getSuppressed();
              }
            }
          }
        }

        public static final class MetaInterceptorPackage extends MetaPackage {
          private final MetaRsocketConnectorLoggingInterceptorClass rsocketConnectorLoggingInterceptorClass = register(new MetaRsocketConnectorLoggingInterceptorClass());

          private final MetaRsocketLoggingProxyClass rsocketLoggingProxyClass = register(new MetaRsocketLoggingProxyClass());

          private final MetaRsocketServerLoggingInterceptorClass rsocketServerLoggingInterceptorClass = register(new MetaRsocketServerLoggingInterceptorClass());

          private MetaInterceptorPackage() {
            super("interceptor");
          }

          public MetaRsocketConnectorLoggingInterceptorClass rsocketConnectorLoggingInterceptorClass(
              ) {
            return rsocketConnectorLoggingInterceptorClass;
          }

          public MetaRsocketLoggingProxyClass rsocketLoggingProxyClass() {
            return rsocketLoggingProxyClass;
          }

          public MetaRsocketServerLoggingInterceptorClass rsocketServerLoggingInterceptorClass() {
            return rsocketServerLoggingInterceptorClass;
          }

          public static final class MetaRsocketConnectorLoggingInterceptorClass extends MetaClass<io.art.rsocket.interceptor.RsocketConnectorLoggingInterceptor> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaField<java.util.concurrent.atomic.AtomicReference<java.lang.Object>> loggerField = register(new MetaField<>("logger",metaType(java.util.concurrent.atomic.AtomicReference.class,metaType(java.lang.Object.class)),false));

            private final MetaField<java.lang.String> connectorIdField = register(new MetaField<>("connectorId",metaType(java.lang.String.class),false));

            private final MetaField<io.art.core.property.Property<java.lang.Boolean>> enabledField = register(new MetaField<>("enabled",metaType(io.art.core.property.Property.class,metaType(java.lang.Boolean.class)),false));

            private final MetaApplyMethod applyMethod = register(new MetaApplyMethod());

            private MetaRsocketConnectorLoggingInterceptorClass() {
              super(metaType(io.art.rsocket.interceptor.RsocketConnectorLoggingInterceptor.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<java.util.concurrent.atomic.AtomicReference<java.lang.Object>> loggerField(
                ) {
              return loggerField;
            }

            public MetaField<java.lang.String> connectorIdField() {
              return connectorIdField;
            }

            public MetaField<io.art.core.property.Property<java.lang.Boolean>> enabledField() {
              return enabledField;
            }

            public MetaApplyMethod applyMethod() {
              return applyMethod;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.interceptor.RsocketConnectorLoggingInterceptor> {
              private final MetaParameter<java.lang.String> connectorIdParameter = register(new MetaParameter<>(0, "connectorId",metaType(java.lang.String.class)));

              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.interceptor.RsocketConnectorLoggingInterceptor.class));
              }

              @Override
              public io.art.rsocket.interceptor.RsocketConnectorLoggingInterceptor invoke(
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return new io.art.rsocket.interceptor.RsocketConnectorLoggingInterceptor((java.lang.String)(arguments[0]));
              }

              @Override
              public io.art.rsocket.interceptor.RsocketConnectorLoggingInterceptor invoke(
                  java.lang.Object argument) throws java.lang.Throwable {
                return new io.art.rsocket.interceptor.RsocketConnectorLoggingInterceptor((java.lang.String)(argument));
              }

              public MetaParameter<java.lang.String> connectorIdParameter() {
                return connectorIdParameter;
              }
            }

            public static final class MetaApplyMethod extends InstanceMetaMethod<io.art.rsocket.interceptor.RsocketConnectorLoggingInterceptor, io.rsocket.RSocket> {
              private final MetaParameter<io.rsocket.RSocket> rsocketParameter = register(new MetaParameter<>(0, "rsocket",metaType(io.rsocket.RSocket.class)));

              private MetaApplyMethod() {
                super("apply",metaType(io.rsocket.RSocket.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketConnectorLoggingInterceptor instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.apply((io.rsocket.RSocket)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketConnectorLoggingInterceptor instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.apply((io.rsocket.RSocket)(argument));
              }

              public MetaParameter<io.rsocket.RSocket> rsocketParameter() {
                return rsocketParameter;
              }
            }
          }

          public static final class MetaRsocketLoggingProxyClass extends MetaClass<io.art.rsocket.interceptor.RsocketLoggingProxy> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaField<io.rsocket.RSocket> sourceField = register(new MetaField<>("source",metaType(io.rsocket.RSocket.class),true));

            private final MetaField<io.art.logging.logger.Logger> loggerField = register(new MetaField<>("logger",metaType(io.art.logging.logger.Logger.class),false));

            private final MetaField<io.art.core.property.Property<java.lang.Boolean>> enabledField = register(new MetaField<>("enabled",metaType(io.art.core.property.Property.class,metaType(java.lang.Boolean.class)),false));

            private final MetaFireAndForgetMethod fireAndForgetMethod = register(new MetaFireAndForgetMethod());

            private final MetaFireAndForget_1Method fireAndForget_1Method = register(new MetaFireAndForget_1Method());

            private final MetaRequestResponseMethod requestResponseMethod = register(new MetaRequestResponseMethod());

            private final MetaRequestResponse_1Method requestResponse_1Method = register(new MetaRequestResponse_1Method());

            private final MetaRequestStreamMethod requestStreamMethod = register(new MetaRequestStreamMethod());

            private final MetaRequestStream_1Method requestStream_1Method = register(new MetaRequestStream_1Method());

            private final MetaRequestChannelMethod requestChannelMethod = register(new MetaRequestChannelMethod());

            private final MetaRequestChannel_1Method requestChannel_1Method = register(new MetaRequestChannel_1Method());

            private final MetaMetadataPushMethod metadataPushMethod = register(new MetaMetadataPushMethod());

            private final MetaMetadataPush_1Method metadataPush_1Method = register(new MetaMetadataPush_1Method());

            private final MetaAvailabilityMethod availabilityMethod = register(new MetaAvailabilityMethod());

            private final MetaDisposeMethod disposeMethod = register(new MetaDisposeMethod());

            private final MetaIsDisposedMethod isDisposedMethod = register(new MetaIsDisposedMethod());

            private final MetaOnCloseMethod onCloseMethod = register(new MetaOnCloseMethod());

            private MetaRsocketLoggingProxyClass() {
              super(metaType(io.art.rsocket.interceptor.RsocketLoggingProxy.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<io.rsocket.RSocket> sourceField() {
              return sourceField;
            }

            public MetaField<io.art.logging.logger.Logger> loggerField() {
              return loggerField;
            }

            public MetaField<io.art.core.property.Property<java.lang.Boolean>> enabledField() {
              return enabledField;
            }

            public MetaFireAndForgetMethod fireAndForgetMethod() {
              return fireAndForgetMethod;
            }

            public MetaFireAndForget_1Method fireAndForget_1Method() {
              return fireAndForget_1Method;
            }

            public MetaRequestResponseMethod requestResponseMethod() {
              return requestResponseMethod;
            }

            public MetaRequestResponse_1Method requestResponse_1Method() {
              return requestResponse_1Method;
            }

            public MetaRequestStreamMethod requestStreamMethod() {
              return requestStreamMethod;
            }

            public MetaRequestStream_1Method requestStream_1Method() {
              return requestStream_1Method;
            }

            public MetaRequestChannelMethod requestChannelMethod() {
              return requestChannelMethod;
            }

            public MetaRequestChannel_1Method requestChannel_1Method() {
              return requestChannel_1Method;
            }

            public MetaMetadataPushMethod metadataPushMethod() {
              return metadataPushMethod;
            }

            public MetaMetadataPush_1Method metadataPush_1Method() {
              return metadataPush_1Method;
            }

            public MetaAvailabilityMethod availabilityMethod() {
              return availabilityMethod;
            }

            public MetaDisposeMethod disposeMethod() {
              return disposeMethod;
            }

            public MetaIsDisposedMethod isDisposedMethod() {
              return isDisposedMethod;
            }

            public MetaOnCloseMethod onCloseMethod() {
              return onCloseMethod;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.interceptor.RsocketLoggingProxy> {
              private final MetaParameter<io.art.logging.logger.Logger> loggerParameter = register(new MetaParameter<>(0, "logger",metaType(io.art.logging.logger.Logger.class)));

              private final MetaParameter<io.rsocket.RSocket> rsocketParameter = register(new MetaParameter<>(1, "rsocket",metaType(io.rsocket.RSocket.class)));

              private final MetaParameter<io.art.core.property.Property<java.lang.Boolean>> enabledParameter = register(new MetaParameter<>(2, "enabled",metaType(io.art.core.property.Property.class,metaType(java.lang.Boolean.class))));

              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.interceptor.RsocketLoggingProxy.class));
              }

              @Override
              public io.art.rsocket.interceptor.RsocketLoggingProxy invoke(
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return new io.art.rsocket.interceptor.RsocketLoggingProxy((io.art.logging.logger.Logger)(arguments[0]),(io.rsocket.RSocket)(arguments[1]),(io.art.core.property.Property<java.lang.Boolean>)(arguments[2]));
              }

              public MetaParameter<io.art.logging.logger.Logger> loggerParameter() {
                return loggerParameter;
              }

              public MetaParameter<io.rsocket.RSocket> rsocketParameter() {
                return rsocketParameter;
              }

              public MetaParameter<io.art.core.property.Property<java.lang.Boolean>> enabledParameter(
                  ) {
                return enabledParameter;
              }
            }

            public static final class MetaFireAndForgetMethod extends InstanceMetaMethod<io.art.rsocket.interceptor.RsocketLoggingProxy, reactor.core.publisher.Mono<java.lang.Void>> {
              private final MetaParameter<io.rsocket.Payload> payloadParameter = register(new MetaParameter<>(0, "payload",metaType(io.rsocket.Payload.class)));

              private MetaFireAndForgetMethod() {
                super("fireAndForget",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.Void.class)));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.fireAndForget((io.rsocket.Payload)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.fireAndForget((io.rsocket.Payload)(argument));
              }

              public MetaParameter<io.rsocket.Payload> payloadParameter() {
                return payloadParameter;
              }
            }

            public static final class MetaFireAndForget_1Method extends InstanceMetaMethod<io.art.rsocket.interceptor.RsocketLoggingProxy, reactor.core.publisher.Mono<java.lang.Void>> {
              private final MetaParameter<io.rsocket.Payload> payloadParameter = register(new MetaParameter<>(0, "payload",metaType(io.rsocket.Payload.class)));

              private MetaFireAndForget_1Method() {
                super("fireAndForget",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.Void.class)));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.fireAndForget((io.rsocket.Payload)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.fireAndForget((io.rsocket.Payload)(argument));
              }

              public MetaParameter<io.rsocket.Payload> payloadParameter() {
                return payloadParameter;
              }
            }

            public static final class MetaRequestResponseMethod extends InstanceMetaMethod<io.art.rsocket.interceptor.RsocketLoggingProxy, reactor.core.publisher.Mono<io.rsocket.Payload>> {
              private final MetaParameter<io.rsocket.Payload> payloadParameter = register(new MetaParameter<>(0, "payload",metaType(io.rsocket.Payload.class)));

              private MetaRequestResponseMethod() {
                super("requestResponse",metaType(reactor.core.publisher.Mono.class,metaType(io.rsocket.Payload.class)));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.requestResponse((io.rsocket.Payload)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.requestResponse((io.rsocket.Payload)(argument));
              }

              public MetaParameter<io.rsocket.Payload> payloadParameter() {
                return payloadParameter;
              }
            }

            public static final class MetaRequestResponse_1Method extends InstanceMetaMethod<io.art.rsocket.interceptor.RsocketLoggingProxy, reactor.core.publisher.Mono<io.rsocket.Payload>> {
              private final MetaParameter<io.rsocket.Payload> payloadParameter = register(new MetaParameter<>(0, "payload",metaType(io.rsocket.Payload.class)));

              private MetaRequestResponse_1Method() {
                super("requestResponse",metaType(reactor.core.publisher.Mono.class,metaType(io.rsocket.Payload.class)));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.requestResponse((io.rsocket.Payload)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.requestResponse((io.rsocket.Payload)(argument));
              }

              public MetaParameter<io.rsocket.Payload> payloadParameter() {
                return payloadParameter;
              }
            }

            public static final class MetaRequestStreamMethod extends InstanceMetaMethod<io.art.rsocket.interceptor.RsocketLoggingProxy, reactor.core.publisher.Flux<io.rsocket.Payload>> {
              private final MetaParameter<io.rsocket.Payload> payloadParameter = register(new MetaParameter<>(0, "payload",metaType(io.rsocket.Payload.class)));

              private MetaRequestStreamMethod() {
                super("requestStream",metaType(reactor.core.publisher.Flux.class,metaType(io.rsocket.Payload.class)));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.requestStream((io.rsocket.Payload)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.requestStream((io.rsocket.Payload)(argument));
              }

              public MetaParameter<io.rsocket.Payload> payloadParameter() {
                return payloadParameter;
              }
            }

            public static final class MetaRequestStream_1Method extends InstanceMetaMethod<io.art.rsocket.interceptor.RsocketLoggingProxy, reactor.core.publisher.Flux<io.rsocket.Payload>> {
              private final MetaParameter<io.rsocket.Payload> payloadParameter = register(new MetaParameter<>(0, "payload",metaType(io.rsocket.Payload.class)));

              private MetaRequestStream_1Method() {
                super("requestStream",metaType(reactor.core.publisher.Flux.class,metaType(io.rsocket.Payload.class)));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.requestStream((io.rsocket.Payload)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.requestStream((io.rsocket.Payload)(argument));
              }

              public MetaParameter<io.rsocket.Payload> payloadParameter() {
                return payloadParameter;
              }
            }

            public static final class MetaRequestChannelMethod extends InstanceMetaMethod<io.art.rsocket.interceptor.RsocketLoggingProxy, reactor.core.publisher.Flux<io.rsocket.Payload>> {
              private final MetaParameter<org.reactivestreams.Publisher<io.rsocket.Payload>> payloadsParameter = register(new MetaParameter<>(0, "payloads",metaType(org.reactivestreams.Publisher.class,metaType(io.rsocket.Payload.class))));

              private MetaRequestChannelMethod() {
                super("requestChannel",metaType(reactor.core.publisher.Flux.class,metaType(io.rsocket.Payload.class)));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.requestChannel((org.reactivestreams.Publisher<io.rsocket.Payload>)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.requestChannel((org.reactivestreams.Publisher)(argument));
              }

              public MetaParameter<org.reactivestreams.Publisher<io.rsocket.Payload>> payloadsParameter(
                  ) {
                return payloadsParameter;
              }
            }

            public static final class MetaRequestChannel_1Method extends InstanceMetaMethod<io.art.rsocket.interceptor.RsocketLoggingProxy, reactor.core.publisher.Flux<io.rsocket.Payload>> {
              private final MetaParameter<org.reactivestreams.Publisher<io.rsocket.Payload>> payloadsParameter = register(new MetaParameter<>(0, "payloads",metaType(org.reactivestreams.Publisher.class,metaType(io.rsocket.Payload.class))));

              private MetaRequestChannel_1Method() {
                super("requestChannel",metaType(reactor.core.publisher.Flux.class,metaType(io.rsocket.Payload.class)));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.requestChannel((org.reactivestreams.Publisher<io.rsocket.Payload>)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.requestChannel((org.reactivestreams.Publisher)(argument));
              }

              public MetaParameter<org.reactivestreams.Publisher<io.rsocket.Payload>> payloadsParameter(
                  ) {
                return payloadsParameter;
              }
            }

            public static final class MetaMetadataPushMethod extends InstanceMetaMethod<io.art.rsocket.interceptor.RsocketLoggingProxy, reactor.core.publisher.Mono<java.lang.Void>> {
              private final MetaParameter<io.rsocket.Payload> payloadParameter = register(new MetaParameter<>(0, "payload",metaType(io.rsocket.Payload.class)));

              private MetaMetadataPushMethod() {
                super("metadataPush",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.Void.class)));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.metadataPush((io.rsocket.Payload)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.metadataPush((io.rsocket.Payload)(argument));
              }

              public MetaParameter<io.rsocket.Payload> payloadParameter() {
                return payloadParameter;
              }
            }

            public static final class MetaMetadataPush_1Method extends InstanceMetaMethod<io.art.rsocket.interceptor.RsocketLoggingProxy, reactor.core.publisher.Mono<java.lang.Void>> {
              private final MetaParameter<io.rsocket.Payload> payloadParameter = register(new MetaParameter<>(0, "payload",metaType(io.rsocket.Payload.class)));

              private MetaMetadataPush_1Method() {
                super("metadataPush",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.Void.class)));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.metadataPush((io.rsocket.Payload)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.metadataPush((io.rsocket.Payload)(argument));
              }

              public MetaParameter<io.rsocket.Payload> payloadParameter() {
                return payloadParameter;
              }
            }

            public static final class MetaAvailabilityMethod extends InstanceMetaMethod<io.art.rsocket.interceptor.RsocketLoggingProxy, Double> {
              private MetaAvailabilityMethod() {
                super("availability",metaType(double.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.availability();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance) throws
                  java.lang.Throwable {
                return instance.availability();
              }
            }

            public static final class MetaDisposeMethod extends InstanceMetaMethod<io.art.rsocket.interceptor.RsocketLoggingProxy, java.lang.Void> {
              private MetaDisposeMethod() {
                super("dispose",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.dispose();
                return null;
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance) throws
                  java.lang.Throwable {
                instance.dispose();
                return null;
              }
            }

            public static final class MetaIsDisposedMethod extends InstanceMetaMethod<io.art.rsocket.interceptor.RsocketLoggingProxy, java.lang.Boolean> {
              private MetaIsDisposedMethod() {
                super("isDisposed",metaType(boolean.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.isDisposed();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance) throws
                  java.lang.Throwable {
                return instance.isDisposed();
              }
            }

            public static final class MetaOnCloseMethod extends InstanceMetaMethod<io.art.rsocket.interceptor.RsocketLoggingProxy, reactor.core.publisher.Mono<java.lang.Void>> {
              private MetaOnCloseMethod() {
                super("onClose",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.Void.class)));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.onClose();
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketLoggingProxy instance) throws
                  java.lang.Throwable {
                return instance.onClose();
              }
            }
          }

          public static final class MetaRsocketServerLoggingInterceptorClass extends MetaClass<io.art.rsocket.interceptor.RsocketServerLoggingInterceptor> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaField<java.util.concurrent.atomic.AtomicReference<java.lang.Object>> loggerField = register(new MetaField<>("logger",metaType(java.util.concurrent.atomic.AtomicReference.class,metaType(java.lang.Object.class)),false));

            private final MetaField<io.art.core.property.Property<java.lang.Boolean>> enabledField = register(new MetaField<>("enabled",metaType(io.art.core.property.Property.class,metaType(java.lang.Boolean.class)),false));

            private final MetaApplyMethod applyMethod = register(new MetaApplyMethod());

            private MetaRsocketServerLoggingInterceptorClass() {
              super(metaType(io.art.rsocket.interceptor.RsocketServerLoggingInterceptor.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<java.util.concurrent.atomic.AtomicReference<java.lang.Object>> loggerField(
                ) {
              return loggerField;
            }

            public MetaField<io.art.core.property.Property<java.lang.Boolean>> enabledField() {
              return enabledField;
            }

            public MetaApplyMethod applyMethod() {
              return applyMethod;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.interceptor.RsocketServerLoggingInterceptor> {
              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.interceptor.RsocketServerLoggingInterceptor.class));
              }

              @Override
              public io.art.rsocket.interceptor.RsocketServerLoggingInterceptor invoke(
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return new io.art.rsocket.interceptor.RsocketServerLoggingInterceptor();
              }

              @Override
              public io.art.rsocket.interceptor.RsocketServerLoggingInterceptor invoke() throws
                  java.lang.Throwable {
                return new io.art.rsocket.interceptor.RsocketServerLoggingInterceptor();
              }
            }

            public static final class MetaApplyMethod extends InstanceMetaMethod<io.art.rsocket.interceptor.RsocketServerLoggingInterceptor, io.rsocket.RSocket> {
              private final MetaParameter<io.rsocket.RSocket> rsocketParameter = register(new MetaParameter<>(0, "rsocket",metaType(io.rsocket.RSocket.class)));

              private MetaApplyMethod() {
                super("apply",metaType(io.rsocket.RSocket.class));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketServerLoggingInterceptor instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.apply((io.rsocket.RSocket)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(
                  io.art.rsocket.interceptor.RsocketServerLoggingInterceptor instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.apply((io.rsocket.RSocket)(argument));
              }

              public MetaParameter<io.rsocket.RSocket> rsocketParameter() {
                return rsocketParameter;
              }
            }
          }
        }

        public static final class MetaSocketPackage extends MetaPackage {
          private final MetaServingRsocketClass servingRsocketClass = register(new MetaServingRsocketClass());

          private MetaSocketPackage() {
            super("socket");
          }

          public MetaServingRsocketClass servingRsocketClass() {
            return servingRsocketClass;
          }

          public static final class MetaServingRsocketClass extends MetaClass<io.art.rsocket.socket.ServingRsocket> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaField<io.art.transport.payload.TransportPayloadReader> dataReaderField = register(new MetaField<>("dataReader",metaType(io.art.transport.payload.TransportPayloadReader.class),false));

            private final MetaField<io.art.transport.payload.TransportPayloadWriter> dataWriterField = register(new MetaField<>("dataWriter",metaType(io.art.transport.payload.TransportPayloadWriter.class),false));

            private final MetaField<io.rsocket.RSocket> requesterSocketField = register(new MetaField<>("requesterSocket",metaType(io.rsocket.RSocket.class),false));

            private final MetaField<io.art.rsocket.state.RsocketModuleState> moduleStateField = register(new MetaField<>("moduleState",metaType(io.art.rsocket.state.RsocketModuleState.class),false));

            private final MetaField<io.art.rsocket.model.RsocketSetupPayload> setupPayloadField = register(new MetaField<>("setupPayload",metaType(io.art.rsocket.model.RsocketSetupPayload.class),false));

            private final MetaField<io.art.server.method.ServiceMethod> serviceMethodField = register(new MetaField<>("serviceMethod",metaType(io.art.server.method.ServiceMethod.class),false));

            private final MetaFireAndForgetMethod fireAndForgetMethod = register(new MetaFireAndForgetMethod());

            private final MetaRequestResponseMethod requestResponseMethod = register(new MetaRequestResponseMethod());

            private final MetaRequestStreamMethod requestStreamMethod = register(new MetaRequestStreamMethod());

            private final MetaRequestChannelMethod requestChannelMethod = register(new MetaRequestChannelMethod());

            private final MetaMetadataPushMethod metadataPushMethod = register(new MetaMetadataPushMethod());

            private final MetaDisposeMethod disposeMethod = register(new MetaDisposeMethod());

            private MetaServingRsocketClass() {
              super(metaType(io.art.rsocket.socket.ServingRsocket.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<io.art.transport.payload.TransportPayloadReader> dataReaderField() {
              return dataReaderField;
            }

            public MetaField<io.art.transport.payload.TransportPayloadWriter> dataWriterField() {
              return dataWriterField;
            }

            public MetaField<io.rsocket.RSocket> requesterSocketField() {
              return requesterSocketField;
            }

            public MetaField<io.art.rsocket.state.RsocketModuleState> moduleStateField() {
              return moduleStateField;
            }

            public MetaField<io.art.rsocket.model.RsocketSetupPayload> setupPayloadField() {
              return setupPayloadField;
            }

            public MetaField<io.art.server.method.ServiceMethod> serviceMethodField() {
              return serviceMethodField;
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

            public MetaMetadataPushMethod metadataPushMethod() {
              return metadataPushMethod;
            }

            public MetaDisposeMethod disposeMethod() {
              return disposeMethod;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.socket.ServingRsocket> {
              private final MetaParameter<io.rsocket.ConnectionSetupPayload> payloadParameter = register(new MetaParameter<>(0, "payload",metaType(io.rsocket.ConnectionSetupPayload.class)));

              private final MetaParameter<io.rsocket.RSocket> requesterSocketParameter = register(new MetaParameter<>(1, "requesterSocket",metaType(io.rsocket.RSocket.class)));

              private final MetaParameter<io.art.rsocket.configuration.RsocketServerConfiguration> rsocketConfigurationParameter = register(new MetaParameter<>(2, "rsocketConfiguration",metaType(io.art.rsocket.configuration.RsocketServerConfiguration.class)));

              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.socket.ServingRsocket.class));
              }

              @Override
              public io.art.rsocket.socket.ServingRsocket invoke(java.lang.Object[] arguments)
                  throws java.lang.Throwable {
                return new io.art.rsocket.socket.ServingRsocket((io.rsocket.ConnectionSetupPayload)(arguments[0]),(io.rsocket.RSocket)(arguments[1]),(io.art.rsocket.configuration.RsocketServerConfiguration)(arguments[2]));
              }

              public MetaParameter<io.rsocket.ConnectionSetupPayload> payloadParameter() {
                return payloadParameter;
              }

              public MetaParameter<io.rsocket.RSocket> requesterSocketParameter() {
                return requesterSocketParameter;
              }

              public MetaParameter<io.art.rsocket.configuration.RsocketServerConfiguration> rsocketConfigurationParameter(
                  ) {
                return rsocketConfigurationParameter;
              }
            }

            public static final class MetaFireAndForgetMethod extends InstanceMetaMethod<io.art.rsocket.socket.ServingRsocket, reactor.core.publisher.Mono<java.lang.Void>> {
              private final MetaParameter<io.rsocket.Payload> payloadParameter = register(new MetaParameter<>(0, "payload",metaType(io.rsocket.Payload.class)));

              private MetaFireAndForgetMethod() {
                super("fireAndForget",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.Void.class)));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.socket.ServingRsocket instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.fireAndForget((io.rsocket.Payload)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.socket.ServingRsocket instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.fireAndForget((io.rsocket.Payload)(argument));
              }

              public MetaParameter<io.rsocket.Payload> payloadParameter() {
                return payloadParameter;
              }
            }

            public static final class MetaRequestResponseMethod extends InstanceMetaMethod<io.art.rsocket.socket.ServingRsocket, reactor.core.publisher.Mono<io.rsocket.Payload>> {
              private final MetaParameter<io.rsocket.Payload> payloadParameter = register(new MetaParameter<>(0, "payload",metaType(io.rsocket.Payload.class)));

              private MetaRequestResponseMethod() {
                super("requestResponse",metaType(reactor.core.publisher.Mono.class,metaType(io.rsocket.Payload.class)));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.socket.ServingRsocket instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.requestResponse((io.rsocket.Payload)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.socket.ServingRsocket instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.requestResponse((io.rsocket.Payload)(argument));
              }

              public MetaParameter<io.rsocket.Payload> payloadParameter() {
                return payloadParameter;
              }
            }

            public static final class MetaRequestStreamMethod extends InstanceMetaMethod<io.art.rsocket.socket.ServingRsocket, reactor.core.publisher.Flux<io.rsocket.Payload>> {
              private final MetaParameter<io.rsocket.Payload> payloadParameter = register(new MetaParameter<>(0, "payload",metaType(io.rsocket.Payload.class)));

              private MetaRequestStreamMethod() {
                super("requestStream",metaType(reactor.core.publisher.Flux.class,metaType(io.rsocket.Payload.class)));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.socket.ServingRsocket instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.requestStream((io.rsocket.Payload)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.socket.ServingRsocket instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.requestStream((io.rsocket.Payload)(argument));
              }

              public MetaParameter<io.rsocket.Payload> payloadParameter() {
                return payloadParameter;
              }
            }

            public static final class MetaRequestChannelMethod extends InstanceMetaMethod<io.art.rsocket.socket.ServingRsocket, reactor.core.publisher.Flux<io.rsocket.Payload>> {
              private final MetaParameter<org.reactivestreams.Publisher<io.rsocket.Payload>> payloadsParameter = register(new MetaParameter<>(0, "payloads",metaType(org.reactivestreams.Publisher.class,metaType(io.rsocket.Payload.class))));

              private MetaRequestChannelMethod() {
                super("requestChannel",metaType(reactor.core.publisher.Flux.class,metaType(io.rsocket.Payload.class)));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.socket.ServingRsocket instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.requestChannel((org.reactivestreams.Publisher<io.rsocket.Payload>)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.socket.ServingRsocket instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.requestChannel((org.reactivestreams.Publisher)(argument));
              }

              public MetaParameter<org.reactivestreams.Publisher<io.rsocket.Payload>> payloadsParameter(
                  ) {
                return payloadsParameter;
              }
            }

            public static final class MetaMetadataPushMethod extends InstanceMetaMethod<io.art.rsocket.socket.ServingRsocket, reactor.core.publisher.Mono<java.lang.Void>> {
              private final MetaParameter<io.rsocket.Payload> payloadParameter = register(new MetaParameter<>(0, "payload",metaType(io.rsocket.Payload.class)));

              private MetaMetadataPushMethod() {
                super("metadataPush",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.Void.class)));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.socket.ServingRsocket instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                return instance.metadataPush((io.rsocket.Payload)(arguments[0]));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.socket.ServingRsocket instance,
                  java.lang.Object argument) throws java.lang.Throwable {
                return instance.metadataPush((io.rsocket.Payload)(argument));
              }

              public MetaParameter<io.rsocket.Payload> payloadParameter() {
                return payloadParameter;
              }
            }

            public static final class MetaDisposeMethod extends InstanceMetaMethod<io.art.rsocket.socket.ServingRsocket, java.lang.Void> {
              private MetaDisposeMethod() {
                super("dispose",metaType(java.lang.Void.class));
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.socket.ServingRsocket instance,
                  java.lang.Object[] arguments) throws java.lang.Throwable {
                instance.dispose();
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.rsocket.socket.ServingRsocket instance) throws
                  java.lang.Throwable {
                instance.dispose();
                return null;
              }
            }
          }
        }
      }
    }
  }
}
