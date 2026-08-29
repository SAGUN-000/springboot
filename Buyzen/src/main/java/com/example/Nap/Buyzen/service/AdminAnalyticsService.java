package com.example.Nap.Buyzen.service;

import com.example.Nap.Buyzen.dto.AdminOrderDto;
import com.example.Nap.Buyzen.dto.AdminOrderItemDto;
import com.example.Nap.Buyzen.dto.UserPurchaseDto;
import com.example.Nap.Buyzen.entities.Order;
import com.example.Nap.Buyzen.entities.OrderItem;
import com.example.Nap.Buyzen.repository.OrderItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminAnalyticsService {
    private final OrderItemRepo orderItemRepo;

    public List<UserPurchaseDto> getUserPurchaseStats() {
        return orderItemRepo.getUserPurchaseStats();
    }

    public List<AdminOrderDto> getAllOrdersForAdmin() {

        List<OrderItem> items = orderItemRepo.getAllOrderItemsForAdmin();

        return items.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getOrder().getId(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ))
                .values()
                .stream()
                .map(itemsForOrder -> {

                    Order order = itemsForOrder.get(0).getOrder();

                    return new AdminOrderDto(
                            order.getId(),
                            order.getUser().getId(),
                            order.getUser().getName(),
                            order.getUser().getEmail(),
                            order.getStatus().toString(),
                            itemsForOrder.stream()
                                    .map(item -> new AdminOrderItemDto(
                                            item.getProduct().getId(),
                                            item.getProduct().getName(),
                                            item.getProduct().getUrl(),
                                            item.getQuantity()
                                    ))
                                    .toList()
                    );
                })
                .toList();
    }




}
