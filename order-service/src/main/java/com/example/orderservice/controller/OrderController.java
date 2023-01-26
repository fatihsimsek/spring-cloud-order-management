package com.example.orderservice.controller;

import com.example.orderservice.entity.Order;
import com.example.orderservice.payload.ApiResponse;
import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Order order) {
        orderService.save(order);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        Optional<Order> order = orderService.getById(id);
        if(order.isPresent()) {
            return ResponseEntity.ok(order.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Order not found"));
        }
    }
}
