package com.mysite.jump.question;

import com.mysite.jump.answer.Answer;
import com.mysite.jump.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;

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
