local services = {
    builder = {
        pending_spaces = {},
        service_fibers = {},
        watchdog_fiber = nil,

        start = function()
            art.cluster.mapping.builder.watchdog_fiber = art.core.fiber.create(art.cluster.mapping.builder.watchdog)
        end,

        build = function(space)
            if not (box.space[space].index.bucket_id) then return end
            table.insert(art.cluster.mapping.builder.pending_spaces, space)
        end,

        watchdog = function()
            while true do
                for k,space in pairs(art.cluster.mapping.builder.pending_spaces) do
                    art.cluster.mapping.builder.service_fibers[space] = art.core.fiber.create(art.cluster.mapping.builder.service, space)
                    art.cluster.mapping.builder.pending_spaces[k] = nil
                end
                art.core.fiber.sleep(art.config.mapping.builder.watchdog_timeout)
            end
        end,

        service = function(space)
            local counter = 0
            for _,v in box.space[space]:pairs() do
                art.cluster.mapping.put(space, v)
                counter = counter + 1
                if (counter == art.config.mapping.builder.batch_size) then
                    art.core.fiber.sleep(art.config.mapping.builder.timeout)
                    counter = 0
                end
            end
            art.cluster.mapping.builder.service_fibers[space] = nil
        end
    },

    watcher = {
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
                art.core.fiber.sleep(art.config.mapping.watcher.watchdog_timeout)

            end
        end,

        service = function()
            local batches = {}
            local prev_iteration_batches_count = 0
            local spaces
            local min_timestamp = 0xffffffffffffffffULL --max unsigned int64
            local is_sent

            while(true) do
                spaces = box.space._mapping_watched_spaces:select()

                for _, v in pairs(spaces) do
                    local batch = art.cluster.mapping.watcher.collect_updates(v)
                    if (batch) then
                        if (batch[1][1] < min_timestamp) then min_timestamp = batch[1][1] end
                        table.insert(batches, {v.space, batch})
                    end
                end

                if (#batches >= art.config.mapping.max_batches_to_send) or (#batches == prev_iteration_batches_count) then
                    is_sent = false
                    while (not is_sent) do
                        is_sent = pcall(art.cluster.mapping.watcher.send, batches)
                        art.core.fiber.sleep(art.config.mapping.watcher.timeout)
                    end

                    batches = {}
                    if (min_timestamp < 0xffffffffffffffffULL) then
                        art.cluster.mapping.last_upload_min_timestamp = min_timestamp
                        min_timestamp = 0xffffffffffffffffULL
                    end
                    art.core.fiber.sleep(art.config.mapping.watcher.timeout)
                end

                prev_iteration_batches_count = #batches
            end
        end,

        collect_updates = function(watched_space)
            if not (art.cluster.mapping.last_collected_timestamps[watched_space.space]) then
                art.cluster.mapping.last_collected_timestamps[watched_space.space] = art.cluster.mapping.last_upload_min_timestamp
            end

            local batch = {}
            for _,v in art.core.mapping_updates_of(watched_space.space).index.timestamp:pairs(
                    art.cluster.mapping.last_collected_timestamps[watched_space.space], 'GT') do
                table.insert(batch, v)
                if #batch == watched_space.batch_size then break end
            end

            if batch[1] then
                art.cluster.mapping.last_collected_timestamps[watched_space.space] = batch[#batch][1]
                return batch
            end
        end,

        send = function(batches)
            if not(batches[1]) then return end
            art.cluster.mapping.nodes[art.cluster.mapping.primary_node_uuid]:call(
                    'art.cluster.mapping.save_to_pending', {batches})
        end
    },

    garbage_collector = {
        service_fiber = nil,
        watchdog_fiber = nil,

        start = function()
            art.cluster.mapping.garbage_collector.service_fiber = art.core.fiber.create(art.cluster.mapping.garbage_collector.service)
            art.cluster.mapping.garbage_collector.watchdog_fiber = art.core.fiber.create(art.cluster.mapping.garbage_collector.watchdog)
        end,

        watchdog = function()
            while(true) do
                if (art.core.fiber.status(art.cluster.mapping.garbage_collector.service_fiber) == 'dead') then
                    art.cluster.mapping.garbage_collector.service_fiber = art.core.fiber.create(art.cluster.mapping.garbage_collector.service)
                end
                art.core.fiber.sleep(art.config.mapping.garbage_collector.watchdog_timeout)

            end
        end,

        service = function()
            while true do
                local watched_spaces = box.space._mapping_watched_spaces:select()
                for _, v in pairs(watched_spaces) do
                    art.cluster.mapping.garbage_collector.cleanup_space(v.space)
                end
                art.core.fiber.sleep(art.config.mapping.garbage_collector.timeout)

            end
        end,

        cleanup_space = function(space)
            for _, v in art.core.mapping_updates_of(space).index.timestamp:pairs(art.cluster.mapping.last_upload_min_timestamp, 'LT') do
                art.core.mapping_updates_of(space):delete(v:transform(1, 3))
            end
        end
    },

    network_manager = {
        start = function()
            art.cluster.mapping.network_manager.watchdog_fiber = art.core.fiber.create(art.cluster.mapping.network_manager.watchdog)
        end,

        watchdog_fiber = nil,

        watchdog = function()
            while(true) do
                for uri, connection in pairs(art.cluster.mapping.nodes) do
                    if ( (not(connection)) or (not connection:is_connected()) ) then art.cluster.mapping.network_manager.connect(uri) end
                end
                art.core.fiber.sleep(art.config.mapping.network_manager.timeout)
            end
        end,

        connect = function(uri)
            art.cluster.mapping.nodes[uri] = require('net.box').connect(uri)
        end,

    }
}

return services