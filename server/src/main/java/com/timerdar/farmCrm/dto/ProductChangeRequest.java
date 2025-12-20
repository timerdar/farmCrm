package com.timerdar.farmCrm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ProductChangeRequest {
    private long id;
	private String name;
    private double cost;
    private int createdCount;

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ProductChangeRequest{");
		sb.append("id=").append(id);
		sb.append(", name='").append(name).append('\'');
		sb.append(", cost=").append(cost);
		sb.append(", createdCount=").append(createdCount);
		sb.append('}');
		return sb.toString();
	}
}
