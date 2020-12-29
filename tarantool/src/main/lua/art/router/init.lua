art = {
    core = require('art.core'),

    config = require('art.router.config'),

    cluster = require('art.router.cluster'),

    transaction = require('art.router.transaction'),

    api = require('art.router.api')
}

art.cluster.mapping.init()
return art
