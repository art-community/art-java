--
--ART module for tarantool boxes
--

    art_svc = {
        clusterwide = {
            execute = function(operation, args)
                local connections = art_svc.clusterwide.get_all_masters()
                local results = {}

                results = art_svc.clusterwide.check_availability(connections, operation, args)
                if (not art_svc.clusterwide.check_results(results))
                then
                    art_svc.clusterwide.cancel_all(connections, operation, args)
                    return {false, results}
                else
                    results = art_svc.clusterwide.commit_all(connections, operation, args)
                    return {art_svc.clusterwide.check_results(results), results}
                end
                return
            end,

            check_availability = function(connections, operation, args)
                local results = {}
                for k,v in pairs(connections) do
                    results[k] = art_svc.clusterwide.remote_call(v,
                        'art_svc.vshard.space.check_op_availability', {operation, args})
                end
                return results
            end,

            cancel_all = function(connections)
                local results = {}
                for k,v in pairs(connections) do
                    results[k] = art_svc.clusterwide.remote_call(v, 'art_svc.vshard.space.cancel_op')
                end
                return results
            end,

            commit_all = function(connections, operation, args)
                local results = {}
                for k,v in pairs(connections) do
                    results[k] = art_svc.clusterwide.remote_call(v,
                        'art_svc.vshard.space.execute_op', {operation, args})
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
                for k,v in pairs(results) do
                    is_ok = (is_ok and v[1])
                end
                return is_ok
            end
        }
    }

    art = {
        get = function(space, request)
            return vshard.router.callro(request[2], 'art.get', {space, request[1]})
        end,

        delete = function(space, key)
            return vshard.router.callrw(key[2], 'art.delete', {space, key[1]})
        end,

        insert = function(space, data)
            return vshard.router.callrw(data[1][2], 'art.insert', {space, data, data[1][2]})
        end,

        auto_increment = function(space, data)
            return vshard.router.callrw(data[1][2], 'art.auto_increment', {space, data, data[1][2]})
        end,

        put = function(space, data)
            return vshard.router.callrw(data[1][2], 'art.put', {space, data, data[1][2]})
        end,

        update = function(space, key, commands)
            return vshard.router.callrw(key[2], 'art.update', {space, key[1], commands, key[2]})
        end,

        replace = function(space, data)
            return vshard.router.callrw(data[1][2], 'art.replace', {space, data, data[1][2]})
        end,

        upsert = function(space, data, commands)
            return vshard.router.callrw(data[1][2], 'art.upsert', {space, data, commands, data[1][2]})
        end,

        select = '',

        space = {
            create = function(name, config)
                return art_svc.clusterwide.execute('create_vsharded', {name, config})
            end,

            format = function(space, format)
                return art_svc.clusterwide.execute('format', {space, format})
            end,

            create_index = function(space, index_name, index)
                return art_svc.clusterwide.execute('create_index', {space, index_name, index})
            end,

            rename = function(space, new_name)
                return art_svc.clusterwide.execute('rename', {space, new_name})
            end,

            truncate = function(space)
                return art_svc.clusterwide.execute('truncate', {space})
            end,

            drop = function(space)
                return art_svc.clusterwide.execute('drop', {space})
            end,

            count = function(space)
                local counts = art_svc.clusterwide.execute('count', {space})
                local result = 0
                if (not counts[1]) then return counts end
                for k,v in pairs(counts[2]) do
                    result = result + v[2]
                end
                return result
            end,

            schema_count = function(space)
                local counts = art_svc.clusterwide.execute('schema_count', {space})
                local result = 0
                if (not counts[1]) then return counts end
                for k,v in pairs(counts[2]) do
                    result = result + v[2]
                end
                return result
            end,

            len = function(space)
                local counts = art_svc.clusterwide.execute('len', {space})
                local result = 0
                if (not counts[1]) then return counts end
                for k,v in pairs(counts[2]) do
                    result = result + v[2]
                end
                return result
            end,

            schema_len = function(space)
                local counts = art_svc.clusterwide.execute('schema_len', {space})
                local result = 0
                if (not counts[1]) then return counts end
                for k,v in pairs(counts[2]) do
                    result = result + v[2]
                end
                return result
            end
        }
    }

return art
