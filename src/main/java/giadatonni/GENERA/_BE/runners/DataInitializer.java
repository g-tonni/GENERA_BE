package giadatonni.GENERA._BE.runners;

import giadatonni.GENERA._BE.payloads.CategoryDTO;
import giadatonni.GENERA._BE.payloads.RegisterDTO;
import giadatonni.GENERA._BE.payloads.RoleDTO;
import giadatonni.GENERA._BE.services.CategoriesService;
import giadatonni.GENERA._BE.services.RolesService;
import giadatonni.GENERA._BE.services.UsersService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private final UsersService usersService;
    private final RolesService rolesService;
    private final CategoriesService categoriesService;
    private final String adminName;
    private final String adminEmail;
    private final String adminPassword;

    public DataInitializer(UsersService usersService,
                           RolesService rolesService,
                           CategoriesService categoriesService,
                           @Value("${ADMIN_NAME}") String adminName,
                           @Value("${ADMIN_EMAIL}") String adminEmail,
                           @Value("${ADMIN_PASSWORD}") String adminPassword) {
        this.usersService = usersService;
        this.rolesService = rolesService;
        this.categoriesService = categoriesService;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) throws Exception {
        if (this.rolesService.findAllRoles().isEmpty()) {
            this.rolesService.addRole(new RoleDTO("SUPER_ADMIN"));
            this.rolesService.addRole(new RoleDTO("ADMIN"));
            this.rolesService.addRole(new RoleDTO("USER"));
        }

        if (this.categoriesService.findAllCategories().isEmpty()) {
            this.categoriesService.addCategory(new CategoryDTO("Generative"));
            this.categoriesService.addCategory(new CategoryDTO("Interactive"));
            this.categoriesService.addCategory(new CategoryDTO("Spatial"));
            this.categoriesService.addCategory(new CategoryDTO("Patterns"));
            this.categoriesService.addCategory(new CategoryDTO("Particles"));
        }

        if (this.usersService.findAllUsers().isEmpty()) {
            this.usersService.addFirstAdmin(new RegisterDTO(adminName, adminEmail, adminPassword));
        }
    }
}
