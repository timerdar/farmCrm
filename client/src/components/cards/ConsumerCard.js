import { TextWithLabel } from "../TextWithLabel.js";

export function ConsumerCard(consumer, onClick){

    const card = document.createElement("div");
    card.className = 'card';
    card.addEventListener('click', () => onClick(consumer.id));


    const nameDiv = document.createElement("div");
    nameDiv.className = 'name';

    nameDiv.innerText = `${consumer.name}`;

    card.appendChild(nameDiv);
    
    card.appendChild(TextWithLabel("Адрес", consumer.address));
    card.appendChild(TextWithLabel("Телефон", consumer.phone));
    

    return card;

}