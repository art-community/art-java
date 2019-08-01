import axios from 'axios';
import HttpStatus from 'http-status-codes'
import {STATE_ROUTER} from "../constants/Routers";

export default {
    setupInterceptors: (history) => {
        axios.defaults.withCredentials = true;
        axios.interceptors.response.use(response => {
            return response;
        }, error => {
            if (error.response.status === HttpStatus.UNAUTHORIZED) {
                history.push(STATE_ROUTER);
            }
            return Promise.reject(error)
        });
    }
};