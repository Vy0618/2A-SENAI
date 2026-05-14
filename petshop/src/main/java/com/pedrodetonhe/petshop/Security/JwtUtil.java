package com.pedrodetonhe.petshop.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utilitário responsável por GERAR e VALIDAR tokens JWT.
 *
 * Como funciona o JWT no fluxo de autenticação:
 * 1. Usuário faz login com e-mail + senha
 * 2. Backend valida as credenciais e gera um token JWT assinado
 * 3. O token é enviado ao frontend na resposta do login
 * 4. O frontend armazena o token (ex: localStorage) e o envia em toda requisição no header:
 *      Authorization: Bearer <token>
 * 5. O backend intercepta cada requisição, extrai e valida o token,
 *    e libera ou bloqueia o acesso com base no perfil (ADMIN/USER)
 */
@Component
public class JwtUtil {

    /**
     * Chave secreta usada para assinar os tokens.
     * Em produção, mova isso para application.properties ou variável de ambiente.
     * NUNCA exponha essa chave publicamente.
     */
    private static final String SECRET = "petshop-chave-super-secreta-deve-ter-256bits!!";

    /** Tempo de expiração do token: 24 horas em milissegundos */
    private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000L;

    /** Gera a chave criptográfica a partir da string secreta */
    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * Gera um token JWT contendo:
     * - subject: e-mail do usuário (identificador principal)
     * - claim "role": perfil do usuário (ADMIN ou USER)
     * - data de emissão e expiração
     * - assinatura HMAC-SHA256
     *
     * @param email e-mail do usuário autenticado
     * @param role  perfil do usuário (ADMIN ou USER)
     * @return token JWT como String
     */
    public String gerarToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)                          // quem é o dono do token
                .claim("role", role)                        // dado extra: perfil de acesso
                .setIssuedAt(new Date())                    // quando foi gerado
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS)) // quando expira
                .signWith(getKey(), SignatureAlgorithm.HS256) // algoritmo de assinatura
                .compact();                                 // monta e retorna o token
    }

    /**
     * Extrai o e-mail (subject) de dentro do token.
     * Usado pelo filtro de autenticação para identificar o usuário da requisição.
     */
    public String extrairEmail(String token) {
        return parsearToken(token).getBody().getSubject();
    }

    /**
     * Extrai o perfil (role) de dentro do token.
     * Usado para verificar se o usuário tem permissão para a rota acessada.
     */
    public String extrairRole(String token) {
        return (String) parsearToken(token).getBody().get("role");
    }

    /**
     * Valida se o token é autêntico e ainda não expirou.
     * Retorna false se o token for inválido, adulterado ou expirado.
     */
    public boolean tokenValido(String token) {
        try {
            parsearToken(token); // lança exceção se inválido
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // token inválido ou expirado
        }
    }

    /**
     * Faz o parse/decodificação do token JWT.
     * Verifica a assinatura e retorna os claims (dados internos do token).
     */
    private Jws<Claims> parsearToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token);
    }
}
