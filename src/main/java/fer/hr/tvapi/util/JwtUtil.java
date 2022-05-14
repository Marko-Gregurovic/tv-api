package fer.hr.tvapi.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class JwtUtil {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final long EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 30; // one month in miliseconds

    public static String extractEmail(String token, String secretKey) {
        return extractClaim(token, Claims::getSubject, secretKey);
    }

    public static Date extractExpiration(String token, String secretKey) {
        return extractClaim(token, Claims::getExpiration, secretKey);
    }

    /**
     * @param token          base64 encoded jwt token
     * @param claimsResolver function object which returns the wanted property(claim) to extract
     * @param secretKey      secret key
     * @param <T>            class type of the wanted property(claim)
     * @return extracted property instance
     */
    private static <T> T extractClaim(String token, Function<Claims, T> claimsResolver, String secretKey) {
        final Claims claims = extractAllClaims(token, secretKey);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private static Boolean isTokenExpired(String token, String secretKey) {
        return extractExpiration(token, secretKey).before(new Date());
    }

    public static String generateToken(AuthenticatedUser authenticatedUser, String secretKey) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", authenticatedUser.getAuthorities());
        return createToken(claims, authenticatedUser.getUsername(), secretKey); // username is actually email
    }

    /**
     * @param claims    map describing properties of the user (e.g. roles, id)
     * @param subject   user email (must be email because it's used for authentication)
     * @param secretKey secret key
     */
    private static String createToken(Map<String, Object> claims, String subject, String secretKey) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // email
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public static boolean validateToken(String token, AuthenticatedUser authenticatedUser, String secretKey) {
        String email = extractEmail(token, secretKey);
        return !isTokenExpired(token, secretKey)
                && email.equals(authenticatedUser.getUsername());
        // TODO - extract and validate roles
    }

    public static boolean isHeaderValid(String header) {
        return header != null && header.startsWith(TOKEN_PREFIX) && header.length() > TOKEN_PREFIX.length();
    }

    public static String extractTokenFromHeader(String header) {
        return header.substring(TOKEN_PREFIX.length());
    }

}

