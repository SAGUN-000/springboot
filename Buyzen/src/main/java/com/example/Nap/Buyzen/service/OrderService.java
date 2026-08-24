package com.example.Nap.Buyzen.service;

import com.example.Nap.Buyzen.dto.CheckoutRequestDto;
import com.example.Nap.Buyzen.dto.OrderDto;
import com.example.Nap.Buyzen.dto.OrderItemDto;
import com.example.Nap.Buyzen.entities.Order;
import com.example.Nap.Buyzen.entities.OrderItem;
import com.example.Nap.Buyzen.entities.Product;
import com.example.Nap.Buyzen.entities.User;
import com.example.Nap.Buyzen.enums.OrderStatus;
import com.example.Nap.Buyzen.repository.OrderItemRepo;
import com.example.Nap.Buyzen.repository.OrderRepo;
import com.example.Nap.Buyzen.repository.ProductRepo;
import com.example.Nap.Buyzen.repository.UserRepo;
import com.example.Nap.Buyzen.security.SecurityPrinciple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.Collection;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;

    private int getCurrentUserId() {
        SecurityPrinciple principle = (SecurityPrinciple) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return principle.getUserId(); // ✅ real user from JWT
    }

    @Transactional
    public OrderDto placeOrder(CheckoutRequestDto checkoutRequestDto) {

        List<OrderItemDto> orderItemDtoList = checkoutRequestDto.getOrderItems();

        int userId = getCurrentUserId();

        // 1. Merge quantities for same product
        Map<Integer, Integer> quantityMap = new HashMap<>();

        for (OrderItemDto dto : orderItemDtoList) {
            quantityMap.put(
                    dto.getProductId(),
                    quantityMap.getOrDefault(dto.getProductId(), 0) + dto.getQuantity()
            );
        }

        // 2. Fetch products
        List<Product> productList = productRepo.findAllById(quantityMap.keySet());

        if (productList.size() != quantityMap.size()) {
            throw new RuntimeException("Some products not found");
        }

        Map<Integer, Product> productMap = productList.stream()
                .collect(Collectors.toMap(
                        Product::getId,
                        p -> p
                ));

        // 3. Stock validation
        for (Product product : productList) {
            int requestedQty = quantityMap.get(product.getId());

            if (product.getStock() < requestedQty) {
                throw new RuntimeException("Not enough stock for product id: " + product.getId());
            }
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 4. Create order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setAddress(checkoutRequestDto.getAddress());
        order.setCity(checkoutRequestDto.getCity());
        order.setProvince(checkoutRequestDto.getProvince());
        order.setPostalCode(checkoutRequestDto.getPostalCode());
        order.setCountry(checkoutRequestDto.getCountry());
        order.setLatitude(checkoutRequestDto.getLatitude());
        order.setLongitude(checkoutRequestDto.getLongitude());


        // IMPORTANT: we calculate total here
        BigDecimal total = BigDecimal.ZERO;

        List<OrderItem> items = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : quantityMap.entrySet()) {

            Product product = productMap.get(entry.getKey());
            int qty = entry.getValue();

            BigDecimal itemTotal = BigDecimal.valueOf(product.getPrice())
                    .multiply(BigDecimal.valueOf(qty));

            total = total.add(itemTotal);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setProductName(product.getName());
            item.setPrice(BigDecimal.valueOf(product.getPrice()));
            item.setQuantity(qty);

            // deduct stock
         //   product.setStock(product.getStock() - qty); for later after payment integration

            items.add(item);
        }

        // 5. Set relationships properly
        order.setOrderItems(items);
        order.setTotal(total);

         // 6. Save (cascade handles items)
        Order savedOrder = orderRepo.save(order);


        return mapToDto(savedOrder);
    }

    private OrderDto mapToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setTotalPrice(order.getTotal());

        List<OrderItemDto> itemDtos = order.getOrderItems()
                .stream()
                .map(item -> {
                    OrderItemDto itemDto = new OrderItemDto();
                    itemDto.setProductId(item.getProduct().getId());
                    itemDto.setProduct_name(
                            item.getProductName() != null
                                    ? item.getProductName()
                                    : item.getProduct().getName()
                    );
                    itemDto.setUrl(item.getProduct().getUrl());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setPrice(item.getPrice());
                    return itemDto;
                })
                .toList();

        dto.setItems(itemDtos);
        return dto;
    }

    @Transactional
    public List<OrderDto> viewOrders() {
        int userId = getCurrentUserId();

        List<Order> orders = orderRepo.findByUserId(userId);

        return orders.stream()
                .map(this::mapToDto)
                .toList();
    }

}




