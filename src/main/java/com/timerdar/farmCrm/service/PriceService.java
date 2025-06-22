package com.timerdar.farmCrm.service;

import com.timerdar.farmCrm.model.Price;
import com.timerdar.farmCrm.repository.PriceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    public Price createPrice(Price price){
        if (price.isValid()){
            return priceRepository.save(price);
        }else{
            throw new IllegalArgumentException("Для создания позиции введены не все данные");
        }
    }

    public List<Price> getPriceList(){
        return priceRepository.findAll(Sort.by("productName"));
    }

    public Price getPriceById(long id){
        Optional<Price> price = priceRepository.findById(id);
        if (price.isPresent()){
            return price.get();
        }else{
            throw new EntityNotFoundException("Такой позиции не существует");
        }
    }

    public boolean isPriceExists(long id){
        return priceRepository.existsById(id);
    }
}
