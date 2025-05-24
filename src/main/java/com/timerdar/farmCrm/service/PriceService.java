package com.timerdar.farmCrm.service;

import com.timerdar.farmCrm.model.Price;
import com.timerdar.farmCrm.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    public Price createPrice(Price price){
        return priceRepository.save(price);
    }

    public List<Price> getPriceList(){
        return priceRepository.findAll();
    }

    public Price getPriceById(long id){
        return priceRepository.getReferenceById(id);
    }

}
