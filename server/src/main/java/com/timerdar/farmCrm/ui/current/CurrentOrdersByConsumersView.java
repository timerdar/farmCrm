package com.timerdar.farmCrm.ui.current;

import com.timerdar.farmCrm.dto.CreateOrderRequest;
import com.timerdar.farmCrm.dto.ProductWithOrdersCount;
import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.model.Product;
import com.timerdar.farmCrm.service.ConsumerService;
import com.timerdar.farmCrm.service.OrderService;
import com.timerdar.farmCrm.service.ProductService;
import com.timerdar.farmCrm.ui.EntitiesListView;
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

@Route("current/consumers")
public class CurrentOrdersByConsumersView extends EntitiesListView {

	private final ProductService productService;
	private final ConsumerService consumerService;
	private final  OrderService orderService;

	@Autowired
	public CurrentOrdersByConsumersView(ProductService productService, ConsumerService consumerService, OrderService orderService){
		this.consumerService = consumerService;
		this.productService = productService;
		this.orderService = orderService;

		setDialog(getNewDialog());

		refreshGrid();
	}

	@Override
	public List<?> getData() {
		return orderService.getCreatedConsumer();
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

	private List<Consumer> filteredItems(String filter){
		return 	orderService.getCreatedConsumer()
				.stream().filter(consumer ->
						consumer.getName().toLowerCase().contains(filter.toLowerCase()))
				.collect(Collectors.toList());
	}

	@Override
	public Card getEntityCard(Object object){
		Consumer consumer = (Consumer) object;
		Card card = new Card();
		card.setTitle(consumer.getName());
		VerticalLayout l = new VerticalLayout(
				new Div(consumer.getAddress()),
				new Div("Сумма выкупа - " + consumer.getTotalSum() + " руб."));
		l.setSpacing(false);
		card.setSubtitle(l);
		return card;
	}
	//TODO переработать current

	@Override
	public String getEntityId(Object o) {
		return o instanceof Consumer ? String.valueOf(((Consumer) o).getId()) : null;
	}

}
