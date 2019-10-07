import {AUTHORIZE, GET_USER, REGISTER_USER} from "../constants/Constants";
import {createMethodRequest, executeRequest} from "./PlatformClient";

export const registerUser = async (requestData: UserRegistrationRequest, onComplete: (user: User) => void) => {
    await executeRequest(createMethodRequest(REGISTER_USER, requestData), onComplete);
};

export const authorize = async (requestData: UserAuthorizationRequest, onComplete: (token: String) => void) => {
    await executeRequest(createMethodRequest(AUTHORIZE, requestData), onComplete);
};

export const getUser = async (requestData: String, onComplete: (user: User | null) => void, onError: () => void) => {
    await executeRequest(createMethodRequest(GET_USER, requestData), onComplete, onError);
};