package com.rest.api.entity.board;

import com.rest.api.entity.User;
import com.rest.api.entity.common.CommonDateEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post extends CommonDateEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    @Column(nullable = false, length = 50)
    private String author;
    @Column(nullable = false, length = 100)
    private String title;
    @Column(length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_id")
    private Board board; // 게시글 - 게시판의 관계 - N:1

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "msrl")
    private User user;

    // Join 테이블이 Json결과에 표시되지 않도록 처리.
    protected Board getBoard() {
        return board; // 게시글 - 회원의 관계 - N:1
    }

    // 생성자
    public Post(User user, Board board, String author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.board = board;
        this.user = user;
    }

    // 수정시 데이터 처리
    public Post setUpdate(String author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
        return this;
    }
}
