let DEFAULT_URL = "http://localhost:11000/configurator";

export const EMPTY_STRING = "";
export const BASE_URL = module.hot ? DEFAULT_URL : document.head.getElementsByTagName("meta")["baseUrl"].getAttribute("content");
export const CONFIGURATOR_PATH = module.hot ? '/' : '/configurator';
export const IMAGE_URL = `${BASE_URL}/image`;
export const IMAGE_PATH = name => module.hot ? name : `${IMAGE_URL}/${name.substring(name.lastIndexOf("/"))}`;
export const MAIN_COMPONENT = 'mainComponent';
export const TOKEN_COOKIE = 'TOKEN';
export const COOKIE_MAX_AGE = '86400';

export const PRIMARY_COLOR = 'violet';
export const SECONDARY_COLOR = 'orange';