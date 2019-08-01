import React from 'react';
import {withCookies} from "react-cookie";
import {Message} from "semantic-ui-react";

class ClusterManagementComponent extends React.Component {
    render = () =>
        <main>
            <Message>Управление кластером</Message>
        </main>
}

export default withCookies(ClusterManagementComponent)