package com.pedrodetonhe.petshop.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro JWT — executado UMA VEZ a cada requisição HTTP recebida.
 *
 * Responsabilidade: interceptar requisições, extrair o token JWT do header,
 * validá-lo e registrar o usuário autenticado no contexto de segurança do Spring.
 *
 * Fluxo de cada requisição:
 * 1. Extrai o header "Authorization"
 * 2. Verifica se começa com "Bearer "
 * 3. Extrai e valida o token JWT
 * 4. Lê o e-mail e o perfil (role) do token
 * 5. Registra o usuário no SecurityContext — o Spring usa isso para autorizar rotas
 * 6. Passa a requisição adiante na cadeia de filtros
 *
 * Se o token for inválido ou ausente, a requisição continua sem autenticação —
 * e o Spring bloqueará automaticamente o acesso às rotas protegidas.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Lê o header "Authorization" da requisição
        String authHeader = request.getHeader("Authorization");

        // 2. Só processa se o header existir e começar com "Bearer "
        //    Requisições sem token (login, cadastro) passam direto para o próximo filtro
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            // 3. Remove o prefixo "Bearer " para obter apenas o token
            String token = authHeader.substring(7);

            // 4. Valida o token (assinatura + expiração)
            if (jwtUtil.tokenValido(token)) {

                // 5. Extrai os dados do token
                String email = jwtUtil.extrairEmail(token);
                String role  = jwtUtil.extrairRole(token);

                // 6. Cria a "authority" do Spring Security com o prefixo ROLE_
                //    Ex: role="ADMIN" → authority="ROLE_ADMIN"
                //    Isso é necessário para que @PreAuthorize("hasRole('ADMIN')") funcione
                List<SimpleGrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority("ROLE_" + role));

                // 7. Cria o objeto de autenticação do Spring Security
                //    (sem precisar de senha, pois o token já prova a identidade)
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                // 8. Registra o usuário como autenticado no contexto de segurança
                //    A partir daqui o Spring sabe quem está fazendo a requisição
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            // Se o token for inválido, simplesmente não autentica — o Spring bloqueará rotas protegidas
        }

        // 9. Continua para o próximo filtro (ou para o controller, se for o último)
        filterChain.doFilter(request, response);
    }
}
