package com.thuy.shopeeproject.service;

import java.util.List;
import java.util.Optional;

public interface IService<E, T> {
    List<E> findAll();

    Optional<E> findById(T id);

    E save(E e);

    void delete(E e);

    void deleteById(T id);
}
