import {GET_INFORMATION_PATH} from "../constants/Constants";

export const getInformation = (onResponse: (response: InformationResponse) => void) => {
    fetch(GET_INFORMATION_PATH)
        .then(response => response.text())
        .then(JSON.parse)
        .then(onResponse)
        .catch(console.error);
};