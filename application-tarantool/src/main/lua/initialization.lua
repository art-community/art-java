function loadModule(moduleId)
    local module = package.loaded[moduleId]
    if module ~= nil then
        package.loaded[moduleId] = nil
    end

    require(moduleId)
end
loadModule('lua.configuration')
loadModule('lua.user')
