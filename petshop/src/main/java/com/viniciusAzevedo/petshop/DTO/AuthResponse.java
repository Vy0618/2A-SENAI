package com.viniciusAzevedo.petshop.DTO;

// DTO de resposta após login/cadastro
public class AuthResponse {
    private Integer idUsuario;
    private String token;
    private String nome;
    private String email;
    private String role;

    public AuthResponse(Integer idUsuario, String token, String nome, String email, String role) {
        this.idUsuario = idUsuario;
        this.token = token;
        this.nome = nome;
        this.email = email;
        this.role = role;
    }

    public Integer getIdUsuario() { return idUsuario; }
    public String getToken() { return token; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}
