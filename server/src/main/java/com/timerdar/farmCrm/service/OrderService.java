package com.timerdar.farmCrm.service;

import com.timerdar.farmCrm.dto.*;
import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.model.Order;
import com.timerdar.farmCrm.model.OrderStatus;
import com.timerdar.farmCrm.model.Product;
import com.timerdar.farmCrm.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ConsumerService consumerService;

    public Order createOrder(CreateOrderRequest orderRequest){
        if(!orderRequest.isFullyEntered()){
            throw new IllegalArgumentException("Для создания заказа введены не все данные");
        }else if (!productService.isPriceExists(orderRequest.getProductId())){
            throw new EntityNotFoundException("Указанной позиции в прайс-листе нет");
        }else if(!consumerService.isConsumerExists(orderRequest.getConsumerId())){
            throw new EntityNotFoundException("Указанного заказчика нет в системе");
        }else {
            Order newOrder = new Order();
            newOrder.setProductId(orderRequest.getProductId());
            newOrder.setCreatedAt(LocalDate.now());
            newOrder.setConsumerId(orderRequest.getConsumerId());
            newOrder.setCount(orderRequest.getAmount());
            newOrder.setWeight(0);
            newOrder.setStatus(OrderStatus.CREATED);
            Order createdOrder = orderRepository.save(newOrder);
            log.info("Создание заказа:{}", createdOrder);
            return evalCost(createdOrder.getId());
        }
    }

    public List<OrderWithNameAndWeightable> getOrdersWithName(long id, String source, String status){
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        List<OrderWithNameAndWeightable> ordersWithName = new ArrayList<>();
        List<Order> orders = source.equals("products") ? getOrdersOfProduct(id, orderStatus) : getOrdersOfConsumer(id, orderStatus);
        for(Order order: orders){
			Product product = productService.getProductById(order.getProductId());
            if(source.equals("consumers")){
                ordersWithName.add(new OrderWithNameAndWeightable(order, product.getName(), product.isWeighed()));
            }else{
                Consumer consumer = consumerService.getConsumerById(order.getConsumerId());
                ordersWithName.add(new OrderWithNameAndWeightable(order, consumer.getName(), product.isWeighed()));
            }
        }
        log.info("Получение заказов: source = {}, {}Id = {}, status = {}", source, source, id, status);
        return ordersWithName;
    }

    public List<Order> getOrdersOfConsumer(long consumerId, OrderStatus status){
        return orderRepository.findByConsumerIdAndStatusOrderByIdAsc(consumerId, status);
    }

    public List<Order> getOrdersOfProduct(long productId, OrderStatus status){
        return orderRepository.findByProductIdAndStatusOrderByIdAsc(productId, status);
    }

    public Order changeStatus(OrderChangeRequest request){
        OrderStatus newStatus = OrderStatus.valueOf(request.getStatus());
		Order order = orderRepository.findById(request.getId()).orElseThrow(() -> new EntityNotFoundException("Order not found"));
		String oldStatus = order.getStatus().toString();
        if (request.getStatus().equals("ARCHIVED")){
            consumerService.increaseTotalSum(order.getConsumerId(), order.getCost());
        }
        order.setStatus(newStatus);
        log.info("Изменение статус заказа c id = {} из {} в {}", request.getId(), oldStatus, request.getStatus());
        return orderRepository.save(order);
    }

    public Order changeAmount(OrderChangeRequest request){
		Order order = orderRepository.findById(request.getId()).orElseThrow(() -> new EntityNotFoundException("Order not found"));
		order.setCount(request.getAmount());
        log.info("Изменение количества в заказе: orderId = {}, newAmount = {}", request.getId(), request.getAmount());
        return evalCost(orderRepository.save(order).getId());
    }

    public Order changeWeight(OrderChangeRequest request){
		Order order = orderRepository.findById(request.getId()).orElseThrow(() -> new EntityNotFoundException("Order not found"));
		order.setWeight(request.getWeight());
        log.info("Изменение веса заказа: orderId = {}, newWeight = {}", request.getId(), request.getWeight());
        return evalCost(orderRepository.save(order).getId());
    }

    public Order evalCost(long id){
		Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found"));
		Product product = productService.getProductById(order.getProductId());
        int cost;
        if (product.isWeighed()){
            cost = (int) (product.getCost() * order.getWeight());
            log.info("Пересчет стоимости заказа с весом: orderId = {}, product = {}, newCost = {}", id, product.getName(), cost);
        }else{
            cost = (int)(product.getCost() * order.getCount());
            log.info("Пересчет стоимости штучного заказа: orderId = {}, product = {}, newCost = {}", id, product.getName(), cost);
        }
        order.setCost(cost);
        return orderRepository.save(order);
    }

    private static final EnumSet<OrderStatus> DELIVERY_STATUSES = EnumSet.of(OrderStatus.DELIVERY, OrderStatus.DONE);

    public List<DeliveryStop> getDeliveryStops() {
        List<DeliveryStop> stops = new ArrayList<>();
        for (Object[] row : orderRepository.findDeliveryStops()) {
            stops.add(new DeliveryStop(
                    asLong(row[0]),
                    text(row[1]),
                    text(row[2]),
                    text(row[3]),
                    (int) asLong(row[4]),
                    (int) asLong(row[5]),
                    (int) asLong(row[6])));
        }
        return stops;
    }

    public List<OrderWithNameAndWeightable> getConsumerDeliveryOrders(long consumerId) {
        return toNamedOrders(orderRepository.findConsumerDeliveryOrders(consumerId, DELIVERY_STATUSES));
    }

    public List<ConsumerWithOrders> getDeliveryData() {
        Map<Long, ConsumerWithOrders> grouped = new LinkedHashMap<>();
        for (Object[] row : orderRepository.findDeliveryOrdersWithNames(DELIVERY_STATUSES)) {
            Order order = (Order) row[0];
            Product product = (Product) row[1];
            Consumer consumer = (Consumer) row[2];
            ConsumerWithOrders bucket = grouped.computeIfAbsent(consumer.getId(),
                    id -> new ConsumerWithOrders(consumer, new ArrayList<>()));
            bucket.getOrders().add(new OrderWithNameAndWeightable(order, product.getName(), product.isWeighed()));
        }
        return new ArrayList<>(grouped.values());
    }

	public List<Consumer> getCreatedConsumer(){
		List<Consumer> consumers = new ArrayList<>();
		for(Long id: orderRepository.getCreatedConsumerIds())
			consumers.add(consumerService.getConsumerById(id));
		return consumers;
	}

    public int clearDelivery(){
        int count = 0;
        for (Order order: orderRepository.getDeliveryOrders()) {
            OrderChangeRequest request = new OrderChangeRequest();
            request.setId(order.getId());
            request.setStatus("ARCHIVED");
            changeStatus(request);
            count++;
            consumerService.changeOrderNumForConsumer(order.getConsumerId(), 0);
        }
        log.info("Очистка доставки");
        return count;
    }

    public int getOrdersCount(long productId, OrderStatus status){
        Integer amount = orderRepository.getOrderedProductAmountByIdAndStatus(productId, status.toString());
        return amount != null ? amount : 0;
    }

    public List<DeliverySummaryItem> getDeliverySummary(){
        Map<String, SummaryAcc> grouped = new LinkedHashMap<>();
        for (Object[] row : orderRepository.findDeliverySummaryRows(DELIVERY_STATUSES)) {
            String productName = text(row[0]);
            int createdCount = (int) asLong(row[1]);
            String consumerName = text(row[2]);
            double count = row[3] instanceof Number number ? number.doubleValue() : 0;
            OrderStatus status = (OrderStatus) row[4];
            SummaryAcc acc = grouped.computeIfAbsent(productName, name -> new SummaryAcc(createdCount));
            acc.orderedCount += count;
            if (status == OrderStatus.DELIVERY) {
                acc.consumers.put(consumerName, count);
            }
        }
        List<DeliverySummaryItem> res = new ArrayList<>();
        for (Map.Entry<String, SummaryAcc> entry : grouped.entrySet()) {
            SummaryAcc acc = entry.getValue();
            res.add(new DeliverySummaryItem(entry.getKey(), acc.createdCount, (int) acc.orderedCount, acc.consumers));
        }
		log.info("Получение сводки доставки");
        return res;
    }

	public String getBills(){
		StringBuilder sb = new StringBuilder();
		int totalSumOfDelivery = 0;
		for (ConsumerWithOrders consumer : getDeliveryData()){
			sb.append(consumer.getName()).append("\n");
			sb.append(consumer.getPhone()).append("\n");
			sb.append(consumer.getAddress()).append("\n");

			StringBuilder ordersSb = new StringBuilder();
			int s = 0;
			for (OrderWithNameAndWeightable order: consumer.getOrders()){
				s = s + order.getCost();
				ordersSb.append(order.getName()).append(" ");
				if(order.isWeighed())
					ordersSb.append(order.getWeight()).append(" кг ");
				ordersSb.append(order.getCount()).append(" шт ").append(order.getCost()).append(" руб\n");
			}
			sb.append(ordersSb);
			sb.append("Сумма заказа: ").append(s).append(" 	руб.\n");
			sb.append("\n");
			totalSumOfDelivery += s;
		}
		sb.append("\nОбщая сумма доставки: ").append(totalSumOfDelivery).append(" руб. Молодцы! Хорошая работа!");
		log.info("Получены чеки доставки");
		return sb.toString();
	}

	public String getBillOfConsumer(long consumerId){
		StringBuilder sb = new StringBuilder();
		int s = 0;
		for (OrderWithNameAndWeightable order : getConsumerDeliveryOrders(consumerId)){
			if (order.getStatus() != OrderStatus.DELIVERY) {
				continue;
			}
			s = s + order.getCost();
			sb.append(order.getName()).append(" ");
			if(order.isWeighed())
				sb.append(order.getWeight()).append(" кг ");
			sb.append(order.getCount()).append(" шт ").append(order.getCost()).append(" руб\n");
		}
		sb.append("Сумма заказа: ").append(s).append(" руб.\n");
		return sb.toString();
	}

	public void deleteByConsumerId(long consumerId){
		log.info("Удаление заказов для consumerId={}", consumerId);
		List<Order> listToDelete = orderRepository.findByConsumerId(consumerId);
		for(Order order: listToDelete){
			orderRepository.delete(order);
			log.info("Удален заказ: {}", order);
		}
		consumerService.deleteConsumer(consumerId);
	}

	public void deleteByProductId(long productId){
		log.info("Удаление заказов для productId={}", productId);
		List<Order> listToDelete = orderRepository.findByProductId(productId);
		for(Order order: listToDelete){
			orderRepository.delete(order);
			log.info("Удален заказ: {}", order);
		}
		productService.deleteProduct(productId);
	}

	private List<OrderWithNameAndWeightable> toNamedOrders(List<Object[]> rows) {
		List<OrderWithNameAndWeightable> orders = new ArrayList<>();
		for (Object[] row : rows) {
			Order order = (Order) row[0];
			Product product = (Product) row[1];
			orders.add(new OrderWithNameAndWeightable(order, product.getName(), product.isWeighed()));
		}
		return orders;
	}

	private static long asLong(Object value) {
		return value instanceof Number number ? number.longValue() : 0L;
	}

	private static String text(Object value) {
		return value == null ? "" : value.toString();
	}

	private static final class SummaryAcc {
		private final int createdCount;
		private double orderedCount;
		private final Map<String, Double> consumers = new HashMap<>();

		private SummaryAcc(int createdCount) {
			this.createdCount = createdCount;
		}
	}

	public void groupedToDelivery(List<OrderWithNameAndWeightable> orders){
		for(OrderWithNameAndWeightable order: orders){
			OrderChangeRequest req = new OrderChangeRequest();
			req.setStatus(OrderStatus.DELIVERY.toString());
			req.setId(order.getId());
			changeStatus(req);
		}
	}

}
