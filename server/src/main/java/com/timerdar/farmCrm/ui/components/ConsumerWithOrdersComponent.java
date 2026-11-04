package com.timerdar.farmCrm.ui.components;

import com.timerdar.farmCrm.dto.DeliveryStop;
import com.timerdar.farmCrm.dto.OrderWithNameAndWeightable;
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

import java.util.List;

public class ConsumerWithOrdersComponent extends Details {

	private final OrderService orderService;
	private final DeliveryStop stop;

	public ConsumerWithOrdersComponent(DeliveryStop stop, OrderService orderService) {
		this.stop = stop;
		this.orderService = orderService;

		setWidthFull();
		setSummary(header(stop.deliverySum(), stop.openCount() == 0));
		addOpenedChangeListener(event -> {
			if (event.isOpened()) {
				reloadOrders();
			}
		});
		getElement().getStyle().set("overflow-x", "auto");
	}

	public String getConsumerName() {
		return stop.name();
	}

	private void reloadOrders() {
		List<OrderWithNameAndWeightable> orders = orderService.getConsumerDeliveryOrders(stop.id());
		if (orders.isEmpty()) {
			removeFromParent();
			return;
		}
		removeAll();
		add(ordersLayout(orders));
		setSummary(header(sum(orders), allDone(orders)));
	}

	private Component ordersLayout(List<OrderWithNameAndWeightable> orders) {
		VerticalLayout list = new VerticalLayout();
		list.setPadding(false);
		list.setSpacing(false);
		list.setWidthFull();
		for (OrderWithNameAndWeightable order : orders) {
			OrderComponent orderComponent = new OrderComponent(order, orderService, this::reloadOrders, this::reloadOrders);
			orderComponent.setWidthFull();
			list.add(orderComponent);
		}
		return list;
	}

	private Component header(int sum, boolean allDone) {
		Card card = new Card();
		card.setWidthFull();
		card.setTitle(stop.name());
		VerticalLayout layout = new VerticalLayout(
				new Div(stop.address()),
				new Div(stop.phone()),
				new Div("Сумма " + sum + " руб."),
				new Button("Отправить чек", new Icon(VaadinIcon.SHARE), event -> {
					String bill = orderService.getBillOfConsumer(stop.id());
					shareText("Отправка чека " + stop.name(), bill);
					copyToClipboard(bill);
				})
		);
		layout.setSpacing(false);
		layout.setPadding(false);
		card.setSubtitle(layout);
		if (allDone) {
			card.getStyle().set("background-color", "#4caf50");
		}
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
		getUI().ifPresent(ui -> ui.getPage().executeJs("""
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
                """, text));
		Notification.show("Дополнительно текст скопирован в буфер обмена");
	}

	private boolean allDone(List<OrderWithNameAndWeightable> orders) {
		for (OrderWithNameAndWeightable order : orders) {
			if (order.getStatus() == OrderStatus.DELIVERY) {
				return false;
			}
		}
		return true;
	}

	private int sum(List<OrderWithNameAndWeightable> orders) {
		int total = 0;
		for (OrderWithNameAndWeightable order : orders) {
			total += order.getCost();
		}
		return total;
	}
}
