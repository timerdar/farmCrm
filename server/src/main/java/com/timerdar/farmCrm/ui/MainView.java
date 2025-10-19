package com.timerdar.farmCrm.ui;

import com.timerdar.farmCrm.ui.consumers.ConsumersView;
import com.timerdar.farmCrm.ui.delivery.DeliveryView;
import com.timerdar.farmCrm.ui.products.ProductsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.router.RouterLink;

@Layout
@Route("")
public class MainView extends AppLayout {

	public MainView() {
		Tabs tabs = new Tabs(
				createTab("Заказчики", ConsumersView.class),
				createTab("Продукция", ProductsView.class),
				createTab("Доставка", DeliveryView.class)
		);
		tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);
		addToNavbar(tabs);
	}


	private Tab createTab(String text, Class<? extends Component> target){
		RouterLink link = new RouterLink(text, target);
		return new Tab(link);
	}
}
