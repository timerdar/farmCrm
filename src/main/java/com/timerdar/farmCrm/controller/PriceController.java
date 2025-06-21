package com.timerdar.farmCrm.controller;

import com.timerdar.farmCrm.model.Price;
import com.timerdar.farmCrm.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/price")
public class PriceController {

    @Autowired private PriceService priceService;

    @GetMapping
    public Price getPrice(@RequestParam("productId") long productId){
        return priceService.getPriceById(productId);
    }

    @GetMapping("/all")
    public List<Price> getPriceList(){
        return priceService.getPriceList();
    }

}
