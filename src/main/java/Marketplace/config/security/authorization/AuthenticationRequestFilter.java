package Marketplace.config.security.authorization;

import Marketplace.config.security.token.AccessToken;
import Marketplace.config.security.token.AccessTokenDecoder;
import Marketplace.config.security.token.exception.InvalidAccessTokenException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
    public class AuthenticationRequestFilter extends OncePerRequestFilter {

    private static final String SPRING_SECURITY_ROLE_PREFIX = "ROLE_";

    @Autowired
    private AccessTokenDecoder accessTokenDecoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");
        if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessTokenString = requestTokenHeader.substring(7);

        try{
            AccessToken accessToken = accessTokenDecoder.decode(accessTokenString);

            setupSpringSecurityContext(accessToken);
            filterChain.doFilter(request, response);
        } catch (InvalidAccessTokenException e) {
            logger.error("Invalid access token: " + e.getMessage());
            sendAuthenticationError(response);
        }
    }

    private void sendAuthenticationError(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.flushBuffer();
    }

    private void setupSpringSecurityContext(AccessToken accessToken) {
        String role = accessToken.getRole();

        UserDetails userDetails = new User(accessToken.getSubject(), "",
                Collections.singletonList(new SimpleGrantedAuthority(SPRING_SECURITY_ROLE_PREFIX + role)));

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(accessToken);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
