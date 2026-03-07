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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class ProjectsService {

    private final ProjectsRepository projectsRepository;
    private final CategoriesService categoriesService;
    private final Cloudinary cloudinaryUploader;

    public ProjectsService(ProjectsRepository projectsRepository, CategoriesService categoriesService, Cloudinary cloudinaryUploader) {
        this.projectsRepository = projectsRepository;
        this.categoriesService = categoriesService;
        this.cloudinaryUploader = cloudinaryUploader;
    }

    public Project findProjectById(UUID projectId) {
        return this.projectsRepository.findById(projectId).orElseThrow(() -> new NotFoundException(projectId));
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
}
