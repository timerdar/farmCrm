package com.timerdar.farmCrm.ui.delivery;

import com.timerdar.farmCrm.dto.ConsumerWithOrders;
import com.timerdar.farmCrm.service.OrderService;
import com.timerdar.farmCrm.ui.MainView;
import com.timerdar.farmCrm.ui.components.ConsumerWithOrdersComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "delivery", layout = MainView.class)
public class DeliveryView extends VerticalLayout {

	private Grid<ConsumerWithOrders> grid;
	private OrderService orderService;

	@Autowired
	public DeliveryView(OrderService orderService){
		this.orderService = orderService;

		grid = getGrid();
		refreshGrid();
		add(grid);
		setPadding(false);
	}

	private Grid<ConsumerWithOrders> getGrid(){
		Grid<ConsumerWithOrders> grid = new Grid<>();
		grid.setEmptyStateComponent(new Div("Записи не найдены :("));
		grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
		grid.setSelectionMode(Grid.SelectionMode.NONE);

		grid.addColumn(new ComponentRenderer<>(this::getGridItem));

		return grid;
	}

	private List<ConsumerWithOrders> getData(){
		return orderService.getDeliveryData();
	}

	private void refreshGrid(){
		grid.setItems(getData());
	}

	private Component getGridItem(ConsumerWithOrders consumerWithOrders){
		return new ConsumerWithOrdersComponent(consumerWithOrders, this.orderService);
	}
}
