import { api } from "../core/api.js";

var products = [
    { id: 1, name: "Яйцо", cost: 33, orderedCount: 10, createdCount: 4 }]

export async function createProduct(name, cost, isWeight) {
    try {
        if (name !== '' && !isNaN(cost) && cost > 0) {

            var data = {
                name: name,
                cost: cost,
                weightable: isWeight
            }

            const response = await api().post('/api/products', data);
            return response.status == 200;
        }

    } catch (e) {
        console.log(e);
        throw e;
    }
}


export async function getProductsList() {
    try{
        const response = await api().get('/api/products');
        return response.data;
    }catch(e){
        console.log(e);
        throw e;
    }
}

export async function changeCost(productId, newCost) {
    try{
        var data = {
            id: productId,
            cost: newCost
        }

        const response = await api().post('/api/products/change-price', data);
        return response.status == 200;
    }catch(e){
        console.log(e);
        throw e
    }
}

export async function changeCreatedCount(productId, newCreatedCount) {
    try{
        var data = {
            id: productId,
            createdCount: newCreatedCount
        }

        const response = await api().post('/api/products/change-created-count', data);
        return response.status == 200;
    }catch(e){
        console.log(e);
        throw e;
    }
}

export async function getShortProductsList() {
    try{
        const response = await api().get('/api/products/short');
        return response.data;
    }catch(e){
        console.log(e);
        throw e;
    }
}

export async function getProduct(id) {
    try{
        const response = await api().get(`/api/products/${id}`);
        return response.data;
    }catch(e){
        console.log(e);
        throw e;
    }
}

