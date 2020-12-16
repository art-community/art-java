local space = {
    execute = function(operation, args)
        local connections = art.cluster.space.getAllMasters()
        local results = {}

        results = art.cluster.space.checkAvailability(connections, operation, args)
        if (not art.cluster.space.checkResults(results))
        then
            art.cluster.space.cancelAll(connections, operation, args)
            return {false, results}
        else
            results = art.cluster.space.commitAll(connections, operation, args)
            return { art.cluster.space.checkResults(results), results}
        end
        return
    end,

    checkAvailability = function(connections, operation, args)
        local results = {}
        for k,v in pairs(connections) do
            results[k] = art.cluster.space.remoteCall(v,
                    'art.cluster.space.checkOperationAvailability', {operation, args})
        end
        return results
    end,

    cancelAll = function(connections)
        local results = {}
        for k,v in pairs(connections) do
            results[k] = art.cluster.space.remoteCall(v, 'art.cluster.space.cancelOperation')
        end
        return results
    end,

    commitAll = function(connections, operation, args)
        local results = {}
        for k,v in pairs(connections) do
            results[k] = art.cluster.space.remoteCall(v,
                    'art.cluster.space.executeOperation', {operation, args})
        end
        return results
    end,

    getAllMasters = function()
        local routes = vshard.router.routeall()
        local results = {}
        for k,v in pairs(routes) do
            results[k] = v.master.conn
        end
        return results
    end,

    remoteCall = function(peer, functionName, args)
        return peer:call(functionName, args)
    end,

    checkResults = function(results)
        local is_ok = true
        for _,v in pairs(results) do
            is_ok = (is_ok and v[1])
        end
        return is_ok
    end
}

return space