package com.timerdar.farmCrm.service;

import com.timerdar.farmCrm.model.Product;
import com.timerdar.farmCrm.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product){
        if (product.isValid()){
            return productRepository.save(product);
        }else{
            throw new IllegalArgumentException("Для создания позиции введены не все данные");
        }
    }

    public List<Product> getProductsList(){
        return productRepository.findAll(Sort.by("productName"));
    }

    public Product getProductById(long id){
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()){
            return product.get();
        }else{
            throw new EntityNotFoundException("Такой позиции не существует");
        }
    }

    public boolean isPriceExists(long id){
        return productRepository.existsById(id);
    }
}
