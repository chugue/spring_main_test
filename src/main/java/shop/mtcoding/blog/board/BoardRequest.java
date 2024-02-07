package shop.mtcoding.blog.board;

import lombok.Data;

public class BoardRequest {

    @Data
    public class SaveDTO {
        private String title;
        private String content;
    }
}
