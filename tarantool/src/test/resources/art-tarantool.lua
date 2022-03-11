do
local _ENV = _ENV
package.preload[ "art.router.api" ] = function( ... ) local arg = _G.arg;
local api = {
    space = {
        create = function(name, config)
            if not config then config = {} end
            local result = art.cluster.space.execute('createVsharded', {name, config})
            if (result[1]) then
                art.cluster.mapping.space.create(name, config)
            end
            return result
        end,

        format = function(space, format)
            local result = art.cluster.space.execute('format', {space, format})
            if (result[1]) then
                art.cluster.mapping.space.format(space, format)
            end
            return result
        end,

        createIndex = function(space, index_name, index)
            local result = art.cluster.space.execute('createIndex', {space, index_name, index})
            if (result[1]) then
                art.cluster.mapping.space.createIndex(space, index_name, index)
            end
            return result
        end,

        dropIndex = function(space, index_name)
            local result = art.cluster.space.execute('dropIndex', {space, index_name})
            if result[1] then
                art.cluster.mapping.space.dropIndex(space, index_name)
            end
            return result
        end,

        rename = function(space, new_name)
            local result = art.cluster.space.execute('rename', {space, new_name})
            if (result[1]) then
                box.atomic(art.cluster.mapping.space.rename, space, new_name)
            end
            return result
        end,

        truncate = function(space)
            local result = art.cluster.space.execute('truncate', {space})
            if (result[1]) then
                art.cluster.mapping.space.truncate(space)
            end
            return result
        end,

        drop = function(space)
            local result = art.cluster.space.execute('drop', {space})
            if (result[1]) then
                art.cluster.mapping.space.drop(space)
            end
            return result
        end,

        count = function(space)
            local counts = art.cluster.space.execute('count', {space})
            local result = 0
            if (not counts[1]) then return counts end
            for _,v in pairs(counts[2]) do
                result = result + v[2]
            end
            return result
        end,

        len = function(space)
            local counts = art.cluster.space.execute('len', {space})
            local result = 0
            if (not counts[1]) then return counts end
            for _,v in pairs(counts[2]) do
                result = result + v[2]
            end
            return result
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

    transaction = function(requests, bucket_id)
        return unpack(art.transaction.execute(requests, bucket_id))
    end,

    get = function(space, key, index)
        local bucket_id = art.core.mapBucket(space, key)
        if not(bucket_id) then return {{}} end
        return art.core.removeBucket(space, vshard.router.callro(bucket_id, 'art.api.get', {space, key, index}))
    end,

    delete = function(space, key)
        local bucket_id = art.core.mapBucket(space, key)
        if not(bucket_id) then return {{}} end
        return art.core.removeBucket(space, vshard.router.callrw(bucket_id, 'art.api.delete', {space, key}))
    end,

    insert = function(space, data, bucket_id)
        local response = vshard.router.callrw(bucket_id, 'art.api.insert', {space, art.core.insertBucket(space, data, bucket_id)})
        return art.core.removeBucket(space, response)
    end,

    autoIncrement = function(space, data, bucket_id)
        local response = vshard.router.callrw(bucket_id, 'art.api.autoIncrement', {space, art.core.insertBucket(space, data, bucket_id)})
        return art.core.removeBucket(space, response)
    end,

    put = function(space, data, bucket_id)
        local response = vshard.router.callrw(bucket_id, 'art.api.put', {space, art.core.insertBucket(space, data, bucket_id)})
        return art.core.removeBucket(space, response)
    end,

    update = function(space, key, commands)
        local bucket_id = art.core.mapBucket(space, key)
        if not(bucket_id) then return {{}} end
        art.core.correctUpdateOperations(space, commands)
        return art.core.removeBucket(vshard.router.callrw(bucket_id, 'art.api.update', {space, key, commands}))
    end,

    upsert = function(space, data, commands, bucket_id)
        art.core.correctUpdateOperations(space, commands)
        return art.core.removeBucket(space, vshard.router.callrw(bucket_id, 'art.api.upsert', {space, art.core.insertBucket(space, data, bucket_id), commands}))
    end,

    replace = function(space, data, bucket_id)
        local response = vshard.router.callrw(bucket_id, 'art.api.replace', {space, art.core.insertBucket(space, data, bucket_id)})
        return art.core.removeBucket(space, response)
    end,

    select = function(space, request, index, iter, stream)
        if not (index) then index = 0 end
        if not (iter) then iter = 'EQ' end
        if not (stream) then stream = {} end
        local get_requests = {}
        local key_fields_mapping = {}
        local request_entry
        local result = {}
        local bucketFieldNumber = art.core.bucketFieldNumber(space)

        for _,part in pairs(box.space[space].index[0].parts) do
            key_fields_mapping[part.fieldno] = true
        end


        local gen, param, state = box.space[space].index[index]:pairs(request, {iterator = iter})
        for _, operation in pairs(stream) do
            if ((operation[1] == 'filter') or (operation[1] == 'sort')) and (operation[2][2] >= bucketFieldNumber) then
                operation[2][2] = operation[2][2] + 1
            end
            gen, param, state = art.core.stream[operation[1]](gen, param, state, operation[2])
        end
        local mappingResponse = art.core.stream.collect(gen, param, state)
        if mappingResponse[1] == nil then return {} end


        for _,mapping_entry in pairs(mappingResponse) do
            if not (get_requests[mapping_entry.bucket_id]) then get_requests[mapping_entry.bucket_id] = {} end
            request_entry = {}
            for k in pairs(key_fields_mapping) do
                request_entry[k] = mapping_entry[k]
            end
            table.insert(get_requests[mapping_entry.bucket_id], request_entry)
        end

        for bucket_id, batch_req in pairs(get_requests) do
            local response = vshard.router.callro(bucket_id, 'art.api.getBatch', {space, batch_req})
            if (response) then for _,v in pairs(response) do table.insert(result, art.core.removeBucket(space, v)) end end
        end
        if not (result[1]) then return {} end
        return result
    end
}

return api
end
end

do
local _ENV = _ENV
package.preload[ "art.router.config" ] = function( ... ) local arg = _G.arg;
local config = {
    mapping = {
        batchesPerTime = 100,
        timeout = 0.02
    }
}

return config
end
end

do
local _ENV = _ENV
package.preload[ "art.router" ] = function( ... ) local arg = _G.arg;
art = {
    core = require('art.core'),

    config = require('art.router.config'),

    cluster = require('art.router.cluster'),

    transaction = require('art.router.transaction'),

    api = require('art.router.api')
}

art.cluster.mapping()
return art
end
end

do
local _ENV = _ENV
package.preload[ "art.router.transaction" ] = function( ... ) local arg = _G.arg;
--transaction request format: {string function, {arg1, ... argN} }
--arg format: {arg} or {dependency:{prev_response_index}} or {dependency:{prev_response_index, fieldname}}


local transaction = {
    execute = function(transaction, bucket_id)
        if not(bucket_id) then return false, 'Missing bucketId' end
        if not(art.transaction.isSafe(transaction)) then return false, 'Transaction contains unsafe operations for sharded cluster' end
        transaction = art.transaction.insertBuckets(transaction)
        local response = vshard.router.callrw(bucket_id, 'art.transaction.execute', { transaction})
        if response[1] then art.transaction.removeBuckets(transaction, response) end
        return response
    end,

    isSafe = function(transaction)
        for _, operation in pairs(transaction) do
            if (string.startswith(operation[1], 'art.api.space')) then return false end
            if (operation[1] == 'art.api.select') then return false end
        end
        return true
    end,

    insertBuckets = function(transaction)
        local results = {}
        for _, operation in pairs(transaction) do
            local updatedArgs = art.transaction.bucketInserters[operation[1]](operation[2])
            table.insert(results, {operation[1], updatedArgs})
        end
        return results
    end,

    removeBuckets = function(transaction, response)
        for index, tuple in pairs(response[2]) do
            response[2][index] = {art.core.removeBucket(transaction[index][2][1], tuple[1])}
        end
    end,

    bucketInserters = {},
}

local inserters = {}

local function insertBucket(args)
    if (args[2].dependency) then return args end
    args[2] = art.core.insertBucket(args[1], args[2], args[3])
    return args
end

local function identity(args)
    return args
end

inserters['art.api.get'] = identity
inserters['art.api.delete'] = identity
inserters['art.api.update'] = function(args)
    args[3] = art.core.correctUpdateOperations(args[1], args[3])
    return args
end

inserters['art.api.insert'] = insertBucket
inserters['art.api.put'] = insertBucket
inserters['art.api.autoIncrement'] = insertBucket
inserters['art.api.replace'] = insertBucket
inserters['art.api.upsert'] = function(args)
    args = insertBucket(args)
    args[3] = art.core.correctUpdateOperations(args[1], args[3])
    return args
end

transaction.bucketInserters = inserters

return transaction
end
end

do
local _ENV = _ENV
package.preload[ "art.storage.constants" ] = function( ... ) local arg = _G.arg;
local stream = {
    filters = {
        filterEquals = 1,
        filterNotEquals = 2,
        filterMore = 3,
        filterMoreEquals = 4,
        filterLess = 5,
        filterLessEquals = 6,
        filterBetween = 7,
        filterNotBetween = 8,
        filterIn = 9,
        filterNotIn = 10,
        filterStartsWith = 11,
        filterEndsWith = 12,
        filterContains = 13,
    },
    conditions = {
        conditionAnd = 1,
        conditionOr = 2
    },
    filterModes = {
        filterBySpace = 1,
        filterByIndex = 2,
        filterByField = 3,
        filterByFunction = 4,
        nestedFilter = 5
    },
    filterExpressions = {
        filterExpressionField = 1,
        filterExpressionValue = 2,
    },
    mappingModes = {
        mapBySpace = 1,
        mapByIndex = 2,
        mapByFunction = 3,
        mapByField = 4
    },
    comparators = {
        comparatorMore = 1,
        comparatorLess = 2,
    },
    processingFunctions = {
        processingLimit = 1,
        processingOffset = 2,
        processingFilter = 3,
        processingSort = 4,
        processingDistinct = 5,
        processingMap = 6
    },
    terminatingFunctions = {
        terminatingCollect = 1,
        terminatingCount = 2,
        terminatingAll = 3,
        terminatingAny = 4,
        terminatingNone = 5
    }
}
return {
    stream = stream,

    table = "table"
}
end
end

do
local _ENV = _ENV
package.preload[ "art.storage.deep-equal" ] = function( ... ) local arg = _G.arg;
local constants = require("art.storage.constants")

return function(first, second)
    if first == second then
        return true
    end

    if type(first) == constants.table and type(second) == constants.table then
        for key1, value1 in pairs(first) do
            local value2 = second[key1]

            if value2 == nil then
                return false
            end

            if value1 ~= value2 then
                if type(value1) == constants.table and type(value2) == constants.table then
                    if not deepEqual(value1, value2) then
                        return false
                    end
                end

                return false
            end
        end

        for key2, _ in pairs(second) do
            if first[key2] == nil then
                return false
            end
        end

        return true
    end

    return false
end
end
end

do
local _ENV = _ENV
package.preload[ "art.storage.index" ] = function( ... ) local arg = _G.arg;
local index = {
    findFirst = function(space, index, key)
        return box.space[space].index[index]:get(key)
    end,

    findAll = function(space, index, keys)
        local result = {}
        for _, key in pairs(keys) do
            table.insert(result, box.space[space].index[index]:get(key))
        end
        return result
    end,

    stream = function(space, index, processingOperators, terminatingOperator)
        local generator, param, state = box.space[space].index[index]:pairs()

        for _, operator in pairs(processingOperators) do
            local name = operator[1]
            local parameters = operator[2]
            generator, param, state = stream.processingFunctor(name)(generator, param, state, parameters)
        end

        return stream.terminatingFunctor(terminatingOperator[1])(generator, param, state, terminatingOperator[2])
    end,

    count = function(space, index, key)
        return box.space[space].index[index]:count(key)
    end,

    multiple = require("art.storage.index-multiple-transformer"),

    single = require("art.storage.index-single-transformer"),
}

return index
end
end

do
local _ENV = _ENV
package.preload[ "art.storage.index-multiple-transformer" ] = function( ... ) local arg = _G.arg;
local transformer = {
    delete = function(space, index, keys)
        return box.atomic(function()
            local results = {}
            for _, key in pairs(keys) do
                table.insert(results, box.space[space].index[index]:delete(key))
            end
            return results
        end)
    end,
}

return transformer
end
end

do
local _ENV = _ENV
package.preload[ "art.storage.index-single-transformer" ] = function( ... ) local arg = _G.arg;
local transformer = {
    delete = function(space, index, key)
        return box.space[space].index[index]:delete(key)
    end,

    update = function(space, index, key, commands)
        return box.space[space].index[index].update(space, key, commands)
    end,
}

return transformer
end
end

do
local _ENV = _ENV
package.preload[ "art.storage" ] = function( ... ) local arg = _G.arg;
local function initialize()
    box.once("art:main", function()
        for name in pairs(art.space) do
            if name ~= "multiple" and name ~= "single" then
                box.schema.func.create("art.space." .. name, { if_not_exists = true })
            end
        end
        for name in pairs(art.space.single) do
            box.schema.func.create("art.space.single." .. name, { if_not_exists = true })
        end
        for name in pairs(art.space.multiple) do
            box.schema.func.create("art.space.multiple." .. name, { if_not_exists = true })
        end

        for name in pairs(art.index) do
            if name ~= "multiple" and name ~= "single" then
                box.schema.func.create("art.index." .. name, { if_not_exists = true })
            end
        end
        for name in pairs(art.index.single) do
            box.schema.func.create("art.index.single." .. name, { if_not_exists = true })
        end
        for name in pairs(art.index.multiple) do
            box.schema.func.create("art.index.multiple." .. name, { if_not_exists = true })
        end

        for name in pairs(art.schema) do
            box.schema.func.create("art.schema." .. name, { if_not_exists = true })
        end
        for name in pairs(art.space.single) do
            box.schema.func.create("art.space.single." .. name, { if_not_exists = true })
        end
        for name in pairs(art.space.multiple) do
            box.schema.func.create("art.space.multiple." .. name, { if_not_exists = true })
        end
        for name in pairs(art.index.single) do
            box.schema.func.create("art.index.single." .. name, { if_not_exists = true })
        end
        for name in pairs(art.index.multiple) do
            box.schema.func.create("art.index.multiple." .. name, { if_not_exists = true })
        end
    end)
end

art = {
    space = require("art.storage.space"),

    index = require("art.storage.index"),

    schema = require("art.storage.schema"),

    stream = require("art.storage.stream"),

    subscription = require("art.storage.subscription"),

    initialize = initialize
}

return art
end
end

do
local _ENV = _ENV
package.preload[ "art.storage.schema" ] = function( ... ) local arg = _G.arg;
local schema = {
    createIndex = function(space, name, configuration)
        if not configuration then
            configuration = {}
        end
        return box.space[space]:create_index(name, configuration)
    end,

    dropIndex = function(space, name)
        box.space[space].index[name]:drop()
        return {}
    end,

    renameSpace = function(space, name)
        return box.space[space]:rename(name)
    end,

    formatSpace = function(space, format)
        return box.space[space]:format(format)
    end,

    dropSpace = function(space)
        box.space[space]:drop()
    end,

    createSpace = function(name, configuration)
        if not configuration then
            configuration = {}
        end
        box.schema.space.create(name, configuration)
    end,

    spaces = function()
        local result = {}
        for _, value in pairs(box.space._space:select()) do
            if not (string.startswith(value[3], '_')) then
                table.insert(result, value[3])
            end
        end
        return result
    end,

    indices = function(space)
        local result = {}
        for _, value in pairs(box.space[space].index) do
            table.insert(result, value.name)
        end
        return result
    end,
}

return schema
end
end

do
local _ENV = _ENV
package.preload[ "art.storage.space" ] = function( ... ) local arg = _G.arg;
stream = require("art.storage.stream")

local space = {
    findFirst = function(space, key)
        return box.space[space]:get(key)
    end,

    findAll = function(space, keys)
        local result = {}
        for _, key in pairs(keys) do
            local value = box.space[space]:get(key)
            if value ~= nil then
                table.insert(result, value)
            end
        end
        return result
    end,

    stream = function(space, processingOperators, terminatingOperator)
        local generator, parameter, state = box.space[space]:pairs()

        for _, operator in pairs(processingOperators) do
            local name = operator[1]
            local parameters = operator[2]

            generator, parameter, state = stream.processingFunctor(name)(generator, parameter, state, parameters)
        end

        return stream.terminatingFunctor(terminatingOperator[1])(generator, parameter, state, terminatingOperator[2])
    end,

    count = function(space)
        return box.space[space]:count()
    end,

    truncate = function(space)
        box.space[space]:truncate()
        return {}
    end,

    multiple = require("art.storage.space-multiple-transformer"),

    single = require("art.storage.space-single-transformer"),
}

return space
end
end

do
local _ENV = _ENV
package.preload[ "art.storage.space-multiple-transformer" ] = function( ... ) local arg = _G.arg;
local transformer = {
    delete = function(space, keys)
        return box.atomic(function()
            local results = {}
            for _, key in pairs(keys) do
                table.insert(results, box.space[space]:delete(key))
            end
            return results
        end)
    end,

    insert = function(space, values)
        return box.atomic(function()
            local results = {}
            for _, value in pairs(values) do
                table.insert(results, box.space[space]:insert(value))
            end
            return results
        end)
    end,

    put = function(space, values)
        return box.atomic(function()
            local results = {}
            for _, value in pairs(values) do
                table.insert(results, box.space[space]:put(value))
            end
            return results
        end)
    end,

    replace = put
}

return transformer
end
end

do
local _ENV = _ENV
package.preload[ "art.storage.space-single-transformer" ] = function( ... ) local arg = _G.arg;
local transformer = {
    delete = function(space, key)
        return box.space[space]:delete(key)
    end,

    insert = function(space, data)
        return box.space[space]:insert(data)
    end,

    put = function(space, data)
        return box.space[space]:put(data)
    end,

    replace = put,

    update = function(space, key, commands)
        return box.space[space].update(space, key, commands)
    end
}

return transformer
end
end

do
local _ENV = _ENV
package.preload[ "art.storage.stream" ] = function( ... ) local arg = _G.arg;
local functional = require('fun')
local constants = require("art.storage.constants").stream
local streamFilter = require("art.storage.stream-filter")
local streamMapper = require("art.storage.stream-mapper")

local comparators = {}

comparators[constants.comparators.comparatorMore] = function(first, second, field)
    return first[field] > second[field]
end

comparators[constants.comparators.comparatorLess] = function(first, second, field)
    return first[field] < second[field]
end

local comparatorSelector = function(id, field)
    return function(first, second)
        return comparators[id](first, second, field)
    end
end

local terminatingFunctors = {}

terminatingFunctors[constants.terminatingFunctions.terminatingCollect] = function(generator, parameter, state)
    local results = {}
    for _, item in functional.iter(generator, parameter, state) do
        table.insert(results, item)
    end
    return results
end

local collect = terminatingFunctors[constants.terminatingFunctions.terminatingCollect]

terminatingFunctors[constants.terminatingFunctions.count] = function(generator, parameter, state)
    return functional.length(generator, parameter, state)
end

terminatingFunctors[constants.terminatingFunctions.terminatingAll] = function(generator, parameter, state, request)
    return functional.all(streamFilter.selector(unpack(request)), generator, parameter, state)
end

terminatingFunctors[constants.terminatingFunctions.terminatingAny] = function(generator, parameter, state, request)
    return functional.any(streamFilter.selector(unpack(request)), generator, parameter, state)
end

terminatingFunctors[constants.terminatingFunctions.terminatingNone] = function(generator, parameter, state, request)
    return not functional.any(streamFilter.selector(unpack(request)), generator, parameter, state)
end

local processingFunctors = {}

processingFunctors[constants.processingFunctions.processingLimit] = function(generator, parameter, state, count)
    return functional.take_n(count, generator, parameter, state)
end

processingFunctors[constants.processingFunctions.processingOffset] = function(generator, parameter, state, count)
    return functional.drop_n(count, generator, parameter, state)
end

processingFunctors[constants.processingFunctions.processingDistinct] = function(generator, parameter, state, field)
    local result = {}
    for _, item in functional.iter(generator, parameter, state) do
        result[item[field]] = item
    end
    return pairs(result)
end

processingFunctors[constants.processingFunctions.processingSort] = function(generator, parameter, state, request)
    local values = collect(generator, parameter, state)
    table.sort(values, comparatorSelector(unpack(request)))
    return functional.iter(values)
end

processingFunctors[constants.processingFunctions.processingFilter] = streamFilter.functor

processingFunctors[constants.processingFunctions.processingMap] = streamMapper

return {
    processingFunctor = function(stream)
        return processingFunctors[stream]
    end,

    terminatingFunctor = function(stream)
        return terminatingFunctors[stream]
    end,
}
end
end

do
local _ENV = _ENV
package.preload[ "art.storage.stream-filter" ] = function( ... ) local arg = _G.arg;
local deepEqual = require('art.storage.deep-equal')
local constants = require("art.storage.constants").stream
local functional = require('fun')

local filters = {}

filters[constants.filters.filterEquals] = function(filtering, field, request)
    return deepEqual(filtering[field], request[1])
end

filters[constants.filters.filterNotEquals] = function(filtering, field, request)
    return not deepEqual(filtering[field], request[1])
end

filters[constants.filters.filterMore] = function(filtering, field, request)
    return filtering[field] > request[1]
end

filters[constants.filters.filterLess] = function(filtering, field, request)
    return filtering[field] < request[1]
end

filters[constants.filters.filterMoreEquals] = function(filtering, field, request)
    return filtering[field] >= request[1]
end

filters[constants.filters.filterLessEquals] = function(filtering, field, request)
    return filtering[field] <= request[1]
end

filters[constants.filters.filterBetween] = function(filtering, field, request)
    return (filtering[field] >= request[1]) and (filtering[field] <= request[2])
end

filters[constants.filters.filterNotBetween] = function(filtering, field, request)
    return not ((filtering[field] >= request[1]) and (filtering[field] <= request[2]))
end

filters[constants.filters.filterIn] = function(filtering, field, values)
    for _, value in pairs(values) do
        if deepEqual(filtering[field], value) then
            return true
        end
    end

    return false
end

filters[constants.filters.filterNotIn] = function(filtering, field, values)
    for _, value in pairs(values) do
        if deepEqual(filtering[field], value) then
            return false
        end
    end

    return true
end

filters[constants.filters.filterStartsWith] = function(filtering, field, request)
    return string.startswith(filtering[field], request[1])
end

filters[constants.filters.filterEndsWith] = function(filtering, field, request)
    return string.endswith(filtering[field], request[1])
end

filters[constants.filters.filterContains] = function(filtering, field, request)
    return string.find(filtering[field], request[1])
end

local applyFilter = function(id, filtering, field, request)
    return filters[id](filtering, field, request)
end

local selector = function(id, field, request)
    return function(filtering)
        return applyFilter(id, filtering, field, request)
    end
end

local applyCondition = function(condition, currentResult, newResult)
    if condition == constants.conditions.conditionAnd then
        return currentResult and newResult
    end

    if condition == constants.conditions.conditionOr then
        return currentResult or newResult
    end
end

local processExpressions = function(expressions, filtering, mapped, currentResult)
    local result = currentResult
    for _, expression in pairs(expressions) do
        local expressionType = expression[1]
        local expressionCondition = expression[2]
        local expressionName = expression[3]
        local expressionField = expression[4]

        local expressionTarget
        local expressionValues

        if expressionType == constants.filterExpressions.filterExpressionField then
            expressionTarget = filtering
            for _, mappedField in pairs(expression[5]) do
                table.insert(expressionValues, mapped[mappedField])
            end
        end

        if expressionType == constants.filterExpressions.filterExpressionValue then
            expressionTarget = mapped
            expressionValues = expression[5]
        end

        local newResult = applyFilter(expressionName, expressionTarget, expressionField, expressionValues)
        result = applyCondition(expressionCondition, result, newResult);
    end
    return result
end

local processFilters
processFilters = function(filtering, inputFilters)
    local result = true
    for _, filter in pairs(inputFilters) do
        local condition = filter[1]
        local mode = filter[2]

        if mode == constants.filterModes.nestedFilter then
            result = applyCondition(condition, result, processFilters(filter[3]));
        end

        if mode == constants.filterModes.filterByField then
            local parameters = filter[3]
            local field = parameters[1]
            local id = parameters[2]
            local values = parameters[3]
            result = applyCondition(condition, result, applyFilter(id, filtering, field, values));
        end

        if mode == constants.filterModes.filterByFunction then
            local functionName = filter[3]
            result = applyCondition(condition, result, box.func[functionName]:call(filtering));
        end

        if mode == constants.filterModes.filterBySpace then
            local parameters = filter[3]
            local otherSpace = parameters[1]
            local filteringField = parameters[2]
            local mapped = box.space[otherSpace]:get(filtering[filteringField])
            if mapped ~= nil then
                result = processExpressions(filter[4], filtering, mapped, result)
            else
                result = applyCondition(condition, result, false)
            end
        end

        if mode == constants.filterModes.filterByIndex then
            local parameters = filter[3]
            local otherSpace = parameters[1]
            local filteringFields = parameters[2]
            local otherIndex = parameters[3]
            local indexKeys = {}
            for _, keyField in pairs(filteringFields) do
                table.insert(indexKeys, filtering[keyField])
            end
            if next(indexKeys) ~= nil then
                local mapped = box.space[otherSpace]:index(otherIndex):get(indexKeys)
                if mapped ~= nil then
                    result = processExpressions(filter[4], filtering, mapped, result)
                else
                    result = applyCondition(condition, result, false)
                end
            else
                result = applyCondition(condition, result, false)
            end
        end
    end
    return result
end

local filter = function(generator, parameter, state, request)
    local filteringFunction = function(filtering)
        return processFilters(filtering, request)
    end
    return functional.filter(filteringFunction, generator, parameter, state)
end

return {
    selector = selector,

    functor = filter
}
end
end

do
local _ENV = _ENV
package.preload[ "art.storage.stream-mapper" ] = function( ... ) local arg = _G.arg;
local constants = require("art.storage.constants").stream
local functional = require('fun')

return function(generator, parameter, state, request)
    local mappingFunction = function(mapping)
        local mode = request[1]

        if mode == constants.mappingModes.mapByFunction then
            local functionName = request[2]
            return box.func[functionName]:call(mapping)
        end

        if mode == constants.mappingModes.mapByField then
            local field = request[2]
            return mapping[field]
        end

        if mode == constants.mappingModes.mapBySpace then
            local otherSpace = request[3]
            local currentField = request[4]
            return box.space[otherSpace]:get(mapping[currentField])
        end

        if mode == constants.mappingModes.mapByIndex then
            local otherSpace = request[3]
            local currentFields = request[4]
            local otherIndex = request[5]
            local indexKeys = {}
            for _, keyField in pairs(currentFields) do
                table.insert(indexKeys, mapping[keyField])
            end
            if next(indexKeys) == nil then
                return nil
            end
            return box.space[otherSpace]:index(otherIndex):get(indexKeys)
        end
    end

    return functional.map(mappingFunction, generator, parameter, state)
end
end
end

do
local _ENV = _ENV
package.preload[ "art.storage.subscription" ] = function( ... ) local arg = _G.arg;
local subscription = {
    publish = function(serviceId, methodId, value)
        box.session.push({ serviceId, methodId, value })
    end
}
return subscription
end
end

do
local _ENV = _ENV
package.preload[ "vshard.cfg" ] = function( ... ) local arg = _G.arg;
-- vshard.cfg

local log = require('log')
local luri = require('uri')
local lutil = require('vshard.util')
local consts = require('vshard.consts')

local function check_uri(uri)
    if not luri.parse(uri) then
        error('Invalid URI: ' .. uri)
    end
end

local function check_replica_master(master, ctx)
    if master then
        if ctx.master then
            error('Only one master is allowed per replicaset')
        else
            ctx.master = master
        end
    end
end

local function check_replicaset_master(master, ctx)
    -- Limit the version due to extensive usage of netbox is_async feature.
    if not lutil.version_is_at_least(1, 10, 1) then
        error('Only versions >= 1.10.1 support master discovery')
    end
    if master ~= 'auto' then
        error('Only "auto" master is supported')
    end
end

local function is_number(v)
    return type(v) == 'number' and v == v
end

local function is_non_negative_number(v)
    return is_number(v) and v >= 0
end

local function is_positive_number(v)
    return is_number(v) and v > 0
end

local function is_non_negative_integer(v)
    return is_non_negative_number(v) and math.floor(v) == v
end

local function is_positive_integer(v)
    return is_positive_number(v) and math.floor(v) == v
end

local type_validate = {
    ['string'] = function(v) return type(v) == 'string' end,
    ['non-empty string'] = function(v)
        return type(v) == 'string' and #v > 0
    end,
    ['boolean'] = function(v) return type(v) == 'boolean' end,
    ['number'] = is_number,
    ['non-negative number'] = is_non_negative_number,
    ['positive number'] = is_positive_number,
    ['non-negative integer'] = is_non_negative_integer,
    ['positive integer'] = is_positive_integer,
    ['table'] = function(v) return type(v) == 'table' end,
}

local function validate_config(config, template, check_arg)
    for key, template_value in pairs(template) do
        local value = config[key]
        local name = template_value.name
        local expected_type = template_value.type
        if template_value.is_deprecated then
            if value ~= nil then
                local reason = template_value.reason
                if reason then
                    reason = '. '..reason
                else
                    reason = ''
                end
                log.warn('Option "%s" is deprecated'..reason, name)
            end
        elseif value == nil then
            if not template_value.is_optional then
                error(string.format('%s must be specified', name))
            else
                config[key] = template_value.default
            end
        else
            if type(expected_type) == 'string' then
                if not type_validate[expected_type](value) then
                    error(string.format('%s must be %s', name, expected_type))
                end
                local max = template_value.max
                if max and value > max then
                    error(string.format('%s must not be greater than %s', name,
                                        max))
                end
            else
                local is_valid_type_found = false
                for _, t in pairs(expected_type) do
                    if type_validate[t](value) then
                        is_valid_type_found = true
                        break
                    end
                end
                if not is_valid_type_found then
                    local types = table.concat(expected_type, ', ')
                    error(string.format('%s must be one of the following '..
                                        'types: %s', name, types))
                end
            end
            if template_value.check then
                template_value.check(value, check_arg)
            end
        end
    end
end

local replica_template = {
    uri = {type = 'non-empty string', name = 'URI', check = check_uri},
    name = {type = 'string', name = "Name", is_optional = true},
    zone = {type = {'string', 'number'}, name = "Zone", is_optional = true},
    master = {
        type = 'boolean', name = "Master", is_optional = true,
        check = check_replica_master
    },
}

local function check_replicas(replicas)
    local ctx = {master = false}
    for _, replica in pairs(replicas) do
        validate_config(replica, replica_template, ctx)
    end
end

local replicaset_template = {
    replicas = {type = 'table', name = 'Replicas', check = check_replicas},
    weight = {
        type = 'non-negative number', name = 'Weight', is_optional = true,
        default = 1,
    },
    lock = {type = 'boolean', name = 'Lock', is_optional = true},
    master = {
        type = 'string', name = 'Master search mode', is_optional = true,
        check = check_replicaset_master
    },
}

--
-- Check weights map on correctness.
--
local function cfg_check_weights(weights)
    for zone1, v in pairs(weights) do
        if type(zone1) ~= 'number' and type(zone1) ~= 'string' then
            -- Zone1 can be not number or string, if an user made
            -- this: weights = {[{1}] = ...}. In such a case
            -- {1} is the unaccessible key of a lua table, which
            -- is available only via pairs.
            error('Zone identifier must be either string or number')
        end
        if type(v) ~= 'table' then
            error('Zone must be map of relative weights of other zones')
        end
        for zone2, weight in pairs(v) do
            if type(zone2) ~= 'number' and type(zone2) ~= 'string' then
                error('Zone identifier must be either string or number')
            end
            if type(weight) ~= 'number' or weight < 0 then
                error('Zone weight must be either nil or non-negative number')
            end
            if zone2 == zone1 and weight ~= 0 then
                error('Weight of own zone must be either nil or 0')
            end
        end
    end
end

local function check_discovery_mode(value)
    if value ~= 'on' and value ~= 'off' and value ~= 'once' then
        error("Expected 'on', 'off', or 'once' for discovery_mode")
    end
end

local function check_sharding(sharding)
    local uuids = {}
    local uris = {}
    local names = {}
    local is_all_weights_zero = true
    for replicaset_uuid, replicaset in pairs(sharding) do
        if uuids[replicaset_uuid] then
            error(string.format('Duplicate uuid %s', replicaset_uuid))
        end
        uuids[replicaset_uuid] = true
        if type(replicaset) ~= 'table' then
            error('Replicaset must be a table')
        end
        local w = replicaset.weight
        if w == math.huge or w == -math.huge then
            error('Replicaset weight can not be Inf')
        end
        validate_config(replicaset, replicaset_template)
        local is_auto_master = replicaset.master == 'auto'
        for replica_uuid, replica in pairs(replicaset.replicas) do
            if uris[replica.uri] then
                error(string.format('Duplicate uri %s', replica.uri))
            end
            uris[replica.uri] = true
            if uuids[replica_uuid] then
                error(string.format('Duplicate uuid %s', replica_uuid))
            end
            uuids[replica_uuid] = true
            if is_auto_master and replica.master ~= nil then
                error(string.format('Can not specify master nodes when '..
                                    'master search is enabled, but found '..
                                    'master flag in replica uuid %s',
                                    replica_uuid))
            end
            -- Log warning in case replica.name duplicate is
            -- found. Message appears once for each unique
            -- duplicate.
            local name = replica.name
            if name then
                if names[name] == nil then
                    names[name] = 1
                elseif names[name] == 1 then
                    log.warn('Duplicate replica.name is found: %s', name)
                    -- Next duplicates should not be reported.
                    names[name] = 2
                end
            end
        end
        is_all_weights_zero = is_all_weights_zero and replicaset.weight == 0
    end
    if next(sharding) and is_all_weights_zero then
        error('At least one replicaset weight should be > 0')
    end
end

local cfg_template = {
    sharding = {type = 'table', name = 'Sharding', check = check_sharding},
    weights = {
        type = 'table', name = 'Weight matrix', is_optional = true,
        check = cfg_check_weights
    },
    shard_index = {
        type = {'non-empty string', 'non-negative integer'},
        name = 'Shard index', is_optional = true, default = 'bucket_id',
    },
    zone = {
        type = {'string', 'number'}, name = 'Zone identifier',
        is_optional = true
    },
    bucket_count = {
        type = 'positive integer', name = 'Bucket count', is_optional = true,
        default = consts.DEFAULT_BUCKET_COUNT
    },
    rebalancer_disbalance_threshold = {
        type = 'non-negative number', name = 'Rebalancer disbalance threshold',
        is_optional = true,
        default = consts.DEFAULT_REBALANCER_DISBALANCE_THRESHOLD
    },
    rebalancer_max_receiving = {
        type = 'positive integer',
        name = 'Rebalancer max receiving bucket count', is_optional = true,
        default = consts.DEFAULT_REBALANCER_MAX_RECEIVING
    },
    rebalancer_max_sending = {
        type = 'positive integer',
        name = 'Rebalancer max sending bucket count',
        is_optional = true,
        default = consts.DEFAULT_REBALANCER_MAX_SENDING,
        max = consts.REBALANCER_MAX_SENDING_MAX
    },
    collect_bucket_garbage_interval = {
        name = 'Garbage bucket collect interval', is_deprecated = true,
        reason = 'Has no effect anymore'
    },
    collect_lua_garbage = {
        type = 'boolean', name = 'Garbage Lua collect necessity',
        is_optional = true, default = false
    },
    sync_timeout = {
        type = 'non-negative number', name = 'Sync timeout', is_optional = true,
        default = consts.DEFAULT_SYNC_TIMEOUT
    },
    connection_outdate_delay = {
        type = 'non-negative number', name = 'Object outdate timeout',
        is_optional = true
    },
    failover_ping_timeout = {
        type = 'positive number', name = 'Failover ping timeout',
        is_optional = true, default = consts.DEFAULT_FAILOVER_PING_TIMEOUT
    },
    discovery_mode = {
        type = 'string', name = 'Discovery mode: on, off, once',
        is_optional = true, default = 'on', check = check_discovery_mode
    },
    sched_ref_quota = {
        name = 'Scheduler storage ref quota', type = 'non-negative number',
        is_optional = true, default = consts.DEFAULT_SCHED_REF_QUOTA
    },
    sched_move_quota = {
        name = 'Scheduler bucket move quota', type = 'non-negative number',
        is_optional = true, default = consts.DEFAULT_SCHED_MOVE_QUOTA
    },
}

--
-- Split it into vshard_cfg and box_cfg parts.
--
local function cfg_split(cfg)
    local vshard_cfg = {}
    local box_cfg = {}
    for k, v in pairs(cfg) do
        if cfg_template[k] then
            vshard_cfg[k] = v
        else
            box_cfg[k] = v
        end
    end
    return vshard_cfg, box_cfg
end

--
-- Names of options which cannot be changed during reconfigure.
--
local non_dynamic_options = {
    'bucket_count', 'shard_index'
}

--
-- Check sharding config on correctness. Check types, name and uri
-- uniqueness, master count (in each replicaset must be <= 1).
--
local function cfg_check(shard_cfg, old_cfg)
    if type(shard_cfg) ~= 'table' then
        error('Сonfig must be map of options')
    end
    shard_cfg = table.deepcopy(shard_cfg)
    validate_config(shard_cfg, cfg_template)
    if not old_cfg then
        return shard_cfg
    end
    -- Check non-dynamic after default values are added.
    for _, f_name in pairs(non_dynamic_options) do
        -- New option may be added in new vshard version.
        if shard_cfg[f_name] ~= old_cfg[f_name] then
           error(string.format('Non-dynamic option %s ' ..
                               'cannot be reconfigured', f_name))
        end
    end
    return shard_cfg
end

return {
    check = cfg_check,
    split = cfg_split,
}
end
end

do
local _ENV = _ENV
package.preload[ "vshard.consts" ] = function( ... ) local arg = _G.arg;
return {
    -- Bucket FSM
    BUCKET = {
        ACTIVE = 'active',
        PINNED = 'pinned',
        SENDING = 'sending',
        SENT = 'sent',
        RECEIVING = 'receiving',
        GARBAGE = 'garbage',
    },

    STATUS = {
        GREEN = 0,
        YELLOW = 1,
        ORANGE = 2,
        RED = 3,
    },

    REPLICATION_THRESHOLD_SOFT = 1,
    REPLICATION_THRESHOLD_HARD = 5,
    REPLICATION_THRESHOLD_FAIL = 10,

    DEFAULT_BUCKET_COUNT = 3000;
    BUCKET_SENT_GARBAGE_DELAY = 0.5;
    BUCKET_CHUNK_SIZE = 1000;
    LUA_CHUNK_SIZE = 100000,
    DEFAULT_REBALANCER_DISBALANCE_THRESHOLD = 1;
    REBALANCER_IDLE_INTERVAL = 60 * 60;
    REBALANCER_WORK_INTERVAL = 10;
    REBALANCER_CHUNK_TIMEOUT = 60 * 5;
    DEFAULT_REBALANCER_MAX_SENDING = 1;
    REBALANCER_MAX_SENDING_MAX = 15;
    DEFAULT_REBALANCER_MAX_RECEIVING = 100;
    CALL_TIMEOUT_MIN = 0.5;
    CALL_TIMEOUT_MAX = 64;
    FAILOVER_UP_TIMEOUT = 5;
    FAILOVER_DOWN_TIMEOUT = 1;
    DEFAULT_FAILOVER_PING_TIMEOUT = 5;
    DEFAULT_SYNC_TIMEOUT = 1;
    RECONNECT_TIMEOUT = 0.5;
    GC_BACKOFF_INTERVAL = 5,
    RECOVERY_BACKOFF_INTERVAL = 5,
    REPLICA_BACKOFF_INTERVAL = 5,
    COLLECT_LUA_GARBAGE_INTERVAL = 100;
    DEFAULT_BUCKET_SEND_TIMEOUT = 10,
    DEFAULT_BUCKET_RECV_TIMEOUT = 10,

    DEFAULT_SCHED_REF_QUOTA = 300,
    DEFAULT_SCHED_MOVE_QUOTA = 1,

    DISCOVERY_IDLE_INTERVAL = 10,
    DISCOVERY_WORK_INTERVAL = 1,
    DISCOVERY_WORK_STEP = 0.01,
    DISCOVERY_TIMEOUT = 10,

    MASTER_SEARCH_IDLE_INTERVAL = 5,
    MASTER_SEARCH_WORK_INTERVAL = 0.5,
    MASTER_SEARCH_BACKOFF_INTERVAL = 5,
    MASTER_SEARCH_TIMEOUT = 5,

    TIMEOUT_INFINITY = 500 * 365 * 86400,
    DEADLINE_INFINITY = math.huge,
}
end
end

do
local _ENV = _ENV
package.preload[ "vshard.error" ] = function( ... ) local arg = _G.arg;
local ffi = require('ffi')
local json = require('json')

--
-- Error messages description.
-- * name -- Key by which an error code can be retrieved from
--   the exported by the module `code` dictionary.
-- * msg -- Error message which can use `args` using
--   `string.format` notation.
-- * args -- Names of arguments passed while constructing an
--   error. After constructed, an error-object contains provided
--   arguments by the names specified in the field.
--
local error_message_template = {
    [1] = {
        name = 'WRONG_BUCKET',
        msg = 'Cannot perform action with bucket %d, reason: %s',
        args = {'bucket_id', 'reason', 'destination'}
    },
    [2] = {
        name = 'NON_MASTER',
        msg = 'Replica %s is not a master for replicaset %s anymore',
        args = {'replica_uuid', 'replicaset_uuid', 'master_uuid'}
    },
    [3] = {
        name = 'BUCKET_ALREADY_EXISTS',
        msg = 'Bucket %d already exists',
        args = {'bucket_id'}
    },
    [4] = {
        name = 'NO_SUCH_REPLICASET',
        msg = 'Replicaset %s not found',
        args = {'replicaset_uuid'}
    },
    [5] = {
        name = 'MOVE_TO_SELF',
        msg = 'Cannot move: bucket %d is already on replicaset %s',
        args = {'bucket_id', 'replicaset_uuid'}
    },
    [6] = {
         name = 'MISSING_MASTER',
         msg = 'Master is not configured for replicaset %s',
         args = {'replicaset_uuid'}
    },
    [7] = {
        name = 'TRANSFER_IS_IN_PROGRESS',
        msg = 'Bucket %d is transferring to replicaset %s',
        args = {'bucket_id', 'destination'}
    },
    [8] = {
        name = 'UNREACHABLE_REPLICASET',
        msg = 'There is no active replicas in replicaset %s',
        args = {'unreachable_uuid', 'bucket_id'}
    },
    [9] = {
        name = 'NO_ROUTE_TO_BUCKET',
        msg = 'Bucket %d cannot be found. Is rebalancing in progress?',
        args = {'bucket_id'}
    },
    [10] = {
        name = 'NON_EMPTY',
        msg = 'Cluster is already bootstrapped'
    },
    [11] = {
        name = 'UNREACHABLE_MASTER',
        msg = 'Master of replicaset %s is unreachable: %s',
        args = {'uuid', 'reason'}
    },
    [12] = {
        name = 'OUT_OF_SYNC',
        msg = 'Replica is out of sync'
    },
    [13] = {
        name = 'HIGH_REPLICATION_LAG',
        msg = 'High replication lag: %f',
        args = {'lag'}
    },
    [14] = {
        name = 'UNREACHABLE_REPLICA',
        msg = "Replica %s isn't active",
        args = {'unreachable_uuid'}
    },
    [15] = {
        name = 'LOW_REDUNDANCY',
        msg = 'Only one replica is active'
    },
    [16] = {
        name = 'INVALID_REBALANCING',
        msg = 'Sending and receiving buckets at same time is not allowed'
    },
    [17] = {
        name = 'SUBOPTIMAL_REPLICA',
        msg = 'A current read replica in replicaset %s is not optimal'
    },
    [18] = {
        name = 'UNKNOWN_BUCKETS',
        msg = '%d buckets are not discovered',
        args = {'not_discovered_cnt'}
    },
    [19] = {
        name = 'REPLICASET_IS_LOCKED',
        msg = 'Replicaset is locked'
    },
    [20] = {
        name = 'OBJECT_IS_OUTDATED',
        msg = 'Object is outdated after module reload/reconfigure. ' ..
              'Use new instance.'
    },
    [21] = {
        name = 'ROUTER_ALREADY_EXISTS',
        msg = 'Router with name %s already exists',
        args = {'name'},
    },
    [22] = {
        name = 'BUCKET_IS_LOCKED',
        msg = 'Bucket %d is locked',
        args = {'bucket_id'},
    },
    [23] = {
        name = 'INVALID_CFG',
        msg = 'Invalid configuration: %s',
        args = {'reason'},
    },
    [24] = {
        name = 'BUCKET_IS_PINNED',
        msg = 'Bucket %d is pinned',
        args = {'bucket_id'}
    },
    [25] = {
        name = 'TOO_MANY_RECEIVING',
        msg = 'Too many receiving buckets at once, please, throttle'
    },
    [26] = {
        name = 'STORAGE_IS_REFERENCED',
        msg = 'Storage is referenced'
    },
    [27] = {
        name = 'STORAGE_REF_ADD',
        msg = 'Can not add a storage ref: %s',
        args = {'reason'},
    },
    [28] = {
        name = 'STORAGE_REF_USE',
        msg = 'Can not use a storage ref: %s',
        args = {'reason'},
    },
    [29] = {
        name = 'STORAGE_REF_DEL',
        msg = 'Can not delete a storage ref: %s',
        args = {'reason'},
    },
    [30] = {
        name = 'BUCKET_RECV_DATA_ERROR',
        msg = 'Can not receive the bucket %s data in space "%s" at tuple %s: %s',
        args = {'bucket_id', 'space', 'tuple', 'reason'},
    },
    [31] = {
        name = 'MULTIPLE_MASTERS_FOUND',
        msg = 'Found more than one master in replicaset %s on nodes %s and %s',
        args = {'replicaset_uuid', 'master1', 'master2'},
    },
    [32] = {
        name = 'REPLICASET_IN_BACKOFF',
        msg = 'Replicaset %s is in backoff, can\'t take requests right now. '..
              'Last error was %s',
        args = {'replicaset_uuid', 'error'}
    },
    [33] = {
        name = 'STORAGE_IS_DISABLED',
        msg = 'Storage is disabled: %s',
        args = {'reason'}
    },
}

--
-- User-visible error_name -> error_number dictionary.
--
local error_code = {}
for code, err in pairs(error_message_template) do
    assert(type(code) == 'number')
    assert(err.msg, 'msg is required field')
    assert(error_code[err.name] == nil, "Dublicate error name")
    error_code[err.name] = code
end

--
-- There are 2 error types:
-- * box_error - it is created on tarantool errors: client error,
--   oom error, socket error etc. It has type = one of tarantool
--   error types, trace (file, line), message;
-- * vshard_error - it is created on sharding errors like
--   replicaset unavailability, master absence etc. It has type =
--   'ShardingError', one of codes below and optional
--   message.
--

local function box_error(original_error)
    return setmetatable(original_error:unpack(), {__tostring = json.encode})
end

--
-- Construct an vshard error.
-- @param code Number, one of error_code constants.
-- @param ... Arguments from `error_message_template` `args`
--        field. Caller have to pass at least as many arguments
--        as `msg` field requires.
-- @retval ShardingError object.
--
local function vshard_error(code, ...)
    local format = error_message_template[code]
    assert(format, 'Error message format is not found.')
    local args_passed_cnt = select('#', ...)
    local args = format.args or {}
    assert(#args == args_passed_cnt,
           string.format('Wrong number of arguments are passed to %s error',
                         format.name))
    local ret = setmetatable({}, {__tostring = json.encode})
    -- Save error arguments.
    for i = 1, #args do
        ret[args[i]] = select(i, ...)
    end
    ret.message = string.format(format.msg, ...)
    ret.type = 'ShardingError'
    ret.code = code
    ret.name = format.name
    return ret
end

--
-- Convert error object from pcall to lua, box or vshard error
-- object.
--
local function make_error(e)
    if type(e) == 'cdata' and ffi.istype('struct error', e) then
        -- box.error, return unpacked
        return box_error(e)
    elseif type(e) == 'string' then
        local ok, err = pcall(box.error, box.error.PROC_LUA, e)
        return box_error(err)
    elseif type(e) == 'table' then
        return setmetatable(e, {__tostring = json.encode})
    else
        return e
    end
end

--
-- Restore an error object from its string serialization.
--
local function from_string(str)
    -- Error objects in VShard are stringified into json. Hence can restore also
    -- as json. The only problem is that the json might be truncated if it was
    -- stored in an error message of a real error object. It is not very
    -- reliable.
    local ok, res = pcall(json.decode, str)
    if not ok then
        return nil
    end
    if type(res) ~= 'table' or type(res.type) ~= 'string' or
       type(res.code) ~= 'number' or type(res.message) ~= 'string' then
        return nil
    end
    return make_error(res)
end

local function make_alert(code, ...)
    local format = error_message_template[code]
    assert(format)
    local r = {format.name, string.format(format.msg, ...)}
    return setmetatable(r, { __serialize = 'seq' })
end

--
-- Create a timeout error object. Box.error.new() can't be used because is
-- present only since 1.10.
--
local function make_timeout()
    local _, err = pcall(box.error, box.error.TIMEOUT)
    return make_error(err)
end

return {
    code = error_code,
    box = box_error,
    vshard = vshard_error,
    make = make_error,
    from_string = from_string,
    alert = make_alert,
    timeout = make_timeout,
}
end
end

do
local _ENV = _ENV
package.preload[ "vshard.hash" ] = function( ... ) local arg = _G.arg;
-- hash.lua
local ldigest = require('digest')
local mpencode = require('msgpackffi').encode

--
-- Fast and simple hash. However it works incorrectly with
-- floating point cdata values. Also hash of an integer value
-- depends on its type: Lua number, cdata int64, cdata uint64.
--
local function strcrc32(shard_key)
    if type(shard_key) ~= 'table' then
        return ldigest.crc32(tostring(shard_key))
    else
        local crc32 = ldigest.crc32.new()
        for _, v in ipairs(shard_key) do
            crc32:update(tostring(v))
        end
        return crc32:result()
    end
end

local function mpcrc32_one(value)
    if type(value) ~= 'string' then
        return mpencode(value)
    else
        -- Despite the function called 'mp', strings are not
        -- encoded. This is because it does not make much sense to
        -- copy the whole string onto a temporary buffer just to
        -- add a small MessagePack header. Such 'hack' makes
        -- hashing of strings several magnitudes of order faster.
        return value
    end
end

--
-- Stable hash providing the correct values for integers not
-- depending on their size. However may return different hashes
-- for the same floating point value if it is cdata float or cdata
-- double.
--
local function mpcrc32(shard_key)
    if type(shard_key) ~= 'table' then
        return ldigest.crc32(mpcrc32_one(shard_key))
    else
        local crc32 = ldigest.crc32.new()
        for _, v in ipairs(shard_key) do
            crc32:update(mpcrc32_one(v))
        end
        return crc32:result()
    end
end

return {
    strcrc32 = strcrc32,
    mpcrc32 = mpcrc32,
}
end
end

do
local _ENV = _ENV
package.preload[ "vshard.heap" ] = function( ... ) local arg = _G.arg;
local math_floor = math.floor

--
-- Implementation of a typical algorithm of the binary heap.
-- The heap is intrusive - it stores index of each element inside of it. It
-- allows to update and delete elements in any place in the heap, not only top
-- elements.
--

local function heap_parent_index(index)
    return math_floor(index / 2)
end

local function heap_left_child_index(index)
    return index * 2
end

--
-- Generate a new heap.
--
-- The implementation is targeted on as few index accesses as possible.
-- Everything what could be is stored as upvalue variables instead of as indexes
-- in a table. What couldn't be an upvalue and is used in a function more than
-- once is saved on the stack.
--
local function heap_new(is_left_above)
    -- Having it as an upvalue allows not to do 'self.data' lookup in each
    -- function.
    local data = {}
    -- Saves #data calculation. In Lua it is not just reading a number.
    local count = 0

    local function heap_update_index_up(idx)
        if idx == 1 then
            return false
        end

        local orig_idx = idx
        local value = data[idx]
        local pidx = heap_parent_index(idx)
        local parent = data[pidx]
        while is_left_above(value, parent) do
            data[idx] = parent
            parent.index = idx
            idx = pidx
            if idx == 1 then
                break
            end
            pidx = heap_parent_index(idx)
            parent = data[pidx]
        end

        if idx == orig_idx then
            return false
        end
        data[idx] = value
        value.index = idx
        return true
    end

    local function heap_update_index_down(idx)
        local left_idx = heap_left_child_index(idx)
        if left_idx > count then
            return false
        end

        local orig_idx = idx
        local left
        local right
        local right_idx = left_idx + 1
        local top
        local top_idx
        local value = data[idx]
        repeat
            right_idx = left_idx + 1
            if right_idx > count then
                top = data[left_idx]
                if is_left_above(value, top) then
                    break
                end
                top_idx = left_idx
            else
                left = data[left_idx]
                right = data[right_idx]
                if is_left_above(left, right) then
                    if is_left_above(value, left) then
                        break
                    end
                    top_idx = left_idx
                    top = left
                else
                    if is_left_above(value, right) then
                        break
                    end
                    top_idx = right_idx
                    top = right
                end
            end

            data[idx] = top
            top.index = idx
            idx = top_idx
            left_idx = heap_left_child_index(idx)
        until left_idx > count

        if idx == orig_idx then
            return false
        end
        data[idx] = value
        value.index = idx
        return true
    end

    local function heap_update_index(idx)
        if not heap_update_index_up(idx) then
            heap_update_index_down(idx)
        end
    end

    local function heap_push(self, value)
        count = count + 1
        data[count] = value
        value.index = count
        heap_update_index_up(count)
    end

    local function heap_update_top(self)
        heap_update_index_down(1)
    end

    local function heap_update(self, value)
        heap_update_index(value.index)
    end

    local function heap_remove_top(self)
        if count == 0 then
            return
        end
        data[1].index = -1
        if count == 1 then
            data[1] = nil
            count = 0
            return
        end
        local value = data[count]
        data[count] = nil
        data[1] = value
        value.index = 1
        count = count - 1
        heap_update_index_down(1)
    end

    local function heap_remove(self, value)
        local idx = value.index
        value.index = -1
        if idx == count then
            data[count] = nil
            count = count - 1
            return
        end
        value = data[count]
        data[idx] = value
        data[count] = nil
        value.index = idx
        count = count - 1
        heap_update_index(idx)
    end

    local function heap_remove_try(self, value)
        local idx = value.index
        if idx and idx > 0 then
            heap_remove(self, value)
        end
    end

    local function heap_pop(self)
        if count == 0 then
            return
        end
        -- Some duplication from remove_top, but allows to save a few
        -- condition checks, index accesses, and a function call.
        local res = data[1]
        res.index = -1
        if count == 1 then
            data[1] = nil
            count = 0
            return res
        end
        local value = data[count]
        data[count] = nil
        data[1] = value
        value.index = 1
        count = count - 1
        heap_update_index_down(1)
        return res
    end

    local function heap_top(self)
        return data[1]
    end

    local function heap_count(self)
        return count
    end

    return {
        -- Expose the data. For testing.
        data = data,
        -- Methods are exported as members instead of __index so as to save on
        -- not taking a metatable and going through __index on each method call.
        push = heap_push,
        update_top = heap_update_top,
        remove_top = heap_remove_top,
        pop = heap_pop,
        update = heap_update,
        remove = heap_remove,
        remove_try = heap_remove_try,
        top = heap_top,
        count = heap_count,
    }
end

return {
    new = heap_new,
}
end
end

do
local _ENV = _ENV
package.preload[ "vshard" ] = function( ... ) local arg = _G.arg;
return {
    router = require('vshard.router'),
    storage = require('vshard.storage'),
    consts = require('vshard.consts'),
    error = require('vshard.error'),
}
end
end

do
local _ENV = _ENV
package.preload[ "vshard.lua_gc" ] = function( ... ) local arg = _G.arg;
--
-- This module implements background lua GC fiber.
-- It's purpose is to make GC more aggressive.
--

local lfiber = require('fiber')
local MODULE_INTERNALS = '__module_vshard_lua_gc'

local M = rawget(_G, MODULE_INTERNALS)
if not M then
    M = {
        -- Background fiber.
        bg_fiber = nil,
        -- GC interval in seconds.
        interval = nil,
        -- Main loop.
        -- Stored here to make the fiber reloadable.
        main_loop = nil,
        -- Number of `collectgarbage()` calls.
        iterations = 0,
    }
end

M.main_loop = function()
    lfiber.sleep(M.interval)
    collectgarbage()
    M.iterations = M.iterations + 1
    return M.main_loop()
end

local function set_state(active, interval)
    assert(type(interval) == 'number')
    M.interval = interval
    if active and not M.bg_fiber then
        M.bg_fiber = lfiber.create(M.main_loop)
        M.bg_fiber:name('vshard.lua_gc')
    end
    if not active and M.bg_fiber then
        M.bg_fiber:cancel()
        M.bg_fiber = nil
    end
    if active then
        M.bg_fiber:wakeup()
    end
end

if not rawget(_G, MODULE_INTERNALS) then
    rawset(_G, MODULE_INTERNALS, M)
end

return {
    set_state = set_state,
    internal = M,
}
end
end

do
local _ENV = _ENV
package.preload[ "vshard.registry" ] = function( ... ) local arg = _G.arg;
--
-- Registry is a way to resolve cyclic dependencies which normally can exist
-- between files of the same module/library.
--
-- Files, which want to expose their API to the other files, which in turn can't
-- require the formers directly, should put their API to the registry.
--
-- The files should use the registry to get API of the other files. They don't
-- require() and use the latter directly if there is a known loop dependency
-- between them.
--
-- At runtime, when all require() are done, the registry is full, and all the
-- files see API of each other.
--
-- Having the modules accessed via the registry adds at lest +1 indexing
-- operation at runtime when need to get a function from there. But sometimes it
-- can be cached to reduce the effect in perf-sensitive code. For example, like
-- this:
--
--     local lreg = require('vshard.registry')
--
--     local storage_func
--
--     local function storage_func_no_cache(...)
--         storage_func = lreg.storage.func
--         return storage_func(...)
--     end
--
--     storage_func = storage_func_no_cache
--
-- The code will always call storage_func(), but will load it from the registry
-- only on first invocation.
--
-- However in case reload is important, it is not possible - the original
-- function object in the registry may change. In such situation still makes
-- sense to cache at least 'lreg.storage' to save 1 indexing operation.
--
--     local lreg = require('vshard.registry')
--
--     local lstorage
--     local storage_func
--
--     local function storage_func_cache(...)
--         return lstorage.storage_func(...)
--     end
--
--     local function storage_func_no_cache(...)
--         lstorage = lref.storage
--         storage_func = storage_func_cache
--         return lstorage.storage_func(...)
--     end
--
--     storage_func = storage_func_no_cache
--
-- A harder way would be to use the first approach + add triggers on reload of
-- the cached module to update the cached function refs. If the code is
-- extremely perf-critical (which should not be Lua then).
--

local MODULE_INTERNALS = '__module_vshard_registry'

local M = rawget(_G, MODULE_INTERNALS)
if not M then
    M = {}
    rawset(_G, MODULE_INTERNALS, M)
end

return M
end
end

do
local _ENV = _ENV
package.preload[ "vshard.replicaset" ] = function( ... ) local arg = _G.arg;
-- vshard.replicaset

--
-- <replicaset> = {
--     replicas = {
--         [replica_uuid] = {
--             uri = string,
--             name = string,
--             uuid = string,
--             conn = <netbox> + .replica + .replicaset,
--             zone = number,
--             next_by_priority = <replica object of the same type>,
--             weight = number,
--             down_ts = <timestamp of disconnect from the
--                        replica>,
--             backoff_ts = <timestamp when was sent into backoff state>,
--             backoff_err = <error object caused the backoff>,
--             net_timeout = <current network timeout for calls,
--                            doubled on each network fail until
--                            max value, and reset to minimal
--                            value on each success>,
--             net_sequential_ok = <count of sequential success
--                                  requests to the replica>,
--             net_sequential_fail = <count of sequential failed
--                                    requests to the replica>,
--             is_outdated = nil/true,
--          }
--      },
--      master = <master server from the array above>,
--      master_cond = <condition variable signaled when the replicaset finds or
--                     changes its master>,
--      is_auto_master = <true when is configured to find the master on
--                        its own>,
--      replica = <nearest available replica object>,
--      balance_i = <index of a next replica in priority_list to
--                   use for a load-balanced request>,
--      replica_up_ts = <timestamp updated on each attempt to
--                       connect to the nearest replica, and on
--                       each connect event>,
--      uuid = <replicaset_uuid>,
--      weight = number,
--      priority_list = <list of replicas, sorted by weight asc>,
--      etalon_bucket_count = <bucket count, that must be stored
--                             on this replicaset to reach the
--                             balance in a cluster>,
--      is_outdated = nil/true,
--  }
--
-- replicasets = {
--    [replicaset_uuid] = <replicaset>
-- }
--

local log = require('log')
local netbox = require('net.box')
local consts = require('vshard.consts')
local lerror = require('vshard.error')
local fiber = require('fiber')
local luri = require('uri')
local luuid = require('uuid')
local ffi = require('ffi')
local util = require('vshard.util')
local fiber_clock = fiber.clock
local fiber_yield = fiber.yield
local fiber_cond_wait = util.fiber_cond_wait
local gsc = util.generate_self_checker

--
-- on_connect() trigger for net.box
--
local function netbox_on_connect(conn)
    log.info("connected to %s:%s", conn.host, conn.port)
    local rs = conn.replicaset
    local replica = conn.replica
    assert(replica ~= nil)
    -- If a replica's connection has revived, then unset
    -- replica.down_ts - it is not down anymore.
    replica.down_ts = nil
    if conn.peer_uuid ~= replica.uuid and
        -- XXX: Zero UUID means not a real Tarantool instance. It
        -- is likely to be a cartridge.remote-control server,
        -- which is started before the actual storage. Let it
        -- work, anyway it will be shut down, and reconnect to the
        -- real storage will happen. Otherwise the connection will
        -- be left broken in 'closed' state until a request will
        -- come specifically for this instance, or reconfiguration
        -- will happen. That would prevent reconnect to the real
        -- storage.
       conn.peer_uuid ~= luuid.NULL:str() then
        log.info('Mismatch server UUID on replica %s: expected "%s", but got '..
                 '"%s"', replica, replica.uuid, conn.peer_uuid)
        conn:close()
        return
    end
    if replica == rs.replica and replica == rs.priority_list[1] then
        -- Update replica_up_ts, if the current replica has the
        -- biggest priority. Really, it is not necessary to
        -- increase replica connection priority, if the current
        -- one already has the biggest priority. (See failover_f).
        rs.replica_up_ts = fiber_clock()
    end
end

--
-- on_disconnect() trigger for net.box
--
local function netbox_on_disconnect(conn)
    log.info("disconnected from %s:%s", conn.host, conn.port)
    assert(conn.replica)
    -- Replica is down - remember this time to decrease replica
    -- priority after FAILOVER_DOWN_TIMEOUT seconds.
    conn.replica.down_ts = fiber_clock()
end

--
-- Wait until the connection is established. This is necessary at least for
-- async requests because they fail immediately if the connection is not done.
-- Returns the remaining timeout because is expected to be used to connect to
-- many instances in a loop, where such return saves one clock get in the caller
-- code and is just cleaner code.
--
local function netbox_wait_connected(conn, timeout)
    -- Fast path. Usually everything is connected.
    if conn:is_connected() then
        return timeout
    end
    local deadline = fiber_clock() + timeout
    -- Loop against spurious wakeups.
    repeat
        -- Netbox uses fiber_cond inside, which throws an irrelevant usage error
        -- at negative timeout. Need to check the case manually.
        if timeout < 0 then
            return nil, lerror.timeout()
        end
        local ok, res = pcall(conn.wait_connected, conn, timeout)
        if not ok then
            return nil, lerror.make(res)
        end
        if not res then
            return nil, lerror.timeout()
        end
        timeout = deadline - fiber_clock()
    until conn:is_connected()
    return timeout
end

--
-- Check if the replica is not in backoff. It also serves as an update - if the
-- replica still has an old backoff timestamp, it is cleared. This way of
-- backoff update does not require any fibers to perform background updates.
-- Hence works not only on the router.
--
local function replica_check_backoff(replica, now)
    if not replica.backoff_ts then
        return true
    end
    if replica.backoff_ts + consts.REPLICA_BACKOFF_INTERVAL > now then
        return false
    end
    log.warn('Replica %s returns from backoff', replica.uuid)
    replica.backoff_ts = nil
    replica.backoff_err = nil
    return true
end

--
-- Connect to a specified replica and remember a new connection
-- in the replica object. Note, that the function does not wait
-- until a connection is established.
--
local function replicaset_connect_to_replica(replicaset, replica)
    local conn = replica.conn
    if not conn or conn.state == 'closed' then
        conn = netbox.connect(replica.uri, {
            reconnect_after = consts.RECONNECT_TIMEOUT,
            wait_connected = false
        })
        conn.replica = replica
        conn.replicaset = replicaset
        conn.on_connect_ref = netbox_on_connect
        conn:on_connect(netbox_on_connect)
        conn.on_disconnect_ref = netbox_on_disconnect
        conn:on_disconnect(netbox_on_disconnect)
        replica.conn = conn
    end
    return conn
end

local function replicaset_wait_master(replicaset, timeout)
    local master = replicaset.master
    -- Fast path - master is almost always known.
    if master then
        return master, timeout
    end
    -- Slow path.
    local deadline = fiber_clock() + timeout
    repeat
        if not replicaset.is_auto_master or
           not fiber_cond_wait(replicaset.master_cond, timeout) then
            return nil, lerror.vshard(lerror.code.MISSING_MASTER,
                                      replicaset.uuid)
        end
        timeout = deadline - fiber_clock()
        master = replicaset.master
    until master
    return master, timeout
end

--
-- Create net.box connection to master.
--
local function replicaset_connect_master(replicaset)
    local master = replicaset.master
    if master == nil then
        return nil, lerror.vshard(lerror.code.MISSING_MASTER,
                                  replicaset.uuid)
    end
    return replicaset_connect_to_replica(replicaset, master)
end

--
-- Wait until the master instance is connected.
--
local function replicaset_wait_connected(replicaset, timeout)
    local master
    master, timeout = replicaset_wait_master(replicaset, timeout)
    if not master then
        return nil, timeout
    end
    local conn = replicaset_connect_to_replica(replicaset, master)
    return netbox_wait_connected(conn, timeout)
end

--
-- Create net.box connections to all replicas and master.
--
local function replicaset_connect_all(replicaset)
    for _, replica in pairs(replicaset.replicas) do
        replicaset_connect_to_replica(replicaset, replica)
    end
end

--
-- Connect to a next replica with less priority against a current
-- one. It is needed, if a current replica's connection is down
-- too long.
--
local function replicaset_down_replica_priority(replicaset)
    local old_replica = replicaset.replica
    assert(old_replica and old_replica.down_ts and
           not old_replica:is_connected())
    local new_replica = old_replica.next_by_priority
    if new_replica then
        assert(new_replica ~= old_replica)
        replicaset_connect_to_replica(replicaset, new_replica)
        replicaset.replica = new_replica
    end
    -- Else the current replica already has the lowest priority.
    -- Can not down it.
end

--
-- Search a replica with higher priority than a current replica
-- has.
--
local function replicaset_up_replica_priority(replicaset)
    local old_replica = replicaset.replica
    if old_replica == replicaset.priority_list[1] and
       old_replica:is_connected() then
        replicaset.replica_up_ts = fiber_clock()
        return
    end
    for _, replica in pairs(replicaset.priority_list) do
        if replica == old_replica then
            -- Failed to up priority.
            return
        end
        if replica:is_connected() then
            replicaset.replica = replica
            assert(not old_replica or
                   old_replica.weight >= replicaset.replica.weight)
            return
        end
    end
end

--
-- Handler for failed request to a replica. It increments count
-- of sequentially failed requests. When it reaches 2, it
-- increases network timeout twice.
--
local function replica_on_failed_request(replica)
    replica.net_sequential_ok = 0
    local val = replica.net_sequential_fail + 1
    if val >= 2 then
        local new_timeout = replica.net_timeout * 2
        if new_timeout <= consts.CALL_TIMEOUT_MAX then
            replica.net_timeout = new_timeout
        end
        replica.net_sequential_fail = 1
    else
        replica.net_sequential_fail = val
    end
end

--
-- Same, as above, but for success request. And when count of
-- success requests reaches 10, the network timeout is decreased
-- to minimal timeout.
--
local function replica_on_success_request(replica)
    replica.net_sequential_fail = 0
    local val = replica.net_sequential_ok + 1
    if val >= 10 then
        replica.net_timeout = consts.CALL_TIMEOUT_MIN
        replica.net_sequential_ok = 1
    else
        replica.net_sequential_ok = val
    end
end

--
-- Call a function on a replica using its connection. The typical
-- usage is calls under storage.call, because of which there
-- are no more than 3 return values. It is because storage.call
-- returns:
-- * true/nil for storage.call();
-- * error object, if storage.call() was not ok, or called
--   function retval;
-- * error object, if called function has been failed, or nil
--   else.
-- @retval  true, ... The correct response is received.
-- @retval false, ... Response is not received. It can be timeout
--         or unexpectedly closed connection.
--
local function replica_call(replica, func, args, opts)
    assert(opts and opts.timeout)
    local conn = replica.conn
    local net_status, storage_status, retval, error_object =
        pcall(conn.call, conn, func, args, opts)
    if not net_status then
        -- Do not increase replica's network timeout, if the
        -- requested one was less, than network's one. For
        -- example, if replica's timeout was 30s, but an user
        -- specified 1s and it was expired, then there is no
        -- reason to increase network timeout.
        if opts.timeout >= replica.net_timeout then
            replica_on_failed_request(replica)
        end
        local err = storage_status
        -- VShard functions can throw exceptions using error() function. When
        -- it reaches the network layer, it is wrapped into LuajitError. Try to
        -- extract the original error if this is the case. Not always is
        -- possible - the string representation could be truncated.
        --
        -- In old Tarantool versions LuajitError turned into ClientError on the
        -- client. Check both types.
        if func:startswith('vshard.') and (err.type == 'LuajitError' or
           err.type == 'ClientError') then
            err = lerror.from_string(err.message) or err
        end
        log.error("Exception during calling '%s' on '%s': %s", func, replica,
                  err)
        return false, nil, lerror.make(err)
    else
        replica_on_success_request(replica)
    end
    if storage_status == nil then
        -- Workaround for `not msgpack.NULL` magic.
        storage_status = nil
    end
    return true, storage_status, retval, error_object
end

--
-- Detach the connection object from its replica object.
-- Detachment means that the connection is not closed, but all its
-- links with the replica are teared. All current requests are
-- finished, but next calls on this replica are processed by
-- another connection.
-- Initially this function is intended for failover, which should
-- not close the old connection in case if it receives a huge
-- response and because of it ignores pings.
--
local function replica_detach_conn(replica)
    local c = replica.conn
    if c ~= nil then
        -- The connection now has nothing to do with the replica
        -- object. In particular, it shall not touch up and down
        -- ts.
        c:on_connect(nil, c.on_connect_ref)
        c.on_connect_ref = nil
        c:on_disconnect(nil, c.on_disconnect_ref)
        c.on_disconnect_ref = nil
        -- Detach looks like disconnect for an observer.
        netbox_on_disconnect(c)
        c.replica = nil
        c.replicaset = nil
        replica.conn = nil
    end
end

--
-- Call a function on remote storage
-- Note: this function uses pcall-style error handling
-- @retval false, err on error
-- @retval true, ... on success
--
local function replicaset_master_call(replicaset, func, args, opts)
    assert(opts == nil or type(opts) == 'table')
    assert(type(func) == 'string', 'function name')
    assert(args == nil or type(args) == 'table', 'function arguments')
    local master = replicaset.master
    if not master then
        opts = opts and table.copy(opts) or {}
        if opts.is_async then
            return nil, lerror.vshard(lerror.code.MISSING_MASTER,
                                      replicaset.uuid)
        end
        local timeout = opts.timeout or consts.MASTER_SEARCH_TIMEOUT
        master, timeout = replicaset_wait_master(replicaset, timeout)
        if not master then
            return nil, timeout
        end
        opts.timeout = master.net_timeout
    else
        if not opts then
            opts = {timeout = master.net_timeout}
        elseif not opts.timeout then
            opts = table.copy(opts)
            opts.timeout = master.net_timeout
        end
    end
    replicaset_connect_to_replica(replicaset, master)
    local net_status, storage_status, retval, error_object =
        replica_call(master, func, args, opts)
    -- Ignore net_status - master does not retry requests.
    return storage_status, retval, error_object
end

--
-- True, if after error @a e a read request can be retried.
--
local function can_retry_after_error(e)
    if not e or (type(e) ~= 'table' and
                 (type(e) ~= 'cdata' or not ffi.istype('struct error', e))) then
        return false
    end
    if e.type == 'ShardingError' and
       (e.code == lerror.code.WRONG_BUCKET or
        e.code == lerror.code.TRANSFER_IS_IN_PROGRESS) then
        return true
    end
    return e.type == 'ClientError' and e.code == box.error.TIMEOUT
end

--
-- True if after the given error on call of the given function the connection
-- must go into backoff.
--
local function can_backoff_after_error(e, func)
    if not e then
        return false
    end
    if type(e) ~= 'table' and
       (type(e) ~= 'cdata' or not ffi.istype('struct error', e)) then
        return false
    end
    -- So far it is enabled only for vshard's own functions. Including
    -- vshard.storage.call(). Otherwise it is not possible to retry safely -
    -- user's function could have side effects before raising that error.
    -- For instance, 'access denied' could be raised by user's function
    -- internally after it already changed some data on the storage.
    if not func:startswith('vshard.') then
        return false
    end
    -- ClientError is sent for all errors by old Tarantool versions which didn't
    -- keep error type. New versions preserve the original error type.
    if e.type == 'ClientError' or e.type == 'AccessDeniedError' then
        if e.code == box.error.ACCESS_DENIED then
            return e.message:startswith("Execute access to function 'vshard.")
        end
        if e.code == box.error.NO_SUCH_PROC then
            return e.message:startswith("Procedure 'vshard.")
        end
    end
    if e.type == 'ShardingError' then
        return e.code == lerror.code.STORAGE_IS_DISABLED
    end
    return false
end

--
-- Pick a next replica according to round-robin load balancing
-- policy.
--
local function replicaset_balance_replica(replicaset)
    local i = replicaset.balance_i
    local pl = replicaset.priority_list
    local size = #pl
    replicaset.balance_i = i % size + 1
    assert(i <= size)
    return pl[i]
end

--
-- Template to implement a function able to visit multiple
-- replicas with certain details. One of applications - a function
-- making a call on a nearest available replica. It is possible
-- for 'read' requests only. And if the nearest replica is not
-- available now, then use master's connection - we can not wait
-- until failover fiber will repair the nearest connection.
--
local function replicaset_template_multicallro(prefer_replica, balance)
    local function pick_next_replica(replicaset, now)
        local r
        local master = replicaset.master
        if balance then
            local i = #replicaset.priority_list
            while i > 0 do
                r = replicaset_balance_replica(replicaset)
                i = i - 1
                if r:is_connected() and (not prefer_replica or r ~= master) and
                   replica_check_backoff(r, now) then
                    return r
                end
            end
        else
            local start_r = replicaset.replica
            r = start_r
            while r do
                if r:is_connected() and (not prefer_replica or r ~= master) and
                   replica_check_backoff(r, now) then
                    return r
                end
                r = r.next_by_priority
            end
            -- Iteration above could start not from the best prio replica.
            -- Check the beginning of the list too.
            for _, r in ipairs(replicaset.priority_list) do
                if r == start_r then
                    -- Reached already checked part.
                    break
                end
                if r:is_connected() and (not prefer_replica or r ~= master) and
                   replica_check_backoff(r, now) then
                    return r
                end
            end
        end
    end

    return function(replicaset, func, args, opts)
        assert(opts == nil or type(opts) == 'table')
        assert(type(func) == 'string', 'function name')
        assert(args == nil or type(args) == 'table', 'function arguments')
        opts = opts and table.copy(opts) or {}
        local timeout = opts.timeout or consts.CALL_TIMEOUT_MAX
        local net_status, storage_status, retval, err, replica
        if timeout <= 0 then
            return nil, lerror.timeout()
        end
        local now = fiber_clock()
        local end_time = now + timeout
        while not net_status and timeout > 0 do
            replica = pick_next_replica(replicaset, now)
            if not replica then
                replica, timeout = replicaset_wait_master(replicaset, timeout)
                if not replica then
                    return nil, timeout
                end
                replicaset_connect_to_replica(replicaset, replica)
                if replica.backoff_ts then
                    return nil, lerror.vshard(
                        lerror.code.REPLICASET_IN_BACKOFF, replicaset.uuid,
                        replica.backoff_err)
                end
            end
            opts.timeout = timeout
            net_status, storage_status, retval, err =
                replica_call(replica, func, args, opts)
            now = fiber_clock()
            timeout = end_time - now
            if not net_status and not storage_status and
               not can_retry_after_error(retval) then
                if can_backoff_after_error(retval, func) then
                    if not replica.backoff_ts then
                        log.warn('Replica %s goes into backoff for %s sec '..
                                 'after error %s', replica.uuid,
                                 consts.REPLICA_BACKOFF_INTERVAL, retval)
                        replica.backoff_ts = now
                        replica.backoff_err = retval
                    end
                else
                    -- There is no sense to retry LuaJit errors, such as
                    -- assertions, undefined variables etc.
                    net_status = true
                    break
                end
            end
        end
        if not net_status then
            return nil, lerror.make(retval)
        else
            return storage_status, retval, err
        end
    end
end

--
-- Nice formatter for replicaset
--
local function replicaset_tostring(replicaset)
    local master
    if replicaset.master then
        master = replicaset.master
    else
        master = 'missing'
    end
    return string.format('replicaset(uuid="%s", master=%s)', replicaset.uuid,
                         master)
end

--
-- Copy netbox connections from old replica objects to new ones
-- and outdate old objects.
-- @param replicasets New replicasets
-- @param old_replicasets Replicasets and replicas to be outdated.
--
local function rebind_replicasets(replicasets, old_replicasets)
    for replicaset_uuid, replicaset in pairs(replicasets) do
        local old_replicaset = old_replicasets and
                               old_replicasets[replicaset_uuid]
        for replica_uuid, replica in pairs(replicaset.replicas) do
            local old_replica = old_replicaset and
                                old_replicaset.replicas[replica_uuid]
            if old_replica and old_replica.uri == replica.uri then
                local conn = old_replica.conn
                replica.conn = conn
                replica.down_ts = old_replica.down_ts
                replica.backoff_ts = old_replica.backoff_ts
                replica.backoff_err = old_replica.backoff_err
                replica.net_timeout = old_replica.net_timeout
                replica.net_sequential_ok = old_replica.net_sequential_ok
                replica.net_sequential_fail = old_replica.net_sequential_fail
                if conn then
                    conn.replica = replica
                    conn.replicaset = replicaset
                end
            end
        end
        if old_replicaset then
            -- Take a hint from the old replicaset who is the master now.
            if replicaset.is_auto_master then
                local master = old_replicaset.master
                if master then
                    replicaset.master = replicaset.replicas[master.uuid]
                end
            end
            -- Stop waiting for master in the old replicaset. Its running
            -- requests won't find it anyway. Auto search works only for the
            -- most actual replicaset objects.
            if old_replicaset.is_auto_master then
                old_replicaset.is_auto_master = false
                old_replicaset.master_cond:broadcast()
            end
        end
    end
end

--
-- Let the replicaset know @a old_master_uuid is not a master anymore, should
-- use @a candidate_uuid instead.
-- Returns whether the request, which brought this information, can be retried.
--
local function replicaset_update_master(replicaset, old_master_uuid,
                                        candidate_uuid)
    local is_auto = replicaset.is_auto_master
    local replicaset_uuid = replicaset.uuid
    if old_master_uuid == candidate_uuid then
        -- It should not happen ever, but be ready to everything.
        log.warn('Replica %s in replicaset %s reports self as both master '..
                 'and not master', old_master_uuid, replicaset_uuid)
        return is_auto
    end
    local master = replicaset.master
    if not master then
        if not is_auto or not candidate_uuid then
            return is_auto
        end
        local candidate = replicaset.replicas[candidate_uuid]
        if not candidate then
            return true
        end
        replicaset.master = candidate
        log.info('Replica %s becomes a master as reported by %s for '..
                 'replicaset %s', candidate_uuid, old_master_uuid,
                 replicaset_uuid)
        return true
    end
    local master_uuid = master.uuid
    if master_uuid ~= old_master_uuid then
        -- Master was changed while the master update information was coming.
        -- It means it is outdated and should be ignored.
        -- Return true regardless of the auto-master config. Because the master
        -- change means the caller's request has a chance to succeed on the new
        -- master on retry.
        return true
    end
    if not is_auto then
        log.warn('Replica %s is not master for replicaset %s anymore, please '..
                 'update configuration', master_uuid, replicaset_uuid)
        return false
    end
    if not candidate_uuid then
        replicaset.master = nil
        log.warn('Replica %s is not a master anymore for replicaset %s, no '..
                 'candidate was reported', master_uuid, replicaset_uuid)
        return true
    end
    local candidate = replicaset.replicas[candidate_uuid]
    if candidate then
        replicaset.master = candidate
        log.info('Replica %s becomes master instead of %s for replicaset %s',
                 candidate_uuid, master_uuid, replicaset_uuid)
    else
        replicaset.master = nil
        log.warn('Replica %s is not a master anymore for replicaset %s, new '..
                 'master %s could not be found in the config',
                 master_uuid, replicaset_uuid, candidate_uuid)
    end
    return true
end

--
-- Check if the master is still master, and find a new master if there is no a
-- known one.
--
local function replicaset_locate_master(replicaset)
    local is_done = true
    local is_nop = true
    if not replicaset.is_auto_master then
        return is_done, is_nop
    end
    local func = 'vshard.storage._call'
    local args = {'info'}
    local const_timeout = consts.MASTER_SEARCH_TIMEOUT
    local ok, res, err, f
    local master = replicaset.master
    if master then
        local sync_opts = {timeout = const_timeout}
        ok, res, err = replica_call(master, func, args, sync_opts)
        if not ok then
            return is_done, is_nop, err
        end
        if res.is_master then
            return is_done, is_nop
        end
        -- Could be changed during the call from the outside. For
        -- instance, by a failed request with a hint from the old
        -- master.
        local cur_master = replicaset.master
        if cur_master == master then
            log.info('Master of replicaset %s, node %s, has resigned. Trying '..
                     'to find a new one', replicaset.uuid, master.uuid)
            replicaset.master = nil
        elseif cur_master then
            -- Another master was already found. But check it via another call
            -- later to avoid an infinite loop here.
            return is_done, is_nop
        end
    end
    is_nop = false

    local last_err
    local futures = {}
    local timeout = const_timeout
    local deadline = fiber_clock() + timeout
    local async_opts = {is_async = true}
    local replicaset_uuid = replicaset.uuid
    for replica_uuid, replica in pairs(replicaset.replicas) do
        local conn = replica.conn
        if conn:is_connected() then
            ok, f = pcall(conn.call, conn, func, args, async_opts)
            if not ok then
                last_err = lerror.make(f)
            else
                futures[replica_uuid] = f
            end
        end
    end
    local master_uuid
    for replica_uuid, f in pairs(futures) do
        if timeout < 0 then
            -- Netbox uses cond var inside, whose wait throws an error when gets
            -- a negative timeout. Avoid that.
            -- Even if the timeout has expired, still walk though the futures
            -- hoping for a chance that some of them managed to finish and bring
            -- a sign of a master.
            timeout = 0
        end
        res, err = f:wait_result(timeout)
        timeout = deadline - fiber_clock()
        if not res then
            f:discard()
            last_err = err
            goto next_wait
        end
        res = res[1]
        if not res.is_master then
            goto next_wait
        end
        if not master_uuid then
            master_uuid = replica_uuid
            goto next_wait
        end
        is_done = false
        last_err = lerror.vshard(lerror.code.MULTIPLE_MASTERS_FOUND,
                                 replicaset_uuid, master_uuid, replica_uuid)
        do return is_done, is_nop, last_err end
        ::next_wait::
    end
    master = replicaset.replicas[master_uuid]
    if master then
        log.info('Found master for replicaset %s: %s', replicaset_uuid,
                 master_uuid)
        replicaset.master = master
        replicaset.master_cond:broadcast()
    else
        is_done = false
    end
    return is_done, is_nop, last_err
end

local function locate_masters(replicasets)
    local is_all_done = true
    local is_all_nop = true
    local last_err
    for _, replicaset in pairs(replicasets) do
        local is_done, is_nop, err = replicaset_locate_master(replicaset)
        is_all_done = is_all_done and is_done
        is_all_nop = is_all_nop and is_nop
        last_err = err or last_err
        fiber_yield()
    end
    return is_all_done, is_all_nop, last_err
end

--
-- Meta-methods
--
local replicaset_mt = {
    __index = {
        connect = replicaset_connect_master;
        connect_master = replicaset_connect_master;
        connect_all = replicaset_connect_all;
        connect_replica = replicaset_connect_to_replica;
        down_replica_priority = replicaset_down_replica_priority;
        up_replica_priority = replicaset_up_replica_priority;
        wait_connected = replicaset_wait_connected,
        call = replicaset_master_call;
        callrw = replicaset_master_call;
        callro = replicaset_template_multicallro(false, false);
        callbro = replicaset_template_multicallro(false, true);
        callre = replicaset_template_multicallro(true, false);
        callbre = replicaset_template_multicallro(true, true);
        update_master = replicaset_update_master,
    };
    __tostring = replicaset_tostring;
}
--
-- Wrap self methods with a sanity checker.
--
local index = {}
for name, func in pairs(replicaset_mt.__index) do
    index[name] = gsc("replicaset", name, replicaset_mt, func)
end
replicaset_mt.__index = index

local replica_mt = {
    __index = {
        is_connected = function(replica)
            return replica.conn and replica.conn:is_connected()
        end,
        safe_uri = function(replica)
            local uri = luri.parse(replica.uri)
            uri.password = nil
            return luri.format(uri)
        end,
        detach_conn = replica_detach_conn,
    },
    __tostring = function(replica)
        if replica.name then
            return replica.name..'('..replica:safe_uri()..')'
        else
            return replica:safe_uri()
        end
    end,
}
index = {}
for name, func in pairs(replica_mt.__index) do
    index[name] = gsc("replica", name, replica_mt, func)
end
replica_mt.__index = index

--
-- Meta-methods of outdated objects.
-- They define only attributes from corresponding metatables to
-- make user able to access fields of old objects.
--
local function outdated_warning(...)
    return nil, lerror.vshard(lerror.code.OBJECT_IS_OUTDATED)
end

local outdated_replicaset_mt = {
    __index = {
        is_outdated = true
    }
}
for fname, func in pairs(replicaset_mt.__index) do
    outdated_replicaset_mt.__index[fname] = outdated_warning
end

local outdated_replica_mt = {
    __index = {
        is_outdated = true
    }
}
for fname, func in pairs(replica_mt.__index) do
    outdated_replica_mt.__index[fname] = outdated_warning
end

local function outdate_replicasets_f(replicasets)
    for _, replicaset in pairs(replicasets) do
        setmetatable(replicaset, outdated_replicaset_mt)
        for _, replica in pairs(replicaset.replicas) do
            setmetatable(replica, outdated_replica_mt)
            replica.conn = nil
        end
    end
    log.info('Old replicaset and replica objects are outdated.')
end

--
-- Outdate replicaset and replica objects:
--  * Set outdated_metatables.
--  * Remove connections.
-- @param replicasets Old replicasets to be outdated.
-- @param outdate_delay Delay in seconds before the outdating.
--
local function outdate_replicasets(replicasets, outdate_delay)
    if replicasets then
        util.async_task(outdate_delay, outdate_replicasets_f,
                        replicasets)
    end
end

--
-- Calculate for each replicaset its etalon bucket count.
-- Iterative algorithm is used to learn the best balance in a
-- cluster. On each step it calculates perfect bucket count for
-- each replicaset. If this count can not be satisfied due to
-- pinned buckets, the algorithm does best effort to get the
-- perfect balance. This is done via ignoring of replicasets
-- disbalanced via pinning, and their pinned buckets. After that a
-- new balance is calculated. And it can happen, that it can not
-- be satisfied too. It is possible, because ignoring of pinned
-- buckets in overpopulated replicasets leads to decrease of
-- perfect bucket count in other replicasets, and a new values can
-- become less that their pinned bucket count.
--
-- On each step the algorithm either is finished, or ignores at
-- least one new overpopulated replicaset, so it has complexity
-- O(N^2), where N - replicaset count.
--
local function cluster_calculate_etalon_balance(replicasets, bucket_count)
    local is_balance_found = false
    local weight_sum = 0
    local step_count = 0
    local replicaset_count = 0
    for _, replicaset in pairs(replicasets) do
        weight_sum = weight_sum + replicaset.weight
        replicaset_count = replicaset_count + 1
    end
    while not is_balance_found do
        step_count = step_count + 1
        assert(weight_sum > 0)
        local bucket_per_weight = bucket_count / weight_sum
        local buckets_calculated = 0
        for _, replicaset in pairs(replicasets) do
            if not replicaset.ignore_disbalance then
                replicaset.etalon_bucket_count =
                    math.ceil(replicaset.weight * bucket_per_weight)
                buckets_calculated =
                    buckets_calculated + replicaset.etalon_bucket_count
            end
        end
        local buckets_rest = buckets_calculated - bucket_count
        is_balance_found = true
        for _, replicaset in pairs(replicasets) do
            if not replicaset.ignore_disbalance then
                -- A situation is possible, when bucket_per_weight
                -- is not integer. Lets spread this disbalance
                -- over the cluster.
                if buckets_rest > 0 then
                    local n = replicaset.weight * bucket_per_weight
                    local ceil = math.ceil(n)
                    local floor = math.floor(n)
                    if replicaset.etalon_bucket_count > 0 and ceil ~= floor then
                        replicaset.etalon_bucket_count =
                            replicaset.etalon_bucket_count - 1
                        buckets_rest = buckets_rest - 1
                    end
                end
                --
                -- Search for incorrigible disbalance due to
                -- pinned buckets.
                --
                local pinned = replicaset.pinned_count
                if pinned and replicaset.etalon_bucket_count < pinned then
                    -- This replicaset can not send out enough
                    -- buckets to reach a balance. So do the best
                    -- effort balance by sending from the
                    -- replicaset though non-pinned buckets. This
                    -- replicaset and its pinned buckets does not
                    -- participate in the next steps of balance
                    -- calculation.
                    is_balance_found = false
                    bucket_count = bucket_count - replicaset.pinned_count
                    replicaset.etalon_bucket_count = replicaset.pinned_count
                    replicaset.ignore_disbalance = true
                    weight_sum = weight_sum - replicaset.weight
                end
            end
        end
        assert(buckets_rest == 0)
        if step_count > replicaset_count then
            -- This can happed only because of a bug in this
            -- algorithm. But it occupies 100% of transaction
            -- thread, so check step count explicitly.
            return error('PANIC: the rebalancer is broken')
        end
    end
end

--
-- Update/build replicasets from configuration
--
local function buildall(sharding_cfg)
    local new_replicasets = {}
    local weights = sharding_cfg.weights
    local zone = sharding_cfg.zone
    local zone_weights
    if weights and zone and weights[zone] then
        zone_weights = weights[zone]
    else
        zone_weights = {}
    end
    local curr_ts = fiber_clock()
    for replicaset_uuid, replicaset in pairs(sharding_cfg.sharding) do
        local new_replicaset = setmetatable({
            replicas = {},
            uuid = replicaset_uuid,
            weight = replicaset.weight,
            bucket_count = 0,
            lock = replicaset.lock,
            balance_i = 1,
            is_auto_master = replicaset.master == 'auto',
            master_cond = fiber.cond(),
        }, replicaset_mt)
        local priority_list = {}
        for replica_uuid, replica in pairs(replicaset.replicas) do
            -- The old replica is saved in the new object to
            -- rebind its connection at the end of a
            -- router/storage reconfiguration.
            local new_replica = setmetatable({
                uri = replica.uri, name = replica.name, uuid = replica_uuid,
                zone = replica.zone, net_timeout = consts.CALL_TIMEOUT_MIN,
                net_sequential_ok = 0, net_sequential_fail = 0,
                down_ts = curr_ts, backoff_ts = nil, backoff_err = nil,
            }, replica_mt)
            new_replicaset.replicas[replica_uuid] = new_replica
            if replica.master then
                new_replicaset.master = new_replica
            end
            if new_replica.zone then
                if zone_weights[new_replica.zone] then
                    new_replica.weight = zone_weights[new_replica.zone]
                elseif zone and new_replica.zone == zone then
                    new_replica.weight = 0
                else
                    new_replica.weight = math.huge
                end
            else
                new_replica.weight = math.huge
            end
            table.insert(priority_list, new_replica)
        end
        --
        -- Sort replicas of a replicaset by weight. The less is weight,
        -- the more priority has the replica. Sorted replicas are stored
        -- into replicaset.priority_list array.
        --

        -- Return true, if r1 has priority over r2.
        local function replica_cmp_weight(r1, r2)
            -- Master has priority over replicas with the same
            -- weight.
            if r1.weight == r2.weight then
                return r1 == new_replicaset.master
            else
                return r1.weight < r2.weight
            end
        end
        table.sort(priority_list, replica_cmp_weight)
        -- Create a forward list for down_replica_priority().
        for i = 1, #priority_list - 1 do
            priority_list[i].next_by_priority = priority_list[i + 1]
        end
        new_replicaset.priority_list = priority_list
        new_replicasets[replicaset_uuid] = new_replicaset
    end
    return new_replicasets
end

--
-- Wait for masters connection during RECONNECT_TIMEOUT seconds.
--
local function wait_masters_connect(replicasets)
    for _, rs in pairs(replicasets) do
        if rs.master then
            rs.master.conn:wait_connected(consts.RECONNECT_TIMEOUT)
        end
    end
end

return {
    buildall = buildall,
    calculate_etalon_balance = cluster_calculate_etalon_balance,
    wait_masters_connect = wait_masters_connect,
    rebind_replicasets = rebind_replicasets,
    outdate_replicasets = outdate_replicasets,
    locate_masters = locate_masters,
}
end
end

do
local _ENV = _ENV
package.preload[ "vshard.rlist" ] = function( ... ) local arg = _G.arg;
--
-- A subset of rlist methods from the main repository. Rlist is a
-- doubly linked list, and is used here to implement a queue of
-- routes in the parallel rebalancer.
--
local rlist_index = {}

function rlist_index.add_tail(rlist, object)
    local last = rlist.last
    if last then
        last.next = object
        object.prev = last
    else
        rlist.first = object
    end
    rlist.last = object
    rlist.count = rlist.count + 1
end

function rlist_index.remove(rlist, object)
    local prev = object.prev
    local next = object.next
    local belongs_to_list = false
    if prev then
        belongs_to_list = true
        prev.next = next
    end
    if next then
        belongs_to_list = true
        next.prev = prev
    end
    object.prev = nil
    object.next = nil
    if rlist.last == object then
        belongs_to_list = true
        rlist.last = prev
    end
    if rlist.first == object then
        belongs_to_list = true
        rlist.first = next
    end
    if belongs_to_list then
        rlist.count = rlist.count - 1
    end
end

local rlist_mt = {
    __index = rlist_index,
}

local function rlist_new()
    return setmetatable({count = 0}, rlist_mt)
end

return {
    new = rlist_new,
}
end
end

do
local _ENV = _ENV
package.preload[ "vshard.router" ] = function( ... ) local arg = _G.arg;
local log = require('log')
local lfiber = require('fiber')
local table_new = require('table.new')
local fiber_clock = lfiber.clock

local MODULE_INTERNALS = '__module_vshard_router'
-- Reload requirements, in case this module is reloaded manually.
if rawget(_G, MODULE_INTERNALS) then
    local vshard_modules = {
        'vshard.consts', 'vshard.error', 'vshard.cfg',
        'vshard.hash', 'vshard.replicaset', 'vshard.util',
        'vshard.lua_gc',
    }
    for _, module in pairs(vshard_modules) do
        package.loaded[module] = nil
    end
end
local consts = require('vshard.consts')
local lerror = require('vshard.error')
local lcfg = require('vshard.cfg')
local lhash = require('vshard.hash')
local lreplicaset = require('vshard.replicaset')
local util = require('vshard.util')
local lua_gc = require('vshard.lua_gc')
local seq_serializer = { __serialize = 'seq' }

local M = rawget(_G, MODULE_INTERNALS)
if not M then
    M = {
        ---------------- Common module attributes ----------------
        errinj = {
            ERRINJ_CFG = false,
            ERRINJ_FAILOVER_CHANGE_CFG = false,
            ERRINJ_RELOAD = false,
            ERRINJ_LONG_DISCOVERY = false,
            ERRINJ_MASTER_SEARCH_DELAY = false,
        },
        -- Dictionary, key is router name, value is a router.
        routers = {},
        -- Router object which can be accessed by old api:
        -- e.g. vshard.router.call(...)
        static_router = nil,
        -- This counter is used to restart background fibers with
        -- new reloaded code.
        module_version = 0,
        -- Number of router which require collecting lua garbage.
        collect_lua_garbage_cnt = 0,

        ----------------------- Map-Reduce -----------------------
        -- Storage Ref ID. It must be unique for each ref request
        -- and therefore is global and monotonically growing.
        ref_id = 0,
    }
end

--
-- Router object attributes.
--
local ROUTER_TEMPLATE = {
        -- Name of router.
        name = nil,
        -- The last passed configuration.
        current_cfg = nil,
        -- Time to outdate old objects on reload.
        connection_outdate_delay = nil,
        -- Bucket map cache.
        route_map = {},
        -- All known replicasets used for bucket re-balancing
        replicasets = nil,
        -- Fiber to maintain replica connections.
        failover_fiber = nil,
        -- Fiber to watch for master changes and find new masters.
        master_search_fiber = nil,
        -- Fiber to discovery buckets in background.
        discovery_fiber = nil,
        -- How discovery works. On - work infinitely. Off - no
        -- discovery.
        discovery_mode = nil,
        -- Bucket count stored on all replicasets.
        total_bucket_count = 0,
        known_bucket_count = 0,
        -- Boolean lua_gc state (create periodic gc task).
        collect_lua_garbage = nil,
        -- Timeout after which a ping is considered to be
        -- unacknowledged. Used by failover fiber to detect if a
        -- node is down.
        failover_ping_timeout = nil,
        --
        -- Timeout to wait sync on storages. Used by sync() call
        -- when no timeout is specified.
        --
        sync_timeout = consts.DEFAULT_SYNC_TIMEOUT,
}

local STATIC_ROUTER_NAME = '_static_router'

-- Set a bucket to a replicaset.
local function bucket_set(router, bucket_id, rs_uuid)
    local replicaset = router.replicasets[rs_uuid]
    -- It is technically possible to delete a replicaset at the
    -- same time when route to the bucket is discovered.
    if not replicaset then
        return nil, lerror.vshard(lerror.code.NO_ROUTE_TO_BUCKET, bucket_id)
    end
    local old_replicaset = router.route_map[bucket_id]
    if old_replicaset ~= replicaset then
        if old_replicaset then
            old_replicaset.bucket_count = old_replicaset.bucket_count - 1
        else
            router.known_bucket_count = router.known_bucket_count + 1
        end
        replicaset.bucket_count = replicaset.bucket_count + 1
    end
    router.route_map[bucket_id] = replicaset
    return replicaset
end

-- Remove a bucket from the cache.
local function bucket_reset(router, bucket_id)
    local replicaset = router.route_map[bucket_id]
    if replicaset then
        replicaset.bucket_count = replicaset.bucket_count - 1
        router.known_bucket_count = router.known_bucket_count - 1
    end
    router.route_map[bucket_id] = nil
end

local function route_map_clear(router)
    router.route_map = {}
    router.known_bucket_count = 0
    for _, rs in pairs(router.replicasets) do
        rs.bucket_count = 0
    end
end

--
-- Increase/decrease number of routers which require to collect
-- a lua garbage and change state of the `lua_gc` fiber.
--

local function lua_gc_cnt_inc()
    M.collect_lua_garbage_cnt = M.collect_lua_garbage_cnt + 1
    if M.collect_lua_garbage_cnt == 1 then
        lua_gc.set_state(true, consts.COLLECT_LUA_GARBAGE_INTERVAL)
    end
end

local function lua_gc_cnt_dec()
    M.collect_lua_garbage_cnt = M.collect_lua_garbage_cnt - 1
    assert(M.collect_lua_garbage_cnt >= 0)
    if M.collect_lua_garbage_cnt == 0 then
        lua_gc.set_state(false, consts.COLLECT_LUA_GARBAGE_INTERVAL)
    end
end

--------------------------------------------------------------------------------
-- Discovery
--------------------------------------------------------------------------------

-- Search bucket in whole cluster
local function bucket_discovery(router, bucket_id)
    local replicaset = router.route_map[bucket_id]
    if replicaset ~= nil then
        return replicaset
    end

    log.verbose("Discovering bucket %d", bucket_id)
    local last_err = nil
    local unreachable_uuid = nil
    for uuid, replicaset in pairs(router.replicasets) do
        local _, err =
            replicaset:callrw('vshard.storage.bucket_stat', {bucket_id})
        if err == nil then
            return bucket_set(router, bucket_id, replicaset.uuid)
        elseif err.code ~= lerror.code.WRONG_BUCKET and
               err.code ~= lerror.code.REPLICASET_IN_BACKOFF then
            last_err = err
            unreachable_uuid = uuid
        end
    end
    local err = nil
    if last_err then
        if last_err.type == 'ClientError' and
           last_err.code == box.error.NO_CONNECTION then
            err = lerror.vshard(lerror.code.UNREACHABLE_REPLICASET,
                                unreachable_uuid, bucket_id)
        else
            err = lerror.make(last_err)
        end
    else
        -- All replicasets were scanned, but a bucket was not
        -- found anywhere, so most likely it does not exist. It
        -- can be wrong, if rebalancing is in progress, and a
        -- bucket was found to be RECEIVING on one replicaset, and
        -- was not found on other replicasets (it was sent during
        -- discovery).
        err = lerror.vshard(lerror.code.NO_ROUTE_TO_BUCKET, bucket_id)
    end

    return nil, err
end

-- Resolve bucket id to replicaset uuid
local function bucket_resolve(router, bucket_id)
    local replicaset, err
    local replicaset = router.route_map[bucket_id]
    if replicaset ~= nil then
        return replicaset
    end
    -- Replicaset removed from cluster, perform discovery
    replicaset, err = bucket_discovery(router, bucket_id)
    if replicaset == nil then
        return nil, err
    end
    return replicaset
end

--
-- Arrange downloaded buckets to the route map so as they
-- reference a given replicaset.
--
local function discovery_handle_buckets(router, replicaset, buckets)
    local count = replicaset.bucket_count
    local affected = {}
    for _, bucket_id in pairs(buckets) do
        local old_rs = router.route_map[bucket_id]
        if old_rs ~= replicaset then
            count = count + 1
            if old_rs then
                local bc = old_rs.bucket_count
                if not affected[old_rs] then
                    affected[old_rs] = bc
                end
                old_rs.bucket_count = bc - 1
            else
                router.known_bucket_count = router.known_bucket_count + 1
            end
            router.route_map[bucket_id] = replicaset
        end
    end
    if count ~= replicaset.bucket_count then
        log.info('Updated %s buckets: was %d, became %d', replicaset,
                 replicaset.bucket_count, count)
    end
    replicaset.bucket_count = count
    for rs, old_bucket_count in pairs(affected) do
        log.info('Affected buckets of %s: was %d, became %d', rs,
                 old_bucket_count, rs.bucket_count)
    end
end

local discovery_f

if util.version_is_at_least(1, 10, 0) then
--
-- >= 1.10 version of discovery fiber does parallel discovery of
-- all replicasets at once. It uses is_async feature of netbox
-- for that.
--
discovery_f = function(router)
    local module_version = M.module_version
    assert(router.discovery_mode == 'on' or router.discovery_mode == 'once')
    local iterators = {}
    local opts = {is_async = true}
    local mode
    while module_version == M.module_version do
        -- Just typical map reduce - send request to each
        -- replicaset in parallel, and collect responses. Many
        -- requests probably will be needed for each replicaset.
        --
        -- Step 1: create missing iterators, in case this is a
        -- first discovery iteration, or some replicasets were
        -- added after the router is started.
        for rs_uuid in pairs(router.replicasets) do
            local iter = iterators[rs_uuid]
            if not iter then
                iterators[rs_uuid] = {
                    args = {{from = 1}},
                    future = nil,
                }
            end
        end
        -- Step 2: map stage - send parallel requests for every
        -- iterator, prune orphan iterators whose replicasets were
        -- removed.
        for rs_uuid, iter in pairs(iterators) do
            local replicaset = router.replicasets[rs_uuid]
            if not replicaset then
                log.warn('Replicaset %s was removed during discovery', rs_uuid)
                iterators[rs_uuid] = nil
                goto continue
            end
            local future, err =
                replicaset:callro('vshard.storage.buckets_discovery', iter.args,
                                  opts)
            if not future then
                log.warn('Error during discovery %s, retry will be done '..
                         'later: %s', rs_uuid, err)
                goto continue
            end
            iter.future = future
            -- Don't spam many requests at once. Give
            -- storages time to handle them and other
            -- requests.
            lfiber.sleep(consts.DISCOVERY_WORK_STEP)
            if module_version ~= M.module_version then
                return
            end
            ::continue::
        end
        -- Step 3: reduce stage - collect responses, restart
        -- iterators which reached the end.
        for rs_uuid, iter in pairs(iterators) do
            lfiber.yield()
            local future = iter.future
            if not future then
                goto continue
            end
            local result, err = future:wait_result(consts.DISCOVERY_TIMEOUT)
            if module_version ~= M.module_version then
                return
            end
            if not result then
                future:discard()
                log.warn('Error during discovery %s, retry will be done '..
                         'later: %s', rs_uuid, err)
                goto continue
            end
            local replicaset = router.replicasets[rs_uuid]
            if not replicaset then
                iterators[rs_uuid] = nil
                log.warn('Replicaset %s was removed during discovery', rs_uuid)
                goto continue
            end
            result = result[1]
            -- Buckets are returned as plain array by storages
            -- using old vshard version. But if .buckets is set,
            -- this is a new storage.
            discovery_handle_buckets(router, replicaset,
                                     result.buckets or result)
            local discovery_args = iter.args[1]
            discovery_args.from = result.next_from
            if not result.next_from then
                -- Nil next_from means no more buckets to get.
                -- Restart the iterator.
                iterators[rs_uuid] = nil
            end
            ::continue::
        end
        local unknown_bucket_count
        repeat
            unknown_bucket_count =
                router.total_bucket_count - router.known_bucket_count
            if unknown_bucket_count == 0 then
                if router.discovery_mode == 'once' then
                    log.info("Discovery mode is 'once', and all is "..
                             "discovered - shut down the discovery process")
                    router.discovery_fiber = nil
                    lfiber.self():cancel()
                    return
                end
                if mode ~= 'idle' then
                    log.info('Discovery enters idle mode, all buckets are '..
                             'known. Discovery works with %s seconds '..
                             'interval now', consts.DISCOVERY_IDLE_INTERVAL)
                    mode = 'idle'
                end
                lfiber.sleep(consts.DISCOVERY_IDLE_INTERVAL)
            elseif not next(router.replicasets) then
                if mode ~= 'idle' then
                    log.info('Discovery enters idle mode because '..
                             'configuration does not have replicasets. '..
                             'Retries will happen with %s seconds interval',
                             consts.DISCOVERY_IDLE_INTERVAL)
                    mode = 'idle'
                end
                lfiber.sleep(consts.DISCOVERY_IDLE_INTERVAL)
            else
                if mode ~= 'aggressive' then
                    log.info('Start aggressive discovery, %s buckets are '..
                             'unknown. Discovery works with %s seconds '..
                             'interval', unknown_bucket_count,
                             consts.DISCOVERY_WORK_INTERVAL)
                    mode = 'aggressive'
                end
                lfiber.sleep(consts.DISCOVERY_WORK_INTERVAL)
                break
            end
            while M.errinj.ERRINJ_LONG_DISCOVERY do
                M.errinj.ERRINJ_LONG_DISCOVERY = 'waiting'
                lfiber.sleep(0.01)
            end
        until next(router.replicasets)
    end
end

-- Version >= 1.10.
else
-- Version < 1.10.

--
-- Background fiber to perform discovery. It periodically scans
-- replicasets one by one and updates route_map.
--
discovery_f = function(router)
    local module_version = M.module_version
    while module_version == M.module_version do
        while not next(router.replicasets) do
            lfiber.sleep(consts.DISCOVERY_IDLE_INTERVAL)
        end
        local old_replicasets = router.replicasets
        for rs_uuid, replicaset in pairs(router.replicasets) do
            local active_buckets, err =
                replicaset:callro('vshard.storage.buckets_discovery', {},
                                  {timeout = 2})
            while M.errinj.ERRINJ_LONG_DISCOVERY do
                M.errinj.ERRINJ_LONG_DISCOVERY = 'waiting'
                lfiber.sleep(0.01)
            end
            -- Renew replicasets object captured by the for loop
            -- in case of reconfigure and reload events.
            if router.replicasets ~= old_replicasets then
                break
            end
            if not active_buckets then
                log.error('Error during discovery %s: %s', replicaset, err)
            else
                discovery_handle_buckets(router, replicaset, active_buckets)
            end
            lfiber.sleep(consts.DISCOVERY_IDLE_INTERVAL)
        end
    end
end

-- Version < 1.10.
end

--
-- Immediately wakeup discovery fiber if exists.
--
local function discovery_wakeup(router)
    if router.discovery_fiber then
        router.discovery_fiber:wakeup()
    end
end

local function discovery_set(router, new_mode)
    local current_mode = router.discovery_mode
    if current_mode == new_mode then
        return
    end
    router.discovery_mode = new_mode
    if router.discovery_fiber ~= nil then
        pcall(router.discovery_fiber.cancel, router.discovery_fiber)
        router.discovery_fiber = nil
    end
    if new_mode == 'off' then
        return
    end
    if new_mode == 'once' and
       router.total_bucket_count == router.known_bucket_count then
        -- 'Once' discovery is supposed to stop working when all
        -- is found. But it is the case already. So nothing to do.
        return
    end
    router.discovery_fiber = util.reloadable_fiber_create(
        'vshard.discovery.' .. router.name, M, 'discovery_f', router)
end

--------------------------------------------------------------------------------
-- API
--------------------------------------------------------------------------------

local function vshard_future_tostring(self)
    return 'vshard.net.box.request'
end

local function vshard_future_serialize(self)
    -- Drop the metatable. It is also copied and if returned as is leads to
    -- recursive serialization.
    local s = setmetatable(table.deepcopy(self), {})
    s._base = nil
    return s
end

local function vshard_future_is_ready(self)
    return self._base:is_ready()
end

local function vshard_future_wrap_result(res)
    local storage_ok, res, err = res[1], res[2], res[3]
    if storage_ok then
        if res == nil and err ~= nil then
            return nil, lerror.make(err)
        end
        return setmetatable({res}, seq_serializer)
    end
    return nil, lerror.make(res)
end

local function vshard_future_result(self)
    local res, err = self._base:result()
    if res == nil then
        return nil, lerror.make(err)
    end
    return vshard_future_wrap_result(res)
end

local function vshard_future_wait_result(self, timeout)
    local res, err = self._base:wait_result(timeout)
    if res == nil then
        return nil, lerror.make(err)
    end
    return vshard_future_wrap_result(res)
end

local function vshard_future_discard(self)
    return self._base:discard()
end

local function vshard_future_iter_next(iter, i)
    local res, err
    local base_next = iter.base_next
    local base_req = iter.base_req
    local base = iter.base
    -- Need to distinguish the last response from the pushes. Because the former
    -- has metadata returned by vshard.storage.call().
    -- At the same time there is no way to check if the base pairs() did its
    -- last iteration except calling its next() function again.
    -- This, in turn, might lead to a block if the result is not ready yet.
    i, res = base_next(base, i)
    -- To avoid that there is a 2-phase check.
    -- If the request isn't finished after first next(), it means the result is
    -- not received. This is a push. Return as is.
    -- If the request is finished, it is safe to call next() again to check if
    -- it ended. It won't block.
    local is_done = base_req:is_ready()

    if not is_done then
        -- Definitely a push. It would be finished if the final result was
        -- received.
        if i == nil then
            return nil, lerror.make(res)
        end
        return i, res
    end
    if i == nil then
        if res ~= nil then
            return i, lerror.make(res)
        end
        return nil, nil
    end
    -- Will not block because the request is already finished.
    if base_next(base, i) == nil then
        res, err = vshard_future_wrap_result(res)
        if res ~= nil then
            return i, res
        end
        return i, {nil, lerror.make(err)}
    end
    return i, res
end

local function vshard_future_pairs(self, timeout)
    local next_f, iter, i = self._base:pairs(timeout)
    return vshard_future_iter_next,
           {base = iter, base_req = self, base_next = next_f}, i
end

local vshard_future_mt = {
    __tostring = vshard_future_tostring,
    __serialize = vshard_future_serialize,
    __index = {
        is_ready = vshard_future_is_ready,
        result = vshard_future_result,
        wait_result = vshard_future_wait_result,
        discard = vshard_future_discard,
        pairs = vshard_future_pairs,
    }
}

--
-- Since 1.10 netbox supports flag 'is_async'. Given this flag, a
-- request result is returned immediately in a form of a future
-- object. Future of CALL request returns a result wrapped into an
-- array instead of unpacked values because unpacked values can
-- not be stored anywhere.
--
-- Vshard.router.call calls a user function not directly, but via
-- vshard.storage.call which returns true/false, result, errors.
-- So vshard.router.call should wrap a future object with its own
-- unpacker of result.
--
local function vshard_future_new(future)
    -- Use '_' as a prefix so as users could use all normal names.
    return setmetatable({_base = future}, vshard_future_mt)
end

-- Perform shard operation
-- Function will restart operation after wrong bucket response until timeout
-- is reached
--
local function router_call_impl(router, bucket_id, mode, prefer_replica,
                                balance, func, args, opts)
    if opts then
        if type(opts) ~= 'table' or
           (opts.timeout and type(opts.timeout) ~= 'number') then
            error('Usage: call(bucket_id, mode, func, args, opts)')
        end
        opts = table.copy(opts)
    elseif not opts then
        opts = {}
    end
    local timeout = opts.timeout or consts.CALL_TIMEOUT_MIN
    local replicaset, err
    local tend = fiber_clock() + timeout
    if bucket_id > router.total_bucket_count or bucket_id <= 0 then
        error('Bucket is unreachable: bucket id is out of range')
    end
    local call
    if mode == 'read' then
        if prefer_replica then
            if balance then
                call = 'callbre'
            else
                call = 'callre'
            end
        elseif balance then
            call = 'callbro'
        else
            call = 'callro'
        end
    else
        call = 'callrw'
    end
    repeat
        replicaset, err = bucket_resolve(router, bucket_id)
        if replicaset then
::replicaset_is_found::
            opts.timeout = tend - fiber_clock()
            local storage_call_status, call_status, call_error =
                replicaset[call](replicaset, 'vshard.storage.call',
                                 {bucket_id, mode, func, args}, opts)
            if storage_call_status then
                if call_status == nil and call_error ~= nil then
                    return call_status, call_error
                elseif not opts.is_async then
                    return call_status
                else
                    -- Vshard.storage.call(func) returns two
                    -- values: true/false and func result. But
                    -- async returns future object. No true/false
                    -- nor func result. So return the first value.
                    return vshard_future_new(storage_call_status)
                end
            end
            err = lerror.make(call_status)
            if err.code == lerror.code.WRONG_BUCKET or
               err.code == lerror.code.BUCKET_IS_LOCKED then
                bucket_reset(router, bucket_id)
                if err.destination then
                    replicaset = router.replicasets[err.destination]
                    if not replicaset then
                        log.warn('Replicaset "%s" was not found, but received'..
                                 ' from storage as destination - please '..
                                 'update configuration', err.destination)
                        -- Try to wait until the destination
                        -- appears. A destination can disappear,
                        -- if reconfiguration had been started,
                        -- and while is not executed on router,
                        -- but already is executed on storages.
                        while fiber_clock() <= tend do
                            lfiber.sleep(0.05)
                            replicaset = router.replicasets[err.destination]
                            if replicaset then
                                goto replicaset_is_found
                            end
                        end
                    else
                        replicaset = bucket_set(router, bucket_id,
                                                replicaset.uuid)
                        lfiber.yield()
                        -- Protect against infinite cycle in a
                        -- case of broken cluster, when a bucket
                        -- is sent on two replicasets to each
                        -- other.
                        if replicaset and fiber_clock() <= tend then
                            goto replicaset_is_found
                        end
                    end
                    return nil, err
                end
            elseif err.code == lerror.code.TRANSFER_IS_IN_PROGRESS then
                -- Do not repeat write requests, even if an error
                -- is not timeout - these requests are repeated in
                -- any case on client, if error.
                assert(mode == 'write')
                bucket_reset(router, bucket_id)
                return nil, err
            elseif err.code == lerror.code.NON_MASTER then
                assert(mode == 'write')
                if not replicaset:update_master(err.replica_uuid,
                                                err.master_uuid) then
                    return nil, err
                end
            else
                return nil, err
            end
        end
        lfiber.yield()
    until fiber_clock() > tend
    if err then
        return nil, err
    else
        return nil, lerror.timeout()
    end
end

--
-- Wrappers for router_call with preset mode.
--
local function router_callro(router, bucket_id, ...)
    return router_call_impl(router, bucket_id, 'read', false, false, ...)
end

local function router_callbro(router, bucket_id, ...)
    return router_call_impl(router, bucket_id, 'read', false, true, ...)
end

local function router_callrw(router, bucket_id, ...)
    return router_call_impl(router, bucket_id, 'write', false, false, ...)
end

local function router_callre(router, bucket_id, ...)
    return router_call_impl(router, bucket_id, 'read', true, false, ...)
end

local function router_callbre(router, bucket_id, ...)
    return router_call_impl(router, bucket_id, 'read', true, true, ...)
end

local function router_call(router, bucket_id, opts, ...)
    local mode, prefer_replica, balance
    if opts then
        if type(opts) == 'string' then
            mode = opts
        elseif type(opts) == 'table' then
            mode = opts.mode or 'write'
            prefer_replica = opts.prefer_replica
            balance = opts.balance
        else
            error('Usage: router.call(bucket_id, shard_opts, func, args, opts)')
        end
    else
        mode = 'write'
    end
    return router_call_impl(router, bucket_id, mode, prefer_replica, balance,
                            ...)
end

local router_map_callrw

if util.version_is_at_least(1, 10, 0) then
--
-- Consistent Map-Reduce. The given function is called on all masters in the
-- cluster with a guarantee that in case of success it was executed with all
-- buckets being accessible for reads and writes.
--
-- Consistency in scope of map-reduce means all the data was accessible, and
-- didn't move during map requests execution. To preserve the consistency there
-- is a third stage - Ref. So the algorithm is actually Ref-Map-Reduce.
--
-- Refs are broadcast before Map stage to pin the buckets to their storages, and
-- ensure they won't move until maps are done.
--
-- Map requests are broadcast in case all refs are done successfully. They
-- execute the user function + delete the refs to enable rebalancing again.
--
-- On the storages there are additional means to ensure map-reduces don't block
-- rebalancing forever and vice versa.
--
-- The function is not as slow as it may seem - it uses netbox's feature
-- is_async to send refs and maps in parallel. So cost of the function is about
-- 2 network exchanges to the most far storage in terms of time.
--
-- @param router Router instance to use.
-- @param func Name of the function to call.
-- @param args Function arguments passed in netbox style (as an array).
-- @param opts Can only contain 'timeout' as a number of seconds. Note that the
--     refs may end up being kept on the storages during this entire timeout if
--     something goes wrong. For instance, network issues appear. This means
--     better not use a value bigger than necessary. A stuck infinite ref can
--     only be dropped by this router restart/reconnect or the storage restart.
--
-- @return In case of success - a map with replicaset UUID keys and values being
--     what the function returned from the replicaset.
--
-- @return In case of an error - nil, error object, optional UUID of the
--     replicaset where the error happened. UUID may be not present if it wasn't
--     about concrete replicaset. For example, not all buckets were found even
--     though all replicasets were scanned.
--
router_map_callrw = function(router, func, args, opts)
    local replicasets = router.replicasets
    local timeout = opts and opts.timeout or consts.CALL_TIMEOUT_MIN
    local deadline = fiber_clock() + timeout
    local err, err_uuid, res, ok, map
    local futures = {}
    local bucket_count = 0
    local opts_async = {is_async = true}
    local rs_count = 0
    local rid = M.ref_id
    M.ref_id = rid + 1
    -- Nil checks are done explicitly here (== nil instead of 'not'), because
    -- netbox requests return box.NULL instead of nils.

    --
    -- Ref stage: send.
    --
    for uuid, rs in pairs(replicasets) do
        -- Netbox async requests work only with active connections. Need to wait
        -- for the connection explicitly.
        timeout, err = rs:wait_connected(timeout)
        if timeout == nil then
            err_uuid = uuid
            goto fail
        end
        res, err = rs:callrw('vshard.storage._call',
                              {'storage_ref', rid, timeout}, opts_async)
        if res == nil then
            err_uuid = uuid
            goto fail
        end
        futures[uuid] = res
        rs_count = rs_count + 1
    end
    map = table_new(0, rs_count)
    --
    -- Ref stage: collect.
    --
    for uuid, future in pairs(futures) do
        res, err = future:wait_result(timeout)
        -- Handle netbox error first.
        if res == nil then
            err_uuid = uuid
            goto fail
        end
        -- Ref returns nil,err or bucket count.
        res, err = res[1], res[2]
        if res == nil then
            err_uuid = uuid
            goto fail
        end
        bucket_count = bucket_count + res
        timeout = deadline - fiber_clock()
    end
    -- All refs are done but not all buckets are covered. This is odd and can
    -- mean many things. The most possible ones: 1) outdated configuration on
    -- the router and it does not see another replicaset with more buckets,
    -- 2) some buckets are simply lost or duplicated - could happen as a bug, or
    -- if the user does a maintenance of some kind by creating/deleting buckets.
    -- In both cases can't guarantee all the data would be covered by Map calls.
    if bucket_count ~= router.total_bucket_count then
        err = lerror.vshard(lerror.code.UNKNOWN_BUCKETS,
                            router.total_bucket_count - bucket_count)
        goto fail
    end
    --
    -- Map stage: send.
    --
    args = {'storage_map', rid, func, args}
    for uuid, rs in pairs(replicasets) do
        res, err = rs:callrw('vshard.storage._call', args, opts_async)
        if res == nil then
            err_uuid = uuid
            goto fail
        end
        futures[uuid] = res
    end
    --
    -- Ref stage: collect.
    --
    for uuid, f in pairs(futures) do
        res, err = f:wait_result(timeout)
        if res == nil then
            err_uuid = uuid
            goto fail
        end
        -- Map returns true,res or nil,err.
        ok, res = res[1], res[2]
        if ok == nil then
            err = res
            err_uuid = uuid
            goto fail
        end
        if res ~= nil then
            -- Store as a table so in future it could be extended for
            -- multireturn.
            map[uuid] = {res}
        end
        timeout = deadline - fiber_clock()
    end
    do return map end

