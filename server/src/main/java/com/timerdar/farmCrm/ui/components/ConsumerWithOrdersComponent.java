package com.timerdar.farmCrm.ui.components;

import com.timerdar.farmCrm.dto.ConsumerWithOrders;
import com.timerdar.farmCrm.dto.OrderWithNameAndWeightable;
import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.model.OrderStatus;
import com.timerdar.farmCrm.service.OrderService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumerWithOrdersComponent extends Details {

	//TODO сделать обновление списка заказов при переводе заказа в CREATED

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
				new Div("Сумма " + getSum(consumer.getOrders()) + " руб.")
		);
		layout.setSpacing(false);
		card.setSubtitle(layout);
		if (isAllDone(consumer.getOrders()))
			card.getStyle().set("background-color", "#4caf50");
		return card;
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
