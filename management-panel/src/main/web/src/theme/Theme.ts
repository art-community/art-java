import {createMuiTheme} from "@material-ui/core";
import {PRIMARY_MAIN_COLOR, SECONDARY_MAIN_COLOR, ThemeMode} from "../constants/Constants";

export const DEFAULT_THEME = createMuiTheme({
    palette: {
        type: ThemeMode.LIGHT,
        primary: {main: PRIMARY_MAIN_COLOR},
        secondary: {main: SECONDARY_MAIN_COLOR}
    },
});