package com.viniciusAzevedo.petshop.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.viniciusAzevedo.petshop.DTO.AuthResponse;
import com.viniciusAzevedo.petshop.DTO.LoginRequest;
import com.viniciusAzevedo.petshop.DTO.RegisterRequest;
import com.viniciusAzevedo.petshop.Service.UsuarioService;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // POST /auth/cadastro — cadastro público, sempre cria USER
    @PostMapping("/cadastro")
    public ResponseEntity<AuthResponse> cadastrar(@RequestBody RegisterRequest request) {
        AuthResponse response = usuarioService.cadastrar(request);
        return ResponseEntity.ok(response);
    }

    // POST /auth/cadastro/admin — apenas ADMIN autenticado pode criar outro ADMIN
    @PostMapping("/cadastro/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> cadastrarAdmin(@RequestBody RegisterRequest request) {
        AuthResponse response = usuarioService.cadastrarAdmin(request);
        return ResponseEntity.ok(response);
    }

    // POST /auth/login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = usuarioService.login(request);
        return ResponseEntity.ok(response);
    }
}