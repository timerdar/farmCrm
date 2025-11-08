package com.timerdar.farmCrm.ui;

import com.timerdar.farmCrm.dto.AuthRequest;
import com.timerdar.farmCrm.dto.AuthResponse;
import com.timerdar.farmCrm.service.AuthService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;

@Route("login")
public class LoginView extends VerticalLayout {

	private TextField login = new TextField("Логин");
	private PasswordField password = new PasswordField("Пароль");
	private Button loginButton = new Button("Войти");

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

				// Сохраняем JWT в Vaadin session и localstorage
				getUI().ifPresent(ui -> {
					ui.getPage().executeJs("window.localStorage.setItem('AUTH_JWT', $0);", response.getToken());
					ui.getSession().setAttribute("jwt", response.getToken());
				});

				// Переходим на главную страницу
				getUI().ifPresent(ui -> ui.navigate("consumers"));

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
