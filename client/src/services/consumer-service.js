import { api } from '../core/api.js';

export async function changeAddress(consumerId, newAddress){
    try{
        const data = {
            id: consumerId,
            address: newAddress
        };

        const response = await api().post('/api/consumers/change-address', data);
        return response.status === 200;
    }catch(e){
        console.log(e);
        throw e;
    }

}

export async function changePhone(consumerId, newPhone){
    try{
        const data = {
            id: consumerId,
            phone: newPhone
        };

        const response = await api().post('/api/consumers/change-phone', data);
        return response.status === 200;
    }catch(e){
        console.log(e);
        throw e;
    }
}   

export async function getConsumersList(){
    try{
        const response = await api().get('/api/consumers');
        return response.data;
    }catch(e){
        console.log(e);
        throw e;
    }
};

export async function createConsumer(name, address, phone){
    if (name !== '' && address !== '' && phone !== ''){

        const data = {
            name: name,
            address: address,
            phone: phone
        };
        const response = await api().post('/api/consumers', data);
        return response.status == 200;
    }
}

export async function getConsumer(consumerId){
    try{
        const response = await api().get(`/api/consumers/${consumerId}`);
        const data = await response.data;
        return data
    }catch(e){
        console.log(e);
        throw e;
    }
}

export async function setConsumersOrder(data) {
    try{
        const response = await api().post("/api/consumers/set-consumers-order", data);
        return response.status == 200;
    }catch(e){
        console.log(e);
        throw e;
    }
}