import * as React from "react";
import {Header, Label, Message, Table} from "semantic-ui-react";
import {PRIMARY_COLOR, REFRESH_RATE, SECONDARY_COLOR} from "../../constants/Constants";
import {getEndpoints} from "../../services/NetworkService";

export default class ModuleEndpointTab extends React.Component {
    state = {profile: "", modulePath: "", endpoints: [], start: Date.now()};

    constructor(props) {
        super(props);
        this.update = this.update.bind(this);
        this.refresh = this.refresh.bind(this);
        this.renderRow = this.renderRow.bind(this);
        this.renderTable = this.renderTable.bind(this);
        this.state = {profile: props.profile, modulePath: props.modulePath, endpoints: [], start: Date.now()};
    }

    componentDidMount() {
        this.timer = setInterval(this.refresh, REFRESH_RATE);
        this.update(this.props);
        this.refresh();
    }

    componentWillUnmount() {
        clearInterval(this.timer);
    }

    componentWillReceiveProps(nextProps, nextContext) {
        this.update(nextProps);
    }

    refresh = () => {
        getEndpoints(this.state.profile, this.state.modulePath, endpoints => this.setState({endpoints: endpoints}))
    };


    update = (newProps) => {
        this.setState({profile: newProps.profile, modulePath: newProps.modulePath});
    };

    renderRow = (endpoint) =>
        <Table.Row key={endpoint.host}>
            {<Table.Cell singleLine><Label color={SECONDARY_COLOR}>{endpoint.host}</Label></Table.Cell>}
            {<Table.Cell singleLine><Label color={SECONDARY_COLOR}>{endpoint.port}</Label></Table.Cell>}
            {<Table.Cell singleLine><Label color={SECONDARY_COLOR}>{endpoint.sessions}</Label></Table.Cell>}
        </Table.Row>;

    renderTable = () =>
        <Table.Body>
            {this.state.endpoints.map(endpoint => this.renderRow(endpoint))}
        </Table.Body>;

    render = () =>
        <div>
            <Message compact color={PRIMARY_COLOR}><Header as={'h5'}>Экземпляры модулей</Header></Message>
            <Table basic='very'>
                <Table.Header>
                    <Table.Row>
                        <Table.HeaderCell singleLine><Label color={PRIMARY_COLOR}>Хост</Label></Table.HeaderCell>
                        <Table.HeaderCell singleLine><Label color={PRIMARY_COLOR}>Порт</Label></Table.HeaderCell>
                        <Table.HeaderCell singleLine><Label color={PRIMARY_COLOR}>Сессии</Label></Table.HeaderCell>
                    </Table.Row>
                </Table.Header>
                {this.renderTable()}
            </Table>
        </div>
}