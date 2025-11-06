package com.timerdar.farmCrm.ui;

import com.timerdar.farmCrm.dto.OrderWithNameAndWeightable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class OrdersListView extends VerticalLayout implements BeforeEnterObserver {

	private Long id;
	private Grid<OrderWithNameAndWeightable> grid;
	private Component placeholder = new Div();
	private Button createOrderButton;
	private Dialog createOrder;

	@Autowired
	public OrdersListView(){
		grid = getGrid();
		createOrder = new Dialog();
		createOrderButton = createOrderButton();

		setHeightFull();
		add(createOrder, placeholder, createOrderButton, grid);
	}

	public abstract List<OrderWithNameAndWeightable> getData();
	public abstract Component getEditableEntity(Long id);
	public abstract Component getGridItem(OrderWithNameAndWeightable order);
	public abstract Dialog getCreateOrderDialog();

	public void renderEntity() {
		Component entity = getEditableEntity(id);
		replace(placeholder, entity);
		placeholder = entity;
	}

	public Grid<OrderWithNameAndWeightable> getGrid(){
		Grid<OrderWithNameAndWeightable> grid = new Grid<>();
		grid.setEmptyStateComponent(new Div("Заказы не найдены :("));
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
		grid.setHeightFull();
		grid.setSelectionMode(Grid.SelectionMode.NONE);

		grid.addColumn(new ComponentRenderer<>(this::getGridItem));

		return grid;
	}

	public void refreshGrid(){
		grid.setItems(getData());
	}

	public void renderDialog(){
		createOrder = getCreateOrderDialog();
		createOrderButton.addClickListener(e -> createOrder.open());
	}

	public void setEntityId(long id){
		this.id = id;
	}

	public Long getEntityId(){
		return id;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		setEntityId(Long.parseLong(beforeEnterEvent.getRouteParameters().get("id").orElse("-1")));
		renderEntity();
		refreshGrid();
		renderDialog();
	}

	public Button createOrderButton(){
		Button button = new Button();
		button.setText("Создать заказ");
		button.setWidthFull();
		button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		return button;
	}

}
