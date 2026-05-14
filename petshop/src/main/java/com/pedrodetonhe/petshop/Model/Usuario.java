package com.pedrodetonhe.petshop.Model;

import jakarta.persistence.*;

/**
 * Entidade que representa um usuário do sistema.
 * Possui dois perfis: ADMIN (acesso total) e USER (acesso limitado à área de compras).
 */
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Nome completo do usuário */
    @Column(nullable = false)
    private String nome;

    /** E-mail usado como login — deve ser único */
    @Column(nullable = false, unique = true)
    private String email;

    /** Senha armazenada como hash BCrypt (nunca em texto puro) */
    @Column(nullable = false)
    private String senha;

    /**
     * Perfil de acesso: ADMIN ou USER.
     * - ADMIN: acesso total (CRUD de usuários, categorias e produtos)
     * - USER: apenas visualizar produtos, carrinho e seus próprios dados
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /** Enum interno que define os perfis possíveis */
    public enum Role {
        ADMIN,
        USER
    }

    // ---- Construtores ----

    public Usuario() {}

    public Usuario(String nome, String email, String senha, Role role) {
        this.nome  = nome;
        this.email = email;
        this.senha = senha;
        this.role  = role;
    }

    // ---- Getters e Setters ----

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
