package com.kalsym.chatbot.flowbuilder.filter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalsym.chatbot.flowbuilder.VersionHolder;
import com.kalsym.chatbot.flowbuilder.security.model.Auth;
import com.kalsym.chatbot.flowbuilder.models.HttpReponse;
import com.kalsym.chatbot.flowbuilder.security.model.MySQLUserDetails;
import com.kalsym.chatbot.flowbuilder.service.MySQLUserDetailsService;
import com.kalsym.chatbot.flowbuilder.utils.DateTimeUtil;
import com.kalsym.chatbot.flowbuilder.utils.Logger;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author Sarosh
 */
@Component
public class SessionRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MySQLUserDetailsService jwtUserDetailsService;

    @Autowired
    RestTemplate restTemplate;

    @Value("${services.user-service.session_details:not-known}")
    String userServiceSessionDetailsUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String logprefix = request.getRequestURI() + " ";

        Logger.application.info(Logger.pattern, VersionHolder.VERSION, "-------------" + logprefix + "-------------", "", "");

        final String authHeader = request.getHeader("Authorization");
        Logger.application.warn(Logger.pattern, VersionHolder.VERSION, logprefix, "Authorization: " + authHeader, "");

        String accessToken = null;

        // Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (null != authHeader && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.replace("Bearer ", "");
            Logger.application.warn(Logger.pattern, VersionHolder.VERSION, logprefix, "token: " + accessToken, "");
            Logger.application.warn(Logger.pattern, VersionHolder.VERSION, logprefix, "token length: " + accessToken.length(), "");

        } else {
            Logger.application.warn(Logger.pattern, VersionHolder.VERSION, logprefix, "token does not begin with Bearer String", "");
        }

        if (accessToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //Logger.application.info(Logger.pattern, VersionHolder.VERSION, logprefix, "sessionId: " + sessionId, "");
            ResponseEntity<HttpReponse> authResponse = restTemplate.postForEntity(userServiceSessionDetailsUrl, accessToken, HttpReponse.class);

            Date expiryTime = null;

            Auth auth = null;
            String username = null;

            if (authResponse.getStatusCode() == HttpStatus.ACCEPTED) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                
                Logger.application.warn(Logger.pattern, VersionHolder.VERSION, logprefix, "data: " + authResponse.getBody().getData(), "");

                auth = mapper.convertValue(authResponse.getBody().getData(), Auth.class);
                username = auth.getSession().getUsername();
                expiryTime = auth.getSession().getExpiry();
            }

            if (null != expiryTime && null != username) {
                long diff = 0;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date currentTime = sdf.parse(DateTimeUtil.currentTimestamp());
                    diff = expiryTime.getTime() - currentTime.getTime();
                } catch (ParseException e) {
                    Logger.application.warn(Logger.pattern, VersionHolder.VERSION, logprefix, "error calculating time to session expiry", "");
                }
                Logger.application.info(Logger.pattern, VersionHolder.VERSION, logprefix, "time to session expiry: " + diff + "ms", "");
                if (0 < diff) {
                    MySQLUserDetails userDetails = new MySQLUserDetails(auth, auth.getAuthorities());

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    Logger.application.warn(Logger.pattern, VersionHolder.VERSION, logprefix, "session expired", "");
                    //response.setStatus(HttpStatus.UNAUTHORIZED);
                    response.getWriter().append("Session expired");
                }
            }
        }
        chain.doFilter(request, response);
    }
}
