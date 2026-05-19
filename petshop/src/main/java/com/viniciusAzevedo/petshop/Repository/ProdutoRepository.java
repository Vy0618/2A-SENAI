package com.viniciusAzevedo.petshop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viniciusAzevedo.petshop.Model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
}