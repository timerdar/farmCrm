package com.timerdar.farmCrm.ui;

import com.timerdar.farmCrm.dto.OrderChangeRequest;
import com.timerdar.farmCrm.dto.OrderWithNameAndWeightable;
import com.timerdar.farmCrm.model.Order;
import com.timerdar.farmCrm.model.OrderStatus;
import com.timerdar.farmCrm.service.OrderService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;

public class OrderComponent extends HorizontalLayout {

	private Span nameLabel;
	private Div countDisplay;        // Отображение количества
	private IntegerField countEditor; // Редактируемое поле количества
	private Div weightDisplay;
	private NumberField weightEditor;
	private Button saveButton;          // Кнопка сохранения edit
	private Span costLabel;
	private Button actionButton;        // Отдельная кнопка для другого действия

	private long id;
	private int count;
	private double weight;
	private boolean isWeighed;
	private final String name;
	private int cost;

	private final OrderService orderService;

	public OrderComponent(OrderWithNameAndWeightable order, OrderService orderService, OrderStatus status){
		this.id = order.getId();
		this.isWeighed = order.isWeighed();
		this.weight = order.getWeight();
		this.count = order.getCount();

		this.name = order.getName();
		this.cost = order.getCost();

		this.orderService = orderService;

		setAlignItems(Alignment.CENTER);
		setSpacing(true);
		setWidth("520px");
		setPadding(true);

		nameLabel = new Span(name);
		nameLabel.setWidth("30%");

		costLabel = new Span(String.format("%d руб.", cost));
		costLabel.setWidth("20%");

		saveButton = new Button("Сохранить");
		saveButton.setVisible(false);

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

			saveButton.addClickListener(e -> saveCount());

			add(nameLabel, countDisplay, countEditor, saveButton, costLabel );
		}else {
			//TODO решить проблему с LaziInit
			//TODO доделать отображение orderComponent для страницы доставки
			add(nameLabel, costLabel);
		}

	}

	private void enableEditingWeight(){

	}

	private void saveWeight(){

	}

	private void enableEditingCount(){
		countEditor.setValue(count);
		countDisplay.setVisible(false);
		countEditor.setVisible(true);
		saveButton.setVisible(true);
	}

	private void saveCount(){
		OrderChangeRequest req = new OrderChangeRequest();
		req.setId(id);
		req.setAmount(countEditor.getValue());
		Order order = orderService.changeAmount(req);
		updateOrder(order);
		countDisplay.setVisible(true);
		countEditor.setVisible(false);
		saveButton.setVisible(false);
	}

	private void updateOrder(Order order){
		this.cost = order.getCost();
		this.count = order.getCount();
		this.weight = order.getWeight();

		costLabel.setText(String.format("%d руб.", cost));
		countDisplay.setText(String.format("%d шт.", count));
		if (weightDisplay != null)
			weightDisplay.setText(String.format("%2f кг.", weight));
	}
}
