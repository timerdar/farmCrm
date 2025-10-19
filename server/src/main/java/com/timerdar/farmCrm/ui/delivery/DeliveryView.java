package com.timerdar.farmCrm.ui.delivery;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/delivery")
public class DeliveryView extends VerticalLayout {
	public DeliveryView(){
		add(
				new H1("Доставка")
		);
	}
}
