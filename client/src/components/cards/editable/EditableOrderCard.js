import { changeCount, changeOrderStatus, changeWeight } from "../../../services/order-service.js";
import { EditableTextWithLabelForNumber } from "../../EditableTextWithLabelForNumber.js";
import { IconButton } from "../../IconButton.js";
import { TextWithLabel } from "../../TextWithLabel.js";

export function EditableOrderCard(order) {

    const card = document.createElement("div");
    card.className = 'card';

    const nameDiv = document.createElement('div');
    nameDiv.className = 'name';
    nameDiv.textContent = order.name;
    card.appendChild(nameDiv);

    async function onWeightChange(newWeight) {
        const response = await changeWeight(order.id, newWeight);
        cost.content.textContent = response.data.cost;
        return response.status == 200;
    };

    async function onCountChange(newCount) {
        const response = await changeCount(order.id, newCount);
        cost.content.textContent = `${response.data.cost} руб.`;
        return response.status == 200;
    };

    if (order.isProductWeight) {
        card.appendChild(EditableTextWithLabelForNumber("Вес", `${order.weight} кг.`, newWeight => {onWeightChange(newWeight)}));
    }
    card.appendChild(EditableTextWithLabelForNumber("Кол-во", `${order.count} шт.`, newCount => onCountChange(newCount)));


    const cost = TextWithLabel("Сумма", `${order.cost} руб.`);
    card.appendChild(cost);

    const invis = document.createElement("div");
    card.appendChild(invis);

    invis.appendChild(IconButton('/src/assets/delivery.png',
        () => {
            if (changeOrderStatus(order.id, 'DELIVERY')){
                invis.innerHTML = '';
            };
        }))

    const invis2 = document.createElement('div');
    card.appendChild(invis2);

    invis2.appendChild(IconButton('/src/assets/remove.png',
        () => {
            if (confirm('Уверены, что хотите удалить заказ?') && changeOrderStatus(order.id, 'ARCHIVED'))
                invis2.innerHTML = '';
        }
    ))

    return card;
}