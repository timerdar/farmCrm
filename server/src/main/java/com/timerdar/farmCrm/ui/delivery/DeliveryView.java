package com.timerdar.farmCrm.ui.delivery;

import com.timerdar.farmCrm.dto.ConsumerWithOrders;
import com.timerdar.farmCrm.dto.DeliveryOrderNumForConsumer;
import com.timerdar.farmCrm.dto.DeliverySummaryItem;
import com.timerdar.farmCrm.service.ConsumerService;
import com.timerdar.farmCrm.service.OrderService;
import com.timerdar.farmCrm.ui.MainView;
import com.timerdar.farmCrm.ui.components.ConsumerWithOrdersComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Route(value = "delivery", layout = MainView.class)
public class DeliveryView extends VerticalLayout {

	private final Grid<ConsumerWithOrders> grid;
	private final Dialog summaryDialog = new Dialog();
	private final OrderService orderService;
	private final ConsumerService consumerService;

	private ConsumerWithOrders draggedItem;

	@Autowired
	public DeliveryView(OrderService orderService, ConsumerService consumerService){
		this.orderService = orderService;
		this.consumerService = consumerService;

		setAlignItems(Alignment.CENTER);
		setSpacing(false);

		grid = getGrid();
		Button copy = getBillCopyButton();
		Button b = getSummaryButton();
		Button clean = getCleanButton();
		Button reorder = getReorderConsumersButton();
		setHeightFull();

		refreshGrid();
		add(summaryDialog, copy, b, clean, reorder, grid);
	}

	private Grid<ConsumerWithOrders> getGrid(){
		Grid<ConsumerWithOrders> grid = new Grid<>();
		grid.setEmptyStateComponent(new Div("Записи не найдены :("));
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
		grid.setSelectionMode(Grid.SelectionMode.NONE);
		grid.setHeightFull();

		grid.addColumn(new ComponentRenderer<>(this::getGridItem));

		return grid;
	}

	private Component getGridItem(ConsumerWithOrders consumerWithOrders){
		return new ConsumerWithOrdersComponent(consumerWithOrders, this.orderService);
	}

	private void refreshGrid(){
		grid.setItems(getData());
	}

	private List<ConsumerWithOrders> getData(){
		return orderService.getDeliveryData();
	}

	private Button getBillCopyButton(){
		Button button = new Button("Скопировать чеки доставки");
		button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		button.setWidthFull();
		button.addClickListener(e ->
			UI.getCurrent().getPage().executeJs(
					"navigator.clipboard.writeText($0).then(() => console.log('Copied!'));",
					orderService.getBills())
		);
		button.addClickListener(e -> Notification.show("Чеки скопированы в буфер обмена"));
		return button;
	}

	private Button getSummaryButton(){
		Button button = new Button("Сводка по продукции");
		button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		button.setWidthFull();
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

	private Button getCleanButton(){
		ConfirmDialog confirm = new ConfirmDialog();
		confirm.setHeader("Внимание!");
		confirm.setText("Вы уверены, что хотите выполнить очистку всех заказов в доставке? Все заказы перенесутся в архив");
		confirm.setCancelable(true);
		confirm.setCancelText("Отмена");
		confirm.setConfirmText("Очистить");
		confirm.setConfirmButtonTheme("error");
		confirm.addConfirmListener(e -> {
				orderService.clearDelivery();
				refreshGrid();
		});

		Button button = new Button("Очистить доставку");
		button.addThemeVariants(ButtonVariant.LUMO_ERROR);
		button.addClickListener(e -> confirm.open());
		button.setWidthFull();

		return button;
	}

	private Button getReorderConsumersButton(){
		Button button = new Button("Поменять порядок доставки");
		button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		button.setWidthFull();

		Dialog reorderDialog = new Dialog("Изменение порядка доставки");

		button.addClickListener(e -> reorderDialog.open());

		VerticalLayout dialogLayout = new VerticalLayout();
		dialogLayout.add(new Div("Для изменения порядка зажмите строчку с заказчиком и перенесите в нужное место"));

		List<ConsumerWithOrders> data = getData();
		Grid<ConsumerWithOrders> reorderGrid = new Grid<>();
		GridListDataView<ConsumerWithOrders> dataView = reorderGrid.setItems(data);
		reorderGrid.setRowsDraggable(true);
		reorderGrid.addColumn(this::getReorderGridItem);

		reorderGrid.addDragStartListener(e -> {
			draggedItem = e.getDraggedItems().get(0);
			reorderGrid.setDropMode(GridDropMode.BETWEEN);
		});

		reorderGrid.addDropListener(e -> {
			ConsumerWithOrders targetConsumer = e.getDropTargetItem().orElse(null);
			GridDropLocation dropLocation = e.getDropLocation();

			boolean droppedOntoItself = draggedItem.equals(targetConsumer);

			if(targetConsumer == null || droppedOntoItself) return;

			dataView.removeItem(draggedItem);

			if(dropLocation == GridDropLocation.BELOW){
				dataView.addItemAfter(draggedItem, targetConsumer);
			}else{
				dataView.addItemBefore(draggedItem, targetConsumer);
			}
			System.out.println(dataView.getItems().toList());
		});

		reorderGrid.addDragEndListener(e -> {
			draggedItem = null;
			reorderGrid.setDropMode(null);
		});

		dialogLayout.add(reorderGrid);

		reorderDialog.add(dialogLayout);

		Button close = new Button("Отмена", e ->
			reorderDialog.close()
		);
		close.addThemeVariants(ButtonVariant.LUMO_ERROR);

		Button accept = new Button("Сохранить", e -> {
			List<DeliveryOrderNumForConsumer> resultOrder = new ArrayList<>();
			List<ConsumerWithOrders> items = dataView.getItems().toList();
			for (int i = 0; i < items.size(); i++){
				resultOrder.add(new DeliveryOrderNumForConsumer((int) items.get(i).getId(), i));
			}
			consumerService.setDeliveryConsumersOrder(resultOrder);
			refreshGrid();
			reorderDialog.close();
		});
		accept.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		reorderDialog.getFooter().add(close, accept);

		return button;
	}

	private String getReorderGridItem(ConsumerWithOrders consumerWithOrders){
		return consumerWithOrders.getName() + " " + consumerWithOrders.getAddress();
	}
}
