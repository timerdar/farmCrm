package com.timerdar.farmCrm.ui;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/auth")
public class AuthView extends  VerticalLayout{
	public AuthView(){
		add(new LoginForm());
	}
}
