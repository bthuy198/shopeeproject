package com.thuy.shopeeproject.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuy.shopeeproject.domain.dto.CartItemResDTO;
import com.thuy.shopeeproject.domain.dto.CartItemUpdateReqDTO;
import com.thuy.shopeeproject.domain.entity.Cart;
import com.thuy.shopeeproject.domain.entity.CartItem;
import com.thuy.shopeeproject.domain.entity.ProductDetail;
import com.thuy.shopeeproject.domain.entity.Size;
import com.thuy.shopeeproject.domain.entity.User;
import com.thuy.shopeeproject.exceptions.CustomErrorException;
import com.thuy.shopeeproject.service.ICartItemService;
import com.thuy.shopeeproject.service.ICartService;
import com.thuy.shopeeproject.service.IProductDetailService;
import com.thuy.shopeeproject.service.IProductService;
import com.thuy.shopeeproject.service.ISizeService;
import com.thuy.shopeeproject.service.IUserService;

import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/cart")
public class CartAPI {

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductDetailService productDetailService;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ICartItemService cartItemService;

    @Autowired
    private ISizeService sizeService;

    @Autowired
    private IUserService userService;

    @GetMapping("")
    public ResponseEntity<?> getCart(HttpServletRequest request) {
        Long cartId = (Long) request.getSession().getAttribute("cartId");

        Optional<Cart> optionalCart = cartService.findById(cartId);

        if (!optionalCart.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found your cart. Please sign in");
        }

        List<CartItem> cartItems = optionalCart.get().getCartItems();
        List<CartItemResDTO> cartItemDTOs = new ArrayList<>();
        for (CartItem item : cartItems) {
            cartItemDTOs.add(item.toCartItemResDTO());
        }
        return new ResponseEntity<>(cartItemDTOs, HttpStatus.OK);
    }

    @PostMapping("/update-cart")
    public ResponseEntity<?> updateCart(@RequestBody CartItemUpdateReqDTO cartItemUpdateReqDTO,
            HttpServletRequest request) {

        Long cartId = (Long) request.getSession().getAttribute("cartId");

        Optional<CartItem> optionalCartItem = cartItemService.findById(cartItemUpdateReqDTO.getCartItemId());
        if (!optionalCartItem.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found this cart item");
        }

        CartItem cartItem = optionalCartItem.get();

        if (cartItem.getCart().getId() != cartService.findById(cartId).get().getId()) {
            throw new CustomErrorException(HttpStatus.FORBIDDEN, "You do not have permission to change");
        }

        CartItem newCartItem = new CartItem();

        if (cartItemUpdateReqDTO.getSizeId() != null) {
            Optional<Size> optionalSize = sizeService.findById(cartItemUpdateReqDTO.getSizeId());
            if (!optionalSize.isPresent()) {
                throw new CustomErrorException(HttpStatus.NOT_FOUND, "Invalid size!!");
            }

            ProductDetail productDetail = productDetailService
                    .findByProductIdAndSize(cartItemUpdateReqDTO.getProductId(), optionalSize.get().getId());
            newCartItem.setProductDetail(productDetail);
            newCartItem.setQuantity(cartItem.getQuantity());

            newCartItem.setUnitPrice(productDetail.getPrice());
            BigDecimal totalPrice = productDetail.getPrice().multiply(BigDecimal.valueOf(newCartItem.getQuantity()));
            newCartItem.setTotalPrice(totalPrice);
        }

        if (cartItemUpdateReqDTO.getQuantity() != null) {
            newCartItem.setQuantity(cartItemUpdateReqDTO.getQuantity());

            BigDecimal totalPrice = cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            newCartItem.setTotalPrice(totalPrice);
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(newCartItem, cartItem);

        cartItem = cartItemService.save(cartItem);

        return new ResponseEntity<>(cartItem.toCartItemResDTO(), HttpStatus.OK);
    }

    @DeleteMapping("/update-cart/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable("cartItemId") Long cartItemId, HttpServletRequest request) {
        Long cartId = (Long) request.getSession().getAttribute("cartId");

        Optional<CartItem> optionalCartItem = cartItemService.findById(cartItemId);

        if (!optionalCartItem.isPresent()) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not found this cart item in your cart");
        }

        CartItem cartItem = optionalCartItem.get();

        if (cartItem.getCart().getId() != cartId) {
            throw new CustomErrorException(HttpStatus.FORBIDDEN, "You do not have permission to delete");
        }

        cartItemService.delete(cartItem);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody List<Long> numbers, HttpServletRequest request) {

        Long cartId = (Long) request.getSession().getAttribute("cartId");

        List<CartItem> checkoutList = new ArrayList<>();
        for (Long itemId : numbers) {
            Optional<CartItem> optionalCartItem = cartItemService.findById(itemId);
            CartItem cartItem = optionalCartItem.get();
            if (cartItem.getCart().getId() != cartId) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "You do not have permission to checkout item");
            }
            checkoutList.add(cartItem);
        }

        List<CartItemResDTO> checkoutDTOs = new ArrayList<>();

        for (CartItem item : checkoutList) {
            checkoutDTOs.add(item.toCartItemResDTO());
        }
        return new ResponseEntity<>(checkoutDTOs, HttpStatus.OK);
    }

}
