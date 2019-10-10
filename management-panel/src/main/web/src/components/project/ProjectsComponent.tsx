import * as React from "react";
import {
    Box,
    Container,
    FormControlLabel,
    FormLabel,
    Grid,
    makeStyles,
    Paper,
    Radio,
    RadioGroup
} from "@material-ui/core";
import {useState} from "react";

const useStyles = makeStyles(theme => ({
    root: {
        flexGrow: 1,
    },
    paper: {
        height: 140,
        width: 100,
    },
    control: {
        padding: theme.spacing(2),
    },
}));

export const ProjectsComponent = () => {
    const [spacing, setSpacing] = useState(2);
    const classes = useStyles();

    const handleChange = (event: any) => {
        setSpacing(Number(event.target.value));
    };

    return <main>
        <Grid container className={classes.root} spacing={2}>
            <Grid item xs={12}>
                <Grid container justify="center" spacing={spacing}>
                    {[0, 1, 2].map(value => (
                        <Grid key={value} item>
                            <Paper className={classes.paper} />
                        </Grid>
                    ))}
                </Grid>
            </Grid>
            <Grid item xs={12}>
                <Paper className={classes.control}>
                    <Grid container>
                        <Grid item>
                            <FormLabel>spacing</FormLabel>
                            <RadioGroup
                                name="spacing"
                                aria-label="spacing"
                                value={spacing.toString()}
                                onChange={handleChange}
                                row
                            >
                                {[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map(value => (
                                    <FormControlLabel
                                        key={value}
                                        value={value.toString()}
                                        control={<Radio />}
                                        label={value.toString()}
                                    />
                                ))}
                            </RadioGroup>
                        </Grid>
                    </Grid>
                </Paper>
            </Grid>
        </Grid>
    </main>;
};
