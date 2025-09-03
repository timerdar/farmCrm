import { changeOrderStatus, changeCount, changeWeight } from "../../services/order-service.js";
import { EditableTextWithLabelForNumber } from "../EditableTextWithLabelForNumber.js";
import { IconButton } from "../IconButton.js";
import { TextWithLabel } from "../TextWithLabel.js";


export function MiniOrderCard(order, update){

    const div = document.createElement("div");
    div.className = 'mini-card';

    const checkbox = document.createElement('input');
    checkbox.type = 'checkbox';
    checkbox.className = 'checkbox';
    checkbox.checked = order.status == 'DONE';
    div.style.backgroundColor = checkbox.checked ? 'green' : 'white';

    checkbox.addEventListener('click', () => {
        if (checkbox.checked){
            changeOrderStatus(order.id, 'DONE');
            div.style.backgroundColor = 'green';
            order.status = "DONE";
        }else{
            changeOrderStatus(order.id, 'DELIVERY');
            div.style.backgroundColor = 'white';
            order.status = "DELIVERY";
        }
    });

    div.appendChild(checkbox);

    const nameDiv = document.createElement("div");
    nameDiv.className = 'name';
    nameDiv.innerText = order.name;
    div.appendChild(nameDiv);

    
    async function changeOrderCount(newCount){
        const response = await changeCount(order.id, newCount);
        if (response.status == 200){
            cost.content.textContent = `${response.data.cost} руб`;
            order.cost = response.data.cost;
            return true;
        }
        return false;
    }

    async function changeOrderWeight(newWeight){
        const response = await changeWeight(order.id, newWeight);
        if (response.status == 200){
            cost.content.textContent = `${response.data.cost} руб`;
            return true;
        }
        return false;
    }

    div.appendChild(EditableTextWithLabelForNumber("Количество", `${order.count} шт`, newCount => changeOrderCount(newCount)));
    
    if (order.weighed)
        div.appendChild(EditableTextWithLabelForNumber("Вес", `${order.weight} кг`, newWeight => changeOrderWeight(newWeight)));
    else 
        div.appendChild(TextWithLabel("", ""));


    const cost = TextWithLabel("Стоимость", `${order.cost} руб`);
    div.cost = cost;
    div.appendChild(cost)

    const invis = document.createElement("div");

    invis.appendChild(IconButton('/src/assets/product-return.png', () => {
        if (changeOrderStatus(order.id, 'CREATED')){
            invis.innerHTML = '';
        }
    }));

    div.appendChild(invis);

    return div;

}