import {registry} from '../../../../framework/registry/Registry'
import {loadUsers, saveUsers} from "../../../../service/UserService";

export const reactors = {
    onMount: () => loadUsers((users) => {
        registry.usersTab.store.users = users;
        registry.usersTab.store.apply();
    }),

    onAddUser: () => {
    },

    onDeleteUser: (user) => {
        registry.usersTab.store.deleteUser(user);
        registry.usersTab.store.apply();
    },

    onSave: (users) => saveUsers(users)
};