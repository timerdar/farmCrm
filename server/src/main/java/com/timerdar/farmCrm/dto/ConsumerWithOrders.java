package com.timerdar.farmCrm.dto;

import com.timerdar.farmCrm.model.Consumer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter @Setter
public class ConsumerWithOrders extends Consumer {
    private List<OrderWithNameAndWeightable> orders;

    public ConsumerWithOrders(Consumer consumer, List<OrderWithNameAndWeightable> orders){
        super(consumer.getId(), consumer.getName(), consumer.getAddress(), consumer.getPhone(), consumer.getTotalSum(), consumer.getDeliveryOrderNumber());
        this.orders = orders;
    }

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		ConsumerWithOrders that = (ConsumerWithOrders) o;
		if (that.getId() != this.getId()) return false;
		return Objects.equals(orders, that.orders);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(orders);
	}
}
