local space = {
    execute = function(operation, args)
        local connections = art.cluster.space.get_all_masters()
        local results = {}

        results = art.cluster.space.check_availability(connections, operation, args)
        if (not art.cluster.space.check_results(results))
        then
            art.cluster.space.cancel_all(connections, operation, args)
            return {false, results}
        else
            results = art.cluster.space.commit_all(connections, operation, args)
            return { art.cluster.space.check_results(results), results}
        end
        return
    end,

    check_availability = function(connections, operation, args)
        local results = {}
        for k,v in pairs(connections) do
            results[k] = art.cluster.space.remote_call(v,
                    'art.cluster.space.check_op_availability', {operation, args})
        end
        return results
    end,

    cancel_all = function(connections)
        local results = {}
        for k,v in pairs(connections) do
            results[k] = art.cluster.space.remote_call(v, 'art.cluster.space.cancel_op')
        end
        return results
    end,

    commit_all = function(connections, operation, args)
        local results = {}
        for k,v in pairs(connections) do
            results[k] = art.cluster.space.remote_call(v,
                    'art.cluster.space.execute_op', {operation, args})
        end
        return results
    end,

    get_all_masters = function()
        local routes = vshard.router.routeall()
        local results = {}
        for k,v in pairs(routes) do
            results[k] = v.master.conn
        end
        return results
    end,

    remote_call = function(peer, func_name, args)
        return peer:call(func_name, args)
    end,

    check_results = function(results)
        local is_ok = true
        for _,v in pairs(results) do
            is_ok = (is_ok and v[1])
        end
        return is_ok
    end
}

return space