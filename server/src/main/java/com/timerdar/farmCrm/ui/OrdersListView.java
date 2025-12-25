package com.timerdar.farmCrm.ui;

import com.timerdar.farmCrm.dto.OrderChangeRequest;
import com.timerdar.farmCrm.dto.OrderWithNameAndWeightable;
import com.timerdar.farmCrm.model.OrderStatus;
import com.timerdar.farmCrm.service.OrderService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
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

	protected final OrderService orderService;

	public OrdersListView(OrderService orderService){
		this.orderService = orderService;

		createGrid();
		createOrder = new Dialog();
		createOrderButton = createOrderButton();
		createContextMenu();

		setHeightFull();
		add(createOrder, placeholder, createOrderButton, grid);
	}

	public abstract List<OrderWithNameAndWeightable> getData();
	public abstract Component getEditableEntity(Long id);
	public abstract Dialog getCreateOrderDialog();

	public void renderEntity() {
		Component entity = getEditableEntity(id);
		replace(placeholder, entity);
		placeholder = entity;
	}

	public void createGrid(){
		grid = new Grid<>();
		configureGrid();
		createColumns();

	}

	private void configureGrid(){
		grid.setEmptyStateComponent(new Div("Заказы не найдены :("));
		grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
		grid.setHeightFull();
		grid.setWidthFull();
		grid.setSelectionMode(Grid.SelectionMode.NONE);
	}

	private void createColumns(){
		createNameColumn();
		createCountColumn();
		createWeightColumn();
		createCostColumn();
	}

	private void createNameColumn(){
		grid.addColumn(OrderWithNameAndWeightable::getName).setWidth("40%");
	}

	private void createCountColumn(){
		grid.addColumn(new ComponentRenderer<>(order -> {
			Div wrapper = new Div();
			wrapper.setWidthFull();

			Span text = new Span(order.getCount() + " шт");
			wrapper.add(text);

			text.addClickListener(e -> {
				wrapper.removeAll();

				IntegerField field = new IntegerField();
				field.setWidthFull();
				field.setRequired(true);
				field.setValue(order.getCount());
				wrapper.add(field);
				field.focus();

				field.addBlurListener(ev -> {
					changeCount(order.getId(), field.getValue());
					wrapper.removeAll();
					wrapper.add(new Span(order.getCount() + " шт"));
				});
			});

			return wrapper;
		})).setWidth("20%");
	}

	private void createWeightColumn(){
		grid.addColumn(new ComponentRenderer<>(order -> {
			Div wrapper = new Div();
			wrapper.setWidthFull();

			Span text = new Span(order.isWeighed() ? order.getWeight() + " кг" : "-");
			wrapper.add(text);

			if(order.isWeighed()) {
				text.addClickListener(e -> {
					wrapper.removeAll();

					NumberField field = new NumberField();
					field.setWidthFull();
					field.setRequired(true);
					field.setValue(order.getWeight());
					wrapper.add(field);
					field.focus();

					field.addBlurListener(ev -> {
						changeWeight(order.getId(), field.getValue());
						wrapper.removeAll();
						wrapper.add(new Span(order.getWeight() + " кг"));
					});

				});
			}

			return wrapper;
		})).setWidth("20%");
	}

	private void createCostColumn(){
		grid.addColumn(new ComponentRenderer<>(item -> {
					Span span = new Span(item.getCost() + " руб.");
					return span;
		})).setWidth("20%");
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

	public void createContextMenu(){
		GridContextMenu<OrderWithNameAndWeightable> menu = new GridContextMenu<OrderWithNameAndWeightable>(grid);

		menu.addItem("В доставку", e -> e.getItem().ifPresent(order -> {
			System.out.println("В доставку перенесен заказ " + order.getId());
		}));

		menu.addItem("Удалить", e -> e.getItem().ifPresent(order -> {
			System.out.println("Удален заказ " + order.getId());
		}));
	}

	private void changeWeight(long id, double newWeight){
		OrderChangeRequest req = new OrderChangeRequest();
		req.setId(id);
		req.setWeight(newWeight);
		orderService.changeWeight(req);
		refreshGrid();
	}

	private void changeCount(long id, int newCount){
		OrderChangeRequest req = new OrderChangeRequest();
		req.setId(id);
		req.setAmount(newCount);
		orderService.changeAmount(req);
		refreshGrid();
	}
}

