import { navigateTo } from "../../../core/navigate.js";
import { changeCost, changeCreatedCount } from "../../../services/product-service.js";
import { EditableTextWithLabelForNumber } from "../../EditableTextWithLabelForNumber.js";
import { TextWithLabel } from "../../TextWithLabel.js";

export function EditableProductCard(product){

    const div = document.createElement("div");
    div.className = 'card';
    
    const nameDiv = document.createElement("div");
    nameDiv.className = 'name';
    nameDiv.innerText = `${product.name}`
    
    async function onCostClick(e) {
        return await changeCost(product.id, e);
    }

    async function onCreatedCountClick(e) {
        return await changeCreatedCount(product.id, e);
    }

    div.appendChild(nameDiv);

    const posfix = product.weighed ? 'кг': 'шт'

    const cost = EditableTextWithLabelForNumber("Цена", `${product.cost} руб/${posfix}`, newCost => onCostClick(newCost));
    const count = TextWithLabel("Заказано", `${product.orderedCount}`);
    const createdCount = EditableTextWithLabelForNumber("Изготовлено", `${product.createdCount} шт.`, newCreatedCount => onCreatedCountClick(newCreatedCount));

    count.addEventListener('click', () => {
        navigateTo(`/products/${product.id}/orders`);
    })

    div.appendChild(cost);
    div.appendChild(count);
    div.appendChild(createdCount);

    return div;

}