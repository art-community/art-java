package ru.art.config.extensions.rsocket;

import com.google.common.hash.*;
import lombok.experimental.*;
import ru.art.core.context.*;
import ru.art.entity.*;
import ru.art.rsocket.resume.*;
import ru.art.tarantool.dao.*;
import static com.google.common.hash.Hashing.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.rocks.db.dao.RocksDbValueDao.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.*;
import static ru.art.tarantool.dao.TarantoolDao.*;
import java.util.function.*;

@UtilityClass
public class RsocketResumableStateRepositories {
    public static ResumableFramesStateRepository storeInTarantool(String token, String instanceId) {
        String name = RESUME_STATE_PREFIX + crc32().hashString(token, contextConfiguration().getCharset()).toString();
        Supplier<ResumableFramesState> provider = () -> TarantoolDao.tarantool(instanceId).selectAny(name)
                .map(ResumableFramesState::fromValue)
                .orElse(ResumableFramesState.builder().build());
        Consumer<ResumableFramesState> consumer = state -> tarantool(instanceId).put(name, state.toValue());
        return ResumableFramesStateRepository.builder().provider(provider).consumer(consumer).build();
    }

    public static ResumableFramesStateRepository storeInRocksDb(String token) {
        Supplier<ResumableFramesState> provider = () -> getBinary(RESUME_STATE_PREFIX + token)
                .map(Value::asEntity)
                .map(ResumableFramesState::fromValue)
                .orElse(ResumableFramesState.builder().build());
        Consumer<ResumableFramesState> consumer = state -> putBinary(RESUME_STATE_PREFIX + token, state.toValue());
        return ResumableFramesStateRepository.builder().provider(provider).consumer(consumer).build();
    }
}
