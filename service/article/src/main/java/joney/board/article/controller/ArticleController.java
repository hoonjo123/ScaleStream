package joney.board.article.controller;

import joney.board.article.service.ArticleService;
import joney.board.article.service.request.ArticleCreateRequest;
import joney.board.article.service.request.ArticleUpdateRequest;
import joney.board.article.service.response.ArticlePageResponse;
import joney.board.article.service.response.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/articles/{articleId}")
    public ArticleResponse read(@PathVariable Long articleId){
        return articleService.read(articleId);
    }

    @GetMapping("/articles")
    public ArticlePageResponse readAll(@RequestParam("boardId") Long boardId,
                                      @RequestParam("page") Long page,
                                      @RequestParam("pageSize") Long pageSize){
        return articleService.readAll(boardId, page, pageSize);
    }

    @PostMapping("/articles")
    public ArticleResponse create(@RequestBody ArticleCreateRequest request){
        return articleService.create(request);
    }

    @PutMapping("/articles/{articleId}")
    public ArticleResponse update(@PathVariable Long articleId,
                                  @RequestBody ArticleUpdateRequest request){
        return articleService.update(articleId, request);
    }

    @DeleteMapping("/articles/{articleId}")
    public void delete(@PathVariable Long articleId){
        articleService.delete(articleId);
    }
}
