import { Auth } from "../routes/Auth.js";
import { Menu } from "../routes/Menu.js";
import { NotFound } from "../routes/NotFound.js";
import { Consumers } from "../routes/consumers/Consumers.js";
import { Products } from "../routes/products/Products.js";
import { Delivery } from "../routes/delivery/Delivery.js"
import { OrdersOfConsumer } from "../routes/orders/OrdersOfConsumer.js";
import { OrdersOfProduct } from "../routes/orders/OrdersOfProduct.js";
import { DeliverySummary } from "../routes/delivery/DeliverySummary.js";
import { IconButton } from "../components/IconButton.js";
import { navigateTo } from "./navigate.js";

const routes = {
    '^/menu$': Menu,
    '^/consumers/(.*?)/orders$': OrdersOfConsumer,
    '^/products/(.*?)/orders$': OrdersOfProduct,
    '^/consumers$': Consumers,
    '^/products$': Products,
    '^/delivery/summary$': DeliverySummary,
    '^/delivery$': Delivery,
    '^/$': Auth
}

export function router() {
    const hash = location.hash.slice(1);

    let page = NotFound;

    if (hash === '') {
        page = Auth;
    } else {
        for (const route in routes) {
            const regex = RegExp(route);
            if (regex.test(hash)) {
                page = routes[route];
                break;
            }
        };
    }

    const body = document.getElementById('body');
    if(body.innerHTML == ''){
        body.appendChild(IconButton("/src/assets/list.png", () => {navigateTo('/menu')}));
        body.appendChild(IconButton("/src/assets/refresh-page-option.png", () => {window.location.reload()}));
    }

    const app = document.getElementById('app');

    app.innerHTML = '<div class="loader">Загрузка...</div>';

    (async () => {
        const s = await page();
        app.innerHTML = '';
        app.appendChild(s);
    })();
};