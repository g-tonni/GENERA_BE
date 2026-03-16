package giadatonni.GENERA._BE.services;

import giadatonni.GENERA._BE.entities.Appreciation;
import giadatonni.GENERA._BE.entities.Project;
import giadatonni.GENERA._BE.entities.User;
import giadatonni.GENERA._BE.exceptions.BadRequestException;
import giadatonni.GENERA._BE.exceptions.NotFoundException;
import giadatonni.GENERA._BE.repositories.AppreciationsRepository;
import giadatonni.GENERA._BE.specifications.ProjectsSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AppreciationsService {

    private final AppreciationsRepository appreciationsRepository;
    private final ProjectsService projectsService;
    private final UsersService usersService;
    private final ProjectsSpecifications projectsSpecifications;

    public AppreciationsService(AppreciationsRepository appreciationsRepository, ProjectsService projectsService, UsersService usersService, ProjectsSpecifications projectsSpecifications) {
        this.appreciationsRepository = appreciationsRepository;
        this.projectsService = projectsService;
        this.usersService = usersService;
        this.projectsSpecifications = projectsSpecifications;
    }

    public Appreciation findAppreciationById(UUID appreciationId) {
        return this.appreciationsRepository.findById(appreciationId).orElseThrow(() -> new NotFoundException(appreciationId));
    }

    public Page<Project> findProjectsAppreciatedByUserId(
            int page,
            int size,
            String orderBy,
            String sortCriteria,
            String partialTitle,
            UUID userId) {

        if (size > 20 || size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(
                page,
                size,
                sortCriteria.equals("asc")
                        ? Sort.by(orderBy).ascending()
                        : Sort.by(orderBy).descending()
        );

        Specification<Project> spec = (root, query, cb) -> cb.conjunction();

        if (partialTitle != null) {
            spec = spec.and(projectsSpecifications.partialTitleEqualsTo(partialTitle));
        }

        return this.appreciationsRepository.findProjectsByUserId(userId, pageable);
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
