return function()
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
end
