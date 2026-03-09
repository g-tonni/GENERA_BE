package giadatonni.GENERA._BE.controllers;

import giadatonni.GENERA._BE.entities.Role;
import giadatonni.GENERA._BE.exceptions.ValidationException;
import giadatonni.GENERA._BE.payloads.RoleDTO;
import giadatonni.GENERA._BE.services.RolesService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RolesController {

    private final RolesService rolesService;

    public RolesController(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public List<Role> findAllRoles() {
        return this.rolesService.findAllRoles();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public Role addRole(@RequestBody @Validated RoleDTO body, BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            List<String> errorsList = validationResults
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            return this.rolesService.addRole(body);
        }
    }

    @DeleteMapping("/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public void deleteRole(@PathVariable String roleId) {
        this.rolesService.deleteRole(roleId);
    }
}
