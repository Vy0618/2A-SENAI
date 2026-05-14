package com.pedrodetonhe.petshop.DTO;

/**
 * DTO (Data Transfer Object) para a requisição de LOGIN.
 *
 * Recebe apenas e-mail e senha — nunca expõe dados internos do banco.
 * O DTO evita que o Controller receba a entidade Usuario diretamente,
 * o que poderia expor campos sensíveis ou permitir alterações indevidas.
 */
public class LoginRequest {

    /** E-mail cadastrado no sistema (usado como identificador de login) */
    private String email;

    /** Senha em texto puro — será comparada com o hash BCrypt no banco */
    private String senha;

    public LoginRequest() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
