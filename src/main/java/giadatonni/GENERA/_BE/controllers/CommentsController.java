package giadatonni.GENERA._BE.controllers;

import giadatonni.GENERA._BE.entities.Comment;
import giadatonni.GENERA._BE.entities.User;
import giadatonni.GENERA._BE.exceptions.ValidationException;
import giadatonni.GENERA._BE.payloads.CommentDTO;
import giadatonni.GENERA._BE.services.CommentsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects/{projectId}/comments")
public class CommentsController {

    private final CommentsService commentsService;

    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @GetMapping
    public List<Comment> findCommentsByProject(@PathVariable UUID projectId) {
        return this.commentsService.findCommentsByProject(projectId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comment addComment(@RequestBody @Validated CommentDTO body,
                              @AuthenticationPrincipal User user,
                              @PathVariable UUID projectId,
                              BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            List<String> errorsList = validationResults
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            return this.commentsService.addComment(body, user, projectId);
        }
    }

    @PatchMapping("/{commentId}")
    public Comment editComment(@AuthenticationPrincipal User user,
                               @RequestBody @Validated CommentDTO body,
                               @PathVariable UUID commentId,
                               BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            List<String> errorsList = validationResults
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            return this.commentsService.editComment(user, body, commentId);
        }
    }


}
