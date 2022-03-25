package io.art.tarantool.authenticator;

import io.art.tarantool.model.*;
import io.netty.channel.*;
import lombok.*;
import org.msgpack.value.Value;
import static io.art.core.checker.NullityChecker.*;
import java.util.function.*;

@RequiredArgsConstructor
public class TarantoolAuthenticationResponder extends SimpleChannelInboundHandler<TarantoolResponse> {
    private final BiConsumer<Boolean, String> listener;

    @Override
    protected void channelRead0(ChannelHandlerContext context, TarantoolResponse tarantoolResponse) {
        System.out.println(tarantoolResponse.getHeader().getSyncId() + " : " + tarantoolResponse.getHeader().getCode());
        System.out.println(tarantoolResponse);
        listener.accept(!tarantoolResponse.isError(), let(tarantoolResponse.getBody(), Value::toJson));
        context.pipeline().remove(this);
    }
}
