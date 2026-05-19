package com.viniciusAzevedo.petshop.Controller;

import org.springframework.web.bind.annotation.*;

import com.viniciusAzevedo.petshop.Model.Produto;
import com.viniciusAzevedo.petshop.Service.ProdutoService;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@CrossOrigin("*")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // GET TODOS
    @GetMapping
    public List<Produto> listar() {
        return produtoService.listarTodos();
    }

    // GET POR ID
    @GetMapping("/{id}")
    public Produto buscar(@PathVariable Integer id) {
        return produtoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    // POST
    @PostMapping("/{idCategoria}")
    public Produto criar(@RequestBody Produto produto,
                         @PathVariable Integer idCategoria) {
        return produtoService.salvar(produto, idCategoria);
    }

    // PUT
    @PutMapping("/{id}/{idCategoria}")
    public Produto atualizar(@PathVariable Integer id,
                             @RequestBody Produto produto,
                             @PathVariable Integer idCategoria) {
        return produtoService.atualizar(id, produto, idCategoria);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        produtoService.deletar(id);
    }
}