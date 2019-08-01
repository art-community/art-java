import React from 'react';
import {withCookies} from "react-cookie";
import {Message} from "semantic-ui-react";

class BalancingManagementComponent extends React.Component {
    render = () =>
        <main>
            <Message>Управление балансировкой</Message>
        </main>
}

export default withCookies(BalancingManagementComponent)