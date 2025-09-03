import { createOrder } from "../../services/order-service.js";
import { getShortProductsList } from "../../services/product-service.js";
import { IconButton } from "../IconButton.js";

export async function OrderCreation(consumerId, updateClick) {

    var unfound;

    const div = document.createElement("div");
    div.className = 'creation-card';

    const label = document.createElement("label");
    label.innerText = 'Создание заказа';
    div.appendChild(label);

    const mainDiv = document.createElement("div");

    const dropdown = document.createElement("div");
    dropdown.className = 'dropdown-div';
    mainDiv.appendChild(dropdown);

    const productNameInput = document.createElement("input");
    productNameInput.type = 'text';
    productNameInput.autocomplete = 'off';
    productNameInput.className = 'creation-input';
    productNameInput.placeholder = 'Название продукта';
    dropdown.appendChild(productNameInput);

    const dropdownList = document.createElement("div");
    dropdownList.className = 'dropdown-list';
    dropdown.appendChild(dropdownList);

    const products = await getShortProductsList();
    console.log(products);


    productNameInput.addEventListener('input', () => {
        localStorage.removeItem("creationProductId");
        dropdownList.innerHTML = '';
        const query = productNameInput.value.toLocaleLowerCase();
        if (!query) {
            dropdownList.style.display = 'none';
            return;
        }

        const filtered = products.filter(p => p.name.toLowerCase().includes(query));

        if (filtered.length === 0) {
            dropdownList.innerHTML = `<div>Не найден</div>`;
            unfound = true;
        } else {
            unfound = false;
            filtered.forEach(product => {
                const item = document.createElement("div");
                item.innerText = product.name;
                item.addEventListener('click', () => {
                    productNameInput.value = product.name;
                    localStorage.setItem('creationProductId', product.id);
                    dropdownList.style.display = 'none';
                });
                dropdownList.appendChild(item);
            })
        }

        dropdownList.style.display = 'block';

    });

    mainDiv.appendChild(dropdown);

    const countInput = document.createElement("input");
    countInput.type = 'number';
    countInput.className = 'creation-input';
    countInput.placeholder = 'Количество';
    mainDiv.appendChild(countInput);

    mainDiv.appendChild(IconButton('/src/assets/accept.png', async () => {
        if (!unfound) {
            await createOrder(consumerId, localStorage.getItem('creationProductId'), Number(countInput.value));
            updateClick();
        }
    }))

    mainDiv.appendChild(IconButton('/src/assets/close.png', () => {
        updateClick();
    }))
    div.appendChild(mainDiv);

    return div;
}