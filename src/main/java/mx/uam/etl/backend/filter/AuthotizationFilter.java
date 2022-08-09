package mx.uam.etl.backend.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
public class AuthotizationFilter extends OncePerRequestFilter {


    private static final String TOKEN_PREFIX = "Bearer";
    private String SECRET = "secret";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/api/login")){
            filterChain.doFilter(request,response);
        }
        else{
            String authorizationHeader = request.getHeader(AUTHORIZATION);


            if(authorizationHeader != null  && authorizationHeader.startsWith(AUTHORIZATION)){
                try {
                    String token = authorizationHeader.substring(TOKEN_PREFIX.length());
                    //Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                    JWTVerifier  verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String roles[] = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    Arrays.stream(roles).forEach( rol ->{
                        authorities.add(new SimpleGrantedAuthority(rol));
                    });
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username,null,authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                }catch (Exception e){
                    log.error("Error  loggin in: {}", e.getMessage());
                    response.setHeader("ERROR", e.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    Map<String,String> error =  new HashMap<>();
                    error.put("ERROR_MESSAGE",e.getMessage());
                    response.setContentType("application/json");
                    new ObjectMapper().writeValue(response.getOutputStream(),error);
                }
            }else {
                filterChain.doFilter(request,response);
            }

        }

    }
}
