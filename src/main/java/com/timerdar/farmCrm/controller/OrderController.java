package com.timerdar.farmCrm.controller;

import com.timerdar.farmCrm.dto.ConsumerWithOrders;
import com.timerdar.farmCrm.dto.CreateOrderRequest;
import com.timerdar.farmCrm.dto.OrderChangeRequest;
import com.timerdar.farmCrm.dto.OrderWithName;
import com.timerdar.farmCrm.model.Order;
import com.timerdar.farmCrm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin()
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order createOrder(@RequestBody CreateOrderRequest request){
        return orderService.createOrder(request);
    }

    @GetMapping("/{source}/{id}")
    public List<OrderWithName> getOrders(@PathVariable("id") long id, @PathVariable("source") String source, @RequestParam(name = "status", defaultValue = "CREATED") String status){
        return orderService.getOrdersWithName(id, source, status);
    }

    @PostMapping("/change-status")
    public Order changeOrderStatus(@RequestBody OrderChangeRequest request){
        return orderService.changeStatus(request);
    }

    @PostMapping("/change-amount")
    public Order changeOrderAmount(@RequestBody OrderChangeRequest request){
        return orderService.changeAmount(request);
    }

    @PostMapping("/change-weight")
    public Order changeOrderWeight(@RequestBody OrderChangeRequest request){
        return orderService.changeWeight(request);
    }

    @GetMapping("/delivery")
    public List<ConsumerWithOrders> getDeliveryOrders(){
        return orderService.getDeliveryData();
    }

    @GetMapping("/clear-delivery")
    public ResponseEntity<?> clearDelivery(){
        int count = orderService.clearDelivery();
        if(count > 0){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.badRequest().build();
        }
    }
}
