package io.art.tarantool.registry;

import io.art.tarantool.model.*;
import io.netty.util.collection.*;
import lombok.*;
import org.msgpack.value.Value;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static java.util.Objects.*;
import static org.msgpack.value.ValueFactory.*;
import static reactor.core.publisher.Sinks.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

@RequiredArgsConstructor
public class TarantoolReceiverRegistry {
    private final int localPoolSize;
    private final IntObjectMap<TarantoolReceiver> receivers = new IntObjectHashMap<>(RECEIVERS_INITIAL_SIZE);
    private final AtomicInteger pools = new AtomicInteger(-1);
    private final ThreadLocal<LocalPool> pool = new ThreadLocal<>();

    public TarantoolReceiver allocate(Consumer<Value> onChunk) {
        LocalPool localPool = pool.get();
        if (nonNull(localPool)) {
            int id = localPool.next();
            TarantoolReceiver receiver = new TarantoolReceiver(newInteger(id), one(), onChunk);
            receivers.put(id, receiver);
            return receiver;
        }

        localPool = new LocalPool(pools.incrementAndGet() * localPoolSize + 1, localPoolSize);
        this.pool.set(localPool);
        int id = localPool.next();
        TarantoolReceiver receiver = new TarantoolReceiver(newInteger(id), one(), onChunk);
        receivers.put(id, receiver);
        return receiver;
    }

    public TarantoolReceiver get(int id) {
        return receivers.get(id);
    }

    public void free(int id) {
        receivers.remove(id);
    }

    private class LocalPool {
        int offset;
        int current;
        int max;

        public LocalPool(int offset, int max) {
            this.offset = offset;
            this.max = max;
        }

        int next() {
            current += REQUEST_ID_STEP;
            if (current >= max) {
                current = 0;
                while (receivers.containsKey(offset + current)) {
                    current += REQUEST_ID_STEP;
                    if (current >= max) {
                        current = 0;
                    }
                }
            }
            return offset + current;
        }
    }
}
