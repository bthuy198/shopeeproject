package com.thuy.shopeeproject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thuy.shopeeproject.domain.entity.CartItem;
import com.thuy.shopeeproject.domain.entity.ProductDetail;
import com.thuy.shopeeproject.repository.CartItemRepository;

@Service
@Transactional
public class CartItemServiceImpl implements ICartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public List<CartItem> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<CartItem> findById(Long id) {
        return cartItemRepository.findById(id);
    }

    @Override
    public CartItem save(CartItem e) {
        return cartItemRepository.save(e);
    }

    @Override
    public void delete(CartItem e) {
        cartItemRepository.delete(e);
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public CartItem findByDetailIdAndCartId(Long productDetailId, Long cartId) {
        return cartItemRepository.findByDetailIdAndCartId(productDetailId, cartId);
    }

}
