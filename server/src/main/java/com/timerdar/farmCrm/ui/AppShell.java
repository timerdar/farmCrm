package com.timerdar.farmCrm.ui;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.PWA;

@PWA(
		name = "Система учета заказов на фермерскую продукцию",
		shortName = "Заказы Фермы")
public class AppShell implements AppShellConfigurator {
	@Override
	public void configurePage(AppShellSettings settings){
		settings.addFavIcon("icon512", "icons/icon.png", "512x512");
		settings.addFavIcon("icon192", "icons/icon.png", "192x192");
		settings.addFavIcon("icon32", "icons/favicon.png", "32x32");
	}
}
