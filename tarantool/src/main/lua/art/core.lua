local core = {
    schemaOf = function(space)
        return box.space['_' .. space .. art.config.schemaPostfix]
    end,

    mappingUpdatesOf = function(space)
        return box.space['_' .. space .. art.config.mappingSpacePostfix]
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

    fiber = require('fiber'),

    functionFromString = function(string)
        return loadstring('return ' .. string)()
    end,

    atomic = function(functionObject, ...)
        if box.is_in_txn() then return functionObject(...) end
        return box.atomic(functionObject, ...)
    end,

    bucketFromData = function(space, data)
        if not(box.space[space].index.bucket_id) then return end
        return data[1][ box.space[space].index.bucket_id.parts[1].fieldno ]
    end,

    bucketFromKey = function(space, key)
        local mapping_entry = box.space[space]:get(key)
        if not (mapping_entry) then return end
        return mapping_entry.bucket_id
    end
}

return core