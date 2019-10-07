import {Build, FeaturedPlayList, Whatshot} from "@material-ui/icons";
import * as React from "react";

export const MENU_ACTIONS =
    [
        {
            action: 'PROJECTS',
            icon: <FeaturedPlayList color={'secondary'}/>,
            text: 'Проекты'
        },
        {
            action: 'BUILD',
            icon: <Build color={'secondary'}/>,
            text: 'Сборка'
        },
        {
            action: 'DEPLOY',
            icon: <Whatshot color={'secondary'}/>,
            text: 'Развертка'
        }
    ];
