package ru.art.config.extensions.rsocket;

import lombok.experimental.*;
import ru.art.entity.*;
import ru.art.rsocket.resume.*;
import ru.art.tarantool.dao.*;
import static ru.art.rocks.db.dao.RocksDbValueDao.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.*;
import static ru.art.tarantool.dao.TarantoolDao.*;
import java.util.function.*;

@UtilityClass
public class RsocketResumableStateRepositories {
    public static ResumableFramesStateRepository storeInTarantool(String token, String instanceId) {
        Supplier<ResumableFramesState> provider = () -> TarantoolDao.tarantool(instanceId).selectAny(RESUME_STATE_PREFIX + token)
                .map(ResumableFramesState::fromValue)
                .orElse(ResumableFramesState.builder().build());
        Consumer<ResumableFramesState> consumer = state -> tarantool(instanceId).put(RESUME_STATE_PREFIX + token, state.toValue());
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
