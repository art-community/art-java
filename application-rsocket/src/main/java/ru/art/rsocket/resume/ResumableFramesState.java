package ru.art.rsocket.resume;

import io.netty.buffer.*;
import lombok.*;
import ru.art.entity.*;
import static ru.art.entity.Value.*;
import static ru.art.rsocket.resume.ResumableFramesStateMapping.*;
import java.util.*;

@lombok.Value
@Builder
@EqualsAndHashCode
public class ResumableFramesState {
    private final Integer cacheLimit;
    private final Integer cacheSize;
    private final Long position;
    private final Queue<ByteBuf> frames;
    private final Integer upstreamFrameRefCnt;
    private final ByteBuf setupFrame;

    public static ResumableFramesState fromValue(Entity value) {
        return toResumableFramesState.map(asEntity(value));
    }

    public Entity toValue() {
        return fromResumableFramesState.map(this);
    }
}
