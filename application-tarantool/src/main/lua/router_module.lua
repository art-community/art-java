--
--ART module for tarantool boxes
--

    art = {
        cluster = {
            execute = function(operation, args)
                local connections = art.cluster.get_all_masters()
                local results = {}

                results = art.cluster.check_availability(connections, operation, args)
                if (not art.cluster.check_results(results))
                then
                    art.cluster.cancel_all(connections, operation, args)
                    return {false, results}
                else
                    results = art.cluster.commit_all(connections, operation, args)
                    return { art.cluster.check_results(results), results}
                end
                return
            end,

            check_availability = function(connections, operation, args)
                local results = {}
                for k,v in pairs(connections) do
                    results[k] = art.cluster.remote_call(v,
                        'art.cluster.check_op_availability', {operation, args})
                end
                return results
            end,

            cancel_all = function(connections)
                local results = {}
                for k,v in pairs(connections) do
                    results[k] = art.cluster.remote_call(v, 'art.cluster.cancel_op')
                end
                return results
            end,

            commit_all = function(connections, operation, args)
                local results = {}
                for k,v in pairs(connections) do
                    results[k] = art.cluster.remote_call(v,
                        'art.cluster.execute_op', {operation, args})
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
            end,

            mapping = {
                create = function(space, config)
                    box.schema.space.create(space)
                    art.cluster.mapping.format(space, config['format'])
                    box.space[space]:create_index('art_mapping_primary', {parts = {1}})
                end,

                format = function(space, format)
                    if(format) then
                        for k,v in pairs(format) do
                            v['is_nullable'] = true
                        end
                        format[1]['is_nullable'] = false
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
        },

        api = {
            get = function(space, request)
                return vshard.router.callro(request[2], 'art.box.get', {space, request[1]})
            end,

            delete = function(space, key)
                return vshard.router.callrw(key[2], 'art.box.delete', {space, key[1]})
            end,

            insert = function(space, data)
                return vshard.router.callrw(data[1][2], 'art.box.insert', {space, data, data[1][2]})
            end,

            auto_increment = function(space, data)
                return vshard.router.callrw(data[1][2], 'art.box.auto_increment', {space, data, data[1][2]})
            end,

            put = function(space, data)
                return vshard.router.callrw(data[1][2], 'art.box.put', {space, data, data[1][2]})
            end,

            update = function(space, key, commands)
                return vshard.router.callrw(key[2], 'art.box.update', {space, key[1], commands, key[2]})
            end,

            replace = function(space, data)
                return vshard.router.callrw(data[1][2], 'art.box.replace', {space, data, data[1][2]})
            end,

            upsert = function(space, data, commands)
                return vshard.router.callrw(data[1][2], 'art.box.upsert', {space, data, commands, data[1][2]})
            end,

            select = '',

            space = {
                create = function(name, config)
                    local result = art.cluster.execute('create_vsharded', { name, config})
                    if (result[1]) then
                        art.cluster.mapping.create(name, config)
                    end
                    return result
                end,

                format = function(space, format)
                    local result = art.cluster.execute('format', { space, format})
                    if (result[1]) then
                        art.cluster.mapping.format(space, format)
                    end
                end,

                create_index = function(space, index_name, index)
                    local result = art.cluster.execute('create_index', { space, index_name, index})
                    if (result[1]) then
                        art.cluster.mapping.create_index(space, index_name, index)
                    end
                    return result
                end,

                rename = function(space, new_name)
                    local result = art.cluster.execute('rename', { space, new_name})
                    if (result[1]) then
                        art.cluster.mapping.rename(space, new_name)
                    end
                    return result
                end,

                truncate = function(space)
                    local result = art.cluster.execute('truncate', { space})
                    if (result[1]) then
                        art.cluster.mapping.truncate(space)
                    end
                    return result
                end,

                drop = function(space)
                    local result = art.cluster.execute('drop', { space})
                    if (result[1]) then
                        art.cluster.mapping.drop(space)
                    end
                    return result
                end,

                count = function(space)
                    local counts = art.cluster.execute('count', { space})
                    local result = 0
                    if (not counts[1]) then return counts end
                    for k,v in pairs(counts[2]) do
                        result = result + v[2]
                    end
                    return result
                end,

                schema_count = function(space)
                    local counts = art.cluster.execute('schema_count', { space})
                    local result = 0
                    if (not counts[1]) then return counts end
                    for k,v in pairs(counts[2]) do
                        result = result + v[2]
                    end
                    return result
                end,

                len = function(space)
                    local counts = art.cluster.execute('len', { space})
                    local result = 0
                    if (not counts[1]) then return counts end
                    for k,v in pairs(counts[2]) do
                        result = result + v[2]
                    end
                    return result
                end,

                schema_len = function(space)
                    local counts = art.cluster.execute('schema_len', { space})
                    local result = 0
                    if (not counts[1]) then return counts end
                    for k,v in pairs(counts[2]) do
                        result = result + v[2]
                    end
                    return result
                end
            }
        } -- public API
    }



return art
