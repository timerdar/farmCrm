package com.timerdar.farmCrm.ui;

import com.timerdar.farmCrm.dto.TokenValidationRequest;
import com.timerdar.farmCrm.service.AuthService;
import com.timerdar.farmCrm.service.JwtUtil;
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
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Optional;

@Layout
@CssImport("./styles/timerdar.css")
public class MainView extends AppLayout implements AfterNavigationObserver, BeforeEnterObserver {

	private final Tabs tabs;
	private final Tab currentOrders;
	private final Tab consumers;
	private final Tab products;
	private final Tab delivery;

	private String COOKIE_NAME = "authToken";

	private AuthService authService;
	private JwtUtil jwtUtil;

	@Autowired
	public MainView(AuthService authService, JwtUtil jwtUtil) {
		this.authService = authService;
		this.jwtUtil = jwtUtil;

		currentOrders = createTab("Текущие", CurrentOrdersView.class);
		consumers = createTab("Заказчики", ConsumersView.class);
		products = createTab("Продукция", ProductsView.class);
		delivery = createTab("Доставка", DeliveryView.class);

		tabs = new Tabs(currentOrders, consumers, products, delivery);
		tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);
		tabs.getStyle().set("overflow-x", "auto");

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
		} else if (route.startsWith("consumers")){
			tabs.setSelectedTab(consumers);
		} else {
			tabs.setSelectedTab(currentOrders);
		}
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {

		Optional<String> token = getTokenFromCookie();

		if (token.isEmpty()) {
			event.forwardTo("/login");
		}

		try {
			String username = jwtUtil.extractUsername(token.get());
			if (!jwtUtil.validateToken(token.get(), username)) {
				event.forwardTo("/login");
			}
		} catch (Exception e) {
			event.forwardTo("/login");
		}
	}

	private Optional<String> getTokenFromCookie() {
		VaadinRequest request = VaadinService.getCurrentRequest();
		Cookie[] cookies = null;

		if (request != null)
			cookies = request.getCookies();

		if (cookies == null)
			return Optional.empty();

		return Arrays.stream(cookies)
				.filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
				.map(Cookie::getValue)
				.findFirst();
	}
}