package com.timerdar.farmCrm.ui;

import com.timerdar.farmCrm.dto.AuthRequest;
import com.timerdar.farmCrm.dto.AuthResponse;
import com.timerdar.farmCrm.service.AuthService;
import com.timerdar.farmCrm.ui.consumers.ConsumersView;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("login")
public class LoginView extends VerticalLayout {

	private TextField login = new TextField("Логин");
	private PasswordField password = new PasswordField("Пароль");
	private Button loginButton = new Button("Войти");

	private AuthService authService; // сервис с методом login(AuthRequest)

	@Autowired
	public LoginView(AuthService authService) {
		this.authService = authService;

		FormLayout form = new FormLayout();
		form.add(login, password, loginButton);

		loginButton.addClickListener(e -> {
			try {
				AuthRequest request = new AuthRequest(login.getValue(), password.getValue());
				AuthResponse response = authService.login(request);

				// Сохраняем JWT, например, в Vaadin session или cookie
				getUI().ifPresent(ui -> ui.getSession().setAttribute("jwt", response.getToken()));

				// Переходим на главную страницу
				getUI().ifPresent(ui -> ui.navigate("consumers"));

			} catch (Exception ex) {
				ex.printStackTrace();
				Notification.show("Неверный логин или пароль", 3000, Notification.Position.MIDDLE);
			}
		});

		add(form);
	}
}
