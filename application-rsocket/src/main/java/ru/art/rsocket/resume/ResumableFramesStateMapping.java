package ru.art.rsocket.resume;

import io.rsocket.util.*;
import lombok.experimental.*;
import ru.art.core.factory.*;
import ru.art.entity.*;
import ru.art.entity.mapper.ValueFromModelMapper.*;
import ru.art.entity.mapper.ValueToModelMapper.*;
import ru.art.rsocket.reader.*;
import static io.rsocket.util.ByteBufPayload.*;
import static java.util.stream.Collectors.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.entity.Entity.*;
import static ru.art.rsocket.reader.ByteBufReader.readByteBufToArray;

@UtilityClass
public class ResumableFramesStateMapping {
    public static final EntityFromModelMapper<ResumableFramesState> fromResumableFramesState = (ResumableFramesState store) ->
            entityBuilder()
                    .longField("position", store.getPosition())
                    .intField("cacheSize", store.getCacheSize())
                    .collectionValueCollectionField("frames", linkedListOf(store.getFrames())
                            .stream()
                            .map(ByteBufReader::readByteBufToArray)
                            .map(CollectionValuesFactory::byteCollection)
                            .collect(toCollection(CollectionsFactory::queueOf)))
                    .intField("cacheLimit", store.getCacheLimit())
                    .intField("upstreamFrameRefCnt", store.getUpstreamFrameRefCnt())
                    .byteArrayField("setupFrame", readByteBufToArray(store.getSetupFrame()))
                    .build();

    public static final EntityToModelMapper<ResumableFramesState> toResumableFramesState = (Entity store) ->
            ResumableFramesState.builder()
                    .position(store.getLong("position"))
                    .cacheSize(store.getInt("cacheSize"))
                    .frames(store.getCollectionValueList("frames")
                            .stream()
                            .map(collection -> create(collection.getByteElements()).data())
                            .collect(toCollection(CollectionsFactory::queueOf)))
                    .cacheLimit(store.getInt("cacheLimit"))
                    .upstreamFrameRefCnt(store.getInt("upstreamFrameRefCnt"))
                    .setupFrame(ByteBufPayload.create(store.getCollectionValue("setupFrame").getByteArray()).data())
                    .build();
}