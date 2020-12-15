local space = {
    create = function(space)
        box.schema.space.create('_' .. space .. art.config.mapping_space_postfix)
    end,

    init_space = function(space)
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

        art.core.mapping_updates_of(space):format(format)
        art.core.mapping_updates_of(space):create_index('primary', { parts = primary_index_parts})
        art.core.mapping_updates_of(space):create_index('timestamp', { unique = false, parts = { 1}})

        box.space._mapping_watched_spaces:insert(box.tuple.new({space, {}, art.config.mapping.default_batch_size }))
    end,

    watch_index = function(space, index_obj)
        if not (art.core.mapping_updates_of(space)) then return end
        if (index_obj.id == 0) then art.cluster.mapping.space.init_space(space, index_obj) end

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
        if(art.core.mapping_updates_of(space)) then
            art.core.mapping_updates_of(space):rename('_' .. name .. art.config.mapping_space_postfix)
            local watched_space = box.space._mapping_watched_spaces:delete(space)
            box.space._mapping_watched_spaces:insert(watched_space:update({{'=', 1, name}}))
            art.cluster.mapping.last_collected_timestamps[name] = art.cluster.mapping.last_collected_timestamps[space]
            art.cluster.mapping.last_collected_timestamps[space] = nil
        end
    end,

    truncate = function(space)
        if(art.core.mapping_updates_of(space)) then
            art.core.mapping_updates_of(space):truncate()
        end
    end,

    unwatch = function(space)
        if(art.core.mapping_updates_of(space)) then
            art.core.mapping_updates_of(space):drop()
            box.space._mapping_watched_spaces:delete(space)
            art.cluster.mapping.last_collected_timestamps[space] = nil
        end
    end,

    unwatch_index = function(space, index)
        if not (art.core.mapping_updates_of(space)) then return end
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