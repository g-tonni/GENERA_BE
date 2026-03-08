package giadatonni.GENERA._BE.controllers;

import giadatonni.GENERA._BE.entities.User;
import giadatonni.GENERA._BE.exceptions.ValidationException;
import giadatonni.GENERA._BE.payloads.SketchDTO;
import giadatonni.GENERA._BE.payloads.UserDTO;
import giadatonni.GENERA._BE.services.UsersService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public Page<User> searchUsers(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "name") String orderBy,
                                  @RequestParam(required = false) String partialName) {
        return this.usersService.searchUsers(page, size, orderBy, partialName);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId) {
        return this.usersService.findUserById(userId);
    }

    @PutMapping("/me/edit")
    public User editProfile(@AuthenticationPrincipal User user,
                            @RequestBody @Validated UserDTO body,
                            BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            List<String> errorsList = validationResults
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            return this.usersService.editProfile(user, body);
        }
    }

    @PatchMapping("/me/profileImage")
    public User editProfileImage(@AuthenticationPrincipal User user, @RequestParam("profile_image") MultipartFile file) {
        return this.usersService.editProfileImage(user, file);
    }

    @PatchMapping("/me/profileCover")
    public void editProfileCoverSketch(@AuthenticationPrincipal User user,
                                       @RequestBody @Validated SketchDTO body,
                                       BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            List<String> errorsList = validationResults
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            this.usersService.editProfileCoverSketch(user, body);
        }
    }


}
