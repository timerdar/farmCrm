package com.timerdar.farmCrm.ui;

import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.ui.consumers.ConsumerOrdersView;
import com.timerdar.farmCrm.ui.products.ProductOrdersView;
import com.vaadin.flow.component.Component;
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
import com.vaadin.flow.router.RouteParameters;

import java.util.List;

public abstract class EntitiesListView extends VerticalLayout {

	private Component search;
	private Dialog creationDialog;
	private Component creationButton;
	protected Grid grid;

	public EntitiesListView(){
		setAlignItems(Alignment.CENTER);

		search = getSearchBox();
		creationDialog = getDialog();
		creationButton = getCreationButon(creationDialog);
		grid = getGrid();

		setHeightFull();

		add(search, creationDialog, creationButton, grid);
	}

	abstract public List<?> getData();
	abstract public Dialog getDialog();
	abstract public void filterGrid(String filter);
	abstract public Card getEntityCard(Object object);
	abstract public String getEntityId(Object object);

	public void refreshGrid(){
		grid.setItems(getData());
	}

	public Button getCreationButon(Dialog creationDialog){
		Button create = new Button("Создать", e -> creationDialog.open());
		create.setWidthFull();
		create.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		return create;
	}

	public HorizontalLayout getSearchBox(){
		HorizontalLayout search = new HorizontalLayout();

		TextField searchField = new TextField();
		searchField.setPlaceholder("Введите имя");
		searchField.setWidthFull();
		searchField.setValueChangeMode(ValueChangeMode.EAGER);
		searchField.addValueChangeListener(e ->
				filterGrid(e.getValue()));

		search.add(new Icon(VaadinIcon.SEARCH));
		search.add(searchField);
		search.setWidthFull();
		search.setAlignItems(Alignment.CENTER);

		return search;
	}

	private Grid<Object> getGrid(){
		Grid<Object> grid = new Grid<>();
		grid.setEmptyStateComponent(new Div("Записи не найдены :("));
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
		grid.setHeightFull();
		grid.addSelectionListener(e -> {
			String[] className = getClass().getName().split("\\.");
			if (className[className.length - 1].startsWith("Product") || className[className.length - 1].startsWith("Current")) {
				UI.getCurrent().navigate(ProductOrdersView.class,
						new RouteParameters("id", getEntityId(e.getFirstSelectedItem().get())));
			} else if (className[className.length - 1].startsWith("Consumer")) {
				UI.getCurrent().navigate(ConsumerOrdersView.class,
						new RouteParameters("id", getEntityId(e.getFirstSelectedItem().get())));

			}
		});

		grid.addColumn(new ComponentRenderer<>(this::getEntityCard));
		return grid;
	}

	public void setDialog(Dialog newCreationDialog){
		Button newCreationButton = getCreationButon(newCreationDialog);
		replace(creationButton, newCreationButton);
		replace(creationDialog, newCreationDialog);
		creationDialog = newCreationDialog;
		creationButton = newCreationButton;
	}
}
