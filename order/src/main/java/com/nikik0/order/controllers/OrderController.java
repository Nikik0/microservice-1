package com.nikik0.order.controllers;

import com.nikik0.order.entities.Order;
import com.nikik0.order.repositories.OrderRepository;
import com.nikik0.order.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {
    private static final String ENTITY_NAME = "order";
    @Value("${spring.application.name}")
    private String applicationName;
    private final OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrders() throws URISyntaxException {
        var result = orderService.getAll();
        HttpHeaders headers = new HttpHeaders();
        String message = String.format("A new %s is created with identifier %s", ENTITY_NAME, result.size());
        headers.add("X-" + applicationName + "-alert", message);
        headers.add("X-" + applicationName + "-params", String.valueOf(result.size()));
        return ResponseEntity.created(new URI("/api/orders/" + result.size())).headers(headers).body(result);
    }

    @PostMapping("/orders")
    @Transactional
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) throws URISyntaxException {
        log.debug("REST request to save Order : {}", order);
        if (order.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new order cannot already have an ID");
        }
        final var result = orderService.createOrder(order);
        HttpHeaders headers = new HttpHeaders();
        String message = String.format("A new %s is created with identifier %s", ENTITY_NAME, result.getId().toString());
        headers.add("X-" + applicationName + "-alert", message);
        headers.add("X-" + applicationName + "-params", result.getId().toString());
        return ResponseEntity.created(new URI("/api/orders/" + result.getId())).headers(headers).body(result);
    }
}
