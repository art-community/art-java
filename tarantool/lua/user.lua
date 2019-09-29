box.schema.user.create('username', {password = 'password', if_not_exists = true})
box.schema.user.grant('username', 'read,write,execute,create,alter,drop', 'universe', nil, {if_not_exists=true})