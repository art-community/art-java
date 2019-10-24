import {orange, purple} from "@material-ui/core/colors";
import {blue500, green700, purple500, red500, red700} from "material-ui/styles/colors";

export const SLASH = '/';

export const MAIN_COMPONENT = 'mainComponent';

const moduleIdMeta = document.querySelector("meta[name='moduleId']");
const hrefAttribute = document.querySelector("base");
const HREF = hrefAttribute ? hrefAttribute.getAttribute("href") : "";

export const MODULE_ID = moduleIdMeta ? moduleIdMeta.getAttribute("content") : "";

export const INFORMATION_WEB_UI_URL = HREF ? HREF : "";

export const GET_INFORMATION_PATH = `${INFORMATION_WEB_UI_URL.split("/ui")[0]}/api/get`;

export const PRIMARY_MAIN_COLOR = purple["800"];
export const SECONDARY_MAIN_COLOR = orange["700"];

export enum ThemeMode {
    DARK = 'dark',
    LIGHT = 'light'
}

export const HTTP_CHIP_STYLE = {
    borderColor: purple500,
    color: purple500
};

export const GRPC_CHIP_STYLE = {
    borderColor: blue500,
    color: blue500
};

export const RSOCKET_CHIP_STYLE = {
    borderColor: red500,
    color: red500
};

export const CHIP_OK_STYLE = {
    borderColor: green700,
    color: green700
};

export const CHIP_NOT_OK_STYLE = {
    borderColor: red700,
    color: red700
};
