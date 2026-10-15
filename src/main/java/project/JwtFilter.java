package project;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/ProfileServlet")
public class JwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest =
                (HttpServletRequest) request;

        HttpServletResponse httpResponse =
                (HttpServletResponse) response;

        // Get Authorization header
        String authorizationHeader =
                httpRequest.getHeader("Authorization");

        String token = null;

        // First check Authorization header
        if (authorizationHeader != null &&
            authorizationHeader.startsWith("Bearer ")) {

            token = authorizationHeader.substring(7);

        } else {

            // If header is not available, get token from session
            HttpSession session = httpRequest.getSession(false);

            if (session != null) {

                token = (String) session.getAttribute("token");
            }
        }

        // Check if token exists
        if (token == null || token.isEmpty()) {

            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            httpResponse.setContentType("text/html");

            httpResponse.getWriter().println(
                    "<h2>JWT Token is missing.</h2>"
            );

            return;
        }

        try {

            // Validate JWT token
            JwtUtil.validateToken(token);

            // Token is valid
            chain.doFilter(request, response);

        } catch (Exception e) {

            httpResponse.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            httpResponse.setContentType("text/html");

            httpResponse.getWriter().println(
                    "<h2>Invalid or expired JWT token.</h2>"
            );
        }
    }
}