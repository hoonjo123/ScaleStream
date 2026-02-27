package joney.board.comment.api;

import joney.board.comment.entity.Comment;
import joney.board.comment.service.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class CommentApiTest {
    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create() {
        CommentResponse response1 = createComment(new CommentCreateRequest(1L, "my comment1", null, 1L));
        CommentResponse response2 = createComment(new CommentCreateRequest(1L, "my comment2", response1.getCommentId(), 1L));
        CommentResponse response3 = createComment(new CommentCreateRequest(1L, "my comment3", response1.getCommentId(), 1L));

        System.out.println("commentId=%s".formatted(response1.getCommentId()));
        System.out.println("\tcommentId=%s".formatted(response2.getCommentId()));
        System.out.println("\tcommentId=%s".formatted(response3.getCommentId()));

//        commentId=285651603576803328
    //        commentId=285651604616990720
    //        commentId=285651604747014144
    }

    CommentResponse createComment(CommentCreateRequest request) {
        return restClient.post()
                .uri("/v1/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
    }

    @Test
    void read(){
        CommentResponse response = restClient.get()
                .uri("/v1/comments/{commentId}",285651603576803328L)
                .retrieve()
                .body(CommentResponse.class);

        System.out.println("response = " + response);
    }

    @Test
    void delete(){
        restClient.delete()
                .uri("/v1/comments/{commentId}",285651604747014144L)
                .retrieve();

//        commentId=285651603576803328
        //        commentId=285651604616990720
        //        commentId=285651604747014144

        // try delete root comment first

//        mysql> select * from comment where comment_id = 285651603576803328;
//        +--------------------+-------------+------------+--------------------+-----------+---------+---------------------+
//        | comment_id         | content     | article_id | parent_comment_id  | writer_id | deleted | created_at          |
//        +--------------------+-------------+------------+--------------------+-----------+---------+---------------------+
//        | 285651603576803328 | my comment1 |          1 | 285651603576803328 |         1 |       1 | 2026-02-27 14:57:29 |
//        +--------------------+-------------+------------+--------------------+-----------+---------+---------------------+
//        모든 데이터 삭제 후, 다시 조회하면 Empty set => 하위 데이터가 모두 삭제가 되야만 상위 데이터도 삭제가 되는 것을 확인할 수 있음.

    }

    @Getter
    @AllArgsConstructor
    public static class CommentCreateRequest {
        private Long articleId;
        private String content;
        private Long parentCommentId;
        private Long writerId;
    }

}
