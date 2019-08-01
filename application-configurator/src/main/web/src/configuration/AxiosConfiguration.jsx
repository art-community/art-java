import axios from 'axios';
import HttpStatus from 'http-status-codes'
import {CONFIGURATOR_ROUTER} from "../constants/Routers";

export default {
    setupInterceptors: (history) => {
        axios.defaults.withCredentials = true;
        axios.interceptors.response.use(response => {
            return response;
        }, error => {
            if (error.response.status === HttpStatus.UNAUTHORIZED) {
                history.push(CONFIGURATOR_ROUTER);
            }
            return Promise.reject(error)
        });
    }
};