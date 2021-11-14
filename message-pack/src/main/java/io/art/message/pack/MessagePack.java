package io.art.message.pack;

import io.art.core.annotation.*;
import io.art.message.pack.descriptor.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.message.pack.module.MessagePackModule.*;
import static lombok.AccessLevel.*;

@Public
@UtilityClass
public class MessagePack {
    private final static Provider provider = new Provider();

    public static Provider messagePack() {
        return provider;
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Provider {
        public MessagePackReader reader() {
            return messagePackModule().configuration().getReader();
        }

        public MessagePackWriter writer() {
            return messagePackModule().configuration().getWriter();
        }
    }
}
