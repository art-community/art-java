package io.art.tarantool.test.meta;

import static io.art.meta.model.MetaType.metaArray;
import static io.art.meta.model.MetaType.metaEnum;
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
          private final MetaModelPackage modelPackage = register(new MetaModelPackage());

          private MetaTestPackage() {
            super("test");
          }

          public MetaModelPackage modelPackage() {
            return modelPackage;
          }

          public static final class MetaModelPackage extends MetaPackage {
            private final MetaOtherSpaceClass otherSpaceClass = register(new MetaOtherSpaceClass());

            private final MetaTestDataClass testDataClass = register(new MetaTestDataClass());

            private final MetaTestStorageClass testStorageClass = register(new MetaTestStorageClass());

            private final MetaTestServiceClass testServiceClass = register(new MetaTestServiceClass());

            private MetaModelPackage() {
              super("model");
            }

            public MetaOtherSpaceClass otherSpaceClass() {
              return otherSpaceClass;
            }

            public MetaTestDataClass testDataClass() {
              return testDataClass;
            }

            public MetaTestStorageClass testStorageClass() {
              return testStorageClass;
            }

            public MetaTestServiceClass testServiceClass() {
              return testServiceClass;
            }

            public static final class MetaOtherSpaceClass extends MetaClass<io.art.tarantool.test.model.OtherSpace> {
              private static final LazyProperty<MetaOtherSpaceClass> self = MetaClass.self(io.art.tarantool.test.model.OtherSpace.class);

              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor(this));

              private final MetaField<MetaOtherSpaceClass, java.lang.Integer> keyField = register(new MetaField<>("key",metaType(int.class),false,this));

              private final MetaField<MetaOtherSpaceClass, java.lang.String> valueField = register(new MetaField<>("value",metaType(java.lang.String.class),false,this));

              private final MetaField<MetaOtherSpaceClass, java.lang.Integer> numberField = register(new MetaField<>("number",metaType(int.class),false,this));

              private final MetaToBuilderMethod toBuilderMethod = register(new MetaToBuilderMethod(this));

              private final MetaGetKeyMethod getKeyMethod = register(new MetaGetKeyMethod(this));

              private final MetaGetValueMethod getValueMethod = register(new MetaGetValueMethod(this));

              private final MetaGetNumberMethod getNumberMethod = register(new MetaGetNumberMethod(this));

              private final MetaOtherSpaceBuilderClass otherSpaceBuilderClass = register(new MetaOtherSpaceBuilderClass());

              private MetaOtherSpaceClass() {
                super(metaType(io.art.tarantool.test.model.OtherSpace.class));
              }

              public static MetaOtherSpaceClass otherSpace() {
                return self.get();
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaField<MetaOtherSpaceClass, java.lang.Integer> keyField() {
                return keyField;
              }

              public MetaField<MetaOtherSpaceClass, java.lang.String> valueField() {
                return valueField;
              }

              public MetaField<MetaOtherSpaceClass, java.lang.Integer> numberField() {
                return numberField;
              }

              public MetaToBuilderMethod toBuilderMethod() {
                return toBuilderMethod;
              }

              public MetaGetKeyMethod getKeyMethod() {
                return getKeyMethod;
              }

              public MetaGetValueMethod getValueMethod() {
                return getValueMethod;
              }

              public MetaGetNumberMethod getNumberMethod() {
                return getNumberMethod;
              }

              public MetaOtherSpaceBuilderClass otherSpaceBuilderClass() {
                return otherSpaceBuilderClass;
              }

              public final class MetaConstructorConstructor extends MetaConstructor<MetaOtherSpaceClass, io.art.tarantool.test.model.OtherSpace> {
                private final MetaParameter<java.lang.Integer> keyParameter = register(new MetaParameter<>(0, "key",metaType(int.class)));

                private final MetaParameter<java.lang.String> valueParameter = register(new MetaParameter<>(1, "value",metaType(java.lang.String.class)));

                private final MetaParameter<java.lang.Integer> numberParameter = register(new MetaParameter<>(2, "number",metaType(int.class)));

                private MetaConstructorConstructor(MetaOtherSpaceClass owner) {
                  super(metaType(io.art.tarantool.test.model.OtherSpace.class),owner);
                }

                @Override
                public io.art.tarantool.test.model.OtherSpace invoke(java.lang.Object[] arguments)
                    throws Throwable {
                  return new io.art.tarantool.test.model.OtherSpace((int)(arguments[0]),(java.lang.String)(arguments[1]),(int)(arguments[2]));
                }

                public MetaParameter<java.lang.Integer> keyParameter() {
                  return keyParameter;
                }

                public MetaParameter<java.lang.String> valueParameter() {
                  return valueParameter;
                }

                public MetaParameter<java.lang.Integer> numberParameter() {
                  return numberParameter;
                }
              }

              public final class MetaToBuilderMethod extends InstanceMetaMethod<MetaOtherSpaceClass, io.art.tarantool.test.model.OtherSpace, io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder> {
                private MetaToBuilderMethod(MetaOtherSpaceClass owner) {
                  super("toBuilder",metaType(io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder.class),owner);
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.OtherSpace instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.toBuilder();
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.OtherSpace instance)
                    throws Throwable {
                  return instance.toBuilder();
                }
              }

              public final class MetaGetKeyMethod extends InstanceMetaMethod<MetaOtherSpaceClass, io.art.tarantool.test.model.OtherSpace, java.lang.Integer> {
                private MetaGetKeyMethod(MetaOtherSpaceClass owner) {
                  super("getKey",metaType(int.class),owner);
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.OtherSpace instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.getKey();
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.OtherSpace instance)
                    throws Throwable {
                  return instance.getKey();
                }
              }

              public final class MetaGetValueMethod extends InstanceMetaMethod<MetaOtherSpaceClass, io.art.tarantool.test.model.OtherSpace, java.lang.String> {
                private MetaGetValueMethod(MetaOtherSpaceClass owner) {
                  super("getValue",metaType(java.lang.String.class),owner);
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.OtherSpace instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.getValue();
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.OtherSpace instance)
                    throws Throwable {
                  return instance.getValue();
                }
              }

              public final class MetaGetNumberMethod extends InstanceMetaMethod<MetaOtherSpaceClass, io.art.tarantool.test.model.OtherSpace, java.lang.Integer> {
                private MetaGetNumberMethod(MetaOtherSpaceClass owner) {
                  super("getNumber",metaType(int.class),owner);
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.OtherSpace instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.getNumber();
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.OtherSpace instance)
                    throws Throwable {
                  return instance.getNumber();
                }
              }

              public static final class MetaOtherSpaceBuilderClass extends MetaClass<io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder> {
                private static final LazyProperty<MetaOtherSpaceBuilderClass> self = MetaClass.self(io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder.class);

                private final MetaField<MetaOtherSpaceBuilderClass, java.lang.Integer> keyField = register(new MetaField<>("key",metaType(int.class),false,this));

                private final MetaField<MetaOtherSpaceBuilderClass, java.lang.String> valueField = register(new MetaField<>("value",metaType(java.lang.String.class),false,this));

                private final MetaField<MetaOtherSpaceBuilderClass, java.lang.Integer> numberField = register(new MetaField<>("number",metaType(int.class),false,this));

                private final MetaKeyMethod keyMethod = register(new MetaKeyMethod(this));

                private final MetaValueMethod valueMethod = register(new MetaValueMethod(this));

                private final MetaNumberMethod numberMethod = register(new MetaNumberMethod(this));

                private final MetaBuildMethod buildMethod = register(new MetaBuildMethod(this));

                private MetaOtherSpaceBuilderClass() {
                  super(metaType(io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder.class));
                }

                public static MetaOtherSpaceBuilderClass otherSpaceBuilder() {
                  return self.get();
                }

                public MetaField<MetaOtherSpaceBuilderClass, java.lang.Integer> keyField() {
                  return keyField;
                }

                public MetaField<MetaOtherSpaceBuilderClass, java.lang.String> valueField() {
                  return valueField;
                }

                public MetaField<MetaOtherSpaceBuilderClass, java.lang.Integer> numberField() {
                  return numberField;
                }

                public MetaKeyMethod keyMethod() {
                  return keyMethod;
                }

                public MetaValueMethod valueMethod() {
                  return valueMethod;
                }

                public MetaNumberMethod numberMethod() {
                  return numberMethod;
                }

                public MetaBuildMethod buildMethod() {
                  return buildMethod;
                }

                public final class MetaKeyMethod extends InstanceMetaMethod<MetaOtherSpaceBuilderClass, io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder, io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder> {
                  private final MetaParameter<java.lang.Integer> keyParameter = register(new MetaParameter<>(0, "key",metaType(int.class)));

                  private MetaKeyMethod(MetaOtherSpaceBuilderClass owner) {
                    super("key",metaType(io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder.class),owner);
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.key((int)(arguments[0]));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder instance,
                      java.lang.Object argument) throws Throwable {
                    return instance.key((int)(argument));
                  }

                  public MetaParameter<java.lang.Integer> keyParameter() {
                    return keyParameter;
                  }
                }

                public final class MetaValueMethod extends InstanceMetaMethod<MetaOtherSpaceBuilderClass, io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder, io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder> {
                  private final MetaParameter<java.lang.String> valueParameter = register(new MetaParameter<>(0, "value",metaType(java.lang.String.class)));

                  private MetaValueMethod(MetaOtherSpaceBuilderClass owner) {
                    super("value",metaType(io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder.class),owner);
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.value((java.lang.String)(arguments[0]));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder instance,
                      java.lang.Object argument) throws Throwable {
                    return instance.value((java.lang.String)(argument));
                  }

                  public MetaParameter<java.lang.String> valueParameter() {
                    return valueParameter;
                  }
                }

                public final class MetaNumberMethod extends InstanceMetaMethod<MetaOtherSpaceBuilderClass, io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder, io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder> {
                  private final MetaParameter<java.lang.Integer> numberParameter = register(new MetaParameter<>(0, "number",metaType(int.class)));

                  private MetaNumberMethod(MetaOtherSpaceBuilderClass owner) {
                    super("number",metaType(io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder.class),owner);
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.number((int)(arguments[0]));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder instance,
                      java.lang.Object argument) throws Throwable {
                    return instance.number((int)(argument));
                  }

                  public MetaParameter<java.lang.Integer> numberParameter() {
                    return numberParameter;
                  }
                }

                public final class MetaBuildMethod extends InstanceMetaMethod<MetaOtherSpaceBuilderClass, io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder, io.art.tarantool.test.model.OtherSpace> {
                  private MetaBuildMethod(MetaOtherSpaceBuilderClass owner) {
                    super("build",metaType(io.art.tarantool.test.model.OtherSpace.class),owner);
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.build();
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.OtherSpace.OtherSpaceBuilder instance) throws
                      Throwable {
                    return instance.build();
                  }
                }
              }
            }

            public static final class MetaTestDataClass extends MetaClass<io.art.tarantool.test.model.TestData> {
              private static final LazyProperty<MetaTestDataClass> self = MetaClass.self(io.art.tarantool.test.model.TestData.class);

              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor(this));

              private final MetaField<MetaTestDataClass, java.lang.Integer> idField = register(new MetaField<>("id",metaType(int.class),false,this));

              private final MetaField<MetaTestDataClass, java.lang.String> contentField = register(new MetaField<>("content",metaType(java.lang.String.class),false,this));

              private final MetaField<MetaTestDataClass, io.art.tarantool.test.model.TestData.Inner> innerField = register(new MetaField<>("inner",metaType(io.art.tarantool.test.model.TestData.Inner.class),false,this));

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

              public MetaField<MetaTestDataClass, java.lang.Integer> idField() {
                return idField;
              }

              public MetaField<MetaTestDataClass, java.lang.String> contentField() {
                return contentField;
              }

              public MetaField<MetaTestDataClass, io.art.tarantool.test.model.TestData.Inner> innerField(
                  ) {
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

              public final class MetaConstructorConstructor extends MetaConstructor<MetaTestDataClass, io.art.tarantool.test.model.TestData> {
                private final MetaParameter<java.lang.Integer> idParameter = register(new MetaParameter<>(0, "id",metaType(int.class)));

                private final MetaParameter<java.lang.String> contentParameter = register(new MetaParameter<>(1, "content",metaType(java.lang.String.class)));

                private final MetaParameter<io.art.tarantool.test.model.TestData.Inner> innerParameter = register(new MetaParameter<>(2, "inner",metaType(io.art.tarantool.test.model.TestData.Inner.class)));

                private MetaConstructorConstructor(MetaTestDataClass owner) {
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

              public final class MetaToBuilderMethod extends InstanceMetaMethod<MetaTestDataClass, io.art.tarantool.test.model.TestData, io.art.tarantool.test.model.TestData.TestDataBuilder> {
                private MetaToBuilderMethod(MetaTestDataClass owner) {
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

              public final class MetaGetIdMethod extends InstanceMetaMethod<MetaTestDataClass, io.art.tarantool.test.model.TestData, java.lang.Integer> {
                private MetaGetIdMethod(MetaTestDataClass owner) {
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

              public final class MetaGetContentMethod extends InstanceMetaMethod<MetaTestDataClass, io.art.tarantool.test.model.TestData, java.lang.String> {
                private MetaGetContentMethod(MetaTestDataClass owner) {
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

              public final class MetaGetInnerMethod extends InstanceMetaMethod<MetaTestDataClass, io.art.tarantool.test.model.TestData, io.art.tarantool.test.model.TestData.Inner> {
                private MetaGetInnerMethod(MetaTestDataClass owner) {
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

                private final MetaField<MetaTestDataBuilderClass, java.lang.Integer> idField = register(new MetaField<>("id",metaType(int.class),false,this));

                private final MetaField<MetaTestDataBuilderClass, java.lang.String> contentField = register(new MetaField<>("content",metaType(java.lang.String.class),false,this));

                private final MetaField<MetaTestDataBuilderClass, io.art.tarantool.test.model.TestData.Inner> innerField = register(new MetaField<>("inner",metaType(io.art.tarantool.test.model.TestData.Inner.class),false,this));

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

                public MetaField<MetaTestDataBuilderClass, java.lang.Integer> idField() {
                  return idField;
                }

                public MetaField<MetaTestDataBuilderClass, java.lang.String> contentField() {
                  return contentField;
                }

                public MetaField<MetaTestDataBuilderClass, io.art.tarantool.test.model.TestData.Inner> innerField(
                    ) {
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

                public final class MetaIdMethod extends InstanceMetaMethod<MetaTestDataBuilderClass, io.art.tarantool.test.model.TestData.TestDataBuilder, io.art.tarantool.test.model.TestData.TestDataBuilder> {
                  private final MetaParameter<java.lang.Integer> idParameter = register(new MetaParameter<>(0, "id",metaType(int.class)));

                  private MetaIdMethod(MetaTestDataBuilderClass owner) {
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

                public final class MetaContentMethod extends InstanceMetaMethod<MetaTestDataBuilderClass, io.art.tarantool.test.model.TestData.TestDataBuilder, io.art.tarantool.test.model.TestData.TestDataBuilder> {
                  private final MetaParameter<java.lang.String> contentParameter = register(new MetaParameter<>(0, "content",metaType(java.lang.String.class)));

                  private MetaContentMethod(MetaTestDataBuilderClass owner) {
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

                public final class MetaInnerMethod extends InstanceMetaMethod<MetaTestDataBuilderClass, io.art.tarantool.test.model.TestData.TestDataBuilder, io.art.tarantool.test.model.TestData.TestDataBuilder> {
                  private final MetaParameter<io.art.tarantool.test.model.TestData.Inner> innerParameter = register(new MetaParameter<>(0, "inner",metaType(io.art.tarantool.test.model.TestData.Inner.class)));

                  private MetaInnerMethod(MetaTestDataBuilderClass owner) {
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

                public final class MetaBuildMethod extends InstanceMetaMethod<MetaTestDataBuilderClass, io.art.tarantool.test.model.TestData.TestDataBuilder, io.art.tarantool.test.model.TestData> {
                  private MetaBuildMethod(MetaTestDataBuilderClass owner) {
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

                private final MetaField<MetaInnerClass, java.lang.String> contentField = register(new MetaField<>("content",metaType(java.lang.String.class),false,this));

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

                public MetaField<MetaInnerClass, java.lang.String> contentField() {
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

                public final class MetaConstructorConstructor extends MetaConstructor<MetaInnerClass, io.art.tarantool.test.model.TestData.Inner> {
                  private final MetaParameter<java.lang.String> contentParameter = register(new MetaParameter<>(0, "content",metaType(java.lang.String.class)));

                  private MetaConstructorConstructor(MetaInnerClass owner) {
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

                public final class MetaToBuilderMethod extends InstanceMetaMethod<MetaInnerClass, io.art.tarantool.test.model.TestData.Inner, io.art.tarantool.test.model.TestData.Inner.InnerBuilder> {
                  private MetaToBuilderMethod(MetaInnerClass owner) {
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

                public final class MetaGetContentMethod extends InstanceMetaMethod<MetaInnerClass, io.art.tarantool.test.model.TestData.Inner, java.lang.String> {
                  private MetaGetContentMethod(MetaInnerClass owner) {
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

                  private final MetaField<MetaInnerBuilderClass, java.lang.String> contentField = register(new MetaField<>("content",metaType(java.lang.String.class),false,this));

                  private final MetaContentMethod contentMethod = register(new MetaContentMethod(this));

                  private final MetaBuildMethod buildMethod = register(new MetaBuildMethod(this));

                  private MetaInnerBuilderClass() {
                    super(metaType(io.art.tarantool.test.model.TestData.Inner.InnerBuilder.class));
                  }

                  public static MetaInnerBuilderClass innerBuilder() {
                    return self.get();
                  }

                  public MetaField<MetaInnerBuilderClass, java.lang.String> contentField() {
                    return contentField;
                  }

                  public MetaContentMethod contentMethod() {
                    return contentMethod;
                  }

                  public MetaBuildMethod buildMethod() {
                    return buildMethod;
                  }

                  public final class MetaContentMethod extends InstanceMetaMethod<MetaInnerBuilderClass, io.art.tarantool.test.model.TestData.Inner.InnerBuilder, io.art.tarantool.test.model.TestData.Inner.InnerBuilder> {
                    private final MetaParameter<java.lang.String> contentParameter = register(new MetaParameter<>(0, "content",metaType(java.lang.String.class)));

                    private MetaContentMethod(MetaInnerBuilderClass owner) {
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

                  public final class MetaBuildMethod extends InstanceMetaMethod<MetaInnerBuilderClass, io.art.tarantool.test.model.TestData.Inner.InnerBuilder, io.art.tarantool.test.model.TestData.Inner> {
                    private MetaBuildMethod(MetaInnerBuilderClass owner) {
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

              private final MetaTestSubscriptionMethod testSubscriptionMethod = register(new MetaTestSubscriptionMethod(this));

              private final MetaTestChannelMethod testChannelMethod = register(new MetaTestChannelMethod(this));

              private final MetaTestMapperMethod testMapperMethod = register(new MetaTestMapperMethod(this));

              private final MetaTestFilterMethod testFilterMethod = register(new MetaTestFilterMethod(this));

              private final MetaTestModelIndexesMethod testModelIndexesMethod = register(new MetaTestModelIndexesMethod(this));

              private final MetaOtherSpaceIndexesMethod otherSpaceIndexesMethod = register(new MetaOtherSpaceIndexesMethod(this));

              private final MetaOtherSpaceIndexesClass otherSpaceIndexesClass = register(new MetaOtherSpaceIndexesClass());

              private final MetaTestModelIndexesClass testModelIndexesClass = register(new MetaTestModelIndexesClass());

              private MetaTestStorageClass() {
                super(metaType(io.art.tarantool.test.model.TestStorage.class));
              }

              public static MetaTestStorageClass testStorage() {
                return self.get();
              }

              public MetaTestSubscriptionMethod testSubscriptionMethod() {
                return testSubscriptionMethod;
              }

              public MetaTestChannelMethod testChannelMethod() {
                return testChannelMethod;
              }

              public MetaTestMapperMethod testMapperMethod() {
                return testMapperMethod;
              }

              public MetaTestFilterMethod testFilterMethod() {
                return testFilterMethod;
              }

              public MetaTestModelIndexesMethod testModelIndexesMethod() {
                return testModelIndexesMethod;
              }

              public MetaOtherSpaceIndexesMethod otherSpaceIndexesMethod() {
                return otherSpaceIndexesMethod;
              }

              @Override
              public MetaProxy proxy(
                  Map<MetaMethod<MetaClass<?>, ?>, Function<java.lang.Object, java.lang.Object>> invocations) {
                return new MetaTestStorageProxy(invocations);
              }

              public MetaOtherSpaceIndexesClass otherSpaceIndexesClass() {
                return otherSpaceIndexesClass;
              }

              public MetaTestModelIndexesClass testModelIndexesClass() {
                return testModelIndexesClass;
              }

              public final class MetaTestSubscriptionMethod extends InstanceMetaMethod<MetaTestStorageClass, io.art.tarantool.test.model.TestStorage, Void> {
                private MetaTestSubscriptionMethod(MetaTestStorageClass owner) {
                  super("testSubscription",metaType(Void.class),owner);
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestStorage instance,
                    java.lang.Object[] arguments) throws Throwable {
                  instance.testSubscription();
                  return null;
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestStorage instance)
                    throws Throwable {
                  instance.testSubscription();
                  return null;
                }
              }

              public final class MetaTestChannelMethod extends InstanceMetaMethod<MetaTestStorageClass, io.art.tarantool.test.model.TestStorage, reactor.core.publisher.Flux<java.lang.String>> {
                private MetaTestChannelMethod(MetaTestStorageClass owner) {
                  super("testChannel",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestStorage instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.testChannel();
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestStorage instance)
                    throws Throwable {
                  return instance.testChannel();
                }
              }

              public final class MetaTestMapperMethod extends InstanceMetaMethod<MetaTestStorageClass, io.art.tarantool.test.model.TestStorage, java.lang.String> {
                private final MetaParameter<io.art.tarantool.test.model.TestData> inputParameter = register(new MetaParameter<>(0, "input",metaType(io.art.tarantool.test.model.TestData.class)));

                private MetaTestMapperMethod(MetaTestStorageClass owner) {
                  super("testMapper",metaType(java.lang.String.class),owner);
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestStorage instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.testMapper((io.art.tarantool.test.model.TestData)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestStorage instance,
                    java.lang.Object argument) throws Throwable {
                  return instance.testMapper((io.art.tarantool.test.model.TestData)(argument));
                }

                public MetaParameter<io.art.tarantool.test.model.TestData> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaTestFilterMethod extends InstanceMetaMethod<MetaTestStorageClass, io.art.tarantool.test.model.TestStorage, Boolean> {
                private final MetaParameter<io.art.tarantool.test.model.TestData> inputParameter = register(new MetaParameter<>(0, "input",metaType(io.art.tarantool.test.model.TestData.class)));

                private MetaTestFilterMethod(MetaTestStorageClass owner) {
                  super("testFilter",metaType(boolean.class),owner);
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestStorage instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.testFilter((io.art.tarantool.test.model.TestData)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestStorage instance,
                    java.lang.Object argument) throws Throwable {
                  return instance.testFilter((io.art.tarantool.test.model.TestData)(argument));
                }

                public MetaParameter<io.art.tarantool.test.model.TestData> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaTestModelIndexesMethod extends StaticMetaMethod<MetaTestStorageClass, io.art.tarantool.test.model.TestStorage.TestModelIndexes> {
                private MetaTestModelIndexesMethod(MetaTestStorageClass owner) {
                  super("testModelIndexes",metaType(io.art.tarantool.test.model.TestStorage.TestModelIndexes.class),owner);
                }

                @Override
                public java.lang.Object invoke(java.lang.Object[] arguments) throws Throwable {
                  return io.art.tarantool.test.model.TestStorage.testModelIndexes();
                }

                @Override
                public java.lang.Object invoke() throws Throwable {
                  return io.art.tarantool.test.model.TestStorage.testModelIndexes();
                }
              }

              public final class MetaOtherSpaceIndexesMethod extends StaticMetaMethod<MetaTestStorageClass, io.art.tarantool.test.model.TestStorage.OtherSpaceIndexes> {
                private MetaOtherSpaceIndexesMethod(MetaTestStorageClass owner) {
                  super("otherSpaceIndexes",metaType(io.art.tarantool.test.model.TestStorage.OtherSpaceIndexes.class),owner);
                }

                @Override
                public java.lang.Object invoke(java.lang.Object[] arguments) throws Throwable {
                  return io.art.tarantool.test.model.TestStorage.otherSpaceIndexes();
                }

                @Override
                public java.lang.Object invoke() throws Throwable {
                  return io.art.tarantool.test.model.TestStorage.otherSpaceIndexes();
                }
              }

              public class MetaTestStorageProxy extends MetaProxy implements io.art.tarantool.test.model.TestStorage {
                private final Function<java.lang.Object, java.lang.Object> testSubscriptionInvocation;

                private final Function<java.lang.Object, java.lang.Object> testChannelInvocation;

                private final Function<java.lang.Object, java.lang.Object> testMapperInvocation;

                private final Function<java.lang.Object, java.lang.Object> testFilterInvocation;

                public MetaTestStorageProxy(
                    Map<MetaMethod<MetaClass<?>, ?>, Function<java.lang.Object, java.lang.Object>> invocations) {
                  super(invocations);
                  testSubscriptionInvocation = invocations.get(testSubscriptionMethod);
                  testChannelInvocation = invocations.get(testChannelMethod);
                  testMapperInvocation = invocations.get(testMapperMethod);
                  testFilterInvocation = invocations.get(testFilterMethod);
                }

                @Override
                public void testSubscription() {
                  testSubscriptionInvocation.apply(null);
                }

                @Override
                public reactor.core.publisher.Flux<java.lang.String> testChannel() {
                  return (reactor.core.publisher.Flux<java.lang.String>)(testChannelInvocation.apply(null));
                }

                @Override
                public java.lang.String testMapper(io.art.tarantool.test.model.TestData input) {
                  return (java.lang.String)(testMapperInvocation.apply(input));
                }

                @Override
                public boolean testFilter(io.art.tarantool.test.model.TestData input) {
                  return (boolean)(testFilterInvocation.apply(input));
                }
              }

              public static final class MetaOtherSpaceIndexesClass extends MetaClass<io.art.tarantool.test.model.TestStorage.OtherSpaceIndexes> {
                private static final LazyProperty<MetaOtherSpaceIndexesClass> self = MetaClass.self(io.art.tarantool.test.model.TestStorage.OtherSpaceIndexes.class);

                private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor(this));

                private final MetaField<MetaOtherSpaceIndexesClass, io.art.storage.index.Index1<io.art.tarantool.test.model.OtherSpace, java.lang.Integer>> idField = register(new MetaField<>("id",metaType(io.art.storage.index.Index1.class,metaType(io.art.tarantool.test.model.OtherSpace.class),metaType(java.lang.Integer.class)),false,this));

                private final MetaField<MetaOtherSpaceIndexesClass, io.art.storage.index.Index2<io.art.tarantool.test.model.OtherSpace, java.lang.String, java.lang.Integer>> valueNumberField = register(new MetaField<>("valueNumber",metaType(io.art.storage.index.Index2.class,metaType(io.art.tarantool.test.model.OtherSpace.class),metaType(java.lang.String.class),metaType(java.lang.Integer.class)),false,this));

                private final MetaIdMethod idMethod = register(new MetaIdMethod(this));

                private final MetaValueNumberMethod valueNumberMethod = register(new MetaValueNumberMethod(this));

                private MetaOtherSpaceIndexesClass() {
                  super(metaType(io.art.tarantool.test.model.TestStorage.OtherSpaceIndexes.class));
                }

                public static MetaOtherSpaceIndexesClass otherSpaceIndexes() {
                  return self.get();
                }

                public MetaConstructorConstructor constructor() {
                  return constructor;
                }

                public MetaField<MetaOtherSpaceIndexesClass, io.art.storage.index.Index1<io.art.tarantool.test.model.OtherSpace, java.lang.Integer>> idField(
                    ) {
                  return idField;
                }

                public MetaField<MetaOtherSpaceIndexesClass, io.art.storage.index.Index2<io.art.tarantool.test.model.OtherSpace, java.lang.String, java.lang.Integer>> valueNumberField(
                    ) {
                  return valueNumberField;
                }

                public MetaIdMethod idMethod() {
                  return idMethod;
                }

                public MetaValueNumberMethod valueNumberMethod() {
                  return valueNumberMethod;
                }

                public final class MetaConstructorConstructor extends MetaConstructor<MetaOtherSpaceIndexesClass, io.art.tarantool.test.model.TestStorage.OtherSpaceIndexes> {
                  private MetaConstructorConstructor(MetaOtherSpaceIndexesClass owner) {
                    super(metaType(io.art.tarantool.test.model.TestStorage.OtherSpaceIndexes.class),owner);
                  }

                  @Override
                  public io.art.tarantool.test.model.TestStorage.OtherSpaceIndexes invoke(
                      java.lang.Object[] arguments) throws Throwable {
                    return new io.art.tarantool.test.model.TestStorage.OtherSpaceIndexes();
                  }

                  @Override
                  public io.art.tarantool.test.model.TestStorage.OtherSpaceIndexes invoke() throws
                      Throwable {
                    return new io.art.tarantool.test.model.TestStorage.OtherSpaceIndexes();
                  }
                }

                public final class MetaIdMethod extends InstanceMetaMethod<MetaOtherSpaceIndexesClass, io.art.tarantool.test.model.TestStorage.OtherSpaceIndexes, io.art.storage.index.Index1<io.art.tarantool.test.model.OtherSpace, java.lang.Integer>> {
                  private MetaIdMethod(MetaOtherSpaceIndexesClass owner) {
                    super("id",metaType(io.art.storage.index.Index1.class,metaType(io.art.tarantool.test.model.OtherSpace.class),metaType(java.lang.Integer.class)),owner);
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestStorage.OtherSpaceIndexes instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.id();
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestStorage.OtherSpaceIndexes instance) throws
                      Throwable {
                    return instance.id();
                  }
                }

                public final class MetaValueNumberMethod extends InstanceMetaMethod<MetaOtherSpaceIndexesClass, io.art.tarantool.test.model.TestStorage.OtherSpaceIndexes, io.art.storage.index.Index2<io.art.tarantool.test.model.OtherSpace, java.lang.String, java.lang.Integer>> {
                  private MetaValueNumberMethod(MetaOtherSpaceIndexesClass owner) {
                    super("valueNumber",metaType(io.art.storage.index.Index2.class,metaType(io.art.tarantool.test.model.OtherSpace.class),metaType(java.lang.String.class),metaType(java.lang.Integer.class)),owner);
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestStorage.OtherSpaceIndexes instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.valueNumber();
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestStorage.OtherSpaceIndexes instance) throws
                      Throwable {
                    return instance.valueNumber();
                  }
                }
              }

              public static final class MetaTestModelIndexesClass extends MetaClass<io.art.tarantool.test.model.TestStorage.TestModelIndexes> {
                private static final LazyProperty<MetaTestModelIndexesClass> self = MetaClass.self(io.art.tarantool.test.model.TestStorage.TestModelIndexes.class);

                private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor(this));

                private final MetaField<MetaTestModelIndexesClass, io.art.storage.index.Index1<io.art.meta.test.TestingMetaModel, java.lang.Integer>> idField = register(new MetaField<>("id",metaType(io.art.storage.index.Index1.class,metaType(io.art.meta.test.TestingMetaModel.class),metaType(java.lang.Integer.class)),false,this));

                private final MetaField<MetaTestModelIndexesClass, io.art.storage.index.Index2<io.art.meta.test.TestingMetaModel, java.lang.Integer, java.lang.String>> f9f16Field = register(new MetaField<>("f9f16",metaType(io.art.storage.index.Index2.class,metaType(io.art.meta.test.TestingMetaModel.class),metaType(java.lang.Integer.class),metaType(java.lang.String.class)),false,this));

                private final MetaIdMethod idMethod = register(new MetaIdMethod(this));

                private final MetaF9f16Method f9f16Method = register(new MetaF9f16Method(this));

                private MetaTestModelIndexesClass() {
                  super(metaType(io.art.tarantool.test.model.TestStorage.TestModelIndexes.class));
                }

                public static MetaTestModelIndexesClass testModelIndexes() {
                  return self.get();
                }

                public MetaConstructorConstructor constructor() {
                  return constructor;
                }

                public MetaField<MetaTestModelIndexesClass, io.art.storage.index.Index1<io.art.meta.test.TestingMetaModel, java.lang.Integer>> idField(
                    ) {
                  return idField;
                }

                public MetaField<MetaTestModelIndexesClass, io.art.storage.index.Index2<io.art.meta.test.TestingMetaModel, java.lang.Integer, java.lang.String>> f9f16Field(
                    ) {
                  return f9f16Field;
                }

                public MetaIdMethod idMethod() {
                  return idMethod;
                }

                public MetaF9f16Method f9f16Method() {
                  return f9f16Method;
                }

                public final class MetaConstructorConstructor extends MetaConstructor<MetaTestModelIndexesClass, io.art.tarantool.test.model.TestStorage.TestModelIndexes> {
                  private MetaConstructorConstructor(MetaTestModelIndexesClass owner) {
                    super(metaType(io.art.tarantool.test.model.TestStorage.TestModelIndexes.class),owner);
                  }

                  @Override
                  public io.art.tarantool.test.model.TestStorage.TestModelIndexes invoke(
                      java.lang.Object[] arguments) throws Throwable {
                    return new io.art.tarantool.test.model.TestStorage.TestModelIndexes();
                  }

                  @Override
                  public io.art.tarantool.test.model.TestStorage.TestModelIndexes invoke() throws
                      Throwable {
                    return new io.art.tarantool.test.model.TestStorage.TestModelIndexes();
                  }
                }

                public final class MetaIdMethod extends InstanceMetaMethod<MetaTestModelIndexesClass, io.art.tarantool.test.model.TestStorage.TestModelIndexes, io.art.storage.index.Index1<io.art.meta.test.TestingMetaModel, java.lang.Integer>> {
                  private MetaIdMethod(MetaTestModelIndexesClass owner) {
                    super("id",metaType(io.art.storage.index.Index1.class,metaType(io.art.meta.test.TestingMetaModel.class),metaType(java.lang.Integer.class)),owner);
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestStorage.TestModelIndexes instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.id();
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestStorage.TestModelIndexes instance) throws
                      Throwable {
                    return instance.id();
                  }
                }

                public final class MetaF9f16Method extends InstanceMetaMethod<MetaTestModelIndexesClass, io.art.tarantool.test.model.TestStorage.TestModelIndexes, io.art.storage.index.Index2<io.art.meta.test.TestingMetaModel, java.lang.Integer, java.lang.String>> {
                  private MetaF9f16Method(MetaTestModelIndexesClass owner) {
                    super("f9f16",metaType(io.art.storage.index.Index2.class,metaType(io.art.meta.test.TestingMetaModel.class),metaType(java.lang.Integer.class),metaType(java.lang.String.class)),owner);
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestStorage.TestModelIndexes instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.f9f16();
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestStorage.TestModelIndexes instance) throws
                      Throwable {
                    return instance.f9f16();
                  }
                }
              }
            }

            public static final class MetaTestServiceClass extends MetaClass<io.art.tarantool.test.model.TestService> {
              private static final LazyProperty<MetaTestServiceClass> self = MetaClass.self(io.art.tarantool.test.model.TestService.class);

              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor(this));

              private final MetaField<MetaTestServiceClass, java.util.concurrent.CountDownLatch> WAITERField = register(new MetaField<>("WAITER",metaType(java.util.concurrent.CountDownLatch.class),false,this));

              private final MetaAwaitMethod awaitMethod = register(new MetaAwaitMethod(this));

              private final MetaTestEmptyMethod testEmptyMethod = register(new MetaTestEmptyMethod(this));

              private final MetaTestRequestMethod testRequestMethod = register(new MetaTestRequestMethod(this));

              private final MetaTestChannelMethod testChannelMethod = register(new MetaTestChannelMethod(this));

              private final MetaTestRequestClass testRequestClass = register(new MetaTestRequestClass());

              private MetaTestServiceClass() {
                super(metaType(io.art.tarantool.test.model.TestService.class));
              }

              public static MetaTestServiceClass testService() {
                return self.get();
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaField<MetaTestServiceClass, java.util.concurrent.CountDownLatch> WAITERField(
                  ) {
                return WAITERField;
              }

              public MetaAwaitMethod awaitMethod() {
                return awaitMethod;
              }

              public MetaTestEmptyMethod testEmptyMethod() {
                return testEmptyMethod;
              }

              public MetaTestRequestMethod testRequestMethod() {
                return testRequestMethod;
              }

              public MetaTestChannelMethod testChannelMethod() {
                return testChannelMethod;
              }

              public MetaTestRequestClass testRequestClass() {
                return testRequestClass;
              }

              public final class MetaConstructorConstructor extends MetaConstructor<MetaTestServiceClass, io.art.tarantool.test.model.TestService> {
                private MetaConstructorConstructor(MetaTestServiceClass owner) {
                  super(metaType(io.art.tarantool.test.model.TestService.class),owner);
                }

                @Override
                public io.art.tarantool.test.model.TestService invoke(java.lang.Object[] arguments)
                    throws Throwable {
                  return new io.art.tarantool.test.model.TestService();
                }

                @Override
                public io.art.tarantool.test.model.TestService invoke() throws Throwable {
                  return new io.art.tarantool.test.model.TestService();
                }
              }

              public final class MetaAwaitMethod extends StaticMetaMethod<MetaTestServiceClass, Boolean> {
                private MetaAwaitMethod(MetaTestServiceClass owner) {
                  super("await",metaType(boolean.class),owner);
                }

                @Override
                public java.lang.Object invoke(java.lang.Object[] arguments) throws Throwable {
                  return io.art.tarantool.test.model.TestService.await();
                }

                @Override
                public java.lang.Object invoke() throws Throwable {
                  return io.art.tarantool.test.model.TestService.await();
                }
              }

              public final class MetaTestEmptyMethod extends InstanceMetaMethod<MetaTestServiceClass, io.art.tarantool.test.model.TestService, Void> {
                private MetaTestEmptyMethod(MetaTestServiceClass owner) {
                  super("testEmpty",metaType(Void.class),owner);
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestService instance,
                    java.lang.Object[] arguments) throws Throwable {
                  instance.testEmpty();
                  return null;
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestService instance)
                    throws Throwable {
                  instance.testEmpty();
                  return null;
                }
              }

              public final class MetaTestRequestMethod extends InstanceMetaMethod<MetaTestServiceClass, io.art.tarantool.test.model.TestService, Void> {
                private final MetaParameter<io.art.tarantool.test.model.TestService.TestRequest> requestParameter = register(new MetaParameter<>(0, "request",metaType(io.art.tarantool.test.model.TestService.TestRequest.class)));

                private MetaTestRequestMethod(MetaTestServiceClass owner) {
                  super("testRequest",metaType(Void.class),owner);
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestService instance,
                    java.lang.Object[] arguments) throws Throwable {
                  instance.testRequest((io.art.tarantool.test.model.TestService.TestRequest)(arguments[0]));
                  return null;
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestService instance,
                    java.lang.Object argument) throws Throwable {
                  instance.testRequest((io.art.tarantool.test.model.TestService.TestRequest)(argument));
                  return null;
                }

                public MetaParameter<io.art.tarantool.test.model.TestService.TestRequest> requestParameter(
                    ) {
                  return requestParameter;
                }
              }

              public final class MetaTestChannelMethod extends InstanceMetaMethod<MetaTestServiceClass, io.art.tarantool.test.model.TestService, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<io.art.tarantool.test.model.TestService.TestRequest>> channelParameter = register(new MetaParameter<>(0, "channel",metaType(reactor.core.publisher.Flux.class,metaType(io.art.tarantool.test.model.TestService.TestRequest.class))));

                private MetaTestChannelMethod(MetaTestServiceClass owner) {
                  super("testChannel",metaType(Void.class),owner);
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestService instance,
                    java.lang.Object[] arguments) throws Throwable {
                  instance.testChannel((reactor.core.publisher.Flux<io.art.tarantool.test.model.TestService.TestRequest>)(arguments[0]));
                  return null;
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestService instance,
                    java.lang.Object argument) throws Throwable {
                  instance.testChannel((reactor.core.publisher.Flux)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Flux<io.art.tarantool.test.model.TestService.TestRequest>> channelParameter(
                    ) {
                  return channelParameter;
                }
              }

              public static final class MetaTestRequestClass extends MetaClass<io.art.tarantool.test.model.TestService.TestRequest> {
                private static final LazyProperty<MetaTestRequestClass> self = MetaClass.self(io.art.tarantool.test.model.TestService.TestRequest.class);

                private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor(this));

                private final MetaField<MetaTestRequestClass, java.lang.Integer> intValueField = register(new MetaField<>("intValue",metaType(int.class),false,this));

                private final MetaField<MetaTestRequestClass, java.lang.String> stringValueField = register(new MetaField<>("stringValue",metaType(java.lang.String.class),false,this));

                private final MetaGetIntValueMethod getIntValueMethod = register(new MetaGetIntValueMethod(this));

                private final MetaGetStringValueMethod getStringValueMethod = register(new MetaGetStringValueMethod(this));

                private MetaTestRequestClass() {
                  super(metaType(io.art.tarantool.test.model.TestService.TestRequest.class));
                }

                public static MetaTestRequestClass testRequest() {
                  return self.get();
                }

                public MetaConstructorConstructor constructor() {
                  return constructor;
                }

                public MetaField<MetaTestRequestClass, java.lang.Integer> intValueField() {
                  return intValueField;
                }

                public MetaField<MetaTestRequestClass, java.lang.String> stringValueField() {
                  return stringValueField;
                }

                public MetaGetIntValueMethod getIntValueMethod() {
                  return getIntValueMethod;
                }

                public MetaGetStringValueMethod getStringValueMethod() {
                  return getStringValueMethod;
                }

                public final class MetaConstructorConstructor extends MetaConstructor<MetaTestRequestClass, io.art.tarantool.test.model.TestService.TestRequest> {
                  private final MetaParameter<java.lang.Integer> intValueParameter = register(new MetaParameter<>(0, "intValue",metaType(int.class)));

                  private final MetaParameter<java.lang.String> stringValueParameter = register(new MetaParameter<>(1, "stringValue",metaType(java.lang.String.class)));

                  private MetaConstructorConstructor(MetaTestRequestClass owner) {
                    super(metaType(io.art.tarantool.test.model.TestService.TestRequest.class),owner);
                  }

                  @Override
                  public io.art.tarantool.test.model.TestService.TestRequest invoke(
                      java.lang.Object[] arguments) throws Throwable {
                    return new io.art.tarantool.test.model.TestService.TestRequest((int)(arguments[0]),(java.lang.String)(arguments[1]));
                  }

                  public MetaParameter<java.lang.Integer> intValueParameter() {
                    return intValueParameter;
                  }

                  public MetaParameter<java.lang.String> stringValueParameter() {
                    return stringValueParameter;
                  }
                }

                public final class MetaGetIntValueMethod extends InstanceMetaMethod<MetaTestRequestClass, io.art.tarantool.test.model.TestService.TestRequest, java.lang.Integer> {
                  private MetaGetIntValueMethod(MetaTestRequestClass owner) {
                    super("getIntValue",metaType(int.class),owner);
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestService.TestRequest instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getIntValue();
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestService.TestRequest instance) throws
                      Throwable {
                    return instance.getIntValue();
                  }
                }

                public final class MetaGetStringValueMethod extends InstanceMetaMethod<MetaTestRequestClass, io.art.tarantool.test.model.TestService.TestRequest, java.lang.String> {
                  private MetaGetStringValueMethod(MetaTestRequestClass owner) {
                    super("getStringValue",metaType(java.lang.String.class),owner);
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestService.TestRequest instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getStringValue();
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestService.TestRequest instance) throws
                      Throwable {
                    return instance.getStringValue();
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
