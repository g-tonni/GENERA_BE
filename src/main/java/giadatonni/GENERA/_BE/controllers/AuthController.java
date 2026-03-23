package giadatonni.GENERA._BE.controllers;

import giadatonni.GENERA._BE.entities.User;
import giadatonni.GENERA._BE.exceptions.ValidationException;
import giadatonni.GENERA._BE.payloads.*;
import giadatonni.GENERA._BE.services.AuthService;
import giadatonni.GENERA._BE.services.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UsersService usersService;

    public AuthController(AuthService authService, UsersService usersService) {
        this.authService = authService;
        this.usersService = usersService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody @Validated RegisterDTO body, BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            List<String> errorsList = validationResults
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            return this.usersService.addUser(body);
        }
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Validated LoginDTO body, BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            List<String> errorsList = validationResults
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            return this.authService.checkCredentialsAndGenerateToken(body);
        }
    }

    @PostMapping("/token")
    public ValidationTokenDTO verifyToken(@RequestBody AccessTokenDTO body) {
        return this.authService.verifyValidationToken(body);
    }
}
