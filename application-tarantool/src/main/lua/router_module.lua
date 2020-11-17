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
        },
        mapping = {
            create = function(space, config)
                box.schema.space.create(space)
                art_svc.mapping.format(space, config['format'])
                box.space[space]:create_index('art_mapping_primary', {parts = {1}})
            end,

            format = function(space, format)
                if(format) then
                    for k,v in pairs(format) do
                        v['is_nullable'] = true
                    end
                    box.space[space]:format(format)
                end
            end,

            create_index = function(space, index_name, index)
                local parts = index.parts
                for k,v in pairs(parts) do
                    if not (type(v) == 'table') then v = {v} end
                    v['is_nullable'] = true
                end
                index.parts = parts
                box.space[space]:create_index(index_name, index)
                return
            end,

            rename = function(space, new_name)
                box.space[space]:rename(new_name)
            end,

            truncate = function(space)
                box.space[space]:truncate()
            end,

            drop = function(space)
                box.space[space]:drop()
            end,

            put = function(space, data)

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
                local result = art_svc.clusterwide.execute('create_vsharded', {name, config})
                if (result[1]) then
                    art_svc.mapping.create(name, config)
                end
                return result
            end,

            format = function(space, format)
                local result = art_svc.clusterwide.execute('format', {space, format})
                if (result[1]) then
                    art_svc.mapping.format(space, format)
                end
            end,

            create_index = function(space, index_name, index)
                local result = art_svc.clusterwide.execute('create_index', {space, index_name, index})
                if (result[1]) then
                    art_svc.mapping.create_index(space, index_name, index)
                end
                return result
            end,

            rename = function(space, new_name)
                local result = art_svc.clusterwide.execute('rename', {space, new_name})
                if (result[1]) then
                    art_svc.mapping.rename(space, new_name)
                end
                return result
            end,

            truncate = function(space)
                local result = art_svc.clusterwide.execute('truncate', {space})
                if (result[1]) then
                    art_svc.mapping.truncate(space)
                end
                return result
            end,

            drop = function(space)
                local result = art_svc.clusterwide.execute('drop', {space})
                if (result[1]) then
                    art_svc.mapping.drop(space)
                end
                return result
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
    } -- public API

return art
