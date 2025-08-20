package com.timerdar.farmCrm.service;

import com.timerdar.farmCrm.dto.CreateProductRequest;
import com.timerdar.farmCrm.dto.ProductWithOrdersCount;
import com.timerdar.farmCrm.dto.ShortProductInfo;
import com.timerdar.farmCrm.model.OrderStatus;
import com.timerdar.farmCrm.model.Product;
import com.timerdar.farmCrm.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    @Lazy
    private OrderService orderService;

    public Product createProduct(CreateProductRequest request){
        request.check();
        Product product = new Product(0, request.getName(), request.getCost(), request.isWeightable(), 0);
        return productRepository.save(product);
    }

    public List<ProductWithOrdersCount> getProductsList(){
        List<ProductWithOrdersCount> res = new ArrayList<>();
        for (Product product : productRepository.findAll(Sort.by("name"))){
            res.add(new ProductWithOrdersCount(product, orderService.getOrdersCount(product.getId(), OrderStatus.CREATED)));
        }
        return res;
    }

    public Product getProductById(long id){
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()){
            return product.get();
        }else{
            throw new EntityNotFoundException("Такой позиции не существует");
        }
    }

    public List<ShortProductInfo> getShortInfo(){
        List<ShortProductInfo> shortProducts = new ArrayList<>();
        for(Product product: getProductsList()){
            shortProducts.add(product.toShort());
        }
        return shortProducts;
    }

    public boolean isPriceExists(long id){
        return productRepository.existsById(id);
    }

    @Transactional
    public int changeCreatedCount(long id, int createdCount){
        return productRepository.updateCreatedCount(id, createdCount);
    }

    @Transactional
    public int changePrice(long id, double cost){
        return productRepository.updatePrice(id, cost);
    }
}
