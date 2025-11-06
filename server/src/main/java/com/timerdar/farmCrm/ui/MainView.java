package com.timerdar.farmCrm.ui;

import com.timerdar.farmCrm.ui.consumers.ConsumersView;
import com.timerdar.farmCrm.ui.delivery.DeliveryView;
import com.timerdar.farmCrm.ui.products.ProductsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.*;

@Layout
@CssImport("./styles/timerdar.css")
public class MainView extends AppLayout implements AfterNavigationObserver, BeforeEnterObserver {

	private final Tabs tabs;
	private final Tab consumers;
	private final Tab products;
	private final Tab delivery;

	public MainView() {
		consumers = createTab("Заказчики", ConsumersView.class);
		products = createTab("Продукция", ProductsView.class);
		delivery = createTab("Доставка", DeliveryView.class);

		tabs = new Tabs(consumers, products, delivery);
		tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);
		addToNavbar(tabs);

	}


	private Tab createTab(String text, Class<? extends Component> target){
		RouterLink link = new RouterLink(text, target);
		return new Tab(link);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		String route = event.getLocation().getFirstSegment();

		if (route.startsWith("delivery")) {
			tabs.setSelectedTab(delivery);
		} else if (route.startsWith("products")) {
			tabs.setSelectedTab(products);
		} else {
			tabs.setSelectedTab(consumers);
		}
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		//TODO добавить проверку корркетности токена
	}
}
