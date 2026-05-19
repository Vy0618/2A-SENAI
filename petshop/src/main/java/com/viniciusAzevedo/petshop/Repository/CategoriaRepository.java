package com.viniciusAzevedo.petshop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viniciusAzevedo.petshop.Model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
