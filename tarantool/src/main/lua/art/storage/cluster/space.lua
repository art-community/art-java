local space = {
    cancellerFiber = nil,

    checkOperationAvailability = function(operation, args)
        art.box.space.waitForClusterOperation()
        art.box.space.activeClusterOperation = true
        art.cluster.space.cancellerFiber = art.core.fiber.create(art.cluster.space.autoCanceller)
        local result = {}
        box.begin()
        result[1], result[2] = pcall(art.box.space[operation], unpack(args))
        box.rollback()
        return result
    end,

    autoCanceller = function()
        art.core.fiber.sleep(art.config.space.autoCancelTimeout)
        art.box.space.activeClusterOperation = false
        art.cluster.space.cancellerFiber = nil
    end,

    cancelOperation = function()
        art.box.space.activeClusterOperation = false
        art.cluster.space.cancellerFiber:cancel()
        art.cluster.space.cancellerFiber = nil
    end,

    executeOperation = function(operation, args)
        local result = {}
        box.begin()
        result[1], result[2] = pcall(art.box.space[operation], unpack(args))
        if (result[1])
        then
            box.commit()
        else
            box.rollback()
        end
        art.box.space.activeClusterOperation = false
        art.cluster.space.cancellerFiber:cancel()
        art.cluster.space.cancellerFiber = nil
        return result
    end,
}

return space