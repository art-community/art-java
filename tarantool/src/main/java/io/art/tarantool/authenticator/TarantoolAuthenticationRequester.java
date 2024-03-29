package io.art.tarantool.authenticator;

import io.art.tarantool.model.*;
import io.netty.buffer.*;
import io.netty.channel.*;
import lombok.*;
import org.msgpack.value.Value;
import static io.art.core.constants.AlgorithmConstants.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProtocolConstants.*;
import static io.art.tarantool.descriptor.TarantoolRequestWriter.*;
import static io.art.tarantool.factory.TarantoolRequestContentFactory.*;
import java.security.*;
import java.util.*;

@RequiredArgsConstructor
public class TarantoolAuthenticationRequester extends SimpleChannelInboundHandler<ByteBuf> {
    private final String username;
    private final String password;

    @Override
    protected void channelRead0(ChannelHandlerContext context, ByteBuf input) {
        if (input.readableBytes() < GREETING_LENGTH) return;
        Value request = createAuthenticationRequest(input, username, password);
        context.channel().writeAndFlush(writeTarantoolRequest(new TarantoolHeader(ZERO, IPROTO_AUTH), request));
        context.pipeline().remove(this);
    }

    private static Value createAuthenticationRequest(ByteBuf input, String username, String password) {
        byte[] array = new byte[VERSION_LENGTH];
        input.readBytes(array);
        array = new byte[SALT_LENGTH];
        input.readBytes(array).skipBytes(VERSION_LENGTH - SALT_LENGTH);
        return authenticationRequest(username, createPassword(array, password));
    }

    private static byte[] createPassword(byte[] serverAuthData, String password) {
        MessageDigest digest = sha1();
        byte[] auth = digest.digest(password.getBytes());
        byte[] auth2 = digest.digest(auth);
        byte[] salt = Base64.getDecoder().decode(serverAuthData);
        digest.update(salt, 0, SCRAMBLE_SIZE);
        digest.update(auth2);
        byte[] scramble = digest.digest();
        for (int i = 0; i < SCRAMBLE_SIZE; i++) {
            auth[i] ^= scramble[i];
        }
        return auth;
    }
}
