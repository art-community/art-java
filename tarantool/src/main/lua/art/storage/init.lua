art = {
    core = require('art.storage.core'),

    config = require('art.storage.config'),

    box = require('art.storage.box'),

    cluster = require('art.storage.cluster'),

    api = require('art.storage.api')
}

art.cluster.mapping.init(mapping_uri)
return art
