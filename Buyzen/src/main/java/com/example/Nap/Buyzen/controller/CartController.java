package com.example.Nap.Buyzen.controller;

import com.example.Nap.Buyzen.dto.CartDto;
import com.example.Nap.Buyzen.dto.CartItemsDto;
import com.example.Nap.Buyzen.dto.ProductDto;
import com.example.Nap.Buyzen.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/addtocart")
    public ResponseEntity<CartDto> addToCart(@RequestBody CartItemsDto cartItemsDto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED).build();

        } else {
            System.out.println("DTO RECEIVED: " + cartItemsDto);
            System.out.println("PRODUCT ID: " + cartItemsDto.getId());
            CartDto cartDto = cartService.addToCart(cartItemsDto);
            if (cartDto == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(cartDto);
        }
    }

    @GetMapping("/view_cart")
    public ResponseEntity<CartDto> viewCart() {
        return ResponseEntity.ok(cartService.viewCart());
    }

    @DeleteMapping("/delete_cartItem")
    public ResponseEntity<CartDto>deleteCartItem(@RequestBody List<Integer>productIds){
        return ResponseEntity.ok(cartService.deleteCartItems(productIds));
    }

}

