package io.art.configurator.custom;

import io.art.core.source.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.singleton.SingletonsRegistry.*;

public interface CustomConfigurationProxy<T> {
    T configure(ConfigurationSource source);

    class MyConfigProxy implements CustomConfigurationProxy<MyConfig> {
        @Override
        public MyConfig configure(ConfigurationSource source) {
            String value = source.getString("value");
            MyConfig nested = let(source.getNested("nested"), singleton(MyConfigProxy.class, MyConfigProxy::new)::configure);
            return new MyConfig(value, nested);
        }
    }


    public static void main(String[] args) {

    }
}
