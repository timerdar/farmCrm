package com.timerdar.farmCrm.ui.products;

import com.timerdar.farmCrm.dto.OrderWithNameAndWeightable;
import com.timerdar.farmCrm.dto.ProductChangeRequest;
import com.timerdar.farmCrm.dto.ProductWithOrdersCount;
import com.timerdar.farmCrm.model.OrderStatus;
import com.timerdar.farmCrm.service.OrderService;
import com.timerdar.farmCrm.service.ProductService;
import com.timerdar.farmCrm.ui.components.OrderComponent;
import com.timerdar.farmCrm.ui.OrdersListView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("products/:id/orders")
public class ProductOrdersView extends OrdersListView {

	private final ProductService productService;
	private final OrderService orderService;

	@Autowired
	public ProductOrdersView(OrderService orderService, ProductService productService){
		this.orderService = orderService;
		this.productService = productService;
	}

	@Override
	public List<OrderWithNameAndWeightable> getData() {
		return orderService.getOrdersWithName(getEntityId(), "products", OrderStatus.CREATED.toString());
	}

	@Override
	public Component getEditableEntity(Long id) {
		ProductWithOrdersCount product = productService.getProductById(id);
		FormLayout card = new FormLayout();

		TextField name = new TextField("Название");
		name.setValue(product.getName());
		name.setReadOnly(true);

		NumberField cost = new NumberField("Цена");
		cost.setValue((double) product.getCost());
		cost.setReadOnly(true);

		NumberField createdCount = new NumberField("Изготовлено");
		createdCount.setValue((double) product.getCreatedCount());
		createdCount.setReadOnly(true);

		NumberField orderedCount = new NumberField("Заказано");
		orderedCount.setValue((double) product.getOrderedCount());
		orderedCount.setReadOnly(true);
		orderedCount.setStep(1);

		Div buttons = new Div();

		Button edit = new Button(new Icon(VaadinIcon.EDIT), e -> {
			name.setReadOnly(false);
			cost.setReadOnly(false);
			createdCount.setReadOnly(false);
			buttons.setVisible(true);
			e.getSource().setVisible(false);
		});

		Button saveData = new Button(new Icon(VaadinIcon.CHECK), e -> {
			productService.updateProduct(new ProductChangeRequest(id, name.getValue(), cost.getValue(), (int) Math.round(createdCount.getValue())));
			name.setReadOnly(true);
			cost.setReadOnly(true);
			createdCount.setReadOnly(true);
			buttons.setVisible(false);
			edit.setVisible(true);
		});

		Button cancelData = new Button(new Icon(VaadinIcon.CLOSE), e -> {
			name.setReadOnly(true);
			cost.setReadOnly(true);
			createdCount.setReadOnly(true);
			buttons.setVisible(false);
			edit.setVisible(true);
		});

		buttons.add(saveData, cancelData);
		buttons.setVisible(false);

		card.addFormRow(name, cost, createdCount, orderedCount);
		card.setRowSpacing("30px");
		card.add(buttons, edit);

		card.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

		return card;
	}

	@Override
	public Component getGridItem(OrderWithNameAndWeightable order) {
		return new OrderComponent(order, orderService, this::renderEntity, this::refreshGrid);
	}

}
