package ru.art.rsocket.resume;

import lombok.*;
import java.util.function.*;

@Getter
@Builder
public class ResumableFramesStateRepository {
    @Builder.Default
    private final Supplier<ResumableFramesState> provider = () -> ResumableFramesState.builder().build();
    @Builder.Default
    private final Consumer<ResumableFramesState> consumer = state -> {
    };
}