::fail::
    for uuid, f in pairs(futures) do
        f:discard()
        -- Best effort to remove the created refs before exiting. Can help if
        -- the timeout was big and the error happened early.
        f = replicasets[uuid]:callrw('vshard.storage._call',
                                     {'storage_unref', rid}, opts_async)
        if f ~= nil then
            -- Don't care waiting for a result - no time for this. But it won't
            -- affect the request sending if the connection is still alive.
            f:discard()
        end
    end
    err = lerror.make(err)
    return nil, err, err_uuid
end

-- Version >= 1.10.
else
-- Version < 1.10.

router_map_callrw = function()
    error('Supported for Tarantool >= 1.10')
end

end

--
-- Get replicaset object by bucket identifier.
-- @param bucket_id Bucket identifier.
-- @retval Netbox connection.
--
local function router_route(router, bucket_id)
    if type(bucket_id) ~= 'number' then
        error('Usage: router.route(bucket_id)')
    end
    return bucket_resolve(router, bucket_id)
end

--
-- Return map of all replicasets.
-- @retval See self.replicasets map.
--
local function router_routeall(router)
    return router.replicasets
end

--------------------------------------------------------------------------------
-- Failover
--------------------------------------------------------------------------------

local function failover_ping_round(router)
    for _, replicaset in pairs(router.replicasets) do
        local replica = replicaset.replica
        if replica ~= nil and replica.conn ~= nil and
           replica.down_ts == nil then
            if not replica.conn:ping({timeout =
                                      router.failover_ping_timeout}) then
                log.info('Ping error from %s: perhaps a connection is down',
                         replica)
                -- Connection hangs. Recreate it to be able to
                -- fail over to a replica next by priority. The
                -- old connection is not closed in case if it just
                -- processes too big response at this moment. Any
                -- way it will be eventually garbage collected
                -- and closed.
                replica:detach_conn()
                replicaset:connect_replica(replica)
            end
        end
    end
