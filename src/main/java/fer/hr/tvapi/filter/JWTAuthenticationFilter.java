package fer.hr.tvapi.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import fer.hr.tvapi.dto.AuthenticationRequest;
import fer.hr.tvapi.entity.Users;
import fer.hr.tvapi.exception.NotFoundException;
import fer.hr.tvapi.service.UserService;
import fer.hr.tvapi.util.AuthenticatedUser;
import fer.hr.tvapi.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private final String jwtSecret;

    public JWTAuthenticationFilter
            (AuthenticationManager authenticationManager,
             UserService userService,
             String jwtSecret) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtSecret = jwtSecret;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        AuthenticationRequest authenticationRequest;
        try {
            authenticationRequest = new ObjectMapper().readValue(request.getInputStream(), AuthenticationRequest.class);
        } catch (IOException ex) {
            LOGGER.info("Login attempt with data which could not be parsed to AuthenticationRequest");
            throw new BadCredentialsException("Data sent for log in could not be parsed");
        }

        String email = authenticationRequest.getEmail();
        String password = authenticationRequest.getPassword();

        if (email == null || password == null) {
            LOGGER.info("Login attempt with incomplete data");
            throw new BadCredentialsException("Username and password must not be null.");
        }

        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (Exception ex) {
            if (!(ex instanceof NotFoundException) &&
                    !(ex instanceof AuthenticationException)) {
                throw ex;
            }

            if (ex instanceof InternalAuthenticationServiceException) {
                LOGGER.info("Login attempt with unregistered email {}", authenticationRequest.getEmail());
            } else {
                Users user = userService.findByEmail(email).get();
                // Email is logged because user can change email
                LOGGER.info("Failed login attempt for user with id {} using email {}",
                        user.getId(), authenticationRequest.getEmail());
            }

            throw new BadCredentialsException("Invalid username or password");
        }

    }

    @Override
    protected void successfulAuthentication
            (HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) auth.getPrincipal();

        String jwt = JwtUtil.generateToken(authenticatedUser, jwtSecret);

        LOGGER.info("Log in success for user with id {}", authenticatedUser.getId());

        response.addHeader("Authorization", "Bearer " + jwt);
    }

}
