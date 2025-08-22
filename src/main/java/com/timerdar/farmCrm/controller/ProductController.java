package com.timerdar.farmCrm.controller;

import com.timerdar.farmCrm.dto.CreateProductRequest;
import com.timerdar.farmCrm.dto.ProductChangeRequest;
import com.timerdar.farmCrm.dto.ProductWithOrdersCount;
import com.timerdar.farmCrm.dto.ShortProductInfo;
import com.timerdar.farmCrm.model.Product;
import com.timerdar.farmCrm.service.OrderService;
import com.timerdar.farmCrm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin()
public class ProductController {

    @Autowired private ProductService productService;


    @GetMapping
    public List<ProductWithOrdersCount> getPriceList(){
        return productService.getProductsList();
    }

    @PostMapping
    public Product createProduct(@RequestBody CreateProductRequest request){
        return productService.createProduct(request);
    }

    @GetMapping("/{id}")
    public ProductWithOrdersCount getProduct(@PathVariable long id){
        return productService.getProductById(id);
    }

    @GetMapping("/short")
    public List<ShortProductInfo> getShortInfo(){
        return productService.getShortInfo();
    }

    @PostMapping("/change-created-count")
    public ResponseEntity<?> changeCreatedCount(@RequestBody ProductChangeRequest request){
        int count = productService.changeCreatedCount(request.getId(), request.getCreatedCount());
        if(count > 0){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/change-price")
    public ResponseEntity<?> changePrice(@RequestBody ProductChangeRequest request){
        int count = productService.changePrice(request.getId(), request.getCost());
        if(count > 0){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.badRequest().build();
        }
    }
}
