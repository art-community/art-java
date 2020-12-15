art = {
    core = {
        fiber = require('fiber'),
    },

    cluster = require('art.router.cluster'),

    api = require('art.router.api')
}

art.cluster.mapping.init()
return art
