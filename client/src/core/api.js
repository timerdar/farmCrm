import axios from 'https://cdn.jsdelivr.net/npm/axios@1.6.7/+esm';
import { navigateTo } from './navigate.js';


export function api(){

    const config = {
        baseURL: 'http://localhost:8085',
        timeout: 7000,
        headers: {
            'Content-Type': 'application/json'
        }
    }
    const instanse = axios.create(config);


    instanse.interceptors.request.use((config) => {
        const token = localStorage.getItem('FARM_LOGIN');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    });

    instanse.interceptors.response.use(
        (response) => response,
        (error) => {
            if (error.response && (error.response.status == 401 || error.response.status == 403)) {
                navigateTo('');
                console.log(error)
            }
            return Promise.reject(error);
        }
    );

    return instanse;
};