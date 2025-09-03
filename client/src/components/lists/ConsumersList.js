import { ConsumerCard } from "../cards/ConsumerCard.js";

export function ConsumersList(consumers, onClick){

    const ul = document.createElement("ul");

    function render (data) {
        ul.innerHTML = '';
        data.forEach(consumer => {
            const li = document.createElement("li");
            li.appendChild(ConsumerCard(consumer, onClick));
            ul.appendChild(li);
    })
    };
    
    render(consumers);

    ul.update = render;

    return ul;

}