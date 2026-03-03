package joney.board.comment.api;

import joney.board.comment.entity.Comment;
import joney.board.comment.service.request.CommentCreateRequest;
import joney.board.comment.service.response.CommentPageResponse;
import joney.board.comment.service.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

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

    @Test
    void readAll() {
        CommentPageResponse response = restClient.get()
                .uri("/v1/comments?articleId=1&page=10000&pageSize=10")
                .retrieve()
                .body(CommentPageResponse.class);

        System.out.println("response.getCommentCount() = " + response.getCommentCount());
        for (CommentResponse comment : response.getComments()) {

            // 코멘트 아이디가 1뎁스 댓글이면, 댓글 아이디와 부모 댓글 아이디가 같음.
            // 이게 다르면, 2뎁스의 댓글을 의미.
            if (!comment.getCommentId().equals(comment.getParentCommentId())) {
                System.out.print("\t");
            }
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }
    }
    // 10000페이지 매우 느린 조선컴으로 테스트 결과 BUILD SUCCESSFUL in 4s (성능 매우 준수)

    /* readAll 테스트 결과
        response.getCommentCount() = 101
    comment.getCommentId() = 285654454711644160
        comment.getCommentId() = 285654454795530243
    comment.getCommentId() = 285654454711644161
        comment.getCommentId() = 285654454795530241
    comment.getCommentId() = 285654454711644162
        comment.getCommentId() = 285654454795530259
    comment.getCommentId() = 285654454711644163
        comment.getCommentId() = 285654454795530240
    comment.getCommentId() = 285654454711644164
        comment.getCommentId() = 285654454795530242
    **/

    @Test
    void readAllInfiniteScroll() {
        List<CommentResponse> responses1 = restClient.get()
                .uri("/v1/comments/infinite-scroll?articleId=1&pageSize=5")
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("firstPage");
        for (CommentResponse comment : responses1) {
            if (!comment.getCommentId().equals(comment.getParentCommentId())) {
                System.out.print("\t");
            }
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }

        Long lastParentCommentId = responses1.getLast().getParentCommentId();
        Long lastCommentId = responses1.getLast().getCommentId();

        List<CommentResponse> responses2 = restClient.get()
                .uri("/v1/comments/infinite-scroll?articleId=1&pageSize=5&lastParentCommentId=%s&lastCommentId=%s"
                        .formatted(lastParentCommentId, lastCommentId))
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("secondPage");
        for (CommentResponse comment : responses2) {
            if (!comment.getCommentId().equals(comment.getParentCommentId())) {
                System.out.print("\t");
            }
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }

        /*
            firstPage
            comment.getCommentId() = 285654454711644160
                comment.getCommentId() = 285654454795530243
            comment.getCommentId() = 285654454711644161
                comment.getCommentId() = 285654454795530241
            comment.getCommentId() = 285654454711644162
            secondPage
                comment.getCommentId() = 285654454795530259
            comment.getCommentId() = 285654454711644163
                comment.getCommentId() = 285654454795530240
            comment.getCommentId() = 285654454711644164
                comment.getCommentId() = 285654454795530242
            > Task :service:comment:test
            BUILD SUCCESSFUL in 3s
        * **/
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
