package com.ibs.fixmtoacris.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Value("${APP_USERNAME:admin}")
    private String appAdminUsername;

    @Value("${APP_PASSWORD:adminpw}")
    private String appAdminPassword;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {

            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Long userId = tokenProvider.getUserIdFromJWT(jwt);

                /*
                 * Note that you could also encode the user's username and roles inside JWT
                 * claims and create the UserDetails object by parsing those claims from the
                 * JWT. That would avoid the following database hit. It's completely up to you.
                 */
                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else {

                String authorizationCredentials = getBasicRequest(request);

                if (StringUtils.hasText(authorizationCredentials)) {

                    String[] decodedCredentials = new String(Base64.getDecoder().decode(authorizationCredentials))
                            .split(":");
                    System.out.println((decodedCredentials[0].trim().equalsIgnoreCase("admin")
                            && decodedCredentials[1].trim().equalsIgnoreCase("adminpw"))
                            + "$$$$===================================================inside3============"
                            + decodedCredentials[0] + "--" + decodedCredentials[1]);
                

                    // verifying if the credentials received are valid
                    if (decodedCredentials[0].trim().equalsIgnoreCase(appAdminUsername)
                            && decodedCredentials[1].trim().equalsIgnoreCase(appAdminPassword)) {
                        // user retrieving logic
                        System.out.println("====inside========");

                        UserDetails userDetails = customUserDetailsService
                                .loadUserByUsername(decodedCredentials[0].trim());
                                log.debug("User details %s", userDetails.getUsername());
                        if (userDetails == null)
                            throw new AuthenticationException("Invalid user");

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);

                    } else {

                        throw new AuthenticationException("Invalid user");

                    }

                }

            }

        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    private String getBasicRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Basic ")) {
            return bearerToken.substring(6, bearerToken.length());
        }
        return null;
    }

}
