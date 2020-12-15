local space = {
    canceller_fiber = nil,

    check_op_availability = function(operation, args)
        art.box.space.wait_for_clustered_op()
        art.box.space.cluster_op_in_progress = true
        art.cluster.space.canceller_fiber = art.core.fiber.create(art.cluster.space.auto_canceller)
        local result = {}
        box.begin()
        result[1], result[2] = pcall(art.box.space[operation], unpack(args))
        box.rollback()
        return result
    end,

    auto_canceller = function()
        art.core.fiber.sleep(art.config.space.auto_cancel_timeout)
        art.box.space.cluster_op_in_progress = false
        art.cluster.space.canceller_fiber = nil
    end,

    cancel_op = function()
        art.box.space.cluster_op_in_progress = false
        art.cluster.space.canceller_fiber:cancel()
        art.cluster.space.canceller_fiber = nil
    end,

    execute_op = function(operation, args)
        local result = {}
        box.begin()
        result[1], result[2] = pcall(art.box.space[operation], unpack(args))
        if (result[1])
        then
            box.commit()
        else
            box.rollback()
        end
        art.box.space.cluster_op_in_progress = false
        art.cluster.space.canceller_fiber:cancel()
        art.cluster.space.canceller_fiber = nil
        return result
    end,
}

return space