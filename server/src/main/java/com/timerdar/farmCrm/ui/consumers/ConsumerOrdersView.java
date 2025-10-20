package com.timerdar.farmCrm.ui.consumers;

import com.timerdar.farmCrm.dto.ConsumerChangeRequest;
import com.timerdar.farmCrm.dto.OrderWithNameAndWeightable;
import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.model.OrderStatus;
import com.timerdar.farmCrm.service.ConsumerService;
import com.timerdar.farmCrm.service.OrderService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route("consumers/:id/orders")
public class ConsumerOrdersView extends VerticalLayout implements BeforeEnterObserver {

	private final String SOURCE = "consumers";

	private final ConsumerService consumerService;
	private final OrderService orderService;

	private long consumerId;
	private boolean isEditMode = true;

	private final Grid<OrderWithNameAndWeightable> ordersGrid = new Grid<>();

	@Autowired
	public ConsumerOrdersView(ConsumerService consumerService, OrderService orderService){
		this.consumerService = consumerService;
		this.orderService = orderService;


		ordersGrid.setEmptyStateComponent(new Div("Созданных заказов нет :("));

		refreshOrders();
		add(ordersGrid);
	}

	private void refreshOrders(){
		ordersGrid.setItems(orderService.getOrdersWithName(consumerId, SOURCE, "CREATED"));
	}

	private void renderConsumer(){
		Consumer consumer = consumerService.getConsumerById(consumerId);

		FormLayout formLayout = new FormLayout();
		TextField name = new TextField("Имя");
		name.setValue(consumer.getName());
		name.setReadOnly(true);
		name.setValueChangeMode(ValueChangeMode.EAGER);

		TextField address = new TextField("Адрес");
		address.setValue(consumer.getAddress());
		address.setReadOnly(true);
		address.setValueChangeMode(ValueChangeMode.EAGER);

		TextField phone = new TextField("Телефон");
		phone.setValue(consumer.getPhone());
		phone.setReadOnly(true);
		phone.setValueChangeMode(ValueChangeMode.EAGER);


		//TODO добавить отмену изменений при кнопке
		Button changeMode = new Button(new Icon(VaadinIcon.EDIT));
		changeMode.addClickListener(e -> {
			changeMode.setIcon(!isEditMode ? new Icon(VaadinIcon.EDIT) : new Icon(VaadinIcon.CHECK));
			changeMode.addThemeVariants(!isEditMode ? ButtonVariant.LUMO_ICON : ButtonVariant.LUMO_SUCCESS);

			name.setReadOnly(!isEditMode);
			phone.setReadOnly(!isEditMode);
			address.setReadOnly(!isEditMode);

			if(isEditMode)
				consumerService.updateConsumer(
						new ConsumerChangeRequest(
								consumerId, name.getValue(), address.getValue(), phone.getValue()
						)
				);

			isEditMode = !isEditMode;
		});

		formLayout.addFormRow(name, address);
		formLayout.addFormRow(phone, changeMode);

		addComponentAsFirst(formLayout);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		this.consumerId = Long.parseLong(beforeEnterEvent.getRouteParameters().get("id").get());

		renderConsumer();
		refreshOrders();
	}
}
