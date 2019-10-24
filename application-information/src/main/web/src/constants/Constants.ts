import {orange, purple} from "@material-ui/core/colors";

export const SLASH = '/';

export const MAIN_COMPONENT = 'mainComponent';

const moduleIdMeta = document.querySelector("meta[name='moduleId']");

export const MODULE_ID = moduleIdMeta ? moduleIdMeta.getAttribute("content") : "";

export const INFORMATION_PATH = '/information';
export const API_PATH = '/api';
export const WEB_UI_PATH = '/ui';

export const HOST = window.location.hostname;

export const BASE_URL = window.location.href.split(WEB_UI_PATH)[0];

export const GET_INFORMATION_PATH = "http://localhost:10000/module/information/api/get";

export const PRIMARY_MAIN_COLOR = purple["800"];
export const SECONDARY_MAIN_COLOR = orange["700"];

export enum ThemeMode {
    DARK = 'dark',
    LIGHT = 'light'
}