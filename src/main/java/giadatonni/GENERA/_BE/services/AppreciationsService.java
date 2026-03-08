package giadatonni.GENERA._BE.services;

import giadatonni.GENERA._BE.entities.Appreciation;
import giadatonni.GENERA._BE.entities.Project;
import giadatonni.GENERA._BE.entities.User;
import giadatonni.GENERA._BE.exceptions.BadRequestException;
import giadatonni.GENERA._BE.exceptions.NotFoundException;
import giadatonni.GENERA._BE.repositories.AppreciationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AppreciationsService {

    private final AppreciationsRepository appreciationsRepository;
    private final ProjectsService projectsService;
    private final UsersService usersService;

    public AppreciationsService(AppreciationsRepository appreciationsRepository, ProjectsService projectsService, UsersService usersService) {
        this.appreciationsRepository = appreciationsRepository;
        this.projectsService = projectsService;
        this.usersService = usersService;
    }

    public Appreciation findAppreciationById(UUID appreciationId) {
        return this.appreciationsRepository.findById(appreciationId).orElseThrow(() -> new NotFoundException(appreciationId));
    }

    public List<Project> findProjectsAppreciatedByUserId(UUID userId) {
        User user = this.usersService.findUserById(userId);
        return this.appreciationsRepository.findByUser(user)
                .stream()
                .map(appreciation -> appreciation.getProject())
                .toList();
    }

    public List<Project> findProjectsAppreciatedByUser(User user) {
        return this.appreciationsRepository.findByUser(user)
                .stream()
                .map(appreciation -> appreciation.getProject())
                .toList();
    }

    public List<Appreciation> findAppreciationsByProject(UUID projectId) {

        Project project = this.projectsService.findProjectById(projectId);

        return this.appreciationsRepository.findByProject(project);
    }

    public Appreciation addAppreciation(UUID projectId, User user) {

        Project project = this.projectsService.findProjectById(projectId);

        if (this.appreciationsRepository.existsByUserAndProject(user, project))
            throw new BadRequestException("A user can only like a project once");

        Appreciation newAppreciation = new Appreciation(project, user);

        this.appreciationsRepository.save(newAppreciation);

        System.out.println("Added appreciation");

        return newAppreciation;
    }

    public void deleteAppreciation(User user, UUID projectId) {
        Project project = this.projectsService.findProjectById(projectId);

        Appreciation appreciation = this.appreciationsRepository.findByUserAndProject(user, project);

        if (appreciation == null) throw new BadRequestException("The user has never given a rating to this project");

        this.appreciationsRepository.delete(appreciation);

        System.out.println("Appreciation deleted");
    }


}
