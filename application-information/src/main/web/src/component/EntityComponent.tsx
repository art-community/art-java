import React from "react";
import {
    Chip,
    ExpansionPanel,
    ExpansionPanelDetails,
    ExpansionPanelSummary,
    Typography,
    useTheme
} from "@material-ui/core";
import {ExpandMore} from "@material-ui/icons";
import ReactJson from "react-json-view";

interface EntityComponentProps {
    type: EntityType
    entity: string
}

export enum EntityType {
    REQUEST,
    RESPONSE
}

export const EntityComponent = (props: EntityComponentProps) => {
    const theme = useTheme();
    const getEntity =() => {
        const entityJson = JSON.parse(props.entity);
        if (typeof entityJson != "object") {
            return props.entity
        }
        return <ReactJson style={{fontSize: theme.typography.h6.fontSize}}
                   displayObjectSize={false}
                   displayDataTypes={false}
                   name={props.type == EntityType.REQUEST ? "request" : "response"}
                   src={entityJson}/>
    };
    return <ExpansionPanel>
        <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
            <Chip color={"primary"}
                  label={<Typography>{props.type == EntityType.REQUEST ? "Пример запроса" : "Пример ответа"}</Typography>}/>
        </ExpansionPanelSummary>
        <ExpansionPanelDetails>
            {getEntity()}
        </ExpansionPanelDetails>
    </ExpansionPanel>;
};
