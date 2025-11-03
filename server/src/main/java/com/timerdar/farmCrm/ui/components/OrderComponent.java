package com.timerdar.farmCrm.ui.components;

import com.timerdar.farmCrm.dto.OrderChangeRequest;
import com.timerdar.farmCrm.dto.OrderWithNameAndWeightable;
import com.timerdar.farmCrm.model.Order;
import com.timerdar.farmCrm.model.OrderStatus;
import com.timerdar.farmCrm.service.OrderService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;

public class OrderComponent extends HorizontalLayout {

	private Span nameLabel;
	private Div countDisplay;        // Отображение количества
	private IntegerField countEditor; // Редактируемое поле количества
	private Div weightDisplay;
	private NumberField weightEditor;
	private Button countSaveButton;          // Кнопка сохранения edit
	private Button weightSaveButton;          // Кнопка сохранения edit
	private final Span costLabel;

	private long id;
	private int count;
	private OrderStatus status;
	private double weight;
	private boolean isWeighed;
	private final String name;
	private int cost;

	private final OrderService orderService;

	public OrderComponent(OrderWithNameAndWeightable order, OrderService orderService, Runnable updateMainEntity, Runnable updateGrid){
		this.id = order.getId();
		this.isWeighed = order.isWeighed();
		this.weight = order.getWeight();
		this.count = order.getCount();
		this.status = order.getStatus();

		this.name = order.getName();
		this.cost = order.getCost();

		this.orderService = orderService;

		setAlignItems(Alignment.CENTER);
		setSpacing(true);
		setPadding(true);

		nameLabel = new Span(name);
		nameLabel.setWidth("30%");

		costLabel = new Span(String.format("%d руб.", cost));
		costLabel.setWidth("20%");

		countSaveButton = new Button("Сохранить");
		countSaveButton.setVisible(false);
		weightSaveButton = new Button("Сохранить");
		weightSaveButton.setVisible(false);

		Button actionButton;
		if (status == OrderStatus.CREATED){
			countDisplay = new Div();
			countDisplay.setText(String.format("%d шт.", count));
			countDisplay.getStyle().set("cursor", "pointer");
			countDisplay.setWidth("20%");
			countDisplay.getElement().addEventListener("click", e -> enableEditingCount());

			countEditor = new IntegerField();
			countEditor.setPlaceholder("ШТ:");
			countEditor.setMin(0);
			countEditor.setValue(count);
			countEditor.setWidth("20%");
			countEditor.setVisible(false);

			countSaveButton.addClickListener(e -> saveCount());

			actionButton = new Button(new Icon(VaadinIcon.CART_O));
			actionButton.addClickListener(e -> changeStatusAction(updateGrid, updateMainEntity, "DELIVERY"));

			ConfirmDialog dialog = new ConfirmDialog();

			dialog.setHeader("Удаление заказа");
			dialog.setText("Вы уверены, что готовы удалить заказ " + name + " " + count + " шт");

			dialog.setCancelable(true);
			dialog.setCancelText("Отмена");
			dialog.addCancelListener(e -> e.getSource().close());

			dialog.setConfirmText("Удалить");
			dialog.addConfirmListener(e -> changeStatusAction(updateGrid, updateMainEntity, "DELETED"));

			Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
			deleteButton.addClickListener(e -> dialog.open());

			add(dialog, nameLabel, countDisplay, countEditor, countSaveButton, actionButton, deleteButton);
		}else {
			add(nameLabel);
			actionButton = new Button(new Icon(VaadinIcon.LEVEL_LEFT));
			actionButton.addClickListener(e -> changeStatusAction(updateGrid, updateMainEntity, "CREATED"));

			countDisplay = new Div();
			countDisplay.setText(String.format("%d шт.", count));
			countDisplay.getStyle().set("cursor", "pointer");
			countDisplay.setWidth("20%");
			countDisplay.getElement().addEventListener("click", e -> enableEditingCount());

			countEditor = new IntegerField();
			countEditor.setPlaceholder("ШТ:");
			countEditor.setMin(0);
			countEditor.setValue(count);
			countEditor.setWidth("20%");
			countEditor.setVisible(false);

			countSaveButton.addClickListener(e -> saveCount());

			if(isWeighed){
				weightDisplay = new Div();
				weightDisplay.setText(String.format("%.3f кг", weight));
				weightDisplay.getStyle().set("cursor", "pointer");
				weightDisplay.setWidth("20%");
				weightDisplay.getElement().addEventListener("click", e -> enableEditingWeight());

				weightEditor = new NumberField();
				weightEditor.setPlaceholder("КГ:");
				weightEditor.setMin(0);
				weightEditor.setValue(weight);
				weightEditor.setWidth("20%");
				weightEditor.setVisible(false);

				weightSaveButton.addClickListener(e -> saveWeight());
				add(countDisplay, countEditor, countSaveButton, weightDisplay, weightEditor, weightSaveButton, costLabel,	 actionButton);
			}else{
				add(countDisplay, countEditor, countSaveButton, costLabel, actionButton);
			}
		}

		countSaveButton.addClickListener(e -> updateMainEntity.run());
		weightSaveButton.addClickListener(e -> updateMainEntity.run());
	}

	private void enableEditingWeight(){
		weightEditor.setValue(weight);
		weightDisplay.setVisible(false);
		weightEditor.setVisible(true);
		weightSaveButton.setVisible(true);
	}

	private void saveWeight(){
		OrderChangeRequest req = new OrderChangeRequest();
		req.setId(id);
		req.setWeight(weightEditor.getValue());
		Order order = orderService.changeWeight(req);
		updateOrder(order);
		weightDisplay.setVisible(true);
		weightEditor.setVisible(false);
		weightSaveButton.setVisible(false);
	}

	private void enableEditingCount(){
		countEditor.setValue(count);
		countDisplay.setVisible(false);
		countEditor.setVisible(true);
		countSaveButton.setVisible(true);
	}

	private void saveCount(){
		OrderChangeRequest req = new OrderChangeRequest();
		req.setId(id);
		req.setAmount(countEditor.getValue());
		Order order = orderService.changeAmount(req);
		updateOrder(order);
		countDisplay.setVisible(true);
		countEditor.setVisible(false);
		countSaveButton.setVisible(false);
	}

	private void updateOrder(Order order){
		this.cost = order.getCost();
		this.count = order.getCount();
		this.weight = order.getWeight();

		costLabel.setText(String.format("%d руб.", cost));
		countDisplay.setText(String.format("%d шт.", count));
		if (weightDisplay != null)
			weightDisplay.setText(String.format("%.3f кг.", weight));
	}

	private void changeStatusAction(Runnable updateGrid, Runnable updateMainEntity, String newStatus){
		OrderChangeRequest req = new OrderChangeRequest();
		req.setId(id);
		req.setStatus(newStatus);
		orderService.changeStatus(req);
		updateGrid.run();
		updateMainEntity.run();
	}
}
