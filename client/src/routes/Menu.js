import { Button } from "../components/Button.js";
import { navigateTo } from "../core/navigate.js";

export function Menu() {
    const div = document.createElement('div');
    div.className = 'container';

    const btn = Button('Заказчики', () => navigateTo('/consumers'));
    const productsBtn = Button('Продукция', () => navigateTo('/products'));
    const deliveryBtn = Button('Доставка', () => navigateTo('/delivery'));

    div.appendChild(btn);
    div.appendChild(productsBtn);
    div.appendChild(deliveryBtn);

    return div;
}