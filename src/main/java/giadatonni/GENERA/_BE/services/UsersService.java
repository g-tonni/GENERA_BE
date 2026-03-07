package giadatonni.GENERA._BE.services;

import giadatonni.GENERA._BE.entities.Role;
import giadatonni.GENERA._BE.entities.User;
import giadatonni.GENERA._BE.exceptions.BadRequestException;
import giadatonni.GENERA._BE.exceptions.NotFoundException;
import giadatonni.GENERA._BE.payloads.RegisterDTO;
import giadatonni.GENERA._BE.payloads.RoleDTO;
import giadatonni.GENERA._BE.repositories.RolesRepository;
import giadatonni.GENERA._BE.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;

    public UsersService(UsersRepository usersRepository, RolesRepository rolesRepository) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
    }

    public Role findRoleById(String role) {
        return this.rolesRepository.findById(role).orElseThrow(() -> new NotFoundException(role));
    }

    public Role addRole(RoleDTO body) {
        this.findRoleById(body.role());

        Role newRole = new Role(body.role());

        this.rolesRepository.save(newRole);

        System.out.println("Added role: " + newRole.getRole());

        return newRole;
    }

    public User findUserById(UUID userId) {
        return this.usersRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
    }

    public User addUser(RegisterDTO body) {
        if (this.usersRepository.existsByEmail(body.email())) throw new BadRequestException("Existing email");

        Role role = this.findRoleById("USER");

        User newUser = new User(body.name(), body.email(), body.password(), role);

        this.usersRepository.save(newUser);

        System.out.println("Added user: " + newUser.getUserId());

        return newUser;
    }

}
