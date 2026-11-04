package com.timerdar.farmCrm.ui;

import com.vaadin.flow.component.ReconnectDialogConfiguration;
import com.vaadin.flow.server.CustomizedSystemMessages;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.SystemMessages;
import com.vaadin.flow.server.SystemMessagesInfo;
import com.vaadin.flow.server.SystemMessagesProvider;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component
public class VaadinConnectionConfig implements VaadinServiceInitListener {

	private static final int RECONNECT_INTERVAL_MS = 3000;

	@Override
	public void serviceInit(ServiceInitEvent event) {
		event.getSource().setSystemMessagesProvider(new SessionRecoveryMessages());
		event.getSource().addUIInitListener(uiEvent -> {
			ReconnectDialogConfiguration dialog = uiEvent.getUI().getReconnectDialogConfiguration();
			dialog.setDialogText("Связь пропала. Подключаемся снова…");
			dialog.setDialogTextGaveUp("Не удалось восстановить связь. Обновите страницу.");
			dialog.setReconnectInterval(RECONNECT_INTERVAL_MS);
		});
	}

	private static final class SessionRecoveryMessages implements SystemMessagesProvider {
		@Override
		public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
			CustomizedSystemMessages messages = new CustomizedSystemMessages();
			// Сессия на сервере уже умерла. Перезагрузка текущей страницы открывает новую.
			// Сообщение с кнопкой на телефоне выглядит как зависший экран.
			messages.setSessionExpiredCaption(null);
			messages.setSessionExpiredMessage(null);
			messages.setSessionExpiredNotificationEnabled(false);
			messages.setSessionExpiredURL(null);
			return messages;
		}
	}
}
