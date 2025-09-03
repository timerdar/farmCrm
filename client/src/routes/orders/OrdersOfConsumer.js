import { Button } from "../../components/Button.js";
import { EditableConsumerCard } from "../../components/cards/editable/EditableConsumerCard.js";
import { OrderCreation } from "../../components/creation_forms/OrderCreation.js";
import { OrdersList } from "../../components/lists/OrdersList.js";
import { TextWithLabel } from "../../components/TextWithLabel.js";
import { getCreatedOrdersOfConsumer } from "../../services/order-service.js";
import { getConsumer } from "../../services/consumer-service.js";

export async function OrdersOfConsumer(){
    
    const id = extractId();
    console.log(id);
    
    const consumer = await getConsumer(id);
    var orders = await getCreatedOrdersOfConsumer(id);  

    const div = document.createElement('div');
    div.className = 'container';

    const h1 = document.createElement("h1");
    h1.textContent = 'Карта заказчика';
    div.appendChild(h1);

    const consumerCard = EditableConsumerCard(consumer, () => {});
    consumerCard.appendChild(TextWithLabel("Сумма выкупа", `${consumer.totalSum} руб.`));
    div.appendChild(consumerCard);

    const replacableDiv = document.createElement("div");
    replacableDiv.className = 'replacable-div'
    div.appendChild(replacableDiv);

    const ordersList = OrdersList(orders, true);

    async function setButton(){
        const addOrderBtn = Button("Добавить заказ", setForm);
        orders = await getCreatedOrdersOfConsumer(id);
        ordersList.update(orders);
        replacableDiv.innerHTML = '';
        replacableDiv.appendChild(addOrderBtn);
    }

    async function setForm(){
        const creationForm = await OrderCreation(id, setButton);
        replacableDiv.innerHTML = '';
        replacableDiv.appendChild(creationForm);
    }

    await setButton();

    div.appendChild(ordersList);

    return div;
    
}

function extractId(){
    const hash = location.hash;
    const regex = /consumers\/(.*?)\/orders/;
    const id = hash.match(regex);
    return id[1];
}