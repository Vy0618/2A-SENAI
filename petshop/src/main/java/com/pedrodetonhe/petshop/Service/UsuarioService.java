package com.pedrodetonhe.petshop.Service;

import com.pedrodetonhe.petshop.DTO.CadastroRequest;
import com.pedrodetonhe.petshop.DTO.LoginRequest;
import com.pedrodetonhe.petshop.DTO.LoginResponse;
import com.pedrodetonhe.petshop.Model.Usuario;
import com.pedrodetonhe.petshop.Repository.UsuarioRepository;
import com.pedrodetonhe.petshop.Security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service responsável pela lógica de negócio dos usuários:
 * - Cadastro com senha hasheada
 * - Login com geração de token JWT
 * - CRUD de usuários (apenas ADMIN pode chamar estas operações)
 */
@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder; // BCrypt, configurado no SecurityConfig
    private final JwtUtil jwtUtil;

    public UsuarioService(UsuarioRepository repository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.repository      = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil         = jwtUtil;
    }

    // -------------------------------------------------------------------------
    // CADASTRO
    // -------------------------------------------------------------------------

    /**
     * Registra um novo usuário no sistema.
     *
     * Passos:
     * 1. Verifica se o e-mail já está em uso (e-mail é o identificador de login)
     * 2. Hasheia a senha com BCrypt antes de salvar
     * 3. Persiste o usuário no banco
     *
     * Retorna o usuário salvo (sem a senha — o frontend não precisa dela).
     */
    public Usuario cadastrar(CadastroRequest request) {

        // 1. Impede cadastro com e-mail duplicado
        if (repository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }

        // 2. Cria o objeto usuário e hasheia a senha
        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha())); // NUNCA salva a senha pura
        usuario.setRole(request.getRole() != null ? request.getRole() : Usuario.Role.USER); // padrão: USER

        // 3. Salva e retorna
        return repository.save(usuario);
    }

    // -------------------------------------------------------------------------
    // LOGIN
    // -------------------------------------------------------------------------

    /**
     * Autentica o usuário e retorna um token JWT.
     *
     * Passos:
     * 1. Busca o usuário pelo e-mail
     * 2. Compara a senha enviada com o hash armazenado no banco
     * 3. Gera e retorna o token JWT com o perfil embutido
     *
     * O token gerado será usado em todas as próximas requisições no header:
     *   Authorization: Bearer <token>
     */
    public LoginResponse login(LoginRequest request) {

        // 1. Busca o usuário — erro 401 se não encontrado (não informamos "e-mail não existe"
        //    por segurança, para não facilitar enumeração de e-mails)
        Usuario usuario = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));

        // 2. Compara a senha em texto puro com o hash BCrypt do banco
        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }

        // 3. Gera o token JWT com e-mail e role embutidos
        String token = jwtUtil.gerarToken(usuario.getEmail(), usuario.getRole().name());

        // 4. Retorna o token + dados básicos para o frontend configurar a sessão
        return new LoginResponse(token, usuario.getRole().name(), usuario.getNome(), usuario.getId());
    }

    // -------------------------------------------------------------------------
    // CRUD DE USUÁRIOS (apenas ADMIN)
    // A proteção de quem pode chamar estes métodos está no Controller + SecurityConfig
    // -------------------------------------------------------------------------

    /** Lista todos os usuários cadastrados */
    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    /** Busca um usuário por ID — erro 404 se não encontrado */
    public Usuario buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    /**
     * Atualiza dados de um usuário existente.
     * Se uma nova senha for enviada, ela é hasheada antes de salvar.
     */
    public Usuario atualizar(Integer id, CadastroRequest request) {

        Usuario usuario = buscarPorId(id);

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());

        // Só atualiza a senha se uma nova for enviada
        if (request.getSenha() != null && !request.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        }

        if (request.getRole() != null) {
            usuario.setRole(request.getRole());
        }

        return repository.save(usuario);
    }

    /** Remove um usuário pelo ID */
    public void deletar(Integer id) {
        buscarPorId(id); // valida existência antes de deletar
        repository.deleteById(id);
    }
}
