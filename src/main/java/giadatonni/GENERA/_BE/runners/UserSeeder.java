package giadatonni.GENERA._BE.runners;

import giadatonni.GENERA._BE.exceptions.BadRequestException;
import giadatonni.GENERA._BE.payloads.UserDTO;
import giadatonni.GENERA._BE.services.UsersService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

@Component
@Order(2)
public class UserSeeder implements CommandLineRunner {

    private final UsersService userService; // Usiamo il tuo service
    private final ObjectMapper objectMapper;

    public UserSeeder(UsersService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        if (this.userService.findAllUsers().size() == 1) {

            InputStream inputStream = TypeReference.class.getResourceAsStream("/users.json");

            List<UserDTO> usersList = objectMapper.readValue(inputStream, new TypeReference<List<UserDTO>>() {
            });

            for (UserDTO dto : usersList) {
                try {
                    userService.addInitialUser(dto);
                } catch (BadRequestException e) {
                    System.out.println("Skip user: " + e.getMessage());
                }
            }

            System.out.println("Seeding complete");
        }
    }
}