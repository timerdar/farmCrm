package com.timerdar.farmCrm.service;

import com.timerdar.farmCrm.dto.CreateProductRequest;
import com.timerdar.farmCrm.dto.ProductChangeRequest;
import com.timerdar.farmCrm.dto.ProductWithOrdersCount;
import com.timerdar.farmCrm.dto.ShortProductInfo;
import com.timerdar.farmCrm.model.Order;
import com.timerdar.farmCrm.model.OrderStatus;
import com.timerdar.farmCrm.model.Product;
import com.timerdar.farmCrm.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
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
        Product createdProduct = productRepository.save(product);
        log.info("Создание продукта: {}", createdProduct);
        return createdProduct;
    }

    public List<ProductWithOrdersCount> getProductsList(){
        List<ProductWithOrdersCount> res = new ArrayList<>();
        for (Product product : productRepository.findAll(Sort.by("name"))){
            int ordersCount = orderService.getOrdersCount(product.getId(), OrderStatus.CREATED) + orderService.getOrdersCount(product.getId(), OrderStatus.DELIVERY);
            res.add(new ProductWithOrdersCount(product, ordersCount));
        }
        log.info("Получение списка продуктов");
        return res;
    }

    public ProductWithOrdersCount getProductById(long id){
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()){
            int ordersCount = orderService.getOrdersCount(product.get().getId(), OrderStatus.CREATED);
            log.info("Получение продукта: id = {}", id);
            return new ProductWithOrdersCount(product.get(), ordersCount);
        }else{
            throw new EntityNotFoundException("Такой позиции не существует");
        }
    }

    public List<ShortProductInfo> getShortInfo(){
        List<ShortProductInfo> shortProducts = new ArrayList<>();
        for(Product product: getProductsList()){
            shortProducts.add(product.toShort());
        }
        log.info("Получение short-листа продуктов");
        return shortProducts;
    }

    public boolean isPriceExists(long id){
        return productRepository.existsById(id);
    }

    @Transactional
    public int changeCreatedCount(long id, int createdCount){
        log.info("Изменение createdCount: id = {}, newCreatedCount = {}", id, createdCount);
        return productRepository.updateCreatedCount(id, createdCount);
    }

    @Transactional
    public int changePrice(long id, double cost){
        log.info("Изменение цены продукта: id = {}, newPrice = {}", id, cost);
        return productRepository.updatePrice(id, cost);
    }

    public List<Product> getProductsFromDelivery(){
        log.info("Получение списка продуктов, которые находятся в доставке");
        return productRepository.getProductsListFromDelivery();
    }

	@Transactional
	public int updateProduct(ProductChangeRequest request){
		log.info("Обновление данных продукта: req = {}", request);
		int i = productRepository.updateProduct(request.getId(), request.getName(), request.getCost(), request.getCreatedCount());
		for(Order order : orderService.getOrdersOfProduct(request.getId(), OrderStatus.CREATED)){
			orderService.evalCost(order.getId());
		}
		for (Order order : orderService.getOrdersOfProduct(request.getId(), OrderStatus.DELIVERY)){
			orderService.evalCost(order.getId());
		}
		return i;
	}
}
