local api = {
    space = {
        create = function(name, config)
            art.box.space.waitForClusterOperation()
            return art.core.atomic(art.box.space.create, name, config)
        end,

        format = function(space, format)
            art.box.space.waitForClusterOperation()
            return art.core.atomic(art.box.space.format, space, format)
        end,

        createIndex = function(space, index_name, index)
            art.box.space.waitForClusterOperation()
            local result = art.core.atomic(art.box.space.createIndex, space, index_name, index)
            return result
        end,

        dropIndex = function(space, index_name)
            art.box.space.waitForClusterOperation()
            art.core.atomic(art.box.space.dropIndex, space, index_name)
        end,

        rename = function(space, new_name)
            art.box.space.waitForClusterOperation()
            return art.core.atomic(art.box.space.rename, space, new_name)
        end,

        truncate = function(space)
            return art.core.atomic(art.box.space.truncate, space)
        end,

        drop = function(space)
            art.box.space.waitForClusterOperation()
            return art.core.atomic(art.box.space.drop, space)
        end,

        count = function(space)
            return art.box.space.count(space)
        end,

        schemaCount = function(space)
            return art.box.space.schemaCount(space)
        end,

        len = function(space)
            return art.box.space.len(space)
        end,

        schemaLen = function(space)
            return art.box.space.schemaLen(space)
        end,

        list = function()
            return art.box.space.list()
        end,

        listIndices = function(space)
            return art.box.space.listIndices(space)
        end
    },

    transaction = function(requests)
        return unpack(art.transaction.execute(requests))
    end,

    get = function(space, key, index)
        return art.box.get(space, key, index)
    end,

    getBatch = function(space, keys)
        return art.box.getBatch(space, keys)
    end,

    delete = function(space, key)
        local result = art.core.atomic(art.box.delete, space, key)
        return result
    end,

    insert = function(space, data)
        local result = art.core.atomic(art.box.insert, space, data)
        return result
    end,

    autoIncrement = function(space, data)
        local result = art.core.atomic(art.box.autoIncrement, space, data)
        return result
    end,

    put = function(space, data)
        local result =  art.core.atomic(art.box.put, space, data)
        return result
    end,

    update = function(space, id, commands)
        local result = art.core.atomic(art.box.update, space, id, commands)
        return result
    end,

    replace = function(space, data)
        local result = art.core.atomic(art.box.replace, space, data)
        return result
    end,

    upsert = function(space, data, commands)
        local result = art.core.atomic(art.box.upsert, space, data, commands)
        return result
    end,

    select = function(space, request, index, ...)
        return art.core.atomic(art.box.select, space, request, index, ...)
    end
}

return api