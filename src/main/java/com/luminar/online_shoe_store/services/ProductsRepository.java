package com.luminar.online_shoe_store.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luminar.online_shoe_store.models.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer> {

}
