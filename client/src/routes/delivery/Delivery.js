import { Button } from "../../components/Button.js";
import { ConsumerWithOrders } from "../../components/cards/ConsumerWithOrders.js";
import { IconButton } from "../../components/IconButton.js";
import { navigateTo } from "../../core/navigate.js";
import { setConsumersOrder } from "../../services/consumer-service.js";
import { clearDelivery, getDeliveryOrders } from "../../services/order-service.js";

export async function Delivery() {

    const div = document.createElement("div");
    div.className = 'container';

    let isEditing = false;
    let draggedItem = null;

    const seeSummaryOfProductsBtn = Button("Сводку по продукции", () => { navigateTo('/delivery/summary') });
    const cleanDeliveryBtn = Button('Очистить доставку', async () => {
        if (confirm("Точно готовы почистить доставку?")) {
            const res = await clearDelivery();
            if (res) navigateTo('/menu');
        }
    });
    const changeOrder = Button("Поменять порядок доставки", changeOrderF);
    const saveOrder = Button("Сохранить порядок доставки", saveOrderF);
    saveOrder.style.display = 'none';
    saveOrder.style.backgroundColor = 'red';


    function changeOrderF() {
        isEditing = true;
        enableDragNDrop(true);
        changeOrder.style.display = 'none';
        saveOrder.style.display = 'inline-block';
    }

    function saveOrderF() {
        isEditing = false;
        enableDragNDrop(false);
        changeOrder.style.display = 'inline-block';
        saveOrder.style.display = 'none';

        const orderData = [];
        div.querySelectorAll('.consumer-with-orders-card').forEach((item, index) => {
            orderData.push({ consumerId: item.id, deliveryOrderNumber: index + 1 });
        });

        setConsumersOrder(orderData).then(
            resp => {
                if (resp) {
                    window.location.reload();
                }
            }
        );
    }

    function enableDragNDrop(enable) {
        const items = div.querySelectorAll('.consumer-with-orders-card');
        items.forEach(item => {
            item.draggable = enable;
            if (enable) {
                item.style.cursor = 'move';
            } else {
                item.style.cursor = 'default';
            }
        });
    }

    div.addEventListener('dragstart', (e) => {
        if (!isEditing) return;
        draggedItem = e.target;
        e.dataTransfer.effectAllowed = 'move';
    })

    div.addEventListener('dragover', (e) => {
        if (!isEditing) return;
        e.preventDefault();
        const target = e.target;
        if (target && target !== draggedItem && target.classList.contains('consumer-with-orders-card')) {
            const bounding = target.getBoundingClientRect();
            const offset = e.clientY - bounding.top;
            if (offset > bounding.height / 2) {
                target.style.borderBottom = '2px solid #000';
                target.style.borderTop = '';
            } else {
                target.style.borderTop = '2px solid #000';
                target.style.borderBottom = '';
            }
        }
    });

    div.addEventListener('dragleave', (e) => {
        const target = e.target;
        if (target && target.classList.contains('consumer-with-orders-card')) {
            target.style.borderTop = '';
            target.style.borderBottom = '';
        }
    });

    div.addEventListener('drop', (e) => {
        e.preventDefault();
        if (!isEditing || !draggedItem) return;
        const target = e.target;
        if (target && target !== draggedItem && target.classList.contains('consumer-with-orders-card')) {
            const bounding = target.getBoundingClientRect();
            const offset = e.clientY - bounding.top;
            if (offset > bounding.height / 2) {
                target.style.borderBottom = '';
                target.parentNode.insertBefore(draggedItem, target.nextSibling);
            } else {
                target.style.borderTop = '';
                target.parentNode.insertBefore(draggedItem, target);
            }
        }
        draggedItem = null;
    });





    div.appendChild(changeOrder);
    div.appendChild(saveOrder);
    div.appendChild(seeSummaryOfProductsBtn);
    div.appendChild(cleanDeliveryBtn);

    const delivery = await getDeliveryOrders();

    for (let consumer of delivery) {
        div.appendChild(ConsumerWithOrders(consumer, consumer.orders));
    }




    return div;

}
