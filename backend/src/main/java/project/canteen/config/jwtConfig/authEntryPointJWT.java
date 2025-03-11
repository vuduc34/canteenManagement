package project.canteen.config.jwtConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.security.web.AuthenticationEntryPoint;
import java.io.IOException;

@Component
public class authEntryPointJWT implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(authEntryPointJWT.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        logger.error("Unauthorized error: {}", authException.getMessage());
        response.sendRedirect("/api/v1/project/auth/authFail");
//		response.setContentType("application/json;charset=UTF-8");
//		response.setStatus(200);
//		response.getWriter("{resultCode:-1}");

    }
}
