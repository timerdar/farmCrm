package com.timerdar.farmCrm.ui.components;

import com.timerdar.farmCrm.dto.ConsumerWithOrders;
import com.timerdar.farmCrm.dto.OrderWithNameAndWeightable;
import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.model.OrderStatus;
import com.timerdar.farmCrm.service.OrderService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumerWithOrdersComponent extends Details {

	private OrderService orderService;

	private ConsumerWithOrders consumer;
	private List<OrderWithNameAndWeightable> orders;
	private Map<Long, Component> orderCards = new HashMap<>();

	private final Component consumerCard;

	public ConsumerWithOrdersComponent(ConsumerWithOrders consumerWithOrders, OrderService orderService){
		this.consumer = consumerWithOrders;
		this.orders = consumerWithOrders.getOrders();
		this.orderService = orderService;


		consumerCard = getConsumer();
		this.setSummary(consumerCard);
		Component ordersLayout = getOrdersLayout(orders);
		this.add(ordersLayout);
		this.getElement().getStyle().set("overflow-x", "auto");
	}

	private Component getOrdersLayout(List<OrderWithNameAndWeightable> orders){
		VerticalLayout list = new VerticalLayout();
		list.setPadding(false);
		for (OrderWithNameAndWeightable order: orders){
			OrderComponent orderComponent = new OrderComponent(order, orderService, this::updateConsumer, this::updateGrid);
			orderComponent.setWidthFull();
			orderCards.put(order.getId(), orderComponent);
			list.add(orderComponent);
		}
		list.getElement().getStyle().set("overflow-x", "auto");
		list.setHeightFull();
		return list;
	}

	private Component getConsumer(){
		Card card = new Card();
		card.setTitle(consumer.getName());
		VerticalLayout layout = new VerticalLayout(
				new Div(consumer.getAddress()),
				new Div(consumer.getPhone()),
				new Div("Сумма " + getSum(consumer.getOrders()) + " руб."),
				new Button("Отправить чек", new Icon(VaadinIcon.SHARE), e -> {
					String bill = orderService.getBillOfConsumer(consumer.getId());
					shareText("Отправка чека " + consumer.getName(), bill);
					copyToClipboard(bill);
				})
		);
		layout.setSpacing(false);
		card.setSubtitle(layout);
		if (isAllDone(consumer.getOrders()))
			card.getStyle().set("background-color", "#4caf50");
		return card;
	}

	private void shareText(String title, String text) {
		getElement().executeJs("""
        if (navigator.share) {
            navigator.share({
                title: $0,
                text: $1
            }).catch(err => console.log('Ошибка шаринга:', err));
        } else {
            navigator.clipboard.writeText($1)
                .then(() => alert('Текст скопирован в буфер'));
        }
        """, title, text);
	}

	private void copyToClipboard(String text) {
		getUI().ifPresent(ui -> {
			ui.getPage().executeJs("""
                if (navigator.clipboard && window.isSecureContext) {
                    navigator.clipboard.writeText($0).then(() => {
                        console.log('Скопировано через Clipboard API');
                    }).catch(err => {
                        fallbackCopy($0);
                    });
                } else {
                    fallbackCopy($0);
                }
                function fallbackCopy(text) {
                    const textarea = document.createElement('textarea');
                    textarea.value = text;
                    document.body.appendChild(textarea);
                    textarea.select();
                    document.execCommand('copy');
                    document.body.removeChild(textarea);
                }
                """, text);
		});
		Notification.show("Дополнительно текст скопирован в буфер обмена");
	}


	private void updateConsumer(){
		updateGrid();
		this.setSummary(getConsumer());
	}

	private void updateGrid(){
		List<OrderWithNameAndWeightable> updatedOrders = new ArrayList<>();
		updatedOrders.addAll(orderService.getOrdersWithName(consumer.getId(), "consumers", "DELIVERY"));
		updatedOrders.addAll(orderService.getOrdersWithName(consumer.getId(), "consumers", "DONE"));
		consumer.setOrders(updatedOrders);

		Component ordersLayout = getOrdersLayout(consumer.getOrders());
		this.removeAll();
		this.add(ordersLayout);
	}

	private boolean isAllDone(List<OrderWithNameAndWeightable> orders){
		for(OrderWithNameAndWeightable order: orders){
			if (order.getStatus() == OrderStatus.DELIVERY) {
				return false;
			}
		}
		return true;
	}

	private int getSum(List<OrderWithNameAndWeightable> orders){
		int s = 0;
		for (OrderWithNameAndWeightable order: orders)
			s += order.getCost();
		return s;
	}
}
