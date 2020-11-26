--
--ART module for tarantool storages
--

    art = {
        core = {
            fiber = require('fiber'),
        },

        cluster = {
            space_ops = {
                execute = function(operation, args)
                    local connections = art.cluster.space_ops.get_all_masters()
                    local results = {}

                    results = art.cluster.space_ops.check_availability(connections, operation, args)
                    if (not art.cluster.space_ops.check_results(results))
                    then
                        art.cluster.space_ops.cancel_all(connections, operation, args)
                        return {false, results}
                    else
                        results = art.cluster.space_ops.commit_all(connections, operation, args)
                        return { art.cluster.space_ops.check_results(results), results}
                    end
                    return
                end,

                check_availability = function(connections, operation, args)
                    local results = {}
                    for k,v in pairs(connections) do
                        results[k] = art.cluster.space_ops.remote_call(v,
                                'art.cluster.space_ops.check_op_availability', {operation, args})
                    end
                    return results
                end,

                cancel_all = function(connections)
                    local results = {}
                    for k,v in pairs(connections) do
                        results[k] = art.cluster.space_ops.remote_call(v, 'art.cluster.space_ops.cancel_op')
                    end
                    return results
                end,

                commit_all = function(connections, operation, args)
                    local results = {}
                    for k,v in pairs(connections) do
                        results[k] = art.cluster.space_ops.remote_call(v,
                                'art.cluster.space_ops.execute_op', {operation, args})
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
                end,

            },

            mapping = {
                init = function()
                    if not (box.space.mapping_pending_updates) then
                        box.schema.space.create('mapping_pending_updates', {is_local = true})
                        box.space.mapping_pending_updates:format({
                            { name = 'id', type = 'unsigned' },
                            { name = 'space', type = 'string' },
                            { name = 'batch', type = 'any' }
                        })
                        box.schema.sequence.create('mapping_pending_updates_id', {cycle = true})
                        box.space.mapping_pending_updates:create_index('id', {parts = {1}, sequence = box.sequence.mapping_pending_updates_id})
                        box.space.mapping_pending_updates:create_index('space', {parts = {2}, unique = false})
                    end
                    art.cluster.mapping.watcher.start()
                end,

                space = {
                    create = function(space, config)
                        box.schema.space.create(space)
                        art.cluster.mapping.space.format(space, config['format'])
                    end,

                    format = function(space, format)
                        if not (format) then return end
                        if not(box.space[space].index[0]) then
                            box.space[space]:format(format)
                            return
                        end

                        for _, v in pairs(format) do
                            v['is_nullable'] = true
                        end

                        for _, v in pairs(box.space[space].index[0].parts) do
                        format[v.fieldno].is_nullable = false
                        end
                        box.space[space]:format(format)
                        end,

                    create_index = function(space, index_name, index)
                        local parts = index.parts
                        for _, v in pairs(parts) do
                            if not (type(v) == 'table') then
                                v = { v }
                            end
                            v['is_nullable'] = true
                        end
                        index.parts = parts
                        local result = box.space[space]:create_index(index_name, index)
                        if (result.id == 0) then box.space[space]:format(box.space[space]:format()) end
                    end,

                    rename = function(space, new_name)
                        box.space[space]:rename(new_name)
                        art.cluster.mapping.space.rename_space_in_pending(space, new_name)
                    end,

                    truncate = function(space)
                        box.space[space]:truncate()
                        art.cluster.mapping.space.delete_pending_space_updates(space)
                    end,

                    drop = function(space)
                        box.space[space]:drop()
                        art.cluster.mapping.space.delete_pending_space_updates(space)
                    end,

                    delete_pending_space_updates = function(space)
                        for _,v in box.space.mapping_pending_updates.index.space:pairs(space) do
                            box.space.mapping_pending_updates:delete(v[1])
                        end
                    end,

                    rename_space_in_pending = function(space, new_name)
                        for _,v in box.space.mapping_pending_updates.index.space:pairs(space) do
                            box.space.mapping_pending_updates:update(v[1], {{'=', 2, new_name}})
                        end
                    end
                },

                save_to_pending = function(batches)
                    for _, v in pairs(batches) do
                        box.space.mapping_pending_updates:insert({nil, unpack(v)})
                    end
                end,

                watcher = {
                    batches_per_time = 100,
                    timeout = 1,
                    service_fiber = nil,
                    watchdog_fiber = nil,

                    start = function()
                        art.cluster.mapping.watcher.service_fiber = art.core.fiber.create(art.cluster.mapping.watcher.service)
                        art.cluster.mapping.watcher.watchdog_fiber = art.core.fiber.create(art.cluster.mapping.watcher.watchdog)
                    end,

                    watchdog = function()
                        while(true) do
                            if (art.core.fiber.status(art.cluster.mapping.watcher.service_fiber) == 'dead') then
                                art.cluster.mapping.watcher.service_fiber = art.core.fiber.create(art.cluster.mapping.watcher.service)
                            end
                            art.core.fiber.sleep(5)

                        end
                    end,

                    service = function()
                        local counter = 0

                        while true do
                            counter = 0
                            for _,v in box.space.mapping_pending_updates:pairs(box.sequence.mapping_pending_updates_id:current(), 'GT') do
                                art.cluster.mapping.watcher.update_batch(v)
                                counter = counter+1
                                if (counter == art.cluster.mapping.watcher.batches_per_time) then
                                    art.core.fiber.sleep(art.cluster.mapping.watcher.timeout)
                                    counter = 0
                                end
                            end

                            counter = 0
                            for _,v in box.space.mapping_pending_updates:pairs() do
                                art.cluster.mapping.watcher.update_batch(v)
                                counter = counter+1
                                if (counter == art.cluster.mapping.watcher.batches_per_time) then
                                    art.core.fiber.sleep(art.cluster.mapping.watcher.timeout)
                                    counter = 0
                                end
                            end
                            art.core.fiber.sleep(art.cluster.mapping.watcher.timeout)
                        end
                    end,

                    update_batch = function(batch)
                        local space = box.space[batch[2]]
                        if not(space) then box.space.mapping_pending_updates:delete(batch[1]) end
                        for _, v in pairs(batch[3]) do
                            if v[2] then
                                table.remove(v, 1)
                                table.remove(v, 1)
                                table.remove(v, 1)
                                space:delete(v)
                            else
                                space:replace(v[3])
                            end
                        end
                        box.space.mapping_pending_updates:delete(batch[1])
                    end

                }
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
                    local result = art.cluster.space_ops.execute('create_vsharded', { name, config})
                    if (result[1]) then
                        art.cluster.mapping.space.create(name, config)
                    end
                    return result
                end,

                format = function(space, format)
                    local result = art.cluster.space_ops.execute('format', { space, format})
                    if (result[1]) then
                        art.cluster.mapping.space.format(space, format)
                    end
                end,

                create_index = function(space, index_name, index)
                    local result = art.cluster.space_ops.execute('create_index', { space, index_name, index})
                    if (result[1]) then
                        art.cluster.mapping.space.create_index(space, index_name, index)
                    end
                    return result
                end,

                rename = function(space, new_name)
                    local result = art.cluster.space_ops.execute('rename', { space, new_name})
                    if (result[1]) then
                        box.atomic(art.cluster.mapping.space.rename, space, new_name)
                    end
                    return result
                end,

                truncate = function(space)
                    local result = art.cluster.space_ops.execute('truncate', { space})
                    if (result[1]) then
                        box.atomic(art.cluster.mapping.space.truncate, space)
                    end
                    return result
                end,

                drop = function(space)
                    local result = art.cluster.space_ops.execute('drop', { space})
                    if (result[1]) then
                        box.atomic(art.cluster.mapping.space.drop, space)
                    end
                    return result
                end,

                count = function(space)
                    local counts = art.cluster.space_ops.execute('count', { space})
                    local result = 0
                    if (not counts[1]) then return counts end
                    for _,v in pairs(counts[2]) do
                        result = result + v[2]
                    end
                    return result
                end,

                schema_count = function(space)
                    local counts = art.cluster.space_ops.execute('schema_count', { space})
                    local result = 0
                    if (not counts[1]) then return counts end
                    for _,v in pairs(counts[2]) do
                        result = result + v[2]
                    end
                    return result
                end,

                len = function(space)
                    local counts = art.cluster.space_ops.execute('len', { space})
                    local result = 0
                    if (not counts[1]) then return counts end
                    for _,v in pairs(counts[2]) do
                        result = result + v[2]
                    end
                    return result
                end,

                schema_len = function(space)
                    local counts = art.cluster.space_ops.execute('schema_len', { space})
                    local result = 0
                    if (not counts[1]) then return counts end
                    for _,v in pairs(counts[2]) do
                        result = result + v[2]
                    end
                    return result
                end,

                list = function()
                    local list = box.space._space:select()
                    local result = {}
                    for _, v in pairs(list) do
                        if not (string.startswith(v.name, '_') or v.name == 'mapping_pending_updates') then table.insert(result, v.name) end
                    end
                    return result
                end,

                list_indices = function(space)
                    local result = {}
                    for k,v in pairs(box.space[space].index) do
                        result[k] = v.name
                    end
                    result['primary'] = nil
                    return result
                end
            }
        } -- public API
    }



return art
