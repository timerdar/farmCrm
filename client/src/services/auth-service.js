import { api } from "../core/api.js";

export async function auth(login, password){
    try{
        const data = {
            login: login,
            password: password
        };

        const response = await api().post('/auth/login', data);
        return response;
    }catch (error) {
        console.log(error);
        throw error;
    }
}