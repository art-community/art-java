art = {
    core = require('art.core'),

    config = require('art.storage.config'),

    box = require('art.storage.box'),

    cluster = require('art.storage.cluster'),

    transaction = require('art.transaction'),

    api = require('art.storage.api')
}

art.cluster.mapping.init(mapping_uri)
return art
