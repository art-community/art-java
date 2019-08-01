package ru.adk.configurator.service;

import ru.adk.configurator.api.entity.UserRequest;
import ru.adk.configurator.api.entity.UserResponse;
import static ru.adk.configurator.dao.UserDao.*;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;

public interface UserService {
    static UserResponse login(UserRequest userRequest) {
        if (!userExists(userRequest.getName(), userRequest.getPassword())) return new UserResponse(false);
        return new UserResponse(true, getToken());
    }

    static void register(String userName, String password) {
        saveUser(userName, password);
    }

    static boolean checkToken(String requestToken) {
        String token = getToken();
        if (isEmpty(token)) return false;
        return token.equals(requestToken);
    }
}
