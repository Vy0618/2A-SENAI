package com.viniciusAzevedo.petshop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viniciusAzevedo.petshop.Model.ItensPedido;

import java.util.List;
import java.util.Optional;

public interface ItensPedidoRepository extends JpaRepository<ItensPedido, Integer> {
    Optional<ItensPedido> findByIdPedidoFkAndIdProdutoFk(Integer idPedidoFk, Integer idProdutoFk);

    List<ItensPedido> findByIdPedidoFk(Integer idPedidoFk);
}
