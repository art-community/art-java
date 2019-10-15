import * as React from "react";
import {ReactElement} from "react";
import {Drawer, List, ListItem, ListItemIcon, ListItemText, makeStyles} from "@material-ui/core";
import * as H from 'history';
import {useHistory} from "react-router";
import {Build, FeaturedPlayList} from "@material-ui/icons";
import {BUILD_PATH, PROJECT_PATH} from "../../constants/Constants";
import {SideBarProps} from "./SideBarComponentProps";

const useStyles = makeStyles(theme => ({
    root: {
        display: 'flex',
    },
    drawer: {
        width: 160,
    },
    drawerPaper: {
        width: 160,
    }
}));

const actions =
    [
        {
            action: 'PROJECTS',
            icon: <FeaturedPlayList color={'secondary'}/>,
            text: 'Проекты',
            path: PROJECT_PATH
        },
        {
            action: 'BUILD',
            icon: <Build color={'secondary'}/>,
            text: 'Сборки',
            path: BUILD_PATH
        }
    ];


const createMenuActions = (history: H.History) => actions
    .map(action =>
        <ListItem color={'primary'}
                  button
                  key={action.action}
                  onClick={() => history.push(action.path)}>
            <ListItemIcon>{action.icon as ReactElement}</ListItemIcon>
            <ListItemText primary={action.text}/>
        </ListItem>
    );

export const SideBarComponent = (props: SideBarProps) => {
    const history = useHistory();
    const classes = useStyles();
    return <main className={classes.root}>
        <Drawer
            variant="persistent"
            open
            className={classes.drawer}
            classes={{paper: classes.drawerPaper}}>
            <List>{createMenuActions(history)}</List>
        </Drawer>
        {props.children}
    </main>
};

