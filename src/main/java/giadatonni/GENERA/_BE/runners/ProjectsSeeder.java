package giadatonni.GENERA._BE.runners;

import giadatonni.GENERA._BE.entities.User;
import giadatonni.GENERA._BE.payloads.InitialProjectDTO;
import giadatonni.GENERA._BE.services.ProjectsService;
import giadatonni.GENERA._BE.services.UsersService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

@Component
@Order(3)
public class ProjectsSeeder implements CommandLineRunner {

    private final UsersService userService; // Usiamo il tuo service
    private final ObjectMapper objectMapper;
    private final ProjectsService projectsService;

    public ProjectsSeeder(UsersService userService, ObjectMapper objectMapper, ProjectsService projectsService) {
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.projectsService = projectsService;
    }

    @Override
    public void run(String... args) throws Exception {
        List<User> usersList = this.userService.findAllUsers().stream()
                .filter(user -> !user.getUsername().equals("Giada Tonni")).toList();

        InputStream inputStream = TypeReference.class.getResourceAsStream("/projects1.json");
        InputStream inputStream2 = TypeReference.class.getResourceAsStream("/projects2.json");
        InputStream inputStream3 = TypeReference.class.getResourceAsStream("/projects3.json");

        List<InitialProjectDTO> projectsList1 = objectMapper.readValue(inputStream, new TypeReference<List<InitialProjectDTO>>() {
        });
        List<InitialProjectDTO> projectsList2 = objectMapper.readValue(inputStream2, new TypeReference<List<InitialProjectDTO>>() {
        });
        List<InitialProjectDTO> projectsList3 = objectMapper.readValue(inputStream3, new TypeReference<List<InitialProjectDTO>>() {
        });

        if (this.projectsService.findAllProjects().isEmpty()) {
            for (int i = 0; i < usersList.size(); i++) {

                User user = usersList.get(i);

                InitialProjectDTO p1 = projectsList1.get(i);
                InitialProjectDTO p2 = projectsList2.get(i);
                InitialProjectDTO p3 = projectsList3.get(i);

                this.projectsService.addInitialProject(
                        p1,
                        user
                );

                this.projectsService.addInitialProject(
                        p2,
                        user
                );

                this.projectsService.addInitialProject(
                        p3,
                        user
                );
            }
        }
    }
}
