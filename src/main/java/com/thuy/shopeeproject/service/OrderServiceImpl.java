package com.thuy.shopeeproject.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thuy.shopeeproject.domain.dto.OrderCreateReqDTO;
import com.thuy.shopeeproject.domain.dto.OrderItemCreateReqDTO;
import com.thuy.shopeeproject.domain.entity.Bill;
import com.thuy.shopeeproject.domain.entity.Cart;
import com.thuy.shopeeproject.domain.entity.CartItem;
import com.thuy.shopeeproject.domain.entity.Order;
import com.thuy.shopeeproject.domain.entity.OrderItem;
import com.thuy.shopeeproject.domain.entity.Product;
import com.thuy.shopeeproject.domain.entity.ProductDetail;
import com.thuy.shopeeproject.domain.entity.Size;
import com.thuy.shopeeproject.domain.enums.EOrderStatus;
import com.thuy.shopeeproject.exceptions.CustomErrorException;
import com.thuy.shopeeproject.repository.OrderRepository;

@Service
@Transactional
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private ICartItemService cartItemService;

    @Autowired
    private IOrderItemService orderItemService;

    @Autowired
    private IBillService billService;

    @Autowired
    private IProductDetailService productDetailService;

    @Autowired
    private ISizeService sizeService;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order save(Order order) {
        order = orderRepository.save(order);
        if (order.getOrderStatus() == EOrderStatus.COMPLETED) {
            Bill bill = new Bill();
            bill.setOrder(order);
            bill.setTotal(order.getTotal());
            bill.setDate(new Date());
            bill = billService.save(bill);
        }
        return order;
    }

    @Override
    public void delete(Order e) {
        e.setDeleted(true);
        e = orderRepository.save(e);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setDeleted(true);
            order = orderRepository.save(order);
        }
    }

    @Override
    public Order createOrder(Order order, Cart cart, OrderCreateReqDTO orderCreateReqDTO) {
        order = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItemCreateReqDTO> orderItemCreateReqDTOs = orderCreateReqDTO.getOrderItems();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemCreateReqDTO item : orderItemCreateReqDTOs) {
            Optional<CartItem> optionalCartItem = cartItemService.findById(item.getCartItemId());
            if (!optionalCartItem.isPresent()) {
                throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found this item in your cart");
            }
            CartItem cartItem = optionalCartItem.get();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            Product p = cartItem.getProductDetail().getProduct();
            orderItem.setProduct(p);

            ProductDetail productDetail = cartItem.getProductDetail();
            if (productDetail.getQuantity() == 0) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "This item has sold out");
            }
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            orderItem.setSize(cartItem.getProductDetail().getSize().getSize().getValue());

            total = total.add(orderItem.getTotalPrice());

            orderItem = orderItemService.save(orderItem);
            orderItems.add(orderItem);

            Long newQuantity = productDetail.getQuantity() - orderItem.getQuantity();
            if (newQuantity == 0) {
                productDetail.setSold_out(true);
            }
            if (newQuantity < 0) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Exeed quantity!!!");
            }
            productDetail.setQuantity(newQuantity);
            productDetail = productDetailService.save(productDetail);

            cartItemService.delete(cartItem);
        }

        order.setOrderItem(orderItems);
        order.setTotal(total);
        order.setOrderStatus(EOrderStatus.TO_PAY);
        order = orderRepository.save(order);
        return order;
    }

    @Override
    public List<Order> findByOrderStatus(EOrderStatus orderStatus) {
        return orderRepository.findByOrderStatus(orderStatus);
    }

    @Override
    public Order cancelOrder(Order order) {
        List<OrderItem> orderItems = order.getOrderItem();
        for (OrderItem item : orderItems) {
            Size size = sizeService.findSizeBySize(item.getSize());
            ProductDetail productDetail = productDetailService.findByProductIdAndSize(item.getProduct().getId(),
                    size.getId());
            Long newQuantity = productDetail.getQuantity() + item.getQuantity();
            productDetail.setQuantity(newQuantity);
            productDetail = productDetailService.save(productDetail);
        }
        order.setOrderStatus(EOrderStatus.CANCELLED);
        order = orderRepository.save(order);
        return order;
    }

}
