package joney.board.article.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PageLimitCalculator {

    // (((n – 1) / k) + 1) * m * k + 1
    // 현재 페이지 : n
    // 페이지 당 게시글 개수 : m
    // 이동 가능한 페이지 개수 : k

    public static Long calculatePageLimit(Long page, Long pageSize, Long movablePageCount) {
        return (((page - 1) / movablePageCount) + 1) * pageSize * movablePageCount + 1;
    }
}
