import * as React from "react";
import {Grid, Typography} from "@material-ui/core";

export const MainComponent = () => {
    return <Grid container direction={"column"}>
        <Grid item>
            <Typography>
                Информация о модуле
            </Typography>
        </Grid>
        <Grid item>
            <Typography>
                HTTP Информация
            </Typography>
        </Grid>
    </Grid>
};