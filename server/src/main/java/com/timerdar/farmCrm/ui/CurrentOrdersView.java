package com.timerdar.farmCrm.ui;

import com.timerdar.farmCrm.dto.CreateOrderRequest;
import com.timerdar.farmCrm.dto.ProductWithOrdersCount;
import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.model.Product;
import com.timerdar.farmCrm.service.*;
import com.timerdar.farmCrm.ui.products.ProductsView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route("current")
public class CurrentOrdersView extends EntitiesListView{

	private final ProductService productService;
	private final ConsumerService consumerService;
	private final  OrderService orderService;

	@Autowired
	public CurrentOrdersView(ProductService productService, ConsumerService consumerService, OrderService orderService){
		this.consumerService = consumerService;
		this.productService = productService;
		this.orderService = orderService;

		setDialog(getNewDialog());

		refreshGrid();
	}

	@Override
	public List<?> getData() {
		return productService.getProductsFromCreated();
	}

	@Override
	public Dialog getDialog(){
		return new Dialog();
	}

	public Dialog getNewDialog() {
		Dialog dialog = new Dialog();
		dialog.setWidth("300px");
		dialog.setHeaderTitle("Создать заказ");
		VerticalLayout layout = new VerticalLayout();
		layout.setPadding(false);

		ComboBox<Consumer> consumerChooser = new ComboBox<>();
		consumerChooser.setRequired(true);
		consumerChooser.setItemLabelGenerator(Consumer::getName);
		consumerChooser.setItems(consumerService.getAllConsumers());
		consumerChooser.setLabel("Заказчик");
		consumerChooser.setWidthFull();

		ComboBox<ProductWithOrdersCount> productChooser = new ComboBox<>();
		productChooser.setRequired(true);
		productChooser.setItemLabelGenerator(Product::getName);
		productChooser.setItems(productService.getProductsList());
		productChooser.setLabel("Продукт");
		productChooser.setWidthFull();

		IntegerField count = new IntegerField("Количество");
		count.setMin(0);
		count.setRequired(true);
		count.setPlaceholder("ШТ:");
		count.setWidthFull();

		layout.add(consumerChooser, productChooser, count);
		dialog.add(layout);

		Button cancelButton = new Button("Закрыть");
		cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancelButton.addClickListener(e -> dialog.close());

		Button saveButton = new Button("Создать");
		saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		saveButton.addClickListener(e -> {
			if(consumerChooser.isEmpty() || count.isEmpty()){
				Notification.show("Заполните все поля");
			}else{
				CreateOrderRequest req = new CreateOrderRequest(productChooser.getValue().getId(), consumerChooser.getValue().getId(), count.getValue());
				orderService.createOrder(req);
				dialog.close();
				productChooser.clear();
				count.clear();
				refreshGrid();
			}
		});

		dialog.getFooter().add(cancelButton, saveButton);

		return dialog;
	}

	@Override
	public void filterGrid(String filter) {
		grid.setItems(filteredItems(filter));
	}

	private List<ProductWithOrdersCount> filteredItems(String filter){
		return 	productService.getProductsList()
				.stream().filter(product ->
						product.getName().toLowerCase().contains(filter.toLowerCase()))
				.collect(Collectors.toList());
	}

	@Override
	public Card getEntityCard(Object object) {
		ProductWithOrdersCount product = (ProductWithOrdersCount) object;
		Card card = new Card();
		card.setTitle(product.getName());
		VerticalLayout subtitle = new VerticalLayout();
		subtitle.add(
				new Div("Заказано - " + product.getOrderedCount() + " | Изготовлено - " + product.getCreatedCount())
		);
		card.setSubtitle(subtitle);
		return card;
	}

	@Override
	public String getEntityId(Object object) {
		return object instanceof Product ? String.valueOf(((Product) object).getId()) : null;
	}
}
