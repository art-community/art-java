--transaction request format: {string function, {arg1, ... argN} }
--arg format: {arg} or {dependency:{prev_response_index}} or {dependency:{prev_response_index, fieldname}}
--bucket mapping:
-- null means 'impossible to get bucket from provided operation, next'
-- >0 means 'got bucket, let`s try'

local transaction = {
    execute = function(transaction)
        if not(art.transaction.isSafe(transaction)) then return false, 'Transaction contains unsafe operations for sharded cluster' end
        local bucket_id = art.transaction.mapBucket(transaction)
        if not(bucket_id) then return false, 'Failed to get bucket_id for transaction operations' end
        return vshard.router.callrw(bucket_id, 'art.transaction.execute', {transaction})
    end,

    isSafe = function(transaction)
        for _, operation in pairs(transaction) do
            if (string.startswith(operation[1], 'art.api.space') and (not (operation[1]:find('list'))) ) then return false end
            if (operation[1] == 'art.api.select') then return false end
        end
        return true
    end,

    mapBucket = function(transaction)
        local bucket
        for _, operation in pairs(transaction) do
            bucket = art.transaction.bucketMappers[operation[1]](operation[2])
            if (bucket) then return bucket end
        end
    end,

    bucketMappers = {},
}

local mappers = {}

local function mapFromKey(args)
    if not(args[2].dependency) then return art.core.bucketFromKey(args[1], args[2]) end
end

local function mapFromData(args)
    if not(args[2].dependency) then return art.core.bucketFromData(args[1], args[2]) end
end

local function noMap()
    return nil
end

mappers['art.api.space.list'] = noMap
mappers['art.api.space.listIndices'] = noMap

mappers['art.api.get'] = mapFromKey
mappers['art.api.delete'] = mapFromKey
mappers['art.api.update'] = mapFromKey

mappers['art.api.insert'] = mapFromData
mappers['art.api.put'] = mapFromData
mappers['art.api.autoIncrement'] = mapFromData
mappers['art.api.replace'] = mapFromData
mappers['art.api.upsert'] = mapFromData

transaction.bucketMappers = mappers

return transaction