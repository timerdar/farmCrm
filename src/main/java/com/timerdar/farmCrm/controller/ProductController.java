package com.timerdar.farmCrm.controller;

import com.timerdar.farmCrm.model.Product;
import com.timerdar.farmCrm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired private ProductService productService;

    @GetMapping
    public List<Product> getPriceList(){
        return productService.getProductsList();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable long id){
        return productService.getProductById(id);
    }

}
