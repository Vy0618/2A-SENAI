package com.pedrodetonhe.petshop.Repository;

import com.pedrodetonhe.petshop.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório JPA para a entidade Usuario.
 * O Spring Data gera automaticamente as queries a partir dos nomes dos métodos.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Busca um usuário pelo e-mail.
     * Usado no login e para validar e-mails duplicados no cadastro.
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica se já existe um usuário com o e-mail informado.
     * Útil para dar mensagem de erro amigável antes de tentar salvar.
     */
    boolean existsByEmail(String email);
}
