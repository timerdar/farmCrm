package com.timerdar.farmCrm.ui.consumers;

import com.timerdar.farmCrm.dto.OrderWithNameAndWeightable;
import com.timerdar.farmCrm.model.OrderStatus;
import com.timerdar.farmCrm.service.ConsumerService;
import com.timerdar.farmCrm.service.OrderService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route("consumers/:id/orders")
public class ConsumerOrdersView extends VerticalLayout implements BeforeEnterObserver {

	private final String SOURCE = "consumers";

	private final ConsumerService consumerService;
	private final OrderService orderService;

	private long consumerId;

	private final Div consumerInfo = new Div();
	private final Grid<OrderWithNameAndWeightable> ordersGrid = new Grid<>();

	@Autowired
	public ConsumerOrdersView(ConsumerService consumerService, OrderService orderService){
		this.consumerService = consumerService;
		this.orderService = orderService;

		add(consumerInfo, ordersGrid);
	}

	private void refreshOrders(){
		ordersGrid.setItems(orderService.getOrdersWithName(consumerId, SOURCE, "CREATED"));
	}

	private void renderConsumer(){

	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		String id = beforeEnterEvent.getRouteParameters().get("id").get();
		renderConsumer();
		refreshOrders();
	}
}
