--
--ART module for tarantool boxes
--

art = {
    schema_suffix = 'Schema',

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
        if (data == nil) then return '', '' end
        local schema_hash = data[#data]
        local response = data:transform(#data, 1)
        local response_schema = box.space[space .. art.schema_suffix]:get(schema_hash):transform(1, 2)
        return response, response_schema
    end,

    delete = function(space, id)
        local data = box.space[space]:get(id)
        if (data == nil) then return '', '' end

        local schema_hash = data[#data]
        local response = data:transform(#data, 1)
        local response_schema = box.space[space .. art.schema_suffix]:get(schema_hash)
        if (response_schema['count'] == 1) then
            box.space[space .. art.schema_suffix]:delete(schema_hash)
        else
            box.space[space .. art.schema_suffix]:update(schema_hash, {{'-', 'count', 1}})
        end
        box.space[space]:delete(id)
        response_schema = response_schema:transform(1, 2)
        return response, response_schema
    end,

    insert = function(space, data)
        data[1] = box.tuple.new(data[1])
        data[2] = box.tuple.new(data[2])
        local id = data[1][1]
        if (box.space[space]:get(id)) then return '','' end
        local schema_hash = art.hash(data[2])
        box.space[space]:insert(data[1]:transform(#data[1]+1, 1, schema_hash))
        local schema_tuple = data[2]:update({{'!', 1, schema_hash},{'!', 2, 1}})
        box.space[space .. art.schema_suffix]:upsert(schema_tuple, {{'+', 'count', 1}})
        return data[1], data[2]
    end,

    auto_increment = function(space, data)
        data[1] = box.tuple.new(data[1])
        data[2] = box.tuple.new(data[2])
        local response_schema = data[2]
        local schema_hash = art.hash(response_schema)
        local response = box.space[space]:auto_increment(data[1]:update({{'!', #data[1] + 1, schema_hash}}):transform(1,1):totable())
        local schema_tuple = response_schema:update({{'!', 1, schema_hash}, {'!', 2, 1}})
        box.space[space .. art.schema_suffix]:upsert(schema_tuple, {{'+', 'count', 1}})
        return response, response_schema
    end,

    put = function(space, data)
        data[1] = box.tuple.new(data[1])
        data[2] = box.tuple.new(data[2])
        local id = data[1][1]
        box.begin()
        art.delete(space, id)
        local response, response_schema = art.insert(space, data)
        box.commit()
        return response, response_schema
    end,

    update = function(space, id, commands)
        local data, data_schema = art.get(space, id)
        data = data:update(commands[1])
        data_schema = data_schema:update(commands[2])
        box.begin()
        art.delete(space, id)
        art.insert(space, {data, data_schema}) --а оно так будет работать?
        box.commit()
        return data, data_schema
    end,

    replace = function(space, data)
        data[1] = box.tuple.new(data[1])
        data[2] = box.tuple.new(data[2])
        local id = data[1][1]
        art.delete(space, id)
        return art.insert(space, data)
    end,

    upsert = function(space, data, commands)
        data[1] = box.tuple.new(data[1])
        data[2] = box.tuple.new(data[2])
        return atomic(function()
            local id = data[1][1]
            if box.space[space]:get(id) then
            return art.update(space, id, commands)
            else
            return art.insert(space, data)
            end
        end)
    end,

    select = function(space, request)
        local response = {}
        local response_schema = {}
        local list = box.space[space]:select(request)
        for index = 1, #list do
            response[index], response_schema[index] = art.get(space, list[index][1])
        end
        return response, response_schema
    end,

    space = {
        count = function(space)
            return box.space[space]:count()
        end,

        schema_count = function(space)
            return box.space[space .. art.schema_suffix]:count()
        end,

        len = function(space)
            return box.space[space]:len()
        end,

        schema_len = function(space)
            return box.space[space .. art.schema_suffix]:len()
        end,

        create_index = function(space, index_name, index)
            return box.space[space]:create_index(index_name, index)
        end,

        truncate = function(space)
            return box.space[space]:truncate(), box.space[space .. art.schema_suffix]:truncate()
        end,

        rename = function(space, name)
            return box.space[space]:rename(name), box.space[space .. art.schema_suffix]:rename(name .. art.schema_suffix)
        end,

        format = function(space, format)
            return box.space[space]:format(format)
        end,

        drop = function(space)
            return box.space[space]:drop(), box.space[space .. art.schema_suffix]:drop()
        end,

        create = function(space)
            local result = {}
            result [1] = box.schema.space.create(space)
            result [2] = box.schema.space.create(space .. art.schema_suffix)
            result[2]:format({
                {name = 'hash', type = 'unsigned'},
                {name = 'count', type = 'unsigned'}
                })
            result[2]:create_index('primary', {
                type = 'tree',
                parts = {'hash'}
                })
            return result[1], result[2]
        end

}}

return art