end

--
-- Replicaset must fall its replica connection to lower priority,
-- if the current one is down too long.
--
local function failover_need_down_priority(replicaset, curr_ts)
    local r = replicaset.replica
    -- down_ts not nil does not mean that the replica is not
    -- connected. Probably it is connected and now fetches schema,
    -- or does authorization. Either case, it is healthy, no need
    -- to down the prio.
    return r and r.down_ts and not r:is_connected() and
           curr_ts - r.down_ts >= consts.FAILOVER_DOWN_TIMEOUT
           and r.next_by_priority
end

--
-- Once per FAILOVER_UP_TIMEOUT a replicaset must try to connect
-- to a replica with a higher priority.
--
local function failover_need_up_priority(replicaset, curr_ts)
    local up_ts = replicaset.replica_up_ts
    return not up_ts or curr_ts - up_ts >= consts.FAILOVER_UP_TIMEOUT
end

--
-- Collect UUIDs of replicasets, priority of whose replica
-- connections must be updated.
--
local function failover_collect_to_update(router)
    local ts = fiber_clock()
    local uuid_to_update = {}
    for uuid, rs in pairs(router.replicasets) do
        if failover_need_down_priority(rs, ts) or
           failover_need_up_priority(rs, ts) then
            table.insert(uuid_to_update, uuid)
        end
    end
    return uuid_to_update
