package com.timerdar.farmCrm.ui.consumers;

import com.timerdar.farmCrm.dto.CreateConsumerRequest;
import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.service.ConsumerService;
import com.timerdar.farmCrm.ui.EntitiesListView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;


@Route("consumers")
@RouteAlias("")
public class ConsumersView extends EntitiesListView {

	private final ConsumerService consumerService;

	@Autowired
	public ConsumersView(ConsumerService consumerService){
		this.consumerService = consumerService;

		refreshGrid();
	}

	@Override
	public List<Consumer> getData() {
		return consumerService.getAllConsumers();
	}

	@Override
	public Dialog getDialog() {

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

	@Override
	public void filterGrid(String filter) {
		this.grid.setItems(filteredItems(filter));
	}

	private List<Consumer> filteredItems(String filter) {
		return getData().stream().filter(consumer ->
				consumer.getName().toLowerCase().contains(filter.toLowerCase())
		).collect(Collectors.toList());
	}

	@Override
	public String getEntityId(Object o) {
		return o instanceof Consumer ? String.valueOf(((Consumer) o).getId()) : null;
	}

	@Override
	public Card getEntityCard(Object object){
		Consumer consumer = (Consumer) object;
		Card card = new Card();
		card.setTitle(consumer.getName());
		card.setSubtitle(new Div(consumer.getAddress()));
		return card;
	}

}
