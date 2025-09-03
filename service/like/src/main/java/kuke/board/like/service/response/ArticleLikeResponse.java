package kuke.board.like.service.response;

import kuke.board.like.entity.ArticleLike;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ArticleLikeResponse {

    private Long articleLikeId;
    private Long articleId;
    private Long userId;
    private LocalDateTime createdAt;

    public static ArticleLikeResponse from(ArticleLike articleLike) {
        ArticleLikeResponse res = new ArticleLikeResponse();
        res.articleLikeId = articleLike.getArticleLikeId();
        res.articleId = articleLike.getArticleId();
        res.userId = articleLike.getUserId();
        res.createdAt = articleLike.getCreatedAt();
        return res;
    }

}
