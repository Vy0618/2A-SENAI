package com.viniciusAzevedo.petshop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viniciusAzevedo.petshop.Model.Pedido;

import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    Optional<Pedido> findByIdUsuarioFkAndStatus(Integer idUsuarioFk, String status);
}
