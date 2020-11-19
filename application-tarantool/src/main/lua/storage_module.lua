--
--ART module for tarantool boxes
--

art = {
    fiber = require('fiber'),

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
    
    constants = {
        schema_suffix = '_schema',

        mapping_space_suffix = '_mapping_updates',
    },
    
    box = {
        get = function(space, id)
            local data = box.space[space]:get(id)
            if (data == nil) then return {'', ''} end
            local schema_hash = data[#data]
            local response = data:transform(#data, 1)
            local response_schema = box.space[space .. art.constants.schema_suffix]:get(schema_hash)['schema']
            return {response, response_schema}
        end,

        delete = function(space, id)
            local data = box.space[space]:get(id)
            if data == nil then return {'', ''} end
            local schema_hash = data[#data]
            local response = data:transform(#data, 1)

            local response_schema = box.space[space .. art.constants.schema_suffix]:get(schema_hash)

            if (response_schema['count'] == 1) then
                box.space[space .. art.constants.schema_suffix]:delete(schema_hash)
            else
                box.space[space .. art.constants.schema_suffix]:update(schema_hash, { { '-', 'count', 1}})
            end
            box.space[space]:delete(id)

            art.cluster.mapping.delete(space, id)

            response_schema = response_schema['schema']
            return {response, response_schema}
        end,

        insert = function(space, data, bucket_id)
            data[1] = box.tuple.new(data[1])
            data[2] = box.tuple.new(data[2])
            local schema_hash = art.hash({ data[2], bucket_id})
            data[1] = data[1]:update({{'!', #data[1]+1, schema_hash}})
            box.space[space]:insert(data[1])

            local schema_tuple = box.tuple.new({schema_hash, 1, data[2], bucket_id})
            box.space[space .. art.constants.schema_suffix]:upsert(schema_tuple, { { '+', 'count', 1}})

            art.cluster.mapping.put(space, data[1])

            return {data[1]:transform(#data[1], 1), data[2]}
        end,

        auto_increment = function(space, data, bucket_id)
            data[1] = box.tuple.new(data[1])
            data[2] = box.tuple.new(data[2])
            local schema_hash = art.hash({ data[2], bucket_id})

            data[1] = data[1]:update({{'!', #data[1]+1, schema_hash}, {'#', 1, 1}})
            data[1] = box.space[space]:auto_increment(data[1]:totable())


            local schema_tuple = box.tuple.new({schema_hash, 1, data[2], bucket_id})
            box.space[space .. art.constants.schema_suffix]:upsert(schema_tuple, { { '+', 'count', 1}})

            art.cluster.mapping.put(space, data[1])

            return {data[1]:transform(-1, 1), data[2]}
        end,

        put = function(space, data, bucket_id)
            art.box.delete(space, data[1][1])
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
            local id = data[1][1]
            if box.space[space]:get(id) then
                return art.box.update(space, id, commands, bucket_id)
            else
                return art.box.insert(space, data, bucket_id)
            end
        end,
        
        select = function(space, request)
            local response = {}
            local response_entry = {}
            local list = box.space[space]:select(request)
            for index = 1, #list do
                response_entry[1], response_entry[2] = art.box.get(space, list[index][1])
                response[index] = response_entry
            end
            if response[1] == nil then return {} end
            return response
        end,

        space = {

            cluster_op_in_progress = false,

            wait_for_clustered_op = function()
                while art.box.space.cluster_op_in_progress do
                    art.fiber.sleep(1)
                end
            end,

            count = function(space)
                return box.space[space]:count()
            end,

            schema_count = function(space)
                return box.space[space .. art.constants.schema_suffix]:count()
            end,

            len = function(space)
                return box.space[space]:len()
            end,

            schema_len = function(space)
                return box.space[space .. art.constants.schema_suffix]:len()
            end,

            create_index = function(space, name, index)
                local index_obj = box.space[space]:create_index(name, index)
                art.cluster.mapping.space.watch_index(space, index_obj)
                return index_obj
            end,

            truncate = function(space)
                box.space[space]:truncate()
                box.space[space .. art.constants.schema_suffix]:truncate()
                art.cluster.mapping.space.truncate(space)
            end,

            rename = function(space, name)
                box.space[space .. art.constants.schema_suffix]:rename(name .. art.constants.schema_suffix)
                art.cluster.mapping.space.rename(space, name)
                return box.space[space]:rename(name)
            end,

            format = function(space, format)
                return box.space[space]:format(format)
            end,

            drop = function(space)
                box.space[space]:drop()
                art.cluster.mapping.space.unwatch(space)
                box.space[space .. art.constants.schema_suffix]:drop()
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
                local schema = box.schema.space.create(name .. art.constants.schema_suffix, config)
                schema:create_index('primary', {
                    type = 'tree',
                    if_not_exists = true,
                    parts = {'hash'}
                })
            end,

            create_vsharded = function(name, config)
                box.schema.space.create(name, config)
                config.field_count = nil
                config.id = nil
                config.format = {
                    {name = 'hash', type = 'unsigned'},
                    {name = 'count', type = 'unsigned'},
                    {name = 'schema', type = 'any'},
                    {name = 'bucket_id', type = 'unsigned'}
                }
                local schema = box.schema.space.create(name .. art.constants.schema_suffix, config)
                schema:create_index('primary', {
                    type = 'tree',
                    if_not_exists = true,
                    parts = {'hash'}
                })
                art.cluster.mapping.space.watch(name)
            end
        },
    },

    cluster = {
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
            if(result[1])
            then box.commit()
            else box.rollback()
            end
            art.box.space.cluster_op_in_progress = false
            return result
        end,

        mapping = {
            default_batch_size = 1024,
            max_batches_count = 100,
            uri = {},
            nodes = {},
            primary_node = '',

            last_upload_min_timestamp = 0,

            init = function(uri_list)
                art.cluster.mapping.uri = uri_list

                if not (box.space.mapping_watched_spaces) then
                    box.schema.space.create('mapping_watched_spaces')
                    box.space.mapping_watched_spaces:format({
                        {name = 'space', type = 'string'},
                        {name = 'watched_fields_counter', type = 'array'},
                        {name = 'batch_size', type = 'unsigned'}
                    })
                    box.space.mapping_watched_spaces:create_index('primary', {parts = {1}})
                end

                local this_rs_uuid = vshard.storage.internal.this_replicaset.uuid
                local netbox = require('net.box')
                for _,v in pairs(art.cluster.mapping.uri) do
                    table.insert(art.cluster.mapping.nodes, netbox.connect(v))
                end
                art.cluster.mapping.primary_node = netbox.connect(art.cluster.mapping.uri[this_rs_uuid])

                art.cluster.mapping.watcher.start()
            end,

            put = function(space, data)
            end,

            delete = function(space, key)
            end,

            builder = {
                build = function(space, field)

                end
            },

            watcher = {
                timeout = 1, --watcher sleep time

                iterators = {},

                service_fiber = nil,
                watchdog_fiber = nil,

                start = function()
                    art.cluster.mapping.watcher.service_fiber = art.fiber.create(art.cluster.mapping.watcher.service)
                    art.cluster.mapping.watcher.watchdog_fiber = art.fiber.create(art.cluster.mapping.watcher.watchdog)
                end,

                watchdog = function()
                    while(true) do
                        if (art.fiber.status(art.cluster.mapping.watcher.service_fiber) == 'dead') then
                            art.cluster.mapping.watcher.service_fiber = art.fiber.create(art.cluster.mapping.watcher.service)
                        end
                        art.fiber.sleep(5)

                    end
                end,

                service = function()
                    local batches = {}
                    local prev_iteration_batches_count = 0
                    local spaces
                    local min_timestamps = {}

                    while(true) do
                        spaces = box.space.mapping_watched_spaces:select()

                        for _, v in pairs(spaces) do
                            local batch = art.cluster.mapping.watcher.collect_updates(v)
                            table.insert(batches, batch)
                        end

                        if (#batches >= art.cluster.mapping.max_batches_count) or (#batches == prev_iteration_batches_count) then
                            art.cluster.mapping.watcher.send(batches)
                            batches = {}
                            art.fiber.sleep(art.cluster.mapping.watcher.timeout)
                        end

                        prev_iteration_batches_count = #batches
                    end
                end,

                collect_updates = function(batch, watched_space)
                    return nil
                end,

                send = function(batches)
                    if (#batches < 1) then return
                    end
                end
            },

            garbage_collector = {

            },

            space = {
                watch = function(space)
                    box.schema.space.create(space .. art.constants.mapping_space_suffix)
                    box.space[space .. art.constants.mapping_space_suffix]:format({
                        {name = 'id', type = 'any'},
                        {name = 'timestamp', type = 'unsigned'},
                        {name = 'is_delete', type = 'boolean'},
                        {name = 'data', type = 'any', is_nullable = true}
                    })
                    box.space[space .. art.constants.mapping_space_suffix]:create_index('timestamp', { parts = {2} })

                    local watched_fields_counter = {} --add id and bucket_id to watchlist
                    watched_fields_counter[1] = 1
                    watched_fields_counter[2] = 1

                    box.space.mapping_watched_spaces:insert(box.tuple.new({space, watched_fields_counter, art.cluster.mapping.default_batch_size }))
                end,

                watch_index = function(space, index_obj)
                    local watched_space = box.space.mapping_watched_spaces:get(space)
                    if not (watched_space) then return end
                    for _, v in pairs(index_obj.parts) do
                        if not (watched_space[2][v.fieldno]) then watched_space[2][v.fieldno] = 0 end
                        watched_space[2][v.fieldno] = watched_space[2][v.fieldno] + 1
                    end
                end,

                rename = function(space, name)
                    if(box.space[space .. art.constants.mapping_space_suffix]) then
                        box.space[space .. art.constants.mapping_space_suffix]:rename(name .. art.constants.mapping_space_suffix)
                    end
                end,

                truncate = function(space)
                    if(box.space[space .. art.constants.mapping_space_suffix]) then
                        box.space[space .. art.constants.mapping_space_suffix]:truncate()
                    end
                end,

                unwatch = function(space)
                    if(box.space[space .. art.constants.mapping_space_suffix]) then
                        box.space[space .. art.constants.mapping_space_suffix]:drop()
                        box.space.mapping_watched_spaces:delete(space)
                    end
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
            end
        }
    } --public API
}

return art


