package com.thuy.shopeeproject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thuy.shopeeproject.domain.entity.Size;
import com.thuy.shopeeproject.repository.SizeRepository;

@Service
@Transactional
public class SizeServiceImpl implements ISizeService {
    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public List<Size> findAll() {
        return sizeRepository.findAll();
    }

    @Override
    public Optional<Size> findById(Long id) {
        return sizeRepository.findById(id);
    }

    @Override
    public Size save(Size e) {
        return sizeRepository.save(e);
    }

    @Override
    public void delete(Size e) {
        sizeRepository.delete(e);
    }

    @Override
    public void deleteById(Long id) {
        sizeRepository.deleteById(id);
    }

    @Override
    public Size findSizeBySize(String size) {
        return sizeRepository.findSizeBySize(size);
    }

}
