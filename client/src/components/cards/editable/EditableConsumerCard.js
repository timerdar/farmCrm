import { changeAddress, changePhone } from "../../../services/consumer-service.js";
import { EditableTextWithLabelForNumber } from "../../EditableTextWithLabelForNumber.js";
import { EditableTextWithLabelForString } from "../../EditableTextWithLabelForString.js";

export function EditableConsumerCard(consumer, onClick){

    const card = document.createElement("div");
    card.className = 'card';

    const nameDiv = document.createElement("div");
    nameDiv.className = 'name';

    nameDiv.innerText = `${consumer.name}`;
    nameDiv.addEventListener('click', () => onClick(consumer.id));

    card.appendChild(nameDiv);
    
    card.appendChild(EditableTextWithLabelForString("Адрес", consumer.address, newAddress => onAddressChange(newAddress)));
    card.appendChild(EditableTextWithLabelForString("Телефон", consumer.phone, newPhone => onPhoneChange(newPhone)));

    return card;

    async function onAddressChange(newAddress){
        return await changeAddress(consumer.id, newAddress);
    }

    async function onPhoneChange(newPhone){
        return await changePhone(consumer.id, newPhone);
    }
}