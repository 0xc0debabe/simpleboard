package kuke.board.comment.api;

import kuke.board.comment.service.response.CommentPageResponse;
import kuke.board.comment.service.response.CommentResponse;
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
        CommentResponse response1 = createComment(new CommentCreateRequest(1L, "my comment1", null, 1000L));
        CommentResponse response2 = createComment(new CommentCreateRequest(1L, "my comment2", response1.getCommentId(), 1000L));

        System.out.println("commentId=%s".formatted(response1.getCommentId()));
        System.out.println("commentId=%s".formatted(response2.getCommentId()));
    }

    @Test
    void read() {
        CommentResponse response = restClient.get()
                .uri("/v1/comments/{commentId}", 220938689457184768L)
                .retrieve()
                .body(CommentResponse.class);

        System.out.println("response=" + response);
    }

    @Test
    void delete() {
        //        commentId=220938689457184768 - x
//        commentId=220938689687871488
//        commentId=220938689746591744
        restClient.delete()
                .uri("/v1/comments/{commentId}", 220938689746591744L)
                .retrieve();
    }

    @Test
    void readAll() {
        CommentPageResponse response = restClient.get()
                .uri("/v1/comments?articleId=1&page=1&pageSize=10")
                .retrieve()
                .body(CommentPageResponse.class);
        System.out.println("response.getCommentCount()=" + response.getCommentCount());
        for(CommentResponse comment : response.getComments()) {
            if (comment.getCommentId().equals(comment.getParentCommentId())) {
                System.out.println("");
            }
            System.out.println("comment.getCommentId()=" + comment.getCommentId());
        }

    }

    /**
     * 1번 페이지 구행 결과
     * comment.getCommentId()=220938628245512192
     * comment.getCommentId()=220938628954349568
     * comment.getCommentId()=220938629050818560
     *
     * comment.getCommentId()=220942086034968576
     * comment.getCommentId()=220942086097883138
     *
     * comment.getCommentId()=220942086034968577
     * comment.getCommentId()=220942086097883171
     *
     * comment.getCommentId()=220942086034968578
     * comment.getCommentId()=220942086102077477
     *
     * comment.getCommentId()=220942086034968579
     */

    @Test
    void readAllInfiniteScroll() {
        List<CommentResponse> response1 = restClient.get()
                .uri("/v1/comments/infinite-scroll?articleId=1&pageSize=5")
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("first page");
        for(CommentResponse comment : response1) {
            if (comment.getCommentId().equals(comment.getParentCommentId())) {
                System.out.println("");
            }
            System.out.println("comment.getCommentId()=" + comment.getCommentId());
        }

        Long lastCommentId = response1.get(response1.size() - 1).getCommentId();
        Long lastParentCommentId = response1.get(response1.size() - 1).getParentCommentId();


        List<CommentResponse> response2 = restClient.get()
                .uri("/v1/comments/infinite-scroll?articleId=1&pageSize=5&lastCommentId=%s&lastParentCommentId=%s"
                        .formatted(lastCommentId, lastParentCommentId))
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("second page");
        for(CommentResponse comment : response2) {
            if (comment.getCommentId().equals(comment.getParentCommentId())) {
                System.out.println("");
            }
            System.out.println("comment.getCommentId()=" + comment.getCommentId());
        }
    }

    CommentResponse createComment(CommentCreateRequest request) {
        return restClient.post()
                .uri("/v1/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
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
