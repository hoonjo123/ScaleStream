package joney.board.article.api;

import joney.board.article.service.request.ArticleCreateRequest;
import joney.board.article.service.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class ArticleApiTest {
    RestClient restClient = RestClient.create("http://localhost:9000");

    @Test
    void createTest(){
        ArticleResponse response = create(new ArticleCreateRequest(
                "title",
                "content",
                1L,
                1L
        ));
        System.out.println("response = " + response);
    }

    ArticleResponse create(ArticleCreateRequest request){
        return restClient.post()
                .uri("/v1/articles")
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void readTest(){
        ArticleResponse response = read(278031835679850496L);
        System.out.println("response = " + response);
    }

    ArticleResponse read(Long articleId){
        return restClient.get()
                .uri("/v1/articles/{articleId}", articleId)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void updateTest(){
        update(278031835679850496L);
        ArticleResponse response = read(278031835679850496L);
        System.out.println("response = " + response);
    }

    void update(Long articleId){
          restClient.put()
                .uri("/v1/articles/{articleId}", articleId)
                .body(new ArticleUpdateRequest(
                        "updated title",
                        "updated content"
                ))
                .retrieve();
    }

    @Test
    void deleteTest(){
        restClient.delete()
                .uri("/v1/articles/{articleId}", 278031835679850496L)
                .retrieve();
    }

    @Getter
    @AllArgsConstructor
    static class ArticleCreateRequest {
        private String title;
        private String content;
        private Long writerId;
        private Long boardId;
    }

    @Getter
    @AllArgsConstructor
    static class ArticleUpdateRequest {
        private String title;
        private String content;
    }
}
