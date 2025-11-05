package com.timerdar.farmCrm.ui.components;

import com.timerdar.farmCrm.dto.ConsumerWithOrders;
import com.timerdar.farmCrm.dto.OrderWithNameAndWeightable;
import com.timerdar.farmCrm.service.OrderService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumerWithOrdersComponent extends Details {

	//TODO сделать обновление списка заказов при переводе заказа в CREATED

	private OrderService orderService;

	private ConsumerWithOrders consumer;
	private List<OrderWithNameAndWeightable> orders;
	private Map<Long, Component> orderCards = new HashMap<>();

	public ConsumerWithOrdersComponent(ConsumerWithOrders consumerWithOrders, OrderService orderService){
		this.consumer = consumerWithOrders;
		this.orders = consumerWithOrders.getOrders();
		this.orderService = orderService;


		Component consumer = getConsumer();
		this.setSummary(consumer);
		Component ordersLayout = getOrdersLayout(orders);
		this.add(ordersLayout);
	}

	private Component getOrdersLayout(List<OrderWithNameAndWeightable> orders){
		VerticalLayout list = new VerticalLayout();
		for (OrderWithNameAndWeightable order: orders){
			OrderComponent orderComponent = new OrderComponent(order, orderService, this::empty, this::empty);
			orderComponent.setWidthFull();
			orderCards.put(order.getId(), orderComponent);
			list.add(orderComponent);
		}
		return list;
	}

	private Component getConsumer(){
		Card card = new Card();
		card.setTitle(consumer.getName());
		card.setSubtitle(new VerticalLayout(new Text(consumer.getAddress()), new Text(consumer.getPhone())));
		return card;
	}

	private void empty(){
	}
}
