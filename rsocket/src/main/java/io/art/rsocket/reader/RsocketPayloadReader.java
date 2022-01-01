/*
 * ART
 *
 * Copyright 2019-2022 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.rsocket.reader;

import io.art.meta.model.*;
import io.art.transport.payload.*;
import io.rsocket.*;
import lombok.experimental.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static io.art.transport.payload.TransportPayload.*;
import static java.util.Objects.*;

@UtilityClass
public class RsocketPayloadReader {
    public static TransportPayload readRsocketPayload(TransportPayloadReader reader, Payload payload, MetaType<?> type) {
        if (isNull(type) || type.internalKind() == VOID) {
            payload.release(payload.refCnt());
            return emptyTransportPayload();
        }
        TransportPayload transportPayload = reader.read(payload.sliceData(), type);
        payload.release(payload.refCnt());
        return transportPayload;
    }
}
