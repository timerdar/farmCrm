package com.timerdar.farmCrm.ui.products;

import com.timerdar.farmCrm.dto.ProductWithOrdersCount;
import com.timerdar.farmCrm.model.Product;
import com.timerdar.farmCrm.service.ProductService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("products")
public class ProductsView extends VerticalLayout {

	private final ProductService productService;

	private final Grid<ProductWithOrdersCount> productGrid = new Grid<>();


	@Autowired
	public ProductsView(ProductService productService){
		this.productService = productService;

		productGrid.addColumn(ProductWithOrdersCount::getName).setHeader("Название").setAutoWidth(true);
		productGrid.addColumn(ProductWithOrdersCount::getCost).setHeader("Цена").setAutoWidth(true);
		productGrid.addColumn(new ComponentRenderer<>(productWithOrdersCount -> {
			return new Text(productWithOrdersCount.isWeighed() ? "за КГ" : "за штуку");
		})).setAutoWidth(true);


		productGrid.isColumnReorderingAllowed();
		refreshGrid();
		add(
				new H1("Продукты"),
				productGrid
		);
	}

	private void refreshGrid(){
		productGrid.setItems(productService.getProductsList());
	}
}
