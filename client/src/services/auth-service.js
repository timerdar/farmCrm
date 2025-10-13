import { api } from "../core/api.js";

export async function auth(login, password){
    try{
        const data = {
            login: login,
            password: password
        };

        const response = await api().post('/api/auth/login', data);
        return response;
    }catch (error) {
        console.log(error);
        throw error;
    }
}

export async function check(token) {
    try{
        const data = {
            token: token
        };

        const response = await api().post("/api/auth/check", data);
        return response;
    }catch(error) {
        console.log(error);
        throw error;
    }
}