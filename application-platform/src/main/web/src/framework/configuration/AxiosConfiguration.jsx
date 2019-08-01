import axios from 'axios';
import {UNAUTHORIZED} from "http-status-codes";

export default {
    setupInterceptors: (history, unauthorizedRouter, onResponse) => {
        axios.defaults.withCredentials = true;
        axios.interceptors.response.use(response => onResponse(response), error => {
            if (error.response.status === UNAUTHORIZED) {
                history.push(unauthorizedRouter);
            }
            return Promise.reject(error)
        });
    }
};