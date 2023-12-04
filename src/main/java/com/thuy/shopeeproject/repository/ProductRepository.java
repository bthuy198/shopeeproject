package com.thuy.shopeeproject.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.thuy.shopeeproject.domain.dto.ProductFilterReqDTO;
import com.thuy.shopeeproject.domain.entity.Product;

import jakarta.persistence.criteria.Predicate;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    default Page<Product> findAll(ProductFilterReqDTO productFilterReqDTO,
            Pageable pageable) {
        return findAll((root, criteriaQuery, criteriaBuilder) -> {

            criteriaQuery.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            String keyword = productFilterReqDTO.getKeyWord();
            Integer categoryId = productFilterReqDTO.getCategoryId();
            String min = productFilterReqDTO.getMinPrice();
            String max = productFilterReqDTO.getMaxPrice();
            BigDecimal minPrice;
            BigDecimal maxPrice;

            if (min != null) {
                minPrice = BigDecimal.valueOf(Long.parseLong(productFilterReqDTO.getMinPrice()));
                Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(root.get("productDetails").get("price"),
                        minPrice);

                predicates.add(predicate);
            }

            if (max != null) {
                maxPrice = BigDecimal.valueOf(Long.parseLong(productFilterReqDTO.getMaxPrice()));
                Predicate predicate = criteriaBuilder.lessThanOrEqualTo(root.get("productDetails").get("price"),
                        maxPrice);

                predicates.add(predicate);
            }

            if (keyword != null) {
                Predicate predicateNameProduct = criteriaBuilder.like(root.get("productName"), '%'
                        + keyword + '%');
                Predicate predicateDescription = criteriaBuilder.like(root.get("description"), '%' + keyword + '%');
                Predicate predicateKw = criteriaBuilder.or(predicateNameProduct,
                        predicateDescription);
                predicates.add(predicateKw);
            }

            if (categoryId != null && categoryId != -1) {
                Predicate predicate = criteriaBuilder.equal(root.get("category").get("id"),
                        categoryId);
                predicates.add(predicate);
            }

            Predicate predicate = criteriaBuilder.equal(root.get("deleted"), false);
            predicates.add(predicate);

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, pageable);

    }
}
