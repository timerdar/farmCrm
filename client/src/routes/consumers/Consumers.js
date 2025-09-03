import { Button } from "../../components/Button.js";
import { ConsumerCreation } from "../../components/creation_forms/ConsumerCreation.js";
import { ConsumersList } from "../../components/lists/ConsumersList.js";
import { navigateTo } from "../../core/navigate.js";
import { getConsumersList } from "../../services/consumer-service.js";

export async function Consumers(){

    var consumers = await getConsumersList();

    const div = document.createElement("div");
    div.className = 'container';
    
    const h1 = document.createElement("h1");
    h1.innerText = 'Заказчики';
    div.appendChild(h1);

    const searchInput = document.createElement("input");
    searchInput.type = 'text';
    searchInput.className = 'search-input';
    searchInput.placeholder = 'Начните вводить имя заказчика';
    div.appendChild(searchInput);

    const replacableDiv = document.createElement("div");
    replacableDiv.className = 'replacable-div'
    div.appendChild(replacableDiv);

    const list = ConsumersList(consumers, (id) => {
        navigateTo(`/consumers/${id}/orders`);
    });

    async function setButton(){
        const addNewConsumerBtn = Button("Добавить заказчика", setForm);
        consumers = await getConsumersList();
        list.update(consumers);
        replacableDiv.innerHTML = '';
        replacableDiv.appendChild(addNewConsumerBtn);
    }   

    function setForm(){
        const creationForm = ConsumerCreation(setButton);
        replacableDiv.innerHTML = '';
        replacableDiv.appendChild(creationForm);
    }

    await setButton();    


    searchInput.addEventListener('input', () => {
        if (searchInput.value === ''){
            list.update(consumers);
        }else{
            const newConsumers = [];
            consumers.forEach(consumer => {
                if (consumer.name.toLocaleLowerCase().includes(searchInput.value.toLowerCase())){
                    newConsumers.push(consumer);
                }
            })
            list.update(newConsumers);

        }
    })

    div.appendChild(list);

    return div;

}