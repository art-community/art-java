local mapping = {
    last_upload_min_timestamp = 0ULL,
    nodes = {},
    primary_node_uuid = '',
    last_collected_timestamps = {},

    init = function(uri_list)
        if not (box.space._mapping_watched_spaces) then
            box.schema.space.create('_mapping_watched_spaces')
            box.space._mapping_watched_spaces:format({
                {name = 'space', type = 'string'},
                {name = 'watched_fields_counter', type = 'array'},
                {name = 'batch_size', type = 'unsigned'}
            })
            box.space._mapping_watched_spaces:create_index('primary', {parts = {1}})
        end

        for _,v in pairs(uri_list) do art.cluster.mapping.nodes[v] = false end
        art.cluster.mapping.primary_node_uuid = uri_list[vshard.storage.internal.this_replicaset.uuid]

        art.cluster.mapping.network_manager.start()
        art.cluster.mapping.garbage_collector.start()
        art.cluster.mapping.watcher.start()
        art.cluster.mapping.builder.start()
    end,

    put = function(space, data)
        if not (art.core.mapping_updates_of(space)) then return end
        local update = {art.core.clock.realtime64(), false}
        local update_data = {}
        for k,_ in pairs(box.space._mapping_watched_spaces:get(space)[2]) do
            update_data[k]=data[k]
        end
        update[3] = update_data

        for _,v in pairs(box.space[space].index[0].parts) do
            table.insert(update, data[v.fieldno])
        end
        art.core.mapping_updates_of(space):put(update)
    end,

    delete = function(space, key)
        if not (art.core.mapping_updates_of(space)) then return end
        if not (type(key) == 'table') then key = {key} end

        art.core.mapping_updates_of(space):put({art.core.clock.realtime64(), true, {}, unpack(key)})
    end,

    space = require('art.storage.cluster.mapping.space'),
}

local services = require('art.storage.cluster.mapping.services')
mapping.watcher = services.watcher
mapping.builder = services.builder
mapping.garbage_collector = services.garbage_collector
mapping.network_manager = services.network_manager

return mapping