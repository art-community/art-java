import React from 'react';
import {withCookies} from "react-cookie";
import {Message} from "semantic-ui-react";

class ReleaseManagementComponent extends React.Component {
    render = () =>
        <main>
            <Message>Управление проектами</Message>
        </main>
}

export default withCookies(ReleaseManagementComponent)