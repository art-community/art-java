box.schema.user.create('user', {password = 'password', if_not_exists = true})
box.schema.user.grant('user', 'read,write,execute,create,alter,drop', 'universe', nil, {if_not_exists=true})