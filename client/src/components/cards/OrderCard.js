import { TextWithLabel } from "../TextWithLabel.js";

export function OrderCard(order){

    const card = document.createElement("div");
    card.className = 'card';

    const nameDiv = document.createElement('div');
    nameDiv.className = 'name';
    nameDiv.textContent = order.name;
    card.appendChild(nameDiv);

    if (order.isProductWeight){
        card.appendChild(TextWithLabel("КГ", order.weight));
    }else{
        card.appendChild(TextWithLabel("ШТ", order.amount));
    };

    card.appendChild(TextWithLabel("Сумма", `${order.cost} руб.`));

    return card;
}