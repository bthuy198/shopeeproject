package com.thuy.shopeeproject.domain.entity;

import java.math.BigDecimal;

import com.thuy.shopeeproject.domain.dto.CartItemResDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;
    private Long quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "id", nullable = false)
    private Cart cart;

    public CartItem(ProductDetail p, Long quantity) {
        this.quantity = quantity;
        this.productDetail = p;
        this.unitPrice = productDetail.getPrice();
        this.totalPrice = productDetail.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public CartItem(ProductDetail p) {
        this.productDetail = p;
        this.unitPrice = productDetail.getPrice();
        this.totalPrice = productDetail.getPrice().multiply(BigDecimal.valueOf(this.quantity));
    }

    public CartItemResDTO toCartItemResDTO() {
        return new CartItemResDTO()
                .setId(id)
                .setProductDetail(productDetail.toProductDetailResDTO())
                .setQuantity(quantity)
                .setTotalPrice(totalPrice)
                .setUnitPrice(unitPrice);
    }

}
