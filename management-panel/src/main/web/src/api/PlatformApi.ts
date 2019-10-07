import {GET_USER, REGISTER_USER} from "../constants/Constants";
import {createMethodRequest, executeRequest} from "./PlatformClient";

export const registerUser = async (requestData: UserRegistrationRequest, onComplete: (user: User) => void) => {
    await executeRequest(createMethodRequest(REGISTER_USER, requestData), onComplete);
};

export const getUser = async (requestData: UserRequest, onComplete: (user: User) => void) => {
    await executeRequest(createMethodRequest(GET_USER, requestData), onComplete);
};