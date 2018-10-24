package cn.com.dxn.demo.common.config;

import cn.com.dxn.demo.common.filter.JwtTokenAuthenticationFilter;
import cn.com.dxn.demo.common.filter.JwtUsernameAndPasswordAuthenticationFilter;
import cn.com.dxn.demo.common.security.JwtConfig;
import cn.com.dxn.demo.common.util.HttpUtil;
import cn.com.dxn.demo.model.repository.UserRepository;
import cn.com.dxn.demo.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

/**
 * @author richard
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityCredentialsConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AuthService authService;
    @Resource
    private JwtConfig jwtConfig;
    @Resource
    private HttpUtil httpUtil;
    @Resource
    private UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                // make sure we use stateless session; session won't be used to store user's state.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // handle an authorized attempts
                .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                // Add a filter to validate user credentials and add token in the response header

                // What's the authenticationManager()?
                // An object provided by WebSecurityConfigurerAdapter, used to authenticate the user passing user's credentials
                // The filter needs this auth manager to authenticate the user.
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, httpUtil, userRepository))
                // Add a filter to validate the tokens with every request
                .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig, httpUtil), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                // allow all POST requests
                .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
                // must be an admin if trying to access admin area (authentication is also required here)
                .antMatchers("/users/**", "/roles/**").hasAnyRole("USER", "ADMIN")
                // any other requests must be authenticated
                .anyRequest().authenticated();
    }

    /**
     * Spring has UserDetailsService interface, which can be overriden to provide our implementation for fetching user from database (or any other source).
     * The UserDetailsService object is used by the auth manager to load the user from database.
     * In addition, we need to define the password encoder also. So, auth manager can compare and verify passwords.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.authService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}