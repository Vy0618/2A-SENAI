package com.pedrodetonhe.petshop.DTO;

/**
 * DTO para a RESPOSTA do login bem-sucedido.
 *
 * Retorna ao frontend:
 * - O token JWT (que será enviado em todas as próximas requisições)
 * - O perfil do usuário (ADMIN ou USER), para o frontend decidir quais menus exibir
 * - O nome do usuário, para exibição na interface
 *
 * NUNCA retorna a senha, mesmo que hasheada.
 */
public class LoginResponse {

    /** Token JWT gerado após autenticação — o frontend deve armazená-lo e enviá-lo no header Authorization */
    private String token;

    /** Perfil do usuário autenticado: "ADMIN" ou "USER" */
    private String role;

    /** Nome do usuário para exibição na interface */
    private String nome;

    /** ID do usuário, útil para o frontend buscar/editar dados do próprio usuário */
    private Integer id;

    public LoginResponse(String token, String role, String nome, Integer id) {
        this.token = token;
        this.role  = role;
        this.nome  = nome;
        this.id    = id;
    }

    public String getToken() { return token; }
    public String getRole()  { return role; }
    public String getNome()  { return nome; }
    public Integer getId()   { return id; }
}
