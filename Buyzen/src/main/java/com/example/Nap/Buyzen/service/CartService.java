package com.example.Nap.Buyzen.service;

import com.example.Nap.Buyzen.dto.CartDto;
import com.example.Nap.Buyzen.dto.CartItemsDto;
import com.example.Nap.Buyzen.dto.ProductDto;
import com.example.Nap.Buyzen.entities.Cart;
import com.example.Nap.Buyzen.entities.CartItems;
import com.example.Nap.Buyzen.entities.Product;
import com.example.Nap.Buyzen.entities.User;
import com.example.Nap.Buyzen.repository.CartItemRepo;
import com.example.Nap.Buyzen.repository.CartRepo;
import com.example.Nap.Buyzen.repository.ProductRepo;
import com.example.Nap.Buyzen.repository.UserRepo;
import com.example.Nap.Buyzen.security.SecurityPrinciple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {


    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
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
    public CartDto addToCart(CartItemsDto cartItemsDto) {
        int userId = getCurrentUserId();

        // 1. Get or create cart
        Cart cart = cartRepo.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();

                    User user = userRepo.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    newCart.setUser(user);

                    return cartRepo.save(newCart);
                });

        // 2. Get product
        Product product = productRepo.findById(cartItemsDto.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));


        int quantity = cartItemsDto.getQuantity() != null ? cartItemsDto.getQuantity() : 1;


        // 3. Check existing item
        Optional<CartItems> existingItem =
                cartItemRepo.findByCartIdAndProductId(cart.getId(), product.getId());

        if (existingItem.isPresent()) {
            CartItems item = existingItem.get();
            int newQty = item.getQuantity() + quantity;

            if (newQty <= 0) {
                cart.getItems().remove(item);
                cartItemRepo.delete(item);
            } else {
                item.setQuantity(newQty);
            }

        } else {
            if (quantity < 1 ) {
                throw new RuntimeException("invalid quantity");
            }

            CartItems item = new CartItems();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);

            cart.getItems().add(item);
            cartItemRepo.save(item);
        } // OK here


        // ❌ NO RE-FETCH
        // ❌ NO EXTRA SAVE

        return mapToCartDto(cart);
    }

    public CartDto viewCart(){

        int userId=getCurrentUserId();
        Cart cart=cartRepo.findByUserId(userId)
                .orElse(null);

        if (cart==null){
            return new CartDto(Collections.emptyList());
        }
        return mapToCartDto(cart);
    }

    private CartDto mapToCartDto(Cart cart) {

        List<CartItemsDto> items = cart.getItems().stream()
                .map(item -> {
                    CartItemsDto dto = new CartItemsDto();
                    dto.setId(item.getProduct().getId());
                    dto.setName(item.getProduct().getName());
                    dto.setPrice(item.getProduct().getPrice());
                    dto.setUrl(item.getProduct().getUrl());
                    dto.setQuantity(item.getQuantity());
                    dto.setSubtotal(item.getProduct().getPrice() * item.getQuantity());
                    return dto;
                })
                .toList();

        double total = items.stream()
                .mapToDouble(CartItemsDto::getSubtotal)
                .sum();

        CartDto cartDto = new CartDto();
        cartDto.setCartId(cart.getId());
        cartDto.setUserId(cart.getUser().getId());
        cartDto.setCartItemsDtoList(items);
        cartDto.setSubtotal(total);

        return cartDto;
    }

    public CartDto deleteCartItems(List<Integer> productIds){

        int userId = getCurrentUserId();

        List<CartItems> cartItems =
                cartItemRepo.findByCart_User_IdAndProduct_IdIn(
                        userId,
                        productIds
                );

        if(cartItems.size() != productIds.size()){
            throw new RuntimeException("Some products not found");
        }

        cartItemRepo.deleteAllInBatch(cartItems);

        return viewCart();
    }


}
