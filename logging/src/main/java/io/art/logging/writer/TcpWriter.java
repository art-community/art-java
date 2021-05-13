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
import io.art.logging.model.*;
import static com.google.common.base.Throwables.*;
import static io.art.core.colorizer.AnsiColorizer.*;
import static io.art.core.constants.AnsiColor.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.logging.colorizer.LogColorizer.*;
import static io.art.logging.constants.LoggingModuleConstants.*;
import static io.art.logging.module.LoggingModule.*;
import static java.net.InetSocketAddress.*;
import static java.net.StandardSocketOptions.*;
import static java.nio.channels.SocketChannel.*;
import static java.util.Objects.*;
import java.nio.*;
import java.nio.channels.*;
import java.text.*;

public class TcpWriter implements LoggerWriter {
    private final LoggerWriterConfiguration writerConfiguration;
    private SocketChannel channel;

    public TcpWriter(LoggerWriterConfiguration writerConfiguration) {
        this.writerConfiguration = writerConfiguration;
        openChannel();
    }

    @Override
    public void write(LoggingMessage message) {
        try {
            channel.write(ByteBuffer.wrap(format(message).getBytes(writerConfiguration.getCharset())));
        } catch (Throwable throwable) {
            System.err.println(getStackTraceAsString(throwable));
            closeChannel(channel);
            openChannel();
        }
    }

    private String format(LoggingMessage message) {
        String dateTime = writerConfiguration.getDateTimeFormatter().format(message.getDateTime());
        LoggingLevel level = message.getLevel();
        return MessageFormat.format(LOGGING_FORMAT,
                message(dateTime, BLUE),
                byLevel(level, level.name()),
                OPENING_SQUARE_BRACES + message(message.getThread().getName(), PURPLE_BOLD) + CLOSING_SQUARE_BRACES,
                message.getLogger(),
                message.getMessage()
        );
    }

    private void closeChannel(SocketChannel channel) {
        ignoreException(channel::close);
        loggingModule().state().remove(channel);
    }

    private void openChannel() {
        try {
            channel = open(createUnresolved(writerConfiguration.getTcp().getHost(), writerConfiguration.getTcp().getPort()));
            channel.setOption(TCP_NODELAY, true);
            channel.setOption(SO_KEEPALIVE, true);
            loggingModule().state().register(channel);
        } catch (Throwable ioException) {
            System.err.println(getStackTraceAsString(ioException));
        } finally {
            if (nonNull(channel)) closeChannel(channel);
        }
    }
}
