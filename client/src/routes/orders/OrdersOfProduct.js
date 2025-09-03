import { getCreatedOrdersOfProduct } from "../../services/order-service.js";
import { getProduct } from "../../services/product-service.js";
import { EditableProductCard } from '../../components/cards/editable/EditableProductCard.js';
import { OrdersList } from '../../components/lists/OrdersList.js';
import { EditableOrderCard } from "../../components/cards/editable/EditableOrderCard.js";

export async function OrdersOfProduct(){

    const id = extractId();

    const div = document.createElement('div');
    div.className = 'container';

    const product = await getProduct(id);
    const orders = await getCreatedOrdersOfProduct(id);

    const productCard = EditableProductCard(product);
    div.appendChild(productCard);

    div.appendChild(document.createElement('hr'));
    
    const ordersList = OrdersList(orders, true);
    div.appendChild(ordersList);

    return div;

}

function extractId(){
    const hash = location.hash;
    const regex = /products\/(.*?)\/orders/;
    const id = hash.match(regex);
    return id[1];
}