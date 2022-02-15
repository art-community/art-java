package io.art.tarantool.test.meta;

import static io.art.meta.model.MetaType.metaType;

import io.art.core.property.LazyProperty;
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
import io.art.tarantool.test.*;
import io.art.tarantool.test.manager.*;
import io.art.tarantool.test.model.*;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings({"all","unchecked","unused"})
public class MetaTarantoolTest extends MetaLibrary {
  private final MetaIoPackage ioPackage = register(new MetaIoPackage());

  public MetaTarantoolTest(MetaLibrary... dependencies) {
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
      private final MetaTarantoolPackage tarantoolPackage = register(new MetaTarantoolPackage());

      private MetaArtPackage() {
        super("art");
      }

      public MetaTarantoolPackage tarantoolPackage() {
        return tarantoolPackage;
      }

      public static final class MetaTarantoolPackage extends MetaPackage {
        private final MetaTestPackage testPackage = register(new MetaTestPackage());

        private MetaTarantoolPackage() {
          super("tarantool");
        }

        public MetaTestPackage testPackage() {
          return testPackage;
        }

        public static final class MetaTestPackage extends MetaPackage {
          private final MetaTarantoolStorageTestClass tarantoolStorageTestClass = register(new MetaTarantoolStorageTestClass());

          private final MetaConstantsPackage constantsPackage = register(new MetaConstantsPackage());

          private final MetaStoragePackage storagePackage = register(new MetaStoragePackage());

          private final MetaModelPackage modelPackage = register(new MetaModelPackage());

          private MetaTestPackage() {
            super("test");
          }

          public MetaTarantoolStorageTestClass tarantoolStorageTestClass() {
            return tarantoolStorageTestClass;
          }

          public MetaConstantsPackage constantsPackage() {
            return constantsPackage;
          }

          public MetaStoragePackage storagePackage() {
            return storagePackage;
          }

          public MetaModelPackage modelPackage() {
            return modelPackage;
          }

          public static final class MetaTarantoolStorageTestClass extends MetaClass<io.art.tarantool.test.TarantoolStorageTest> {
            private static final LazyProperty<MetaTarantoolStorageTestClass> self = MetaClass.self(io.art.tarantool.test.TarantoolStorageTest.class);

            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor(this));

            private final MetaSetupMethod setupMethod = register(new MetaSetupMethod(this));

            private final MetaCleanupMethod cleanupMethod = register(new MetaCleanupMethod(this));

            private final MetaTestSinglePutMethod testSinglePutMethod = register(new MetaTestSinglePutMethod(this));

            private final MetaTestFindFirstMethod testFindFirstMethod = register(new MetaTestFindFirstMethod(this));

            private MetaTarantoolStorageTestClass() {
              super(metaType(io.art.tarantool.test.TarantoolStorageTest.class));
            }

            public static MetaTarantoolStorageTestClass tarantoolStorageTest() {
              return self.get();
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaSetupMethod setupMethod() {
              return setupMethod;
            }

            public MetaCleanupMethod cleanupMethod() {
              return cleanupMethod;
            }

            public MetaTestSinglePutMethod testSinglePutMethod() {
              return testSinglePutMethod;
            }

            public MetaTestFindFirstMethod testFindFirstMethod() {
              return testFindFirstMethod;
            }

            public final class MetaConstructorConstructor extends MetaConstructor<MetaClass<?>, TarantoolStorageTest> {
              private MetaConstructorConstructor(MetaClass owner) {
                super(metaType(io.art.tarantool.test.TarantoolStorageTest.class),owner);
              }

              @Override
              public io.art.tarantool.test.TarantoolStorageTest invoke(java.lang.Object[] arguments)
                  throws Throwable {
                return new io.art.tarantool.test.TarantoolStorageTest();
              }

              @Override
              public io.art.tarantool.test.TarantoolStorageTest invoke() throws Throwable {
                return new io.art.tarantool.test.TarantoolStorageTest();
              }
            }

            public final class MetaSetupMethod extends StaticMetaMethod<MetaClass<?>, Void> {
              private MetaSetupMethod(MetaClass owner) {
                super("setup",metaType(Void.class),owner);
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws Throwable {
                io.art.tarantool.test.TarantoolStorageTest.setup();
                return null;
              }

              @Override
              public java.lang.Object invoke() throws Throwable {
                io.art.tarantool.test.TarantoolStorageTest.setup();
                return null;
              }
            }

            public final class MetaCleanupMethod extends StaticMetaMethod<MetaClass<?>, Void> {
              private MetaCleanupMethod(MetaClass owner) {
                super("cleanup",metaType(Void.class),owner);
              }

              @Override
              public java.lang.Object invoke(java.lang.Object[] arguments) throws Throwable {
                io.art.tarantool.test.TarantoolStorageTest.cleanup();
                return null;
              }

              @Override
              public java.lang.Object invoke() throws Throwable {
                io.art.tarantool.test.TarantoolStorageTest.cleanup();
                return null;
              }
            }

            public final class MetaTestSinglePutMethod extends InstanceMetaMethod<MetaClass<?>, TarantoolStorageTest, Void> {
              private MetaTestSinglePutMethod(MetaClass owner) {
                super("testSinglePut",metaType(Void.class),owner);
              }

              @Override
              public java.lang.Object invoke(io.art.tarantool.test.TarantoolStorageTest instance,
                  java.lang.Object[] arguments) throws Throwable {
                instance.testSinglePut();
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.tarantool.test.TarantoolStorageTest instance)
                  throws Throwable {
                instance.testSinglePut();
                return null;
              }
            }

            public final class MetaTestFindFirstMethod extends InstanceMetaMethod<MetaClass<?>, TarantoolStorageTest, Void> {
              private MetaTestFindFirstMethod(MetaClass owner) {
                super("testFindFirst",metaType(Void.class),owner);
              }

              @Override
              public java.lang.Object invoke(io.art.tarantool.test.TarantoolStorageTest instance,
                  java.lang.Object[] arguments) throws Throwable {
                instance.testFindFirst();
                return null;
              }

              @Override
              public java.lang.Object invoke(io.art.tarantool.test.TarantoolStorageTest instance)
                  throws Throwable {
                instance.testFindFirst();
                return null;
              }
            }
          }

          public static final class MetaConstantsPackage extends MetaPackage {
            private final MetaTestTarantoolConstantsClass testTarantoolConstantsClass = register(new MetaTestTarantoolConstantsClass());

            private MetaConstantsPackage() {
              super("constants");
            }

            public MetaTestTarantoolConstantsClass testTarantoolConstantsClass() {
              return testTarantoolConstantsClass;
            }

            public static final class MetaTestTarantoolConstantsClass extends MetaClass<io.art.tarantool.test.constants.TestTarantoolConstants> {
              private static final LazyProperty<MetaTestTarantoolConstantsClass> self = MetaClass.self(io.art.tarantool.test.constants.TestTarantoolConstants.class);

              private final MetaField<MetaClass<?>, String> STORAGE_DIRECTORYField = register(new MetaField<>("STORAGE_DIRECTORY",metaType(java.lang.String.class),false,this));

              private final MetaField<MetaClass<?>, String> STORAGE_COMMANDField = register(new MetaField<>("STORAGE_COMMAND",metaType(java.lang.String.class),false,this));

              private final MetaField<MetaClass<?>, String> STORAGE_PIDField = register(new MetaField<>("STORAGE_PID",metaType(java.lang.String.class),false,this));

              private final MetaField<MetaClass<?>, String> KILL_COMMANDField = register(new MetaField<>("KILL_COMMAND",metaType(java.lang.String.class),false,this));

              private final MetaField<MetaClass<?>, String> BASHField = register(new MetaField<>("BASH",metaType(java.lang.String.class),false,this));

              private final MetaField<MetaClass<?>, String> BASH_ARGUMENTField = register(new MetaField<>("BASH_ARGUMENT",metaType(java.lang.String.class),false,this));

              private final MetaField<MetaClass<?>, String> STORAGE_SCRIPTField = register(new MetaField<>("STORAGE_SCRIPT",metaType(java.lang.String.class),false,this));

              private final MetaField<MetaClass<?>, Integer> STORAGE_PORTField = register(new MetaField<>("STORAGE_PORT",metaType(int.class),false,this));

              private final MetaField<MetaClass<?>, String> USERNAMEField = register(new MetaField<>("USERNAME",metaType(java.lang.String.class),false,this));

              private final MetaField<MetaClass<?>, String> PASSWORDField = register(new MetaField<>("PASSWORD",metaType(java.lang.String.class),false,this));

              private final MetaField<MetaClass<?>, String> MODULE_SCRIPTField = register(new MetaField<>("MODULE_SCRIPT",metaType(java.lang.String.class),false,this));

              private MetaTestTarantoolConstantsClass() {
                super(metaType(io.art.tarantool.test.constants.TestTarantoolConstants.class));
              }

              public static MetaTestTarantoolConstantsClass testTarantoolConstants() {
                return self.get();
              }

              public MetaField<MetaClass<?>, String> STORAGE_DIRECTORYField() {
                return STORAGE_DIRECTORYField;
              }

              public MetaField<MetaClass<?>, String> STORAGE_COMMANDField() {
                return STORAGE_COMMANDField;
              }

              public MetaField<MetaClass<?>, String> STORAGE_PIDField() {
                return STORAGE_PIDField;
              }

              public MetaField<MetaClass<?>, String> KILL_COMMANDField() {
                return KILL_COMMANDField;
              }

              public MetaField<MetaClass<?>, String> BASHField() {
                return BASHField;
              }

              public MetaField<MetaClass<?>, String> BASH_ARGUMENTField() {
                return BASH_ARGUMENTField;
              }

              public MetaField<MetaClass<?>, String> STORAGE_SCRIPTField() {
                return STORAGE_SCRIPTField;
              }

              public MetaField<MetaClass<?>, Integer> STORAGE_PORTField() {
                return STORAGE_PORTField;
              }

              public MetaField<MetaClass<?>, String> USERNAMEField() {
                return USERNAMEField;
              }

              public MetaField<MetaClass<?>, String> PASSWORDField() {
                return PASSWORDField;
              }

              public MetaField<MetaClass<?>, String> MODULE_SCRIPTField() {
                return MODULE_SCRIPTField;
              }

              @Override
              public MetaProxy proxy(
                  Map<MetaMethod<MetaClass<?>, ?>, Function<java.lang.Object, java.lang.Object>> invocations) {
                return new MetaTestTarantoolConstantsProxy(invocations);
              }

              public class MetaTestTarantoolConstantsProxy extends MetaProxy implements io.art.tarantool.test.constants.TestTarantoolConstants {
                public MetaTestTarantoolConstantsProxy(
                    Map<MetaMethod<MetaClass<?>, ?>, Function<java.lang.Object, java.lang.Object>> invocations) {
                  super(invocations);
                }
              }
            }
          }

          public static final class MetaStoragePackage extends MetaPackage {
            private final MetaTestTarantoolStorageClass testTarantoolStorageClass = register(new MetaTestTarantoolStorageClass());

            private MetaStoragePackage() {
              super("storage");
            }

            public MetaTestTarantoolStorageClass testTarantoolStorageClass() {
              return testTarantoolStorageClass;
            }

            public static final class MetaTestTarantoolStorageClass extends MetaClass<TestTarantoolInstanceManager> {
              private static final LazyProperty<MetaTestTarantoolStorageClass> self = MetaClass.self(TestTarantoolInstanceManager.class);

              private final MetaInitializeStorageMethod initializeStorageMethod = register(new MetaInitializeStorageMethod(this));

              private final MetaShutdownStorageMethod shutdownStorageMethod = register(new MetaShutdownStorageMethod(this));

              private MetaTestTarantoolStorageClass() {
                super(metaType(TestTarantoolInstanceManager.class));
              }

              public static MetaTestTarantoolStorageClass testTarantoolStorage() {
                return self.get();
              }

              public MetaInitializeStorageMethod initializeStorageMethod() {
                return initializeStorageMethod;
              }

              public MetaShutdownStorageMethod shutdownStorageMethod() {
                return shutdownStorageMethod;
              }

              public final class MetaInitializeStorageMethod extends StaticMetaMethod<MetaClass<?>, Void> {
                private MetaInitializeStorageMethod(MetaClass owner) {
                  super("initializeStorage",metaType(Void.class),owner);
                }

                @Override
                public java.lang.Object invoke(java.lang.Object[] arguments) throws Throwable {
                  TestTarantoolInstanceManager.initializeStorage();
                  return null;
                }

                @Override
                public java.lang.Object invoke() throws Throwable {
                  TestTarantoolInstanceManager.initializeStorage();
                  return null;
                }
              }

              public final class MetaShutdownStorageMethod extends StaticMetaMethod<MetaClass<?>, Void> {
                private MetaShutdownStorageMethod(MetaClass owner) {
                  super("shutdownStorage",metaType(Void.class),owner);
                }

                @Override
                public java.lang.Object invoke(java.lang.Object[] arguments) throws Throwable {
                  TestTarantoolInstanceManager.shutdownStorage();
                  return null;
                }

                @Override
                public java.lang.Object invoke() throws Throwable {
                  TestTarantoolInstanceManager.shutdownStorage();
                  return null;
                }
              }
            }
          }

          public static final class MetaModelPackage extends MetaPackage {
            private final MetaTestDataClass testDataClass = register(new MetaTestDataClass());

            private final MetaTestStorageClass testStorageClass = register(new MetaTestStorageClass());

            private MetaModelPackage() {
              super("model");
            }

            public MetaTestDataClass testDataClass() {
              return testDataClass;
            }

            public MetaTestStorageClass testStorageClass() {
              return testStorageClass;
            }

            public static final class MetaTestDataClass extends MetaClass<io.art.tarantool.test.model.TestData> {
              private static final LazyProperty<MetaTestDataClass> self = MetaClass.self(io.art.tarantool.test.model.TestData.class);

              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor(this));

              private final MetaField<MetaClass<?>, Integer> idField = register(new MetaField<>("id",metaType(int.class),false,this));

              private final MetaField<MetaClass<?>, String> contentField = register(new MetaField<>("content",metaType(java.lang.String.class),false,this));

              private final MetaField<MetaClass<?>, TestData.Inner> innerField = register(new MetaField<>("inner",metaType(io.art.tarantool.test.model.TestData.Inner.class),false,this));

              private final MetaToBuilderMethod toBuilderMethod = register(new MetaToBuilderMethod(this));

              private final MetaGetIdMethod getIdMethod = register(new MetaGetIdMethod(this));

              private final MetaGetContentMethod getContentMethod = register(new MetaGetContentMethod(this));

              private final MetaGetInnerMethod getInnerMethod = register(new MetaGetInnerMethod(this));

              private final MetaTestDataBuilderClass testDataBuilderClass = register(new MetaTestDataBuilderClass());

              private final MetaInnerClass innerClass = register(new MetaInnerClass());

              private MetaTestDataClass() {
                super(metaType(io.art.tarantool.test.model.TestData.class));
              }

              public static MetaTestDataClass testData() {
                return self.get();
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaField<MetaClass<?>, Integer> idField() {
                return idField;
              }

              public MetaField<MetaClass<?>, String> contentField() {
                return contentField;
              }

              public MetaField<MetaClass<?>, TestData.Inner> innerField() {
                return innerField;
              }

              public MetaToBuilderMethod toBuilderMethod() {
                return toBuilderMethod;
              }

              public MetaGetIdMethod getIdMethod() {
                return getIdMethod;
              }

              public MetaGetContentMethod getContentMethod() {
                return getContentMethod;
              }

              public MetaGetInnerMethod getInnerMethod() {
                return getInnerMethod;
              }

              public MetaTestDataBuilderClass testDataBuilderClass() {
                return testDataBuilderClass;
              }

              public MetaInnerClass innerClass() {
                return innerClass;
              }

              public final class MetaConstructorConstructor extends MetaConstructor<MetaClass<?>, TestData> {
                private final MetaParameter<java.lang.Integer> idParameter = register(new MetaParameter<>(0, "id",metaType(int.class)));

                private final MetaParameter<java.lang.String> contentParameter = register(new MetaParameter<>(1, "content",metaType(java.lang.String.class)));

                private final MetaParameter<io.art.tarantool.test.model.TestData.Inner> innerParameter = register(new MetaParameter<>(2, "inner",metaType(io.art.tarantool.test.model.TestData.Inner.class)));

                private MetaConstructorConstructor(MetaClass owner) {
                  super(metaType(io.art.tarantool.test.model.TestData.class),owner);
                }

                @Override
                public io.art.tarantool.test.model.TestData invoke(java.lang.Object[] arguments)
                    throws Throwable {
                  return new io.art.tarantool.test.model.TestData((int)(arguments[0]),(java.lang.String)(arguments[1]),(io.art.tarantool.test.model.TestData.Inner)(arguments[2]));
                }

                public MetaParameter<java.lang.Integer> idParameter() {
                  return idParameter;
                }

                public MetaParameter<java.lang.String> contentParameter() {
                  return contentParameter;
                }

                public MetaParameter<io.art.tarantool.test.model.TestData.Inner> innerParameter() {
                  return innerParameter;
                }
              }

              public final class MetaToBuilderMethod extends InstanceMetaMethod<MetaClass<?>, TestData, TestData.TestDataBuilder> {
                private MetaToBuilderMethod(MetaClass owner) {
                  super("toBuilder",metaType(io.art.tarantool.test.model.TestData.TestDataBuilder.class),owner);
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestData instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.toBuilder();
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestData instance) throws
                    Throwable {
                  return instance.toBuilder();
                }
              }

              public final class MetaGetIdMethod extends InstanceMetaMethod<MetaClass<?>, TestData, Integer> {
                private MetaGetIdMethod(MetaClass owner) {
                  super("getId",metaType(int.class),owner);
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestData instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.getId();
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestData instance) throws
                    Throwable {
                  return instance.getId();
                }
              }

              public final class MetaGetContentMethod extends InstanceMetaMethod<MetaClass<?>, TestData, String> {
                private MetaGetContentMethod(MetaClass owner) {
                  super("getContent",metaType(java.lang.String.class),owner);
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestData instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.getContent();
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestData instance) throws
                    Throwable {
                  return instance.getContent();
                }
              }

              public final class MetaGetInnerMethod extends InstanceMetaMethod<MetaClass<?>, TestData, TestData.Inner> {
                private MetaGetInnerMethod(MetaClass owner) {
                  super("getInner",metaType(io.art.tarantool.test.model.TestData.Inner.class),owner);
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestData instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.getInner();
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestData instance) throws
                    Throwable {
                  return instance.getInner();
                }
              }

              public static final class MetaTestDataBuilderClass extends MetaClass<io.art.tarantool.test.model.TestData.TestDataBuilder> {
                private static final LazyProperty<MetaTestDataBuilderClass> self = MetaClass.self(io.art.tarantool.test.model.TestData.TestDataBuilder.class);

                private final MetaField<MetaClass<?>, Integer> idField = register(new MetaField<>("id",metaType(int.class),false,this));

                private final MetaField<MetaClass<?>, String> contentField = register(new MetaField<>("content",metaType(java.lang.String.class),false,this));

                private final MetaField<MetaClass<?>, TestData.Inner> innerField = register(new MetaField<>("inner",metaType(io.art.tarantool.test.model.TestData.Inner.class),false,this));

                private final MetaIdMethod idMethod = register(new MetaIdMethod(this));

                private final MetaContentMethod contentMethod = register(new MetaContentMethod(this));

                private final MetaInnerMethod innerMethod = register(new MetaInnerMethod(this));

                private final MetaBuildMethod buildMethod = register(new MetaBuildMethod(this));

                private MetaTestDataBuilderClass() {
                  super(metaType(io.art.tarantool.test.model.TestData.TestDataBuilder.class));
                }

                public static MetaTestDataBuilderClass testDataBuilder() {
                  return self.get();
                }

                public MetaField<MetaClass<?>, Integer> idField() {
                  return idField;
                }

                public MetaField<MetaClass<?>, String> contentField() {
                  return contentField;
                }

                public MetaField<MetaClass<?>, TestData.Inner> innerField() {
                  return innerField;
                }

                public MetaIdMethod idMethod() {
                  return idMethod;
                }

                public MetaContentMethod contentMethod() {
                  return contentMethod;
                }

                public MetaInnerMethod innerMethod() {
                  return innerMethod;
                }

                public MetaBuildMethod buildMethod() {
                  return buildMethod;
                }

                public final class MetaIdMethod extends InstanceMetaMethod<MetaClass<?>, TestData.TestDataBuilder, TestData.TestDataBuilder> {
                  private final MetaParameter<java.lang.Integer> idParameter = register(new MetaParameter<>(0, "id",metaType(int.class)));

                  private MetaIdMethod(MetaClass owner) {
                    super("id",metaType(io.art.tarantool.test.model.TestData.TestDataBuilder.class),owner);
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.TestDataBuilder instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.id((int)(arguments[0]));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.TestDataBuilder instance,
                      java.lang.Object argument) throws Throwable {
                    return instance.id((int)(argument));
                  }

                  public MetaParameter<java.lang.Integer> idParameter() {
                    return idParameter;
                  }
                }

                public final class MetaContentMethod extends InstanceMetaMethod<MetaClass<?>, TestData.TestDataBuilder, TestData.TestDataBuilder> {
                  private final MetaParameter<java.lang.String> contentParameter = register(new MetaParameter<>(0, "content",metaType(java.lang.String.class)));

                  private MetaContentMethod(MetaClass owner) {
                    super("content",metaType(io.art.tarantool.test.model.TestData.TestDataBuilder.class),owner);
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.TestDataBuilder instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.content((java.lang.String)(arguments[0]));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.TestDataBuilder instance,
                      java.lang.Object argument) throws Throwable {
                    return instance.content((java.lang.String)(argument));
                  }

                  public MetaParameter<java.lang.String> contentParameter() {
                    return contentParameter;
                  }
                }

                public final class MetaInnerMethod extends InstanceMetaMethod<MetaClass<?>, TestData.TestDataBuilder, TestData.TestDataBuilder> {
                  private final MetaParameter<io.art.tarantool.test.model.TestData.Inner> innerParameter = register(new MetaParameter<>(0, "inner",metaType(io.art.tarantool.test.model.TestData.Inner.class)));

                  private MetaInnerMethod(MetaClass owner) {
                    super("inner",metaType(io.art.tarantool.test.model.TestData.TestDataBuilder.class),owner);
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.TestDataBuilder instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.inner((io.art.tarantool.test.model.TestData.Inner)(arguments[0]));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.TestDataBuilder instance,
                      java.lang.Object argument) throws Throwable {
                    return instance.inner((io.art.tarantool.test.model.TestData.Inner)(argument));
                  }

                  public MetaParameter<io.art.tarantool.test.model.TestData.Inner> innerParameter(
                      ) {
                    return innerParameter;
                  }
                }

                public final class MetaBuildMethod extends InstanceMetaMethod<MetaClass<?>, TestData.TestDataBuilder, TestData> {
                  private MetaBuildMethod(MetaClass owner) {
                    super("build",metaType(io.art.tarantool.test.model.TestData.class),owner);
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.TestDataBuilder instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.build();
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.TestDataBuilder instance) throws
                      Throwable {
                    return instance.build();
                  }
                }
              }

              public static final class MetaInnerClass extends MetaClass<io.art.tarantool.test.model.TestData.Inner> {
                private static final LazyProperty<MetaInnerClass> self = MetaClass.self(io.art.tarantool.test.model.TestData.Inner.class);

                private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor(this));

                private final MetaField<MetaClass<?>, String> contentField = register(new MetaField<>("content",metaType(java.lang.String.class),false,this));

                private final MetaToBuilderMethod toBuilderMethod = register(new MetaToBuilderMethod(this));

                private final MetaGetContentMethod getContentMethod = register(new MetaGetContentMethod(this));

                private final MetaInnerBuilderClass innerBuilderClass = register(new MetaInnerBuilderClass());

                private MetaInnerClass() {
                  super(metaType(io.art.tarantool.test.model.TestData.Inner.class));
                }

                public static MetaInnerClass inner() {
                  return self.get();
                }

                public MetaConstructorConstructor constructor() {
                  return constructor;
                }

                public MetaField<MetaClass<?>, String> contentField() {
                  return contentField;
                }

                public MetaToBuilderMethod toBuilderMethod() {
                  return toBuilderMethod;
                }

                public MetaGetContentMethod getContentMethod() {
                  return getContentMethod;
                }

                public MetaInnerBuilderClass innerBuilderClass() {
                  return innerBuilderClass;
                }

                public final class MetaConstructorConstructor extends MetaConstructor<MetaClass<?>, TestData.Inner> {
                  private final MetaParameter<java.lang.String> contentParameter = register(new MetaParameter<>(0, "content",metaType(java.lang.String.class)));

                  private MetaConstructorConstructor(MetaClass owner) {
                    super(metaType(io.art.tarantool.test.model.TestData.Inner.class),owner);
                  }

                  @Override
                  public io.art.tarantool.test.model.TestData.Inner invoke(
                      java.lang.Object[] arguments) throws Throwable {
                    return new io.art.tarantool.test.model.TestData.Inner((java.lang.String)(arguments[0]));
                  }

                  @Override
                  public io.art.tarantool.test.model.TestData.Inner invoke(
                      java.lang.Object argument) throws Throwable {
                    return new io.art.tarantool.test.model.TestData.Inner((java.lang.String)(argument));
                  }

                  public MetaParameter<java.lang.String> contentParameter() {
                    return contentParameter;
                  }
                }

                public final class MetaToBuilderMethod extends InstanceMetaMethod<MetaClass<?>, TestData.Inner, TestData.Inner.InnerBuilder> {
                  private MetaToBuilderMethod(MetaClass owner) {
                    super("toBuilder",metaType(io.art.tarantool.test.model.TestData.Inner.InnerBuilder.class),owner);
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.Inner instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.toBuilder();
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.Inner instance) throws Throwable {
                    return instance.toBuilder();
                  }
                }

                public final class MetaGetContentMethod extends InstanceMetaMethod<MetaClass<?>, TestData.Inner, String> {
                  private MetaGetContentMethod(MetaClass owner) {
                    super("getContent",metaType(java.lang.String.class),owner);
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.Inner instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getContent();
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.Inner instance) throws Throwable {
                    return instance.getContent();
                  }
                }

                public static final class MetaInnerBuilderClass extends MetaClass<io.art.tarantool.test.model.TestData.Inner.InnerBuilder> {
                  private static final LazyProperty<MetaInnerBuilderClass> self = MetaClass.self(io.art.tarantool.test.model.TestData.Inner.InnerBuilder.class);

                  private final MetaField<MetaClass<?>, String> contentField = register(new MetaField<>("content",metaType(java.lang.String.class),false,this));

                  private final MetaContentMethod contentMethod = register(new MetaContentMethod(this));

                  private final MetaBuildMethod buildMethod = register(new MetaBuildMethod(this));

                  private MetaInnerBuilderClass() {
                    super(metaType(io.art.tarantool.test.model.TestData.Inner.InnerBuilder.class));
                  }

                  public static MetaInnerBuilderClass innerBuilder() {
                    return self.get();
                  }

                  public MetaField<MetaClass<?>, String> contentField() {
                    return contentField;
                  }

                  public MetaContentMethod contentMethod() {
                    return contentMethod;
                  }

                  public MetaBuildMethod buildMethod() {
                    return buildMethod;
                  }

                  public final class MetaContentMethod extends InstanceMetaMethod<MetaClass<?>, TestData.Inner.InnerBuilder, TestData.Inner.InnerBuilder> {
                    private final MetaParameter<java.lang.String> contentParameter = register(new MetaParameter<>(0, "content",metaType(java.lang.String.class)));

                    private MetaContentMethod(MetaClass owner) {
                      super("content",metaType(io.art.tarantool.test.model.TestData.Inner.InnerBuilder.class),owner);
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.tarantool.test.model.TestData.Inner.InnerBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.content((java.lang.String)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.tarantool.test.model.TestData.Inner.InnerBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.content((java.lang.String)(argument));
                    }

                    public MetaParameter<java.lang.String> contentParameter() {
                      return contentParameter;
                    }
                  }

                  public final class MetaBuildMethod extends InstanceMetaMethod<MetaClass<?>, TestData.Inner.InnerBuilder, TestData.Inner> {
                    private MetaBuildMethod(MetaClass owner) {
                      super("build",metaType(io.art.tarantool.test.model.TestData.Inner.class),owner);
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.tarantool.test.model.TestData.Inner.InnerBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.build();
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.tarantool.test.model.TestData.Inner.InnerBuilder instance) throws
                        Throwable {
                      return instance.build();
                    }
                  }
                }
              }
            }

            public static final class MetaTestStorageClass extends MetaClass<io.art.tarantool.test.model.TestStorage> {
              private static final LazyProperty<MetaTestStorageClass> self = MetaClass.self(io.art.tarantool.test.model.TestStorage.class);

              private MetaTestStorageClass() {
                super(metaType(io.art.tarantool.test.model.TestStorage.class));
              }

              public static MetaTestStorageClass testStorage() {
                return self.get();
              }

              @Override
              public MetaProxy proxy(
                  Map<MetaMethod<MetaClass<?>, ?>, Function<java.lang.Object, java.lang.Object>> invocations) {
                return new MetaTestStorageProxy(invocations);
              }

              public class MetaTestStorageProxy extends MetaProxy implements io.art.tarantool.test.model.TestStorage {
                public MetaTestStorageProxy(
                    Map<MetaMethod<MetaClass<?>, ?>, Function<java.lang.Object, java.lang.Object>> invocations) {
                  super(invocations);
                }
              }
            }
          }
        }
      }
    }
  }
}
