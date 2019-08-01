import {ComponentStore} from "../../../../framework/store/ComponentStore";
import remove from 'licia/remove'

export class UsersTabComponentStore extends ComponentStore {
    users = [];

    deleteUser = (deletingUser) => remove(this.users, (user) => user === deletingUser);

    toState = () => ({users: this.users});
}