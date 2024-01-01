package com.thuy.shopeeproject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thuy.shopeeproject.domain.entity.PaymentMethod;
import com.thuy.shopeeproject.repository.PaymentMethodRepository;

@Service
@Transactional
public class PaymentMethodServiceImpl implements IPaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public List<PaymentMethod> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<PaymentMethod> findById(Long id) {
        return paymentMethodRepository.findById(id);
    }

    @Override
    public PaymentMethod save(PaymentMethod e) {
        return paymentMethodRepository.save(e);
    }

    @Override
    public void delete(PaymentMethod e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

}
