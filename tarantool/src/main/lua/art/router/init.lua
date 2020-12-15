art = {
    core = {
        fiber = require('fiber'),
    },

    config = require('art.router.config'),

    cluster = require('art.router.cluster'),

    api = require('art.router.api')
}

art.cluster.mapping.init()
return art