end

--
-- Detect not optimal or disconnected replicas. For not optimal
-- try to update them to optimal, and down priority of
-- disconnected replicas.
-- @retval true A replica of an replicaset has been changed.
--
local function failover_step(router)
    failover_ping_round(router)
    local uuid_to_update = failover_collect_to_update(router)
    if #uuid_to_update == 0 then
        return false
    end
    local curr_ts = fiber_clock()
    local replica_is_changed = false
    for _, uuid in pairs(uuid_to_update) do
        local rs = router.replicasets[uuid]
        if M.errinj.ERRINJ_FAILOVER_CHANGE_CFG then
            rs = nil
            M.errinj.ERRINJ_FAILOVER_CHANGE_CFG = false
        end
        if rs == nil then
            log.info('Configuration has changed, restart failovering')
            lfiber.yield()
            return true
        end
        if not next(rs.replicas) then
            goto continue
        end
        local old_replica = rs.replica
        if failover_need_up_priority(rs, curr_ts) then
            rs:up_replica_priority()
        end
        if failover_need_down_priority(rs, curr_ts) then
            rs:down_replica_priority()
        end
        if old_replica ~= rs.replica then
            log.info('New replica %s for %s', rs.replica, rs)
            replica_is_changed = true
        end
::continue::
    end
    return replica_is_changed
end

--
-- Failover background function. Replica connection is the
-- connection to the nearest available server. Replica connection
-- is hold for each replicaset. This function periodically scans
-- replicasets and their replica connections. And some of them
-- appear to be disconnected or connected not to optimal replica.
--
-- If a connection is disconnected too long (more than
-- FAILOVER_DOWN_TIMEOUT), this function tries to connect to the
-- server with the lower priority. Priorities are specified in
-- weight matrix in config.
--
-- If a current replica connection has no the highest priority,
-- then this function periodically (once per FAILOVER_UP_TIMEOUT)
-- tries to reconnect to the best replica. When the connection is
-- established, it replaces the original replica.
--
local function failover_f(router)
    local module_version = M.module_version
    local min_timeout = math.min(consts.FAILOVER_UP_TIMEOUT,
                                 consts.FAILOVER_DOWN_TIMEOUT)
    -- This flag is used to avoid logging like:
    -- 'All is ok ... All is ok ... All is ok ...'
    -- each min_timeout seconds.
    local prev_was_ok = false
    while module_version == M.module_version do
::continue::
        local ok, replica_is_changed = pcall(failover_step, router)
        if not ok then
            log.error('Error during failovering: %s',
                      lerror.make(replica_is_changed))
            replica_is_changed = true
        elseif not prev_was_ok then
            log.info('All replicas are ok')
        end
        prev_was_ok = not replica_is_changed
        local logf
        if replica_is_changed then
            logf = log.info
        else
            -- In any case it is necessary to periodically log
            -- failover heartbeat.
            logf = log.verbose
        end
        logf('Failovering step is finished. Schedule next after %f seconds',
             min_timeout)
        lfiber.sleep(min_timeout)
    end
end

--------------------------------------------------------------------------------
-- Master search
--------------------------------------------------------------------------------

local function master_search_step(router)
    local ok, is_done, is_nop, err = pcall(lreplicaset.locate_masters,
                                           router.replicasets)
    if not ok then
        err = is_done
        is_done = false
        is_nop = false
    end
    return is_done, is_nop, err
end

--
-- Master discovery background function. It is supposed to notice master changes
-- and find new masters in the replicasets, which are configured for that.
--
-- XXX: due to polling the search might notice master change not right when it
-- happens. In future it makes sense to rewrite master search using
-- subscriptions. The problem is that at the moment of writing the subscriptions
-- are not working well in all Tarantool versions.
--
local function master_search_f(router)
    local module_version = M.module_version
    local is_in_progress = false
    local errinj = M.errinj
    while module_version == M.module_version do
        if errinj.ERRINJ_MASTER_SEARCH_DELAY then
            errinj.ERRINJ_MASTER_SEARCH_DELAY = 'in'
            repeat
                lfiber.sleep(0.001)
            until not errinj.ERRINJ_MASTER_SEARCH_DELAY
        end
        local timeout
        local start_time = fiber_clock()
        local is_done, is_nop, err = master_search_step(router)
        if err then
            log.error('Error during master search: %s', lerror.make(err))
        end
        if is_done then
            timeout = consts.MASTER_SEARCH_IDLE_INTERVAL
        elseif err then
            timeout = consts.MASTER_SEARCH_BACKOFF_INTERVAL
        else
            timeout = consts.MASTER_SEARCH_WORK_INTERVAL
        end
        if not is_in_progress then
            if not is_nop and is_done then
                log.info('Master search happened')
            elseif not is_done then
                log.info('Master search is started')
                is_in_progress = true
            end
        elseif is_done then
            log.info('Master search is finished')
            is_in_progress = false
        end
        local end_time = fiber_clock()
        local duration = end_time - start_time
        if not is_nop then
            log.verbose('Master search step took %s seconds. Next in %s '..
                        'seconds', duration, timeout)
        end
        lfiber.sleep(timeout)
    end
end

local function master_search_set(router)
    local enable = false
    for _, rs in pairs(router.replicasets) do
        if rs.is_auto_master then
            enable = true
            break
        end
    end
    local search_fiber = router.master_search_fiber
    if enable and search_fiber == nil then
        log.info('Master auto search is enabled')
        router.master_search_fiber = util.reloadable_fiber_create(
            'vshard.master_search.' .. router.name, M, 'master_search_f',
            router)
    elseif not enable and search_fiber ~= nil then
        -- Do not make users pay for what they do not use - when the search is
        -- disabled for all replicasets, there should not be any fiber.
        log.info('Master auto search is disabled')
        if search_fiber:status() ~= 'dead' then
            search_fiber:cancel()
        end
        router.master_search_fiber = nil
    end
end

local function master_search_wakeup(router)
    local f = router.master_search_fiber
    if f then
        f:wakeup()
    end
end

--------------------------------------------------------------------------------
-- Configuration
--------------------------------------------------------------------------------

local function router_cfg(router, cfg, is_reload)
    cfg = lcfg.check(cfg, router.current_cfg)
    local vshard_cfg, box_cfg = lcfg.split(cfg)
    if not M.replicasets then
        log.info('Starting router configuration')
    else
        log.info('Starting router reconfiguration')
    end
    local new_replicasets = lreplicaset.buildall(vshard_cfg)
    log.info("Calling box.cfg()...")
    for k, v in pairs(box_cfg) do
        log.info({[k] = v})
    end
    -- It is considered that all possible errors during cfg
    -- process occur only before this place.
    -- This check should be placed as late as possible.
    if M.errinj.ERRINJ_CFG then
        error('Error injection: cfg')
    end
    if not is_reload then
        box.cfg(box_cfg)
        log.info("Box has been configured")
    end
    -- Move connections from an old configuration to a new one.
    -- It must be done with no yields to prevent usage both of not
    -- fully moved old replicasets, and not fully built new ones.
    lreplicaset.rebind_replicasets(new_replicasets, router.replicasets)
    -- Now the new replicasets are fully built. Can establish
    -- connections and yield.
    for _, replicaset in pairs(new_replicasets) do
        replicaset:connect_all()
    end
    -- Change state of lua GC.
    if vshard_cfg.collect_lua_garbage and not router.collect_lua_garbage then
        lua_gc_cnt_inc()
    elseif not vshard_cfg.collect_lua_garbage and
       router.collect_lua_garbage then
        lua_gc_cnt_dec()
    end
    lreplicaset.wait_masters_connect(new_replicasets)
    lreplicaset.outdate_replicasets(router.replicasets,
                                    vshard_cfg.connection_outdate_delay)
    router.connection_outdate_delay = vshard_cfg.connection_outdate_delay
    router.total_bucket_count = vshard_cfg.bucket_count
    router.collect_lua_garbage = vshard_cfg.collect_lua_garbage
    router.current_cfg = cfg
    router.replicasets = new_replicasets
    router.failover_ping_timeout = vshard_cfg.failover_ping_timeout
    router.sync_timeout = vshard_cfg.sync_timeout
    local old_route_map = router.route_map
    local known_bucket_count = 0
    router.route_map = table_new(router.total_bucket_count, 0)
    for bucket, rs in pairs(old_route_map) do
        local new_rs = router.replicasets[rs.uuid]
        if new_rs then
            router.route_map[bucket] = new_rs
            new_rs.bucket_count = new_rs.bucket_count + 1
            known_bucket_count = known_bucket_count + 1
        end
    end
    router.known_bucket_count = known_bucket_count
    if router.failover_fiber == nil then
        router.failover_fiber = util.reloadable_fiber_create(
            'vshard.failover.' .. router.name, M, 'failover_f', router)
    end
    discovery_set(router, vshard_cfg.discovery_mode)
    master_search_set(router)
end

--------------------------------------------------------------------------------
-- Bootstrap
--------------------------------------------------------------------------------

local function cluster_bootstrap(router, opts)
    local replicasets = {}
    local count, err, last_err, ok, if_not_bootstrapped
    if opts then
        if type(opts) ~= 'table' then
            return error('Usage: vshard.router.bootstrap({<options>})')
        end
        if_not_bootstrapped = opts.if_not_bootstrapped
        opts = {timeout = opts.timeout}
        if if_not_bootstrapped == nil then
            if_not_bootstrapped = false
        end
    else
        if_not_bootstrapped = false
    end

    for uuid, replicaset in pairs(router.replicasets) do
        table.insert(replicasets, replicaset)
        count, err = replicaset:callrw('vshard.storage.buckets_count', {}, opts)
        if count == nil then
            -- If the client considers a bootstrapped cluster ok,
            -- then even one count > 0 is enough. So don't stop
            -- attempts after a first error. Return an error only
            -- if all replicasets responded with an error.
            if if_not_bootstrapped then
                last_err = err
            else
                return nil, err
            end
        elseif count > 0 then
            if if_not_bootstrapped then
                return true
            end
            return nil, lerror.vshard(lerror.code.NON_EMPTY)
        end
    end
    if last_err then
        return nil, err
    end
    lreplicaset.calculate_etalon_balance(router.replicasets,
                                         router.total_bucket_count)
    local bucket_id = 1
    for uuid, replicaset in pairs(router.replicasets) do
        if replicaset.etalon_bucket_count > 0 then
            ok, err =
                replicaset:callrw('vshard.storage.bucket_force_create',
                                  {bucket_id, replicaset.etalon_bucket_count},
                                  opts)
            if not ok then
                return nil, err
            end
            local next_bucket_id = bucket_id + replicaset.etalon_bucket_count
            log.info('Buckets from %d to %d are bootstrapped on "%s"',
                     bucket_id, next_bucket_id - 1, uuid)
            bucket_id = next_bucket_id
        end
    end
    return true
end

--------------------------------------------------------------------------------
-- Monitoring
--------------------------------------------------------------------------------

--
-- Collect info about a replicaset's replica with a specified
-- name. Found alerts are appended to @an alerts table, if a
-- replica does not exist or is unavailable. In a case of error
-- @a errcolor is returned, and GREEN else.
--
local function replicaset_instance_info(replicaset, name, alerts, errcolor,
                                        errcode_unreachable, params1,
                                        errcode_missing, params2)
    local info = {}
    local replica = replicaset[name]
    if replica then
        info.uri = replica:safe_uri()
        info.uuid = replica.uuid
        info.network_timeout = replica.net_timeout
        if replica:is_connected() then
            info.status = 'available'
        else
            info.status = 'unreachable'
            if errcode_unreachable then
                table.insert(alerts, lerror.alert(errcode_unreachable,
                                                  unpack(params1)))
                return info, errcolor
            end
        end
    else
        info.status = 'missing'
        if errcode_missing then
            table.insert(alerts, lerror.alert(errcode_missing, unpack(params2)))
            return info, errcolor
        end
    end
    return info, consts.STATUS.GREEN
end

