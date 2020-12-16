local services = {
    builder = {
        pendingSpaces = {},
        serviceFibers = {},
        watchdogFiber = nil,

        start = function()
            art.cluster.mapping.builder.watchdogFiber = art.core.fiber.create(art.cluster.mapping.builder.watchdog)
        end,

        build = function(space)
            if not (box.space[space].index.bucket_id) then return end
            table.insert(art.cluster.mapping.builder.pendingSpaces, space)
        end,

        watchdog = function()
            while true do
                for k,space in pairs(art.cluster.mapping.builder.pendingSpaces) do
                    art.cluster.mapping.builder.serviceFibers[space] = art.core.fiber.create(art.cluster.mapping.builder.service, space)
                    art.cluster.mapping.builder.pendingSpaces[k] = nil
                end
                art.core.fiber.sleep(art.config.mapping.builder.watchdogTimeout)
            end
        end,

        service = function(space)
            local counter = 0
            for _,v in box.space[space]:pairs() do
                art.cluster.mapping.put(space, v)
                counter = counter + 1
                if (counter == art.config.mapping.builder.batchSize) then
                    art.core.fiber.sleep(art.config.mapping.builder.timeout)
                    counter = 0
                end
            end
            art.cluster.mapping.builder.serviceFibers[space] = nil
        end
    },

    watcher = {
        serviceFiber = nil,
        watchdogFiber = nil,

        start = function()
            art.cluster.mapping.watcher.serviceFiber = art.core.fiber.create(art.cluster.mapping.watcher.service)
            art.cluster.mapping.watcher.watchdogFiber = art.core.fiber.create(art.cluster.mapping.watcher.watchdog)
        end,

        watchdog = function()
            while(true) do
                if (art.core.fiber.status(art.cluster.mapping.watcher.serviceFiber) == 'dead') then
                    art.cluster.mapping.watcher.serviceFiber = art.core.fiber.create(art.cluster.mapping.watcher.service)
                end
                art.core.fiber.sleep(art.config.mapping.watcher.watchdogTimeout)

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
                    local batch = art.cluster.mapping.watcher.collectUpdates(v)
                    if (batch) then
                        if (batch[1][1] < min_timestamp) then min_timestamp = batch[1][1] end
                        table.insert(batches, {v.space, batch})
                    end
                end

                if (#batches >= art.config.mapping.maxBatchesToSend) or (#batches == prev_iteration_batches_count) then
                    is_sent = false
                    while (not is_sent) do
                        is_sent = pcall(art.cluster.mapping.watcher.send, batches)
                        art.core.fiber.sleep(art.config.mapping.watcher.timeout)
                    end

                    batches = {}
                    if (min_timestamp < 0xffffffffffffffffULL) then
                        art.cluster.mapping.lastUploadMinTimestamp = min_timestamp
                        min_timestamp = 0xffffffffffffffffULL
                    end
                    art.core.fiber.sleep(art.config.mapping.watcher.timeout)
                end

                prev_iteration_batches_count = #batches
            end
        end,

        collectUpdates = function(watched_space)
            if not (art.cluster.mapping.lastCollectedTimestamps[watched_space.space]) then
                art.cluster.mapping.lastCollectedTimestamps[watched_space.space] = art.cluster.mapping.lastUploadMinTimestamp
            end

            local batch = {}
            for _,v in art.core.mappingUpdatesOf(watched_space.space).index.timestamp:pairs(
                    art.cluster.mapping.lastCollectedTimestamps[watched_space.space], 'GT') do
                table.insert(batch, v)
                if #batch == watched_space.batchSize then break end
            end

            if batch[1] then
                art.cluster.mapping.lastCollectedTimestamps[watched_space.space] = batch[#batch][1]
                return batch
            end
        end,

        send = function(batches)
            if not(batches[1]) then return end
            art.cluster.mapping.nodes[art.cluster.mapping.primaryNodeUUID]:call(
                    'art.cluster.mapping.saveToPending', {batches})
        end
    },

    garbageCollector = {
        serviceFiber = nil,
        watchdogFiber = nil,

        start = function()
            art.cluster.mapping.garbageCollector.serviceFiber = art.core.fiber.create(art.cluster.mapping.garbageCollector.service)
            art.cluster.mapping.garbageCollector.watchdogFiber = art.core.fiber.create(art.cluster.mapping.garbageCollector.watchdog)
        end,

        watchdog = function()
            while(true) do
                if (art.core.fiber.status(art.cluster.mapping.garbageCollector.serviceFiber) == 'dead') then
                    art.cluster.mapping.garbageCollector.serviceFiber = art.core.fiber.create(art.cluster.mapping.garbageCollector.service)
                end
                art.core.fiber.sleep(art.config.mapping.garbageCollector.watchdogTimeout)

            end
        end,

        service = function()
            while true do
                local watched_spaces = box.space._mapping_watched_spaces:select()
                for _, v in pairs(watched_spaces) do
                    art.cluster.mapping.garbageCollector.cleanupSpace(v.space)
                end
                art.core.fiber.sleep(art.config.mapping.garbageCollector.timeout)

            end
        end,

        cleanupSpace = function(space)
            for _, v in art.core.mappingUpdatesOf(space).index.timestamp:pairs(art.cluster.mapping.lastUploadMinTimestamp, 'LT') do
                art.core.mappingUpdatesOf(space):delete(v:transform(1, 3))
            end
        end
    },

    networkManager = {
        start = function()
            art.cluster.mapping.networkManager.watchdogFiber = art.core.fiber.create(art.cluster.mapping.networkManager.watchdog)
        end,

        watchdogFiber = nil,

        watchdog = function()
            while(true) do
                for uri, connection in pairs(art.cluster.mapping.nodes) do
                    if ( (not(connection)) or (not connection:is_connected()) ) then art.cluster.mapping.networkManager.connect(uri) end
                end
                art.core.fiber.sleep(art.config.mapping.networkManager.timeout)
            end
        end,

        connect = function(uri)
            art.cluster.mapping.nodes[uri] = require('net.box').connect(uri)
        end,

    }
}

return services