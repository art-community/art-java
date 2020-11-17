--
--ART module for tarantool boxes
--

art_svc = {
    schema_suffix = '_schema',

    mapping_space_suffix = '_mapping_updates',

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

    get = function(space, id)
        local data = box.space[space]:get(id)
        if (data == nil) then return {'', ''} end
        local schema_hash = data[#data]
        local response = data:transform(#data, 1)
        local response_schema = box.space[space .. art_svc.schema_suffix]:get(schema_hash)['schema']
        return {response, response_schema}
    end,

    delete = function(space, id)
        local data = box.space[space]:get(id)
        if data == nil then return {'', ''} end
        local schema_hash = data[#data]
        local response = data:transform(#data, 1)

        local response_schema = box.space[space .. art_svc.schema_suffix]:get(schema_hash)

        if (response_schema['count'] == 1) then
            box.space[space .. art_svc.schema_suffix]:delete(schema_hash)
        else
            box.space[space .. art_svc.schema_suffix]:update(schema_hash, {{'-', 'count', 1}})
        end
        box.space[space]:delete(id)

        art_svc.vshard.mapping.delete(space, id)

        response_schema = response_schema['schema']
        return {response, response_schema}
    end,

    insert = function(space, data, bucket_id)
        data[1] = box.tuple.new(data[1])
        data[2] = box.tuple.new(data[2])
        local schema_hash = art_svc.hash({data[2], bucket_id})
        data[1] = data[1]:update({{'!', #data[1]+1, schema_hash}})
        box.space[space]:insert(data[1])

        local schema_tuple = box.tuple.new({schema_hash, 1, data[2], bucket_id})
        box.space[space .. art_svc.schema_suffix]:upsert(schema_tuple, {{'+', 'count', 1}})

        art_svc.vshard.mapping.put(space, data[1])

        return {data[1]:transform(#data[1], 1), data[2]}
    end,

    auto_increment = function(space, data, bucket_id)
        data[1] = box.tuple.new(data[1])
        data[2] = box.tuple.new(data[2])
        local schema_hash = art_svc.hash({data[2], bucket_id})

        data[1] = data[1]:update({{'!', #data[1]+1, schema_hash}, {'#', 1, 1}})
        data[1] = box.space[space]:auto_increment(data[1]:totable())


        local schema_tuple = box.tuple.new({schema_hash, 1, data[2], bucket_id})
        box.space[space .. art_svc.schema_suffix]:upsert(schema_tuple, {{'+', 'count', 1}})

        art_svc.vshard.mapping.put(space, data[1])

        return {data[1]:transform(-1, 1), data[2]}
    end,

    put = function(space, data, bucket_id)
        art_svc.delete(space, data[1][1])
        local response = art_svc.insert(space, data, bucket_id)
        return response
    end,

    update = function(space, id, commands, bucket_id)
        local data = art_svc.get(space, id)
        data[1] = data[1]:update(commands[1])
        data[2] = box.tuple.new(data[2]):update(commands[2]):totable()
        art_svc.put(space, data, bucket_id)
        return data
    end,

    replace = function(space, data, bucket_id)
        return art_svc.put(space, data, bucket_id)
    end,

    upsert = function(space, data, commands, bucket_id)
        local id = data[1][1]
        if box.space[space]:get(id) then
            return art_svc.update(space, id, commands, bucket_id)
        else
            return art_svc.insert(space, data, bucket_id)
        end
    end,

    select = function(space, request)
        local response = {}
        local response_entry = {}
        local list = box.space[space]:select(request)
        for index = 1, #list do
            response_entry[1], response_entry[2] = art_svc.get(space, list[index][1])
            response[index] = response_entry
        end
        if response[1] == nil then return {} end
        return response
    end,

    space = {

        cluster_op_in_progress = false,

        wait_for_clustered_op = function()
            while art_svc.space.cluster_op_in_progress do
                art_svc.fiber.sleep(1)
            end
        end,

        count = function(space)
            return box.space[space]:count()
        end,

        schema_count = function(space)
            return box.space[space .. art_svc.schema_suffix]:count()
        end,

        len = function(space)
            return box.space[space]:len()
        end,

        schema_len = function(space)
            return box.space[space .. art_svc.schema_suffix]:len()
        end,

        create_index = function(space, name, index)
            art_svc.vshard.mapping.space.create_index(space, name, index)
            return box.space[space]:create_index(name, index)
        end,

        truncate = function(space)
            box.space[space]:truncate()
            box.space[space .. art_svc.schema_suffix]:truncate()
            art_svc.vshard.mapping.space.truncate(space)
        end,

        rename = function(space, name)
            box.space[space .. art_svc.schema_suffix]:rename(name .. art_svc.schema_suffix)
            art_svc.vshard.mapping.space.rename(space, name)
            return box.space[space]:rename(name)
        end,

        format = function(space, format)
            art_svc.vshard.mapping.space.format(space, format)
            return box.space[space]:format(format)
        end,

        drop = function(space)
            box.space[space]:drop()
            art_svc.vshard.mapping.space.unwatch(space)
            box.space[space .. art_svc.schema_suffix]:drop()
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
            local schema = box.schema.space.create(name .. art_svc.schema_suffix, config)
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
            local schema = box.schema.space.create(name .. art_svc.schema_suffix, config)
            schema:create_index('primary', {
                type = 'tree',
                if_not_exists = true,
                parts = {'hash'}
            })
            art_svc.vshard.mapping.space.watch(name, config)
        end

    },

    vshard = {
        space = {
            check_op_availability = function(operation, args)
                art_svc.space.wait_for_clustered_op()
                art_svc.space.cluster_op_in_progress = true
                local result = {}
                box.begin()
                result[1], result[2] = pcall(art_svc.space[operation], unpack(args))
                box.rollback()
                return result
            end,

            cancel_op = function()
                art_svc.space.cluster_op_in_progress = false
            end,

            execute_op = function(operation, args)
                local result = {}
                box.begin()
                result[1], result[2] = pcall(art_svc.space[operation], unpack(args))
                if(result[1])
                    then box.commit()
                    else box.rollback()
                end
                art_svc.space.cluster_op_in_progress = false
                return result
            end
        },

        mapping = {
            watched_spaces = {},
            uri = {},
            nodes = {},
            primary_node = '',

            init = function(uri_list)
                art_svc.vshard.mapping.uri = uri_list

                if not (box.space.mapping_watched_spaces) then
                    box.schema.space.create('mapping_watched_spaces')
                    box.space.mapping_watched_spaces:create_index('primary', {parts = {1}})
                end
                art_svc.vshard.mapping.watched_spaces = box.space.mapping_watched_spaces:select()

                local this_rs_uuid = art_svc.vshard.storage.internal.this_replicaset.uuid
                local netbox = require('net.box')
                for k,v in pairs(art_svc.vshard.index.uri) do
                    table.insert(art_svc.vshard.index.nodes, netbox.connect(v))
                end
                art_svc.vshard.index.primary_node = netbox.connect(art_svc.vshard.index.uri[this_rs_uuid])
            end,

            put = function(space, data)
            end,

            delete = function(space, key)
            end,

            builder = {
                build = function(space, field)

                end
            },

            watcher = {},

            space = {
                watch = function(space, config)
                    box.schema.space.create(space .. art_svc.mapping_space_suffix)
                    art_svc.vshard.mapping.space.format(space, config['format'])
                    box.space[space .. art_svc.mapping_space_suffix]:create_index('mapping_primary_index', {parts = {1}})
                end,

                format = function(space, format)

                end,

                create_index = function(space, name, index) end,

                rename = function(space, name)
                    if(box.space[space .. art_svc.mapping_space_suffix]) then
                        box.space[space .. art_svc.mapping_space_suffix]:rename(name .. art_svc.mapping_space_suffix)
                    end
                end,

                truncate = function(space) end,

                unwatch = function(space)
                    if(box.space[space .. art_svc.mapping_space_suffix]) then
                        box.space[space .. art_svc.mapping_space_suffix]:drop()
                    end
                end,

            }
        },


    }
}

art = {
    get = function(space, id)
        return box.atomic(art_svc.get, space, id)
    end,

    delete = function(space, id)
        local result = box.atomic(art_svc.delete, space, id)
        return result
    end,

    insert = function(space, data, bucket_id)
        local result = box.atomic(art_svc.insert, space, data, bucket_id)
        return result
    end,

    auto_increment = function(space, data, bucket_id)
        local result = box.atomic(art_svc.auto_increment, space, data, bucket_id)
        return result
    end,

    put = function(space, data, bucket_id)
        local result =  box.atomic(art_svc.put, space, data, bucket_id)
        return result
    end,

    update = function(space, id, commands, bucket_id)
        local result = box.atomic(art_svc.update, space, id, commands, bucket_id)
        return result
    end,

    replace = function(space, data, bucket_id)
        local result = box.atomic(art_svc.replace, space, data, bucket_id)
        return result
    end,

    upsert = function(space, data, commands, bucket_id)
        local result = box.atomic(art_svc.upsert, space, data, commands, bucket_id)
        return result
    end,

    select = function(space, request)
        return box.atomic(art_svc.select, space, request)
    end,

    space = {
        create = function(name, config)
            art_svc.space.wait_for_clustered_op()
            return box.atomic(art_svc.space.create, name, config)
        end,

        format = function(space, format)
            art_svc.space.wait_for_clustered_op()
            return box.atomic(art_svc.space.format, space, format)
        end,

        create_index = function(space, index_name, index)
            art_svc.space.wait_for_clustered_op()
            return box.atomic(art_svc.space.create_index, space, index_name, index)
        end,

        rename = function(space, new_name)
            art_svc.space.wait_for_clustered_op()
            return box.atomic(art_svc.space.rename, space, new_name)
        end,

        truncate = function(space)
            return box.atomic(art_svc.space.truncate, space)
        end,

        drop = function(space)
            art_svc.space.wait_for_clustered_op()
            return box.atomic(art_svc.space.drop, space)
        end,

        count = function(space)
            return art_svc.space.count(space)
        end,

        schema_count = function(space)
            return art_svc.space.schema_count(space)
        end,

        len = function(space)
            return art_svc.space.len(space)
        end,

        schema_len = function(space)
            return art_svc.space.schema_len(space)
        end
    }
} --public API

return art
