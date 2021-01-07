local box = {
    get = function(space, key, index)
        if not(index) then index = 0 end
        local data = box.space[space].index[index]:get(key)
        if (data == nil) then return {'', ''} end
        local schema_hash = data[#data]
        local response = data:transform(#data, 1)
        local response_schema = art.core.schemaOf(space):get(schema_hash)['schema']
        return {response, response_schema}
    end,

    getBatch = function(space, keys)
        local result = {}
        for _, key in pairs(keys) do
            table.insert(result, art.api.get(space, key))
        end
        return result
    end,

    delete = function(space, key)
        local data = box.space[space]:get(key)
        if data == nil then return {'', ''} end
        local schema_hash = data[#data]
        local response = data:transform(#data, 1)

        local response_schema = art.core.schemaOf(space):get(schema_hash)

        if (response_schema['count'] == 1) then
            art.core.schemaOf(space):delete(schema_hash)
        else
            art.core.schemaOf(space):update(schema_hash, { { '-', 'count', 1}})
        end
        box.space[space]:delete(key)

        art.cluster.mapping.delete(space, key)

        response_schema = response_schema['schema']
        return {response, response_schema}
    end,

    insert = function(space, data)
        local tuple = {}
        local bucket_id = art.core.bucketFromData(space, data)
        tuple[1] = box.tuple.new(data[1])
        tuple[2] = box.tuple.new(data[2])
        local schema_hash = art.core.hash({ tuple[2], bucket_id})
        tuple[1] = tuple[1]:update({{'!', #tuple[1]+1, schema_hash}})
        box.space[space]:insert(tuple[1])

        local schema_tuple = box.tuple.new({schema_hash, 1, tuple[2], bucket_id})
        art.core.schemaOf(space):upsert(schema_tuple, { { '+', 'count', 1}})

        art.cluster.mapping.put(space, tuple[1])
        tuple[1] = tuple[1]:transform(#tuple[1], 1)
        return tuple
    end,

    autoIncrement = function(space, data)
        local tuple = {}
        local bucket_id = art.core.bucketFromData(space, data)
        tuple[1] = box.tuple.new(data[1])
        tuple[2] = box.tuple.new(data[2])
        local schema_hash = art.core.hash({ tuple[2], bucket_id})

        tuple[1] = tuple[1]:update({{'!', #tuple[1]+1, schema_hash}, {'#', 1, 1}})
        tuple[1] = box.space[space]:auto_increment(tuple[1]:totable())


        local schema_tuple = box.tuple.new({schema_hash, 1, tuple[2], bucket_id})
        art.core.schemaOf(space):upsert(schema_tuple, { { '+', 'count', 1}})

        tuple[1] = tuple[1]:transform(#tuple[1], 1)
        art.cluster.mapping.put(space, tuple[1])

        return tuple
    end,

    put = function(space, data)
        local id = {}
        local bucket_id = art.core.bucketFromData(space, data)
        for k,v in pairs(box.space[space].index[0].parts) do
            id[k] = data[1][v.fieldno]
        end
        art.box.delete(space, id)
        return art.box.insert(space, data, bucket_id)
    end,

    update = function(space, key, commands)
        local data = art.box.get(space, key)
        local bucket_id = data.bucket_id
        data[1] = data[1]:update(commands[1])
        data[2] = box.tuple.new(data[2]):update(commands[2]):totable()
        art.box.put(space, data, bucket_id)
        return data
    end,

    replace = function(space, data)
        local bucket_id = art.core.bucketFromData(space, data)
        return art.box.put(space, data, bucket_id)
    end,

    upsert = function(space, data, commands)
        local key = {}
        local bucket_id = art.core.bucketFromData(space, data)
        for k,v in pairs(box.space[space].index[0].parts) do
            key[k] = data[1][v.fieldno]
        end
        if box.space[space]:get(key) then
            return art.box.update(space, key, commands, bucket_id)
        else
            return art.box.insert(space, data, bucket_id)
        end
    end,

    select = function(space, request, index, ...)
        if not (index) then index = 0 end
        local response_entry = {}
        local result = {}
        local response = box.space[space].index[index]:select(request, ...)
        if response[1] == nil then return {} end
        for _,entry in pairs(response) do
            response_entry[1] = entry:transform(#entry, 1)
            response_entry[2] = art.core.schemaOf(space):get(entry[#entry]).schema
            table.insert(result, response_entry)
        end
        return result
    end,

    space = {

        activeClusterOperation = false,

        waitForClusterOperation = function()
            while art.box.space.activeClusterOperation do
                art.core.fiber.sleep(0.5)
            end
        end,

        count = function(space)
            return box.space[space]:count()
        end,

        len = function(space)
            return box.space[space]:len()
        end,

        createIndex = function(space, name, index)
            local index_obj = box.space[space]:create_index(name, index)
            art.cluster.mapping.space.watchIndex(space, index_obj)
            return index_obj
        end,

        dropIndex = function(space, name)
            art.cluster.mapping.space.unwatchIndex(space, box.space[space].index[name])
            box.space[space].index[name]:drop()
            return {}
        end,

        truncate = function(space)
            box.space[space]:truncate()
            art.core.schemaOf(space):truncate()
            art.cluster.mapping.space.truncate(space)
            return {}
        end,

        rename = function(space, name)
            art.core.schemaOf(space):rename('_' .. name .. art.config.schemaPostfix)
            art.cluster.mapping.space.rename(space, name)
            return box.space[space]:rename(name)
        end,

        format = function(space, format)
            return box.space[space]:format(format)
        end,

        drop = function(space)
            box.space[space]:drop()
            art.cluster.mapping.space.unwatch(space)
            art.core.schemaOf(space):drop()
            return {}
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
            local schema = box.schema.space.create('_' .. name .. art.config.schemaPostfix, config)
            schema:create_index('hash', {
                type = 'tree',
                if_not_exists = true,
                parts = {'hash'}
            })
            return {}
        end,

        createVsharded = function(name, config)
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
            local schema = box.schema.space.create('_' .. name .. art.config.schemaPostfix, config)
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
            return {}
        end,

        list = function()
            local result = {}
            for _,v in pairs(box.space._space:select()) do
                if not (string.startswith(v[3], '_')) then table.insert(result, v[3]) end
            end
            return result
        end,

        listIndices = function(space)
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
    },
}

return box