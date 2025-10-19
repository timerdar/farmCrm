package com.timerdar.farmCrm.ui.consumers;

import com.timerdar.farmCrm.dto.CreateConsumerRequest;
import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.service.ConsumerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;


@Route("consumers")
public class ConsumersView extends VerticalLayout {

	private final ConsumerService consumerService;
	private final Grid<Consumer> grid = new Grid<>(Consumer.class, false);


	@Autowired
	public ConsumersView(ConsumerService consumerService){
		this.consumerService = consumerService;

		Dialog creationDialog = creationDialog();
		Button create = new Button("Создать", e -> creationDialog.open());

		grid.addColumn(Consumer::getName).setHeader("Имя").setAutoWidth(true);
		grid.addColumn(Consumer::getAddress).setHeader("Адрес").setAutoWidth(true);
		grid.addColumn(Consumer::getPhone).setHeader("Номер телефона").setAutoWidth(true);
		grid.addColumn(new ComponentRenderer<>(consumer -> {
			Icon icon = VaadinIcon.CART.create();
			icon.addClickListener(e -> {
				UI.getCurrent().navigate(ConsumerOrdersView.class,
						new RouteParameters("id", String.valueOf(consumer.getId())));
			});
			return icon;
		})).setAutoWidth(true);
		grid.addColumn(Consumer::getTotalSum).setHeader("Сумма выкупа").setSortable(true).setAutoWidth(true);


		refreshGrid();
		add(
				creationDialog,
				create,
				grid
		);
	}

	private void refreshGrid(){
		grid.setItems(consumerService.getAllConsumers());
	}

	private Dialog creationDialog(){

		Dialog dialog = new Dialog();

		dialog.setHeaderTitle("Новый заказчик");
		dialog.setWidth("400px");

		TextField name = new TextField("Имя");
		name.setRequired(true);
		name.setWidthFull();
		TextField address = new TextField("Адрес");
		address.setRequired(true);
		address.setWidthFull();
		TextField phone = new TextField("Номер телефона");
		phone.setRequired(true);
		phone.setWidthFull();

		VerticalLayout dialogLayout = new VerticalLayout();
		dialogLayout.add(name, address, phone);

		Button create = new Button("Добавить", e -> {
			CreateConsumerRequest req = new CreateConsumerRequest();
			req.setName(name.getValue());
			req.setAddress(address.getValue());
			req.setPhone(phone.getValue());
			consumerService.createConsumer(req);
			refreshGrid();
			dialog.close();
		});
		Button cancel = new Button("Отмена", e -> dialog.close());
		dialog.getFooter().add(create, cancel);

		dialog.add(dialogLayout);
		return dialog;
	}
}
