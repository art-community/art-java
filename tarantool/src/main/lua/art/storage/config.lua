local config = {
    schemaPostfix = '_schema',

    mappingSpacePostfix = '_mapping_updates',

    mapping = {
        defaultBatchSize = 1024,
        maxBatchesToSend = 100,

        watcher = {
            timeout = 0.02, --watcher sleep time
            watchdogTimeout = 1
        },

        builder = {
            batchSize = 1024 * 10,
            timeout = 0.02,
            watchdogTimeout = 1
        },

        networkManager = {
            timeout = 0.1
        },

        garbageCollector = {
            timeout = 0.5,
            watchdogTimeout = 1
        }
    },

    space = {
        autoCancelTimeout = 5
    }
}

return config