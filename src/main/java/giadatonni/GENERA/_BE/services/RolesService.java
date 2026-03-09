package giadatonni.GENERA._BE.services;

import giadatonni.GENERA._BE.entities.Role;
import giadatonni.GENERA._BE.exceptions.BadRequestException;
import giadatonni.GENERA._BE.exceptions.NotFoundException;
import giadatonni.GENERA._BE.payloads.RoleDTO;
import giadatonni.GENERA._BE.repositories.RolesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesService {

    private final RolesRepository rolesRepository;

    public RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public List<Role> findAllRoles() {
        return this.rolesRepository.findAll();
    }

    public Role findRoleById(String role) {
        return this.rolesRepository.findById(role).orElseThrow(() -> new NotFoundException(role));
    }

    public Role addRole(RoleDTO body) {
        if (this.rolesRepository.existsById(body.role())) throw new BadRequestException("Existing role");

        Role newRole = new Role(body.role());

        this.rolesRepository.save(newRole);

        System.out.println("Added role: " + newRole.getRole());

        return newRole;
    }

    public void deleteRole(String roleId) {

        Role role = this.findRoleById(roleId);

        this.rolesRepository.delete(role);

        System.out.println("Role deleted");
    }
}
