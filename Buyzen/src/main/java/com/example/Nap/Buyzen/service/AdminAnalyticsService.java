package com.example.Nap.Buyzen.service;

import com.example.Nap.Buyzen.dto.UserPurchaseDto;
import com.example.Nap.Buyzen.repository.OrderItemRepo;
import com.example.Nap.Buyzen.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAnalyticsService {
    private final OrderItemRepo orderItemRepo;

    public List<UserPurchaseDto> getUserPurchaseStats() {
        return orderItemRepo.getUserPurchaseStats();
    }


}
