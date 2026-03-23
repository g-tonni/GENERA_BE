package giadatonni.GENERA._BE.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import giadatonni.GENERA._BE.entities.Project;
import giadatonni.GENERA._BE.entities.Role;
import giadatonni.GENERA._BE.entities.User;
import giadatonni.GENERA._BE.exceptions.BadRequestException;
import giadatonni.GENERA._BE.exceptions.NotFoundException;
import giadatonni.GENERA._BE.payloads.RegisterDTO;
import giadatonni.GENERA._BE.payloads.SketchDTO;
import giadatonni.GENERA._BE.payloads.UserDTO;
import giadatonni.GENERA._BE.repositories.ProjectsRepository;
import giadatonni.GENERA._BE.repositories.UsersRepository;
import giadatonni.GENERA._BE.specifications.ProjectsSpecifications;
import giadatonni.GENERA._BE.specifications.UsersSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    private final RolesService rolesService;
    private final PasswordEncoder passwordEncoder;
    private final Cloudinary cloudinaryUploader;
    private final UsersSpecifications usersSpecifications;
    private final ProjectsSpecifications projectsSpecifications;
    private final ProjectsRepository projectsRepository;

    public UsersService(UsersRepository usersRepository, RolesService rolesService, PasswordEncoder passwordEncoder, Cloudinary cloudinaryUploader, UsersSpecifications usersSpecifications, ProjectsSpecifications projectsSpecifications, ProjectsRepository projectsRepository) {
        this.usersRepository = usersRepository;
        this.rolesService = rolesService;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryUploader = cloudinaryUploader;
        this.usersSpecifications = usersSpecifications;
        this.projectsSpecifications = projectsSpecifications;
        this.projectsRepository = projectsRepository;
    }

    public List<User> findAllUsers() {
        return this.usersRepository.findAll();
    }

    public Page<User> searchUsers(int page, int size, String orderBy, String partialName) {
        if (size > 20 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy).ascending());

        Specification<User> spec = (root, query, cb) -> cb.conjunction();

        if (partialName != null) {
            spec = spec.and(usersSpecifications.partialNameEqualsTo(partialName));
        }

        return this.usersRepository.findAll(spec, pageable);
    }

    public User findUserByEmail(String email) {
        User found = this.usersRepository.findByEmail(email);
        if (found == null) {
            throw new NotFoundException();
        } else {
            return found;
        }
    }

    public User findUserById(UUID userId) {
        return this.usersRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
    }

    public User addUser(RegisterDTO body) {
        if (this.usersRepository.existsByEmail(body.email())) throw new BadRequestException("Existing email");

        Role role = this.rolesService.findRoleById("USER");

        User newUser = new User(body.name(), body.email(), passwordEncoder.encode(body.password()), role);

        this.usersRepository.save(newUser);

        System.out.println("Added user: " + newUser.getUserId());

        return newUser;
    }

    public User addFirstAdmin(RegisterDTO body) {
        Role role = this.rolesService.findRoleById("SUPER_ADMIN");

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
        if (body.password() != null && !body.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(body.password()));
        }

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

    public void deleteUser(User user) {
        this.usersRepository.delete(user);

        System.out.println("User deleted");
    }

    public Page<Project> getProjectsByAuthorId(
            int page,
            int size,
            String orderBy,
            String sortCriteria,
            String partialTitle,
            UUID authorId
    ) {

        User author = this.findUserById(authorId);

        if (size > 20 || size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(
                page,
                size,
                sortCriteria.equals("asc")
                        ? Sort.by(orderBy).ascending()
                        : Sort.by(orderBy).descending()
        );

        Specification<Project> spec = (root, query, cb) ->
                cb.equal(root.get("author"), author);

        if (partialTitle != null) {
            spec = spec.and(projectsSpecifications.partialTitleEqualsTo(partialTitle));
        }

        return projectsRepository.findAll(spec, pageable);
    }
}
