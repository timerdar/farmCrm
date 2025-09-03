import { createConsumer } from "../../services/consumer-service.js";
import { IconButton } from "../IconButton.js";

export function ConsumerCreation(updateClick) {

    const mainDiv = document.createElement("div");
    mainDiv.className = 'creation-card';

    const label = document.createElement('label');
    label.textContent = 'Создание заказчика';
    mainDiv.appendChild(label);

    const div = document.createElement('div');
    div.className = 'forms';

    const nameInput = document.createElement('input');
    nameInput.type = 'text';
    nameInput.className = 'creation-input';
    nameInput.placeholder = 'Имя';
    div.appendChild(nameInput);

    const addressInput = document.createElement('input');
    addressInput.type = 'text';
    addressInput.className = 'creation-input';
    addressInput.placeholder = 'Адрес доставки';
    div.appendChild(addressInput);

    const phoneInput = document.createElement('input');
    phoneInput.type = 'number';
    phoneInput.className = 'creation-input';
    phoneInput.placeholder = 'Номер телефона';
    div.appendChild(phoneInput);

    div.appendChild(IconButton('/src/assets/accept.png', () => {
        createConsumer(nameInput.value, addressInput.value, phoneInput.value).then(updateClick);
    }))

    div.appendChild(IconButton('/src/assets/close.png', () => {
        updateClick();
    }))

    mainDiv.appendChild(div);

    return mainDiv;

}