package com.timerdar.farmCrm.ui;

import com.timerdar.farmCrm.dto.AuthRequest;
import com.timerdar.farmCrm.dto.AuthResponse;
import com.timerdar.farmCrm.service.AuthService;
import com.timerdar.farmCrm.ui.consumers.ConsumersView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Route("login")
public class LoginView extends VerticalLayout {

	private TextField login = new TextField("Логин");
	private PasswordField password = new PasswordField("Пароль");
	private Button loginButton = new Button("Войти");
	private boolean authenticated = false;


	private AuthService authService;

	@Autowired
	public LoginView(AuthService authService) {
		this.authService = authService;



		setSizeFull();
		setJustifyContentMode(JustifyContentMode.CENTER);
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);

		FormLayout form = new FormLayout();
		form.setWidth("300px");
		form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
		form.add(login, password, loginButton);

		loginButton.addClickListener(e -> {
			try {
				AuthRequest request = new AuthRequest(login.getValue(), password.getValue());
				AuthResponse response = authService.login(request);
				Notification.show("Вход успешный. Перейдите на нужную вкладку", 3000, Notification.Position.MIDDLE);
				form.setVisible(false);

			} catch (Exception ex) {
				ex.printStackTrace();
				Notification.show("Неверный логин или пароль", 3000, Notification.Position.MIDDLE);
			}
		});

		Div wrapper = new Div(form);
		wrapper.setWidthFull();
		wrapper.getStyle().set("display", "flex");
		wrapper.getStyle().set("justify-content", "center");

		add(wrapper);
	}
}
