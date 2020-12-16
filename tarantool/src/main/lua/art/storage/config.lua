local config = {
    schemaPostfix = '_schema',

    mappingSpacePostfix = '_mapping_updates',

    mapping = {
        defaultBatchSize = 1024,
        maxBatchesToSend = 100,

        watcher = {
            timeout = 0.1, --watcher sleep time
            watchdogTimeout = 1
        },

        builder = {
            batchSize = 1024 * 10,
            timeout = 0.1,
            watchdogTimeout = 1
        },

        networkManager = {
            timeout = 0.2
        },

        garbageCollector = {
            timeout = 0.2,
            watchdogTimeout = 1
        }
    },

    space = {
        autoCancelTimeout = 5
    }
}

return config