package com.pedrodetonhe.petshop.DTO;

import com.pedrodetonhe.petshop.Model.Usuario;

/**
 * DTO para a requisição de CADASTRO de novo usuário.
 *
 * Separa o que vem da requisição HTTP da entidade do banco,
 * permitindo validar e transformar os dados antes de persistir.
 */
public class CadastroRequest {

    private String nome;
    private String email;

    /** Senha em texto puro — será hasheada pelo service antes de salvar */
    private String senha;

    /**
     * Perfil desejado: ADMIN ou USER.
     * Em produção, você pode remover este campo e forçar sempre USER,
     * permitindo que apenas um ADMIN promoção outros usuários a ADMIN.
     */
    private Usuario.Role role;

    public CadastroRequest() {}

    public String getNome()       { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail()      { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha()      { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public Usuario.Role getRole() { return role; }
    public void setRole(Usuario.Role role) { this.role = role; }
}
