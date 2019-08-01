import * as React from "react";
import {Button, Header} from 'semantic-ui-react'
import ReactJson from 'react-json-view'
import {PRIMARY_COLOR, SECONDARY_COLOR} from "../../constants/Constants";

export default class ConfigurationEditor extends React.Component {
    state = {
        configuration: {},
        edited: false,
        saved: false
    };

    constructor(props) {
        super(props);
        this.state.configuration = props.configuration;
        this.onSave = this.onSave.bind(this);
        this.onApply = this.onApply.bind(this);
    }

    onSave = () => {
        this.setState({edited: false});
        this.props.onSave(this.state.configuration)
    };

    onApply = () => {
        this.props.onApply()
    };

    render = () =>
        <div>
            <Header as={'h1'}>Конфигурация: {this.props.entity}</Header>
            <ReactJson
                onEdit={(data) => {
                    this.setState({configuration: data.updated_src, edited: true})
                }}
                onAdd={(data) => {
                    this.setState({configuration: data.updated_src, edited: true})
                }}
                onDelete={(data) => {
                    this.setState({configuration: data.updated_src, edited: true})
                }}
                displayDataTypes={false}
                displayObjectSize={false}
                name={false}
                src={this.props.configuration}>
            </ReactJson>
            <br/>
            <Button color={PRIMARY_COLOR}
                    disabled={!this.state.edited}
                    onClick={() => {
                        this.setState({saved: true});
                        this.onSave();
                    }}
            >Сохранить</Button>

            <span hidden={this.props.withoutApply}>
                <Button color={SECONDARY_COLOR}
                        disabled={!this.state.saved}
                        onClick={() => {
                            this.setState({saved: false});
                            this.onApply();
                        }}>Применить</Button>
            </span>
        </div>
}