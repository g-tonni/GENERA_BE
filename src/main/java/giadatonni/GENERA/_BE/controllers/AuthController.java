package giadatonni.GENERA._BE.controllers;

import giadatonni.GENERA._BE.entities.User;
import giadatonni.GENERA._BE.exceptions.ValidationException;
import giadatonni.GENERA._BE.payloads.LoginDTO;
import giadatonni.GENERA._BE.payloads.LoginResponseDTO;
import giadatonni.GENERA._BE.payloads.RegisterDTO;
import giadatonni.GENERA._BE.services.AuthService;
import giadatonni.GENERA._BE.services.UsersService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            return new LoginResponseDTO(this.authService.checkCredentialsAndGenerateToken(body));
        }
    }
}
