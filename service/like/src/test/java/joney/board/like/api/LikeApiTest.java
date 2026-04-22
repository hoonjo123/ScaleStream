package joney.board.like.api;

import joney.board.like.service.response.ArticleLikeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class LikeApiTest {
    RestClient restClient = RestClient.create("http://localhost:9002");

    @Test
    void likeAndUnlikeTest() {
        Long aritcleId = 9999L;

        like(aritcleId, 1L);
        like(aritcleId, 2L);
        like(aritcleId, 3L);

        ArticleLikeResponse response1 = read(aritcleId, 1L);
        ArticleLikeResponse response2 = read(aritcleId, 1L);
        ArticleLikeResponse response3 = read(aritcleId, 1L);
        System.out.println("response1 = " + response1 );
        System.out.println("response2 = " + response2 );
        System.out.println("response3 = " + response3 );

        unlike(aritcleId, 1L);
        unlike(aritcleId, 2L);
        unlike(aritcleId, 3L);


    }

    void like(Long articleId, Long userId){
        restClient.post()
                .uri("/v1/article-likes/articles/{articleId}/users/{userId}", articleId, userId)
                .retrieve();
    }

    void unlike(Long articleId, Long userId){
        restClient.delete()
                .uri("/v1/article-likes/articles/{articleId}/users/{userId}", articleId, userId)
                .retrieve();
    }

    ArticleLikeResponse read(Long articleId, Long userId){
        return restClient.get()
                .uri("/v1/article-likes/articles/{articleId}/users/{userId}", articleId, userId)
                .retrieve()
                .body(ArticleLikeResponse.class);
    }
}
