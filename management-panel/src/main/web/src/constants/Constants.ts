import {orange, purple} from "@material-ui/core/colors";

export const SLASH = '/';
export const LOGIN_PATH = '/login';
export const REGISTER_PATH = '/register';
export const BUILD_PATH = '/build';
export const PROJECT_PATH = '/project';
export const MAIN_COMPONENT = 'mainComponent';
export const PRIMARY_MAIN_COLOR = purple["800"];
export const SECONDARY_MAIN_COLOR = orange["700"];
export const PLATFORM_PATH = '/platform';
export const HOST = window.location.hostname;
export const RSOCKET_PORT = 9001;
export const RSOCKET_URL = `ws://${HOST}:${RSOCKET_PORT}`;

export const RSOCKET_FUNCTION = 'RSOCKET_FUNCTION_SERVICE';
export const REGISTER_USER = 'registerUser';
export const GET_USER = 'getUser';

export const USER_STORE = 'user';
export const TOKEN_COOKIE = 'TOKEN';

export enum ThemeMode {
    DARK = 'dark',
    LIGHT = 'light'
}

export const RSOCKET_OPTIONS = {
    dataMimeType: 'application/message-pack',
    metadataMimeType: 'application/message-pack',
    keepAlive: 1000000,
    lifetime: 100000
};

