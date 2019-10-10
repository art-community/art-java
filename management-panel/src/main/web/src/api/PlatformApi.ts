import {AUTHENTICATE, AUTHORIZE, REGISTER_USER} from "../constants/Constants";
import {createMethodRequest, executeRequest} from "./PlatformClient";

export const registerUser = (requestData: UserRegistrationRequest, onComplete: (user: UserRegistrationResponse) => void) => {
    executeRequest(createMethodRequest(REGISTER_USER, requestData))
        .then(response => onComplete(response));
};

export const authorize = (requestData: UserAuthorizationRequest, onComplete: (user: UserAuthorizationResponse) => void, onError: () => void) => {
    executeRequest(createMethodRequest(AUTHORIZE, requestData))
        .then(response => onComplete(response))
        .catch(() => onError());
};

export const authenticate = (requestData: string, onComplete: () => void, onError: () => void) => {
    executeRequest(createMethodRequest(AUTHENTICATE, requestData)).then(() => onComplete()).catch(() => onError());
};
