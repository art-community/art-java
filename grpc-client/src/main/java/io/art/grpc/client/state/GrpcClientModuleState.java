package io.art.grpc.client.state;

import io.grpc.ManagedChannel;
import lombok.Getter;
import io.art.core.module.ModuleState;
import static io.art.core.factory.CollectionsFactory.linkedListOf;
import java.util.List;

@Getter
public class GrpcClientModuleState implements ModuleState {
    private final List<ManagedChannel> channels = linkedListOf();

    public ManagedChannel registerChannel(ManagedChannel channel) {
        channels.add(channel);
        return channel;
    }
}
