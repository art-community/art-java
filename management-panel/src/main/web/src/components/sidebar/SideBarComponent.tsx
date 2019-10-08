import * as React from "react";
import {ReactElement} from "react";
import {Drawer, List, ListItem, ListItemIcon, ListItemText, Paper} from "@material-ui/core";
import {MENU_ACTIONS} from "./MenuActionsFactory";
import {Link} from "react-router-dom";

export interface SideBarProps {
    children: React.ReactNode
}

const createMenuActions = () => MENU_ACTIONS
    .map(action =>
        <ListItem color={'primary'} button key={action.action}>

        </ListItem>
    );

export const SideBarComponent = (props: SideBarProps) =>
    <main>
        <Drawer variant="permanent">
            <List>{createMenuActions()}</List>
        </Drawer>
        {props.children}
    </main>;