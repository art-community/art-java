package ru.art.rsocket.resume;

import io.netty.buffer.*;
import io.rsocket.resume.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import reactor.util.concurrent.*;
import ru.art.core.extension.*;
import static java.lang.Long.*;
import static java.lang.Math.max;
import static java.lang.String.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.extension.NullCheckingExtensions.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.DEFAULT_RSOCKET_RESUME_STATE_CACHE_SIZE;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

public class PersistableResumableFramesStore implements ResumableFramesStore {
    private static final long SAVE_REQUEST_SIZE = MAX_VALUE;
    private final MonoProcessor<Void> disposed = MonoProcessor.create();
    private volatile long position;
    private volatile AtomicLong impliedPosition;
    private volatile int cacheSize;
    private final Queue<ByteBuf> cachedFrames;
    private final int cacheLimit;
    private volatile int upstreamFrameRefCnt;
    private Consumer<ResumableFramesState> persistingConsumer;

    public PersistableResumableFramesStore(Supplier<ResumableFramesState> stateProvider, Consumer<ResumableFramesState> persistingConsumer) {
        ResumableFramesState state = stateProvider.get();
        this.position = getOrElse(state.getPosition(), 0L);
        this.impliedPosition = new AtomicLong(getOrElse(state.getImpliedPosition(), 0L));
        this.cacheLimit = getOrElse(state.getCacheLimit(),  DEFAULT_RSOCKET_RESUME_STATE_CACHE_SIZE);
        this.cachedFrames = ifEmpty(state.getCachedFrames(), cachedFramesQueue(this.cacheLimit));
        this.upstreamFrameRefCnt = getOrElse(state.getUpstreamFrameRefCnt(), 0);
        this.persistingConsumer = persistingConsumer;
    }

    public Mono<Void> saveFrames(Flux<ByteBuf> frames) {
        MonoProcessor<Void> completed = MonoProcessor.create();
        frames.doFinally(frameStream -> completed.onComplete()).subscribe(new FramesSubscriber(SAVE_REQUEST_SIZE));
        return completed;
    }

    @Override
    public void releaseFrames(long remoteImpliedPos) {
        long pos = position;
        long removeSize = max(0, remoteImpliedPos - pos);
        while (removeSize > 0) {
            ByteBuf cachedFrame = cachedFrames.poll();
            if (cachedFrame != null) {
                removeSize -= releaseTailFrame(cachedFrame);
            } else {
                break;
            }
        }

        if (removeSize > 0) {
            throw new IllegalStateException(format("need to remove additional %d bytes, but cache is empty" + "Local and remote state disagreement: ", removeSize));
        }

        if (removeSize < 0) {
            throw new IllegalStateException("Local and remote state disagreement: " + "local and remote frame sizes are not equal");
        }

        persist();
    }

    @Override
    public Flux<ByteBuf> resumeStream() {
        return Flux.generate(
                () -> new ResumeStreamState(cachedFrames.size(), upstreamFrameRefCnt),
                (state, sink) -> {
                    if (state.next()) {
                        ByteBuf frame = cachedFrames.poll();
                        if (state.shouldRetain(frame)) {
                            frame.retain();
                        }
                        cachedFrames.offer(frame);
                        sink.next(frame);
                    } else {
                        sink.complete();
                    }
                    persist();
                    return state;
                });
    }

    @Override
    public long framePosition() {
        return position;
    }

    @Override
    public long frameImpliedPosition() {
        return impliedPosition.get();
    }

    @Override
    public void resumableFrameReceived(ByteBuf frame) {
        impliedPosition.addAndGet(frame.readableBytes());
        persist();
    }

    @Override
    public Mono<Void> onClose() {
        return disposed;
    }

    @Override
    public void dispose() {
        cacheSize = 0;
        ByteBuf frame = cachedFrames.poll();
        while (frame != null) {
            frame.release();
            frame = cachedFrames.poll();
        }
        persist();
        disposed.onComplete();
    }

    @Override
    public boolean isDisposed() {
        return disposed.isTerminated();
    }

    public void persist() {
        persistingConsumer.accept(ResumableFramesState.builder()
                .position(position)
                .impliedPosition(impliedPosition.get())
                .cacheSize(cacheSize)
                .cachedFrames(cachedFrames)
                .upstreamFrameRefCnt(upstreamFrameRefCnt)
                .build());
    }

    private int releaseTailFrame(ByteBuf content) {
        int frameSize = content.readableBytes();
        cacheSize -= frameSize;
        position += frameSize;
        content.release();
        return frameSize;
    }

    private void saveFrame(ByteBuf frame) {
        if (upstreamFrameRefCnt == 0) {
            upstreamFrameRefCnt = frame.refCnt();
        }

        int frameSize = frame.readableBytes();
        long availableSize = cacheLimit - cacheSize;
        while (availableSize < frameSize) {
            ByteBuf cachedFrame = cachedFrames.poll();
            if (cachedFrame != null) {
                availableSize += releaseTailFrame(cachedFrame);
            } else {
                break;
            }
        }
        if (availableSize >= frameSize) {
            cachedFrames.offer(frame.retain());
            cacheSize += frameSize;
            persist();
            return;
        }
        position += frameSize;
        persist();
    }

    private static class ResumeStreamState {
        private final int cacheSize;
        private final int expectedRefCnt;
        private int cacheCounter;

        public ResumeStreamState(int cacheSize, int expectedRefCnt) {
            this.cacheSize = cacheSize;
            this.expectedRefCnt = expectedRefCnt;
        }

        public boolean next() {
            if (cacheCounter < cacheSize) {
                cacheCounter++;
                return true;
            }
            return false;
        }

        public boolean shouldRetain(ByteBuf frame) {
            return frame.refCnt() == expectedRefCnt;
        }
    }

    private static Queue<ByteBuf> cachedFramesQueue(int size) {
        return Queues.<ByteBuf>get(size).get();
    }

    private class FramesSubscriber implements Subscriber<ByteBuf> {
        private final long firstRequestSize;
        private final long refillSize;
        private int received;
        private Subscription s;

        public FramesSubscriber(long requestSize) {
            this.firstRequestSize = requestSize;
            this.refillSize = firstRequestSize / 2;
        }

        @Override
        public void onSubscribe(Subscription s) {
            this.s = s;
            s.request(firstRequestSize);
        }

        @Override
        public void onNext(ByteBuf byteBuf) {
            saveFrame(byteBuf);
            if (firstRequestSize != MAX_VALUE && ++received == refillSize) {
                received = 0;
                s.request(refillSize);
            }
        }

        @Override
        public void onError(Throwable t) {
        }

        @Override
        public void onComplete() {
        }
    }
}
