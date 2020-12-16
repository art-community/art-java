local space = {
    create = function(space)
        box.schema.space.create('_' .. space .. art.config.mappingSpacePostfix)
    end,

    initSpace = function(space)
        local format = {
            {name = 'timestamp', type = 'unsigned'},
            {name = 'is_delete', type = 'boolean'},
            {name = 'data', type = 'any', is_nullable = true}
        }
        local primary_index_parts = {}
        for k, v in pairs(box.space[space].index[0].parts) do
            table.insert(format, {name = 'id_part_' .. k, type = v.type, is_nullable = v.is_nullable})
            table.insert(primary_index_parts, {v.fieldno + 3, type = v.type})
        end

        art.core.mappingUpdatesOf(space):format(format)
        art.core.mappingUpdatesOf(space):create_index('primary', { parts = primary_index_parts})
        art.core.mappingUpdatesOf(space):create_index('timestamp', { unique = false, parts = { 1}})

        box.space._mapping_watched_spaces:insert(box.tuple.new({space, {}, art.config.mapping.defaultBatchSize }))
    end,

    watchIndex = function(space, index_obj)
        if not (art.core.mappingUpdatesOf(space)) then return end
        if (index_obj.id == 0) then art.cluster.mapping.space.initSpace(space, index_obj) end

        local watched_space = box.space._mapping_watched_spaces:get(space)
        local watched_fields = watched_space[2]

        for _, v in pairs(index_obj.parts) do
            if not (watched_fields[v.fieldno]) then watched_fields[v.fieldno] = 0 end
            watched_fields[v.fieldno] = watched_fields[v.fieldno] + 1
        end

        watched_space = watched_space:update({{'=', 2, watched_fields}})
        box.space._mapping_watched_spaces:replace(watched_space)
        art.cluster.mapping.builder.build(space)
    end,

    rename = function(space, name)
        if(art.core.mappingUpdatesOf(space)) then
            art.core.mappingUpdatesOf(space):rename('_' .. name .. art.config.mappingSpacePostfix)
            local watched_space = box.space._mapping_watched_spaces:delete(space)
            box.space._mapping_watched_spaces:insert(watched_space:update({{'=', 1, name}}))
            art.cluster.mapping.lastCollectedTimestamps[name] = art.cluster.mapping.lastCollectedTimestamps[space]
            art.cluster.mapping.lastCollectedTimestamps[space] = nil
        end
    end,

    truncate = function(space)
        if(art.core.mappingUpdatesOf(space)) then
            art.core.mappingUpdatesOf(space):truncate()
        end
    end,

    unwatch = function(space)
        if(art.core.mappingUpdatesOf(space)) then
            art.core.mappingUpdatesOf(space):drop()
            box.space._mapping_watched_spaces:delete(space)
            art.cluster.mapping.lastCollectedTimestamps[space] = nil
        end
    end,

    unwatchIndex = function(space, index)
        if not (art.core.mappingUpdatesOf(space)) then return end
        if (index.id ==0) or (index.name == 'bucket_id') then
            art.luster.mapping.space.unwatch(space)
            return
        end

        local watched_space = box.space._mapping_watched_spaces:get(space)
        local watched_fields = watched_space[2]

        for _, v in pairs(index_obj.parts) do
            watched_fields[v.fieldno] = watched_fields[v.fieldno] - 1
            if (watched_fields[v.fieldno] < 1) then watched_fields[v.fieldno] = nil end
        end

        watched_space = watched_space:update({{'=', 2, watched_fields}})
        box.space._mapping_watched_spaces:replace(watched_space)
    end
}

return space