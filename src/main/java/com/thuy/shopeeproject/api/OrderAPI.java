package com.thuy.shopeeproject.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thuy.shopeeproject.domain.dto.CancelOrderReqDTO;
import com.thuy.shopeeproject.domain.dto.CompleteOrderReqDTO;
import com.thuy.shopeeproject.domain.dto.OrderCreateReqDTO;
import com.thuy.shopeeproject.domain.dto.OrderItemCreateReqDTO;
import com.thuy.shopeeproject.domain.dto.OrderResDTO;
import com.thuy.shopeeproject.domain.entity.Cart;
import com.thuy.shopeeproject.domain.entity.CartItem;
import com.thuy.shopeeproject.domain.entity.Order;
import com.thuy.shopeeproject.domain.entity.OrderItem;
import com.thuy.shopeeproject.domain.entity.PaymentMethod;
import com.thuy.shopeeproject.domain.entity.Product;
import com.thuy.shopeeproject.domain.entity.User;
import com.thuy.shopeeproject.domain.entity.UserAddress;
import com.thuy.shopeeproject.domain.enums.EOrderStatus;
import com.thuy.shopeeproject.exceptions.CustomErrorException;
import com.thuy.shopeeproject.service.ICartItemService;
import com.thuy.shopeeproject.service.ICartService;
import com.thuy.shopeeproject.service.IOrderItemService;
import com.thuy.shopeeproject.service.IOrderService;
import com.thuy.shopeeproject.service.IPaymentMethodService;
import com.thuy.shopeeproject.service.IUserAddressService;
import com.thuy.shopeeproject.service.IUserService;

import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/order")
public class OrderAPI {

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserAddressService userAddressService;

    @Autowired
    private IPaymentMethodService paymentMethodService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderItemService orderItemService;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ICartItemService cartItemService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllOrder(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");

        Optional<User> optionalUser = userService.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Please sign in to order");
        }

        List<Order> orders = orderService.findAll();

        List<OrderResDTO> orderResDTOs = new ArrayList<>();

        for (Order order : orders) {
            OrderResDTO orderResDTO = order.toOrderResDTO();
            orderResDTOs.add(orderResDTO);
        }

        return new ResponseEntity<>(orderResDTOs, HttpStatus.OK);
    }

    @GetMapping("/get-by-status")
    public ResponseEntity<?> getOrderByStatus(@RequestParam("order_status") String orderStatus,
            HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");

        Optional<User> optionalUser = userService.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Please sign in to order");
        }

        EOrderStatus eOrderStatus = EOrderStatus.fromString(orderStatus);

        List<Order> ordersByStatus = new ArrayList<>();

        if (eOrderStatus == EOrderStatus.ALL) {
            ordersByStatus = orderService.findAll();
        } else {
            ordersByStatus = orderService.findByOrderStatus(eOrderStatus);
        }

        List<OrderResDTO> orderResDTOs = new ArrayList<>();

        for (Order order : ordersByStatus) {
            OrderResDTO orderResDTO = order.toOrderResDTO();
            orderResDTOs.add(orderResDTO);
        }

        return new ResponseEntity<>(orderResDTOs, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderCreateReqDTO orderCreateReqDTO, HttpServletRequest request) {
        Long cartId = (Long) request.getSession().getAttribute("cartId");
        Long userId = (Long) request.getSession().getAttribute("userId");

        Optional<User> optionalUser = userService.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Please sign in to order");
        }

        Optional<Cart> optionalCart = cartService.findById(cartId);
        if (!optionalCart.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found your cart. Please sign in");
        }

        Optional<UserAddress> optionalAddress = userAddressService
                .findById(Long.valueOf(orderCreateReqDTO.getUserAddressId()));
        if (!optionalAddress.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Please select address to order");
        }

        Optional<PaymentMethod> optionalMethod = paymentMethodService
                .findById(Long.valueOf(orderCreateReqDTO.getPaymentMethodId()));
        if (!optionalMethod.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Invalid payment method");
        }

        PaymentMethod paymentMethod = optionalMethod.get();

        UserAddress userAddress = optionalAddress.get();

        Order order = new Order();
        order.setUser(optionalUser.get());
        order.setAddress(userAddress.getAddress());
        order.setName(userAddress.getName());
        order.setPhone(userAddress.getPhone());
        order.setPaymentMethod(paymentMethod);
        order = orderService.createOrder(order, optionalCart.get(), orderCreateReqDTO);

        return new ResponseEntity<>(order.toOrderResDTO(), HttpStatus.OK);
    }

    @PostMapping("/cancel-checkout")
    public ResponseEntity<?> postMethodName(@RequestBody CancelOrderReqDTO cancelOrderReqDTO,
            HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        Optional<User> optionalUser = userService.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Please sign in to order");
        }

        Optional<Order> optionalOrder = orderService.findById(cancelOrderReqDTO.getOrderId());
        if (!optionalOrder.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found your order.");
        }

        if (optionalOrder.get().getUser().getId() != optionalUser.get().getId()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "You do not have permission to cancel this order");
        }

        Order order = optionalOrder.get();
        EOrderStatus orderStatus = order.getOrderStatus();
        if (orderStatus == EOrderStatus.TO_PAY) {
            order.setOrderStatus(EOrderStatus.CANCELLED);
            order = orderService.save(order);
        } else {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Can't cancel your order.");
        }

        return new ResponseEntity<>(order.toOrderResDTO(), HttpStatus.OK);
    }

    @PostMapping("/complete-order")
    public ResponseEntity<?> completeOrder(@RequestBody CompleteOrderReqDTO completeOrderReqDTO,
            HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        Optional<User> optionalUser = userService.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Please sign in to complete order");
        }

        Optional<Order> optionalOrder = orderService.findById(completeOrderReqDTO.getOrderId());
        if (!optionalOrder.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found your order.");
        }

        if (optionalOrder.get().getUser().getId() != optionalUser.get().getId()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "You do not have permission to confirm.");
        }

        Order order = optionalOrder.get();
        EOrderStatus orderStatus = order.getOrderStatus();
        if (orderStatus == EOrderStatus.TO_RECEIVE) {
            order.setOrderStatus(EOrderStatus.COMPLETED);
            order = orderService.save(order);
        } else {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Can't confirm your order.");
        }
        return new ResponseEntity<>(order.toOrderResDTO(), HttpStatus.OK);
    }

}
