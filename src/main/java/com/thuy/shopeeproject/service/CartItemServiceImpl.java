package com.thuy.shopeeproject.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thuy.shopeeproject.domain.dto.CartItemReqDTO;
import com.thuy.shopeeproject.domain.dto.CartItemUpdateReqDTO;
import com.thuy.shopeeproject.domain.entity.Cart;
import com.thuy.shopeeproject.domain.entity.CartItem;
import com.thuy.shopeeproject.domain.entity.ProductDetail;
import com.thuy.shopeeproject.domain.entity.Size;
import com.thuy.shopeeproject.exceptions.CustomErrorException;
import com.thuy.shopeeproject.repository.CartItemRepository;

@Service
@Transactional
public class CartItemServiceImpl implements ICartItemService {

    @Autowired
    private IProductDetailService productDetailService;

    @Autowired
    private ISizeService sizeService;

    @Autowired
    private ICartService cartService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public List<CartItem> findAll() {
        return cartItemRepository.findAll();
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

    @Override
    public CartItem addToCart(Cart cart, ProductDetail productDetail, CartItemReqDTO cartItemReqDTO) {
        CartItem existsCartItem = cartItemRepository.findByDetailIdAndCartId(productDetail.getId(), cart.getId());

        if (existsCartItem != null) {
            Long newQuantity = cartItemReqDTO.getQuantity() + existsCartItem.getQuantity();
            Long newProductDetailQuantity = productDetail.getQuantity() - newQuantity;
            if (newProductDetailQuantity < 0) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Exceeded quantity!!!");
            }
            existsCartItem.setQuantity(newQuantity);
            BigDecimal totalPrice = productDetail.getPrice().multiply(BigDecimal.valueOf(existsCartItem.getQuantity()));
            existsCartItem.setTotalPrice(totalPrice);
            existsCartItem = cartItemRepository.save(existsCartItem);
            return existsCartItem;
        }

        Long newQuantity = cartItemReqDTO.getQuantity();
        Long newProductDetailQuantity = productDetail.getQuantity() - newQuantity;
        if (newProductDetailQuantity < 0) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Exceeded quantity!!!");
        }

        CartItem cartItem = new CartItem(productDetail, cartItemReqDTO.getQuantity());

        cartItem.setCart(cart);
        cart = cartService.save(cart);
        cartItem = cartItemRepository.save(cartItem);
        return cartItem;
    }

    @Override
    public CartItem updateCart(CartItem cartItem, Cart cart, CartItemUpdateReqDTO cartItemUpdateReqDTO) {
        CartItem newCartItem = new CartItem();

        if (cartItemUpdateReqDTO.getSizeId() != null) {
            Optional<Size> optionalSize = sizeService.findById(cartItemUpdateReqDTO.getSizeId());
            if (!optionalSize.isPresent()) {
                throw new CustomErrorException(HttpStatus.NOT_FOUND, "Invalid size!!");
            }

            ProductDetail productDetail = productDetailService
                    .findByProductIdAndSize(cartItemUpdateReqDTO.getProductId(), optionalSize.get().getId());

            CartItem existsCartItem = cartItemRepository.findByDetailIdAndCartId(productDetail.getId(), cart.getId());
            if (existsCartItem != null) {
                Long newQuantity = cartItem.getQuantity() + existsCartItem.getQuantity();
                Long newProductDetailQuantity = productDetail.getQuantity() - newQuantity;
                if (newProductDetailQuantity < 0) {
                    throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Exceeded quantity!!!");
                }
                existsCartItem.setQuantity(newQuantity);
                BigDecimal totalPrice = productDetail.getPrice()
                        .multiply(BigDecimal.valueOf(existsCartItem.getQuantity()));
                existsCartItem.setTotalPrice(totalPrice);
                newCartItem = existsCartItem;

            } else {
                newCartItem.setProductDetail(productDetail);

                Long newQuantity = cartItem.getQuantity();
                Long newProductDetailQuantity = productDetail.getQuantity() - newQuantity;
                if (newProductDetailQuantity < 0) {
                    throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Exceeded quantity!!!");
                }
                newCartItem.setQuantity(cartItem.getQuantity());

                newCartItem.setUnitPrice(productDetail.getPrice());
                BigDecimal totalPrice = productDetail.getPrice()
                        .multiply(BigDecimal.valueOf(newCartItem.getQuantity()));
                newCartItem.setTotalPrice(totalPrice);
            }

        }

        if (cartItemUpdateReqDTO.getQuantity() != null) {
            Long newQuantity = cartItem.getQuantity() + cartItemUpdateReqDTO.getQuantity();
            ProductDetail productDetail = cartItem.getProductDetail();
            Long newProductDetailQuantity = productDetail.getQuantity() - newQuantity;
            if (newProductDetailQuantity < 0) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Exceeded quantity!!!");
            }
            newCartItem.setQuantity(cartItemUpdateReqDTO.getQuantity());

            BigDecimal totalPrice = cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            newCartItem.setTotalPrice(totalPrice);
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(newCartItem, cartItem);

        cartItem = cartItemRepository.save(cartItem);
        return cartItem;
    }

}
