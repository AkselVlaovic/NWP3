package rs.raf.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.Authority;
import rs.raf.demo.services.UserService;

import java.util.*;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "MY JWT SECRET";
    private UserService userService;

    public JwtUtil(UserService userService) {
        this.userService = userService;
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public String generateToken(String email){

        Map<String, Object> claims = new HashMap<>();

        UserDetails userDetails = userService.loadUserByUsername(email);

        Collection<? extends GrantedAuthority> grantedAuthorities = userDetails.getAuthorities();

        for(GrantedAuthority authority : grantedAuthorities)
            claims.put(authority.getAuthority(),true);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    public boolean validateToken(String token, UserDetails user) {
        return (user.getUsername().equals(extractEmail(token)) && !isTokenExpired(token));
    }
}
