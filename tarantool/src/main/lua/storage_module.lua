--
--ART module for tarantool storages
--

art = {
    core = {
        schema_of = function(space)
            return box.space[space .. art.constants.schema_postfix]
        end,

        mapping_updates_of = function(space)
            return box.space[space.. art.constants.mapping_space_postfix]
        end,

        clock = require('clock'),
        
        hash = function(key)
            local ldigest = require('digest')
            if type(key) ~= 'table' then
                return ldigest.crc32(tostring(key))
            else
                local crc32 = ldigest.crc32.new()
                for _, v in ipairs(key) do
                    crc32:update(tostring(v))
                end
                return crc32:result()
            end
        end,

        fiber = require('fiber')
    },

    constants = {
        schema_postfix = '_schema',

        mapping_space_postfix = '_mapping_updates',
    },

    box = {
        get = function(space, key)
            local data = box.space[space]:get(key)
            if (data == nil) then return {'', ''} end
            local schema_hash = data[#data]
            local response = data:transform(#data, 1)
            local response_schema = art.core.schema_of(space):get(schema_hash)['schema']
            return {response, response_schema}
        end,

        delete = function(space, key)
            local data = box.space[space]:get(key)
            if data == nil then return {'', ''} end
            local schema_hash = data[#data]
            local response = data:transform(#data, 1)

            local response_schema = art.core.schema_of(space):get(schema_hash)

            if (response_schema['count'] == 1) then
                art.core.schema_of(space):delete(schema_hash)
            else
                art.core.schema_of(space):update(schema_hash, { { '-', 'count', 1}})
            end
            box.space[space]:delete(key)

            art.cluster.mapping.delete(space, key)

            response_schema = response_schema['schema']
            return {response, response_schema}
        end,

        insert = function(space, data, bucket_id)
            data[1] = box.tuple.new(data[1])
            data[2] = box.tuple.new(data[2])
            local schema_hash = art.core.hash({ data[2], bucket_id})
            data[1] = data[1]:update({{'!', #data[1]+1, schema_hash}})
            box.space[space]:insert(data[1])

            local schema_tuple = box.tuple.new({schema_hash, 1, data[2], bucket_id})
            art.core.schema_of(space):upsert(schema_tuple, { { '+', 'count', 1}})

            art.cluster.mapping.put(space, data[1])

            return {data[1]:transform(#data[1], 1), data[2]}
        end,

        auto_increment = function(space, data, bucket_id)
            data[1] = box.tuple.new(data[1])
            data[2] = box.tuple.new(data[2])
            local schema_hash = art.core.hash({ data[2], bucket_id})

            data[1] = data[1]:update({{'!', #data[1]+1, schema_hash}, {'#', 1, 1}})
            data[1] = box.space[space]:auto_increment(data[1]:totable())


            local schema_tuple = box.tuple.new({schema_hash, 1, data[2], bucket_id})
            art.core.schema_of(space):upsert(schema_tuple, { { '+', 'count', 1}})

            art.cluster.mapping.put(space, data[1])

            return {data[1]:transform(-1, 1), data[2]}
        end,

        put = function(space, data, bucket_id)
            local id = {}
            for k,v in pairs(box.space[space].index[0].parts) do
                id[k] = data[1][v.fieldno]
            end
            art.box.delete(space, id)
            local response = art.box.insert(space, data, bucket_id)
            return response
        end,

        update = function(space, id, commands, bucket_id)
            local data = art.box.get(space, id)
            data[1] = data[1]:update(commands[1])
            data[2] = box.tuple.new(data[2]):update(commands[2]):totable()
            art.box.put(space, data, bucket_id)
            return data
        end,

        replace = function(space, data, bucket_id)
            return art.box.put(space, data, bucket_id)
        end,

        upsert = function(space, data, commands, bucket_id)
            local id = {}
            for k,v in pairs(box.space[space].index[0].parts) do
                id[k] = data[1][v.fieldno]
            end
            if box.space[space]:get(id) then
                return art.box.update(space, id, commands, bucket_id)
            else
                return art.box.insert(space, data, bucket_id)
            end
        end,

        select = function(space, request)
            local response_entry = {}
            local response = box.space[space]:select(request)
            for i = 1, #response do
                response_entry[1] = response[i]:transform(#response[i], 1)
                response_entry[2] = art.core.schema_of(space):get(response[i][ #response[i] ])['schema']
                response[i] = {response_entry}
            end
            if response[1] == nil then return {} end
            return response
        end,

        space = {

            cluster_op_in_progress = false,

            wait_for_clustered_op = function()
                while art.box.space.cluster_op_in_progress do
                    art.core.fiber.sleep(1)
                end
            end,

            count = function(space)
                return box.space[space]:count()
            end,

            schema_count = function(space)
                return art.core.schema_of(space):count()
            end,

            len = function(space)
                return box.space[space]:len()
            end,

            schema_len = function(space)
                return art.core.schema_of(space):len()
            end,

            create_index = function(space, name, index)
                local index_obj = box.space[space]:create_index(name, index)
                art.cluster.mapping.space.watch_index(space, index_obj)
                return index_obj
            end,

            truncate = function(space)
                box.space[space]:truncate()
                art.core.schema_of(space):truncate()
                art.cluster.mapping.space.truncate(space)
            end,

            rename = function(space, name)
                art.core.schema_of(space):rename(name .. art.constants.schema_postfix)
                art.cluster.mapping.space.rename(space, name)
                return box.space[space]:rename(name)
            end,

            format = function(space, format)
                return box.space[space]:format(format)
            end,

            drop = function(space)
                box.space[space]:drop()
                art.cluster.mapping.space.unwatch(space)
                art.core.schema_of(space):drop()
            end,

            create = function(name, config)
                box.schema.space.create(name, config)
                config.field_count = nil
                config.id = nil
                config.format = {
                    {name = 'hash', type = 'unsigned'},
                    {name = 'count', type = 'unsigned'},
                    {name = 'schema', type = 'any'}
                }
                local schema = box.schema.space.create(name .. art.constants.schema_postfix, config)
                schema:create_index('hash', {
                    type = 'tree',
                    if_not_exists = true,
                    parts = {'hash'}
                })
            end,

            create_vsharded = function(name, config)
                box.schema.space.create(name, config)
                art.cluster.mapping.space.create(name)
                config.field_count = nil
                config.id = nil
                config.format = {
                    {name = 'hash', type = 'unsigned'},
                    {name = 'count', type = 'unsigned'},
                    {name = 'schema', type = 'any'},
                    {name = 'bucket_id', type = 'unsigned'}
                }
                local schema = box.schema.space.create(name .. art.constants.schema_postfix, config)
                schema:create_index('hash', {
                    type = 'tree',
                    if_not_exists = true,
                    parts = {'hash'}
                })
                schema:create_index('bucket_id', {
                    type = 'tree',
                    parts = {'bucket_id'},
                    unique = false
                })
                end
        },
    },

    cluster = {
        space_ops = {
            check_op_availability = function(operation, args)
                art.box.space.wait_for_clustered_op()
                art.box.space.cluster_op_in_progress = true
                local result = {}
                box.begin()
                result[1], result[2] = pcall(art.box.space[operation], unpack(args))
                box.rollback()
                return result
            end,

            cancel_op = function()
                art.box.space.cluster_op_in_progress = false
            end,

            execute_op = function(operation, args)
                local result = {}
                box.begin()
                result[1], result[2] = pcall(art.box.space[operation], unpack(args))
                if (result[1])
                then
                    box.commit()
                else
                    box.rollback()
                end
                art.box.space.cluster_op_in_progress = false
                return result
            end,
        },

        mapping = {
            default_batch_size = 1024,
            max_batches_count = 100,
            last_upload_min_timestamp = 0ULL,
            nodes = {},
            primary_node_uuid = '',

            init = function(uri_list)
                if not (box.space.mapping_watched_spaces) then
                    box.schema.space.create('mapping_watched_spaces')
                    box.space.mapping_watched_spaces:format({
                        {name = 'space', type = 'string'},
                        {name = 'watched_fields_counter', type = 'array'},
                        {name = 'batch_size', type = 'unsigned'}
                    })
                    box.space.mapping_watched_spaces:create_index('primary', {parts = {1}})
                end

                for _,v in pairs(uri_list) do art.cluster.mapping.nodes[v] = false end
                art.cluster.mapping.primary_node_uuid = uri_list[vshard.storage.internal.this_replicaset.uuid]

                art.cluster.mapping.network_manager.start()
                art.cluster.mapping.watcher.start()
                art.cluster.mapping.garbage_collector.start()

            end,

            put = function(space, data)
                if not (art.core.mapping_updates_of(space)) then return end
                local update = {art.core.clock.realtime64(), false}
                local update_data = {}
                for k,_ in pairs(box.space.mapping_watched_spaces:get(space)[2]) do
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

            builder = {
                build = function(space, field)

                end
            },

            watcher = {
                timeout = 0.1, --watcher sleep time

                last_collected_timestamps = {},

                service_fiber = nil,
                watchdog_fiber = nil,

                start = function()
                    art.cluster.mapping.watcher.service_fiber = art.core.fiber.create(art.cluster.mapping.watcher.service)
                    art.cluster.mapping.watcher.watchdog_fiber = art.core.fiber.create(art.cluster.mapping.watcher.watchdog)
                end,

                watchdog = function()
                    while(true) do
                        if (art.core.fiber.status(art.cluster.mapping.watcher.service_fiber) == 'dead') then
                            art.cluster.mapping.watcher.service_fiber = art.core.fiber.create(art.cluster.mapping.watcher.service)
                        end
                        art.core.fiber.sleep(5)

                    end
                end,

                service = function()
                    local batches = {}
                    local prev_iteration_batches_count = 0
                    local spaces
                    local min_timestamp = 0xffffffffffffffffULL --max unsigned int64

                    while(true) do
                        spaces = box.space.mapping_watched_spaces:select()

                        for _, v in pairs(spaces) do
                            local batch = art.cluster.mapping.watcher.collect_updates(v)
                            if (batch) then
                                if (batch[1][1] < min_timestamp) then min_timestamp = batch[1][1] end
                                table.insert(batches, {v.space, batch})
                            end
                        end

                        if (#batches >= art.cluster.mapping.max_batches_count) or (#batches == prev_iteration_batches_count) then
                            art.cluster.mapping.watcher.send(batches)
                            batches = {}
                            if (min_timestamp < 0xffffffffffffffffULL) then art.cluster.mapping.last_upload_min_timestamp = min_timestamp end
                            art.core.fiber.sleep(art.cluster.mapping.watcher.timeout)
                        end

                        prev_iteration_batches_count = #batches
                    end
                end,

                collect_updates = function(watched_space)
                    if not (art.cluster.mapping.watcher.last_collected_timestamps[watched_space.space]) then
                        art.cluster.mapping.watcher.last_collected_timestamps[watched_space.space] = art.cluster.mapping.last_upload_min_timestamp
                    end

                    local batch = {}
                    for _,v in art.core.mapping_updates_of(watched_space.space).index.timestamp:pairs(
                            art.cluster.mapping.watcher.last_collected_timestamps[watched_space.space], 'GT') do
                        table.insert(batch, v)
                        if #batch == watched_space.batch_size then break end
                    end

                    if batch[1] then
                        art.cluster.mapping.watcher.last_collected_timestamps[watched_space.space] = batch[#batch][1]
                        return batch
                    end
                end,

                send = function(batches)
                    if not(batches[1]) then return end
                    snd = batches
                    while true do
                        if pcall(art.cluster.mapping.watcher.call(art.cluster.mapping.nodes[art.cluster.mapping.primary_node_uuid],
                                'art.cluster.mapping.save_to_pending', {batches})) then break end
                        art.core.fiber.sleep(art.cluster.mapping.network_manager.checkup_timeout)
                    end
                end,

                call = function(connection, func, args)
                    connection:call(func, args)
                end
            },

            garbage_collector = {
                timeout = 0.1,
                service_fiber = nil,
                watchdog_fiber = nil,

                start = function()
                    art.cluster.mapping.garbage_collector.service_fiber = art.core.fiber.create(art.cluster.mapping.garbage_collector.service)
                    art.cluster.mapping.garbage_collector.watchdog_fiber = art.core.fiber.create(art.cluster.mapping.garbage_collector.watchdog)
                end,

                watchdog = function()
                    while(true) do
                        if (art.core.fiber.status(art.cluster.mapping.garbage_collector.service_fiber) == 'dead') then
                            art.cluster.mapping.garbage_collector.service_fiber = art.core.fiber.create(art.cluster.mapping.garbage_collector.service)
                        end
                        art.core.fiber.sleep(5)

                    end
                end,

                service = function()
                    while true do
                        local watched_spaces = box.space.mapping_watched_spaces:select()
                        for _, v in pairs(watched_spaces) do
                            art.cluster.mapping.garbage_collector.cleanup_space(v.space)
                        end
                        art.core.fiber.sleep(art.cluster.mapping.garbage_collector.timeout)

                    end
                end,

                cleanup_space = function(space)
                    for _, v in art.core.mapping_updates_of(space).index.timestamp:pairs(art.cluster.mapping.last_upload_min_timestamp, 'LT') do
                        art.core.mapping_updates_of(space):delete(v:transform(1, 3))
                    end
                end
            },

            space = {
                create = function(space)
                    box.schema.space.create(space .. art.constants.mapping_space_postfix)
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

                    box.space.mapping_watched_spaces:insert(box.tuple.new({space, {}, art.cluster.mapping.default_batch_size }))
                end,

                watch_index = function(space, index_obj)
                    if not (art.core.mapping_updates_of(space)) then return end
                    if (index_obj.id == 0) then art.cluster.mapping.space.init_space(space, index_obj) end

                    local watched_space = box.space.mapping_watched_spaces:get(space)
                    local watched_fields = watched_space[2]

                    for _, v in pairs(index_obj.parts) do
                        if not (watched_fields[v.fieldno]) then watched_fields[v.fieldno] = 0 end
                        watched_fields[v.fieldno] = watched_fields[v.fieldno] + 1
                    end

                    watched_space = watched_space:update({{'=', 2, watched_fields}})
                    box.space.mapping_watched_spaces:replace(watched_space)
                end,

                rename = function(space, name)
                    if(art.core.mapping_updates_of(space)) then
                        art.core.mapping_updates_of(space):rename(name .. art.constants.mapping_space_postfix)
                        local watched_space = box.space.mapping_watched_spaces:delete(space)
                        box.space.mapping_watched_spaces:insert(watched_space:update({{'=', 1, name}}))
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
                        box.space.mapping_watched_spaces:delete(space)
                    end
                end,

            },

            network_manager = {
                checkup_timeout = 0.2,

                start = function()
                    art.cluster.mapping.network_manager.watchdog_fiber = art.core.fiber.create(art.cluster.mapping.network_manager.watchdog)
                end,

                watchdog_fiber = nil,

                watchdog = function()
                    while(true) do
                        for uri, connection in pairs(art.cluster.mapping.nodes) do
                            if ( (not(connection)) or (not connection:is_connected()) ) then art.cluster.mapping.network_manager.connect(uri) end
                        end
                        art.core.fiber.sleep(art.cluster.mapping.network_manager.checkup_timeout)
                    end
                end,

                connect = function(uri)
                    art.cluster.mapping.nodes[uri] = require('net.box').connect(uri)
                end,

            }
        },
    },

    api = {
        get = function(space, id)
            return box.atomic(art.box.get, space, id)
        end,

        delete = function(space, id)
            local result = box.atomic(art.box.delete, space, id)
            return result
        end,

        insert = function(space, data, bucket_id)
            local result = box.atomic(art.box.insert, space, data, bucket_id)
            return result
        end,

        auto_increment = function(space, data, bucket_id)
            local result = box.atomic(art.box.auto_increment, space, data, bucket_id)
            return result
        end,

        put = function(space, data, bucket_id)
            local result =  box.atomic(art.box.put, space, data, bucket_id)
            return result
        end,

        update = function(space, id, commands, bucket_id)
            local result = box.atomic(art.box.update, space, id, commands, bucket_id)
            return result
        end,

        replace = function(space, data, bucket_id)
            local result = box.atomic(art.box.replace, space, data, bucket_id)
            return result
        end,

        upsert = function(space, data, commands, bucket_id)
            local result = box.atomic(art.box.upsert, space, data, commands, bucket_id)
            return result
        end,

        select = function(space, request)
            return box.atomic(art.box.select, space, request)
        end,

        space = {
            create = function(name, config)
                art.box.space.wait_for_clustered_op()
                return box.atomic(art.box.space.create, name, config)
            end,

            format = function(space, format)
                art.box.space.wait_for_clustered_op()
                return box.atomic(art.box.space.format, space, format)
            end,

            create_index = function(space, index_name, index)
                art.box.space.wait_for_clustered_op()
                return box.atomic(art.box.space.create_index, space, index_name, index)
            end,

            rename = function(space, new_name)
                art.box.space.wait_for_clustered_op()
                return box.atomic(art.box.space.rename, space, new_name)
            end,

            truncate = function(space)
                return box.atomic(art.box.space.truncate, space)
            end,

            drop = function(space)
                art.box.space.wait_for_clustered_op()
                return box.atomic(art.box.space.drop, space)
            end,

            count = function(space)
                return art.box.space.count(space)
            end,

            schema_count = function(space)
                return art.box.space.schema_count(space)
            end,

            len = function(space)
                return art.box.space.len(space)
            end,

            schema_len = function(space)
                return art.box.space.schema_len(space)
            end,

            list = function()
                local result = {}
                for _,v in pairs(box.space._space:select()) do
                    if not (string.startswith(v[3], '_')) then table.insert(result, v[3]) end
                end
                return result
            end,

            list_indices = function(space)
                local temp = {}
                local result = {}
                for _, v in pairs(box.space[space].index) do
                    temp[v.name] = true
                end
                for k in pairs(temp) do
                    table.insert(result, k)
                end
                return result
            end
        }
    } --public API
}

art.cluster.mapping.init(mapping_uri)
return art
