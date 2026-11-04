package com.timerdar.farmCrm.ui;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.shared.ui.Transport;

// Long polling остаётся: на мобильной сети WebSocket обрывается, а WEBSOCKET_XHR
// на long polling сам не переходит. Время одного poll ограничено в application.properties,
// чтобы оборванный запрос не держал экран до очистки кэша.
@Push(transport = Transport.LONG_POLLING)
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