local function router_info(router)
    local state = {
        replicasets = {},
        bucket = {
            available_ro = 0,
            available_rw = 0,
            unreachable = 0,
            unknown = 0,
        },
        alerts = {},
        status = consts.STATUS.GREEN,
    }
    local bucket_info = state.bucket
    for rs_uuid, replicaset in pairs(router.replicasets) do
        -- Replicaset info parameters:
        -- * master instance info;
        -- * replica instance info;
        -- * replicaset uuid.
        --
        -- Instance info parameters:
        -- * uri;
        -- * uuid;
        -- * status - available, unreachable, missing;
        -- * network_timeout - timeout for requests, updated on
        --   each 10 success and 2 failed requests. The greater
        --   timeout, the worse network feels itself.
        local rs_info = {
            uuid = replicaset.uuid,
            bucket = {}
        }
        state.replicasets[replicaset.uuid] = rs_info

        -- Build master info.
        local info, color =
            replicaset_instance_info(replicaset, 'master', state.alerts,
                                     consts.STATUS.ORANGE,
                                     -- Master exists, but not
                                     -- available.
                                     lerror.code.UNREACHABLE_MASTER,
                                     {replicaset.uuid, 'disconnected'},
                                     -- Master does not exists.
                                     lerror.code.MISSING_MASTER,
                                     {replicaset.uuid})
        state.status = math.max(state.status, color)
        rs_info.master = info

        -- Build replica info.
        if replicaset.replica ~= replicaset.master then
            info = replicaset_instance_info(replicaset, 'replica', state.alerts)
        end
        rs_info.replica = info
        if not replicaset.replica or
           (replicaset.replica and
            replicaset.replica ~= replicaset.priority_list[1]) then
            -- If the replica is not optimal, then some replicas
            -- possibly are down.
            local a = lerror.alert(lerror.code.SUBOPTIMAL_REPLICA,
                                   replicaset.uuid)
            table.insert(state.alerts, a)
            state.status = math.max(state.status, consts.STATUS.YELLOW)
        end

        if rs_info.replica.status ~= 'available' and
           rs_info.master.status ~= 'available' then
            local a = lerror.alert(lerror.code.UNREACHABLE_REPLICASET,
                                   replicaset.uuid)
            table.insert(state.alerts, a)
            state.status = consts.STATUS.RED
        end

        -- Bucket info consists of three parameters:
        -- * available_ro: how many buckets are known and
        --                 available for read requests;
        -- * available_rw: how many buckets are known and
        --                 available for both read and write
        --                 requests;
        -- * unreachable: how many buckets are known, but are not
        --                available for any requests;
        -- * unknown: how many buckets are unknown - a router
        --            doesn't know their replicasets.
        if rs_info.master.status ~= 'available' then
            if rs_info.replica.status ~= 'available' then
                rs_info.bucket.unreachable = replicaset.bucket_count
                bucket_info.unreachable = bucket_info.unreachable +
                                          replicaset.bucket_count
            else
                rs_info.bucket.available_ro = replicaset.bucket_count
                bucket_info.available_ro = bucket_info.available_ro +
                                           replicaset.bucket_count
            end
        else
            rs_info.bucket.available_rw = replicaset.bucket_count
            bucket_info.available_rw = bucket_info.available_rw +
                                       replicaset.bucket_count
        end
        -- Not necessary to update the color - it is done above
        -- during replicaset master and replica checking.
        -- If a bucket is unreachable, then replicaset is
        -- unreachable too and color already is red.
    end
    bucket_info.unknown = router.total_bucket_count - router.known_bucket_count
    if bucket_info.unknown > 0 then
        state.status = math.max(state.status, consts.STATUS.YELLOW)
        table.insert(state.alerts, lerror.alert(lerror.code.UNKNOWN_BUCKETS,
                                                bucket_info.unknown))
    elseif bucket_info.unknown < 0 then
        state.status = consts.STATUS.RED
        local msg = "probably router's cfg.bucket_count is different from "..
                    "storages' one, difference is "..(0 - bucket_info.unknown)
        bucket_info.unknown = '???'
        table.insert(state.alerts, lerror.alert(lerror.code.INVALID_CFG, msg))
    end
    return state
end

--
-- Build info about each bucket. Since a bucket map can be huge,
-- the function provides API to get not entire bucket map, but a
-- part.
-- @param offset Offset in a bucket map to select from.
-- @param limit Maximal bucket count in output.
-- @retval Map of type {bucket_id = 'unknown'/replicaset_uuid}.
--
local function router_buckets_info(router, offset, limit)
    if offset ~= nil and type(offset) ~= 'number' or
       limit ~= nil and type(limit) ~= 'number' then
        error('Usage: buckets_info(offset, limit)')
    end
    offset = offset or 0
    limit = limit or router.total_bucket_count
    local ret = {}
    -- Use one string memory for all unknown buckets.
    local available_rw = 'available_rw'
    local available_ro = 'available_ro'
    local unknown = 'unknown'
    local unreachable = 'unreachable'
    -- Collect limit.
    local first = math.max(1, offset + 1)
    local last = math.min(offset + limit, router.total_bucket_count)
    for bucket_id = first, last do
        local rs = router.route_map[bucket_id]
        if rs then
            if rs.master and rs.master:is_connected() then
                ret[bucket_id] = {uuid = rs.uuid, status = available_rw}
            elseif rs.replica and rs.replica:is_connected() then
                ret[bucket_id] = {uuid = rs.uuid, status = available_ro}
            else
                ret[bucket_id] = {uuid = rs.uuid, status = unreachable}
            end
        else
            ret[bucket_id] = {status = unknown}
        end
    end
    return ret
end

--------------------------------------------------------------------------------
-- Other
--------------------------------------------------------------------------------

local router_bucket_id_deprecated_warn = true
local function router_bucket_id(router, key)
    if key == nil then
        error("Usage: vshard.router.bucket_id(key)")
    end
    if router_bucket_id_deprecated_warn then
        router_bucket_id_deprecated_warn = false
        log.warn('vshard.router.bucket_id() is deprecated, use '..
                 'vshard.router.bucket_id_strcrc32() or '..
                 'vshard.router.bucket_id_mpcrc32()')
    end
    return lhash.strcrc32(key) % router.total_bucket_count + 1
end

local function router_bucket_id_strcrc32(router, key)
    if key == nil then
        error("Usage: vshard.router.bucket_id_strcrc32(key)")
    end
    return lhash.strcrc32(key) % router.total_bucket_count + 1
end

local function router_bucket_id_mpcrc32(router, key)
    if key == nil then
        error("Usage: vshard.router.bucket_id_mpcrc32(key)")
    end
    return lhash.mpcrc32(key) % router.total_bucket_count + 1
end

local function router_bucket_count(router)
    return router.total_bucket_count
end

local function router_sync(router, timeout)
    if timeout ~= nil then
        if type(timeout) ~= 'number' then
            error('Usage: vshard.router.sync([timeout: number])')
        end
    else
        timeout = router.sync_timeout
    end
    local arg = {timeout}
    local deadline = timeout and (fiber_clock() + timeout)
    local opts = {timeout = timeout}
    for rs_uuid, replicaset in pairs(router.replicasets) do
        if timeout < 0 then
            return nil, lerror.timeout()
        end
        local status, err = replicaset:callrw('vshard.storage.sync', arg, opts)
        if not status then
            -- Add information about replicaset
            err.replicaset = rs_uuid
            return nil, err
        end
        timeout = deadline - fiber_clock()
        arg[1] = timeout
        opts.timeout = timeout
    end
    return true
end

if M.errinj.ERRINJ_RELOAD then
    error('Error injection: reload')
end

--------------------------------------------------------------------------------
-- Managing router instances
--------------------------------------------------------------------------------

local router_mt = {
    __index = {
        cfg = function(router, cfg) return router_cfg(router, cfg, false) end,
        info = router_info;
        buckets_info = router_buckets_info;
        call = router_call;
        callro = router_callro;
        callbro = router_callbro;
        callrw = router_callrw;
        callre = router_callre;
        callbre = router_callbre;
        map_callrw = router_map_callrw,
        route = router_route;
        routeall = router_routeall;
        bucket_id = router_bucket_id,
        bucket_id_strcrc32 = router_bucket_id_strcrc32,
        bucket_id_mpcrc32 = router_bucket_id_mpcrc32,
        bucket_count = router_bucket_count;
        sync = router_sync;
        bootstrap = cluster_bootstrap;
        bucket_discovery = bucket_discovery;
        discovery_wakeup = discovery_wakeup;
        master_search_wakeup = master_search_wakeup,
        discovery_set = discovery_set,
        _route_map_clear = route_map_clear,
        _bucket_reset = bucket_reset,
    }
}

-- Table which represents this module.
local module = {}

-- This metatable bypasses calls to a module to the static_router.
local module_mt = {__index = {}}
for method_name, method in pairs(router_mt.__index) do
    module_mt.__index[method_name] = function(...)
        return method(M.static_router, ...)
    end
end

--
-- Wrap self methods with a sanity checker.
--
local mt_index = {}
for name, func in pairs(router_mt.__index) do
    mt_index[name] = util.generate_self_checker("router", name, router_mt, func)
end
router_mt.__index = mt_index

local function export_static_router_attributes()
    setmetatable(module, module_mt)
end

--
-- Create a new instance of router.
-- @param name Name of a new router.
-- @param cfg Configuration for `router_cfg`.
-- @retval Router instance.
-- @retval Nil and error object.
--
local function router_new(name, cfg)
    if type(name) ~= 'string' or type(cfg) ~= 'table' then
           error('Wrong argument type. Usage: vshard.router.new(name, cfg).')
    end
    if M.routers[name] then
        return nil, lerror.vshard(lerror.code.ROUTER_ALREADY_EXISTS, name)
    end
    local router = table.deepcopy(ROUTER_TEMPLATE)
    setmetatable(router, router_mt)
    router.name = name
    M.routers[name] = router
    local ok, err = pcall(router_cfg, router, cfg)
    if not ok then
        M.routers[name] = nil
        error(err)
    end
    return router
end

--
-- Wrapper around a `router_new` API, which allow to use old
-- static `vshard.router.cfg()` API.
--
local function legacy_cfg(cfg)
    if M.static_router then
        -- Reconfigure.
        router_cfg(M.static_router, cfg, false)
    else
        -- Create new static instance.
        local router, err = router_new(STATIC_ROUTER_NAME, cfg)
        if router then
            M.static_router = router
            module_mt.__index.static = router
            export_static_router_attributes()
        else
            return nil, err
        end
    end
end

--------------------------------------------------------------------------------
-- Module definition
--------------------------------------------------------------------------------
M.discovery_f = discovery_f
M.failover_f = failover_f
M.master_search_f = master_search_f
M.router_mt = router_mt
--
-- About functions, saved in M, and reloading see comment in
-- storage/init.lua.
--
if not rawget(_G, MODULE_INTERNALS) then
    rawset(_G, MODULE_INTERNALS, M)
else
    if not M.ref_id then
        M.ref_id = 0
    end
    for _, router in pairs(M.routers) do
        router_cfg(router, router.current_cfg, true)
        setmetatable(router, router_mt)
    end
    if M.static_router then
        module_mt.__index.static = M.static_router
        export_static_router_attributes()
    end
    M.module_version = M.module_version + 1
end

module.cfg = legacy_cfg
module.new = router_new
module.internal = M
module.module_version = function() return M.module_version end

return module
end
end

do
local _ENV = _ENV
package.preload[ "vshard.storage" ] = function( ... ) local arg = _G.arg;
local log = require('log')
local luri = require('uri')
local lfiber = require('fiber')
local netbox = require('net.box') -- for net.box:self()
local trigger = require('internal.trigger')
local ffi = require('ffi')
local yaml_encode = require('yaml').encode
local fiber_clock = lfiber.clock
local netbox_self = netbox.self
local netbox_self_call = netbox_self.call

local MODULE_INTERNALS = '__module_vshard_storage'
-- Reload requirements, in case this module is reloaded manually.
if rawget(_G, MODULE_INTERNALS) then
    local vshard_modules = {
        'vshard.consts', 'vshard.error', 'vshard.cfg',
        'vshard.replicaset', 'vshard.util',
        'vshard.storage.reload_evolution',
        'vshard.lua_gc', 'vshard.rlist', 'vshard.registry',
        'vshard.heap', 'vshard.storage.ref', 'vshard.storage.sched',
    }
    for _, module in pairs(vshard_modules) do
        package.loaded[module] = nil
    end
end
local rlist = require('vshard.rlist')
local consts = require('vshard.consts')
local lerror = require('vshard.error')
local lcfg = require('vshard.cfg')
local lreplicaset = require('vshard.replicaset')
local util = require('vshard.util')
local lua_gc = require('vshard.lua_gc')
local lregistry = require('vshard.registry')
local lref = require('vshard.storage.ref')
local lsched = require('vshard.storage.sched')
local reload_evolution = require('vshard.storage.reload_evolution')
local fiber_cond_wait = util.fiber_cond_wait
local bucket_ref_new

local M = rawget(_G, MODULE_INTERNALS)
if not M then
    ffi.cdef[[
        struct bucket_ref {
            uint32_t ro;
            uint32_t rw;
            bool rw_lock;
            bool ro_lock;
        };
    ]]
    bucket_ref_new = ffi.metatype("struct bucket_ref", {})
    --
    -- The module is loaded for the first time.
    --
    -- !!!WARNING: any change of this table must be reflected in
    -- `vshard.storage.reload_evolution` module to guarantee
    -- reloadability of the module.
    M = {
        ---------------- Common module attributes ----------------
        -- The last passed configuration.
        current_cfg = nil,
        --
        -- All known replicasets used for bucket re-balancing.
        -- See format in replicaset.lua.
        --
        replicasets = nil,
        -- Triggers on master switch event. They are called right
        -- before the event occurs.
        _on_master_enable = trigger.new('_on_master_enable'),
        _on_master_disable = trigger.new('_on_master_disable'),
        -- Index which is a trigger to shard its space by numbers in
        -- this index. It must have at first part either unsigned,
        -- or integer or number type and be not nullable. Values in
        -- this part are considered as bucket identifiers.
        shard_index = nil,
        -- Bucket count stored on all replicasets.
        total_bucket_count = 0,
        errinj = {
            ERRINJ_CFG = false,
            ERRINJ_RELOAD = false,
            ERRINJ_CFG_DELAY = false,
            ERRINJ_LONG_RECEIVE = false,
            ERRINJ_LAST_RECEIVE_DELAY = false,
            ERRINJ_RECEIVE_PARTIALLY = false,
            ERRINJ_NO_RECOVERY = false,
            ERRINJ_UPGRADE = false,
            ERRINJ_DISCOVERY = false,
        },
        -- This counter is used to restart background fibers with
        -- new reloaded code.
        module_version = 0,
        --
        -- Timeout to wait sync with slaves. Used on master
        -- demotion or on a manual sync() call.
        --
        sync_timeout = consts.DEFAULT_SYNC_TIMEOUT,
        -- References to a parent replicaset and self in it.
        this_replicaset = nil,
        this_replica = nil,
        --
        -- Incremental generation of the _bucket space. It is
        -- incremented on each _bucket change and is used to
        -- detect that _bucket was not changed between yields.
        --
        bucket_generation = 0,
        -- Condition variable fired on generation update.
        bucket_generation_cond = lfiber.cond(),
        --
        -- Reference to the function used as on_replace trigger on
        -- _bucket space. It is used to replace the trigger with
        -- a new function when reload happens. It is kept
        -- explicitly because the old function is deleted on
        -- reload from the global namespace. On the other hand, it
        -- is still stored in _bucket:on_replace() somewhere, but
        -- it is not known where. The only 100% way to be able to
        -- replace the old function is to keep its reference.
        --
        bucket_on_replace = nil,
        --
        -- Reference to the function used as on_replace trigger on
        -- _schema space. Saved explicitly by the same reason as
        -- _bucket on_replace.
        -- It is used by replicas to wait for schema bootstrap
        -- because they might be configured earlier than the
        -- master.
        schema_on_replace = nil,
        -- Fast alternative to box.space._bucket:count(). But may be nil. Reset
        -- on each generation change.
        bucket_count_cache = nil,
        -- Fast alternative to checking multiple keys presence in
        -- box.space._bucket status index. But may be nil. Reset on each
        -- generation change.
        bucket_are_all_rw_cache = nil,
        -- Redirects for recently sent buckets. They are kept for a while to
        -- help routers find a new location for sent and deleted buckets without
        -- whole cluster scan.
        route_map = {},
        -- Flag whether vshard.storage.cfg() is finished.
        is_configured = false,
        -- Flag whether box.info.status is acceptable. For instance, 'loading'
        -- is not.
        is_loaded = false,
        -- Flag whether the instance is enabled manually. It is true by default
        -- for backward compatibility with old vshard.
        is_enabled = true,
        -- Reference to the function-proxy to most of the public functions. It
        -- allows to avoid 'if's in each function by adding expensive
        -- conditional checks in one rarely used version of the wrapper and no
        -- checks into the other almost always used wrapper.
        api_call_cache = nil,

        ------------------- Garbage collection -------------------
        -- Fiber to remove garbage buckets data.
        collect_bucket_garbage_fiber = nil,
        -- Boolean lua_gc state (create periodic gc task).
        collect_lua_garbage = nil,

        -------------------- Bucket recovery ---------------------
        recovery_fiber = nil,

        ----------------------- Rebalancer -----------------------
        -- Fiber to rebalance a cluster.
        rebalancer_fiber = nil,
        -- Fiber which applies routes one by one. Its presence and
        -- active status means that the rebalancing is in progress
        -- now on the current node.
        rebalancer_applier_fiber = nil,
        -- Internal flag to activate and deactivate rebalancer. Mostly
        -- for tests.
        is_rebalancer_active = true,
        -- Maximal allowed percent deviation of bucket count on a
        -- replicaset from etalon bucket count.
        rebalancer_disbalance_threshold = 0,
        -- How many more receiving buckets this replicaset can
        -- handle simultaneously. This is not just a constant
        -- 'max' number, because in that case a 'count' would be
        -- needed. And such a 'count' wouldn't be precise, because
        -- 'receiving' buckets can appear not only from
        -- bucket_recv. For example, the tests can manually create
        -- receiving buckets.
        rebalancer_receiving_quota = consts.DEFAULT_REBALANCER_MAX_RECEIVING,
        -- Identifier of a bucket that rebalancer is sending or
        -- receiving now. If a bucket has state SENDING/RECEIVING,
        -- but its id is not stored here, it means, that its
        -- transfer has failed.
        rebalancer_transfering_buckets = {},
        -- How many worker fibers will execute the rebalancer
        -- order in parallel. Each fiber sends 1 bucket at a
        -- moment.
        rebalancer_worker_count = consts.DEFAULT_REBALANCER_WORKER_COUNT,
        -- Map of bucket ro/rw reference counters. These counters
        -- works like bucket pins, but countable and are not
        -- persisted. Persistence is not needed since the refs are
        -- used to keep a bucket during a request execution, but
        -- on restart evidently each request fails.
        bucket_refs = {},
        -- Condition variable fired each time a bucket locked for
        -- RW refs reaches 0 of the latter.
        bucket_rw_lock_is_ready_cond = lfiber.cond(),

        ------------------------- Reload -------------------------
        -- Version of the loaded module. This number is used on
        -- reload to determine which upgrade scripts to run.
        reload_version = reload_evolution.version,
    }
else
    bucket_ref_new = ffi.typeof("struct bucket_ref")

    -- It is not set when reloaded from an old vshard version.
    if M.is_enabled == nil then
        M.is_enabled = true
    end
end

--
-- Invoke a function on this instance. Arguments are unpacked into the function
-- as arguments.
-- The function returns pcall() as is, because is used from places where
-- exceptions are not allowed.
--
local function local_call(func_name, args)
    return pcall(netbox_self_call, netbox_self, func_name, args)
end

--
-- Get number of buckets stored on this storage. Regardless of their state.
--
-- The idea is that all the code should use one function ref to get the bucket
-- count. But inside the function never branches. Instead, it points at one of 2
-- branch-less functions. Cached one simply returns a number which is supposed
-- to be super fast. Non-cached remembers the count and changes the global
-- function to the cached one. So on the next call it is cheap. No 'if's at all.
--
local bucket_count

local function bucket_count_cache()
    return M.bucket_count_cache
end

local function bucket_count_not_cache()
    local count = box.space._bucket:count()
    M.bucket_count_cache = count
    bucket_count = bucket_count_cache
    return count
end

bucket_count = bucket_count_not_cache

--
-- Can't expose bucket_count to the public API as is. Need this proxy-call.
-- Because the original function changes at runtime.
--
local function bucket_count_public()
    return bucket_count()
end

--
-- Check if all buckets on the storage are writable. The idea is the same as
-- with bucket count - the value changes very rare, and is cached most of the
-- time. Only that its non-cached calculation is more expensive than with count.
--
local bucket_are_all_rw

local function bucket_are_all_rw_cache()
    return M.bucket_are_all_rw_cache
end

local function bucket_are_all_rw_not_cache()
    local status_index = box.space._bucket.index.status
    local status = consts.BUCKET
    local res = not status_index:min(status.SENDING) and
       not status_index:min(status.SENT) and
       not status_index:min(status.RECEIVING) and
       not status_index:min(status.GARBAGE)

    M.bucket_are_all_rw_cache = res
    bucket_are_all_rw = bucket_are_all_rw_cache
    return res
end

bucket_are_all_rw = bucket_are_all_rw_not_cache

local function bucket_are_all_rw_public()
    return bucket_are_all_rw()
end

--
-- Trigger for on replace into _bucket to update its generation.
--
local function bucket_generation_increment()
    bucket_count = bucket_count_not_cache
    bucket_are_all_rw = bucket_are_all_rw_not_cache
    M.bucket_count_cache = nil
    M.bucket_are_all_rw_cache = nil
    M.bucket_generation = M.bucket_generation + 1
    M.bucket_generation_cond:broadcast()
end

local function bucket_generation_wait(timeout)
    return fiber_cond_wait(M.bucket_generation_cond, timeout)
end

--
-- Check if this replicaset is locked. It means be invisible for
-- the rebalancer.
--
local function is_this_replicaset_locked()
    return M.this_replicaset and M.this_replicaset.lock
end

--
-- Check if @a bucket can accept 'write' requests. Writable
-- buckets can accept 'read' too.
--
local function bucket_is_writable(bucket)
    return bucket.status == consts.BUCKET.ACTIVE or
           bucket.status == consts.BUCKET.PINNED
end

--
-- Check if @a bucket can accept 'read' requests.
--
local function bucket_is_readable(bucket)
    return bucket_is_writable(bucket) or bucket.status == consts.BUCKET.SENDING
end

--
-- Check if a bucket is sending or receiving.
--
local function bucket_is_transfer_in_progress(bucket)
    return bucket.status == consts.BUCKET.SENDING or
           bucket.status == consts.BUCKET.RECEIVING
end

--
-- Check if @a bucket is garbage. It is true for
-- * sent buckets;
-- * buckets explicitly marked to be a garbage.
--
local function bucket_is_garbage(bucket)
    return bucket.status == consts.BUCKET.SENT or
           bucket.status == consts.BUCKET.GARBAGE
end

--
-- Check that a bucket with the specified id has the needed
-- status.
-- @param bucket_generation Generation since the last check.
-- @param bucket_id Id of the bucket to check.
-- @param status Expected bucket status.
-- @retval New bucket generation.
--
local function bucket_guard_xc(bucket_generation, bucket_id, status)
    if bucket_generation ~= M.bucket_generation then
        local _bucket = box.space._bucket
        local ok, b = pcall(_bucket.get, _bucket, {bucket_id})
        if not ok or (not b and status) or (b and b.status ~= status) then
            local msg =
                string.format("bucket status is changed, was %s, became %s",
                              status, b and b.status or 'dropped')
            error(lerror.vshard(lerror.code.WRONG_BUCKET, bucket_id, msg, nil))
        end
        return M.bucket_generation
    end
    return bucket_generation
end

--
-- Add a positive or a negative value to the receiving buckets
-- quota. Typically this is what bucket_recv() and recovery do.
-- @return Whether the value was added successfully and the quota
--        is changed.
local function bucket_receiving_quota_add(count)
    local new_quota = M.rebalancer_receiving_quota + count
    if new_quota >= 0 then
        local max_quota = M.current_cfg.rebalancer_max_receiving
        if new_quota <= max_quota then
            M.rebalancer_receiving_quota = new_quota
        else
            M.rebalancer_receiving_quota = max_quota
        end
        return true
    end
    return false
end

--
-- Reset the receiving buckets quota. It is used by recovery, when
-- it is discovered, that no receiving buckets are left. In case
-- there was an error which somehow prevented quota return, and a
-- part of the quota was lost.
--
local function bucket_receiving_quota_reset()
    M.rebalancer_receiving_quota = M.current_cfg.rebalancer_max_receiving
end

--------------------------------------------------------------------------------
-- Schema
--------------------------------------------------------------------------------

local schema_version_mt = {
    __tostring = function(self)
        return string.format('{%s}', table.concat(self, '.'))
    end,
    __serialize = function(self)
        return tostring(self)
    end,
    __eq = function(l, r)
        return l[1] == r[1] and l[2] == r[2] and l[3] == r[3] and l[4] == r[4]
    end,
    __lt = function(l, r)
        for i = 1, 4 do
            local diff = l[i] - r[i]
            if diff < 0 then
                return true
            elseif diff > 0 then
                return false
            end
        end
        return false;
    end,
}

local function schema_version_make(ver)
    return setmetatable(ver, schema_version_mt)
end

local function schema_install_triggers()
    local _bucket = box.space._bucket
    if M.bucket_on_replace then
        local ok, err = pcall(_bucket.on_replace, _bucket, nil,
                              M.bucket_on_replace)
        if not ok then
            log.warn('Could not drop old trigger from '..
                     '_bucket: %s', err)
        end
    end
    _bucket:on_replace(bucket_generation_increment)
    M.bucket_on_replace = bucket_generation_increment
end

local function schema_install_on_replace(_, new)
    -- Wait not just for _bucket to appear, but for the entire
    -- schema. This might be important if the schema will ever
    -- consist of more than just _bucket.
    if new == nil or new[1] ~= 'vshard_version' then
        return
    end
    schema_install_triggers()

    local _schema = box.space._schema
    local ok, err = pcall(_schema.on_replace, _schema, nil, M.schema_on_replace)
    if not ok then
        log.warn('Could not drop trigger from _schema inside of the '..
                 'trigger: %s', err)
    end
    M.schema_on_replace = nil
    -- Drop the caches which might have been created while the
    -- schema was being replicated.
    bucket_generation_increment()
end

--
-- Install the triggers later when there is an actual schema to install them on.
-- On replicas it might happen that they are vshard-configured earlier than the
-- master and therefore don't have the schema right away.
--
local function schema_install_triggers_delayed()
    log.info('Could not find _bucket space to install triggers - delayed '..
             'until the schema is replicated')
    assert(not box.space._bucket)
    local _schema = box.space._schema
    if M.schema_on_replace then
        local ok, err = pcall(_schema.on_replace, _schema, nil,
                              M.schema_on_replace)
        if not ok then
            log.warn('Could not drop trigger from _schema: %s', err)
        end
    end
    _schema:on_replace(schema_install_on_replace)
    M.schema_on_replace = schema_install_on_replace
end

-- VShard versioning works in 4 numbers: major, minor, patch, and
-- a last helper number incremented on every schema change, if
-- first 3 numbers stay not changed. That happens when users take
-- the latest master version not having a tag yet. They couldn't
-- upgrade if not the 4th number changed inside one tag.

-- The schema first time appeared with 0.1.16. So this function
-- describes schema before that - 0.1.15.
local function schema_init_0_1_15_0(username, password)
    log.info("Initializing schema %s", schema_version_make({0, 1, 15, 0}))
    box.schema.user.create(username, {
        password = password,
        if_not_exists = true,
    })
    -- Replication may has not been granted, if user exists.
    box.schema.user.grant(username, 'replication', nil, nil,
                          {if_not_exists = true})

    local bucket = box.schema.space.create('_bucket')
    bucket:format({
        {'id', 'unsigned'},
        {'status', 'string'},
        {'destination', 'string', is_nullable = true}
    })
    bucket:create_index('pk', {parts = {'id'}})
    bucket:create_index('status', {parts = {'status'}, unique = false})

    local storage_api = {
        'vshard.storage.sync',
        'vshard.storage.call',
        'vshard.storage.bucket_force_create',
        'vshard.storage.bucket_force_drop',
        'vshard.storage.bucket_collect',
        'vshard.storage.bucket_send',
        'vshard.storage.bucket_recv',
        'vshard.storage.bucket_stat',
        'vshard.storage.buckets_count',
        'vshard.storage.buckets_info',
        'vshard.storage.buckets_discovery',
        'vshard.storage.rebalancer_request_state',
        'vshard.storage.rebalancer_apply_routes',
    }

    for _, name in ipairs(storage_api) do
        box.schema.func.create.create(name, {setuid = true})
        box.schema.user.grant(username, 'execute', 'function', name)
    end

    box.space._schema:replace({'vshard_version', 0, 1, 15, 0})
end

local function schema_upgrade_to_0_1_16_0(username)
    -- Since 0.1.16.0 the old versioning by
    -- 'oncevshard:storage:<number>' is dropped because it is not
    -- really extendible nor understandable.
    log.info("Insert 'vshard_version' into _schema")
    box.space._schema:replace({'vshard_version', 0, 1, 16, 0})
    box.space._schema:delete({'oncevshard:storage:1'})

    -- vshard.storage._call() is supposed to replace some internal
    -- functions exposed in _func; to allow introduction of new
    -- functions on replicas; to allow change of internal
    -- functions without touching the schema.
    local func = 'vshard.storage._call'
    log.info('Create function %s()', func)
    box.schema.func.create.create(func, {setuid = true})
    box.schema.user.grant(username, 'execute', 'function', func)
    -- Don't drop old functions in the same version. Removal can
    -- happen only after 0.1.16. Or there should appear support of
    -- rebalancing from too old versions. Drop of these functions
    -- now would immediately make it impossible to rebalance from
    -- old instances.
end

local function schema_downgrade_from_0_1_16_0()
    log.info("Remove 'vshard_version' from _schema")
    box.space._schema:replace({'oncevshard:storage:1'})
    box.space._schema:delete({'vshard_version'})

    local func = 'vshard.storage._call'
    log.info('Remove function %s()', func)
    box.schema.func.create.drop(func, {if_exists = true})
end

local function schema_current_version()
    local version = box.space._schema:get({'vshard_version'})
    if version == nil then
        return schema_version_make({0, 1, 15, 0})
    else
        return schema_version_make(version:totable(2))
    end
end

local schema_latest_version = schema_version_make({0, 1, 16, 0})

local function schema_upgrade_replica()
    local version = schema_current_version()
    -- Replica can't do upgrade - it is read-only. And it
    -- shouldn't anyway - that would conflict with master doing
    -- the same. So the upgrade is either non-critical, and the
    -- replica can work with the new code but old schema. Or it
    -- it is critical, and need to wait the schema upgrade from
    -- the master.
    -- Or it may happen, that the upgrade just is not possible.
    -- For example, when an auto-upgrade tries to change a too old
    -- schema to the newest, skipping some intermediate versions.
    -- For example, from 1.2.3.4 to 1.7.8.9, when it is assumed
    -- that a safe upgrade should go 1.2.3.4 -> 1.2.4.1 ->
    -- 1.3.1.1 and so on step by step.
    if version ~= schema_latest_version then
        log.info('Replica\' vshard schema version is not latest - current '..
                 '%s vs latest %s, but the replica still can work', version,
                 schema_latest_version)
    end
    -- In future for hard changes the replica may be suspended
    -- until its schema is synced with master. Or it may
    -- reject to upgrade in case of incompatible changes. Now
    -- there are too few versions so as such problems could
    -- appear.
end

-- Every handler should be atomic. It is either applied whole, or
-- not applied at all. Atomic upgrade helps to downgrade in case
-- something goes wrong. At least by doing restart with the latest
-- successfully applied version. However, atomicity does not
-- prohibit yields, in case the upgrade, for example, affects huge
-- number of tuples (_bucket records, maybe).
local schema_upgrade_handlers = {
    {
        version = schema_version_make({0, 1, 16, 0}),
        upgrade = schema_upgrade_to_0_1_16_0,
        downgrade = schema_downgrade_from_0_1_16_0
    },
}

