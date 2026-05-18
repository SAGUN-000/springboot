package com.example.Nap.Buyzen.controller;

import com.example.Nap.Buyzen.dto.OrderDto;
import com.example.Nap.Buyzen.dto.OrderItemDto;
import com.example.Nap.Buyzen.service.OrderService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Controller
@CrossOrigin("http://localhost:5174")
@RequestMapping("/order")
@RequiredArgsConstructor

public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderDto>getOrder(@RequestBody List<OrderItemDto> orderItemDto){

        log.info("Dto received: {}", orderItemDto);

       return ResponseEntity.ok(orderService.placeOrder(orderItemDto));

    }

    @GetMapping("/view_order")
    public ResponseEntity<List<OrderDto>> viewOrder(){
         List<OrderDto> orderDtos=orderService.viewOrders();
         return ResponseEntity.ok(orderDtos);
    }


}
