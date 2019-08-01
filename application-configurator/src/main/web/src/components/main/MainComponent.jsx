import './MainComponent.css'
import * as React from "react";
import {Fragment} from "react";
import {Container} from 'semantic-ui-react'
import ApplicationTab from "../application/ApplicationTab";

export default class MainComponent extends React.Component {
    render = () =>
        <Fragment>
            <Container fluid>
                <ApplicationTab/>
            </Container>
        </Fragment>;
}