local function schema_upgrade_master(target_version, username, password)
    local _schema = box.space._schema
    local is_old_versioning = _schema:get({'oncevshard:storage:1'}) ~= nil
    local version = schema_current_version()
    local is_bootstrap = not box.space._bucket

    if is_bootstrap then
        schema_init_0_1_15_0(username, password)
    elseif is_old_versioning then
        log.info("The instance does not have 'vshard_version' record. "..
                 "It is 0.1.15.0.")
    end
    assert(schema_upgrade_handlers[#schema_upgrade_handlers].version ==
           schema_latest_version)
    local prev_version = version
    local ok, err1, err2
    local errinj = M.errinj.ERRINJ_UPGRADE
    for _, handler in pairs(schema_upgrade_handlers) do
        local next_version = handler.version
        if next_version > target_version then
            break
        end
        if next_version > version then
            log.info("Upgrade vshard schema to %s", next_version)
            if errinj == 'begin' then
                ok, err1 = false, 'Errinj in begin'
            else
                ok, err1 = pcall(handler.upgrade, username)
                if ok and errinj == 'end' then
                    ok, err1 = false, 'Errinj in end'
                end
            end
            if not ok then
                -- Rollback in case the handler started a
                -- transaction before the exception.
                box.rollback()
                log.info("Couldn't upgrade schema to %s: '%s'. Revert to %s",
                         next_version, err1, prev_version)
                ok, err2 = pcall(handler.downgrade)
                if not ok then
                    log.info("Couldn't downgrade schema to %s - fatal error: "..
                             "'%s'", prev_version, err2)
                    os.exit(-1)
                end
                error(err1)
            end
            ok, err1 = pcall(_schema.replace, _schema,
                             {'vshard_version', unpack(next_version)})
            if not ok then
                log.info("Upgraded schema to %s but couldn't update _schema "..
                         "'vshard_version' - fatal error: '%s'", next_version,
                         err1)
                os.exit(-1)
            end
            log.info("Successful vshard schema upgrade to %s", next_version)
        end
        prev_version = next_version
    end
end

local function schema_upgrade(is_master, username, password)
    if is_master then
        return schema_upgrade_master(schema_latest_version, username, password)
    else
        return schema_upgrade_replica()
    end
end

local function this_is_master()
    return M.this_replicaset and M.this_replicaset.master and
           M.this_replica == M.this_replicaset.master
end

local function on_master_disable(new_func, old_func)
    M._on_master_disable(new_func, old_func)
    -- If a trigger is set after storage.cfg(), then notify an
    -- user, that the current instance is not master.
    if old_func == nil and not this_is_master() then
        M._on_master_disable:run()
    end
end

local function on_master_enable(new_func, old_func)
    M._on_master_enable(new_func, old_func)
    -- Same as above, but notify, that the instance is master.
    if old_func == nil and this_is_master() then
        M._on_master_enable:run()
    end
end

--------------------------------------------------------------------------------
-- Recovery
--------------------------------------------------------------------------------

--
-- Check if a rebalancing is in progress. It is true, if the node
-- applies routes received from a rebalancer node in the special
-- fiber.
--
local function rebalancing_is_in_progress()
    local f = M.rebalancer_applier_fiber
    return f ~= nil and f:status() ~= 'dead'
end

--
-- Check if a local bucket can be deleted.
--
local function recovery_local_bucket_is_garbage(local_bucket, remote_bucket)
    if not remote_bucket then
        return false
    end
    if bucket_is_writable(remote_bucket) then
        return true
    end
    if remote_bucket.status == consts.BUCKET.SENDING and
       local_bucket.status == consts.BUCKET.RECEIVING then
        assert(not remote_bucket.is_transfering)
        assert(not M.rebalancer_transfering_buckets[local_bucket.id])
        return true
    end
    return false
end

--
-- Check if a local bucket can become active.
--
local function recovery_local_bucket_is_active(local_bucket, remote_bucket)
    return not remote_bucket or bucket_is_garbage(remote_bucket)
end

--
-- Check status of each transferring bucket. Resolve status where
-- possible.
--
local function recovery_step_by_type(type)
    local _bucket = box.space._bucket
    local is_step_empty = true
    local recovered = 0
    local total = 0
    local start_format = 'Starting %s buckets recovery step'
    for _, bucket in _bucket.index.status:pairs(type) do
        total = total + 1
        local bucket_id = bucket.id
        if M.rebalancer_transfering_buckets[bucket_id] then
            goto continue
        end
        assert(bucket_is_transfer_in_progress(bucket))
        local peer_uuid = bucket.destination
        local destination = M.replicasets[peer_uuid]
        if not destination or not destination.master then
            -- No replicaset master for a bucket. Wait until it
            -- appears.
            if is_step_empty then
                log.info(start_format, type)
                log.warn('Can not find for bucket %s its peer %s', bucket_id,
                         peer_uuid)
                is_step_empty = false
            end
            goto continue
        end
        local remote_bucket, err =
            destination:callrw('vshard.storage.bucket_stat', {bucket_id})
        -- Check if it is not a bucket error, and this result can
        -- not be used to recovery anything. Try later.
        if not remote_bucket and (not err or err.type ~= 'ShardingError' or
                                  err.code ~= lerror.code.WRONG_BUCKET) then
            if is_step_empty then
                if err == nil then
                    err = 'unknown'
                end
                log.info(start_format, type)
                log.error('Error during recovery of bucket %s on replicaset '..
                          '%s: %s', bucket_id, peer_uuid, err)
                is_step_empty = false
            end
            goto continue
        end
        -- Do nothing until the bucket on both sides stopped
        -- transferring.
        if remote_bucket and remote_bucket.is_transfering then
            goto continue
        end
        -- It is possible that during lookup a new request arrived
        -- which finished the transfer.
        bucket = _bucket:get{bucket_id}
        if not bucket or not bucket_is_transfer_in_progress(bucket) then
            goto continue
        end
        if is_step_empty then
            log.info(start_format, type)
        end
        if recovery_local_bucket_is_garbage(bucket, remote_bucket) then
            _bucket:update({bucket_id}, {{'=', 2, consts.BUCKET.GARBAGE}})
            recovered = recovered + 1
        elseif recovery_local_bucket_is_active(bucket, remote_bucket) then
            _bucket:replace({bucket_id, consts.BUCKET.ACTIVE})
            recovered = recovered + 1
        elseif is_step_empty then
            log.info('Bucket %s is %s local and %s on replicaset %s, waiting',
                     bucket_id, bucket.status, remote_bucket.status, peer_uuid)
        end
        is_step_empty = false
::continue::
    end
    if not is_step_empty then
        log.info('Finish bucket recovery step, %d %s buckets are recovered '..
                 'among %d', recovered, type, total)
    end
    return total, recovered
end

--
-- Infinite function to resolve status of buckets, whose 'sending'
-- has failed due to tarantool or network problems. Restarts on
-- reload.
--
local function recovery_f()
    local module_version = M.module_version
    -- Change of _bucket increments bucket generation. Recovery has its own
    -- bucket generation which is <= actual. Recovery is finished, when its
    -- generation == bucket generation. In such a case the fiber does nothing
    -- until next _bucket change.
    local bucket_generation_recovered = -1
    local bucket_generation_current = M.bucket_generation
    local ok, sleep_time, is_all_recovered, total, recovered
    -- Interrupt recovery if a module has been reloaded. Perhaps,
    -- there was found a bug, and reload fixes it.
    while module_version == M.module_version do
        if M.errinj.ERRINJ_NO_RECOVERY then
            lfiber.yield()
            goto continue
        end
        is_all_recovered = true
        if bucket_generation_recovered == bucket_generation_current then
            goto sleep
        end

        ok, total, recovered = pcall(recovery_step_by_type,
                                     consts.BUCKET.SENDING)
        if not ok then
            is_all_recovered = false
            log.error('Error during sending buckets recovery: %s', total)
        elseif total ~= recovered then
            is_all_recovered = false
        end

        ok, total, recovered = pcall(recovery_step_by_type,
                                     consts.BUCKET.RECEIVING)
        if not ok then
            is_all_recovered = false
            log.error('Error during receiving buckets recovery: %s', total)
        elseif total == 0 then
            bucket_receiving_quota_reset()
        else
            bucket_receiving_quota_add(recovered)
            if total ~= recovered then
                is_all_recovered = false
            end
        end

    ::sleep::
        if not is_all_recovered then
            bucket_generation_recovered = -1
        else
            bucket_generation_recovered = bucket_generation_current
        end
        bucket_generation_current = M.bucket_generation

        if not is_all_recovered then
            -- One option - some buckets are not broken. Their transmission is
            -- still in progress. Don't need to retry immediately. Another
            -- option - network errors when tried to repair the buckets. Also no
            -- need to retry often. It won't help.
            sleep_time = consts.RECOVERY_BACKOFF_INTERVAL
        elseif bucket_generation_recovered ~= bucket_generation_current then
            sleep_time = 0
        else
            sleep_time = consts.TIMEOUT_INFINITY
        end
        if module_version == M.module_version then
            M.bucket_generation_cond:wait(sleep_time)
        end
    ::continue::
    end
end

--
-- Immediately wakeup recovery fiber, if exists.
--
local function recovery_wakeup()
    if M.recovery_fiber then
        M.recovery_fiber:wakeup()
    end
end

--------------------------------------------------------------------------------
-- Replicaset
--------------------------------------------------------------------------------

-- Vclock comparing function
local function vclock_lesseq(vc1, vc2)
    local lesseq = true
    for i, lsn in ipairs(vc1) do
        lesseq = lesseq and lsn <= (vc2[i] or 0)
        if not lesseq then
            break
        end
    end
    return lesseq
end

local function sync(timeout)
    if timeout ~= nil and type(timeout) ~= 'number' then
        error('Usage: vshard.storage.sync([timeout: number])')
    end

    log.debug("Synchronizing replicaset...")
    timeout = timeout or M.sync_timeout
    local vclock = box.info.vclock
    local tstart = fiber_clock()
    repeat
        local done = true
        for _, replica in ipairs(box.info.replication) do
            local down = replica.downstream
            if down and (down.status == 'stopped' or
                         not vclock_lesseq(vclock, down.vclock)) then
                done = false
                break
            end
        end
        if done then
            log.debug("Replicaset has been synchronized")
            return true
        end
        lfiber.sleep(0.001)
    until fiber_clock() > tstart + timeout
    log.warn("Timed out during synchronizing replicaset")
    return nil, lerror.timeout()
end

--------------------------------------------------------------------------------
-- Buckets
--------------------------------------------------------------------------------

--
-- Check that an action of a specified mode can be applied to a
-- bucket.
-- @param bucket_id Bucket identifier.
-- @param mode 'Read' or 'write' mode.
--
-- @retval bucket Bucket that can accept an action of a specified
--         mode.
-- @retval bucket and error object Bucket that can not accept the
--         action, and a reason why.
--
local function bucket_check_state(bucket_id, mode)
    assert(type(bucket_id) == 'number')
    assert(mode == 'read' or mode == 'write')
    local bucket = box.space._bucket:get({bucket_id})
    local reason = nil
    if not bucket then
        reason = 'Not found'
    elseif mode == 'read' then
        if bucket_is_readable(bucket) then
            return bucket
        end
        reason = 'read is prohibited'
    elseif not bucket_is_writable(bucket) then
        if bucket_is_transfer_in_progress(bucket) then
            return bucket, lerror.vshard(lerror.code.TRANSFER_IS_IN_PROGRESS,
                                         bucket_id, bucket.destination)
        end
        reason = 'write is prohibited'
    elseif M.this_replicaset.master ~= M.this_replica then
        local master_uuid = M.this_replicaset.master
        if master_uuid then
            master_uuid = master_uuid.uuid
        end
        return bucket, lerror.vshard(lerror.code.NON_MASTER,
                                     M.this_replica.uuid,
                                     M.this_replicaset.uuid, master_uuid)
    else
        return bucket
    end
    local dst = bucket and bucket.destination or M.route_map[bucket_id]
    return bucket, lerror.vshard(lerror.code.WRONG_BUCKET, bucket_id, reason,
                                 dst)
end

--
-- Take Read-Only reference on the bucket identified by
-- @a bucket_id. Under such reference a bucket can not be deleted
-- from the storage. Its transfer still can start, but can not
-- finish until ref == 0.
-- @param bucket_id Identifier of a bucket to ref.
--
-- @retval true The bucket is referenced ok.
-- @retval nil, error Can not ref the bucket. An error object is
--         returned via the second value.
--
local function bucket_refro(bucket_id)
    local ref = M.bucket_refs[bucket_id]
    if not ref then
        local _, err = bucket_check_state(bucket_id, 'read')
        if err then
            return nil, err
        end
        ref = bucket_ref_new()
        ref.ro = 1
        M.bucket_refs[bucket_id] = ref
    elseif ref.ro_lock then
        return nil, lerror.vshard(lerror.code.BUCKET_IS_LOCKED, bucket_id)
    elseif ref.ro == 0 and ref.rw == 0 then
    -- RW is more strict ref than RO so rw != 0 is sufficient to
    -- take an RO ref.
        local _, err = bucket_check_state(bucket_id, 'read')
        if err then
            return nil, err
        end
        ref.ro = 1
    else
        ref.ro = ref.ro + 1
    end
    return true
end

--
-- Remove one RO reference.
--
local function bucket_unrefro(bucket_id)
    local ref = M.bucket_refs[bucket_id]
    local count = ref and ref.ro or 0
    if count == 0 then
        return nil, lerror.vshard(lerror.code.WRONG_BUCKET, bucket_id,
                                  "no refs", nil)
    end
    if count == 1 then
        ref.ro = 0
        if ref.ro_lock then
            -- Garbage collector is waiting for the bucket if RO
            -- is locked. Let it know it has one more bucket to
            -- collect. It relies on generation, so its increment
            -- is enough.
            bucket_generation_increment()
        end
        return true
    end
    ref.ro = count - 1
    return true
end

--
-- Same as bucket_refro, but more strict - the bucket transfer
-- can not start until a bucket has such refs. And if the bucket
-- is already scheduled for transfer then it can not take new RW
-- refs. The rebalancer waits until all RW refs gone and starts
-- transfer.
--
local function bucket_refrw(bucket_id)
    local ref = M.bucket_refs[bucket_id]
    if not ref then
        local _, err = bucket_check_state(bucket_id, 'write')
        if err then
            return nil, err
        end
        ref = bucket_ref_new()
        ref.rw = 1
        M.bucket_refs[bucket_id] = ref
    elseif ref.rw_lock then
        return nil, lerror.vshard(lerror.code.BUCKET_IS_LOCKED, bucket_id)
    elseif ref.rw == 0 then
        local _, err = bucket_check_state(bucket_id, 'write')
        if err then
            return nil, err
        end
        ref.rw = 1
    else
        ref.rw = ref.rw + 1
    end
    return true
end

--
-- Remove one RW reference.
--
local function bucket_unrefrw(bucket_id)
    local ref = M.bucket_refs[bucket_id]
    if not ref or ref.rw == 0 then
        return nil, lerror.vshard(lerror.code.WRONG_BUCKET, bucket_id,
                                  "no refs", nil)
    end
    if ref.rw == 1 and ref.rw_lock then
        ref.rw = 0
        M.bucket_rw_lock_is_ready_cond:broadcast()
    else
        ref.rw = ref.rw - 1
    end
    return true
end

--
-- Ensure that a bucket ref exists and can be referenced for an RW
-- request.
--
local function bucket_refrw_touch(bucket_id)
    local status, err = bucket_refrw(bucket_id)
    if not status then
        return nil, err
    end
    bucket_unrefrw(bucket_id)
    return M.bucket_refs[bucket_id]
end

--
-- Ref/unref shortcuts for an obscure mode.
--

local function bucket_ref(bucket_id, mode)
    if mode == 'read' then
        return bucket_refro(bucket_id)
    elseif mode == 'write' then
        return bucket_refrw(bucket_id)
    else
        error('Unknown mode')
    end
end

local function bucket_unref(bucket_id, mode)
    if mode == 'read' then
        return bucket_unrefro(bucket_id)
    elseif mode == 'write' then
        return bucket_unrefrw(bucket_id)
    else
        error('Unknown mode')
    end
end

--
-- Return information about bucket
--
local function bucket_stat(bucket_id)
    if type(bucket_id) ~= 'number' then
        error('Usage: bucket_stat(bucket_id)')
    end
    local stat, err = bucket_check_state(bucket_id, 'read')
    if stat then
        stat = stat:tomap()
        if M.rebalancer_transfering_buckets[bucket_id] then
            stat.is_transfering = true
        end
    end
    return stat, err
end

--
-- Create bucket range manually for initial bootstrap, tests or
-- emergency cases. Buckets id, id + 1, id + 2, ..., id + count
-- are inserted.
-- @param first_bucket_id Identifier of a first bucket in a range.
-- @param count Bucket range length to insert. By default is 1.
--
local function bucket_force_create_impl(first_bucket_id, count)
    local _bucket = box.space._bucket
    box.begin()
    local limit = consts.BUCKET_CHUNK_SIZE
    for i = first_bucket_id, first_bucket_id + count - 1 do
        _bucket:insert({i, consts.BUCKET.ACTIVE})
        limit = limit - 1
        if limit == 0 then
            box.commit()
            box.begin()
            limit = consts.BUCKET_CHUNK_SIZE
        end
    end
    box.commit()
end

local function bucket_force_create(first_bucket_id, count)
    if type(first_bucket_id) ~= 'number' or
       (count ~= nil and (type(count) ~= 'number' or
                          math.floor(count) ~= count)) then
        error('Usage: bucket_force_create(first_bucket_id, count)')
    end
    count = count or 1
    local ok, err = pcall(bucket_force_create_impl, first_bucket_id, count)
    if not ok then
        box.rollback()
        return nil, err
    end
    return true
end

--
-- Drop bucket manually for tests or emergency cases
--
local function bucket_force_drop(bucket_id)
    if type(bucket_id) ~= 'number' then
        error('Usage: bucket_force_drop(bucket_id)')
    end

    box.space._bucket:delete({bucket_id})
    return true
end


--
-- Receive bucket data. If the bucket is not presented here, it is
-- created as RECEIVING.
-- @param bucket_id Bucket to receive.
-- @param from Source UUID.
-- @param data Bucket data in the format:
--        [{space_name, [space_tuples]}, ...].
-- @param opts Options. Now the only possible option is 'is_last'.
--        It is set to true when the data portion is last and the
--        bucket can be activated here.
--
-- @retval nil, error Error occurred.
-- @retval true The data is received ok.
--
local function bucket_recv_xc(bucket_id, from, data, opts)
    if not from or not M.replicasets[from] then
        return nil, lerror.vshard(lerror.code.NO_SUCH_REPLICASET, from)
    end
    local _bucket = box.space._bucket
    local b = _bucket:get{bucket_id}
    local recvg = consts.BUCKET.RECEIVING
    local is_last = opts and opts.is_last
    if not b then
        if is_last then
            local msg = 'last message is received, but the bucket does not '..
                        'exist anymore'
            return nil, lerror.vshard(lerror.code.WRONG_BUCKET, bucket_id, msg,
                                      from)
        end
        if is_this_replicaset_locked() then
            return nil, lerror.vshard(lerror.code.REPLICASET_IS_LOCKED)
        end
        if not bucket_receiving_quota_add(-1) then
            return nil, lerror.vshard(lerror.code.TOO_MANY_RECEIVING)
        end
        local timeout = opts and opts.timeout or
                        consts.DEFAULT_BUCKET_SEND_TIMEOUT
        local ok, err = lsched.move_start(timeout)
        if not ok then
            return nil, err
        end
        assert(lref.count == 0)
        -- Move schedule is done only for the time of _bucket update.
        -- The reason is that one bucket_send() calls bucket_recv() on the
        -- remote storage multiple times. If the latter would schedule new moves
        -- on each call, it could happen that the scheduler would block it in
        -- favor of refs right in the middle of bucket_send().
        -- It would lead to a deadlock, because refs won't be able to start -
        -- the bucket won't be writable.
        -- This way still provides fair scheduling, but does not have the
        -- described issue.
        ok, err = pcall(_bucket.insert, _bucket, {bucket_id, recvg, from})
        lsched.move_end(1)
        if not ok then
            return nil, lerror.make(err)
        end
    elseif b.status ~= recvg then
        local msg = string.format("bucket state is changed: was receiving, "..
                                  "became %s", b.status)
        return nil, lerror.vshard(lerror.code.WRONG_BUCKET, bucket_id, msg,
                                  from)
    end
    local bucket_generation = M.bucket_generation
    local limit = consts.BUCKET_CHUNK_SIZE
    for _, row in ipairs(data) do
        local space_name, space_data = row[1], row[2]
        local space = box.space[space_name]
        if space == nil then
            -- Tarantool doesn't provide API to create box.error
            -- objects before 1.10.
            local _, boxerror = pcall(box.error, box.error.NO_SUCH_SPACE,
                                      space_name)
            return nil, lerror.box(boxerror)
        end
        box.begin()
        for _, tuple in ipairs(space_data) do
            local ok, err = pcall(space.insert, space, tuple)
            if not ok then
                box.rollback()
                return nil, lerror.vshard(lerror.code.BUCKET_RECV_DATA_ERROR,
                                          bucket_id, space.name,
                                          box.tuple.new(tuple), err)
            end
            limit = limit - 1
            if limit == 0 then
                box.commit()
                if M.errinj.ERRINJ_RECEIVE_PARTIALLY then
                    box.error(box.error.INJECTION,
                              "the bucket is received partially")
                end
                bucket_generation =
                    bucket_guard_xc(bucket_generation, bucket_id, recvg)
                box.begin()
                limit = consts.BUCKET_CHUNK_SIZE
            end
        end
        box.commit()
        if M.errinj.ERRINJ_RECEIVE_PARTIALLY then
            box.error(box.error.INJECTION, "the bucket is received partially")
        end
        bucket_generation = bucket_guard_xc(bucket_generation, bucket_id, recvg)
    end
    if is_last then
        _bucket:replace({bucket_id, consts.BUCKET.ACTIVE})
        bucket_receiving_quota_add(1)
        if M.errinj.ERRINJ_LONG_RECEIVE then
            box.error(box.error.TIMEOUT)
        end
    end
    return true
end

--
-- Exception safe version of bucket_recv_xc.
--
local function bucket_recv(bucket_id, from, data, opts)
    while opts and opts.is_last and M.errinj.ERRINJ_LAST_RECEIVE_DELAY do
        lfiber.sleep(0.01)
    end
    M.rebalancer_transfering_buckets[bucket_id] = true
    local status, ret, err = pcall(bucket_recv_xc, bucket_id, from, data, opts)
    M.rebalancer_transfering_buckets[bucket_id] = nil
    if status then
        if ret then
            return ret
        end
    else
        err = ret
        ret = status
    end
    box.rollback()
    return nil, err
end

--
-- Find spaces with index having the specified (in cfg) name.
-- The function result is cached using `schema_version`.
-- @retval Map of type {space_id = <space object>}.
--
local sharded_spaces_cache_schema_version = nil
local sharded_spaces_cache = nil
local function find_sharded_spaces()
    if sharded_spaces_cache_schema_version == box.internal.schema_version() then
        return sharded_spaces_cache
    end
    local spaces = {}
    local idx = M.shard_index
    for k, space in pairs(box.space) do
        if type(k) == 'number' and space.index[idx] ~= nil then
            local parts = space.index[idx].parts
            local p = parts[1].type
            if p == 'unsigned' or p == 'integer' or p == 'number' then
                spaces[k] = space
            end
        end
    end
    sharded_spaces_cache_schema_version = box.internal.schema_version()
    sharded_spaces_cache = spaces
    return spaces
end

--
-- Public wrapper for sharded spaces list getter.
--
local function storage_sharded_spaces()
    return table.deepcopy(find_sharded_spaces())
end

if M.errinj.ERRINJ_RELOAD then
    error('Error injection: reload')
end

--
-- Collect content of the readable bucket.
--
local function bucket_collect(bucket_id)
    if type(bucket_id) ~= 'number' then
        error('Usage: bucket_collect(bucket_id)')
    end
    local status, err = bucket_check_state(bucket_id, 'read')
    if err then
        return nil, err
    end
    local data = {}
    local spaces = find_sharded_spaces()
    local idx = M.shard_index
    for k, space in pairs(spaces) do
        assert(space.index[idx] ~= nil)
        local space_data = space.index[idx]:select({bucket_id})
        table.insert(data, {space.name, space_data})
    end
    return data
end

-- Discovery used by routers. It returns limited number of
-- buckets to avoid stalls when _bucket is huge.
local function buckets_discovery_extended(opts)
    local limit = consts.BUCKET_CHUNK_SIZE
    local buckets = table.new(limit, 0)
    local active = consts.BUCKET.ACTIVE
    local pinned = consts.BUCKET.PINNED
    local next_from
    local errcnt = M.errinj.ERRINJ_DISCOVERY
    if errcnt then
        if errcnt > 0 then
            M.errinj.ERRINJ_DISCOVERY = errcnt - 1
            if errcnt % 2 == 0 then
                box.error(box.error.INJECTION, 'discovery')
            end
        else
            M.errinj.ERRINJ_DISCOVERY = false
        end
    end
    -- No way to select by {status, id}, because there are two
    -- statuses to select. A router would need to maintain a
    -- separate iterator for each status it wants to get. This may
    -- be implemented in future. But _bucket space anyway 99% of
    -- time contains only active and pinned buckets. So there is
    -- no big benefit in optimizing that. Perhaps a compound index
    -- {status, id} could help too.
    for _, bucket in box.space._bucket:pairs({opts.from},
                                             {iterator = box.index.GE}) do
        local status = bucket.status
        if status == active or status == pinned then
            table.insert(buckets, bucket.id)
        end
        limit = limit - 1
        if limit == 0 then
            next_from = bucket.id + 1
            break
        end
    end
    -- Buckets list can even be empty, if all buckets in the
    -- scanned chunk are not active/pinned. But next_from still
    -- should be returned. So as the router could request more.
    return {buckets = buckets, next_from = next_from}
end

--
-- Collect array of active bucket identifiers for discovery.
--
local function buckets_discovery(opts)
    if opts then
        -- Private method. Is not documented intentionally.
        return buckets_discovery_extended(opts)
    end
    local ret = {}
    local status = box.space._bucket.index.status
    for _, bucket in status:pairs({consts.BUCKET.ACTIVE}) do
        table.insert(ret, bucket.id)
    end
    for _, bucket in status:pairs({consts.BUCKET.PINNED}) do
        table.insert(ret, bucket.id)
    end
    return ret
end

--
-- The only thing, that must be done to abort a master demote is
-- a reset of read_only.
--
local function local_on_master_disable_abort()
    if not M.current_cfg or M.current_cfg.read_only == nil then
        box.cfg{read_only = false}
    end
end

--
-- Prepare to a master demotion. Before it, a master must stop
-- accept writes, and try to wait until all of its data is
-- replicated to each slave.
--
local function local_on_master_disable_prepare()
    log.info("Resigning from the replicaset master role...")
    if not M.current_cfg or M.current_cfg.read_only == nil then
        box.cfg({read_only = true})
        sync(M.sync_timeout)
    end
end

--
-- This function executes when a master role is removed from local
-- instance during configuration
--
local function local_on_master_disable()
    M._on_master_disable:run()
    -- Stop garbage collecting
    if M.collect_bucket_garbage_fiber ~= nil then
        M.collect_bucket_garbage_fiber:cancel()
        M.collect_bucket_garbage_fiber = nil
        log.info("GC stopped")
    end
    if M.recovery_fiber ~= nil then
        M.recovery_fiber:cancel()
        M.recovery_fiber = nil
        log.info('Recovery stopped')
    end
    log.info("Resigned from the replicaset master role")
end

--
-- The only thing, that must be done to abort a master promotion
-- is a set read_only back to true.
--
local function local_on_master_enable_abort()
    if not M.current_cfg or M.current_cfg.read_only == nil then
        box.cfg({read_only = true})
    end
end

--
-- Promote does not require sync, because a replica can not have a
-- data, that is not on a current master - the replica is read
-- only. But read_only can not be set to false here, because
-- until box.cfg is called, it can not be guaranteed, that the
-- promotion will be successful.
--
local function local_on_master_enable_prepare()
    log.info("Taking on replicaset master role...")
end
--
-- This function executes whan a master role is added to local
-- instance during configuration
--
local function local_on_master_enable()
    if not M.current_cfg or M.current_cfg.read_only == nil then
        box.cfg({read_only = false})
    end
    M._on_master_enable:run()
    -- Start background process to collect garbage.
    M.collect_bucket_garbage_fiber =
        util.reloadable_fiber_create('vshard.gc', M, 'gc_bucket_f')
    M.recovery_fiber =
        util.reloadable_fiber_create('vshard.recovery', M, 'recovery_f')
    -- TODO: check current status
    log.info("Took on replicaset master role")
end

--
-- Send a bucket to other replicaset.
--
local function bucket_send_xc(bucket_id, destination, opts, exception_guard)
    local uuid = box.info.cluster.uuid
    local status
    local ref, err = bucket_refrw_touch(bucket_id)
    if not ref then
        return nil, err
    end
    ref.rw_lock = true
    exception_guard.ref = ref
    exception_guard.drop_rw_lock = true
    local timeout = opts and opts.timeout or consts.DEFAULT_BUCKET_SEND_TIMEOUT
    local deadline = fiber_clock() + timeout
    while ref.rw ~= 0 do
        timeout = deadline - fiber_clock()
        if not M.bucket_rw_lock_is_ready_cond:wait(timeout) then
            return nil, lerror.timeout()
        end
        lfiber.testcancel()
    end

    local _bucket = box.space._bucket
    local bucket = _bucket:get({bucket_id})
    if is_this_replicaset_locked() then
        return nil, lerror.vshard(lerror.code.REPLICASET_IS_LOCKED)
    end
    if bucket.status == consts.BUCKET.PINNED then
        return nil, lerror.vshard(lerror.code.BUCKET_IS_PINNED, bucket_id)
    end
    local replicaset = M.replicasets[destination]
    if replicaset == nil then
        return nil, lerror.vshard(lerror.code.NO_SUCH_REPLICASET, destination)
    end
    if destination == uuid then
        return nil, lerror.vshard(lerror.code.MOVE_TO_SELF, bucket_id, uuid)
    end
    local data = {}
    local spaces = find_sharded_spaces()
    local limit = consts.BUCKET_CHUNK_SIZE
    local idx = M.shard_index
    local bucket_generation = M.bucket_generation
    local sendg = consts.BUCKET.SENDING

    local ok, err = lsched.move_start(timeout)
    if not ok then
        return nil, err
    end
    assert(lref.count == 0)
    -- Move is scheduled only for the time of _bucket update because:
    --
    -- * it is consistent with bucket_recv() (see its comments);
    --
    -- * gives the same effect as if move was in the scheduler for the whole
    --   bucket_send() time, because refs won't be able to start anyway - the
    --   bucket is not writable.
    ok, err = pcall(_bucket.replace, _bucket, {bucket_id, sendg, destination})
    lsched.move_end(1)
    if not ok then
        return nil, lerror.make(err)
    end

    -- From this moment the bucket is SENDING. Such a status is
    -- even stronger than the lock.
    ref.rw_lock = false
    exception_guard.drop_rw_lock = false
    for _, space in pairs(spaces) do
        local index = space.index[idx]
        local space_data = {}
        for _, t in index:pairs({bucket_id}) do
            table.insert(space_data, t)
            limit = limit - 1
            if limit == 0 then
                table.insert(data, {space.name, space_data})
                status, err = replicaset:callrw('vshard.storage.bucket_recv',
                                                {bucket_id, uuid, data}, opts)
                bucket_generation =
                    bucket_guard_xc(bucket_generation, bucket_id, sendg)
                if not status then
                    return status, lerror.make(err)
                end
                limit = consts.BUCKET_CHUNK_SIZE
                data = {}
                space_data = {}
            end
        end
        table.insert(data, {space.name, space_data})
    end
    status, err = replicaset:callrw('vshard.storage.bucket_recv',
                                    {bucket_id, uuid, data}, opts)
    if not status then
        return status, lerror.make(err)
    end
    -- Always send at least two messages to prevent the case, when
    -- a bucket is sent, hung in the network. Then it is recovered
    -- to active on the source, and then the message arrives and
    -- the same bucket is activated on the destination.
    status, err = replicaset:callrw('vshard.storage.bucket_recv',
                                    {bucket_id, uuid, {}, {is_last = true}},
                                    opts)
    if not status then
        return status, lerror.make(err)
    end
    _bucket:replace({bucket_id, consts.BUCKET.SENT, destination})
    ref.ro_lock = true
    return true
end

--
-- Exception and recovery safe version of bucket_send_xc.
--
local function bucket_send(bucket_id, destination, opts)
    if type(bucket_id) ~= 'number' or type(destination) ~= 'string' then
        error('Usage: bucket_send(bucket_id, destination)')
    end
    M.rebalancer_transfering_buckets[bucket_id] = true
    local exception_guard = {}
    local status, ret, err = pcall(bucket_send_xc, bucket_id, destination, opts,
                                   exception_guard)
    if exception_guard.drop_rw_lock then
        exception_guard.ref.rw_lock = false
    end
    M.rebalancer_transfering_buckets[bucket_id] = nil
    if status then
        if ret then
            return ret
        end
    else
        err = ret
        ret = status
    end
    return ret, err
end

--
-- Pin a bucket to a replicaset. Pinned bucket can not be sent
-- even if is breaks the cluster balance.
-- @param bucket_id Bucket identifier to pin.
-- @retval true A bucket is pinned.
-- @retval nil, err A bucket can not be pinned. @A err is the
--         reason why.
--
local function bucket_pin(bucket_id)
    if type(bucket_id) ~= 'number' then
        error('Usage: bucket_pin(bucket_id)')
    end
    local bucket, err = bucket_check_state(bucket_id, 'write')
    if err then
        return nil, err
    end
    assert(bucket)
    if bucket.status ~= consts.BUCKET.PINNED then
        assert(bucket.status == consts.BUCKET.ACTIVE)
        box.space._bucket:update({bucket_id}, {{'=', 2, consts.BUCKET.PINNED}})
    end
    return true
end

--
-- Return a pinned bucket back into active state.
-- @param bucket_id Bucket identifier to unpin.
-- @retval true A bucket is unpinned.
-- @retval nil, err A bucket can not be unpinned. @A err is the
--         reason why.
--
local function bucket_unpin(bucket_id)
    if type(bucket_id) ~= 'number' then
        error('Usage: bucket_unpin(bucket_id)')
    end
    local bucket, err = bucket_check_state(bucket_id, 'write')
    if err then
        return nil, err
    end
    assert(bucket)
    if bucket.status == consts.BUCKET.PINNED then
        box.space._bucket:update({bucket_id}, {{'=', 2, consts.BUCKET.ACTIVE}})
    else
        assert(bucket.status == consts.BUCKET.ACTIVE)
    end
    return true
end

--------------------------------------------------------------------------------
-- Garbage collector
--------------------------------------------------------------------------------
--
-- Delete from a space tuples with a specified bucket id.
-- @param space Space to cleanup.
-- @param bucket_id Id of the bucket to cleanup.
-- @param status Bucket status for guard checks.
--
local function gc_bucket_in_space_xc(space, bucket_id, status)
    local bucket_index = space.index[M.shard_index]
    if #bucket_index:select({bucket_id}, {limit = 1}) == 0 then
        return
    end
    local bucket_generation = M.bucket_generation
    local pk_parts = space.index[0].parts
::restart::
    local limit = consts.BUCKET_CHUNK_SIZE
    box.begin()
    for _, tuple in bucket_index:pairs({bucket_id}) do
        space:delete(util.tuple_extract_key(tuple, pk_parts))
        limit = limit - 1
        if limit == 0 then
            box.commit()
            bucket_generation =
                bucket_guard_xc(bucket_generation, bucket_id, status)
            goto restart
        end
    end
    box.commit()
end

--
-- Exception safe version of gc_bucket_in_space_xc.
--
local function gc_bucket_in_space(space, bucket_id, status)
    local ok, err = pcall(gc_bucket_in_space_xc, space, bucket_id, status)
    if not ok then
        box.rollback()
    end
    return ok, err
end

--
-- Drop buckets with the given status along with their data in all spaces.
-- @param status Status of target buckets.
-- @param route_map Destinations of deleted buckets are saved into this table.
--
local function gc_bucket_drop_xc(status, route_map)
    local limit = consts.BUCKET_CHUNK_SIZE
    local _bucket = box.space._bucket
    local sharded_spaces = find_sharded_spaces()
    for _, b in _bucket.index.status:pairs(status) do
        local id = b.id
        local ref = M.bucket_refs[id]
        if ref then
            assert(ref.rw == 0)
            if ref.ro ~= 0 then
                ref.ro_lock = true
                goto continue
            end
            M.bucket_refs[id] = nil
        end
        for _, space in pairs(sharded_spaces) do
            gc_bucket_in_space_xc(space, id, status)
            limit = limit - 1
            if limit == 0 then
                lfiber.sleep(0)
                limit = consts.BUCKET_CHUNK_SIZE
            end
        end
        route_map[id] = b.destination
        _bucket:delete{id}
    ::continue::
    end
end

--
-- Exception safe version of gc_bucket_drop_xc.
--
local function gc_bucket_drop(status, route_map)
    local status, err = pcall(gc_bucket_drop_xc, status, route_map)
    if not status then
        box.rollback()
    end
    return status, err
end

--
-- Garbage collector. Works on masters. The garbage collector wakes up when
-- state of any bucket changes.
-- After wakeup it follows the plan:
-- 1) Check if state of any bucket has really changed. If not, then sleep again;
-- 2) Delete all GARBAGE and SENT buckets along with their data in chunks of
--    limited size.
-- 3) Bucket destinations are saved into a global route_map to reroute incoming
--    requests from routers in case they didn't notice the buckets being moved.
--    The saved routes are scheduled for deletion after a timeout, which is
--    checked on each iteration of this loop.
-- 4) Sleep, go to (1).
-- For each step details see comments in the code.
--
function gc_bucket_f()
    local module_version = M.module_version
    -- Changes of _bucket increments bucket generation. Garbage
    -- collector has its own bucket generation which is <= actual.
    -- Garbage collection is finished, when collector's
    -- generation == bucket generation. In such a case the fiber
    -- does nothing until next _bucket change.
    local bucket_generation_collected = -1
    local bucket_generation_current = M.bucket_generation
    -- Deleted buckets are saved into a route map to redirect routers if they
    -- didn't discover new location of the buckets yet. However route map does
    -- not grow infinitely. Otherwise it would end up storing redirects for all
    -- buckets in the cluster. Which could also be outdated.
    -- Garbage collector periodically drops old routes from the map. For that it
    -- remembers state of route map in one moment, and after a while clears the
    -- remembered routes from the global route map.
    local route_map = M.route_map
    local route_map_old = {}
    local route_map_deadline = 0
    local status, err
    while M.module_version == module_version do
        if bucket_generation_collected ~= bucket_generation_current then
            status, err = gc_bucket_drop(consts.BUCKET.GARBAGE, route_map)
            if status then
                status, err = gc_bucket_drop(consts.BUCKET.SENT, route_map)
            end
            if not status then
                box.rollback()
                log.error('Error during garbage collection step: %s', err)
            else
                -- Don't use global generation. During the collection it could
                -- already change. Instead, remember the generation known before
                -- the collection has started.
                -- Since the collection also changes the generation, it makes
                -- the GC happen always at least twice. But typically on the
                -- second iteration it should not find any buckets to collect,
                -- and then the collected generation matches the global one.
                bucket_generation_collected = bucket_generation_current
            end
        else
            status = true
        end

        local sleep_time = route_map_deadline - fiber_clock()
        if sleep_time <= 0 then
            local chunk = consts.LUA_CHUNK_SIZE
            util.table_minus_yield(route_map, route_map_old, chunk)
            route_map_old = util.table_copy_yield(route_map, chunk)
            if next(route_map_old) then
                sleep_time = consts.BUCKET_SENT_GARBAGE_DELAY
            else
                sleep_time = consts.TIMEOUT_INFINITY
            end
            route_map_deadline = fiber_clock() + sleep_time
        end
        bucket_generation_current = M.bucket_generation

        if bucket_generation_current ~= bucket_generation_collected then
            -- Generation was changed during collection. Or *by* collection.
            if status then
                -- Retry immediately. If the generation was changed by the
                -- collection itself, it will notice it next iteration, and go
                -- to proper sleep.
                sleep_time = 0
            else
                -- An error happened during the collection. Does not make sense
                -- to retry on each iteration of the event loop. The most likely
                -- errors are either a WAL error or a transaction abort - both
                -- look like an issue in the user's code and can't be fixed
                -- quickly anyway. Backoff.
                sleep_time = consts.GC_BACKOFF_INTERVAL
            end
        end

        if M.module_version == module_version then
            M.bucket_generation_cond:wait(sleep_time)
        end
    end
