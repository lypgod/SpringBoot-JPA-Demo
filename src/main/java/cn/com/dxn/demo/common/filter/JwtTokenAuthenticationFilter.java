package cn.com.dxn.demo.common.filter;

import cn.com.dxn.demo.common.security.JwtConfig;
import cn.com.dxn.demo.common.util.HttpUtil;
import io.jsonwebtoken.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author richard
 */
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final HttpUtil httpUtil;
    private final JwtConfig jwtConfig;
    public JwtTokenAuthenticationFilter(JwtConfig jwtConfig, HttpUtil httpUtil) {
        this.jwtConfig = jwtConfig;
        this.httpUtil = httpUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 1. get the authentication header. Tokens are supposed to be passed in the authentication header
        String header = request.getHeader(jwtConfig.getHeader());
        // 2. validate the header and check the prefix
        if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
            // If not valid, go to the next filter.
            chain.doFilter(request, response);
            return;
        }

        // If there is no token provided and hence the user won't be authenticated.
        // It's Ok. Maybe the user accessing a public path or asking for a token.

        // All secured paths that needs a token are already defined and secured in config class.
        // And If user tried to access without access token, then he won't be authenticated and an exception will be thrown.

        // 3. Get the token
        String token = header.replace(jwtConfig.getPrefix(), "");

        try {    // exceptions might be thrown in creating the claims if for example the token is expired

            // 4. Validate the token
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSecret().getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            if (username != null) {
                @SuppressWarnings("unchecked")
                List<String> authorities = (List<String>) claims.get("authorities");

                // 5. Create auth object
                // UsernamePasswordAuthenticationToken: A built-in object, used by spring to represent the current authenticated / being authenticated user.
                // It needs a list of authorities, which has type of GrantedAuthority interface, where SimpleGrantedAuthority is an implementation of that interface
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

                // 6. Authenticate the user
                // Now, user is authenticated
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (ExpiredJwtException e) {
            SecurityContextHolder.clearContext();
            this.httpUtil.writeExceptionResponse(response, HttpStatus.UNAUTHORIZED.value(), "登录已过期！");
            return;
        } catch (SignatureException | MalformedJwtException e) {
            SecurityContextHolder.clearContext();
            this.httpUtil.writeExceptionResponse(response, HttpStatus.UNAUTHORIZED.value(), "令牌错误！");
            return;
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            this.httpUtil.writeExceptionResponse(response, HttpStatus.UNAUTHORIZED.value(), "权限认证失败！");
            return;
        }

        // go to the next filter in the filter chain
        chain.doFilter(request, response);
    }

}