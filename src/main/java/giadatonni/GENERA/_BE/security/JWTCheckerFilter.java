package giadatonni.GENERA._BE.security;

import giadatonni.GENERA._BE.entities.User;
import giadatonni.GENERA._BE.exceptions.UnauthorizedException;
import giadatonni.GENERA._BE.services.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class JWTCheckerFilter extends OncePerRequestFilter {

    private final JWTTools jwtTools;
    private final UsersService usersService;

    public JWTCheckerFilter(JWTTools jwtTools, UsersService usersService) {
        this.jwtTools = jwtTools;
        this.usersService = usersService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer "))
            throw new UnauthorizedException("Enter the token in the header in the correct form");

        String token = authorization.replace("Bearer ", "");

        UUID userId = this.jwtTools.getUserIdByToken(token);

        User user = this.usersService.findUserById(userId);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher matcher = new AntPathMatcher();
        String path = request.getServletPath();
        String method = request.getMethod();

        List<String> publicPaths = List.of(
                "/auth/**"
        );

        if (method.equals("GET") && matcher.match("/projects", path)) {
            return true;
        }

        if (method.equals("GET") && matcher.match("/projects/*/comments", path)) {
            return true;
        }

        if (method.equals("GET") && matcher.match("/projects/*/appreciations", path)) {
            return true;
        }

        return publicPaths.stream().anyMatch(p -> matcher.match(p, path));
    }
}
