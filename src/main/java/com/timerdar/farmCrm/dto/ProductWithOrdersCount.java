package com.timerdar.farmCrm.dto;

import com.timerdar.farmCrm.model.Product;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductWithOrdersCount extends Product {
    private int orderedCount;

    public ProductWithOrdersCount(Product product, int orderedCount){
        super(product.getId(), product.getName(), product.getCost(), product.isWeighed(), product.getCreatedCount());
        this.orderedCount = orderedCount;
    }
}
