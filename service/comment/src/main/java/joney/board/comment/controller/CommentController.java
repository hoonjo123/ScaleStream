package joney.board.comment.controller;

import joney.board.comment.service.CommentService;
import joney.board.comment.service.request.CommentCreateRequest;
import joney.board.comment.service.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1")
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/comments/{commentId}")
    public CommentResponse read(
            @PathVariable("commentId") Long commentId
    ) {
        return commentService.read(commentId);
    }

    @PostMapping("/comments")
    public CommentResponse create(@RequestBody CommentCreateRequest request) {
        return commentService.create(request);
    }

    @DeleteMapping("/comments/{commentId}")
    public void delete(@PathVariable("commentId") Long commentId) {
        commentService.delete(commentId);
    }
}