/*
 * ART
 *
 * Copyright 2019-2021 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.logging.writer;

import io.art.logging.configuration.*;
import io.art.logging.constants.*;
import io.art.logging.exception.*;
import io.art.logging.manager.*;
import io.art.logging.model.*;
import jnr.unixsocket.*;
import static com.google.common.base.Throwables.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.SystemExtensions.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.logging.constants.LoggingModuleConstants.*;
import static jnr.unixsocket.UnixSocketChannel.*;
import java.nio.*;
import java.nio.channels.*;
import java.text.*;

public class UnixTcpWriter implements LoggerWriter {
    private final LoggingManager manager;
    private final LoggerWriterConfiguration writerConfiguration;
    private UnixSocketChannel channel;

    public UnixTcpWriter(LoggingManager manager, LoggerWriterConfiguration writerConfiguration) {
        this.manager = manager;
        this.writerConfiguration = writerConfiguration;
        openChannel();
    }

    @Override
    public void write(LoggingMessage message) {
        try {
            channel.write(ByteBuffer.wrap(format(message).getBytes(writerConfiguration.getCharset())));
        } catch (Throwable throwable) {
            printError(getStackTraceAsString(throwable));
            closeChannel(channel);
            openChannel();
        }
    }

    private String format(LoggingMessage message) {
        String dateTime = writerConfiguration.getDateTimeFormatter().format(message.getDateTime());
        LoggingLevel level = message.getLevel();
        return MessageFormat.format(LOGGING_FORMAT,
                dateTime,
                level.name(),
                OPENING_SQUARE_BRACES + message.getThread().getName() + CLOSING_SQUARE_BRACES,
                message.getLogger(),
                message.getMessage()
        );
    }

    private void closeChannel(SocketChannel channel) {
        ignoreException(channel::close);
        manager.remove(channel);
    }

    private void openChannel() {
        try {
            channel = open(new UnixSocketAddress(writerConfiguration.getUnixTcp().getSocketPath().toFile()));
            manager.register(channel);
        } catch (Throwable throwable) {
            apply(channel, this::closeChannel);
            throw new LoggingModuleException(throwable);
        }
    }
}
