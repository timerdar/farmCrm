package com.timerdar.farmCrm.ui.products;

import com.timerdar.farmCrm.dto.CreateProductRequest;
import com.timerdar.farmCrm.dto.ProductWithOrdersCount;
import com.timerdar.farmCrm.model.Product;
import com.timerdar.farmCrm.service.OrderService;
import com.timerdar.farmCrm.service.ProductService;
import com.timerdar.farmCrm.ui.EntitiesListView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route("products")
public class ProductsView extends EntitiesListView {

	private final ProductService productService;
	private Grid<ProductWithOrdersCount> productGrid;

	@Autowired
	public ProductsView(ProductService productService){
		this.productService = productService;

		refreshGrid();
	}

	@Override
	public List<?> getData() {
		return productService.getProductsList();
	}

	@Override
	public Dialog getDialog() {
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

	@Override
	public void filterGrid(String filter) {
		grid.setItems(filteredItems(filter));
	}

	private List<Product> filteredItems(String filter){
		return 	productService.getProductsList()
						.stream().filter(consumer ->
								consumer.getName().toLowerCase().contains(filter.toLowerCase()))
						.collect(Collectors.toList());
	}

	@Override
	public Card getEntityCard(Object object) {
		Product product = (Product) object;
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

	@Override
	public String getEntityId(Object object) {
		return object instanceof Product ? String.valueOf(((Product) object).getId()) : null;
	}
}
