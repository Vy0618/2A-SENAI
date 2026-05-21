package com.viniciusAzevedo.petshop.Service;

import org.springframework.stereotype.Service;

import com.viniciusAzevedo.petshop.Model.ItensPedido;
import com.viniciusAzevedo.petshop.Model.Produto;
import com.viniciusAzevedo.petshop.Repository.ItensPedidoRepository;
import com.viniciusAzevedo.petshop.Repository.ProdutoRepository;

import java.util.List;

@Service
public class ItensPedidoService {

    private final ItensPedidoRepository itensPedidoRepository;
    private final ProdutoRepository produtoRepository;

    public ItensPedidoService(ItensPedidoRepository itensPedidoRepository, ProdutoRepository produtoRepository) {
        this.itensPedidoRepository = itensPedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    public void adicionarOuAtualizarItem(Integer idPedido, Integer idProduto, Integer quantidade) {
        Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado"));

        ItensPedido item = itensPedidoRepository.findByIdPedidoFkAndIdProdutoFk(idPedido, idProduto)
                .orElseGet(() -> {
                    ItensPedido novoItem = new ItensPedido();
                    novoItem.setIdPedidoFk(idPedido);
                    novoItem.setIdProdutoFk(idProduto);
                    novoItem.setNomeProduto(produto.getNome());
                    novoItem.setImagemProduto(produto.getImagem());
                    novoItem.setQuantidade(0);
                    novoItem.setPrecoUnitario(produto.getPreco_desconto());
                    return novoItem;
                });

        item.setQuantidade(item.getQuantidade() + quantidade);
        itensPedidoRepository.save(item);
    }

    public void alterarQuantidade(Integer idItemPedido, Integer quantidade) {
        if (quantidade <= 0) {
            remover(idItemPedido);
            return;
        }

        ItensPedido item = itensPedidoRepository.findById(idItemPedido)
                .orElseThrow(() -> new RuntimeException("Item nao encontrado"));
        item.setQuantidade(quantidade);
        itensPedidoRepository.save(item);
    }

    public List<ItensPedido> listarPorPedido(Integer idPedido) {
        return itensPedidoRepository.findByIdPedidoFk(idPedido);
    }

    public void remover(Integer idItemPedido) {
        itensPedidoRepository.deleteById(idItemPedido);
    }

    public void limparItensDoPedido(Integer idPedido) {
        itensPedidoRepository.deleteAll(itensPedidoRepository.findByIdPedidoFk(idPedido));
    }
}
