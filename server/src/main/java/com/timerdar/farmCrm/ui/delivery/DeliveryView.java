package com.timerdar.farmCrm.ui.delivery;

import com.timerdar.farmCrm.dto.ConsumerWithOrders;
import com.timerdar.farmCrm.dto.DeliverySummaryItem;
import com.timerdar.farmCrm.service.OrderService;
import com.timerdar.farmCrm.ui.MainView;
import com.timerdar.farmCrm.ui.components.ConsumerWithOrdersComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "delivery", layout = MainView.class)
public class DeliveryView extends VerticalLayout {

	private Grid<ConsumerWithOrders> grid;
	private Dialog summaryDialog = new Dialog();
	private OrderService orderService;

	@Autowired
	public DeliveryView(OrderService orderService){
		this.orderService = orderService;



		grid = getGrid();
		Button b = getSummaryButton();
		Button copy = getBillCopyButton();

		refreshGrid();
		add(summaryDialog, copy, b, grid);
		setPadding(false);
	}

	private Grid<ConsumerWithOrders> getGrid(){
		Grid<ConsumerWithOrders> grid = new Grid<>();
		grid.setEmptyStateComponent(new Div("Записи не найдены :("));
		grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
		grid.setSelectionMode(Grid.SelectionMode.NONE);
		grid.setHeight("500px");

		grid.addColumn(new ComponentRenderer<>(this::getGridItem));

		return grid;
	}

	private List<ConsumerWithOrders> getData(){
		return orderService.getDeliveryData();
	}

	private void refreshGrid(){
		grid.setItems(getData());
	}

	private Component getGridItem(ConsumerWithOrders consumerWithOrders){
		return new ConsumerWithOrdersComponent(consumerWithOrders, this.orderService);
	}

	private Button getBillCopyButton(){
		Button button = new Button("Скопировать чеки доставки");

		button.addClickListener(e -> {
			UI.getCurrent().getPage().executeJs(
					"navigator.clipboard.writeText($0).then(() => console.log('Copied!'));",
					orderService.getBills());

		});
		button.addClickListener(e -> Notification.show("Чеки скопированы в буфер обмена"));
		return button;
	}

	private Button getSummaryButton(){
		Button button = new Button("Сводка по продукции");
		button.addClickListener(e -> {
			renderSummaryDialog();
			summaryDialog.open();
		});
		return button;
	}


	private void renderSummaryDialog(){
		summaryDialog.setHeaderTitle("Сводка по заказанным продукциям");
		summaryDialog.removeAll();

		VerticalLayout layout = new VerticalLayout();
		layout.add(new Text("Ниже представлены позиции и количество заказанных и изготовленных"));


		for(DeliverySummaryItem item : orderService.getDeliverySummary()){
			layout.add(new Div(item.getProductName() + " - Заказано " + item.getOrderedCount() + " - Изготовлено " + item.getCreatedCount()));
		}

		summaryDialog.add(layout);

		summaryDialog.getFooter().add(new Button("Закрыть", e -> summaryDialog.close()));
	}

	//TODO добавить кнопку очистки доставки
}