end

--
-- Immediately wakeup bucket garbage collector.
--
local function garbage_collector_wakeup()
    if M.collect_bucket_garbage_fiber then
        M.collect_bucket_garbage_fiber:wakeup()
    end
end

--
-- Delete data of a specified garbage bucket. If a bucket is not
-- garbage, then force option must be set. A bucket is not
-- deleted from _bucket space.
-- @param bucket_id Identifier of a bucket to delete data from.
-- @param opts Options. Can contain only 'force' flag to delete a
--        bucket regardless of is it garbage or not.
--
local function bucket_delete_garbage(bucket_id, opts)
    if bucket_id == nil or (opts ~= nil and type(opts) ~= 'table') then
        error('Usage: bucket_delete_garbage(bucket_id, opts)')
    end
    opts = opts or {}
    local bucket = box.space._bucket:get({bucket_id})
    if bucket ~= nil and not bucket_is_garbage(bucket) and not opts.force then
        error('Can not delete not garbage bucket. Use "{force=true}" to '..
              'ignore this attention')
    end
    local sharded_spaces = find_sharded_spaces()
    local bucket_status = bucket and bucket.status
    for _, space in pairs(sharded_spaces) do
        local status, err = gc_bucket_in_space(space, bucket_id, bucket_status)
        if not status then
            error(err)
        end
    end
end

--------------------------------------------------------------------------------
-- Rebalancer
--------------------------------------------------------------------------------
--
-- Calculate a set of metrics:
-- * maximal disbalance over all replicasets;
-- * needed buckets for each replicaset.
-- @param replicasets Map of type: {
--     uuid = {bucket_count = number, weight = number},
--     ...
-- }
--
-- @retval Maximal disbalance over all replicasets, and UUID of
--        a replicaset having it.
--
local function rebalancer_calculate_metrics(replicasets)
    local max_disbalance = 0
    local max_disbalance_uuid
    for uuid, replicaset in pairs(replicasets) do
        local needed = replicaset.etalon_bucket_count - replicaset.bucket_count
        if replicaset.etalon_bucket_count ~= 0 then
            local disbalance =
                math.abs(needed) / replicaset.etalon_bucket_count * 100
            if disbalance > max_disbalance then
                max_disbalance = disbalance
                max_disbalance_uuid = uuid
            end
        elseif replicaset.bucket_count ~= 0 then
            max_disbalance = math.huge
            max_disbalance_uuid = uuid
        end
        assert(needed >= 0 or -needed <= replicaset.bucket_count)
        replicaset.needed = needed
    end
    return max_disbalance, max_disbalance_uuid
end

--
-- Move @a needed bucket count from a pool to @a dst_uuid and
-- remember the route in @a bucket_routes table.
--
local function rebalancer_take_buckets_from_pool(bucket_pool, bucket_routes,
                                                 dst_uuid, needed)
    local to_remove_from_pool = {}
    for src_uuid, bucket_count in pairs(bucket_pool) do
        local count = math.min(bucket_count, needed)
        local src = bucket_routes[src_uuid]
        if src == nil then
            bucket_routes[src_uuid] = {[dst_uuid] = count}
        else
            local dst = src[dst_uuid]
            if dst == nil then
                src[dst_uuid] = count
            else
                src[dst_uuid] = dst + count
            end
        end
        local new_count = bucket_pool[src_uuid] - count
        needed = needed - count
        bucket_pool[src_uuid] = new_count
        if new_count == 0 then
            table.insert(to_remove_from_pool, src_uuid)
        end
        if needed == 0 then
            break
        end
    end
    for _, src_uuid in pairs(to_remove_from_pool) do
        bucket_pool[src_uuid] = nil
    end
end

--
-- Build a table with routes defining from which node to which one
-- how many buckets should be moved to reach the best balance in
-- a cluster.
-- @param replicasets Map of type: {
--     uuid = {bucket_count = number, weight = number,
--             needed = number},
--     ...
-- }      This parameter is a result of
--        rebalancer_calculate_metrics().
--
-- @retval Bucket routes. It is a map of type: {
--     src_uuid = {
--         dst_uuid = number, -- Bucket count to move from
--                               src to dst.
--         ...
--     },
--     ...
-- }
--
local function rebalancer_build_routes(replicasets)
    -- Map of type: {
    --     uuid = number, -- free buckets of uuid.
    -- }
    local bucket_pool = {}
    for uuid, replicaset in pairs(replicasets) do
        if replicaset.needed < 0 then
            bucket_pool[uuid] = -replicaset.needed
            replicaset.needed = 0
        end
    end
    local bucket_routes = {}
    for uuid, replicaset in pairs(replicasets) do
        if replicaset.needed > 0 then
            rebalancer_take_buckets_from_pool(bucket_pool, bucket_routes, uuid,
                                              replicaset.needed)
        end
    end
    return bucket_routes
end

--
-- Dispenser is a container of routes received from the
-- rebalancer. Its task is to hand out the routes to worker fibers
-- in a round-robin manner so as any two sequential results are
-- different. It allows to spread dispensing evenly over the
-- receiver nodes.
--
local function route_dispenser_create(routes)
    local rlist = rlist.new()
    local map = {}
    for uuid, bucket_count in pairs(routes) do
        local new = {
            -- Receiver's UUID.
            uuid = uuid,
            -- Rest of buckets to send. The receiver will be
            -- dispensed this number of times.
            bucket_count = bucket_count,
            -- Constant value to be able to track progress.
            need_to_send = bucket_count,
            -- Number of *successfully* sent buckets.
            progress = 0,
            -- If a user set too long max number of receiving
            -- buckets, or too high number of workers, worker
            -- fibers will receive 'throttle' errors, perhaps
            -- quite often. So as not to clog the log each
            -- destination is logged as throttled only once.
            is_throttle_warned = false,
        }
        -- Map of destinations is stored in addition to the queue,
        -- because
        -- 1) It is possible, that there are no more buckets to
        --    send, but suddenly one of the workers trying to send
        --    the last bucket receives a throttle error. In that
        --    case the bucket is put back, and the destination
        --    returns to the queue;
        -- 2) After all buckets are sent, and the queue is empty,
        --    the main applier fiber does some analysis on the
        --    destinations.
        map[uuid] = new
        rlist:add_tail(new)
    end
    return {
        rlist = rlist,
        map = map,
    }
end

--
-- Put one bucket back to the dispenser. It happens, if the worker
-- receives a throttle error. This is the only error that can be
-- tolerated.
--
local function route_dispenser_put(dispenser, uuid)
    local dst = dispenser.map[uuid]
    if dst then
        local bucket_count = dst.bucket_count + 1
        dst.bucket_count = bucket_count
        if bucket_count == 1 then
            dispenser.rlist:add_tail(dst)
        end
    end
end

--
-- In case if a receiver responded with a serious error it is not
-- safe to send more buckets to there. For example, if it was a
-- timeout, it is unknown whether the bucket was received or not.
-- If it was a box error like index key conflict, then it is even
-- worse and the cluster is broken.
--
local function route_dispenser_skip(dispenser, uuid)
    local map = dispenser.map
    local dst = map[uuid]
    if dst then
        map[uuid] = nil
        dispenser.rlist:remove(dst)
    end
end

--
-- Set that the receiver @a uuid was throttled. When it happens
-- first time it is logged.
--
local function route_dispenser_throttle(dispenser, uuid)
    local dst = dispenser.map[uuid]
    if dst then
        local old_value = dst.is_throttle_warned
        dst.is_throttle_warned = true
        return not old_value
    end
    return false
end

--
-- Notify the dispenser that a bucket was successfully sent to
-- @a uuid. It has no any functional purpose except tracking
-- progress.
--
local function route_dispenser_sent(dispenser, uuid)
    local dst = dispenser.map[uuid]
    if dst then
        local new_progress = dst.progress + 1
        dst.progress = new_progress
        local need_to_send = dst.need_to_send
        return new_progress == need_to_send, need_to_send
    end
    return false
end

--
-- Take a next destination to send a bucket to.
--
local function route_dispenser_pop(dispenser)
    local rlist = dispenser.rlist
    local dst = rlist.first
    if dst then
        local bucket_count = dst.bucket_count - 1
        dst.bucket_count = bucket_count
        rlist:remove(dst)
        if bucket_count > 0 then
            rlist:add_tail(dst)
        end
        return dst.uuid
    end
    return nil
end

--
-- Body of one rebalancer worker. All the workers share a
-- dispenser to synchronize their round-robin, and a quit
-- condition to be able to quit, when one of the workers sees that
-- no more buckets are stored, and others took a nap because of
-- throttling.
--
local function rebalancer_worker_f(worker_id, dispenser, quit_cond)
    lfiber.name(string.format('vshard.rebalancer_worker_%d', worker_id))
    if not util.version_is_at_least(1, 10, 0) then
        -- Return control to the caller immediately to allow it
        -- to finish preparations. In 1.9 a caller couldn't create
        -- a fiber without switching to it.
        lfiber.yield()
    end

    local _status = box.space._bucket.index.status
    local opts = {timeout = consts.REBALANCER_CHUNK_TIMEOUT}
    local active_key = {consts.BUCKET.ACTIVE}
    local uuid = route_dispenser_pop(dispenser)
    local worker_throttle_count = 0
    local bucket_id, is_found
    while uuid do
        is_found = false
        -- Can't just take a first active bucket. It may be
        -- already locked by a manual bucket_send in another
        -- fiber.
        for _, bucket in _status:pairs(active_key) do
            bucket_id = bucket.id
            if not M.rebalancer_transfering_buckets[bucket_id] then
                is_found = true
                break
            end
        end
        if not is_found then
            log.error('Can not find active buckets')
            break
        end
        local ret, err = bucket_send(bucket_id, uuid, opts)
        if ret then
            worker_throttle_count = 0
            local finished, total = route_dispenser_sent(dispenser, uuid)
            if finished then
                log.info('%d buckets were successfully sent to %s', total, uuid)
            end
            goto continue
        end
        route_dispenser_put(dispenser, uuid)
        if err.type ~= 'ShardingError' or
           err.code ~= lerror.code.TOO_MANY_RECEIVING then
            log.error('Error during rebalancer routes applying: receiver %s, '..
                      'error %s', uuid, err)
            log.info('Can not finish transfers to %s, skip to next round', uuid)
            worker_throttle_count = 0
            route_dispenser_skip(dispenser, uuid)
            goto continue
        end
        worker_throttle_count = worker_throttle_count + 1
        if route_dispenser_throttle(dispenser, uuid) then
            log.error('Too many buckets is being sent to %s', uuid)
        end
        if worker_throttle_count < dispenser.rlist.count then
            goto continue
        end
        log.info('The worker was asked for throttle %d times in a row. '..
                 'Sleep for %d seconds', worker_throttle_count,
                 consts.REBALANCER_WORK_INTERVAL)
        worker_throttle_count = 0
        if not quit_cond:wait(consts.REBALANCER_WORK_INTERVAL) then
            log.info('The worker is back')
        end
::continue::
        uuid = route_dispenser_pop(dispenser)
    end
    quit_cond:broadcast()
end

--
-- Main applier of rebalancer routes. It manages worker fibers,
-- logs total results.
--
local function rebalancer_apply_routes_f(routes)
    lfiber.name('vshard.rebalancer_applier')
    local worker_count = M.rebalancer_worker_count
    setmetatable(routes, {__serialize = 'mapping'})
    log.info('Apply rebalancer routes with %d workers:\n%s', worker_count,
             yaml_encode(routes))
    local dispenser = route_dispenser_create(routes)
    local _status = box.space._bucket.index.status
    assert(_status:count({consts.BUCKET.SENDING}) == 0)
    assert(_status:count({consts.BUCKET.RECEIVING}) == 0)
    -- Can not assign it on fiber.create() like
    -- var = fiber.create(), because when it yields, we have no
    -- guarantee that an event loop does not contain events
    -- between this fiber and its creator.
    M.rebalancer_applier_fiber = lfiber.self()
    local quit_cond = lfiber.cond()
    local workers = table.new(worker_count, 0)
    for i = 1, worker_count do
        local f
        if util.version_is_at_least(1, 10, 0) then
            f = lfiber.new(rebalancer_worker_f, i, dispenser, quit_cond)
        else
            f = lfiber.create(rebalancer_worker_f, i, dispenser, quit_cond)
        end
        f:set_joinable(true)
        workers[i] = f
    end
    log.info('Rebalancer workers have started, wait for their termination')
    for i = 1, worker_count do
        local f = workers[i]
        local ok, res = f:join()
        if not ok then
            log.error('Rebalancer worker %d threw an exception: %s', i, res)
        end
    end
    log.info('Rebalancer routes are applied')
    local throttled = {}
    for uuid, dst in pairs(dispenser.map) do
        if dst.is_throttle_warned then
            table.insert(throttled, uuid)
        end
    end
    if next(throttled) then
        log.warn('Note, the replicasets {%s} reported too many receiving '..
                 'buckets. Perhaps you need to increase '..
                 '"rebalancer_max_receiving" or decrease '..
                 '"rebalancer_worker_count"', table.concat(throttled, ', '))
    end
end

--
-- Apply routes table of type: {
--     dst_uuid = number, -- Bucket count to send.
--     ...
-- }. Is used by a rebalancer.
--
local function rebalancer_apply_routes(routes)
    if is_this_replicaset_locked() then
        return false, lerror.vshard(lerror.code.REPLICASET_IS_LOCKED);
    end
    assert(not rebalancing_is_in_progress())
    -- Can not apply routes here because of gh-946 in tarantool
    -- about problems with long polling. Apply routes in a fiber.
    lfiber.create(rebalancer_apply_routes_f, routes)
    return true
end

--
-- From each replicaset download bucket count, check all buckets
-- to have SENT or ACTIVE state.
-- @retval not nil Argument of rebalancer_calculate_metrics().
-- @retval     nil Not all replicasets have only SENT and ACTIVE
--         buckets, or some replicasets are unavailable.
--
local function rebalancer_download_states()
    local replicasets = {}
    local total_bucket_locked_count = 0
    local total_bucket_active_count = 0
    for uuid, replicaset in pairs(M.replicasets) do
        local state =
            replicaset:callrw('vshard.storage.rebalancer_request_state', {})
        if state == nil then
            return
        end
        local bucket_count = state.bucket_active_count +
                             state.bucket_pinned_count
        if replicaset.lock then
            total_bucket_locked_count = total_bucket_locked_count + bucket_count
        else
            total_bucket_active_count = total_bucket_active_count + bucket_count
            replicasets[uuid] = {bucket_count = bucket_count,
                                 weight = replicaset.weight,
                                 pinned_count = state.bucket_pinned_count}
        end
    end
    local sum = total_bucket_active_count + total_bucket_locked_count
    if sum == M.total_bucket_count then
        return replicasets, total_bucket_active_count
    else
        log.info('Total active bucket count is not equal to total. '..
                 'Possibly a boostrap is not finished yet. Expected %d, but '..
                 'found %d', M.total_bucket_count, sum)
    end
end

--
-- Background rebalancer. Works on a storage which has the
-- smallest replicaset uuid and which is master.
--
local function rebalancer_f()
    local module_version = M.module_version
    while module_version == M.module_version do
        while not M.is_rebalancer_active do
            log.info('Rebalancer is disabled. Sleep')
            lfiber.sleep(consts.REBALANCER_IDLE_INTERVAL)
        end
        local status, replicasets, total_bucket_active_count =
            pcall(rebalancer_download_states)
        if M.module_version ~= module_version then
            return
        end
        if not status or replicasets == nil then
            if not status then
                log.error('Error during downloading rebalancer states: %s',
                          replicasets)
            end
            log.info('Some buckets are not active, retry rebalancing later')
            lfiber.sleep(consts.REBALANCER_WORK_INTERVAL)
            goto continue
        end
        lreplicaset.calculate_etalon_balance(replicasets,
                                             total_bucket_active_count)
        local max_disbalance, max_disbalance_uuid =
            rebalancer_calculate_metrics(replicasets)
        local threshold = M.rebalancer_disbalance_threshold
        if max_disbalance <= threshold then
            local balance_msg
            if max_disbalance > 0 then
                local rep = replicasets[max_disbalance_uuid]
                balance_msg = string.format(
                    'The cluster is balanced ok with max disbalance %f%% at '..
                    '"%s": etalon bucket count is %d, stored count is %d. '..
                    'The disbalance is smaller than your threshold %f%%, '..
                    'nothing to do.', max_disbalance, max_disbalance_uuid,
                    rep.etalon_bucket_count, rep.bucket_count, threshold)
            else
                balance_msg = 'The cluster is balanced ok.'
            end
            log.info('%s Schedule next rebalancing after %f seconds',
                     balance_msg, consts.REBALANCER_IDLE_INTERVAL)
            lfiber.sleep(consts.REBALANCER_IDLE_INTERVAL)
            goto continue
        end
        local routes = rebalancer_build_routes(replicasets)
        -- Routes table can not be empty. If it had been empty,
        -- then max_disbalance would have been calculated
        -- incorrectly.
        assert(next(routes) ~= nil)
        for src_uuid, src_routes in pairs(routes) do
            local rs = M.replicasets[src_uuid]
            local status, err =
                rs:callrw('vshard.storage.rebalancer_apply_routes',
                          {src_routes})
            if not status then
                log.error('Error during routes appying on "%s": %s. '..
                          'Try rebalance later', rs, lerror.make(err))
                lfiber.sleep(consts.REBALANCER_WORK_INTERVAL)
                goto continue
            end
        end
        log.info('Rebalance routes are sent. Schedule next wakeup after '..
                 '%f seconds', consts.REBALANCER_WORK_INTERVAL)
        lfiber.sleep(consts.REBALANCER_WORK_INTERVAL)
::continue::
    end
end

--
-- Check all buckets of the host storage to have SENT or ACTIVE
-- state, return active bucket count.
-- @retval not nil Count of active buckets.
-- @retval     nil Not SENT or not ACTIVE buckets were found.
--
local function rebalancer_request_state()
    if not M.is_rebalancer_active or rebalancing_is_in_progress() then
        return
    end
    local _bucket = box.space._bucket
    local status_index = _bucket.index.status
    if #status_index:select({consts.BUCKET.SENDING}, {limit = 1}) > 0 then
        return
    end
    if #status_index:select({consts.BUCKET.RECEIVING}, {limit = 1}) > 0 then
        return
    end
    if #status_index:select({consts.BUCKET.GARBAGE}, {limit = 1}) > 0 then
        return
    end
    return {
        bucket_active_count = status_index:count({consts.BUCKET.ACTIVE}),
        bucket_pinned_count = status_index:count({consts.BUCKET.PINNED}),
    }
end

--
-- Immediately wakeup rebalancer, if it exists on the current
-- node.
--
local function rebalancer_wakeup()
    if M.rebalancer_fiber ~= nil then
        M.rebalancer_fiber:wakeup()
    end
end

--
-- Disable/enable rebalancing. Disabled rebalancer sleeps until it
-- is enabled back. If not a rebalancer node is disabled, it does
-- not sends its state to rebalancer.
--
local function rebalancer_disable()
    M.is_rebalancer_active = false
end
local function rebalancer_enable()
    M.is_rebalancer_active = true
end

--------------------------------------------------------------------------------
-- API
--------------------------------------------------------------------------------

-- Call wrapper
-- There is two modes for call operation: read and write, explicitly used for
-- call protocol: there is no way to detect what corresponding function does.
-- NOTE: may be a custom function call api without any checks is needed,
-- for example for some monitoring functions.
--
-- NOTE: this function uses pcall-style error handling
-- @retval nil, err Error.
-- @retval values Success.
local function storage_call(bucket_id, mode, name, args)
    local ok, err, ret1, ret2, ret3, _ = bucket_ref(bucket_id, mode)
    if not ok then
        return ok, err
    end
    ok, ret1, ret2, ret3 = local_call(name, args)
    _, err = bucket_unref(bucket_id, mode)
    assert(not err)
    if not ok then
        ret1 = lerror.make(ret1)
    end
    return ok, ret1, ret2, ret3
end

--
-- Bind a new storage ref to the current box session. Is used as a part of
-- Map-Reduce API.
--
local function storage_ref(rid, timeout)
    local ok, err = lref.add(rid, box.session.id(), timeout)
    if not ok then
        return nil, err
    end
    return bucket_count()
end

--
-- Drop a storage ref from the current box session. Is used as a part of
-- Map-Reduce API.
--
local function storage_unref(rid)
    return lref.del(rid, box.session.id())
end

--
-- Execute a user's function under an infinite storage ref protecting from
-- bucket moves. The ref should exist before, and is deleted after, regardless
-- of the function result. Is used as a part of Map-Reduce API.
--
local function storage_map(rid, name, args)
    local ok, err, res
    local sid = box.session.id()
    ok, err = lref.use(rid, sid)
    if not ok then
        return nil, err
    end
    ok, res = local_call(name, args)
    if not ok then
        lref.del(rid, sid)
        return nil, lerror.make(res)
    end
    ok, err = lref.del(rid, sid)
    if not ok then
        return nil, err
    end
    return true, res
end

local function storage_service_info()
    return {
        is_master = this_is_master(),
    }
end

local service_call_api

local function service_call_test_api(...)
    return service_call_api, ...
end

service_call_api = setmetatable({
    bucket_recv = bucket_recv,
    rebalancer_apply_routes = rebalancer_apply_routes,
    rebalancer_request_state = rebalancer_request_state,
    storage_ref = storage_ref,
    storage_unref = storage_unref,
    storage_map = storage_map,
    info = storage_service_info,
    test_api = service_call_test_api,
}, {__serialize = function(api)
    local res = {}
    for k, _ in pairs(api) do
        table.insert(res, k)
    end
    table.sort(res)
    return res
end})

local function service_call(service_name, ...)
    return service_call_api[service_name](...)
end

--------------------------------------------------------------------------------
-- Configuration
--------------------------------------------------------------------------------

local function storage_cfg(cfg, this_replica_uuid, is_reload)
    if this_replica_uuid == nil then
        error('Usage: cfg(configuration, this_replica_uuid)')
    end
    cfg = lcfg.check(cfg, M.current_cfg)
    local vshard_cfg, box_cfg = lcfg.split(cfg)
    if vshard_cfg.weights or vshard_cfg.zone then
        error('Weights and zone are not allowed for storage configuration')
    end
    if M.replicasets then
        log.info("Starting reconfiguration of replica %s", this_replica_uuid)
    else
        log.info("Starting configuration of replica %s", this_replica_uuid)
    end

    local was_master = M.this_replicaset ~= nil and
                       M.this_replicaset.master == M.this_replica

    local this_replicaset
    local this_replica
    local new_replicasets = lreplicaset.buildall(vshard_cfg)
    local min_master
    for rs_uuid, rs in pairs(new_replicasets) do
        for replica_uuid, replica in pairs(rs.replicas) do
            if (min_master == nil or replica_uuid < min_master.uuid) and
               rs.master == replica then
                min_master = replica
            end
            if replica_uuid == this_replica_uuid then
                assert(this_replicaset == nil)
                this_replicaset = rs
                this_replica = replica
            end
        end
    end
    if this_replicaset == nil then
        error(string.format("Local replica %s wasn't found in config",
                            this_replica_uuid))
    end
    local is_master = this_replicaset.master == this_replica
    if is_master then
        log.info('I am master')
    end

    -- It is considered that all possible errors during cfg
    -- process occur only before this place.
    -- This check should be placed as late as possible.
    if M.errinj.ERRINJ_CFG then
        error('Error injection: cfg')
    end

    --
    -- Sync timeout is a special case - it must be updated before
    -- all other options to allow a user to demote a master with
    -- a new sync timeout.
    --
    local old_sync_timeout = M.sync_timeout
    M.sync_timeout = vshard_cfg.sync_timeout

    if was_master and not is_master then
        local_on_master_disable_prepare()
    end
    if not was_master and is_master then
        local_on_master_enable_prepare()
    end

    if not is_reload then
        -- Do not change 'read_only' option here - if a master is
        -- disabled and there are triggers on master disable, then
        -- they would not be able to modify anything, if 'read_only'
        -- had been set here. 'Read_only' is set in
        -- local_on_master_disable after triggers and is unset in
        -- local_on_master_enable before triggers.
        --
        -- If a master role of the replica is not changed, then
        -- 'read_only' can be set right here.
        box_cfg.listen = box_cfg.listen or this_replica.uri
        if not box_cfg.replication then
            box_cfg.replication = {}
            for uuid, replica in pairs(this_replicaset.replicas) do
                table.insert(box_cfg.replication, replica.uri)
            end
        end
        if was_master == is_master and box_cfg.read_only == nil then
            box_cfg.read_only = not is_master
        end
        if type(box.cfg) == 'function' then
            box_cfg.instance_uuid = box_cfg.instance_uuid or this_replica.uuid
            box_cfg.replicaset_uuid = box_cfg.replicaset_uuid or
                                      this_replicaset.uuid
        else
            local info = box.info
            if this_replica_uuid ~= info.uuid then
                error(string.format('Instance UUID mismatch: already set ' ..
                                    '"%s" but "%s" in arguments', info.uuid,
                                    this_replica_uuid))
            end
            if this_replicaset.uuid ~= info.cluster.uuid then
                error(string.format('Replicaset UUID mismatch: already set ' ..
                                    '"%s" but "%s" in vshard config',
                                    info.cluster.uuid, this_replicaset.uuid))
            end
        end
        local ok, err = pcall(box.cfg, box_cfg)
        while M.errinj.ERRINJ_CFG_DELAY do
            lfiber.sleep(0.01)
        end
        if not ok then
            M.sync_timeout = old_sync_timeout
            if was_master and not is_master then
                local_on_master_disable_abort()
            end
            if not was_master and is_master then
                local_on_master_enable_abort()
            end
            error(err)
        end
        log.info("Box has been configured")
    end

    local uri = luri.parse(this_replica.uri)
    schema_upgrade(is_master, uri.login, uri.password)

    -- Check for master specifically. On master _bucket space must exist.
    -- Because it should have done the schema bootstrap. Shall not ever try to
    -- do anything delayed.
    if is_master or box.space._bucket then
        schema_install_triggers()
    else
        schema_install_triggers_delayed()
    end

    lref.cfg()
    lsched.cfg(vshard_cfg)

    lreplicaset.rebind_replicasets(new_replicasets, M.replicasets)
    lreplicaset.outdate_replicasets(M.replicasets)
    M.replicasets = new_replicasets
    M.this_replicaset = this_replicaset
    M.this_replica = this_replica
    M.total_bucket_count = vshard_cfg.bucket_count
    M.rebalancer_disbalance_threshold =
        vshard_cfg.rebalancer_disbalance_threshold
    M.rebalancer_receiving_quota = vshard_cfg.rebalancer_max_receiving
    M.shard_index = vshard_cfg.shard_index
    M.collect_lua_garbage = vshard_cfg.collect_lua_garbage
    M.rebalancer_worker_count = vshard_cfg.rebalancer_max_sending
    M.current_cfg = cfg

    if was_master and not is_master then
        local_on_master_disable()
    end

    if not was_master and is_master then
        local_on_master_enable()
    end

    if min_master == this_replica then
        if not M.rebalancer_fiber then
            M.rebalancer_fiber =
                util.reloadable_fiber_create('vshard.rebalancer', M,
                                             'rebalancer_f')
        else
            log.info('Wakeup rebalancer')
            -- Configuration had changed. Time to rebalance.
            M.rebalancer_fiber:wakeup()
        end
    elseif M.rebalancer_fiber then
        log.info('Rebalancer location has changed to %s', min_master)
        M.rebalancer_fiber:cancel()
        M.rebalancer_fiber = nil
    end
    lua_gc.set_state(M.collect_lua_garbage, consts.COLLECT_LUA_GARBAGE_INTERVAL)
    M.is_configured = true
    -- Destroy connections, not used in a new configuration.
    collectgarbage()
end

--------------------------------------------------------------------------------
-- Monitoring
--------------------------------------------------------------------------------

local function storage_buckets_info(bucket_id)
    local ibuckets = setmetatable({}, { __serialize = 'mapping' })

    for _, bucket in box.space._bucket:pairs({bucket_id}) do
        local ref = M.bucket_refs[bucket.id]
        local desc = {
            id = bucket.id,
            status = bucket.status,
            destination = bucket.destination,
        }
        if ref then
            if ref.ro ~= 0 then desc.ref_ro = ref.ro end
            if ref.rw ~= 0 then desc.ref_rw = ref.rw end
            if ref.ro_lock then desc.ro_lock = ref.ro_lock end
            if ref.rw_lock then desc.rw_lock = ref.rw_lock end
        end
        ibuckets[bucket.id] = desc
    end

    return ibuckets
end

local function storage_info()
    local state = {
        alerts = {},
        replication = {},
        bucket = {},
        status = consts.STATUS.GREEN,
    }
    local code = lerror.code
    local alert = lerror.alert
    local this_uuid = M.this_replicaset.uuid
    local this_master = M.this_replicaset.master
    if this_master == nil then
        table.insert(state.alerts, alert(code.MISSING_MASTER, this_uuid))
        state.status = math.max(state.status, consts.STATUS.ORANGE)
    end
    if this_master and this_master ~= M.this_replica then
        for id, replica in pairs(box.info.replication) do
            if replica.uuid ~= this_master.uuid then
                goto cont
            end
            state.replication.status = replica.upstream.status
            if replica.upstream.status ~= 'follow' then
                state.replication.idle = replica.upstream.idle
                table.insert(state.alerts, alert(code.UNREACHABLE_MASTER,
                                                 this_uuid,
                                                 replica.upstream.status))
                if replica.upstream.idle > consts.REPLICATION_THRESHOLD_FAIL then
                    state.status = math.max(state.status, consts.STATUS.RED)
                elseif replica.upstream.idle > consts.REPLICATION_THRESHOLD_HARD then
                    state.status = math.max(state.status, consts.STATUS.ORANGE)
                else
                    state.status = math.max(state.status, consts.STATUS.YELLOW)
                end
                goto cont
            end

            state.replication.lag = replica.upstream.lag
            if state.replication.lag >= consts.REPLICATION_THRESHOLD_FAIL then
                table.insert(state.alerts, alert(code.OUT_OF_SYNC))
                state.status = math.max(state.status, consts.STATUS.RED)
            elseif state.replication.lag >= consts.REPLICATION_THRESHOLD_HARD then
                table.insert(state.alerts, alert(code.HIGH_REPLICATION_LAG,
                                                 state.replication.lag))
                state.status = math.max(state.status, consts.STATUS.ORANGE)
            elseif state.replication.lag >= consts.REPLICATION_THRESHOLD_SOFT then
                table.insert(state.alerts, alert(code.HIGH_REPLICATION_LAG,
                                                 state.replication.lag))
                state.status = math.max(state.status, consts.STATUS.YELLOW)
            end
            ::cont::
        end
    elseif this_master then
        state.replication.status = 'master'
        local replica_count = 0
        local not_available_replicas = 0
        for id, replica in pairs(box.info.replication) do
            if replica.uuid ~= M.this_replica.uuid then
                replica_count = replica_count + 1
                if replica.downstream == nil or
                   replica.downstream.vclock == nil then
                    table.insert(state.alerts, alert(code.UNREACHABLE_REPLICA,
                                                     replica.uuid))
                    state.status = math.max(state.status, consts.STATUS.YELLOW)
                    not_available_replicas = not_available_replicas + 1
                end
            end
        end
        local available_replicas = replica_count - not_available_replicas
        if replica_count > 0 and available_replicas == 0 then
            table.insert(state.alerts, alert(code.UNREACHABLE_REPLICASET,
                                             this_uuid))
            state.status = math.max(state.status, consts.STATUS.RED)
        elseif replica_count > 1 and available_replicas == 1 then
            table.insert(state.alerts, alert(code.LOW_REDUNDANCY))
            state.status = math.max(state.status, consts.STATUS.ORANGE)
        end
    else
        state.replication.status = 'slave'
    end

    if is_this_replicaset_locked() then
        state.bucket.lock = true
    end
    local status = box.space._bucket.index.status
    local pinned = status:count({consts.BUCKET.PINNED})
    state.bucket.total = box.space._bucket:count()
    state.bucket.active = status:count({consts.BUCKET.ACTIVE}) + pinned
    state.bucket.garbage = status:count({consts.BUCKET.SENT})
    state.bucket.receiving = status:count({consts.BUCKET.RECEIVING})
    state.bucket.sending = status:count({consts.BUCKET.SENDING})
    state.bucket.pinned = pinned
    if state.bucket.receiving ~= 0 and state.bucket.sending ~= 0 then
        --
        --Some buckets are receiving and some buckets are sending at same time,
        --this may be a balancer issue, alert it.
        --
        table.insert(state.alerts, alert(lerror.code.INVALID_REBALANCING))
        state.status = math.max(state.status, consts.STATUS.YELLOW)
    end

    local ireplicasets = {}
    for uuid, replicaset in pairs(M.replicasets) do
        local master = replicaset.master
        if not master then
            ireplicasets[uuid] = {uuid = uuid, master = 'missing'}
        else
            local uri = master:safe_uri()
            local conn = master.conn
            ireplicasets[uuid] = {
                uuid = uuid;
                master = {
                    uri = uri, uuid = conn and conn.peer_uuid,
                    state = conn and conn.state, error = conn and conn.error,
                };
            };
        end
    end
    state.replicasets = ireplicasets
    return state
end

--------------------------------------------------------------------------------
-- Public API protection
--------------------------------------------------------------------------------

--
-- Arguments are listed explicitly instead of '...' because the latter does not
-- jit.
--
local function storage_api_call_safe(func, arg1, arg2, arg3, arg4)
    return func(arg1, arg2, arg3, arg4)
end

--
-- Unsafe proxy is loaded with protections. But it is used rarely and only in
-- the beginning of instance's lifetime.
--
local function storage_api_call_unsafe(func, arg1, arg2, arg3, arg4)
    -- box.info is quite expensive. Avoid calling it again when the instance
    -- is finally loaded.
    if not M.is_loaded then
        if type(box.cfg) == 'function' then
            local msg = 'box seems not to be configured'
            return error(lerror.vshard(lerror.code.STORAGE_IS_DISABLED, msg))
        end
        local status = box.info.status
        -- 'Orphan' is allowed because even if a replica is an orphan, it still
        -- could be up to date. Just not all other replicas are connected.
        if status ~= 'running' and status ~= 'orphan' then
            local msg = ('instance status is "%s"'):format(status)
            return error(lerror.vshard(lerror.code.STORAGE_IS_DISABLED, msg))
        end
        M.is_loaded = true
    end
    if not M.is_configured then
        local msg = 'storage is not configured'
        return error(lerror.vshard(lerror.code.STORAGE_IS_DISABLED, msg))
    end
    if not M.is_enabled then
        local msg = 'storage is disabled explicitly'
        return error(lerror.vshard(lerror.code.STORAGE_IS_DISABLED, msg))
    end
    M.api_call_cache = storage_api_call_safe
    return func(arg1, arg2, arg3, arg4)
