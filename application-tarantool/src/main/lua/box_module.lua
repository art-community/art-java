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
        local response = {'',''}
        local data = box.space[space]:get(id)
        if (data == nil) then return response end
        local schema_hash = data[#data]
        response[1] = data:transform(#data, 1)
        response[2] = box.space[space .. art.schema_suffix]:get(schema_hash):transform(1, 2)
        return response
    end,

    delete = function(space, id)
        local response = {'',''}
        local data = box.space[space]:get(id)
        if (data == nil) then return {'',''} end

        local schema_hash = data[#data]
        response[1] = data:transform(#data, 1)
        response[2] = box.space[space .. art.schema_suffix]:get(schema_hash)
        if (response[2]['count'] == 1) then
            box.space[space .. art.schema_suffix]:delete(schema_hash)
        else
            box.space[space .. art.schema_suffix]:update(schema_hash, {{'-', 'count', 1}})
        end
        box.space[space]:delete(id)
        response[2] = response[2]:transform(1, 2)
        return response
    end,

    insert = function(space, data)
        local id = data[1][1]
        if (box.space[space]:get(id)) then return {'',''} end
        local schema_hash = art.hash(data[2])
        box.space[space]:insert(data[1]:transform(#data[1]+1, 1, schema_hash))
        local schema_tuple = data[2]:update({{'!', 1, schema_hash},{'!', 2, 1}})
        box.space[space .. art.schema_suffix]:upsert(schema_tuple, {{'+', 'count', 1}})
        return data
    end,

    auto_increment = function(space, data)
        local result = {'', data[2]}
        local schema_hash = art.hash(data[2])
        result[1] = box.space[space]:auto_increment(data[1]:update({{'!', #data[1] + 1, schema_hash}}):transform(1,1):totable())
        local schema_tuple = data[2]:update({{'!', 1, schema_hash}, {'!', 2, 1}})
        box.space[space .. art.schema_suffix]:upsert(schema_tuple, {{'+', 'count', 1}})
        return result
    end,

    put = function(space, data)
        local id = data[1][1]
        box.begin()
        art.delete(space, id)
        local result = art.insert(space, data)
        box.commit()
        return result
    end,

    update = function(space, id, commands)
        local data = art.get(space, id)
        data[1] = data[1]:update(commands[1])
        data[2] = data[2]:update(commands[2])
        box.begin()
        art.delete(space, id)
        art.insert(space, data) --проверить сначала, влезет ли оно вообще в базу. или бэкап чтобы если что восстановиться
        box.commit()
        return data
    end,

    replace = function(space, data)
        local id = data[1][1]
        box.begin()
        art.delete(space, id)
        art.insert(space, data)
        box.commit()
    end,

    upsert = function(space, data, commands)
        local id = data[1][1]
        if box.space[space]:get(id) then
        return art.update(space, id, commands)
        else
        return art.insert(space, data)
        end
    end,

    select = function(space, request)
        local results = {}
        local list = box.space[space]:select(request)
        for index = 1, #list do
            results[index] = art.get(space, list[index][1])
        end
        return results
    end,

    space = {
        count = function(space)
            return {box.space[space]:count(), box.space[space .. art.schema_suffix]:count()}
        end,

        create_index = function(space, index_name, index)
            return box.space[space]:create_index(index_name, index)
        end,

        truncate = function(space)
            return {box.space[space]:truncate(), box.space[space .. art.schema_suffix]:truncate()}
        end,

        rename = function(space, name)
            return {box.space[space]:rename(name), box.space[space .. art.schema_suffix]:rename(name .. art.schema_suffix)}
        end,

        format = function(space, format)
            return box.space[space]:format(format)
        end,

        drop = function(space)
            return {box.space[space]:drop(), box.space[space .. art.schema_suffix]:drop()}
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
            return result
        end

}}

return art
