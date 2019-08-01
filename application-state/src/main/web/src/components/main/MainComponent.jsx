import './MainComponent.css'
import * as React from "react";
import {Fragment} from "react";
import {Container} from 'semantic-ui-react'
import ClusterTab from "../cluster/ClusterTab";

export default class MainComponent extends React.Component {
    render = () =>
        <Fragment>
            <Container fluid>
                <ClusterTab/>
            </Container>
        </Fragment>;
}