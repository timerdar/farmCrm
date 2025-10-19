package com.timerdar.farmCrm.handler;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.server.VaadinSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UIErrorHandler implements ErrorHandler {

	@Override
	public void error(ErrorEvent event) {
		Throwable t = event.getThrowable();

		log.error("Произошла ошибка в Vaadin UI", t);

		if (UI.getCurrent() != null) {
			UI.getCurrent().access(() -> {
				Notification.show(
						"Ошибка: " + t.getMessage(),
						6000,
						Notification.Position.MIDDLE
				);
			});
		}
	}

	@Component
	public static class ErrorHandlerInitializer
			implements com.vaadin.flow.server.VaadinServiceInitListener {

		@Override
		public void serviceInit(com.vaadin.flow.server.ServiceInitEvent event) {
			event.getSource().addSessionInitListener(initEvent ->
					initEvent.getSession().setErrorHandler(new UIErrorHandler())
			);
		}
	}
}
