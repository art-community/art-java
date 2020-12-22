local api = {
    get = function(space, key, index)
        return box.atomic(art.box.get, space, key, index)
    end,

    getBatch = function(space, keys)
        return art.box.getBatch(space, keys)
    end,

    delete = function(space, id)
        local result = box.atomic(art.box.delete, space, id)
        return result
    end,

    insert = function(space, data, bucket_id)
        local result = box.atomic(art.box.insert, space, data, bucket_id)
        return result
    end,

    autoIncrement = function(space, data, bucket_id)
        local result = box.atomic(art.box.autoIncrement, space, data, bucket_id)
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

    select = function(space, request, index)
        return box.atomic(art.box.select, space, request, index)
    end,

    space = {
        create = function(name, config)
            art.box.space.waitForClusterOperation()
            return box.atomic(art.box.space.create, name, config)
        end,

        format = function(space, format)
            art.box.space.waitForClusterOperation()
            return box.atomic(art.box.space.format, space, format)
        end,

        createIndex = function(space, index_name, index)
            art.box.space.waitForClusterOperation()
            local result = box.atomic(art.box.space.createIndex, space, index_name, index)
            return result
        end,

        dropIndex = function(space, index_name)
            art.box.space.waitForClusterOperation()
            local result = box.atomic(art.box.space.dropIndex, space, index_name)
        end,

        rename = function(space, new_name)
            art.box.space.waitForClusterOperation()
            return box.atomic(art.box.space.rename, space, new_name)
        end,

        truncate = function(space)
            return box.atomic(art.box.space.truncate, space)
        end,

        drop = function(space)
            art.box.space.waitForClusterOperation()
            return box.atomic(art.box.space.drop, space)
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
    }
}

return api