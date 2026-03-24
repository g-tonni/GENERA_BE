package giadatonni.GENERA._BE.controllers;

import giadatonni.GENERA._BE.entities.Appreciation;
import giadatonni.GENERA._BE.entities.Project;
import giadatonni.GENERA._BE.entities.User;
import giadatonni.GENERA._BE.services.AppreciationsService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class AppreciationsController {

    private final AppreciationsService appreciationsService;

    public AppreciationsController(AppreciationsService appreciationsService) {
        this.appreciationsService = appreciationsService;
    }

    @GetMapping("/users/{userId}/appreciations")
    public Page<Appreciation> findProjectsAppreciatedByUserId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "likedAt") String orderBy,
            @RequestParam(defaultValue = "desc") String sortCriteria,
            @RequestParam(required = false) String partialTitle,
            @PathVariable UUID userId) {
        return this.appreciationsService.findProjectsAppreciatedByUserId(page, size, orderBy, sortCriteria, partialTitle, userId);
    }

    @GetMapping("/users/me/appreciations")
    public List<Project> findProjectsAppreciatedByUser(@AuthenticationPrincipal User user) {
        return this.appreciationsService.findProjectsAppreciatedByUser(user);
    }

    @GetMapping("/projects/{projectId}/appreciations")
    public List<Appreciation> findAppreciationsByProject(@PathVariable UUID projectId) {
        return this.appreciationsService.findAppreciationsByProject(projectId);
    }

    @PostMapping("/projects/{projectId}/appreciations")
    @ResponseStatus(HttpStatus.CREATED)
    public Appreciation addAppreciations(@PathVariable UUID projectId, @AuthenticationPrincipal User user) {
        return this.appreciationsService.addAppreciation(projectId, user);
    }

    @DeleteMapping("/projects/{projectId}/appreciations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppreciation(@AuthenticationPrincipal User user, @PathVariable UUID projectId) {
        this.appreciationsService.deleteAppreciation(user, projectId);
    }
}
