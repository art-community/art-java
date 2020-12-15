local mapping = {
    lastUploadMinTimestamp = 0ULL,
    nodes = {},
    primaryNodeUUID = '',
    lastCollectedTimestamps = {},

    init = function(uri_list)
        if not (box.space._mapping_watched_spaces) then
            box.schema.space.create('_mapping_watched_spaces')
            box.space._mapping_watched_spaces:format({
                {name = 'space', type = 'string'},
                {name = 'watched_fields_counter', type = 'array'},
                {name = 'batchSize', type = 'unsigned'}
            })
            box.space._mapping_watched_spaces:create_index('primary', {parts = {1}})
        end

        for _,v in pairs(uri_list) do art.cluster.mapping.nodes[v] = false end
        art.cluster.mapping.primaryNodeUUID = uri_list[vshard.storage.internal.this_replicaset.uuid]

        art.cluster.mapping.networkManager.start()
        art.cluster.mapping.garbageCollector.start()
        art.cluster.mapping.watcher.start()
        art.cluster.mapping.builder.start()
    end,

    put = function(space, data)
        if not (art.core.mappingUpdatesOf(space)) then return end
        local update = {art.core.clock.realtime64(), false}
        local update_data = {}
        for k,_ in pairs(box.space._mapping_watched_spaces:get(space)[2]) do
            update_data[k]=data[k]
        end
        update[3] = update_data

        for _,v in pairs(box.space[space].index[0].parts) do
            table.insert(update, data[v.fieldno])
        end
        art.core.mappingUpdatesOf(space):put(update)
    end,

    delete = function(space, key)
        if not (art.core.mappingUpdatesOf(space)) then return end
        if not (type(key) == 'table') then key = {key} end

        art.core.mappingUpdatesOf(space):put({art.core.clock.realtime64(), true, {}, unpack(key)})
    end,

    space = require('art.storage.cluster.mapping.space'),
}

local services = require('art.storage.cluster.mapping.services')
mapping.watcher = services.watcher
mapping.builder = services.builder
mapping.garbageCollector = services.garbageCollector
mapping.networkManager = services.networkManager

return mapping