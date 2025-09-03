import { createProduct } from "../../services/product-service.js";
import { IconButton } from "../IconButton.js";

export function ProductCreation(updateClick){

    const mainDiv = document.createElement("div");
    mainDiv.className = 'creation-card';

    const label = document.createElement("label");
    label.textContent = 'Создание продукта';
    mainDiv.appendChild(label);

    const div = document.createElement("div");
    div.className = 'forms';

    const nameInput = document.createElement("input");
    nameInput.className = 'creation-input';
    nameInput.type = 'text';
    nameInput.placeholder = 'Название';
    div.appendChild(nameInput);

    const priceInput = document.createElement('input');
    priceInput.className = 'creation-input';
    priceInput.type = 'number';
    priceInput.placeholder = 'Цена';
    div.appendChild(priceInput);

    const lbl = document.createElement('label');
    lbl.textContent = 'Весовой товар';
    div.appendChild(lbl);

    const weightCheckbox = document.createElement('input');
    weightCheckbox.classList = 'creation-checkbox';
    weightCheckbox.type = 'checkbox';
    weightCheckbox.placeholder = 'Вес';
    div.appendChild(weightCheckbox);


    div.appendChild(IconButton('/src/assets/accept.png', () => {        
        createProduct(nameInput.value, Number(priceInput.value), weightCheckbox.checked).then(updateClick);    
    }))

    div.appendChild(IconButton('/src/assets/close.png', () => {
        updateClick();
    }))

    mainDiv.appendChild(div);


    return mainDiv;

}