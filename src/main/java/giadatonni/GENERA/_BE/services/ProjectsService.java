package giadatonni.GENERA._BE.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import giadatonni.GENERA._BE.entities.Category;
import giadatonni.GENERA._BE.entities.Project;
import giadatonni.GENERA._BE.entities.User;
import giadatonni.GENERA._BE.exceptions.NotFoundException;
import giadatonni.GENERA._BE.exceptions.UnauthorizedException;
import giadatonni.GENERA._BE.payloads.ProjectDTO;
import giadatonni.GENERA._BE.payloads.SketchDTO;
import giadatonni.GENERA._BE.repositories.ProjectsRepository;
import giadatonni.GENERA._BE.specifications.ProjectsSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class ProjectsService {

    private final ProjectsRepository projectsRepository;
    private final CategoriesService categoriesService;
    private final UsersService usersService;
    private final Cloudinary cloudinaryUploader;
    private final ProjectsSpecifications projectsSpecifications;

    public ProjectsService(ProjectsRepository projectsRepository, CategoriesService categoriesService, Cloudinary cloudinaryUploader, ProjectsSpecifications projectsSpecifications, UsersService usersService) {
        this.projectsRepository = projectsRepository;
        this.usersService = usersService;
        this.categoriesService = categoriesService;
        this.cloudinaryUploader = cloudinaryUploader;
        this.projectsSpecifications = projectsSpecifications;
    }

    public Project findProjectById(UUID projectId) {
        return this.projectsRepository.findById(projectId).orElseThrow(() -> new NotFoundException(projectId));
    }

    public Page<Project> searchProjects(int page, int size, String orderBy, String sortCriteria, String partialTitle, String category) {

        if (size > 20 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, sortCriteria.equals("asc") ? Sort.by(orderBy).ascending() : Sort.by(orderBy).descending());

        Specification<Project> spec = (root, query, cb) -> cb.conjunction();

        if (partialTitle != null) {
            spec = spec.and(projectsSpecifications.partialTitleEqualsTo(partialTitle));
        }

        if (category != null) {
            spec = spec.and(projectsSpecifications.categoryEqualsTo(category));
        }

        return this.projectsRepository.findAll(spec, pageable);
    }

    public Project addProject(User author) {
        Category category = this.categoriesService.findCategoryById("Generative");

        Project newProject = new Project(category, author);

        this.projectsRepository.save(newProject);

        System.out.println("Added project: " + newProject.getProjectId());

        return newProject;
    }

    public Project editProjectInfo(User user, UUID projectId, ProjectDTO body) {

        Project project = this.findProjectById(projectId);

        if (!user.getUserId().equals(project.getAuthor().getUserId()))
            throw new UnauthorizedException("A user can only edit their own projects");

        Category category = this.categoriesService.findCategoryById(body.category());

        project.setTitle(body.title());
        project.setDescription(body.description());
        project.setHowToInteract(body.howToInteract());
        project.setCategory(category);

        this.projectsRepository.save(project);

        System.out.println("Project with id " + project.getProjectId() + " updated");

        return project;
    }


    public Project editCover(User user, UUID projectId, MultipartFile file) {
        Project project = this.findProjectById(projectId);

        if (!user.getUserId().equals(project.getAuthor().getUserId()))
            throw new UnauthorizedException("A user can only edit their own projects");

        try {
            Map result = cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) result.get("secure_url");
            project.setCover(imageUrl);
            this.projectsRepository.save(project);
            System.out.println("Cover updated");
            return project;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void editSketch(User user, UUID projectId, SketchDTO body) {

        Project project = this.findProjectById(projectId);

        if (!user.getUserId().equals(project.getAuthor().getUserId()))
            throw new UnauthorizedException("A user can only edit their own projects");

        project.setScript(body.code());

        this.projectsRepository.save(project);

        System.out.println("Sketch updated");
    }

    public void deleteProject(User user, UUID projectId) {

        Project project = this.findProjectById(projectId);

        if (!user.getUserId().equals(project.getAuthor().getUserId()))
            throw new UnauthorizedException("A user can only delete their own projects");

        this.projectsRepository.delete(project);

        System.out.println("Project deleted");
    }
}
