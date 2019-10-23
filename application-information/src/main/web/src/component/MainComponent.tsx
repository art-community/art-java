import * as React from "react";
import {
    Box,
    Chip,
    ExpansionPanel,
    ExpansionPanelDetails,
    ExpansionPanelSummary,
    Grid,
    Typography
} from "@material-ui/core";
import {DEFAULT_THEME} from "../theme/Theme";
import {ExpandMore} from "@material-ui/icons";
import {ThemeProvider} from "@material-ui/styles";
import ReactJson from 'react-json-view'

export const MainComponent = () => {
    return <ThemeProvider theme={DEFAULT_THEME}>
        <Grid container direction={"column"} spacing={1}>
            <Grid item>
                <Typography variant={"h3"} color={"primary"}>
                    Информация о модуле
                </Typography>
            </Grid>
            <Grid item>
                <Grid alignItems={"flex-start"} container direction={"column"} spacing={1}>
                    <Grid item>
                        <ExpansionPanel>
                            <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
                                <Chip variant={"outlined"} color={"primary"}
                                      label={<Typography>HTTP</Typography>}/>
                                <Box ml={1}>
                                    <Typography variant={"h6"} color={"secondary"}>
                                        Сервис 1
                                    </Typography>
                                </Box>
                            </ExpansionPanelSummary>
                            <ExpansionPanelDetails>
                                <Grid container direction={"column"} spacing={1}>
                                    <Grid item>
                                        <ExpansionPanel>
                                            <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
                                                <Typography variant={"h6"}>
                                                    Метод 1
                                                </Typography>
                                            </ExpansionPanelSummary>
                                            <ExpansionPanelDetails>
                                                <Grid container direction={"column"} spacing={1}>
                                                    <Grid item container spacing={1}>
                                                        <Grid item>
                                                            <Chip color={"primary"}
                                                                  label={<Typography>URL</Typography>}/>
                                                        </Grid>
                                                        <Grid item>
                                                            <Typography variant={"h6"} color={"secondary"}>
                                                                http://localhost:1234/
                                                            </Typography>
                                                        </Grid>
                                                    </Grid>
                                                    <Grid item>
                                                        <ExpansionPanel>
                                                            <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
                                                                <Chip color={"primary"}
                                                                      label={<Typography>Запрос</Typography>}/>
                                                            </ExpansionPanelSummary>
                                                            <ExpansionPanelDetails>
                                                                <ReactJson enableClipboard={false}
                                                                           displayObjectSize={false}
                                                                           displayDataTypes={false}
                                                                           src={{"field": "TEST"}}/>
                                                            </ExpansionPanelDetails>
                                                        </ExpansionPanel>
                                                    </Grid>
                                                    <Grid item>
                                                        <ExpansionPanel>
                                                            <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
                                                                <Chip color={"primary"}
                                                                      label={<Typography>Ответ</Typography>}/>
                                                            </ExpansionPanelSummary>
                                                            <ExpansionPanelDetails>
                                                                <ReactJson enableClipboard={false}
                                                                           displayObjectSize={false}
                                                                           displayDataTypes={false}
                                                                           src={{"field": "TEST"}}/>
                                                            </ExpansionPanelDetails>
                                                        </ExpansionPanel>
                                                    </Grid>
                                                </Grid>
                                            </ExpansionPanelDetails>
                                        </ExpansionPanel>
                                    </Grid>
                                    <Grid item>
                                        <ExpansionPanel>
                                            <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
                                                <Typography variant={"h6"}>
                                                    Метод 1
                                                </Typography>
                                            </ExpansionPanelSummary>
                                            <ExpansionPanelDetails>
                                                <Grid container direction={"column"} spacing={1}>
                                                    <Grid item container spacing={1}>
                                                        <Grid item>
                                                            <Chip color={"primary"}
                                                                  label={<Typography>URL</Typography>}/>
                                                        </Grid>
                                                        <Grid item>
                                                            <Typography variant={"h6"} color={"secondary"}>
                                                                http://localhost:1234/
                                                            </Typography>
                                                        </Grid>
                                                    </Grid>
                                                    <Grid item>
                                                        <ExpansionPanel>
                                                            <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
                                                                <Chip color={"primary"}
                                                                      label={<Typography>Запрос</Typography>}/>
                                                            </ExpansionPanelSummary>
                                                            <ExpansionPanelDetails>
                                                                <ReactJson enableClipboard={false}
                                                                           displayObjectSize={false}
                                                                           displayDataTypes={false}
                                                                           src={{
                                                                               "field": "TEST",
                                                                               "field2": {
                                                                                   "TEST": [
                                                                                       {"test": 123}
                                                                                   ],
                                                                               }
                                                                           }}/>
                                                            </ExpansionPanelDetails>
                                                        </ExpansionPanel>
                                                    </Grid>
                                                    <Grid item>
                                                        <ExpansionPanel>
                                                            <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
                                                                <Chip color={"primary"}
                                                                      label={<Typography>Ответ</Typography>}/>
                                                            </ExpansionPanelSummary>
                                                            <ExpansionPanelDetails>
                                                                <ReactJson enableClipboard={false}
                                                                           displayObjectSize={false}
                                                                           displayDataTypes={false}
                                                                           src={{"field": "TEST"}}/>
                                                            </ExpansionPanelDetails>
                                                        </ExpansionPanel>
                                                    </Grid>
                                                </Grid>
                                            </ExpansionPanelDetails>
                                        </ExpansionPanel>
                                    </Grid>
                                </Grid>
                            </ExpansionPanelDetails>
                        </ExpansionPanel>
                    </Grid>
                    <Grid item>
                        <ExpansionPanel>
                            <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
                                <Chip variant={"outlined"} color={"primary"}
                                      label={<Typography>HTTP</Typography>}/>
                                <Box ml={1}>
                                    <Typography variant={"h5"} color={"secondary"}>
                                        Сервис 1
                                    </Typography>
                                </Box>
                            </ExpansionPanelSummary>
                            <ExpansionPanelDetails>
                                <Grid container direction={"column"} spacing={1}>
                                    <Grid item>
                                        <ExpansionPanel>
                                            <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
                                                <Typography variant={"h5"}>
                                                    Метод 1
                                                </Typography>
                                            </ExpansionPanelSummary>
                                            <ExpansionPanelDetails>
                                                <Grid container direction={"column"} spacing={1}>
                                                    <Grid item container spacing={1}>
                                                        <Grid item>
                                                            <Chip color={"primary"}
                                                                  label={<Typography>URL</Typography>}/>
                                                        </Grid>
                                                        <Grid item>
                                                            <Typography variant={"h6"} color={"secondary"}>
                                                                http://localhost:1234/
                                                            </Typography>
                                                        </Grid>
                                                    </Grid>
                                                    <Grid item>
                                                        <ExpansionPanel>
                                                            <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
                                                                <Chip color={"primary"}
                                                                      label={<Typography>Запрос</Typography>}/>
                                                            </ExpansionPanelSummary>
                                                            <ExpansionPanelDetails>
                                                                <ReactJson enableClipboard={false}
                                                                           displayObjectSize={false}
                                                                           displayDataTypes={false}
                                                                           src={{"field": "TEST"}}/>
                                                            </ExpansionPanelDetails>
                                                        </ExpansionPanel>
                                                    </Grid>
                                                    <Grid item>
                                                        <ExpansionPanel>
                                                            <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
                                                                <Chip color={"primary"}
                                                                      label={<Typography>Ответ</Typography>}/>
                                                            </ExpansionPanelSummary>
                                                            <ExpansionPanelDetails>
                                                                <ReactJson enableClipboard={false}
                                                                           displayObjectSize={false}
                                                                           displayDataTypes={false}
                                                                           src={{"field": "TEST"}}/>
                                                            </ExpansionPanelDetails>
                                                        </ExpansionPanel>
                                                    </Grid>
                                                </Grid>
                                            </ExpansionPanelDetails>
                                        </ExpansionPanel>
                                    </Grid>
                                </Grid>
                            </ExpansionPanelDetails>
                        </ExpansionPanel>
                    </Grid>
                    <Grid item>
                        <ExpansionPanel>
                            <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
                                <Chip variant={"outlined"} color={"primary"}
                                      label={<Typography>HTTP</Typography>}/>
                                <Box ml={1}>
                                    <Typography variant={"h5"} color={"secondary"}>
                                        Сервис 1
                                    </Typography>
                                </Box>
                            </ExpansionPanelSummary>
                            <ExpansionPanelDetails>
                                <Grid container direction={"column"} spacing={1}>
                                    <Grid item>
                                        <ExpansionPanel>
                                            <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
                                                <Typography variant={"h5"}>
                                                    Метод 1
                                                </Typography>
                                            </ExpansionPanelSummary>
                                            <ExpansionPanelDetails>
                                                <Grid container direction={"column"} spacing={1}>
                                                    <Grid item container spacing={1}>
                                                        <Grid item>
                                                            <Chip color={"primary"}
                                                                  label={<Typography>URL</Typography>}/>
                                                        </Grid>
                                                        <Grid item>
                                                            <Typography variant={"h6"} color={"secondary"}>
                                                                http://localhost:1234/
                                                            </Typography>
                                                        </Grid>
                                                    </Grid>
                                                    <Grid item>
                                                        <ExpansionPanel>
                                                            <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
                                                                <Chip color={"primary"}
                                                                      label={<Typography>Запрос</Typography>}/>
                                                            </ExpansionPanelSummary>
                                                            <ExpansionPanelDetails>
                                                                <ReactJson enableClipboard={false}
                                                                           displayObjectSize={false}
                                                                           displayDataTypes={false}
                                                                           src={{"field": "TEST"}}/>
                                                            </ExpansionPanelDetails>
                                                        </ExpansionPanel>
                                                    </Grid>
                                                    <Grid item>
                                                        <ExpansionPanel>
                                                            <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
                                                                <Chip color={"primary"}
                                                                      label={<Typography>Ответ</Typography>}/>
                                                            </ExpansionPanelSummary>
                                                            <ExpansionPanelDetails>
                                                                <ReactJson enableClipboard={false}
                                                                           displayObjectSize={false}
                                                                           displayDataTypes={false}
                                                                           src={{"field": "TEST"}}/>
                                                            </ExpansionPanelDetails>
                                                        </ExpansionPanel>
                                                    </Grid>
                                                </Grid>
                                            </ExpansionPanelDetails>
                                        </ExpansionPanel>
                                    </Grid>
                                </Grid>
                            </ExpansionPanelDetails>
                        </ExpansionPanel>
                    </Grid>
                </Grid>
            </Grid>
        </Grid>
    </ThemeProvider>
};