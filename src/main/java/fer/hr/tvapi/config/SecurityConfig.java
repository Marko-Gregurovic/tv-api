package fer.hr.tvapi.config;

import fer.hr.tvapi.exception.ExceptionHandlerFilter;
import fer.hr.tvapi.filter.CorsCustomFilter;
import fer.hr.tvapi.filter.JWTAuthenticationFilter;
import fer.hr.tvapi.filter.JWTAuthorizationFilter;
import fer.hr.tvapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final CorsCustomFilter corsCustomFilter;

    private final UserDetailsService userDetailsService;
    private final String jwtSecret;

    @Autowired
    public SecurityConfig(
            UserService userService, CorsCustomFilter corsCustomFilter, UserDetailsService userDetailsService, @Value("${security.jwtSecret}") String jwtSecret) {
      this.userService = userService;
        this.corsCustomFilter = corsCustomFilter;
        this.userDetailsService = userDetailsService;
        this.jwtSecret = jwtSecret;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .addFilter(new JWTAuthenticationFilter(authenticationManager(), userService, jwtSecret))
            .addFilter(new JWTAuthorizationFilter(authenticationManager(), userDetailsService, userService, jwtSecret))
            .addFilterBefore(new ExceptionHandlerFilter(), JWTAuthenticationFilter.class)
            .addFilterBefore(corsCustomFilter, JWTAuthenticationFilter.class)
            .authorizeRequests()
            .antMatchers("/register", "/login").permitAll()
            .antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                "/actuator/health"
            ).permitAll()
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
