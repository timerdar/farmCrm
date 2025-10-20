package com.timerdar.farmCrm.ui.consumers;

import com.timerdar.farmCrm.dto.CreateConsumerRequest;
import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.service.ConsumerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouteParameters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;


@Route("consumers")
@RouteAlias("")
public class ConsumersView extends VerticalLayout {

	private final ConsumerService consumerService;
	private Grid<Consumer> grid;

	@Autowired
	public ConsumersView(ConsumerService consumerService){
		this.consumerService = consumerService;
		setAlignItems(Alignment.CENTER);

		searchBox();
		createButton();
		createGrid();
	}

	private void searchBox(){
		HorizontalLayout search = new HorizontalLayout();

		TextField searchField = new TextField();
		searchField.setPlaceholder("Введите имя");
		searchField.setWidthFull();
		searchField.setValueChangeMode(ValueChangeMode.EAGER);
		searchField.addValueChangeListener(e ->
				filterGrid(e.getValue()));

		search.add(new Icon(VaadinIcon.SEARCH));
		search.add(searchField);
		search.setWidth("50%");
		search.setAlignItems(Alignment.CENTER);
		add(search);
	}

	private void createButton(){
		Dialog creationDialog = creationDialog();
		Button create = new Button("Создать", e -> creationDialog.open());
		create.setWidthFull();
		create.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		add(create);
	}

	private void createGrid(){
		Grid<Consumer> grid = new Grid<>(Consumer.class, false);
		grid.setEmptyStateComponent(new Div("Заказчики не найдены :("));
		grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
		grid.setHeight("500px");
		grid.addSelectionListener(e -> {
			UI.getCurrent().navigate(ConsumerOrdersView.class,
					new RouteParameters("id", String.valueOf(e.getFirstSelectedItem().get().getId())));
		});

		grid.addColumn(new ComponentRenderer<>(this::consumerCard));
		this.grid = grid;
		refreshGrid();
		add(grid);
	}

	private void refreshGrid(){
		grid.setItems(consumerService.getAllConsumers());
	}

	private void filterGrid(String filter){
		grid.setItems(
				consumerService.getAllConsumers()
						.stream().filter(consumer ->
								consumer.getName().toLowerCase().contains(filter.toLowerCase()))
						.collect(Collectors.toList())
		);
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

	private Card consumerCard(Consumer consumer){
		Card card = new Card();
		card.setTitle(consumer.getName());
		card.setSubtitle(new Div(consumer.getAddress()));
		return card;
	}

}
