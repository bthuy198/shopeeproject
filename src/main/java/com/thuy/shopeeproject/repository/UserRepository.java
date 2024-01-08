package com.thuy.shopeeproject.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.thuy.shopeeproject.domain.dto.product.ProductFilterReqDTO;
import com.thuy.shopeeproject.domain.dto.user.UserFilterReqDTO;
import com.thuy.shopeeproject.domain.entity.Product;
import com.thuy.shopeeproject.domain.entity.User;
import com.thuy.shopeeproject.domain.enums.ERole;

import jakarta.persistence.criteria.Predicate;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    default Page<User> findAllUser(UserFilterReqDTO userFilterReqDTO,
            Pageable pageable) {
        return findAll((root, criteriaQuery, criteriaBuilder) -> {

            criteriaQuery.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            String keyword = userFilterReqDTO.getKeyWord();
            String role = userFilterReqDTO.getRole().toUpperCase();

            if (keyword != null) {
                Predicate predicateNameUser = criteriaBuilder.like(root.get("username"), '%'
                        + keyword + '%');
                Predicate predicateNameUserInfo = criteriaBuilder.like(root.get("userInfo").get("name"),
                        '%' + keyword + '%');
                Predicate predicateKw = criteriaBuilder.or(predicateNameUser,
                        predicateNameUserInfo);
                predicates.add(predicateKw);
            }

            if (role != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("role"), ERole.getByValue(role.toLowerCase()));
                predicates.add(predicate);
            }

            Predicate predicate = criteriaBuilder.equal(root.get("deleted"), false);
            predicates.add(predicate);

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, pageable);

    }

}
