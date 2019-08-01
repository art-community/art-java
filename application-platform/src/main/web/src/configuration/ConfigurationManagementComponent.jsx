import React from 'react';
import {withCookies} from "react-cookie";
import {Message} from "semantic-ui-react";

class ConfigurationManagementComponent extends React.Component {
    render = () =>
        <main>
            <Message>Управление конфигурациями</Message>
        </main>
}

export default withCookies(ConfigurationManagementComponent)