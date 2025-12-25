package com.timerdar.farmCrm.ui;

import com.timerdar.farmCrm.ui.consumers.ConsumersView;
import com.timerdar.farmCrm.ui.products.ProductsView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("catalog")
public class CatalogView extends VerticalLayout {

	public CatalogView(){
		H1 h1 = new H1("Каталог");
		RouterLink products = new RouterLink("Продукты", ProductsView.class);
		RouterLink consumers = new RouterLink("Заказчики", ConsumersView.class);

		add(h1, products, consumers);
	}

}
