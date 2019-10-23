import {orange, purple} from "@material-ui/core/colors";

export const SLASH = '/';

export const MAIN_COMPONENT = 'mainComponent';

export const HOST = window.location.hostname;
export const BASE_URL = window.location.href.split('?')[0];

export const PRIMARY_MAIN_COLOR = purple["800"];
export const SECONDARY_MAIN_COLOR = orange["700"];

export enum ThemeMode {
    DARK = 'dark',
    LIGHT = 'light'
}