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
import com.vaadin.flow.component.page.History;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataView;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EntitiesListView extends VerticalLayout implements BeforeEnterObserver {

	private static final String PARAM_SCROLL_INDEX = "scrollIndex";

	private Component search;
	private Dialog creationDialog;
	private Component creationButton;
	protected Grid grid;
	protected DataView dataView;

	private int savedScrollIndex = 0;

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
		dataView = grid.setItems(getData());

		if (savedScrollIndex > 0) {
			grid.scrollToIndex(savedScrollIndex);
		}
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
		grid.addItemClickListener(e -> {
			if (dataView != null) {
				dataView.getItemIndex(e.getItem()).ifPresent(index -> {
					int scrollIndex = (int) index;

					UI ui = UI.getCurrent();
					History history = ui.getPage().getHistory();

					Location currentLocation = ui.getInternals().getActiveViewLocation();
					Map<String, List<String>> params =
							new java.util.HashMap<>(currentLocation.getQueryParameters().getParameters());

					params.put("scrollIndex", java.util.List.of(String.valueOf(scrollIndex)));

					Location newLocation = new Location(
							currentLocation.getPath(),
							new QueryParameters(params)
					);

					history.pushState(null, newLocation);
				});
			}

			String[] className = getClass().getName().split("\\.");
			String simple = className[className.length - 1];
			RouteParameters routeParameters =
					new RouteParameters("id", getEntityId(e.getItem()));

			if (simple.startsWith("Product") || simple.startsWith("CurrentOrdersByProducts")) {
				UI.getCurrent().navigate(
						ProductOrdersView.class,
						routeParameters);
			} else if (simple.startsWith("Consumer") || simple.startsWith("CurrentOrdersByConsumers")) {
				UI.getCurrent().navigate(
						ConsumerOrdersView.class,
						routeParameters
				);
			}
		});
		grid.addClassName("flexible-grid");

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

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		QueryParameters qp = event.getLocation().getQueryParameters();
		List<String> values = qp.getParameters().get(PARAM_SCROLL_INDEX);
		if (values != null && !values.isEmpty()) {
			try {
				savedScrollIndex = Integer.parseInt(values.get(0));
			} catch (NumberFormatException ignored) {
				savedScrollIndex = 0;
			}
		}
		refreshGrid();
	}
}
