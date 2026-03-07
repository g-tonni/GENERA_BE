package giadatonni.GENERA._BE.controllers;

import giadatonni.GENERA._BE.entities.Project;
import giadatonni.GENERA._BE.entities.User;
import giadatonni.GENERA._BE.exceptions.ValidationException;
import giadatonni.GENERA._BE.payloads.ProjectDTO;
import giadatonni.GENERA._BE.payloads.SketchDTO;
import giadatonni.GENERA._BE.services.ProjectsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectsController {

    private final ProjectsService projectsService;

    public ProjectsController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @GetMapping("/{projectId}")
    public Project getProjectById(@PathVariable UUID projectId) {
        return this.projectsService.findProjectById(projectId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Project addProject(@AuthenticationPrincipal User user) {
        return this.projectsService.addProject(user);
    }

    @PutMapping("/{projectId}/editInfo")
    public Project editProjectInfo(@PathVariable UUID projectId, @RequestBody @Validated ProjectDTO body, BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            List<String> errorsList = validationResults
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            return this.projectsService.editProjectInfo(projectId, body);
        }
    }

    @PatchMapping("/{projectId}/cover")
    public Project editCover(@PathVariable UUID projectId, @RequestParam("cover") MultipartFile file) {
        return this.projectsService.editCover(projectId, file);
    }

    @PatchMapping("/{projectId}/sketch")
    public void editSketch(@PathVariable UUID projectId,
                           @RequestBody @Validated SketchDTO body,
                           BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            List<String> errorsList = validationResults
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            this.projectsService.editSketch(projectId, body);
        }
    }
}
