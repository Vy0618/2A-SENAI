package com.pedrodetonhe.petshop.Controller;

import com.pedrodetonhe.petshop.DTO.CadastroRequest;
import com.pedrodetonhe.petshop.DTO.LoginRequest;
import com.pedrodetonhe.petshop.DTO.LoginResponse;
import com.pedrodetonhe.petshop.Model.Usuario;
import com.pedrodetonhe.petshop.Service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller de usuários — expõe os endpoints de autenticação e gerenciamento.
 *
 * Rotas públicas (sem token):
 *   POST /usuarios/cadastro  → registra novo usuário
 *   POST /usuarios/login     → autentica e retorna token JWT
 *
 * Rotas protegidas (requerem token JWT com role ADMIN):
 *   GET    /usuarios          → lista todos os usuários
 *   GET    /usuarios/{id}     → busca usuário por ID
 *   PUT    /usuarios/{id}     → atualiza usuário
 *   DELETE /usuarios/{id}     → remove usuário
 *
 * A anotação @PreAuthorize garante que apenas ADMIN acesse as rotas de gerenciamento.
 * Se um USER tentar acessar, o Spring retorna 403 Forbidden automaticamente.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // -------------------------------------------------------------------------
    // ROTAS PÚBLICAS — não precisam de token JWT
    // -------------------------------------------------------------------------

    /**
     * Cadastro de novo usuário.
     * Qualquer pessoa pode se cadastrar (rota pública).
     * Retorna 201 Created com o usuário criado.
     */
    @PostMapping("/cadastro")
    public ResponseEntity<Usuario> cadastrar(@RequestBody CadastroRequest request) {
        Usuario novo = service.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    /**
     * Login — autentica o usuário e retorna o token JWT.
     * O frontend deve salvar o token e enviá-lo no header de todas as próximas requisições:
     *   Authorization: Bearer <token>
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse resposta = service.login(request);
        return ResponseEntity.ok(resposta);
    }

    // -------------------------------------------------------------------------
    // ROTAS ADMINISTRATIVAS — exigem token JWT com role ADMIN
    // @PreAuthorize é processado ANTES de entrar no método
    // -------------------------------------------------------------------------

    /**
     * Lista todos os usuários do sistema.
     * Exclusivo para ADMIN.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // bloqueia USER e requisições sem token
    public List<Usuario> listarTodos() {
        return service.listarTodos();
    }

    /**
     * Busca um usuário específico por ID.
     * Exclusivo para ADMIN.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Usuario buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    /**
     * Atualiza dados de um usuário.
     * Exclusivo para ADMIN.
     * (Para USER editar os próprios dados, seria uma rota separada com validação de ownership)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Usuario atualizar(@PathVariable Integer id,
                             @RequestBody CadastroRequest request) {
        return service.atualizar(id, request);
    }

    /**
     * Remove um usuário do sistema.
     * Exclusivo para ADMIN.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build(); // retorna 204 No Content
    }
}
