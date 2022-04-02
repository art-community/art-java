return function()
    box.schema.user.create('username', { password = 'password', if_not_exists = true })
    box.schema.user.grant('username', 'read,write,execute,create,alter,drop', 'universe', nil, { if_not_exists = true })

    testChannel = function()
        box.session.push("test")
        box.session.push("test")
    end
    box.schema.func.create("testChannel", { if_not_exists = true })

    testMapper = function(data)
        return data[16] .. " - mapped"
    end
    box.schema.func.create("testMapper", { if_not_exists = true })

    testFilter = function(data)
        return data[9] > 3
    end
    box.schema.func.create("testFilter", { if_not_exists = true })
end
