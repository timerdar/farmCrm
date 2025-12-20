package com.timerdar.farmCrm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ConsumerChangeRequest {
    private long id;
	private String name;
    private String phone;
    private String address;

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ConsumerChangeRequest{");
		sb.append("id=").append(id);
		sb.append(", name='").append(name).append('\'');
		sb.append(", phone='").append(phone).append('\'');
		sb.append(", address='").append(address).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
