package com.thuy.shopeeproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.thuy.shopeeproject.domain.entity.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {
    @Query(value = "SELECT * FROM size WHERE size.size = :sizeStr", nativeQuery = true)
    Size findSizeBySize(@Param("sizeStr") String size);
}
