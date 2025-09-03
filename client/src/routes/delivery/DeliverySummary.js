import { getDeliverySummary } from "../../services/order-service.js";

export async function DeliverySummary(){
    const orders = await getDeliverySummary();

    const div = document.createElement("div");
    div.className = 'container';
    
    const h1 = document.createElement('h1');
    h1.innerText = 'Сводка по продукции в доставке';
    div.appendChild(h1);

    const list = document.createElement('ul');

    for (let order of orders){
        const li = document.createElement('li');
        li.innerHTML = `${order.productName} - готово <b>${order.createdCount}</b> из <b>${order.orderedCount}</b>. Свободно: ${order.createdCount - order.orderedCount}`;
        list.appendChild(li);
    }

    div.appendChild(list);

    return div;

}