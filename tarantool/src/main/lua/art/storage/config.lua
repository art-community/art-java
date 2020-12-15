local config = {
    schema_postfix = '_schema',

    mapping_space_postfix = '_mapping_updates',

    mapping = {
        default_batch_size = 1024,
        max_batches_to_send = 100,

        watcher = {
            timeout = 0.1, --watcher sleep time
            watchdog_timeout = 1
        },

        builder = {
            batch_size = 1024 * 10,
            timeout = 0.1,
            watchdog_timeout = 1
        },

        network_manager = {
            timeout = 0.2
        },

        garbage_collector = {
            timeout = 0.2,
            watchdog_timeout = 1
        }
    },

    space = {
        auto_cancel_timeout = 5
    }
}

return config