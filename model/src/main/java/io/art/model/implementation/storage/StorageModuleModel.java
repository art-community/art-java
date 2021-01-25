package io.art.model.implementation.storage;


import io.art.core.collection.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.let;
import static io.art.core.collection.ImmutableMap.*;
import java.util.*;

@Getter
@RequiredArgsConstructor
public class StorageModuleModel {
    private final ImmutableMap<String, TarantoolStorageModel> tarantoolStorages;

    public CommunicatorAction.CommunicatorActionBuilder implement(String id, CommunicatorAction.CommunicatorActionBuilder current) {
        return let(getCommunicators().get(id), communicator -> communicator.implement(current), current);
    }

    public ImmutableMap<String, StorageModel> getStorages() {
        return tarantoolStorages.entrySet().stream().collect(immutableMapCollector(Map.Entry::getKey, Map.Entry::getValue));
    }

}