end

M.api_call_cache = storage_api_call_unsafe

local function storage_make_api(func)
    return function(arg1, arg2, arg3, arg4)
        return M.api_call_cache(func, arg1, arg2, arg3, arg4)
    end
end

local function storage_enable()
    M.is_enabled = true
end

--
-- Disable can be used in case the storage entered a critical state in which
-- requests are not allowed. For instance, its config got broken or too old
-- compared to a centric config somewhere.
--
local function storage_disable()
    M.is_enabled = false
    M.api_call_cache = storage_api_call_unsafe
end

--------------------------------------------------------------------------------
-- Module definition
--------------------------------------------------------------------------------
--
-- There are 3 function types:
-- 1) public functions, returned by require('module') and are not
--    used inside module;
-- 2) functions, used inside or/and outside of module;
-- 3) infinite functions for background fibers.
--
-- To update these functions hot reload can be used, when lua code
-- is updated with no process restart.
-- Reload can be successful (an interpreter received 'return'
-- command) or unsuccessful (fail before 'return').
-- If a reload has been failed, then no functions must be updated.
-- The module must continue work like there was no reload. To
-- provide this atomicity, some functions must be saved in module
-- attributes. The main task is to determine, which functions
-- is enough to store in the module members to reach atomic
-- reload.
--
-- Functions of type 1 can be omitted in module members, because
-- they already are updated only on success reload by definition
-- (reload is success, if an interpreter came to the 'return'
-- command).
--
-- Functions of type 2 can be omitted, because outside of a module
-- they are updated only in a case of successful reload, and
-- inside of the module they are used only inside functions of the
-- type 3.
--
-- Functions of type 3 MUST be saved in a module attributes,
-- because they actually contain pointers to all other functions.
--
-- For example:
--
-- local function func1()
--     ...
-- end
--
-- local function func2()
--     ...
-- end
--
-- local function background_f()
--     while module_version == M.module_version do
--         func1()
--         func2()
--    end
-- end
-- M.background_f = background_f
--
-- On the first module load background_f is created with pointers
-- to func1 and func2. After the module is reloaded (or failed to
-- reload) func1 and func2 are updated, but background_f still
-- uses old func1 and func2 until it stops.
--
-- If the module reload was unsuccessful, then background_f is not
-- restarted (or is restarted from M.background_f, which is not
-- changed) and continues use old func1 and func2.
--

if not rawget(_G, MODULE_INTERNALS) then
    rawset(_G, MODULE_INTERNALS, M)
else
    reload_evolution.upgrade(M)
    if M.current_cfg then
        storage_cfg(M.current_cfg, M.this_replica.uuid, true)
    end
    M.module_version = M.module_version + 1
    -- Background fibers could sleep waiting for bucket changes.
    -- Let them know it is time to reload.
    bucket_generation_increment()
end

M.recovery_f = recovery_f
M.rebalancer_f = rebalancer_f
M.gc_bucket_f = gc_bucket_f

--
-- These functions are saved in M not for atomic reload, but for
-- unit testing.
--
M.gc_bucket_drop = gc_bucket_drop
M.rebalancer_build_routes = rebalancer_build_routes
M.rebalancer_calculate_metrics = rebalancer_calculate_metrics
M.cached_find_sharded_spaces = find_sharded_spaces
M.route_dispenser = {
    create = route_dispenser_create,
    put = route_dispenser_put,
    throttle = route_dispenser_throttle,
    skip = route_dispenser_skip,
    pop = route_dispenser_pop,
    sent = route_dispenser_sent,
}
M.schema_latest_version = schema_latest_version
M.schema_current_version = schema_current_version
M.schema_upgrade_master = schema_upgrade_master
M.schema_upgrade_handlers = schema_upgrade_handlers
M.schema_version_make = schema_version_make
M.schema_bootstrap = schema_init_0_1_15_0

M.bucket_are_all_rw = bucket_are_all_rw_public
M.bucket_generation_wait = bucket_generation_wait
lregistry.storage = M

--
-- Not all methods are public here. Private methods should not be exposed if
-- possible. At least not without notable difference in naming.
--
return {
    --
    -- Bucket methods.
    --
    bucket_force_create = storage_make_api(bucket_force_create),
    bucket_force_drop = storage_make_api(bucket_force_drop),
    bucket_collect = storage_make_api(bucket_collect),
    bucket_recv = storage_make_api(bucket_recv),
    bucket_send = storage_make_api(bucket_send),
    bucket_stat = storage_make_api(bucket_stat),
    bucket_pin = storage_make_api(bucket_pin),
    bucket_unpin = storage_make_api(bucket_unpin),
    bucket_ref = storage_make_api(bucket_ref),
    bucket_unref = storage_make_api(bucket_unref),
    bucket_refro = storage_make_api(bucket_refro),
    bucket_refrw = storage_make_api(bucket_refrw),
    bucket_unrefro = storage_make_api(bucket_unrefro),
    bucket_unrefrw = storage_make_api(bucket_unrefrw),
    bucket_delete_garbage = storage_make_api(bucket_delete_garbage),
    _bucket_delete_garbage = bucket_delete_garbage,
    buckets_info = storage_make_api(storage_buckets_info),
    buckets_count = storage_make_api(bucket_count_public),
    buckets_discovery = storage_make_api(buckets_discovery),
    --
    -- Garbage collector.
    --
    garbage_collector_wakeup = storage_make_api(garbage_collector_wakeup),
    --
    -- Rebalancer.
    --
    rebalancer_wakeup = storage_make_api(rebalancer_wakeup),
    rebalancer_apply_routes = storage_make_api(rebalancer_apply_routes),
    rebalancer_disable = storage_make_api(rebalancer_disable),
    rebalancer_enable = storage_make_api(rebalancer_enable),
    rebalancing_is_in_progress = storage_make_api(rebalancing_is_in_progress),
    rebalancer_request_state = storage_make_api(rebalancer_request_state),
    _rebalancer_request_state = rebalancer_request_state,
    --
    -- Recovery.
    --
    recovery_wakeup = storage_make_api(recovery_wakeup),
    --
    -- Instance info.
    --
    is_locked = storage_make_api(is_this_replicaset_locked),
    info = storage_make_api(storage_info),
    sharded_spaces = storage_make_api(storage_sharded_spaces),
    _sharded_spaces = storage_sharded_spaces,
    module_version = function() return M.module_version end,
    --
    -- Miscellaneous.
    --
    call = storage_make_api(storage_call),
    _call = storage_make_api(service_call),
    sync = storage_make_api(sync),
    cfg = function(cfg, uuid) return storage_cfg(cfg, uuid, false) end,
    on_master_enable = storage_make_api(on_master_enable),
    on_master_disable = storage_make_api(on_master_disable),
    enable = storage_enable,
    disable = storage_disable,
    internal = M,
}
end
end

do
local _ENV = _ENV
package.preload[ "vshard.storage.ref" ] = function( ... ) local arg = _G.arg;
--
-- 'Ref' module helps to ensure that all buckets on the storage stay writable
-- while there is at least one ref on the storage.
-- Having storage referenced allows to execute any kinds of requests on all the
-- visible data in all spaces in locally stored buckets. This is useful when
-- need to access tons of buckets at once, especially when exact bucket IDs are
-- not known.
--
-- Refs have deadlines. So as the storage wouldn't freeze not being able to move
-- buckets until restart in case a ref is not deleted due to an error in user's
-- code or disconnect.
--
-- The disconnects and restarts mean the refs can't be global. Otherwise any
-- kinds of global counters, uuids and so on, even paired with any ids from a
-- client could clash between clients on their reconnects or storage restarts.
-- Unless they establish a TCP-like session, which would be too complicated.
--
-- Instead, the refs are spread over the existing box sessions. This allows to
-- bind refs of each client to its TCP connection and not care about how to make
-- them unique across all sessions, how not to mess the refs on restart, and how
-- to drop the refs when a client disconnects.
--

local MODULE_INTERNALS = '__module_vshard_storage_ref'
-- Update when change behaviour of anything in the file, to be able to reload.
local MODULE_VERSION = 1

local lfiber = require('fiber')
local lheap = require('vshard.heap')
local lerror = require('vshard.error')
local lconsts = require('vshard.consts')
local lregistry = require('vshard.registry')
local fiber_clock = lfiber.clock
local fiber_yield = lfiber.yield
local DEADLINE_INFINITY = lconsts.DEADLINE_INFINITY
local LUA_CHUNK_SIZE = lconsts.LUA_CHUNK_SIZE

--
-- Binary heap sort. Object with the closest deadline should be on top.
--
local function heap_min_deadline_cmp(ref1, ref2)
    return ref1.deadline < ref2.deadline
end

local M = rawget(_G, MODULE_INTERNALS)
if not M then
    M = {
        module_version = MODULE_VERSION,
        -- Total number of references in all sessions.
        count = 0,
        -- Heap of session objects. Each session has refs sorted by their
        -- deadline. The sessions themselves are also sorted by deadlines.
        -- Session deadline is defined as the closest deadline of all its refs.
        -- Or infinity in case there are no refs in it.
        session_heap = lheap.new(heap_min_deadline_cmp),
        -- Map of session objects. This is used to get session object by its ID.
        session_map = {},
        -- On session disconnect trigger to kill the dead sessions. It is saved
        -- here for the sake of future reload to be able to delete the old
        -- on disconnect function before setting a new one.
        on_disconnect = nil,
    }
else
    -- No reload so far. This is a first version. Return as is.
    return M
end

local function ref_session_new(sid)
    -- Session object does not store its internal hot attributes in a table.
    -- Because it would mean access to any session attribute would cost at least
    -- one table indexing operation. Instead, all internal fields are stored as
    -- upvalues referenced by the methods defined as closures.
    --
    -- This means session creation may not very suitable for jitting, but it is
    -- very rare and attempts to optimize the most common case.
    --
    -- Still the public functions take 'self' object to make it look normally.
    -- They even use it a bit.

    -- Ref map to get ref object by its ID.
    local ref_map = {}
    -- Ref heap sorted by their deadlines.
    local ref_heap = lheap.new(heap_min_deadline_cmp)
    -- Total number of refs of the session. Is used to drop the session when it
    -- it is disconnected. Heap size can't be used because not all refs are
    -- stored here.
    local ref_count_total = 0
    -- Number of refs in use. They are included into the total count. The used
    -- refs are accounted explicitly in order to detect when a disconnected
    -- session has no used refs anymore and can be deleted.
    local ref_count_use = 0
    -- When the session becomes disconnected, it must be deleted from the global
    -- heap when all its used refs are gone.
    local is_disconnected = false
    -- Cache global session storages as upvalues to save on M indexing.
    local global_heap = M.session_heap
    local global_map = M.session_map
    local sched = lregistry.storage_sched

    local function ref_session_discount(self, del_count)
        local new_count = M.count - del_count
        assert(new_count >= 0)
        M.count = new_count

        new_count = ref_count_total - del_count
        assert(new_count >= 0)
        ref_count_total = new_count

        sched.ref_end(del_count)
    end

    local function ref_session_delete_if_not_used(self)
        if not is_disconnected or ref_count_use > 0 then
            return
        end
        ref_session_discount(self, ref_count_total)
        global_map[sid] = nil
        global_heap:remove(self)
    end

    local function ref_session_update_deadline(self)
        local ref = ref_heap:top()
        if not ref then
            self.deadline = DEADLINE_INFINITY
            global_heap:update(self)
        else
            local deadline = ref.deadline
            if deadline ~= self.deadline then
                self.deadline = deadline
                global_heap:update(self)
            end
        end
    end

    --
    -- Garbage collect at most 2 expired refs. The idea is that there is no a
    -- dedicated fiber for expired refs collection. It would be too expensive to
    -- wakeup a fiber on each added or removed or updated ref.
    --
    -- Instead, ref GC is mostly incremental and works by the principle "remove
    -- more than add". On each new ref added, two old refs try to expire. This
    -- way refs don't stack infinitely, and the expired refs are eventually
    -- removed. Because removal is faster than addition: -2 for each +1.
    --
    local function ref_session_gc_step(self, now)
        -- This is inlined 2 iterations of the more general GC procedure. The
        -- latter is not called in order to save on not having a loop,
        -- additional branches and variables.
        if self.deadline > now then
            return
        end
        local top = ref_heap:pop()
        ref_map[top.id] = nil
        top = ref_heap:top()
        if not top then
            self.deadline = DEADLINE_INFINITY
            global_heap:update(self)
            ref_session_discount(self, 1)
            return
        end
        local deadline = top.deadline
        if deadline >= now then
            self.deadline = deadline
            global_heap:update(self)
            ref_session_discount(self, 1)
            return
        end
        ref_heap:remove_top()
        ref_map[top.id] = nil
        top = ref_heap:top()
        if not top then
            self.deadline = DEADLINE_INFINITY
        else
            self.deadline = top.deadline
        end
        global_heap:update(self)
        ref_session_discount(self, 2)
    end

    --
    -- GC expired refs until they end or the limit on the number of iterations
    -- is exhausted. The limit is supposed to prevent too long GC which would
    -- occupy TX thread unfairly.
    --
    -- Returns nil if nothing to GC, or number of iterations left from the
    -- limit. The caller is supposed to yield when 0 is returned, and retry GC
    -- until it returns nil.
    -- The function itself does not yield, because it is used from a more
    -- generic function GCing all sessions. It would not ever yield if all
    -- sessions would have less than limit refs, even if total ref count would
    -- be much bigger.
    --
    -- Besides, the session might be killed during general GC. There must not be
    -- any yields in session methods so as not to introduce a support of dead
    -- sessions.
    --
    local function ref_session_gc(self, limit, now)
        if self.deadline >= now then
            return nil
        end
        local top = ref_heap:top()
        local del = 1
        local rest = 0
        local deadline
        repeat
            ref_heap:remove_top()
            ref_map[top.id] = nil
            top = ref_heap:top()
            if not top then
                self.deadline = DEADLINE_INFINITY
                rest = limit - del
                break
            end
            deadline = top.deadline
            if deadline >= now then
                self.deadline = deadline
                rest = limit - del
                break
            end
            del = del + 1
        until del >= limit
        ref_session_discount(self, del)
        global_heap:update(self)
        return rest
    end

    local function ref_session_add(self, rid, deadline, now)
        if ref_map[rid] then
            return nil, lerror.vshard(lerror.code.STORAGE_REF_ADD,
                    'duplicate ref')
        end
        local ref = {
            deadline = deadline,
            id = rid,
            -- Used by the heap.
            index = -1,
        }
        ref_session_gc_step(self, now)
        ref_map[rid] = ref
        ref_heap:push(ref)
        if deadline < self.deadline then
            self.deadline = deadline
            global_heap:update(self)
        end
        ref_count_total = ref_count_total + 1
        M.count = M.count + 1
        return true
    end

    --
    -- Ref use means it can't be expired until deleted explicitly. Should be
    -- done when the request affecting the whole storage starts. After use it is
    -- important to call del afterwards - GC won't delete it automatically now.
    -- Unless the entire session is killed.
    --
    local function ref_session_use(self, rid)
        local ref = ref_map[rid]
        if not ref then
            return nil, lerror.vshard(lerror.code.STORAGE_REF_USE, 'no ref')
        end
        ref_heap:remove(ref)
        ref_session_update_deadline(self)
        ref_count_use = ref_count_use + 1
        return true
    end

    local function ref_session_del(self, rid)
        local ref = ref_map[rid]
        if not ref then
            return nil, lerror.vshard(lerror.code.STORAGE_REF_DEL, 'no ref')
        end
        ref_map[rid] = nil
        if ref.index == -1 then
            ref_session_update_deadline(self)
            ref_session_discount(self, 1)
            ref_count_use = ref_count_use - 1
            ref_session_delete_if_not_used(self)
        else
            ref_heap:remove(ref)
            ref_session_update_deadline(self)
            ref_session_discount(self, 1)
        end
        return true
    end

    local function ref_session_kill(self)
        assert(not is_disconnected)
        is_disconnected = true
        ref_session_delete_if_not_used(self)
    end

    -- Don't use __index. It is useless since all sessions use closures as
    -- methods. Also it is probably slower because on each method call would
    -- need to get the metatable, get __index, find the method here. While now
    -- it is only an index operation on the session object.
    local session = {
        deadline = DEADLINE_INFINITY,
        -- Used by the heap.
        index = -1,
        -- Methods.
        del = ref_session_del,
        gc = ref_session_gc,
        add = ref_session_add,
        use = ref_session_use,
        kill = ref_session_kill,
    }
    global_map[sid] = session
    global_heap:push(session)
    return session
end

local function ref_gc()
    local session_heap = M.session_heap
    local session = session_heap:top()
    if not session then
        return
    end
    local limit = LUA_CHUNK_SIZE
    local now = fiber_clock()
    repeat
        limit = session:gc(limit, now)
        if not limit then
            return
        end
        if limit == 0 then
            fiber_yield()
            limit = LUA_CHUNK_SIZE
            now = fiber_clock()
        end
        session = session_heap:top()
    until not session
end

local function ref_add(rid, sid, timeout)
    local now = fiber_clock()
    local deadline = now + timeout
    local ok, err, session
    local storage = lregistry.storage
    local sched = lregistry.storage_sched

    timeout, err = sched.ref_start(timeout)
    if not timeout then
        return nil, err
    end

    while not storage.bucket_are_all_rw() do
        ok, err = storage.bucket_generation_wait(timeout)
        if not ok then
            goto fail_sched
        end
        now = fiber_clock()
        timeout = deadline - now
    end
    session = M.session_map[sid]
    if not session then
        session = ref_session_new(sid)
    end
    ok, err = session:add(rid, deadline, now)
    if ok then
        return true
    end
    :: fail_sched ::
    sched.ref_end(1)
    return nil, err
end

local function ref_use(rid, sid)
    local session = M.session_map[sid]
    if not session then
        return nil, lerror.vshard(lerror.code.STORAGE_REF_USE, 'no session')
    end
    return session:use(rid)
end

local function ref_del(rid, sid)
    local session = M.session_map[sid]
    if not session then
        return nil, lerror.vshard(lerror.code.STORAGE_REF_DEL, 'no session')
    end
    return session:del(rid)
end

local function ref_next_deadline()
    local session = M.session_heap:top()
    return session and session.deadline or DEADLINE_INFINITY
end

local function ref_kill_session(sid)
    local session = M.session_map[sid]
    if session then
        session:kill()
    end
end

local function ref_on_session_disconnect()
    ref_kill_session(box.session.id())
end

local function ref_cfg()
    if M.on_disconnect then
        pcall(box.session.on_disconnect, nil, M.on_disconnect)
    end
    box.session.on_disconnect(ref_on_session_disconnect)
    M.on_disconnect = ref_on_session_disconnect
end

M.del = ref_del
M.gc = ref_gc
M.add = ref_add
M.use = ref_use
M.cfg = ref_cfg
M.kill = ref_kill_session
M.next_deadline = ref_next_deadline
lregistry.storage_ref = M

return M
end
end

do
local _ENV = _ENV
package.preload[ "vshard.storage.reload_evolution" ] = function( ... ) local arg = _G.arg;
--
-- This module is used to upgrade the vshard.storage on the fly.
-- It updates internal Lua structures in case they are changed
-- in a commit.
--
local log = require('log')
local fiber = require('fiber')

--
-- Array of upgrade functions.
-- migrations[version] = function which upgrades module version
-- from `version` to `version + 1`.
--
local migrations = {}

-- Initialize reload_upgrade mechanism
migrations[#migrations + 1] = function(M)
    -- Code to update Lua objects.
end

migrations[#migrations + 1] = function(M)
    local bucket = box.space._bucket
    if bucket then
        assert(M.bucket_on_replace == nil)
        M.bucket_on_replace = bucket:on_replace()[1]
    end
end

migrations[#migrations + 1] = function(M)
    if not M.route_map then
        M.bucket_generation_cond = fiber.cond()
        M.route_map = {}
    end
end

--
-- Perform an update based on a version stored in `M` (internals).
-- @param M Old module internals which should be updated.
--
local function upgrade(M)
    local start_version = M.reload_version or 1
    if start_version > #migrations then
        local err_msg = string.format(
            'vshard.storage.reload_evolution: ' ..
            'auto-downgrade is not implemented; ' ..
            'loaded version is %d, upgrade script version is %d',
            start_version, #migrations
        )
        log.error(err_msg)
        error(err_msg)
    end
    for i = start_version + 1, #migrations do
        local ok, err = pcall(migrations[i], M)
        if ok then
            log.info('vshard.storage.reload_evolution: upgraded to %d version',
                     i)
        else
            local err_msg = string.format(
                'vshard.storage.reload_evolution: ' ..
                'error during upgrade to %d version: %s', i, err
            )
            log.error(err_msg)
            error(err_msg)
        end
        -- Update the version just after upgrade to have an
        -- actual version in case of an error.
        M.reload_version = i
    end
end

return {
    version = #migrations,
    upgrade = upgrade,
}
end
end

do
local _ENV = _ENV
package.preload[ "vshard.storage.sched" ] = function( ... ) local arg = _G.arg;
--
-- Scheduler module ensures fair time sharing between incompatible operations:
-- storage refs and bucket moves.
-- Storage ref is supposed to prevent all bucket moves and provide safe
-- environment for all kinds of possible requests on entire dataset of all
-- spaces stored on the instance.
-- Bucket move, on the contrary, wants to make a part of the dataset not usable
-- temporary.
-- Without a scheduler it would be possible to always keep at least one ref on
-- the storage and block bucket moves forever. Or vice versa - during
-- rebalancing block all incoming refs for the entire time of data migration,
-- essentially making map-reduce not usable since it heavily depends on refs.
--
-- The schedule divides storage time between refs and moves so both of them can
-- execute without blocking each other. Division proportions depend on the
-- configuration settings.
--
-- Idea of non-blockage is based on quotas and strikes. Move and ref both have
-- quotas. When one op executes more than quota requests in a row (makes a
-- strike) while the other op has queued requests, the first op stops accepting
-- new requests until the other op executes.
--

local MODULE_INTERNALS = '__module_vshard_storage_sched'
-- Update when change behaviour of anything in the file, to be able to reload.
local MODULE_VERSION = 1

local lfiber = require('fiber')
local lerror = require('vshard.error')
local lconsts = require('vshard.consts')
local lregistry = require('vshard.registry')
local lutil = require('vshard.util')
local fiber_clock = lfiber.clock
local fiber_cond_wait = lutil.fiber_cond_wait
local fiber_is_self_canceled = lutil.fiber_is_self_canceled

local M = rawget(_G, MODULE_INTERNALS)
if not M then
    M = {
        ---------------- Common module attributes ----------------
        module_version = MODULE_VERSION,
        -- Scheduler condition is signaled every time anything significant
        -- happens - count of an operation type drops to 0, or quota increased,
        -- etc.
        cond = lfiber.cond(),

        -------------------------- Refs --------------------------
        -- Number of ref requests waiting for start.
        ref_queue = 0,
        -- Number of ref requests being executed. It is the same as ref's module
        -- counter, but is duplicated here for the sake of isolation and
        -- symmetry with moves.
        ref_count = 0,
        -- Number of ref requests executed in a row. When becomes bigger than
        -- quota, any next queued move blocks new refs.
        ref_strike = 0,
        ref_quota = lconsts.DEFAULT_SCHED_REF_QUOTA,

        ------------------------- Moves --------------------------
        -- Number of move requests waiting for start.
        move_queue = 0,
        -- Number of move requests being executed.
        move_count = 0,
        -- Number of move requests executed in a row. When becomes bigger than
        -- quota, any next queued ref blocks new moves.
        move_strike = 0,
        move_quota = lconsts.DEFAULT_SCHED_MOVE_QUOTA,
    }
else
    return M
end

local function sched_wait_anything(timeout)
    return fiber_cond_wait(M.cond, timeout)
end

--
-- Return the remaining timeout in case there was a yield. This helps to save
-- current clock get in the caller code if there were no yields.
--
local function sched_ref_start(timeout)
    local deadline, ok, err
    -- Fast-path. Moves are extremely rare. No need to inc-dec the ref queue
    -- then nor try to start some loops.
    if M.move_count == 0 and M.move_queue == 0 then
        goto success
    end
    deadline = fiber_clock() + timeout

    M.ref_queue = M.ref_queue + 1

::retry::
    if M.move_count > 0 then
        goto wait_and_retry
    end
    -- Even if move count is zero, must ensure the time usage is fair. Does not
    -- matter in case the moves have no quota at all. That allows to ignore them
    -- infinitely until all refs end voluntarily.
    if M.move_queue > 0 and M.ref_strike >= M.ref_quota and
       M.move_quota > 0 then
        goto wait_and_retry
    end

    M.ref_queue = M.ref_queue - 1

::success::
    M.ref_count = M.ref_count + 1
    M.ref_strike = M.ref_strike + 1
    M.move_strike = 0
    do return timeout end

::wait_and_retry::
    ok, err = sched_wait_anything(timeout)
    if not ok then
        M.ref_queue = M.ref_queue - 1
        return nil, err
    end
    timeout = deadline - fiber_clock()
    goto retry
end

local function sched_ref_end(count)
    count = M.ref_count - count
    M.ref_count = count
    if count == 0 and M.move_queue > 0 then
        M.cond:broadcast()
    end
end

--
-- Return the remaining timeout in case there was a yield. This helps to save
-- current clock get in the caller code if there were no yields.
--
local function sched_move_start(timeout)
    local ok, err, deadline, ref_deadline
    local lref = lregistry.storage_ref
    -- Fast-path. Refs are not extremely rare *when used*. But they are not
    -- expected to be used in a lot of installations. So most of the times the
    -- moves should work right away.
    if M.ref_count == 0 and M.ref_queue == 0 then
        goto success
    end
    deadline = fiber_clock() + timeout

    M.move_queue = M.move_queue + 1

::retry::
    if M.ref_count > 0 then
        ref_deadline = lref.next_deadline()
        if ref_deadline < deadline then
            timeout = ref_deadline - fiber_clock()
        end
        ok, err = sched_wait_anything(timeout)
        timeout = deadline - fiber_clock()
        if ok then
            goto retry
        end
        if fiber_is_self_canceled() then
            goto fail
        end
        -- Even if the timeout has expired already (or was 0 from the
        -- beginning), it is still possible the move can be started if all the
        -- present refs are expired too and can be collected.
        lref.gc()
        -- GC could yield - need to refetch the clock again.
        timeout = deadline - fiber_clock()
        if M.ref_count > 0 then
            if timeout < 0 then
                goto fail
            end
            goto retry
        end
    end

    if M.ref_queue > 0 and M.move_strike >= M.move_quota and
       M.ref_quota > 0 then
        ok, err = sched_wait_anything(timeout)
        if not ok then
            goto fail
        end
        timeout = deadline - fiber_clock()
        goto retry
    end

    M.move_queue = M.move_queue - 1

::success::
    M.move_count = M.move_count + 1
    M.move_strike = M.move_strike + 1
    M.ref_strike = 0
    do return timeout end

::fail::
    M.move_queue = M.move_queue - 1
    return nil, err
end

local function sched_move_end(count)
    count = M.move_count - count
    M.move_count = count
    if count == 0 and M.ref_queue > 0 then
        M.cond:broadcast()
    end
end

local function sched_cfg(cfg)
    local new_ref_quota = cfg.sched_ref_quota
    local new_move_quota = cfg.sched_move_quota

    if new_ref_quota then
        if new_ref_quota > M.ref_quota then
            M.cond:broadcast()
        end
        M.ref_quota = new_ref_quota
    end
    if new_move_quota then
        if new_move_quota > M.move_quota then
            M.cond:broadcast()
        end
        M.move_quota = new_move_quota
    end
end

M.ref_start = sched_ref_start
M.ref_end = sched_ref_end
M.move_start = sched_move_start
M.move_end = sched_move_end
M.cfg = sched_cfg
lregistry.storage_sched = M

return M
end
end

do
local _ENV = _ENV
package.preload[ "vshard.util" ] = function( ... ) local arg = _G.arg;
-- vshard.util
local log = require('log')
local fiber = require('fiber')
local lerror = require('vshard.error')

local MODULE_INTERNALS = '__module_vshard_util'
local M = rawget(_G, MODULE_INTERNALS)
if not M then
    --
    -- The module is loaded for the first time.
    --
    M = {
        -- Latest versions of functions.
        reloadable_fiber_main_loop = nil,
    }
    rawset(_G, MODULE_INTERNALS, M)
end

local version_str = _TARANTOOL:sub(1, _TARANTOOL:find('-')-1)
local dot = version_str:find('%.')
local major = tonumber(version_str:sub(1, dot - 1))
version_str = version_str:sub(dot + 1)
dot = version_str:find('%.')
local middle = tonumber(version_str:sub(1, dot - 1))
local minor = tonumber(version_str:sub(dot + 1))

--
-- Extract parts of a tuple.
-- @param tuple Tuple to extract a key from.
-- @param parts Array of index parts. Each part must contain
--        'fieldno' attribute.
--
-- @retval Extracted key.
--
local function tuple_extract_key(tuple, parts)
    local key = {}
    for _, part in ipairs(parts) do
        table.insert(key, tuple[part.fieldno])
    end
    return key
end

--
-- Wrapper to run a func in infinite loop and restart it on
-- errors and module reload.
-- This loop executes the latest version of itself in case of
-- reload of that module.
-- See description of parameters in `reloadable_fiber_create`.
--
local function reloadable_fiber_main_loop(module, func_name, data)
    log.info('%s has been started', func_name)
    local func = module[func_name]
::restart_loop::
    local ok, err = pcall(func, data)
    -- yield serves two purposes:
    --  * makes this fiber cancellable
    --  * prevents 100% cpu consumption
    fiber.yield()
    if not ok then
        log.error('%s has been failed: %s', func_name, err)
        if func == module[func_name] then
            goto restart_loop
        end
        -- There is a chance that error was raised during reload
        -- (or caused by reload). Perform reload in case function
        -- has been changed.
        log.error('reloadable function %s has been changed', func_name)
    end
    log.info('module is reloaded, restarting')
    -- luajit drops this frame if next function is called in
    -- return statement.
    return M.reloadable_fiber_main_loop(module, func_name, data)
end

--
-- Create a new fiber which runs a function in a loop. This loop
-- is aware of reload mechanism and it loads a new version of the
-- function in that case.
-- To handle module reload and run new version of a function
-- in the module, the function should just return.
-- @param fiber_name Name of a new fiber. E.g.
--        "vshard.rebalancer".
-- @param module Module which can be reloaded.
-- @param func_name Name of a function to be executed in the
--        module.
-- @param data Data to be passed to the specified function.
-- @retval New fiber.
--
local function reloadable_fiber_create(fiber_name, module, func_name, data)
    assert(type(fiber_name) == 'string')
    local xfiber = fiber.create(reloadable_fiber_main_loop, module, func_name,
                                data)
    xfiber:name(fiber_name, {truncate = true})
    return xfiber
end

--
-- Wrap a given function to check that self argument is passed.
-- New function returns an error in case one called a method
-- like object.func instead of object:func.
-- Returning wrapped function to a user and using raw functions
-- inside of a module improves speed.
-- This function can be used only in case the second argument is
-- not of the "table" type or has different metatable.
-- @param obj_name Name of the called object. Used only for error
--        message construction.
-- @param func_name Name of the called function. Used only for an
--        error message construction.
-- @param mt Meta table of self argument.
-- @param func A function which is about to be wrapped.
--
-- @retval Wrapped function.
--
local function generate_self_checker(obj_name, func_name, mt, func)
    return function (self, ...)
        if getmetatable(self) ~= mt then
            local fmt = 'Use %s:%s(...) instead of %s.%s(...)'
            error(string.format(fmt, obj_name, func_name, obj_name, func_name))
        end
        return func(self, ...)
    end
end

-- Update latest versions of function
M.reloadable_fiber_main_loop = reloadable_fiber_main_loop

local function sync_task(delay, task, ...)
    if delay then
        fiber.sleep(delay)
    end
    task(...)
end

--
-- Run a function without interrupting current fiber.
-- @param delay Delay in seconds before the task should be
--        executed.
-- @param task Function to be executed.
-- @param ... Arguments which would be passed to the `task`.
--
local function async_task(delay, task, ...)
    assert(delay == nil or type(delay) == 'number')
    fiber.create(sync_task, delay, task, ...)
end

--
-- Check if Tarantool version is >= that a specified one.
--
local function version_is_at_least(major_need, middle_need, minor_need)
    if major > major_need then return true end
    if major < major_need then return false end
    if middle > middle_need then return true end
    if middle < middle_need then return false end
    return minor >= minor_need
end

--
-- Copy @a src table. Fiber yields every @a interval keys copied. Does not give
-- any guarantees on what is the result when the source table is changed during
-- yield.
--
local function table_copy_yield(src, interval)
    local res = {}
    -- Time-To-Yield.
    local tty = interval
    for k, v in pairs(src) do
        res[k] = v
        tty = tty - 1
        if tty <= 0 then
            fiber.yield()
            tty = interval
        end
    end
    return res
end

--
-- Remove @a src keys from @a dst if their values match. Fiber yields every
-- @a interval iterations. Does not give any guarantees on what is the result
-- when the source table is changed during yield.
--
local function table_minus_yield(dst, src, interval)
    -- Time-To-Yield.
    local tty = interval
    for k, srcv in pairs(src) do
        if dst[k] == srcv then
            dst[k] = nil
        end
        tty = tty - 1
        if tty <= 0 then
            fiber.yield()
            tty = interval
        end
    end
    return dst
end

local function fiber_cond_wait_xc(cond, timeout)
    -- Handle negative timeout specifically - otherwise wait() will throw an
    -- ugly usage error.
    -- Don't trust this check to the caller's code, because often it just calls
    -- wait many times until it fails or the condition is met. Code looks much
    -- cleaner when it does not need to check the timeout sign. On the other
    -- hand, perf is not important here - anyway wait() yields which is slow on
    -- its own, but also breaks JIT trace recording which makes pcall() in the
    -- non-xc version of this function inconsiderable.
    if timeout < 0 or not cond:wait(timeout) then
        -- Don't use the original error if cond sets it. Because it sets
        -- TimedOut error. It does not have a proper error code, and may not be
        -- detected by router as a special timeout case if necessary. Or at
        -- least would complicate the handling in future. Instead, try to use a
        -- unified timeout error where possible.
        error(lerror.timeout())
    end
    -- Still possible though that the fiber is canceled and cond:wait() throws.
    -- This is why the _xc() version of this function throws even the timeout -
    -- anyway pcall() is inevitable.
end

--
-- Exception-safe cond wait with unified errors in vshard format.
--
local function fiber_cond_wait(cond, timeout)
    local ok, err = pcall(fiber_cond_wait_xc, cond, timeout)
    if ok then
        return true
    end
    return nil, lerror.make(err)
end

--
-- Exception-safe way to check if the current fiber is canceled.
--
local function fiber_is_self_canceled()
    return not pcall(fiber.testcancel)
end

return {
    tuple_extract_key = tuple_extract_key,
    reloadable_fiber_create = reloadable_fiber_create,
    generate_self_checker = generate_self_checker,
    async_task = async_task,
    internal = M,
    version_is_at_least = version_is_at_least,
    table_copy_yield = table_copy_yield,
    table_minus_yield = table_minus_yield,
    fiber_cond_wait = fiber_cond_wait,
    fiber_is_self_canceled = fiber_is_self_canceled,
}
end
end

