package io.art.tarantool.aggregrator;

import io.art.core.extensions.*;
import io.art.core.local.*;
import io.netty.buffer.*;
import lombok.*;
import reactor.core.*;
import reactor.core.publisher.*;
import static io.art.core.extensions.NettyBufferExtensions.*;
import static io.art.core.local.ThreadLocalValue.*;
import static io.art.tarantool.descriptor.TarantoolResponseReader.*;
import static io.art.transport.allocator.WriteBufferAllocator.*;
import static io.art.transport.module.TransportModule.*;

@RequiredArgsConstructor
public class TarantoolResponseAggregator extends Flux<ByteBuf> {
    private final ThreadLocalValue<ByteBuf> buffer = threadLocal(() -> allocateWriteBuffer(transportModule().configuration()));
    private final ThreadLocalValue<Integer> size = threadLocal(() -> 0);
    private final Flux<ByteBuf> source;

    @Override
    public void subscribe(CoreSubscriber<? super ByteBuf> actual) {
        source
                .doOnNext(ByteBuf::retain)
                .doOnDiscard(ByteBuf.class, NettyBufferExtensions::releaseBuffer)
                .doOnNext(input -> aggregate(input, actual))
                .doOnComplete(actual::onComplete)
                .doOnError(actual::onError)
                .doOnSubscribe(actual::onSubscribe)
                .doFinally(ignore -> releaseBuffer(buffer.get()))
                .subscribe();
    }

    private void aggregate(ByteBuf input, CoreSubscriber<? super ByteBuf> actual) {
        ByteBuf local = buffer.get();
        if (size.get() == 0) {
            int size;
            this.size.set(size = readTarantoolResponseSize(input));
            if (input.readableBytes() >= size) {
                actual.onNext(local.writeBytes(input, size));
                this.size.set(0);
                local.clear();
                return;
            }
            local.writeBytes(input);
            return;
        }

        if (input.readableBytes() >= size.get()) {
            actual.onNext(local.writeBytes(input, size.get()));
            size.set(0);
            local.clear();
            return;
        }

        local.writeBytes(input);
    }
}
