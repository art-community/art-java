import {orange, purple} from "@material-ui/core/colors";

export const SLASH = '/';
export const PLATFORM_PATH = '/platform';
export const AUTHORIZE_PATH = PLATFORM_PATH + '/authorize';
export const REGISTER_PATH = PLATFORM_PATH + '/register';
export const BUILD_PATH = PLATFORM_PATH + '/build';
export const DEPLOY_PATH = PLATFORM_PATH + '/deploy';
export const PROJECT_PATH = PLATFORM_PATH + '/project';

export const MAIN_COMPONENT = 'mainComponent';

export const RSOCKET_FUNCTION = 'RSOCKET_FUNCTION_SERVICE';
export const REGISTER_USER = 'registerUser';
export const AUTHORIZE = 'authorize';
export const AUTHENTICATE = 'authenticate';

export const AUTHORIZED_STORE = 'authorized';
export const TOKEN_COOKIE = 'token';

export const HOST = window.location.hostname;
export const RSOCKET_PORT = 9001;
export const RSOCKET_URL = `ws://${HOST}:${RSOCKET_PORT}`;

export const RSOCKET_OPTIONS = {
    dataMimeType: 'application/message-pack',
    metadataMimeType: 'application/message-pack',
    keepAlive: 1000000,
    lifetime: 100000
};


export const TECHNOLOGIES = {
    gradle: 'Gradle',
    npm: 'NPM'
};

export const PRIMARY_MAIN_COLOR = purple["800"];
export const SECONDARY_MAIN_COLOR = orange["700"];

export enum ThemeMode {
    DARK = 'dark',
    LIGHT = 'light'
}
