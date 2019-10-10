import {createStore, getStoreByName} from "react-hookstore";

export const putStore = (name: string, store: any) => {
    try {
        if (getStoreByName(name)) {
            return store
        }
        return createStore(name, false);
    } catch (e) {
        return createStore(name, false);
    }
};