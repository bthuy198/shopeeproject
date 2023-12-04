package com.thuy.shopeeproject.domain.entity;

import java.util.List;

import jakarta.persistence.*;

import com.thuy.shopeeproject.domain.dto.SizeDTO;
import com.thuy.shopeeproject.domain.enums.ESize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "size")
public class Size extends BaseEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Enumerated(EnumType.STRING)
    @Column(length = 20, name = "size")
	private ESize size;
	
	@OneToMany(mappedBy = "size")
	private List<ProductDetail> productDetails;
	
	public SizeDTO toSizeDTO(){
        return new SizeDTO()
        .setId(id)
        .setSize(size.getValue());
    }

}
