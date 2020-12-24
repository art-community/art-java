--transaction request format: {string function, {arg1, ... argN} }
--arg format: {arg} or {dependency:{prev_response_index}} or {dependency:{prev_response_index, fieldname}}

local transaction = {
    execute = function(transaction)
        local results = {}
        local currentOperationResult = {}

        box.begin()
        for index, request in pairs(transaction) do
            currentOperationResult[1], currentOperationResult[2] = pcall( art.core.functionFromString(request[1]), unpack(art.transaction.insertDependencies(request[2], results)) )
            if not(currentOperationResult[1]) then
                box.rollback()
                return false, currentOperationResult[2]
            end
            results[index] = {currentOperationResult[2]}
        end
        box.commit()

        return true, results
    end,

    insertDependencies = function(args, prevResults)
        local result = {}
        for index, argument in pairs(args) do
            result[index] = argument
            if argument.dependency then result[index] = art.transaction.getDependency(argument.dependency, prevResults) end
        end
        return result
    end,

    getDependency = function(dependencyRecord, prevResults)
        local dependency = prevResults[dependencyRecord[1]][1]
        if dependencyRecord[2] then return art.transaction.extractFieldFromTuple(dependency, dependencyRecord[2]) end
        return dependency
    end,

    extractFieldFromTuple = function(tuple, fieldName)
        local data = tuple[1]
        local schema = tuple[2]
        for index = 2, #schema do
            if schema[index][2] == fieldName then return data[index - 1] end
        end
    end
}

return transaction