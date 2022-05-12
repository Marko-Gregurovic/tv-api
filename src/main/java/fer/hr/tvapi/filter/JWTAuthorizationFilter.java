package fer.hr.tvapi.filter;

import fer.hr.tvapi.entity.Users;
import fer.hr.tvapi.exception.NotFoundException;
import fer.hr.tvapi.service.UserService;
import fer.hr.tvapi.util.AuthenticatedUser;
import fer.hr.tvapi.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

    private final String jwtSecret;

    public JWTAuthorizationFilter
            (AuthenticationManager authenticationManager,
             UserDetailsService userDetailsService,
             UserService userService,
             String jwtSecret) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.jwtSecret = jwtSecret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String jwt = null;

        // Check if authorization header was passed and has the correct format
        if (JwtUtil.isHeaderValid(authorizationHeader)) {

            jwt = JwtUtil.extractTokenFromHeader(authorizationHeader);
            // Will throw InvalidSignature exception if jwt signature does not match jwtSecret
            try {
                email = JwtUtil.extractEmail(jwt, jwtSecret);
            } catch (Exception ex){
                String url = request.getRequestURL().toString();
                if(ex instanceof SignatureException){
                    LOGGER.info("User with invalid JWT signature attempted to access URL {}", url);
                } else if (ex instanceof MalformedJwtException){
                    LOGGER.info("User with malformed JWT signature attempted to access URL {}", url);
                }

                throw ex;
            }
        }

        // If token is valid attach user to context
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            AuthenticatedUser authenticatedUser = (AuthenticatedUser) userDetailsService.loadUserByUsername(email);

            boolean isTokenValid = JwtUtil.validateToken(jwt, authenticatedUser, jwtSecret);

            // If user does not exist user service will throw NotFoundException
            Users user;
            try {
                user = userService.findByEmail(email).get();
            } catch (NotFoundException ex) {
                LOGGER.info("Authorization attempt with valid JWT but non existing email address \"{}\"", email);
                throw ex;
            }
            String url = request.getRequestURL().toString();

            if(!isTokenValid){
                LOGGER.info("User with id {} attempted access to URL {}, with expired JWT token", user.getId(), url);
                throw new ExpiredJwtException(null, null, "Expired JWT");
            }

            LOGGER.info("User with id {} accessed URL: {}", user.getId(), url);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            authenticatedUser,
                            null,
                            authenticatedUser.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        chain.doFilter(request, response);
    }
}
