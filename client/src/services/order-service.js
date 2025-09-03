import { api } from "../core/api.js"; 

export async function changeOrderStatus(orderId, status){
    try{
        var data = {
            id: orderId,
            status: status
        }
        const response = await api().post("/api/orders/change-status", data);
        return response.status == 200;
    }catch(e){
        console.log(e);
        throw e;
    }
}

/*export function getDeliveryOrdersOfConsumer(consumerId){
    var products = [];
    for (let i = 0; i < 5; i++){
        products.push({id: i, name: "Яйца 1", count: i*10, cost: 233, isProductWeight: true, weight: 0.56});
    }
    return products;
    //const orders = [{id: 1, name: "Яйца", count: 10, cost: 233, amount: 3, isProductWeight: true, weight: 0.56}]
}*/

export async function getCreatedOrdersOfConsumer(consumerId){
    try{
        const response = await api().get(`/api/orders/consumers/${consumerId}?status=CREATED`);
        return response.data;
    }catch(e){
        console.log(e);
        throw e;
    }

}

export async function getCreatedOrdersOfProduct(productId){
    try{
        const response = await api().get(`/api/orders/products/${productId}?status=CREATED`);
        return response.data;
    }catch(e){
        console.log(e);
        throw e;
    }

}

export async function changeCount(orderId, newCount){
    try{
        var data = {
            id: orderId,
            amount: newCount
        }
        const response = await api().post('/api/orders/change-amount', data);
        return response;//.status == 200;
    }catch(e){
        console.log(e);
        throw e;
    }
    
}

export async function changeWeight(orderId, newWeight){
    try{
        var data = {
            id: orderId,
            weight: newWeight
        }
        const response = await api().post('/api/orders/change-weight', data);
        return response;//.status == 200;
    }catch(e){
        console.log(e);
        throw e;
    }
}

export async function createOrder(consumerId, productId, amount){
    try{
        var data = {
            consumerId: consumerId,
            productId: productId,
            amount: amount
        };
        const response = await api().post('/api/orders', data);
        return response.status == 200;
    }catch(e){
        console.log(e);
        throw e;
    }
}

export async function clearDelivery(){
    try{
        const response = await api().get("/api/orders/clear-delivery");
        return response.status == 200;
    }catch(e){
        console.log(e);
        throw e;
    }
}

export async function getDeliveryOrders(){
    try{
        const response = await api().get("/api/orders/delivery");
        return response.data;
    }catch(e){
        console.log(e);
        throw e;
    }
}

export async function getDeliverySummary() {
    try{
        const response = await api().get("/api/orders/delivery-summary");
        return response.data;
    }catch(e){
        console.log(e);
        throw e;
    }
}