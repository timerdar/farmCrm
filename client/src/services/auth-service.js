export async function auth(login, password){
    try{
        const data = {
            login: login,
            password: password
        };

        const response = await api().post('/login', data);
        return response.status == 200;
    }catch (error) {
        console.log(error);
        throw error;
    }
}