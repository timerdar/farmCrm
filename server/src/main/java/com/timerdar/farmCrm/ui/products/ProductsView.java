package com.timerdar.farmCrm.ui.products;

import com.timerdar.farmCrm.dto.CreateConsumerRequest;
import com.timerdar.farmCrm.dto.CreateProductRequest;
import com.timerdar.farmCrm.dto.ProductWithOrdersCount;
import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.model.Product;
import com.timerdar.farmCrm.service.ProductService;
import com.timerdar.farmCrm.ui.consumers.ConsumerOrdersView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

//TODO отрефакторить и сделать наследника
@Route("products")
public class ProductsView extends VerticalLayout {

	private final ProductService productService;
	private Grid<ProductWithOrdersCount> productGrid;


	@Autowired
	public ProductsView(ProductService productService){
		this.productService = productService;
		setAlignItems(Alignment.CENTER);


		searchBox();
		createButton();
		createGrid();
	}

	private void refreshGrid(){
		productGrid.setItems(productService.getProductsList());
	}

	private void filterGrid(String filter){
		productGrid.setItems(
				productService.getProductsList()
						.stream().filter(consumer ->
								consumer.getName().toLowerCase().contains(filter.toLowerCase()))
						.collect(Collectors.toList())
		);
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
		Grid<ProductWithOrdersCount> grid = new Grid<>();
		grid.setEmptyStateComponent(new Div("Продукты не найдены :("));
		grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
		grid.setHeight("500px");
		grid.addSelectionListener(e -> {
			UI.getCurrent().navigate(ProductOrdersView.class,
					new RouteParameters("id", String.valueOf(e.getFirstSelectedItem().get().getId())));
		});

		grid.addColumn(new ComponentRenderer<>(this::productCard));
		this.productGrid = grid;
		refreshGrid();
		add(grid);
	}

	private Dialog creationDialog(){

		Dialog dialog = new Dialog();

		dialog.setHeaderTitle("Новый продукт");
		dialog.setWidth("400px");

		TextField name = new TextField("Имя");
		name.setRequired(true);
		name.setWidthFull();
		NumberField cost = new NumberField("Цена");
		cost.setRequired(true);
		cost.setWidthFull();
		Checkbox isWeighed = new Checkbox("Весовой товар");

		VerticalLayout dialogLayout = new VerticalLayout();
		dialogLayout.add(name, cost, isWeighed);

		Button create = new Button("Добавить", e -> {
			CreateProductRequest req = new CreateProductRequest();
			req.setName(name.getValue());
			req.setCost((int) Math.round(cost.getValue()));
			req.setWeightable(isWeighed.getValue());
			productService.createProduct(req);
			refreshGrid();
			dialog.close();
		});
		Button cancel = new Button("Отмена", e -> dialog.close());
		dialog.getFooter().add(create, cancel);

		dialog.add(dialogLayout);
		return dialog;
	}

	private Card productCard(Product product){
		Card card = new Card();
		card.setTitle(product.getName());
		VerticalLayout subtitle = new VerticalLayout();
		subtitle.add(
				new Div("Цена - " + product.getCost() + (product.isWeighed() ? "руб/кг" : "руб/шт")),
				new Div("Изготовлено - " + product.getCreatedCount())
		);
		card.setSubtitle(subtitle);
		return card;
	}
}
