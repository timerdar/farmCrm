package com.timerdar.farmCrm.ui;

import com.timerdar.farmCrm.dto.OrderChangeRequest;
import com.timerdar.farmCrm.dto.OrderWithNameAndWeightable;
import com.timerdar.farmCrm.model.OrderStatus;
import com.timerdar.farmCrm.service.OrderService;
import com.timerdar.farmCrm.ui.products.ProductOrdersView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Location;

import java.util.ArrayList;
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
		grid.addClassName("orders-grid");
		grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
		grid.setHeightFull();
		grid.setWidthFull();
		grid.setSelectionMode(Grid.SelectionMode.MULTI);
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
		grid.addColumn(LitRenderer.<OrderWithNameAndWeightable>of(
						"<span class=\"cell-action\" @click=${editCount}>${item.countLabel}</span>")
				.withProperty("countLabel", order -> order.getCount() + " шт")
				.withFunction("editCount", this::editCount))
				.setWidth("20%");
	}

	private void createWeightColumn(){
		grid.addColumn(LitRenderer.<OrderWithNameAndWeightable>of(
						"<span class=\"${item.weightClass}\" @click=${editWeight}>${item.weightLabel}</span>")
				.withProperty("weightLabel", order -> order.isWeighed() ? order.getWeight() + " кг" : "-")
				.withProperty("weightClass", order -> order.isWeighed() ? "cell-action" : "")
				.withFunction("editWeight", this::editWeight))
				.setWidth("20%");
	}

	private void createCostColumn(){
		grid.addColumn(order -> order.getCost() + " руб.").setWidth("20%");
	}

	private void editCount(OrderWithNameAndWeightable order) {
		openNumberDialog("Количество", order.getName(), "Штук", order.getCount(),
				value -> changeCount(order.getId(), value));
	}

	private void editWeight(OrderWithNameAndWeightable order) {
		if (!order.isWeighed()) {
			return;
		}
		openNumberDialog("Вес", order.getName(), "Килограммы", order.getWeight(),
				value -> changeWeight(order.getId(), value));
	}

	private void openNumberDialog(String title, String itemName, String fieldLabel, double current,
			java.util.function.DoubleConsumer onSave) {
		Dialog dialog = new Dialog();
		dialog.setHeaderTitle(title);
		dialog.setWidth("min(92vw, 22rem)");

		NumberField field = new NumberField(fieldLabel);
		field.setWidthFull();
		field.setMin(0);
		field.setValue(current);

		dialog.add(new Div(itemName), field);
		dialog.addOpenedChangeListener(event -> {
			if (event.isOpened()) {
				field.focus();
			}
		});

		Button cancel = new Button("Отмена", e -> dialog.close());
		Button save = new Button("Сохранить", e -> {
			if (field.getValue() == null) {
				Notification.show("Заполните поле");
				return;
			}
			onSave.accept(field.getValue());
			dialog.close();
		});
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		dialog.getFooter().add(cancel, save);
		dialog.open();
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
			var selected = grid.asMultiSelect().getValue();
			if (selected.isEmpty()) {
				moveToDelivery(order);
			} else {
				selected.forEach(this::moveToDelivery);
			}
			refreshGrid();
			Notification.show("В доставку");
		}));

		menu.addItem("Удалить", e -> e.getItem().ifPresent(order -> {
			ConfirmDialog confirmDialog = createDeletingDialog(order);
			confirmDialog.open();
		}));

		menu.addItem("ВСЕ в доставку", e -> {
			ConfirmDialog confirmDialog = createAllDeliveryDialog();
			confirmDialog.open();
		});

		menu.addItem("Посмотреть все заказы", e -> e.getItem().ifPresent(order -> {
			Location currentLocation = UI.getCurrent().getInternals().getActiveViewLocation();
			List<String> segments = currentLocation.getSegments();

			boolean isProducts = segments.get(0).equals("products");
			UI.getCurrent().navigate(isProducts ? "consumers/" + order.getConsumerId() + "/orders" : "products/" + order.getProductId() + "/orders");
		}));
	}

	private void changeWeight(long id, double newWeight){
		OrderChangeRequest req = new OrderChangeRequest();
		req.setId(id);
		req.setWeight(newWeight);
		orderService.changeWeight(req);
		refreshGrid();
	}

	private void changeCount(long id, double newCount){
		OrderChangeRequest req = new OrderChangeRequest();
		req.setId(id);
		req.setAmount(newCount);
		orderService.changeAmount(req);
		refreshGrid();
	}

	private void moveToDelivery(OrderWithNameAndWeightable order){
		OrderChangeRequest req = new OrderChangeRequest();
		req.setId(order.getId());
		req.setStatus(OrderStatus.DELIVERY.toString());
		orderService.changeStatus(req);
	}

	private void delete(OrderWithNameAndWeightable order){
		OrderChangeRequest req = new OrderChangeRequest();
		req.setId(order.getId());
		req.setStatus(OrderStatus.DELETED.toString());
		orderService.changeStatus(req);
		refreshGrid();
	}

	private void groupedToDelivery(){
		orderService.groupedToDelivery(getData());
		refreshGrid();
	}

	private ConfirmDialog createDeletingDialog(OrderWithNameAndWeightable order){
		ConfirmDialog dialog = new ConfirmDialog();

		dialog.setHeader("Удаление заказа");
		dialog.setText("Вы уверены, что готовы удалить заказ " + order.getName() + " " + order.getCount() + " шт");

		dialog.setCancelable(true);
		dialog.setCancelText("Отмена");
		dialog.addCancelListener(e -> e.getSource().close());

		dialog.setConfirmText("Удалить");
		dialog.addConfirmListener(e -> {
			delete(order);
			Notification.show("Удален").setDuration(1000);
		});

		return dialog;
	}

	private ConfirmDialog createAllDeliveryDialog(){
		ConfirmDialog dialog = new ConfirmDialog();

		dialog.setHeader("Уверены, что ВСЕ нужно перенести в доставку?");
		dialog.setText(new Div(getAllItems()));

		dialog.setCancelable(true);
		dialog.setCancelText("Отмена");
		dialog.addCancelListener(e -> e.getSource().close());

		dialog.setConfirmText("Перенести");
		dialog.addConfirmListener(e -> {
			groupedToDelivery();
			Notification.show("Все в доставке").setDuration(1000);
		});

		return dialog;
	}

	private String getAllItems(){
		StringBuilder sb = new StringBuilder();
		sb.append("Проверьте список: ");
		for (OrderWithNameAndWeightable order: getData()){
			sb.append(order.getName()).append(" ").append(order.getCount()).append(" шт");
			if(order.isWeighed())
				sb.append(" ").append(order.getWeight()).append(" кг");
			sb.append("; ");
		}
		return sb.toString();
	}
}

