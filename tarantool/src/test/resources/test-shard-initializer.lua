return function()
    box.once("main", function()
        testChannel = function()
            box.session.push("test")
            box.session.push("test")
        end

        testMapper = function(data)
            return data[16] .. " - mapped"
        end

        testFilter = function(data)
            return data[9] > 3
        end

        wait = function()
            vshard.storage.sync(30)
        end

        if not box.cfg.read_only then
            box.schema.user.create('username', { password = 'password', if_not_exists = true })
            box.schema.user.grant('username', 'read,write,execute,create,alter,drop', 'universe', nil, { if_not_exists = true })
            box.schema.func.create("testChannel", { if_not_exists = true })
            box.schema.func.create("testMapper", { if_not_exists = true })
            box.schema.func.create("testFilter", { if_not_exists = true })
            box.schema.func.create("wait", { if_not_exists = true })
        end
    end)
end
