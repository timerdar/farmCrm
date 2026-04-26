package com.timerdar.farmCrm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter @Setter
public class DeliverySummaryItem {
    private String productName;
    private int createdCount;
    private int orderedCount;
	private Map<String, Double> consumers;
}
