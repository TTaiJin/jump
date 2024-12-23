package com.mysite.jump.comment;

import com.mysite.jump.answer.Answer;
import com.mysite.jump.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne
    private Answer answer;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter = new HashSet<>();

    private Integer recommend;

    // 추천 수 업데이트 메서드
    @PrePersist
    @PreUpdate
    private void updateRecommend() {
        if (this.voter == null) {
            this.voter = new HashSet<>();
        }
        this.recommend = voter.size();
    }
}

