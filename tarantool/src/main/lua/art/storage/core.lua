local core = {
    schema_of = function(space)
        return box.space['_' .. space .. art.config.schema_postfix]
    end,

    mapping_updates_of = function(space)
        return box.space['_' .. space .. art.config.mapping_space_postfix]
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
}

return core