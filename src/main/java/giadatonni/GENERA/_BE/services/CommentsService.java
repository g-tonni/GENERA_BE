package giadatonni.GENERA._BE.services;

import giadatonni.GENERA._BE.entities.Comment;
import giadatonni.GENERA._BE.entities.Project;
import giadatonni.GENERA._BE.entities.User;
import giadatonni.GENERA._BE.exceptions.BadRequestException;
import giadatonni.GENERA._BE.exceptions.NotFoundException;
import giadatonni.GENERA._BE.exceptions.UnauthorizedException;
import giadatonni.GENERA._BE.payloads.CommentDTO;
import giadatonni.GENERA._BE.repositories.CommentsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final ProjectsService projectsService;

    public CommentsService(CommentsRepository commentsRepository, ProjectsService projectsService) {
        this.commentsRepository = commentsRepository;
        this.projectsService = projectsService;
    }

    public Comment findCommentById(UUID commentId) {
        return this.commentsRepository.findById(commentId).orElseThrow(() -> new NotFoundException(commentId));
    }

    public List<Comment> findCommentsByProject(UUID projectId) {
        Project project = this.projectsService.findProjectById(projectId);
        return this.commentsRepository.findByProject(project);
    }

    public Comment addComment(CommentDTO body, User user, UUID projectId) {
        Project project = this.projectsService.findProjectById(projectId);

        Comment newComment = new Comment(body.content(), project, user);

        this.commentsRepository.save(newComment);

        System.out.println("Added comment");

        return newComment;
    }

    public Comment editComment(User user, CommentDTO body, UUID commentId) {
        Comment comment = this.findCommentById(commentId);
        if (!user.getUserId().equals(comment.getUser().getUserId()))
            throw new UnauthorizedException("A user can only edit their own comments");

        comment.setContent(body.content());
        comment.setUpdated(true);

        this.commentsRepository.save(comment);

        System.out.println("Comment updated");

        return comment;
    }

    public void deleteComment(User user, UUID projectId, UUID commentId) {
        Comment comment = this.findCommentById(commentId);

        if (!user.getUserId().equals(comment.getUser().getUserId()))
            throw new UnauthorizedException("A user can only delete their own comments");

        boolean exists = this.findCommentsByProject(projectId).stream()
                .anyMatch(com -> com.getCommentId().equals(comment.getCommentId()));

        if (!exists) throw new BadRequestException("The comment is not related to the project");

        this.commentsRepository.delete(comment);

        System.out.println("Comment deleted");
    }

}
