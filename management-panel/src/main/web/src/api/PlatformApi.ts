import {AUTHENTICATE, AUTHORIZE, REGISTER_USER} from "../constants/Constants";
import {createMethodRequest, executeRequest} from "./PlatformClient";

export const registerUser = async (requestData: UserRegistrationRequest, onComplete: (user: UserRegistrationResponse) => void) => {
    await executeRequest(createMethodRequest(REGISTER_USER, requestData), onComplete);
};

export const authorize = async (requestData: UserAuthorizationRequest, onComplete: (user: UserAuthorizationResponse) => void, onError: () => void) => {
    await executeRequest(createMethodRequest(AUTHORIZE, requestData), onComplete, onError);
};

export const authenticate = async (requestData: string, onComplete: () => void, onError: () => void) => {
    await executeRequest(createMethodRequest(AUTHENTICATE, requestData), onComplete, onError);
};
