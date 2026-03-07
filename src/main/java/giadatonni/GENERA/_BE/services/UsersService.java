package giadatonni.GENERA._BE.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import giadatonni.GENERA._BE.entities.Role;
import giadatonni.GENERA._BE.entities.User;
import giadatonni.GENERA._BE.exceptions.BadRequestException;
import giadatonni.GENERA._BE.exceptions.NotFoundException;
import giadatonni.GENERA._BE.payloads.RegisterDTO;
import giadatonni.GENERA._BE.payloads.RoleDTO;
import giadatonni.GENERA._BE.payloads.SketchDTO;
import giadatonni.GENERA._BE.payloads.UserDTO;
import giadatonni.GENERA._BE.repositories.RolesRepository;
import giadatonni.GENERA._BE.repositories.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final Cloudinary cloudinaryUploader;

    public UsersService(UsersRepository usersRepository, RolesRepository rolesRepository, PasswordEncoder passwordEncoder, Cloudinary cloudinaryUploader) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryUploader = cloudinaryUploader;
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

    public List<User> findAllUsers() {
        return this.usersRepository.findAll();
    }

    public User findUserByEmail(String email) {
        return this.usersRepository.findByEmail(email);
    }

    public User findUserById(UUID userId) {
        return this.usersRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
    }

    public User addUser(RegisterDTO body) {
        if (this.usersRepository.existsByEmail(body.email())) throw new BadRequestException("Existing email");

        Role role = this.findRoleById("USER");

        User newUser = new User(body.name(), body.email(), passwordEncoder.encode(body.password()), role);

        this.usersRepository.save(newUser);

        System.out.println("Added user: " + newUser.getUserId());

        return newUser;
    }

    public User addFirstAdmin(RegisterDTO body) {
        Role role = this.findRoleById("SUPER_ADMIN");

        User newUser = new User(body.name(), body.email(), passwordEncoder.encode(body.password()), role);

        this.usersRepository.save(newUser);

        System.out.println("Added admin: " + newUser.getUserId());

        return newUser;
    }

    public User editProfile(User user, UserDTO body) {
        if (!user.getEmail().equals(body.email())) {
            if (this.usersRepository.existsByEmail(body.email())) throw new BadRequestException("Existing email");
        }

        user.setName(body.name());
        user.setBio(body.bio());
        user.setLocation(body.location());
        user.setWebsite(body.website());
        user.setEmail(body.email());
        if (!passwordEncoder.matches(body.password(), user.getPassword())) user.setPassword(body.password());

        this.usersRepository.save(user);

        System.out.println("User profile with id " + user.getUserId() + " updated");

        return user;
    }

    public User editProfileImage(User user, MultipartFile file) {
        try {
            Map result = cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) result.get("secure_url");
            user.setProfileImage(imageUrl);
            this.usersRepository.save(user);
            System.out.println("Profile image updated");
            return user;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void editProfileCoverSketch(User user, SketchDTO body) {

        user.setProfileCoverSketch(body.code());

        this.usersRepository.save(user);

        System.out.println("Profile cover sketch updated");
    }
}
