import './MainComponent.css'
import * as React from "react";
import {Button, Grid, Header, Icon, Message} from 'semantic-ui-react'
import {PRIMARY_COLOR, SECONDARY_COLOR} from "../constants/Constants";
import {margin} from "../framework/styles/StylesFactory";
import {
    ADMINISTRATION_ACTION,
    BALANCING_MANAGEMENT_ACTION,
    CLUSTER_MANAGEMENT_ACTION,
    CONFIGURATION_MANAGEMENT_ACTION,
    PROJECT_MANAGEMENT_ACTION,
    USER_CARD_ACTION
} from "../constants/MenuActions";

export default class MainComponent extends React.Component {
    handleItemClick = (e, {name}) => this.setState({activeItem: name});

    createGridButton = (action) =>
        <Grid.Column>
            <Icon className={'buttonIcon'}
                  circular
                  color={SECONDARY_COLOR}
                  size='massive'
                  name={action.icon}/>
            <Button fluid
                    color={PRIMARY_COLOR}
                    onClick={() => this.props.history.push(action.router)}>{action.name}</Button>
        </Grid.Column>;

    render = () =>
        <main style={margin(30)}>
            <Header textAlign='center' as={'h1'}>
                <Message color={PRIMARY_COLOR}>
                    ADK
                </Message>
            </Header>
            <Header textAlign='center' as={'h2'}>
                <Message color={SECONDARY_COLOR}>
                    No box. No restrictions. No problems.
                    <br/>
                    Платформа по управлению ADK приложениями
                </Message>
            </Header>
            <Grid columns={3} stackable textAlign='center'>
                <Grid.Row verticalAlign='middle'>
                    {this.createGridButton( PROJECT_MANAGEMENT_ACTION)}
                    {this.createGridButton(CONFIGURATION_MANAGEMENT_ACTION)}
                    {this.createGridButton(BALANCING_MANAGEMENT_ACTION)}
                </Grid.Row>
                <Grid.Row verticalAlign='middle'>
                    {this.createGridButton(CLUSTER_MANAGEMENT_ACTION)}
                    {this.createGridButton(ADMINISTRATION_ACTION)}
                    {this.createGridButton(USER_CARD_ACTION)}
                </Grid.Row>
            </Grid>
        </main>;

}

