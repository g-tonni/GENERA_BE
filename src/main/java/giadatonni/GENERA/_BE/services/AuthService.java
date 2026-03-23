package giadatonni.GENERA._BE.services;

import giadatonni.GENERA._BE.entities.User;
import giadatonni.GENERA._BE.exceptions.UnauthorizedException;
import giadatonni.GENERA._BE.payloads.AccessTokenDTO;
import giadatonni.GENERA._BE.payloads.LoginDTO;
import giadatonni.GENERA._BE.payloads.LoginResponseDTO;
import giadatonni.GENERA._BE.payloads.ValidationTokenDTO;
import giadatonni.GENERA._BE.security.JWTTools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
    private final JWTTools jwtTools;

    public AuthService(UsersService usersService, PasswordEncoder passwordEncoder, JWTTools jwtTools) {
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTools = jwtTools;
    }

    public LoginResponseDTO checkCredentialsAndGenerateToken(LoginDTO body) {
        User found = this.usersService.findUserByEmail(body.email());
        if (passwordEncoder.matches(body.password(), found.getPassword())) {
            return new LoginResponseDTO(this.jwtTools.generateToken(found), found.getUserId());
        } else {
            throw new UnauthorizedException("Incorrect credentials");
        }
    }

    public ValidationTokenDTO verifyValidationToken(AccessTokenDTO body) {
        try {
            this.jwtTools.verifyToken(body.token());
            return new ValidationTokenDTO(true);
        } catch (Exception ex) {
            return new ValidationTokenDTO(false);
        }
    }
}
