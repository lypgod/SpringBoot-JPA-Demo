package cn.com.dxn.demo.common.filter;

import cn.com.dxn.demo.common.security.JwtAuthenticationResponse;
import cn.com.dxn.demo.common.security.JwtConfig;
import cn.com.dxn.demo.common.util.HttpUtil;
import cn.com.dxn.demo.model.entity.User;
import cn.com.dxn.demo.model.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author richard
 */
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /**
     * We use auth manager to validate the user credentials
     */
    private AuthenticationManager authManager;
    private final HttpUtil httpUtil;
    private final JwtConfig jwtConfig;
    private UserRepository userRepository;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager, JwtConfig jwtConfig, HttpUtil httpUtil, UserRepository userRepository) {
        this.authManager = authManager;
        this.jwtConfig = jwtConfig;
        this.httpUtil = httpUtil;
        this.userRepository = userRepository;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getLoginUrl(), "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {

            // 1. Get credentials from request
            UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);

            // 2. Create auth object (contains credentials) which will be used by auth manager
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    creds.getUsername(), creds.getPassword(), Collections.emptyList());

            // 3. Authentication manager authenticate the user, and use UserDetialsServiceImpl::loadUserByUsername() method to load the user.

            return authManager.authenticate(authToken);
        } catch (BadCredentialsException e) {
            try {
                this.httpUtil.writeExceptionResponse(response, HttpStatus.UNAUTHORIZED.value(), "用户名密码错误！");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Upon successful authentication, generate a token.
     * The 'auth' passed to successfulAuthentication() is the current authenticated user.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException {

        Long now = System.currentTimeMillis();
        String token = Jwts.builder()
                .setSubject(auth.getName())
                // Convert to list of strings.
                // This is important because it affects the way we get them back in the Gateway.
                .claim("authorities", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                .compact();

        final User user = userRepository.findByUsername(auth.getName());
        user.setPassword(null);
        this.httpUtil.writeSuccessResponse(response, new JwtAuthenticationResponse(user, token));
    }

    /**
     * A (temporary) class just to represent the user credentials
     */
    @Data
    private static class UserCredentials {
        private String username, password;
    }
}