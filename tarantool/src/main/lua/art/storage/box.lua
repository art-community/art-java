local box = {
    get = function(space, key, index)
        if not(index) then index = 0 end
        local data = box.space[space].index[index]:get(key)
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

    select = function(space, request, index)
        if not (index) then index = 0 end
        local response_entry = {}
        local result = {}
        local response = box.space[space].index[index]:select(request)
        if response[1] == nil then return {} end
        for _,entry in pairs(response) do
            response_entry[1] = entry:transform(#entry, 1)
            response_entry[2] = art.core.schema_of(space):get(entry[#entry]).schema
            table.insert(result, response_entry)
        end

        return result
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

        drop_index = function(space, name)
            art.cluster.mapping.space.unwatch_index(space, box.space[space].index[name])
            box.space[space].index[name]:drop()
        end,

        truncate = function(space)
            box.space[space]:truncate()
            art.core.schema_of(space):truncate()
            art.cluster.mapping.space.truncate(space)
        end,

        rename = function(space, name)
            art.core.schema_of(space):rename('_' .. name .. art.config.schema_postfix)
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
            if not(config) then config = {} end
            box.schema.space.create(name, config)
            config.field_count = nil
            config.id = nil
            config.format = {
                {name = 'hash', type = 'unsigned'},
                {name = 'count', type = 'unsigned'},
                {name = 'schema', type = 'any'}
            }
            local schema = box.schema.space.create('_' .. name .. art.config.schema_postfix, config)
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
            local schema = box.schema.space.create('_' .. name .. art.config.schema_postfix, config)
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
}

return box