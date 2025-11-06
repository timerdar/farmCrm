package com.timerdar.farmCrm.ui.consumers;

import com.timerdar.farmCrm.dto.ConsumerChangeRequest;
import com.timerdar.farmCrm.dto.CreateOrderRequest;
import com.timerdar.farmCrm.dto.OrderWithNameAndWeightable;
import com.timerdar.farmCrm.dto.ProductWithOrdersCount;
import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.model.OrderStatus;
import com.timerdar.farmCrm.model.Product;
import com.timerdar.farmCrm.service.ConsumerService;
import com.timerdar.farmCrm.service.OrderService;
import com.timerdar.farmCrm.service.ProductService;
import com.timerdar.farmCrm.ui.components.OrderComponent;
import com.timerdar.farmCrm.ui.OrdersListView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("consumers/:id/orders")
public class ConsumerOrdersView extends OrdersListView {


	private final ConsumerService consumerService;
	private final OrderService orderService;
	private final ProductService productService;

	@Autowired
	public ConsumerOrdersView(ConsumerService consumerService, OrderService orderService, ProductService productService){
		this.consumerService = consumerService;
		this.orderService = orderService;
		this.productService = productService;
	}

	@Override
	public List<OrderWithNameAndWeightable> getData() {
		return orderService.getOrdersWithName(getEntityId(), "consumers", OrderStatus.CREATED.toString());
	}

	@Override
	public Component getEditableEntity(Long id) {
		Consumer consumer = consumerService.getConsumerById(id);

		FormLayout formLayout = new FormLayout();
		TextField name = new TextField("Имя");
		name.setValue(consumer.getName());
		name.setReadOnly(true);
		name.setWidthFull();

		TextField address = new TextField("Адрес");
		address.setValue(consumer.getAddress());
		address.setReadOnly(true);
		address.setWidthFull();

		TextField phone = new TextField("Телефон");
		phone.setValue(consumer.getPhone());
		phone.setReadOnly(true);
		phone.setWidthFull();

		IntegerField totalSum = new IntegerField("Сумма выкупа");
		totalSum.setValue(consumer.getTotalSum());
		totalSum.setReadOnly(true);
		totalSum.setWidthFull();

		Div buttons = new Div();

		Button changeMode = new Button(new Icon(VaadinIcon.EDIT), event -> {
			name.setReadOnly(false);
			phone.setReadOnly(false);
			address.setReadOnly(false);
			buttons.setVisible(true);
			event.getSource().setVisible(false);
		});

		Button saveData = new Button(new Icon(VaadinIcon.CHECK), event -> {
			consumerService.updateConsumer(
					new ConsumerChangeRequest(id, name.getValue(), phone.getValue(), address.getValue()));
			name.setReadOnly(true);
			phone.setReadOnly(true);
			address.setReadOnly(true);
			buttons.setVisible(false);
			changeMode.setVisible(true);
		});

		Button cancelUpdate = new Button(new Icon(VaadinIcon.CLOSE), event -> {
			name.setReadOnly(true);
			phone.setReadOnly(true);
			address.setReadOnly(true);
			buttons.setVisible(false);
			changeMode.setVisible(true);
		});

		buttons.add(saveData, cancelUpdate);
		buttons.setVisible(false);

		formLayout.addFormRow(name, address,phone, totalSum);
		formLayout.add(buttons, changeMode);

		//TODO обновить на несколько столбцов
		formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

		return formLayout;
	}

	@Override
	public Component getGridItem(OrderWithNameAndWeightable order) {
		return new OrderComponent(order, orderService, this::renderEntity, this::refreshGrid);
	}

	@Override
	public Dialog getCreateOrderDialog() {
		Consumer consumer = consumerService.getConsumerById(getEntityId());
		Dialog dialog = new Dialog();

		dialog.setHeaderTitle("Создать заказ");

		VerticalLayout layout = new VerticalLayout();

		ComboBox<ProductWithOrdersCount> productChooser = new ComboBox<>();
		productChooser.setLabel("Продукт");
		productChooser.setItemLabelGenerator(Product::getName);
		productChooser.setItems(productService.getProductsList());
		productChooser.setRequired(true);

		IntegerField countField = new IntegerField("Количество");
		countField.setMin(0);
		countField.setPlaceholder("ШТ:");
		countField.setRequired(true);

		layout.add("Создание заказа для: " + consumer.getName() + " (" + consumer.getAddress() + ")");
		layout.add(productChooser, countField);

		dialog.add(layout);

		Button cancelButton = new Button("Закрыть");
		cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancelButton.addClickListener(e -> dialog.close());

		Button saveButton = new Button("Создать");
		saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		saveButton.addClickListener(e -> {
			if (productChooser.isEmpty() || countField.isEmpty()){
				Notification.show("Заполните все поля");
			}else{
				CreateOrderRequest req = new CreateOrderRequest(productChooser.getValue().getId(), consumer.getId(), countField.getValue());
				orderService.createOrder(req);
				dialog.close();
				productChooser.clear();
				countField.clear();
				refreshGrid();
			}

		});

		dialog.getFooter().add(cancelButton, saveButton);
		return dialog;
	}
}
